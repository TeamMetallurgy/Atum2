package com.teammetallurgy.atum.world.gen.layer;

import com.google.common.collect.Lists;
import com.teammetallurgy.atum.misc.AtumConfig;
import com.teammetallurgy.atum.misc.AtumRegistry;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.layer.traits.IC0Transformer;
import net.minecraftforge.common.BiomeManager.BiomeEntry;

import javax.annotation.Nonnull;
import java.util.List;

public class AtumBiomeLayer implements IC0Transformer {
    private final List<BiomeEntry> biomes = Lists.newArrayList();

    public AtumBiomeLayer() {
        for (RegistryKey<Biome> biomeKey : AtumRegistry.BIOME_KEYS) {
            ResourceLocation location = biomeKey.getLocation();
            if (location != null) {
                int weight = AtumConfig.Helper.get(AtumConfig.Helper.getSubConfig(AtumConfig.Biome.BIOME, location.getPath()), "weight");
                if (weight > 0) {
                    final BiomeEntry entry = new BiomeEntry(biomeKey, weight);
                    this.biomes.add(entry);
                }
            }
        }
    }

    @Override
    public int apply(@Nonnull INoiseRandom noiseRandom, int value) {
        List<BiomeEntry> biomeList = this.biomes;
        int totalWeight = WeightedRandom.getTotalWeight(biomeList);
        int weight = noiseRandom.random(totalWeight);
        return WorldGenRegistries.BIOME.getId(WorldGenRegistries.BIOME.getValueForKey(WeightedRandom.getRandomItem(biomeList, weight).getKey()));
    }
}