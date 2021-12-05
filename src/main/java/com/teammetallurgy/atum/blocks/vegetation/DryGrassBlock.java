package com.teammetallurgy.atum.blocks.vegetation;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraftforge.common.PlantType;

import javax.annotation.Nonnull;

public class DryGrassBlock extends OasisGrassBlock {

    @Override
    @Nonnull
    public PlantType getPlantType(BlockGetter world, BlockPos pos) {
        return PlantType.DESERT;
    }
}