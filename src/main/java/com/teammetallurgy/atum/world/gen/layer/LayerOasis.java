/*
package com.teammetallurgy.atum.world.gen.layer;

import com.teammetallurgy.atum.init.AtumBiomes;
import com.teammetallurgy.atum.utils.AtumConfig;
import com.teammetallurgy.atum.world.biome.base.AtumBiome;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

import javax.annotation.Nonnull;

public class LayerOasis extends GenLayer {

    public LayerOasis(long scale, GenLayer parent) {
        super(scale);
        super.parent = parent;
    }

    @Override
    @Nonnull
    public int[] getInts(int x, int z, int areaWidth, int areaHeight) {
        int[] parent = this.parent.getInts(x - 1, z - 1, areaWidth + 2, areaHeight + 2);
        int[] cache = IntCache.getIntCache(areaWidth * areaHeight);

        for (int i = 0; i < areaHeight; ++i) {
            for (int j = 0; j < areaWidth; ++j) {
                this.initChunkSeed(j + x, i + z);
                int k = parent[j + 1 + (i + 1) * (areaWidth + 2)];

                if (AtumConfig.BIOME.subBiomeChance.get() > 0 && this.nextInt(AtumConfig.SUB_BIOME_CHANCE) == 0) {
                    if (k == AtumBiome.getIdForBiome(AtumBiomes.SAND_PLAINS) || k == AtumBiome.getIdForBiome(AtumBiomes.SAND_DUNES)) {
                        if (this.nextInt(100) < AtumConfig.BIOME.oasisChance.get()) {
                            cache[j + i * areaWidth] = AtumBiome.getIdForBiome(AtumBiomes.OASIS);
                        } else {
                            cache[j + i * areaWidth] = AtumBiome.getIdForBiome(AtumBiomes.DEAD_OASIS);
                        }
                    } else {
                        cache[j + i * areaWidth] = k;
                    }
                } else {
                    cache[j + i * areaWidth] = k;
                }
            }
        }
        return cache;
    }
}*/
