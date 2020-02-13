package com.teammetallurgy.atum.blocks.vegetation;

import com.teammetallurgy.atum.init.AtumItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BushBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.IShearable;

import javax.annotation.Nonnull;
import java.util.Random;

public class OasisGrassBlock extends BushBlock implements IShearable {
    private static final VoxelShape TALL_GRASS_AABB = Block.makeCuboidShape(0.09999999403953552D, 0.0D, 0.09999999403953552D, 0.8999999761581421D, 0.800000011920929D, 0.8999999761581421D);

    public OasisGrassBlock() {
        super(Properties.create(Material.TALL_PLANTS).sound(SoundType.PLANT));
    }

    @Override
    @Nonnull
    public VoxelShape getRenderShape(BlockState state, @Nonnull IBlockReader reader, @Nonnull BlockPos pos) {
        return TALL_GRASS_AABB;
    }

    @Override
    public boolean isReplaceable(IBlockReader world, @Nonnull BlockPos pos) {
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
    public boolean isShearable(@Nonnull ItemStack stack, IBlockReader world, BlockPos pos) {
        return true;
    }

    @Override
    @Nonnull
    public NonNullList<ItemStack> onSheared(@Nonnull ItemStack stack, IBlockReader world, BlockPos pos, int fortune) {
        return NonNullList.withSize(1, new ItemStack(this));
    }

    @Override
    public void getDrops(@Nonnull NonNullList<ItemStack> drops, IBlockReader world, BlockPos pos, @Nonnull BlockState state, int fortune) {
        Random random = RANDOM;
        if (random.nextInt(8) != 0) return;
        ItemStack seed;
        double chance = random.nextDouble();
        if (chance < 0.45D) {
            seed = new ItemStack(AtumItems.FLAX_SEEDS);
        } else if (chance >= 0.45 && chance <= 0.90F) {
            seed = new ItemStack(AtumItems.EMMER_SEEDS);
        } else {
            seed = new ItemStack(Items.MELON_SEEDS);
        }
        if (!seed.isEmpty()) {
            drops.add(seed);
        }
    }
}