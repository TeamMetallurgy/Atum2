package com.teammetallurgy.atum.blocks.vegetation;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.neoforged.neoforge.common.PlantType;

import javax.annotation.Nonnull;

public class DryGrassBlock extends OasisGrassBlock {

    public DryGrassBlock(Properties properties) {
        super(properties);
    }

    @Override
    @Nonnull
    public PlantType getPlantType(@Nonnull BlockGetter level, @Nonnull BlockPos pos) {
        return PlantType.DESERT;
    }
}