package com.teammetallurgy.atum.blocks.curio;

import com.teammetallurgy.atum.blocks.curio.tileentity.CurioDisplayTileEntity;
import com.teammetallurgy.atum.misc.StackHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nonnull;

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
    public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter level, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
        return SHAPE;
    }

    @Override
    @Nonnull
    public RenderShape getRenderShape(@Nonnull BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public void attack(@Nonnull BlockState state, Level level, @Nonnull BlockPos pos, @Nonnull Player player) {
        BlockEntity tileEntity = level.getBlockEntity(pos);
        if (tileEntity instanceof CurioDisplayTileEntity displayDisplay) {
            if (!displayDisplay.isEmpty()) {
                StackHelper.dropInventoryItems(level, pos, displayDisplay);
                displayDisplay.setChanged();
            }
        }
        super.attack(state, level, pos, player);
    }

    @Override
    @Nonnull
    public InteractionResult use(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @Nonnull Player player, @Nonnull InteractionHand hand, @Nonnull BlockHitResult rayTraceResult) {
        if (player == null) return InteractionResult.PASS;
        ItemStack heldStack = player.getItemInHand(hand);
        BlockEntity tileEntity = level.getBlockEntity(pos);

        if (tileEntity instanceof CurioDisplayTileEntity curioDisplay) {
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
                StackHelper.dropInventoryItems(level, pos, curioDisplay);
            }
            curioDisplay.setChanged();
            return InteractionResult.PASS;
        }
        return InteractionResult.PASS;
    }

    @Override
    public void onRemove(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @Nonnull BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            if (newState.getBlock() != state.getBlock()) {
                BlockEntity tileEntity = level.getBlockEntity(pos);
                if (tileEntity instanceof CurioDisplayTileEntity) {
                    Containers.dropContents(level, pos, (Container) tileEntity);
                }
                level.removeBlockEntity(pos);
            }
            super.onRemove(state, level, pos, newState, isMoving);
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
    public BlockState updateShape(@Nonnull BlockState state, @Nonnull Direction facing, @Nonnull BlockState facingState, @Nonnull LevelAccessor level, @Nonnull BlockPos currentPos, @Nonnull BlockPos facingPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        return super.updateShape(state, facing, facingState, level, currentPos, facingPos);
    }

    @Override
    public BlockState rotate(BlockState state, LevelAccessor level, BlockPos pos, Rotation rot) {
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