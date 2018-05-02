package com.teammetallurgy.atum.world.biome;

import com.google.common.collect.Lists;
import com.teammetallurgy.atum.init.AtumBiomes;
import com.teammetallurgy.atum.utils.AtumRegistry;
import net.minecraft.util.WeightedRandom;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;
import net.minecraftforge.common.BiomeManager.BiomeEntry;

import javax.annotation.Nonnull;
import java.util.List;

public class GenLayerAtumBiome extends GenLayer {
    //private List<BiomeEntry> lBiomes = Lists.newArrayList();
    //private List<BiomeEntry> hBiomes = Lists.newArrayList();
    private List<BiomeEntry> biomes = Lists.newArrayList();

    //private final NoiseGeneratorSimplex noise;

    public GenLayerAtumBiome(long seed) {
        super(seed);
        //noise = new NoiseGeneratorSimplex(new Random(seed));

        for (AtumBiome biome : AtumRegistry.BIOMES) {
            final BiomeEntry entry = new BiomeEntry(biome, biome.getWeight());
            //if( biome.rootHeight >= 0.25F ) {
            //	hBiomes.add(entry);
            //} else {
            //	lBiomes.add(entry);
            //}
            biomes.add(entry);
        }
    }

    @Override
    @Nonnull
    public int[] getInts(int x, int z, int width, int length) {
        int[] cache = IntCache.getIntCache(width * length);

        //final int lWeight = WeightedRandom.getTotalWeight(lBiomes);
        //final int hWeight = WeightedRandom.getTotalWeight(hBiomes);
        final int totalWeight = WeightedRandom.getTotalWeight(biomes);

        for (int i = 0; i < length; ++i) {
            for (int j = 0; j < width; ++j) {
                this.initChunkSeed((long) (j + x), (long) (i + z));
                //final double elevationType = noise.func_151605_a(x,z);
                final BiomeEntry biome;
                //if( elevationType <= 0.25 ) {
                //	biome = ((BiomeEntry)WeightedRandom.getItem(hBiomes, nextInt(hWeight)));
                //} else {
                //	biome = ((BiomeEntry)WeightedRandom.getItem(lBiomes, nextInt(lWeight)));
                //}
                biome = WeightedRandom.getRandomItem(biomes, nextInt(totalWeight));
                cache[j + i * width] = AtumBiome.getIdForBiome(biome.biome);
            }
        }
        return cache;
    }
}