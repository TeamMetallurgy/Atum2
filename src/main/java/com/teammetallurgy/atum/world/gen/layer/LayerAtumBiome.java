package com.teammetallurgy.atum.world.gen.layer;

import com.google.common.collect.Lists;
import com.teammetallurgy.atum.utils.AtumRegistry;
import com.teammetallurgy.atum.world.biome.AtumBiome;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.layer.traits.IC0Transformer;
import net.minecraftforge.common.BiomeManager.BiomeEntry;

import javax.annotation.Nonnull;
import java.util.List;

public class LayerAtumBiome implements IC0Transformer {
    private List<BiomeEntry> biomes = Lists.newArrayList();

    public LayerAtumBiome() {
        for (AtumBiome biome : AtumRegistry.BIOMES) {
            final BiomeEntry entry = new BiomeEntry(biome, biome.getWeight());
            if (biome.getWeight() > 0) {
                this.biomes.add(entry);
            }
        }
    }

    @Override
    public int apply(@Nonnull INoiseRandom noiseRandom, int value) {
        List<BiomeEntry> biomeList = this.biomes;
        int totalWeight = WeightedRandom.getTotalWeight(biomeList);
        int weight = noiseRandom.random(totalWeight);
        return Registry.BIOME.getId(WeightedRandom.getRandomItem(biomeList, weight).biome);
    }
}