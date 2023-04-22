package com.teammetallurgy.atum.blocks.wood;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class DeadwoodBranchBlock extends Block implements SimpleWaterloggedBlock {
    //States
    public static final EnumProperty<Direction> FACING = EnumProperty.create("facing", Direction.class);
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final BooleanProperty NORTH = BooleanProperty.create("north");
    public static final BooleanProperty EAST = BooleanProperty.create("east");
    public static final BooleanProperty SOUTH = BooleanProperty.create("south");
    public static final BooleanProperty WEST = BooleanProperty.create("west");
    public static final BooleanProperty UP = BooleanProperty.create("up");
    public static final BooleanProperty DOWN = BooleanProperty.create("down");
    //Bounds
    private static final Map<Direction, VoxelShape> BOUNDS;
    private static final Map<Direction, VoxelShape> CONNECTED_BOUNDS;
    //Bounding box
    private static final VoxelShape EAST_AABB = Shapes.box(5 / 16D, 5 / 16D, 5 / 16D, 1.0D, 11 / 16D, 11 / 16D);
    private static final VoxelShape WEST_AABB = Shapes.box(0.0D, 5 / 16D, 5 / 16D, 11 / 16D, 11 / 16D, 11 / 16D);
    private static final VoxelShape NORTH_AABB = Shapes.box(5 / 16D, 5 / 16D, 0.0D, 11 / 16D, 11 / 16D, 11 / 16D);
    private static final VoxelShape SOUTH_AABB = Shapes.box(5 / 16D, 5 / 16D, 5 / 16D, 11 / 16D, 11 / 16D, 1.0D);
    private static final VoxelShape UP_AABB = Shapes.box(5 / 16D, 5 / 16D, 5 / 16D, 11 / 16D, 1.0D, 11 / 16D);
    private static final VoxelShape DOWN_AABB = Shapes.box(5 / 16D, 0.0D, 5 / 16D, 11 / 16D, 11 / 16D, 11 / 16D);

    static {
        BOUNDS = new HashMap<>();
        CONNECTED_BOUNDS = new HashMap<>();
        BOUNDS.put(Direction.EAST, EAST_AABB);
        BOUNDS.put(Direction.WEST, WEST_AABB);
        BOUNDS.put(Direction.NORTH, NORTH_AABB);
        BOUNDS.put(Direction.SOUTH, SOUTH_AABB);
        BOUNDS.put(Direction.UP, UP_AABB);
        BOUNDS.put(Direction.DOWN, DOWN_AABB);

        for (Direction facing : Direction.values()) {
            AABB box = BOUNDS.get(facing).bounds();
            AABB expandedBox = new AABB(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ);
            expandedBox.expandTowards(5 * facing.getStepX(), 5 * facing.getStepY(), 5 * facing.getStepZ());
            CONNECTED_BOUNDS.put(facing, Shapes.create(expandedBox));
        }
    }

    public DeadwoodBranchBlock() {
        super(Properties.of(Material.WOOD).strength(0.8F, 5.0F).sound(SoundType.WOOD).randomTicks().noOcclusion());
        this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(NORTH, false).setValue(EAST, false).setValue(SOUTH, false).setValue(WEST, false).setValue(UP, false).setValue(DOWN, false).setValue(WATERLOGGED, false));
    }

    @Override
    public int getLightBlock(@Nonnull BlockState state, @Nonnull BlockGetter reader, @Nonnull BlockPos pos) {
        return 1;
    }

    @Override
    public boolean isValidSpawn(BlockState state, BlockGetter level, BlockPos pos, SpawnPlacements.Type type, EntityType<?> entityType) {
        return false;
    }

    @Override
    public void tick(@Nonnull BlockState state, @Nonnull ServerLevel level, @Nonnull BlockPos pos, @Nonnull RandomSource rand) {
        if (!this.canSurviveAt(level, pos)) {
            level.destroyBlock(pos, true);
        }
    }

    @Override
    public void neighborChanged(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @Nonnull Block block, @Nonnull BlockPos fromPos, boolean isMoving) {
        if (!this.canSurviveAt(level, pos)) {
            level.scheduleTick(pos, this, 1);
        }
    }

    private boolean canSurviveAt(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        Direction facing = state.getValue(FACING);
        BlockState neighbor = level.getBlockState(pos.offset(facing.getNormal()));
        return neighbor.getMaterial() == Material.WOOD;
    }

    @Override
    @Nonnull
    public VoxelShape getShape(BlockState state, @Nonnull BlockGetter reader, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
        Direction facing = state.getValue(FACING);
        if (reader instanceof  Level) {
            BlockState neighbor = reader.getBlockState(pos.offset(facing.getNormal()));
            if (neighbor.getBlock() == this) {
                AABB box = CONNECTED_BOUNDS.get(facing).bounds();
                AABB expandedBox = new AABB(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ);
                return Shapes.create(expandedBox.expandTowards(5 / 16D * facing.getStepX(), 5 / 16D * facing.getStepY(), 5 / 16D * facing.getStepZ()));
            }
        }
        return BOUNDS.get(facing);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(@Nonnull BlockPlaceContext context) {
        FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
        return this.makeConnections(context.getLevel(), context.getClickedPos(), context.getClickedFace()).setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
    }

    @Override
    @Nonnull
    public BlockState updateShape(BlockState state, @Nonnull Direction direction, @Nonnull BlockState facingState, @Nonnull LevelAccessor level, @Nonnull BlockPos currentPos, @Nonnull BlockPos facingPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        return super.updateShape(state, direction, facingState, level, currentPos, facingPos);
    }

    @Override
    @Nonnull
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> container) {
        container.add(FACING, NORTH, SOUTH, EAST, WEST, UP, DOWN, WATERLOGGED);
    }

    public BlockState makeConnections(LevelReader level, BlockPos pos) {
        return makeConnections(level, pos, level.getBlockState(pos).getValue(FACING).getOpposite());
    }

    public BlockState makeConnections(LevelReader level, BlockPos pos, Direction direction) {
        return this.defaultBlockState().setValue(FACING, direction.getOpposite())
                .setValue(NORTH, shouldCheckDirection(level, pos, direction, Direction.NORTH))
                .setValue(EAST, shouldCheckDirection(level, pos, direction, Direction.EAST))
                .setValue(SOUTH, shouldCheckDirection(level, pos, direction, Direction.SOUTH))
                .setValue(WEST, shouldCheckDirection(level, pos, direction, Direction.WEST))
                .setValue(UP, shouldCheckDirection(level, pos, direction, Direction.UP))
                .setValue(DOWN, shouldCheckDirection(level, pos, direction, Direction.DOWN));
    }

    private boolean shouldCheckDirection(LevelReader level, BlockPos pos, Direction direction, Direction directionToCheck) {
        if (direction != directionToCheck) return shouldConnect(level, pos, directionToCheck);
        if (direction != directionToCheck.getOpposite()) return shouldConnect(level, pos, directionToCheck);

        return false;
    }

    public boolean shouldConnect(LevelReader level, BlockPos pos, Direction direction) {
        BlockState neighborState = level.getBlockState(pos.offset(direction.getNormal()));
        if (neighborState.getBlock() == this) {
            return neighborState.getValue(FACING) == direction.getOpposite();
        }
        return false;
    }

    @Override
    public BlockState rotate(BlockState state, LevelAccessor level, BlockPos pos, Rotation rotation) {
        switch (rotation) {
            case CLOCKWISE_180:
                return state.setValue(NORTH, state.getValue(SOUTH)).setValue(EAST, state.getValue(WEST)).setValue(SOUTH, state.getValue(NORTH)).setValue(WEST, state.getValue(EAST));
            case COUNTERCLOCKWISE_90:
                return state.setValue(NORTH, state.getValue(EAST)).setValue(EAST, state.getValue(SOUTH)).setValue(SOUTH, state.getValue(WEST)).setValue(WEST, state.getValue(NORTH));
            case CLOCKWISE_90:
                return state.setValue(NORTH, state.getValue(WEST)).setValue(EAST, state.getValue(NORTH)).setValue(SOUTH, state.getValue(EAST)).setValue(WEST, state.getValue(SOUTH));
            default:
                return state;
        }
    }

    @Override
    @Nonnull
    public BlockState mirror(@Nonnull BlockState state, Mirror mirror) {
        switch (mirror) {
            case LEFT_RIGHT:
                return state.setValue(NORTH, state.getValue(SOUTH)).setValue(SOUTH, state.getValue(NORTH));
            case FRONT_BACK:
                return state.setValue(EAST, state.getValue(WEST)).setValue(WEST, state.getValue(EAST));
            default:
                return super.mirror(state, mirror);
        }
    }
}