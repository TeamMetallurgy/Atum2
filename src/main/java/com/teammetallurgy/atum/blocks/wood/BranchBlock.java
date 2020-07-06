package com.teammetallurgy.atum.blocks.wood;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class BranchBlock extends Block { //Maybe use SixWayBlock. Look at ChorusPlantBlock
    public static final EnumProperty<Direction> FACING = EnumProperty.create("facing", Direction.class);
    public static final BooleanProperty NORTH = BooleanProperty.create("north");
    public static final BooleanProperty EAST = BooleanProperty.create("east");
    public static final BooleanProperty SOUTH = BooleanProperty.create("south");
    public static final BooleanProperty WEST = BooleanProperty.create("west");
    public static final BooleanProperty UP = BooleanProperty.create("up");
    public static final BooleanProperty DOWN = BooleanProperty.create("down");

    private static final Map<Direction, VoxelShape> bounds;
    private static final Map<Direction, VoxelShape> connectedBounds;

    private static final VoxelShape EAST_AABB = Block.makeCuboidShape(5, 5, 5, 1.0D, 11, 11);
    private static final VoxelShape WEST_AABB = Block.makeCuboidShape(0.0D, 5, 5, 11, 11, 11);
    private static final VoxelShape NORTH_AABB = Block.makeCuboidShape(5, 5, 0.0D, 11, 11, 11);
    private static final VoxelShape SOUTH_AABB = Block.makeCuboidShape(5, 5, 5, 11, 11, 1.0D);
    private static final VoxelShape UP_AABB = Block.makeCuboidShape(5, 5, 5, 11, 1.0D, 11);
    private static final VoxelShape DOWN_AABB = Block.makeCuboidShape(5, 0.0D, 5, 11, 11, 11);

    static {
        bounds = new HashMap<>();
        connectedBounds = new HashMap<>();
        bounds.put(Direction.EAST, EAST_AABB);
        bounds.put(Direction.WEST, WEST_AABB);
        bounds.put(Direction.NORTH, NORTH_AABB);
        bounds.put(Direction.SOUTH, SOUTH_AABB);
        bounds.put(Direction.UP, UP_AABB);
        bounds.put(Direction.DOWN, DOWN_AABB);

        for (Direction facing : Direction.values()) {
            AxisAlignedBB box = bounds.get(facing).getBoundingBox();
            box.expand(5 * facing.getXOffset(), 5 * facing.getYOffset(), 5 * facing.getZOffset());
            VoxelShape expandedBox = Block.makeCuboidShape(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ);
            connectedBounds.put(facing, expandedBox);
        }
    }

    public BranchBlock() {
        super(Properties.create(Material.WOOD).hardnessAndResistance(0.8F, 5.0F).sound(SoundType.WOOD).tickRandomly().harvestTool(ToolType.AXE).harvestLevel(0));
        this.setDefaultState(this.getStateContainer().getBaseState().with(FACING, Direction.NORTH).with(NORTH, false).with(EAST, false).with(SOUTH, false).with(WEST, false).with(UP, false).with(DOWN, false));
    }

    @Override
    public int getOpacity(@Nonnull BlockState state, @Nonnull IBlockReader reader, @Nonnull BlockPos pos) {
        return 1;
    }

    @Override
    public void tick(@Nonnull BlockState state, @Nonnull ServerWorld world, @Nonnull BlockPos pos, @Nonnull Random rand) {
        if (!this.canSurviveAt(world, pos)) {
            world.destroyBlock(pos, true);
        }
    }

    @Override
    public void neighborChanged(@Nonnull BlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull Block block, @Nonnull BlockPos fromPos, boolean isMoving) {
        if (!this.canSurviveAt(world, pos)) {
            world.getPendingBlockTicks().scheduleTick(pos, this, 1);
        }
    }

    private boolean canSurviveAt(World world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        Direction facing = state.get(FACING);
        BlockState neighbor = world.getBlockState(pos.add(facing.getDirectionVec()));
        return neighbor.getMaterial() == Material.WOOD;
    }

    @Override
    @Nonnull
    public VoxelShape getShape(BlockState state, IBlockReader reader, BlockPos pos, @Nonnull ISelectionContext context) {
        Direction facing = state.get(FACING);

        BlockState neighbor = reader.getBlockState(pos.add(facing.getDirectionVec()));
        /*if (neighbor.getBlock() == this) {
            AxisAlignedBB box = connectedBounds.get(facing).getBoundingBox();
            box.expand(5 * facing.getXOffset(), 5 * facing.getYOffset(), 5 * facing.getZOffset());
            return Block.makeCuboidShape(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ);
        } else {*/
            return bounds.get(facing);
        //}
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(@Nonnull BlockItemUseContext context) {
        return this.makeConnections(context.getWorld(), context.getPos(), context.getFace());
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> container) {
        container.add(FACING, NORTH, SOUTH, EAST, WEST, UP, DOWN);
    }

    public BlockState makeConnections(IWorldReader world, BlockPos pos) {
        return makeConnections(world, pos, world.getBlockState(pos).get(FACING).getOpposite());
    }

    public BlockState makeConnections(IWorldReader world, BlockPos pos, Direction direction) {
        return this.getDefaultState().with(FACING, direction.getOpposite())
                .with(NORTH, shouldCheckDirection(world, pos, direction, Direction.NORTH))
                .with(EAST, shouldCheckDirection(world, pos, direction, Direction.EAST))
                .with(SOUTH, shouldCheckDirection(world, pos, direction, Direction.SOUTH))
                .with(WEST, shouldCheckDirection(world, pos, direction, Direction.WEST))
                .with(UP, shouldCheckDirection(world, pos, direction, Direction.UP))
                .with(DOWN, shouldCheckDirection(world, pos, direction, Direction.DOWN));
    }

    private boolean shouldCheckDirection(IWorldReader world, BlockPos pos, Direction direction, Direction directionToCheck) {
        if (direction != directionToCheck) return shouldConnect(world, pos, directionToCheck);
        if (direction != directionToCheck.getOpposite()) return shouldConnect(world, pos, directionToCheck);

        return false;
    }

    public boolean shouldConnect(IWorldReader world, BlockPos pos, Direction direction) {
        BlockState neighborState = world.getBlockState(pos.add(direction.getDirectionVec()));
        if (neighborState.getBlock() == this) {
            return neighborState.get(FACING) == direction.getOpposite();
        }
        return false;
    }

    @Override
    public BlockState rotate(BlockState state, IWorld world, BlockPos pos, Rotation rotation) {
        switch (rotation) {
            case CLOCKWISE_180:
                return state.with(NORTH, state.get(SOUTH)).with(EAST, state.get(WEST)).with(SOUTH, state.get(NORTH)).with(WEST, state.get(EAST));
            case COUNTERCLOCKWISE_90:
                return state.with(NORTH, state.get(EAST)).with(EAST, state.get(SOUTH)).with(SOUTH, state.get(WEST)).with(WEST, state.get(NORTH));
            case CLOCKWISE_90:
                return state.with(NORTH, state.get(WEST)).with(EAST, state.get(NORTH)).with(SOUTH, state.get(EAST)).with(WEST, state.get(SOUTH));
            default:
                return state;
        }
    }

    @Override
    @Nonnull
    public BlockState mirror(@Nonnull BlockState state, Mirror mirror) {
        switch (mirror) {
            case LEFT_RIGHT:
                return state.with(NORTH, state.get(SOUTH)).with(SOUTH, state.get(NORTH));
            case FRONT_BACK:
                return state.with(EAST, state.get(WEST)).with(WEST, state.get(EAST));
            default:
                return super.mirror(state, mirror);
        }
    }
}