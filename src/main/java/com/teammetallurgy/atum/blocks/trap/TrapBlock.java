package com.teammetallurgy.atum.blocks.trap;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.blocks.trap.tileentity.TrapTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
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
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID)
public abstract class TrapBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING = DirectionalBlock.FACING;
    private static final BooleanProperty DISABLED = BooleanProperty.create("disabled");

    protected TrapBlock() {
        super(Properties.of(Material.STONE, MaterialColor.SAND).strength(1.5F));
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(DISABLED, Boolean.FALSE));
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@Nonnull Level level, @Nonnull BlockState state, @Nonnull BlockEntityType<T> blockEntityType) {
        return level.isClientSide ? null : getTrapTickerHelper(level, state, blockEntityType);
    }

    public abstract <E extends TrapTileEntity> BlockEntityType<E> getTrapBlockEntityType();

    public <E extends BlockEntity> BlockEntityTicker<E> getTrapTickerHelper(@Nonnull Level level, @Nonnull BlockState state, @Nonnull BlockEntityType<E> blockEntityType) {
        return createTickerHelper(blockEntityType, getTrapBlockEntityType(), TrapTileEntity::serverTick);
    }

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        BlockState state = event.getState();
        if (state.getBlock() instanceof TrapBlock) {
            BlockEntity tileEntity = event.getLevel().getBlockEntity(event.getPos());
            if (tileEntity instanceof TrapTileEntity && ((TrapTileEntity) tileEntity).isInsidePyramid && !event.getPlayer().isCreative()) {
                event.setCanceled(true);
            }
        }
    }

    @Override
    public float getExplosionResistance(BlockState state, BlockGetter level, BlockPos pos, Explosion explosion) {
        BlockEntity tileEntity = level.getBlockEntity(pos);
        return tileEntity instanceof TrapTileEntity && ((TrapTileEntity) tileEntity).isInsidePyramid ? 6000000.0F : super.getExplosionResistance(state, level, pos, explosion);
    }

    @Override
    @Nonnull
    public InteractionResult use(@Nonnull BlockState state, Level level, @Nonnull BlockPos pos, @Nonnull Player player, @Nonnull InteractionHand hand, @Nonnull BlockHitResult rayTraceResult) {
        if (level.isClientSide) {
            return InteractionResult.PASS;
        } else {
            BlockEntity tileEntity = level.getBlockEntity(pos);
            boolean isToolEffective = ForgeHooks.isCorrectToolForDrops(level.getBlockState(pos), player);
            if (tileEntity instanceof TrapTileEntity trap) {
                if (!trap.isInsidePyramid) {
                    NetworkHooks.openScreen((ServerPlayer) player, trap, pos);
                    return InteractionResult.SUCCESS;
                }
                if (trap.isInsidePyramid && isToolEffective && !state.getValue(DISABLED)) {
                    this.setDisabled(level, pos, state, (TrapTileEntity) tileEntity, true);
                    level.playSound(null, pos, SoundEvents.DISPENSER_FAIL, SoundSource.BLOCKS, 1.1F, 1.5F);
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return super.use(state, level, pos, player, hand, rayTraceResult);
    }

    @Override
    public void neighborChanged(@Nonnull BlockState state, Level level, @Nonnull BlockPos pos, @Nonnull Block block, @Nonnull BlockPos fromPos, boolean isMoving) {
        if (!level.isClientSide) {
            BlockEntity tileEntity = level.getBlockEntity(pos);
            if (tileEntity instanceof TrapTileEntity && !((TrapTileEntity) tileEntity).isInsidePyramid) {
                if (level.hasNeighborSignal(pos)) {
                    this.setDisabled(level, pos, state, (TrapTileEntity) tileEntity, true);
                } else if (!level.hasNeighborSignal(pos)) {
                    level.scheduleTick(pos, this, 4);
                }
            }
        }
    }

    @Override
    public void tick(@Nonnull BlockState state, ServerLevel level, @Nonnull BlockPos pos, @Nonnull RandomSource rand) {
        if (!level.isClientSide) {
            BlockEntity tileEntity = level.getBlockEntity(pos);
            if (tileEntity instanceof TrapTileEntity && !((TrapTileEntity) tileEntity).isInsidePyramid) {
                if (!level.hasNeighborSignal(pos)) {
                    this.setDisabled(level, pos, state, (TrapTileEntity) tileEntity, false);
                }
            }
        }
    }

    private void setDisabled(Level level, BlockPos pos, BlockState state, TrapTileEntity trap, boolean disabledStatus) {
        trap.setDisabledStatus(disabledStatus);
        level.setBlockAndUpdate(pos, state.setValue(DISABLED, disabledStatus));
        level.sendBlockUpdated(pos, state, state, 3);
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
    public void setPlacedBy(Level level, @Nonnull BlockPos pos, @Nonnull BlockState state, LivingEntity placer, @Nonnull ItemStack stack) {
        BlockEntity tileentity = level.getBlockEntity(pos);
        if (tileentity instanceof TrapTileEntity) {
            ((TrapTileEntity) tileentity).isInsidePyramid = false;
            if (stack.hasCustomHoverName()) {
                ((TrapTileEntity) tileentity).setCustomName(stack.getHoverName());
            }
        }
    }

    @Override
    public void onRemove(@Nonnull BlockState state, Level level, @Nonnull BlockPos pos, @Nonnull BlockState newState, boolean isMoving) {
        if (newState.getBlock() != state.getBlock()) {
            BlockEntity tileEntity = level.getBlockEntity(pos);
            if (tileEntity instanceof TrapTileEntity) {
                Containers.dropContents(level, pos, (Container) tileEntity);
                level.updateNeighbourForOutputSignal(pos, this);
            }
            level.removeBlockEntity(pos);
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
    public int getAnalogOutputSignal(@Nonnull BlockState blockState, Level level, @Nonnull BlockPos pos) {
        return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(level.getBlockEntity(pos));
    }

    @Override
    public BlockState rotate(BlockState state, LevelAccessor level, BlockPos pos, Rotation rotation) {
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