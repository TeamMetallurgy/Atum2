package com.teammetallurgy.atum.world.gen.layer;

import com.google.common.collect.Lists;
import com.teammetallurgy.atum.utils.AtumRegistry;
import com.teammetallurgy.atum.world.biome.AtumBiome;
import net.minecraft.util.WeightedRandom;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;
import net.minecraftforge.common.BiomeManager.BiomeEntry;

import javax.annotation.Nonnull;
import java.util.List;

public class GenLayerAtumBiome extends GenLayer {
    private List<BiomeEntry> biomes = Lists.newArrayList();

    public GenLayerAtumBiome(long seed) {
        super(seed);

        for (AtumBiome biome : AtumRegistry.BIOMES) {
            final BiomeEntry entry = new BiomeEntry(biome, biome.getWeight());
            biomes.add(entry);
        }
    }

    @Override
    @Nonnull
    public int[] getInts(int x, int z, int width, int length) {
        int[] cache = IntCache.getIntCache(width * length);

        final int totalWeight = WeightedRandom.getTotalWeight(biomes);

        for (int i = 0; i < length; ++i) {
            for (int j = 0; j < width; ++j) {
                this.initChunkSeed((long) (j + x), (long) (i + z));
                final BiomeEntry biome;
                biome = WeightedRandom.getRandomItem(biomes, nextInt(totalWeight));
                cache[j + i * width] = AtumBiome.getIdForBiome(biome.biome);
            }
        }
        return cache;
    }
}