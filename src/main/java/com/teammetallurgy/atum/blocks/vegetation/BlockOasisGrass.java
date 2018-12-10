package com.teammetallurgy.atum.blocks.vegetation;

import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.IShearable;

import javax.annotation.Nonnull;
import java.util.Random;

public class BlockOasisGrass extends BlockBush implements IShearable {
    private static final AxisAlignedBB TALL_GRASS_AABB = new AxisAlignedBB(0.09999999403953552D, 0.0D, 0.09999999403953552D, 0.8999999761581421D, 0.800000011920929D, 0.8999999761581421D);

    public BlockOasisGrass() {
        super(Material.VINE);
        this.setSoundType(SoundType.PLANT);
    }

    @Override
    @Nonnull
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return TALL_GRASS_AABB;
    }

    @Override
    public boolean isReplaceable(IBlockAccess world, @Nonnull BlockPos pos) {
        return true;
    }

    @Override
    public int quantityDroppedWithBonus(int fortune, @Nonnull Random random) {
        return 1 + random.nextInt(fortune * 2 + 1);
    }

    @Override
    @Nonnull
    public Block.EnumOffsetType getOffsetType() {
        return Block.EnumOffsetType.XYZ;
    }

    @Override
    public boolean isShearable(@Nonnull ItemStack stack, IBlockAccess world, BlockPos pos) {
        return true;
    }

    @Override
    @Nonnull
    public NonNullList<ItemStack> onSheared(@Nonnull ItemStack stack, IBlockAccess world, BlockPos pos, int fortune) {
        return NonNullList.withSize(1, new ItemStack(this));
    }

    @Override
    public void getDrops(@Nonnull NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, @Nonnull IBlockState state, int fortune) {
        if (RANDOM.nextInt(8) != 0) return;
        ItemStack seed;
        if (RANDOM.nextDouble() < 0.50D) {
            seed = new ItemStack(AtumItems.FLAX_SEEDS);
        } else {
            seed = new ItemStack(AtumItems.EMMER_SEEDS);
        }
        if (!seed.isEmpty()) {
            drops.add(seed);
        }
    }
}