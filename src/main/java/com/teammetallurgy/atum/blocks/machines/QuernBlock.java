package com.teammetallurgy.atum.blocks.machines;

import com.teammetallurgy.atum.blocks.machines.tileentity.QuernTileEntity;
import com.teammetallurgy.atum.misc.StackHelper;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
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
import net.minecraftforge.common.util.FakePlayer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class QuernBlock extends ContainerBlock implements IWaterLoggable {
    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    private static final VoxelShape SHAPE = makeCuboidShape(0.12D * 16, 0.0D, 0.12D * 16, 0.88D * 16, 0.38D * 16, 0.88D * 16);

    public QuernBlock() {
        super(Properties.create(Material.ROCK, MaterialColor.SAND).hardnessAndResistance(1.5F));
        this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH).with(WATERLOGGED, false));
    }

    @Override
    @Nonnull
    public VoxelShape getShape(@Nonnull BlockState state, @Nonnull IBlockReader reader, @Nonnull BlockPos pos, @Nonnull ISelectionContext context) {
        return SHAPE;
    }

    @Override
    @Nullable
    public TileEntity createNewTileEntity(@Nonnull IBlockReader reader) {
        return new QuernTileEntity();
    }

    @Override
    public void onBlockClicked(@Nonnull BlockState state, World world, @Nonnull BlockPos pos, @Nonnull PlayerEntity player) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof QuernTileEntity) {
            QuernTileEntity quern = (QuernTileEntity) tileEntity;
            if (!quern.isEmpty()) {
                if (player.isCrouching()) {
                    StackHelper.dropInventoryItems(world, pos, quern);
                } else {
                    ItemStack slotStack = quern.getStackInSlot(0);
                    ItemStack copyStack = new ItemStack(slotStack.getItem());
                    StackHelper.giveItem(player, Hand.MAIN_HAND, copyStack);
                    quern.decrStackSize(0, 1);
                }
                quern.markDirty();
            }
        }
        super.onBlockClicked(state, world, pos, player);
    }

    @Override
    @Nonnull
    public ActionResultType onBlockActivated(@Nonnull BlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull PlayerEntity player, @Nonnull Hand hand, @Nonnull BlockRayTraceResult rayTraceResult) {
        if (player == null || player instanceof FakePlayer) return ActionResultType.PASS;
        ItemStack heldStack = player.getHeldItem(hand);
        TileEntity tileEntity = world.getTileEntity(pos);

        if (tileEntity instanceof QuernTileEntity) {
            QuernTileEntity quern = (QuernTileEntity) tileEntity;
            ItemStack slotStack = quern.getStackInSlot(0);
            int size = slotStack.getCount();
            if (size < slotStack.getMaxStackSize() && quern.isItemValidForSlot(0, heldStack) && (quern.isEmpty() || StackHelper.areStacksEqualIgnoreSize(heldStack, slotStack))) {
                ItemStack copyStack = new ItemStack(heldStack.getItem(), size + 1);
                if (!heldStack.isEmpty()) {
                    quern.setInventorySlotContents(0, copyStack);
                }
                if (!player.isCreative()) {
                    heldStack.shrink(1);
                }
            } else {
                if (world.isRemote) {
                    world.playSound((double) pos.getX() + 0.5D, pos.getY(), (double) pos.getZ() + 0.5D, SoundEvents.BLOCK_STONE_BREAK, SoundCategory.BLOCKS, 1.1F, 0.4F, true);
                } else {
                    quern.setRotations(quern.getRotations() + 24);
                }
            }
            quern.markDirty();
            return ActionResultType.func_233537_a_(world.isRemote());
        }
        return ActionResultType.PASS;
    }

    @Override
    public void onReplaced(@Nonnull BlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull BlockState newState, boolean isMoving) {
        if (!state.isIn(newState.getBlock())) {
            if (newState.getBlock() != state.getBlock()) {
                TileEntity tileEntity = world.getTileEntity(pos);
                if (tileEntity instanceof QuernTileEntity) {
                    InventoryHelper.dropInventoryItems(world, pos, (IInventory) tileEntity);
                }
                world.removeTileEntity(pos);
            }
            super.onReplaced(state, world, pos, newState, isMoving);
        }
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
    @Nonnull
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
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

    @Override
    @Nonnull
    public BlockRenderType getRenderType(@Nonnull BlockState state) {
        return BlockRenderType.MODEL;
    }
}