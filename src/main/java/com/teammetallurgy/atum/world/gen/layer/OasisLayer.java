package com.teammetallurgy.atum.world.gen.layer;

import com.teammetallurgy.atum.misc.AtumConfig;
import net.minecraft.world.level.newbiome.context.Context;
import net.minecraft.world.level.newbiome.layer.traits.C1Transformer;

import javax.annotation.Nonnull;

public enum OasisLayer implements C1Transformer {
    INSTANCE;

    @Override
    public int apply(@Nonnull Context noiseRandom, int value) {
        if (AtumConfig.BIOME.subBiomeChance.get() > 0 && noiseRandom.nextRandom(AtumConfig.BIOME.subBiomeChance.get()) == 0) {
            if (value == AtumLayerUtil.SAND_PLAINS) {
                if (noiseRandom.nextRandom(100) < AtumConfig.BIOME.oasisChance.get()) {
                    value = AtumLayerUtil.OASIS;
                } else {
                    value = AtumLayerUtil.DEAD_OASIS;
                }
            }
        }
        return value;
    }
}