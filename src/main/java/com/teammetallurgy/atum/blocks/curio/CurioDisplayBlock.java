package com.teammetallurgy.atum.blocks.curio;

import com.teammetallurgy.atum.blocks.curio.tileentity.CurioDisplayTileEntity;
import com.teammetallurgy.atum.misc.StackHelper;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public abstract class CurioDisplayBlock extends ContainerBlock {
    private static final VoxelShape SHAPE = makeCuboidShape(3.0D, 0.0D, 3.0D, 13.0D, 16.0D, 13.0D);
    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public CurioDisplayBlock(Material material) {
        super(AbstractBlock.Properties.create(material).hardnessAndResistance(1.5F, 1.0F).sound(SoundType.GLASS));
        this.setDefaultState(this.stateContainer.getBaseState().with(WATERLOGGED, false));
        this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH).with(WATERLOGGED, false));
    }

    @Override
    @Nonnull
    public VoxelShape getShape(@Nonnull BlockState state, @Nonnull IBlockReader world, @Nonnull BlockPos pos, @Nonnull ISelectionContext context) {
        return SHAPE;
    }

    @Override
    @Nonnull
    public BlockRenderType getRenderType(@Nonnull BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public void onBlockClicked(@Nonnull BlockState state, World world, @Nonnull BlockPos pos, @Nonnull PlayerEntity player) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof CurioDisplayTileEntity) {
            CurioDisplayTileEntity displayDisplay = (CurioDisplayTileEntity) tileEntity;
            if (!displayDisplay.isEmpty()) {
                StackHelper.dropInventoryItems(world, pos, displayDisplay);
                displayDisplay.markDirty();
            }
        }
        super.onBlockClicked(state, world, pos, player);
    }

    @Override
    @Nonnull
    public ActionResultType onBlockActivated(@Nonnull BlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull PlayerEntity player, @Nonnull Hand hand, @Nonnull BlockRayTraceResult rayTraceResult) {
        if (player == null) return ActionResultType.PASS;
        ItemStack heldStack = player.getHeldItem(hand);
        TileEntity tileEntity = world.getTileEntity(pos);

        if (tileEntity instanceof CurioDisplayTileEntity) {
            CurioDisplayTileEntity curioDisplay = (CurioDisplayTileEntity) tileEntity;
            ItemStack slotStack = curioDisplay.getStackInSlot(0);
            if (slotStack.isEmpty() && curioDisplay.isItemValidForSlot(0, heldStack)) {
                ItemStack copyStack = new ItemStack(heldStack.getItem());
                if (!heldStack.isEmpty()) {
                    curioDisplay.setInventorySlotContents(0, copyStack);

                    if (!player.isCreative()) {
                        heldStack.shrink(1);
                    }
                    return ActionResultType.SUCCESS;
                }
            } else if (player.isCrouching()) {
                StackHelper.dropInventoryItems(world, pos, curioDisplay);
            }
            curioDisplay.markDirty();
            return ActionResultType.PASS;
        }
        return ActionResultType.PASS;
    }

    @Override
    public void onReplaced(@Nonnull BlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull BlockState newState, boolean isMoving) {
        if (!state.isIn(newState.getBlock())) {
            if (newState.getBlock() != state.getBlock()) {
                TileEntity tileEntity = world.getTileEntity(pos);
                if (tileEntity instanceof CurioDisplayTileEntity) {
                    InventoryHelper.dropInventoryItems(world, pos, (IInventory) tileEntity);
                }
                world.removeTileEntity(pos);
            }
            super.onReplaced(state, world, pos, newState, isMoving);
        }
    }

    @Override
    @Nonnull
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        FluidState fluidstate = context.getWorld().getFluidState(context.getPos());
        return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing()).with(WATERLOGGED, fluidstate.getFluid() == Fluids.WATER);
    }

    @Override
    @Nonnull
    public BlockState updatePostPlacement(@Nonnull BlockState state, @Nonnull Direction facing, @Nonnull BlockState facingState, @Nonnull IWorld world, @Nonnull BlockPos currentPos, @Nonnull BlockPos facingPos) {
        if (state.get(WATERLOGGED)) {
            world.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }
        return super.updatePostPlacement(state, facing, facingState, world, currentPos, facingPos);
    }

    @Override
    public BlockState rotate(BlockState state, IWorld world, BlockPos pos, Rotation rot) {
        return state.with(FACING, rot.rotate(state.get(FACING)));
    }

    @Override
    @Nonnull
    public BlockState mirror(@Nonnull BlockState state, Mirror mirror) {
        return state.rotate(mirror.toRotation(state.get(FACING)));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> container) {
        container.add(FACING, WATERLOGGED);
    }
}