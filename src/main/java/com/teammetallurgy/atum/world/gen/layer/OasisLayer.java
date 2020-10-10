package com.teammetallurgy.atum.world.gen.layer;

import com.teammetallurgy.atum.misc.AtumConfig;
import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.layer.traits.IC1Transformer;

import javax.annotation.Nonnull;

public enum OasisLayer implements IC1Transformer {
    INSTANCE;

    @Override
    public int apply(@Nonnull INoiseRandom noiseRandom, int value) {
        if (AtumConfig.BIOME.subBiomeChance.get() > 0 && noiseRandom.random(AtumConfig.BIOME.subBiomeChance.get()) == 0) {
            if (value == AtumLayerUtil.SAND_PLAINS || value == AtumLayerUtil.SAND_DUNES) {
                if (noiseRandom.random(100) < AtumConfig.BIOME.oasisChance.get()) {
                    value = AtumLayerUtil.OASIS;
                } else {
                    value = AtumLayerUtil.DEAD_OASIS;
                }
            }
        }
        return value;
    }
}