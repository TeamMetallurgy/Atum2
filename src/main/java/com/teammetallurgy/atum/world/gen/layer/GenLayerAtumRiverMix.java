package com.teammetallurgy.atum.world.gen.layer;

import com.teammetallurgy.atum.init.AtumBiomes;
import com.teammetallurgy.atum.world.biome.AtumBiome;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

import javax.annotation.Nonnull;

public class GenLayerAtumRiverMix extends GenLayer {
    private GenLayer biomePatternGeneratorChain;
    private GenLayer riverPatternGeneratorChain;

    public GenLayerAtumRiverMix(long scale, GenLayer biomeChain, GenLayer riverChain) {
        super(scale);
        this.biomePatternGeneratorChain = biomeChain;
        this.riverPatternGeneratorChain = riverChain;
    }

    public void initWorldGenSeed(long seed) {
        this.biomePatternGeneratorChain.initWorldGenSeed(seed);
        this.riverPatternGeneratorChain.initWorldGenSeed(seed);
        super.initWorldGenSeed(seed);
    }

    @Override
    @Nonnull
    public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight) {
        int[] biomes = this.biomePatternGeneratorChain.getInts(areaX, areaY, areaWidth, areaHeight);
        int[] rivers = this.riverPatternGeneratorChain.getInts(areaX, areaY, areaWidth, areaHeight);
        int[] cache = IntCache.getIntCache(areaWidth * areaHeight);

        for (int i = 0; i < areaWidth * areaHeight; ++i) {
            if (rivers[i] == AtumBiome.getIdForBiome(AtumBiomes.DRIED_RIVER)) {
                cache[i] = rivers[i];
            } else {
                cache[i] = biomes[i];
            }
        }
        return cache;
    }
}