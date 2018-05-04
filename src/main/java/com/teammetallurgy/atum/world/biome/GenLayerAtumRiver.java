package com.teammetallurgy.atum.world.biome;

import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class GenLayerAtumRiver extends GenLayer {

    public GenLayerAtumRiver(long scale, GenLayer parent) {
        super(scale);
        super.parent = parent;
    }

    @Override
    public int[] getInts(int x, int z, int width, int length) {
        int xx = x - 1;
        int zz = z - 1;
        int ww = width + 2;
        int ll = length + 2;
        int[] pInts = this.parent.getInts(xx, zz, ww, ll);
        int[] cache = IntCache.getIntCache(width * length);

        //final int riverId = AtumBiomes.BiomeType.DRIED_RIVER.getID();

        for (int i2 = 0; i2 < length; ++i2) {
            for (int j2 = 0; j2 < width; ++j2) {
                int k2 = this.constrain(pInts[j2 + 0 + (i2 + 1) * ww]);
                int l2 = this.constrain(pInts[j2 + 2 + (i2 + 1) * ww]);
                int i3 = this.constrain(pInts[j2 + 1 + (i2 + 0) * ww]);
                int j3 = this.constrain(pInts[j2 + 1 + (i2 + 2) * ww]);
                int k3 = this.constrain(pInts[j2 + 1 + (i2 + 1) * ww]);

                if (k3 == k2 && k3 == i3 && k3 == l2 && k3 == j3) {
                    cache[j2 + i2 * width] = -1;
                } else {
                    //cache[j2 + i2 * width] = riverId;
                }
            }
        }
        return cache;
    }

    private int constrain(int val) {
        return val >= 2 ? 2 + (val & 1) : val;
    }
}