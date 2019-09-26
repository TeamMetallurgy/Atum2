package com.teammetallurgy.atum.blocks.vegetation;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.PlantType;

import javax.annotation.Nonnull;

public class BlockDeadGrass extends BlockOasisGrass {

    @Override
    @Nonnull
    public PlantType getPlantType(IBlockReader world, BlockPos pos) {
        return PlantType.Desert;
    }

    @Override
    public void getDrops(@Nonnull NonNullList<ItemStack> drops, IBlockReader world, BlockPos pos, @Nonnull BlockState state, int fortune) {
        if (RANDOM.nextInt(40) != 0) return;

        super.getDrops(drops, world, pos, state, fortune);
    }
}