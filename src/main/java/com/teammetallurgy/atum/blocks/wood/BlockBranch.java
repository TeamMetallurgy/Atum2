package com.teammetallurgy.atum.blocks.wood;

import com.teammetallurgy.atum.init.AtumItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class BlockBranch extends Block {
    public static final PropertyEnum<Direction> FACING = PropertyEnum.create("facing", Direction.class);
    private static final PropertyBool NORTH = PropertyBool.create("north");
    private static final PropertyBool EAST = PropertyBool.create("east");
    private static final PropertyBool SOUTH = PropertyBool.create("south");
    private static final PropertyBool WEST = PropertyBool.create("west");
    private static final PropertyBool UP = PropertyBool.create("up");
    private static final PropertyBool DOWN = PropertyBool.create("down");

    private static final Map<Direction, AxisAlignedBB> bounds;
    private static final Map<Direction, AxisAlignedBB> connectedBounds;

    private static final AxisAlignedBB EAST_AABB = new AxisAlignedBB(5 / 16D, 5 / 16D, 5 / 16D, 1.0D, 11 / 16D, 11 / 16D);
    private static final AxisAlignedBB WEST_AABB = new AxisAlignedBB(0.0D, 5 / 16D, 5 / 16D, 11 / 16D, 11 / 16D, 11 / 16D);
    private static final AxisAlignedBB NORTH_AABB = new AxisAlignedBB(5 / 16D, 5 / 16D, 0.0D, 11 / 16D, 11 / 16D, 11 / 16D);
    private static final AxisAlignedBB SOUTH_AABB = new AxisAlignedBB(5 / 16D, 5 / 16D, 5 / 16D, 11 / 16D, 11 / 16D, 1.0D);
    private static final AxisAlignedBB UP_AABB = new AxisAlignedBB(5 / 16D, 5 / 16D, 5 / 16D, 11 / 16D, 1.0D, 11 / 16D);
    private static final AxisAlignedBB DOWN_AABB = new AxisAlignedBB(5 / 16D, 0.0D, 5 / 16D, 11 / 16D, 11 / 16D, 11 / 16D);

    static {
        bounds = new HashMap<>();
        connectedBounds = new HashMap<>();
        bounds.put(Direction.EAST, EAST_AABB);
        bounds.put(Direction.WEST, WEST_AABB);
        bounds.put(Direction.NORTH, NORTH_AABB);
        bounds.put(Direction.SOUTH, SOUTH_AABB);
        bounds.put(Direction.UP, UP_AABB);
        bounds.put(Direction.DOWN, DOWN_AABB);

        for (Direction facing : Direction.VALUES) {
            AxisAlignedBB box = bounds.get(facing);
            AxisAlignedBB expandedBox = new AxisAlignedBB(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ);
            expandedBox.expand(5 * facing.getXOffset(), 5 * facing.getYOffset(), 5 * facing.getZOffset());
            connectedBounds.put(facing, expandedBox);
        }
    }

    public BlockBranch() {
        super(Material.WOOD);
        this.setHardness(0.8F);
        this.setResistance(5.0F);
        this.setSoundType(SoundType.WOOD);
        this.setHarvestLevel("axe", 0);
        this.setLightOpacity(1);
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, BlockState state, Random rand) {
        if (!this.canSurviveAt(worldIn, pos)) {
            worldIn.destroyBlock(pos, true);
        }
    }

    @Override
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (!this.canSurviveAt(worldIn, pos)) {
            worldIn.getPendingBlockTicks().scheduleTick(pos, this, 1);
        }
    }

    private boolean canSurviveAt(World world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        Direction facing = state.getValue(FACING);
        BlockState neighbor = world.getBlockState(pos.add(facing.getDirectionVec()));

        return neighbor.getMaterial() == Material.WOOD;
    }

    @Override
    @Nonnull
    public Item getItemDropped(BlockState state, Random rand, int fortune) {
        return AtumItems.DEADWOOD_STICK;
    }

    @Override
    public int quantityDropped(Random random) {
        return random.nextDouble() <= 0.15F ? 1 : 0;
    }

    @Override
    public boolean isOpaqueCube(BlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(BlockState state) {
        return false;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean shouldSideBeRendered(BlockState state, @Nonnull IBlockAccess blockAccess, @Nonnull BlockPos pos, Direction side) {
        return true;
    }

    @Override
    @Nonnull
    public AxisAlignedBB getBoundingBox(BlockState state, IBlockAccess source, BlockPos pos) {
        Direction facing = state.getValue(FACING);

        BlockState neighbor = source.getBlockState(pos.add(facing.getDirectionVec()));
        if (neighbor.getBlock() == this) {
            AxisAlignedBB box = connectedBounds.get(facing);
            AxisAlignedBB expandedBox = new AxisAlignedBB(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ);
            return expandedBox.expand(5 / 16D * facing.getXOffset(), 5 / 16D * facing.getYOffset(), 5 / 16D * facing.getZOffset());
        } else {
            return bounds.get(facing);
        }
    }

    @Override
    @Nonnull
    public BlockState getStateForPlacement(World world, BlockPos pos, Direction facing, float hitX, float hitY, float hitZ, int meta, LivingEntity placer) {
        return super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer).with(FACING, facing.getOpposite());
    }

    @Override
    @Nonnull
    public BlockState getStateFromMeta(int meta) {
        return this.getDefaultState().with(FACING, Direction.UP.VALUES[meta]);
    }

    @Override
    public int getMetaFromState(BlockState state) {
        Direction Direction = state.getValue(FACING);
        return Direction.ordinal();
    }

    @Override
    @Nonnull
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, NORTH, SOUTH, EAST, WEST, UP, DOWN);
    }

    @Override
    @Nonnull
    public BlockState getActualState(@Nonnull BlockState state, IBlockAccess world, BlockPos pos) {
        Direction Direction = state.getValue(FACING);
        return state.with(NORTH, Direction != Direction.NORTH && shouldConnect(Direction.NORTH, world, pos))
                .with(EAST, Direction != Direction.EAST && shouldConnect(Direction.EAST, world, pos))
                .with(SOUTH, Direction != Direction.SOUTH && shouldConnect(Direction.SOUTH, world, pos))
                .with(WEST, Direction != Direction.WEST && shouldConnect(Direction.WEST, world, pos))
                .with(UP, Direction != Direction.UP && shouldConnect(Direction.UP, world, pos))
                .with(DOWN, Direction != Direction.DOWN && shouldConnect(Direction.DOWN, world, pos));
    }

    private boolean shouldConnect(Direction direction, IBlockAccess worldIn, BlockPos pos) {
        BlockState neighborState = worldIn.getBlockState(pos.add(direction.getDirectionVec()));
        if (neighborState.getBlock() == this) {
            return neighborState.getValue(FACING) == direction.getOpposite();
        }
        return false;
    }

    @Override
    @Nonnull
    public BlockState withRotation(@Nonnull BlockState state, Rotation rot) {
        switch (rot) {
            case CLOCKWISE_180:
                return state.with(NORTH, state.getValue(SOUTH)).with(EAST, state.getValue(WEST)).with(SOUTH, state.getValue(NORTH)).with(WEST, state.getValue(EAST));
            case COUNTERCLOCKWISE_90:
                return state.with(NORTH, state.getValue(EAST)).with(EAST, state.getValue(SOUTH)).with(SOUTH, state.getValue(WEST)).with(WEST, state.getValue(NORTH));
            case CLOCKWISE_90:
                return state.with(NORTH, state.getValue(WEST)).with(EAST, state.getValue(NORTH)).with(SOUTH, state.getValue(EAST)).with(WEST, state.getValue(SOUTH));
            default:
                return state;
        }
    }

    @Override
    @Nonnull
    public BlockState withMirror(@Nonnull BlockState state, Mirror mirror) {
        switch (mirror) {
            case LEFT_RIGHT:
                return state.with(NORTH, state.getValue(SOUTH)).with(SOUTH, state.getValue(NORTH));
            case FRONT_BACK:
                return state.with(EAST, state.getValue(WEST)).with(WEST, state.getValue(EAST));
            default:
                return super.withMirror(state, mirror);
        }
    }
}