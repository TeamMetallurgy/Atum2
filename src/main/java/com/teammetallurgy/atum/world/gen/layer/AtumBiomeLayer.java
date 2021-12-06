package com.teammetallurgy.atum.world.gen.layer;

import com.google.common.collect.Lists;
import com.teammetallurgy.atum.misc.AtumConfig;
import com.teammetallurgy.atum.misc.AtumRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.WeighedRandom;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.newbiome.context.Context;
import net.minecraft.world.level.newbiome.layer.traits.C0Transformer;
import net.minecraftforge.common.BiomeManager.BiomeEntry;

import javax.annotation.Nonnull;
import java.util.List;

public class AtumBiomeLayer implements C0Transformer {
    private final List<BiomeEntry> biomes = Lists.newArrayList();
    private final Registry<Biome> biomeRegistry;

    public AtumBiomeLayer(Registry<Biome> biomeRegistry) {
        this.biomeRegistry = biomeRegistry;
        for (ResourceKey<Biome> biomeKey : AtumRegistry.BIOME_KEYS) {
            ResourceLocation location = biomeKey.location();
            if (location != null) {
                String subConfig = AtumConfig.Helper.getSubConfig(AtumConfig.Biome.BIOME, location.getPath());
                if (AtumConfig.Helper.get(subConfig) != null) { //Ensure config exists
                    int weight = AtumConfig.Helper.get(subConfig, "weight");
                    if (weight > 0) {
                        final BiomeEntry entry = new BiomeEntry(biomeKey, weight);
                        this.biomes.add(entry);
                    }
                }
            }
        }
    }

    @Override
    public int apply(@Nonnull Context noiseRandom, int value) {
        List<BiomeEntry> biomeList = this.biomes;
        int totalWeight = WeighedRandom.getTotalWeight(biomeList);
        int weight = noiseRandom.nextRandom(totalWeight);
        return biomeRegistry.getId(biomeRegistry.get(WeighedRandom.getWeightedItem(biomeList, weight).getKey()));
    }
}