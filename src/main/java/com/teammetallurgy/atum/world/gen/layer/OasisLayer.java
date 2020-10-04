package com.teammetallurgy.atum.world.gen.layer;

import com.teammetallurgy.atum.init.AtumBiomes;
import com.teammetallurgy.atum.misc.AtumConfig;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.layer.traits.IC1Transformer;

import javax.annotation.Nonnull;

public enum OasisLayer implements IC1Transformer {
    INSTANCE;

    private static final int SAND_PLAINS = WorldGenRegistries.BIOME.getId(WorldGenRegistries.BIOME.getValueForKey(AtumBiomes.SAND_PLAINS));
    private static final int SAND_DUNES = WorldGenRegistries.BIOME.getId(WorldGenRegistries.BIOME.getValueForKey(AtumBiomes.SAND_DUNES));
    private static final int OASIS = WorldGenRegistries.BIOME.getId(WorldGenRegistries.BIOME.getValueForKey(AtumBiomes.OASIS));
    private static final int DEAD_OASIS = WorldGenRegistries.BIOME.getId(WorldGenRegistries.BIOME.getValueForKey(AtumBiomes.DEAD_OASIS));


    @Override
    public int apply(@Nonnull INoiseRandom noiseRandom, int value) {
        if (AtumConfig.BIOME.subBiomeChance.get() > 0 && noiseRandom.random(AtumConfig.BIOME.subBiomeChance.get()) == 0) {
            if (value == SAND_PLAINS || value == SAND_DUNES) {
                if (noiseRandom.random(100) < AtumConfig.BIOME.oasisChance.get()) {
                    value = OASIS;
                } else {
                    value = DEAD_OASIS;
                }
            }
        }
        return value;
    }
}