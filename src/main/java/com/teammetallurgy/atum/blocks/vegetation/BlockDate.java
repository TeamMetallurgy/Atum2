package com.teammetallurgy.atum.blocks.vegetation;

import com.teammetallurgy.atum.init.AtumItems;
import net.minecraft.block.BlockBush;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Random;

public class BlockDate extends BlockBush implements IGrowable {
    public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 7);
    private static final AxisAlignedBB STEM = new AxisAlignedBB(0.4125D, 0.625D, 0.4125D, 0.6D, 1.0D, 0.6D);
    private static final AxisAlignedBB BOUNDING_BOX = new AxisAlignedBB(0.3125D, 0.125D, 0.3125D, 0.6875D, 1.0D, 0.6875D);

    public BlockDate() {
        super(Material.PLANTS);
        this.setHardness(0.35F);
        this.setTickRandomly(true);
        this.setSoundType(SoundType.PLANT);
        this.setDefaultState(this.blockState.getBaseState().withProperty(AGE, 0));
    }

    @Override
    @SuppressWarnings("all")
    public float getBlockHardness(IBlockState state, World world, BlockPos pos) {
        if (state.getValue(AGE) != 3) {
            this.blockHardness = 0.25F;
        }
        return super.getBlockHardness(state, world, pos);
    }

    @Override
    @Nonnull
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        switch (state.getValue(AGE)) {
            case 0:
                return STEM;
            default:
                return BOUNDING_BOX;
        }
    }

    @Override
    public void updateTick(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, Random rand) {
        if (!world.isRemote) {
            super.updateTick(world, pos, state, rand);
            if (!world.isAreaLoaded(pos, 1)) return;;
            if (world.rand.nextDouble() <= 0.08F) {
                grow(world, rand, pos, state);
            }
        }
    }

    @Override
    public boolean canBlockStay(World world, BlockPos pos, IBlockState state) {
        if (state.getBlock() == this) {
            IBlockState stateUp = world.getBlockState(pos.up());
            return stateUp.getBlock().isLeaves(stateUp, world, pos.up());
        }
        return this.canSustainBush(world.getBlockState(pos.down()));
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (state.getValue(AGE) == 7) {
            dropBlockAsItem(world, pos, state, 0);
            return world.setBlockState(pos, this.getDefaultState());
        }
        return false;
    }

    @Override
    public void getDrops(@Nonnull NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, @Nonnull IBlockState state, int fortune) {
        if (state.getValue(AGE) == 7) {
            super.getDrops(drops, world, pos, state, fortune);
        }
    }

    @Override
    @Nonnull
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return AtumItems.DATE;
    }

    @Override
    public int quantityDropped(Random rand) {
        return rand.nextInt(4) + 1;
    }

    @Override
    @Nonnull
    public ItemStack getPickBlock(@Nonnull IBlockState state, RayTraceResult target, @Nonnull World world, @Nonnull BlockPos pos, EntityPlayer player) {
        return new ItemStack(AtumItems.DATE);
    }

    @Override
    @Nonnull
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(AGE, meta);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(AGE);
    }

    @Override
    @Nonnull
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, AGE);
    }

    @Override
    public boolean canGrow(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, boolean isClient) {
        return state.getValue(AGE) != 7;
    }

    @Override
    public boolean canUseBonemeal(@Nonnull World world, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        return false;
    }

    @Override
    public void grow(@Nonnull World world, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        if (state.getValue(AGE) != 7) {
            world.setBlockState(pos, state.cycleProperty(AGE), 2);
        }
    }
}