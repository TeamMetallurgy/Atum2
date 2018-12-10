package com.teammetallurgy.atum.blocks.vegetation;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.EnumPlantType;

import javax.annotation.Nonnull;

public class BlockDeadGrass extends BlockOasisGrass {

    @Override
    @Nonnull
    public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {
        return EnumPlantType.Desert;
    }

    @Override
    public void getDrops(@Nonnull NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, @Nonnull IBlockState state, int fortune) {
        if (RANDOM.nextInt(40) != 0) return;

        super.getDrops(drops, world, pos, state, fortune);
    }
}