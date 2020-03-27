/*
package com.teammetallurgy.atum.world.gen.layer;

import com.teammetallurgy.atum.init.AtumBiomes;
import com.teammetallurgy.atum.world.biome.base.AtumBiome;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

import javax.annotation.Nonnull;

public class LayerAtumRiver extends GenLayer {

    public LayerAtumRiver(long scale, GenLayer parent) {
        super(scale);
        super.parent = parent;
    }

    @Override
    @Nonnull
    public int[] getInts(int x, int z, int areaWidth, int areaHeight) {
        int xx = x - 1;
        int zz = z - 1;
        int ww = areaWidth + 2;
        int ll = areaHeight + 2;
        int[] pInts = this.parent.getInts(xx, zz, ww, ll);
        int[] cache = IntCache.getIntCache(areaWidth * areaHeight);

        for (int i1 = 0; i1 < areaHeight; ++i1) {
            for (int j1 = 0; j1 < areaWidth; ++j1) {
                int k1 = this.riverFilter(pInts[j1 + (i1 + 1) * ww]);
                int l1 = this.riverFilter(pInts[j1 + 2 + (i1 + 1) * ww]);
                int i2 = this.riverFilter(pInts[j1 + 1 + (i1) * ww]);
                int j2 = this.riverFilter(pInts[j1 + 1 + (i1 + 2) * ww]);
                int k2 = this.riverFilter(pInts[j1 + 1 + (i1 + 1) * ww]);

                if (k2 == k1 && k2 == i2 && k2 == l1 && k2 == j2) {
                    cache[j1 + i1 * areaWidth] = -1;
                } else {
                    cache[j1 + i1 * areaWidth] = AtumBiome.getIdForBiome(AtumBiomes.DRIED_RIVER);
                }
            }
        }
        return cache;
    }

    private int riverFilter(int val) {
        return val >= 2 ? 2 + (val & 1) : val;
    }
}*/
