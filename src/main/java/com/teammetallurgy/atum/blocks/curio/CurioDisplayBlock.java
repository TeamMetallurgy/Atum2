package com.teammetallurgy.atum.blocks.curio;

import com.teammetallurgy.atum.blocks.curio.tileentity.CurioDisplayTileEntity;
import com.teammetallurgy.atum.misc.StackHelper;
import net.minecraft.block.*;
import net.minecraft.world.level.material.Material;
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

import javax.annotation.Nonnull;

import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public abstract class CurioDisplayBlock extends BaseEntityBlock {
    private static final VoxelShape SHAPE = box(3.0D, 0.0D, 3.0D, 13.0D, 16.0D, 13.0D);
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public CurioDisplayBlock(Material material) {
        super(BlockBehaviour.Properties.of(material).strength(1.5F, 1.0F).sound(SoundType.GLASS));
        this.registerDefaultState(this.stateDefinition.any().setValue(WATERLOGGED, false));
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(WATERLOGGED, false));
    }

    @Override
    @Nonnull
    public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter world, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
        return SHAPE;
    }

    @Override
    @Nonnull
    public RenderShape getRenderShape(@Nonnull BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public void attack(@Nonnull BlockState state, Level world, @Nonnull BlockPos pos, @Nonnull Player player) {
        BlockEntity tileEntity = world.getBlockEntity(pos);
        if (tileEntity instanceof CurioDisplayTileEntity) {
            CurioDisplayTileEntity displayDisplay = (CurioDisplayTileEntity) tileEntity;
            if (!displayDisplay.isEmpty()) {
                StackHelper.dropInventoryItems(world, pos, displayDisplay);
                displayDisplay.setChanged();
            }
        }
        super.attack(state, world, pos, player);
    }

    @Override
    @Nonnull
    public InteractionResult use(@Nonnull BlockState state, @Nonnull Level world, @Nonnull BlockPos pos, @Nonnull Player player, @Nonnull InteractionHand hand, @Nonnull BlockHitResult rayTraceResult) {
        if (player == null) return InteractionResult.PASS;
        ItemStack heldStack = player.getItemInHand(hand);
        BlockEntity tileEntity = world.getBlockEntity(pos);

        if (tileEntity instanceof CurioDisplayTileEntity) {
            CurioDisplayTileEntity curioDisplay = (CurioDisplayTileEntity) tileEntity;
            ItemStack slotStack = curioDisplay.getItem(0);
            if (slotStack.isEmpty() && curioDisplay.canPlaceItem(0, heldStack)) {
                ItemStack copyStack = heldStack.copy();
                copyStack.setCount(1);
                if (!heldStack.isEmpty()) {
                    curioDisplay.setItem(0, copyStack);

                    if (!player.isCreative()) {
                        heldStack.shrink(1);
                    }
                    return InteractionResult.SUCCESS;
                }
            } else if (player.isCrouching()) {
                StackHelper.dropInventoryItems(world, pos, curioDisplay);
            }
            curioDisplay.setChanged();
            return InteractionResult.PASS;
        }
        return InteractionResult.PASS;
    }

    @Override
    public void onRemove(@Nonnull BlockState state, @Nonnull Level world, @Nonnull BlockPos pos, @Nonnull BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            if (newState.getBlock() != state.getBlock()) {
                BlockEntity tileEntity = world.getBlockEntity(pos);
                if (tileEntity instanceof CurioDisplayTileEntity) {
                    Containers.dropContents(world, pos, (Container) tileEntity);
                }
                world.removeBlockEntity(pos);
            }
            super.onRemove(state, world, pos, newState, isMoving);
        }
    }

    @Override
    @Nonnull
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
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
}