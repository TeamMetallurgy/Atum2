package com.teammetallurgy.atum.blocks.machines;

import com.teammetallurgy.atum.blocks.machines.tileentity.QuernTileEntity;
import com.teammetallurgy.atum.misc.StackHelper;
import net.minecraft.block.*;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.util.*;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.FakePlayer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;

public class QuernBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    private static final VoxelShape SHAPE = box(0.12D * 16, 0.0D, 0.12D * 16, 0.88D * 16, 0.38D * 16, 0.88D * 16);

    public QuernBlock() {
        super(Properties.of(Material.STONE, MaterialColor.SAND).strength(1.5F));
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(WATERLOGGED, false));
    }

    @Override
    @Nonnull
    public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter reader, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
        return SHAPE;
    }

    @Override
    @Nullable
    public BlockEntity newBlockEntity(@Nonnull BlockGetter reader) {
        return new QuernTileEntity();
    }

    @Override
    public void attack(@Nonnull BlockState state, Level world, @Nonnull BlockPos pos, @Nonnull Player player) {
        BlockEntity tileEntity = world.getBlockEntity(pos);
        if (tileEntity instanceof QuernTileEntity) {
            QuernTileEntity quern = (QuernTileEntity) tileEntity;
            if (!quern.isEmpty()) {
                if (player.isCrouching()) {
                    StackHelper.dropInventoryItems(world, pos, quern);
                } else {
                    ItemStack slotStack = quern.getItem(0);
                    ItemStack copyStack = new ItemStack(slotStack.getItem());
                    StackHelper.giveItem(player, InteractionHand.MAIN_HAND, copyStack);
                    quern.removeItem(0, 1);
                }
                quern.setChanged();
            }
        }
        super.attack(state, world, pos, player);
    }

    @Override
    @Nonnull
    public InteractionResult use(@Nonnull BlockState state, @Nonnull Level world, @Nonnull BlockPos pos, @Nonnull Player player, @Nonnull InteractionHand hand, @Nonnull BlockHitResult rayTraceResult) {
        if (player == null || player instanceof FakePlayer) return InteractionResult.PASS;
        ItemStack heldStack = player.getItemInHand(hand);
        BlockEntity tileEntity = world.getBlockEntity(pos);

        if (tileEntity instanceof QuernTileEntity) {
            QuernTileEntity quern = (QuernTileEntity) tileEntity;
            ItemStack slotStack = quern.getItem(0);
            int size = slotStack.getCount();
            if (size < slotStack.getMaxStackSize() && quern.canPlaceItem(0, heldStack) && (quern.isEmpty() || StackHelper.areStacksEqualIgnoreSize(heldStack, slotStack))) {
                ItemStack copyStack = new ItemStack(heldStack.getItem(), size + 1);
                if (!heldStack.isEmpty()) {
                    quern.setItem(0, copyStack);
                }
                if (!player.isCreative()) {
                    heldStack.shrink(1);
                }
            } else {
                if (world.isClientSide) {
                    world.playLocalSound((double) pos.getX() + 0.5D, pos.getY(), (double) pos.getZ() + 0.5D, SoundEvents.STONE_BREAK, SoundSource.BLOCKS, 1.1F, 0.4F, true);
                } else {
                    quern.setRotations(quern.getRotations() + 24);
                }
            }
            quern.setChanged();
            return InteractionResult.sidedSuccess(world.isClientSide());
        }
        return InteractionResult.PASS;
    }

    @Override
    public void onRemove(@Nonnull BlockState state, @Nonnull Level world, @Nonnull BlockPos pos, @Nonnull BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            if (newState.getBlock() != state.getBlock()) {
                BlockEntity tileEntity = world.getBlockEntity(pos);
                if (tileEntity instanceof QuernTileEntity) {
                    Containers.dropContents(world, pos, (Container) tileEntity);
                }
                world.removeBlockEntity(pos);
            }
            super.onRemove(state, world, pos, newState, isMoving);
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection()).setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
    }

    @Override
    @Nonnull
    public BlockState updateShape(@Nonnull BlockState state, @Nonnull Direction facing, @Nonnull BlockState facingState, @Nonnull LevelAccessor world, @Nonnull BlockPos currentPos, @Nonnull BlockPos facingPos) {
        if (state.getValue(WATERLOGGED)) {
            world.getLiquidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
        }
        return super.updateShape(state, facing, facingState, world, currentPos, facingPos);
    }

    @Override
    @Nonnull
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public BlockState rotate(BlockState state, LevelAccessor world, BlockPos pos, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    @Nonnull
    public BlockState mirror(@Nonnull BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> container) {
        container.add(FACING, WATERLOGGED);
    }

    @Override
    @Nonnull
    public RenderShape getRenderShape(@Nonnull BlockState state) {
        return RenderShape.MODEL;
    }
}