package com.teammetallurgy.atum.blocks.wood;

import com.teammetallurgy.atum.blocks.wood.BlockAtumPlank.WoodType;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class BlockBranch extends Block {
    public static final PropertyEnum<EnumFacing> FACING = PropertyEnum.create("facing", EnumFacing.class);
    private static final PropertyBool NORTH = PropertyBool.create("north");
    private static final PropertyBool EAST = PropertyBool.create("east");
    private static final PropertyBool SOUTH = PropertyBool.create("south");
    private static final PropertyBool WEST = PropertyBool.create("west");
    private static final PropertyBool UP = PropertyBool.create("up");
    private static final PropertyBool DOWN = PropertyBool.create("down");

    private static final Map<EnumFacing, AxisAlignedBB> bounds;
    private static final Map<EnumFacing, AxisAlignedBB> connectedBounds;

    private static final AxisAlignedBB EAST_AABB = new AxisAlignedBB(5 / 16D, 5 / 16D, 5 / 16D, 1.0D, 11 / 16D, 11 / 16D);
    private static final AxisAlignedBB WEST_AABB = new AxisAlignedBB(0.0D, 5 / 16D, 5 / 16D, 11 / 16D, 11 / 16D, 11 / 16D);
    private static final AxisAlignedBB NORTH_AABB = new AxisAlignedBB(5 / 16D, 5 / 16D, 0.0D, 11 / 16D, 11 / 16D, 11 / 16D);
    private static final AxisAlignedBB SOUTH_AABB = new AxisAlignedBB(5 / 16D, 5 / 16D, 5 / 16D, 11 / 16D, 11 / 16D, 1.0D);
    private static final AxisAlignedBB UP_AABB = new AxisAlignedBB(5 / 16D, 5 / 16D, 5 / 16D, 11 / 16D, 1.0D, 11 / 16D);
    private static final AxisAlignedBB DOWN_AABB = new AxisAlignedBB(5 / 16D, 0.0D, 5 / 16D, 11 / 16D, 11 / 16D, 11 / 16D);

    static {
        bounds = new HashMap<>();
        connectedBounds = new HashMap<>();
        bounds.put(EnumFacing.EAST, EAST_AABB);
        bounds.put(EnumFacing.WEST, WEST_AABB);
        bounds.put(EnumFacing.NORTH, NORTH_AABB);
        bounds.put(EnumFacing.SOUTH, SOUTH_AABB);
        bounds.put(EnumFacing.UP, UP_AABB);
        bounds.put(EnumFacing.DOWN, DOWN_AABB);

        for (EnumFacing facing : EnumFacing.VALUES) {
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
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (!this.canSurviveAt(worldIn, pos)) {
            worldIn.destroyBlock(pos, true);
        }
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (!this.canSurviveAt(worldIn, pos)) {
            worldIn.scheduleUpdate(pos, this, 1);
        }
    }

    private boolean canSurviveAt(World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        EnumFacing facing = state.getValue(FACING);
        IBlockState neighbor = world.getBlockState(pos.add(facing.getDirectionVec()));

        return neighbor.getMaterial() == Material.WOOD;
    }

    @Override
    @Nonnull
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return BlockAtumPlank.getStick(WoodType.DEADWOOD);
    }

    @Override
    public int quantityDropped(Random random) {
        return random.nextDouble() <= 0.15F ? 1 : 0;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState state, @Nonnull IBlockAccess blockAccess, @Nonnull BlockPos pos, EnumFacing side) {
        return true;
    }

    @Override
    @Nonnull
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        EnumFacing facing = state.getValue(FACING);

        IBlockState neighbor = source.getBlockState(pos.add(facing.getDirectionVec()));
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
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, LivingEntity placer) {
        return super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty(FACING, facing.getOpposite());
    }

    @Override
    @Nonnull
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(FACING, EnumFacing.UP.VALUES[meta]);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        EnumFacing enumFacing = state.getValue(FACING);
        return enumFacing.ordinal();
    }

    @Override
    @Nonnull
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, NORTH, SOUTH, EAST, WEST, UP, DOWN);
    }

    @Override
    @Nonnull
    public IBlockState getActualState(@Nonnull IBlockState state, IBlockAccess world, BlockPos pos) {
        EnumFacing enumFacing = state.getValue(FACING);
        return state.withProperty(NORTH, enumFacing != EnumFacing.NORTH && shouldConnect(EnumFacing.NORTH, world, pos))
                .withProperty(EAST, enumFacing != EnumFacing.EAST && shouldConnect(EnumFacing.EAST, world, pos))
                .withProperty(SOUTH, enumFacing != EnumFacing.SOUTH && shouldConnect(EnumFacing.SOUTH, world, pos))
                .withProperty(WEST, enumFacing != EnumFacing.WEST && shouldConnect(EnumFacing.WEST, world, pos))
                .withProperty(UP, enumFacing != EnumFacing.UP && shouldConnect(EnumFacing.UP, world, pos))
                .withProperty(DOWN, enumFacing != EnumFacing.DOWN && shouldConnect(EnumFacing.DOWN, world, pos));
    }

    private boolean shouldConnect(EnumFacing direction, IBlockAccess worldIn, BlockPos pos) {
        IBlockState neighborState = worldIn.getBlockState(pos.add(direction.getDirectionVec()));
        if (neighborState.getBlock() == this) {
            return neighborState.getValue(FACING) == direction.getOpposite();
        }
        return false;
    }

    @Override
    @Nonnull
    public IBlockState withRotation(@Nonnull IBlockState state, Rotation rot) {
        switch (rot) {
            case CLOCKWISE_180:
                return state.withProperty(NORTH, state.getValue(SOUTH)).withProperty(EAST, state.getValue(WEST)).withProperty(SOUTH, state.getValue(NORTH)).withProperty(WEST, state.getValue(EAST));
            case COUNTERCLOCKWISE_90:
                return state.withProperty(NORTH, state.getValue(EAST)).withProperty(EAST, state.getValue(SOUTH)).withProperty(SOUTH, state.getValue(WEST)).withProperty(WEST, state.getValue(NORTH));
            case CLOCKWISE_90:
                return state.withProperty(NORTH, state.getValue(WEST)).withProperty(EAST, state.getValue(NORTH)).withProperty(SOUTH, state.getValue(EAST)).withProperty(WEST, state.getValue(SOUTH));
            default:
                return state;
        }
    }

    @Override
    @Nonnull
    public IBlockState withMirror(@Nonnull IBlockState state, Mirror mirror) {
        switch (mirror) {
            case LEFT_RIGHT:
                return state.withProperty(NORTH, state.getValue(SOUTH)).withProperty(SOUTH, state.getValue(NORTH));
            case FRONT_BACK:
                return state.withProperty(EAST, state.getValue(WEST)).withProperty(WEST, state.getValue(EAST));
            default:
                return super.withMirror(state, mirror);
        }
    }
}