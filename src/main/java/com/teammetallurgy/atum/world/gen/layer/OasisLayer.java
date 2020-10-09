package com.teammetallurgy.atum.world.gen.layer;

import com.teammetallurgy.atum.init.AtumBiomes;
import com.teammetallurgy.atum.misc.AtumConfig;
import net.minecraft.util.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.layer.traits.IC1Transformer;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistry;

import javax.annotation.Nonnull;

public enum OasisLayer implements IC1Transformer {
    INSTANCE;

    private static final int SAND_PLAINS = getBiomeId(AtumBiomes.SAND_PLAINS);
    private static final int SAND_DUNES = getBiomeId(AtumBiomes.SAND_DUNES);
    private static final int OASIS = getBiomeId(AtumBiomes.OASIS);
    private static final int DEAD_OASIS = getBiomeId(AtumBiomes.DEAD_OASIS);


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

    public static Biome getBiome(RegistryKey<Biome> key) {
        Biome biome = ForgeRegistries.BIOMES.getValue(key.getLocation());
        if (biome == null) throw new RuntimeException("Attempted to get unregistered biome " + key);
        return biome;
    }

    public static int getBiomeId(Biome biome) {
        if (biome == null) throw new RuntimeException("Attempted to get id of null biome");
        int id = ((ForgeRegistry<Biome>) ForgeRegistries.BIOMES).getID(biome);
        if (id == -1) throw new RuntimeException("Biome id is -1 for biome " + biome.delegate.name());
        return id;
    }

    public static int getBiomeId(RegistryKey<Biome> key) {
        return getBiomeId(getBiome(key));
    }
}