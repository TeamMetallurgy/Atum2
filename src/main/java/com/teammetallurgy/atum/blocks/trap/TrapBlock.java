package com.teammetallurgy.atum.blocks.trap;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.blocks.trap.tileentity.TrapTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nonnull;
import java.util.Random;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID)
public abstract class TrapBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING = DirectionalBlock.FACING;
    private static final BooleanProperty DISABLED = BooleanProperty.create("disabled");

    protected TrapBlock() {
        super(Properties.of(Material.STONE, MaterialColor.SAND).strength(1.5F));
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(DISABLED, Boolean.FALSE));
    }

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        BlockState state = event.getState();
        if (state.getBlock() instanceof TrapBlock) {
            BlockEntity tileEntity = event.getWorld().getBlockEntity(event.getPos());
            if (tileEntity instanceof TrapTileEntity && ((TrapTileEntity) tileEntity).isInsidePyramid && !event.getPlayer().isCreative()) {
                event.setCanceled(true);
            }
        }
    }

    @Override
    public float getExplosionResistance(BlockState state, BlockGetter world, BlockPos pos, Explosion explosion) {
        BlockEntity tileEntity = world.getBlockEntity(pos);
        return tileEntity instanceof TrapTileEntity && ((TrapTileEntity) tileEntity).isInsidePyramid ? 6000000.0F : super.getExplosionResistance(state, world, pos, explosion);
    }

    @Override
    @Nonnull
    public InteractionResult use(@Nonnull BlockState state, Level world, @Nonnull BlockPos pos, @Nonnull Player player, @Nonnull InteractionHand hand, @Nonnull BlockHitResult rayTraceResult) {
        if (world.isClientSide) {
            return InteractionResult.PASS;
        } else {
            BlockEntity tileEntity = world.getBlockEntity(pos);
            boolean isToolEffective = ForgeHooks.isToolEffective(world, pos, player.getItemInHand(InteractionHand.MAIN_HAND)) || ForgeHooks.isToolEffective(world, pos, player.getItemInHand(InteractionHand.OFF_HAND));
            if (tileEntity instanceof TrapTileEntity trap) {
                if (!trap.isInsidePyramid) {
                    NetworkHooks.openGui((ServerPlayer) player, trap, pos);
                    return InteractionResult.SUCCESS;
                }
                if (trap.isInsidePyramid && isToolEffective && !state.getValue(DISABLED)) {
                    this.setDisabled(world, pos, state, (TrapTileEntity) tileEntity, true);
                    world.playSound(null, pos, SoundEvents.DISPENSER_FAIL, SoundSource.BLOCKS, 1.1F, 1.5F);
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return super.use(state, world, pos, player, hand, rayTraceResult);
    }

    @Override
    public void neighborChanged(@Nonnull BlockState state, Level world, @Nonnull BlockPos pos, @Nonnull Block block, @Nonnull BlockPos fromPos, boolean isMoving) {
        if (!world.isClientSide) {
            BlockEntity tileEntity = world.getBlockEntity(pos);
            if (tileEntity instanceof TrapTileEntity && !((TrapTileEntity) tileEntity).isInsidePyramid) {
                if (world.hasNeighborSignal(pos)) {
                    this.setDisabled(world, pos, state, (TrapTileEntity) tileEntity, true);
                } else if (!world.hasNeighborSignal(pos)) {
                    world.scheduleTick(pos, this, 4);
                }
            }
        }
    }

    @Override
    public void tick(@Nonnull BlockState state, ServerLevel world, @Nonnull BlockPos pos, @Nonnull Random rand) {
        if (!world.isClientSide) {
            BlockEntity tileEntity = world.getBlockEntity(pos);
            if (tileEntity instanceof TrapTileEntity && !((TrapTileEntity) tileEntity).isInsidePyramid) {
                if (!world.hasNeighborSignal(pos)) {
                    this.setDisabled(world, pos, state, (TrapTileEntity) tileEntity, false);
                }
            }
        }
    }

    private void setDisabled(Level world, BlockPos pos, BlockState state, TrapTileEntity trap, boolean disabledStatus) {
        trap.setDisabledStatus(disabledStatus);
        world.setBlockAndUpdate(pos, state.setValue(DISABLED, disabledStatus));
        world.sendBlockUpdated(pos, state, state, 3);
    }

    @Override
    @Nonnull
    public RenderShape getRenderShape(@Nonnull BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getNearestLookingDirection().getOpposite());
    }

    @Override
    public void setPlacedBy(Level world, @Nonnull BlockPos pos, @Nonnull BlockState state, LivingEntity placer, @Nonnull ItemStack stack) {
        BlockEntity tileentity = world.getBlockEntity(pos);
        if (tileentity instanceof TrapTileEntity) {
            ((TrapTileEntity) tileentity).isInsidePyramid = false;
            if (stack.hasCustomHoverName()) {
                ((TrapTileEntity) tileentity).setCustomName(stack.getHoverName());
            }
        }
    }

    @Override
    public void onRemove(@Nonnull BlockState state, Level world, @Nonnull BlockPos pos, @Nonnull BlockState newState, boolean isMoving) {
        if (newState.getBlock() != state.getBlock()) {
            BlockEntity tileEntity = world.getBlockEntity(pos);
            if (tileEntity instanceof TrapTileEntity) {
                Containers.dropContents(world, pos, (Container) tileEntity);
                world.updateNeighbourForOutputSignal(pos, this);
            }
            world.removeBlockEntity(pos);
        }
    }

    @Override
    public boolean isSignalSource(@Nonnull BlockState state) {
        return true;
    }

    @Override
    public boolean hasAnalogOutputSignal(@Nonnull BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(@Nonnull BlockState blockState, Level world, @Nonnull BlockPos pos) {
        return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(world.getBlockEntity(pos));
    }

    @Override
    public BlockState rotate(BlockState state, LevelAccessor world, BlockPos pos, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    @Nonnull
    public BlockState mirror(@Nonnull BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> container) {
        container.add(FACING, DISABLED);
    }
}