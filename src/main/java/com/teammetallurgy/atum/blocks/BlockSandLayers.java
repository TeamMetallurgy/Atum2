package com.teammetallurgy.atum.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.Random;

public class BlockSandLayers extends BlockFalling {
    public static final PropertyInteger LAYERS = PropertyInteger.create("layers", 1, 8);
    private static final AxisAlignedBB[] SAND_AABB = new AxisAlignedBB[]{new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.0D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.125D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.25D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.375D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.625D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.75D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.875D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D)};

    public BlockSandLayers() {
        super(Material.SAND);
        this.setDefaultState(this.blockState.getBaseState().withProperty(LAYERS, 1));
        this.setTickRandomly(true);
        this.setSoundType(SoundType.SAND);
        this.setLightOpacity(0);
        this.setHardness(0.5F);
    }

    @Override
    @Nonnull
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return SAND_AABB[state.getValue(LAYERS)];
    }

    @Override
    public boolean isPassable(IBlockAccess world, BlockPos pos) {
        return world.getBlockState(pos).getValue(LAYERS) < 5;
    }

    @Override
    public boolean isTopSolid(IBlockState state) {
        return state.getValue(LAYERS) == 8;
    }

    @Override
    public boolean isSideSolid(IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos, EnumFacing side) {
        IBlockState actualState = this.getActualState(state, world, pos);
        return actualState.getValue(LAYERS) >= 8;
    }

    @Override
    @Nonnull
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return face == EnumFacing.DOWN ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos) {
        int i = state.getValue(LAYERS) - 1;
        float f = 0.125F;
        AxisAlignedBB axisalignedbb = state.getBoundingBox(world, pos);
        return new AxisAlignedBB(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ, axisalignedbb.maxX, (double) ((float) i * f), axisalignedbb.maxZ);
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
    public boolean canPlaceBlockAt(World world, @Nonnull BlockPos pos) {
        IBlockState stateDown = world.getBlockState(pos.down());
        Block block = stateDown.getBlock();

        if (block != Blocks.BARRIER) {
            BlockFaceShape shape = stateDown.getBlockFaceShape(world, pos.down(), EnumFacing.UP);
            return shape == BlockFaceShape.SOLID || stateDown.getBlock().isLeaves(stateDown, world, pos.down()) || block == this && stateDown.getValue(LAYERS) == 8;
        } else {
            return false;
        }
    }

    @Override
    public void neighborChanged(IBlockState state, World world, @Nonnull BlockPos pos, Block block, BlockPos fromPos) {
        if (!this.canPlaceBlockAt(world, pos)) {
            world.setBlockToAir(pos);
        }
        super.neighborChanged(state, world, pos, block, fromPos);
    }

    @Override
    public void harvestBlock(@Nonnull World world, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te, ItemStack stack) {
        super.harvestBlock(world, player, pos, state, te, stack);
        world.setBlockToAir(pos);
    }


    @Override
    @Nonnull
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Items.AIR;
    }

    @Override
    public int quantityDropped(Random random) {
        return 0;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, @Nonnull IBlockAccess blockAccess, @Nonnull BlockPos pos, EnumFacing side) {
        if (side == EnumFacing.UP) {
            return true;
        } else {
            IBlockState state = blockAccess.getBlockState(pos.offset(side));
            return (state.getBlock() != this || state.getValue(LAYERS) < blockState.getValue(LAYERS)) && super.shouldSideBeRendered(blockState, blockAccess, pos, side);
        }
    }

    @Override
    @Nonnull
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(LAYERS, (meta & 7) + 1);
    }

    @Override
    public boolean isReplaceable(IBlockAccess world, @Nonnull BlockPos pos) {
        return world.getBlockState(pos).getValue(LAYERS) == 1;
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(LAYERS) - 1;
    }

    @Override
    public int quantityDropped(IBlockState state, int fortune, @Nonnull Random random) {
        return (state.getValue(LAYERS)) + 1;
    }

    @Override
    @Nonnull
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, LAYERS);
    }
}