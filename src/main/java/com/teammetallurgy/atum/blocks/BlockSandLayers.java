package com.teammetallurgy.atum.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialLogic;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import java.util.Random;

public class BlockSandLayers extends BlockFalling {
    private static final Material SAND_LAYER = new MaterialLogic(MapColor.SAND).setReplaceable();
    public static final PropertyInteger LAYERS = PropertyInteger.create("layers", 1, 8);
    private static final AxisAlignedBB[] SAND_AABB = new AxisAlignedBB[]{new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.0D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.125D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.25D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.375D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.625D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.75D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.875D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D)};

    public BlockSandLayers() {
        super(SAND_LAYER);
        this.setDefaultState(this.blockState.getBaseState().withProperty(LAYERS, 1));
        this.setSoundType(SoundType.SAND);
        this.setLightOpacity(0);
        this.setHardness(0.1F);
    }

    @Override
    @Nonnull
    public AxisAlignedBB getBoundingBox(BlockState state, IBlockAccess source, BlockPos pos) {
        return SAND_AABB[state.getValue(LAYERS)];
    }

    @Override
    public boolean isPassable(IBlockAccess world, BlockPos pos) {
        return world.getBlockState(pos).getValue(LAYERS) < 5;
    }

    @Override
    public boolean isTopSolid(BlockState state) {
        return state.getValue(LAYERS) == 8;
    }

    @Override
    public boolean isSideSolid(BlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos, Direction side) {
        BlockState actualState = this.getActualState(state, world, pos);
        return actualState.getValue(LAYERS) >= 8;
    }

    @Override
    @Nonnull
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, BlockState state, BlockPos pos, Direction face) {
        return face == Direction.DOWN ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(BlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos) {
        int i = state.getValue(LAYERS) - 2;
        i = MathHelper.clamp(i, 0, 8);
        float f = 0.125F;
        AxisAlignedBB axisalignedbb = state.getBoundingBox(world, pos);
        return new AxisAlignedBB(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ, axisalignedbb.maxX, (double) ((float) i * f), axisalignedbb.maxZ);
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
    public boolean canPlaceBlockAt(World world, @Nonnull BlockPos pos) {
        BlockState stateDown = world.getBlockState(pos.down());
        Block block = stateDown.getBlock();

        if (block != Blocks.BARRIER) {
            BlockFaceShape shape = stateDown.getBlockFaceShape(world, pos.down(), Direction.UP);
            return shape == BlockFaceShape.SOLID || block == this && stateDown.getValue(LAYERS) == 8;
        } else {
            return false;
        }
    }

    @Override
    public void neighborChanged(BlockState state, World world, @Nonnull BlockPos pos, Block block, BlockPos fromPos) {
        if (!this.canPlaceBlockAt(world, pos)) {
            world.setBlockToAir(pos);
        } else {
            super.neighborChanged(state, world, pos, block, fromPos);
        }
    }

    @Override
    public void harvestBlock(@Nonnull World world, PlayerEntity player, @Nonnull BlockPos pos, @Nonnull BlockState state, TileEntity te, ItemStack stack) {
        super.harvestBlock(world, player, pos, state, te, stack);
        world.setBlockToAir(pos);
    }

    @Override
    @Nonnull
    public Item getItemDropped(BlockState state, Random rand, int fortune) {
        return Items.AIR;
    }

    @Override
    public int quantityDropped(Random random) {
        return 0;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean shouldSideBeRendered(BlockState blockState, @Nonnull IBlockAccess blockAccess, @Nonnull BlockPos pos, Direction side) {
        if (side == Direction.UP) {
            return true;
        } else {
            BlockState state = blockAccess.getBlockState(pos.offset(side));
            return (state.getBlock() != this || state.getValue(LAYERS) < blockState.getValue(LAYERS)) && super.shouldSideBeRendered(blockState, blockAccess, pos, side);
        }
    }

    @Override
    @Nonnull
    public BlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(LAYERS, (meta & 7) + 1);
    }

    @Override
    public boolean isReplaceable(IBlockAccess world, @Nonnull BlockPos pos) {
        return true;
    }

    @Override
    public int getMetaFromState(BlockState state) {
        return state.getValue(LAYERS) - 1;
    }

    @Override
    public int quantityDropped(BlockState state, int fortune, @Nonnull Random random) {
        return (state.getValue(LAYERS)) + 1;
    }

    @Override
    @Nonnull
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, LAYERS);
    }
}