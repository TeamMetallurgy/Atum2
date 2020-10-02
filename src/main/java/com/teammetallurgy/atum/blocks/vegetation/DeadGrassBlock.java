package com.teammetallurgy.atum.blocks.vegetation;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.PlantType;

import javax.annotation.Nonnull;

public class DeadGrassBlock extends OasisGrassBlock {

    @Override
    @Nonnull
    public PlantType getPlantType(IBlockReader world, BlockPos pos) {
        return PlantType.DESERT;
    }
}