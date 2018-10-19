package com.teammetallurgy.atum.world.gen.layer;

import com.google.common.collect.Lists;
import com.teammetallurgy.atum.utils.AtumRegistry;
import com.teammetallurgy.atum.world.biome.base.AtumBiome;
import net.minecraft.util.WeightedRandom;
import net.minecraft.world.gen.ChunkGeneratorSettings;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;
import net.minecraftforge.common.BiomeManager.BiomeEntry;

import javax.annotation.Nonnull;
import java.util.List;

public class GenLayerAtumBiome extends GenLayer {
    private List<BiomeEntry> biomes = Lists.newArrayList();
    private final ChunkGeneratorSettings settings;

    public GenLayerAtumBiome(long seed, ChunkGeneratorSettings settings) {
        super(seed);
        this.settings = settings;

        for (AtumBiome biome : AtumRegistry.BIOMES) {
            final BiomeEntry entry = new BiomeEntry(biome, biome.getWeight());
            if (biome.getWeight() != 0) {
                biomes.add(entry);
            }
        }
    }

    @Override
    @Nonnull
    public int[] getInts(int x, int z, int width, int areaHeight) {
        int[] cache = IntCache.getIntCache(width * areaHeight);

        final int totalWeight = WeightedRandom.getTotalWeight(biomes);

        for (int i = 0; i < areaHeight; ++i) {
            for (int j = 0; j < width; ++j) {
                this.initChunkSeed((long) (j + x), (long) (i + z));
                if (this.settings != null && this.settings.fixedBiome >= 0) {
                    cache[j + i * width] = this.settings.fixedBiome;
                } else {
                    final BiomeEntry biome;
                    biome = WeightedRandom.getRandomItem(biomes, nextInt(totalWeight));
                    cache[j + i * width] = AtumBiome.getIdForBiome(biome.biome);
                }
            }
        }
        return cache;
    }
}