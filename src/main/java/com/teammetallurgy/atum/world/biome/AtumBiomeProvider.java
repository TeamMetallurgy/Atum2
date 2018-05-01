package com.teammetallurgy.atum.world.biome;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.gen.layer.*;
import net.minecraft.world.storage.WorldInfo;

import java.util.List;
import java.util.Random;

public class AtumBiomeProvider extends BiomeProvider {
    public static final int BIOME_SCALE = 4;
    private GenLayer biomeIndexLayer;

    public AtumBiomeProvider(WorldInfo info) {

        GenLayer layerBiome = new GenLayerAtumBiome(info.getSeed());
        for (int k = 0; k < BIOME_SCALE; ++k) {
            layerBiome = new GenLayerZoom((long) (1000 + k), layerBiome);
        }

        GenLayer layerSmooth = new GenLayerSmooth(1000L, layerBiome);
        GenLayer layerVoronoi = new GenLayerVoronoiZoom(10L, layerSmooth);
        layerVoronoi.initWorldGenSeed(info.getSeed());

        GenLayer layerRiver = new GenLayerRiverInit(100L, layerSmooth);
        layerRiver = GenLayerZoom.magnify(1000L, layerRiver, 2);
        layerRiver = GenLayerZoom.magnify(1000L, layerRiver, BIOME_SCALE);
        layerRiver = new GenLayerAtumRiver(1L, layerRiver);
        layerRiver = new GenLayerSmooth(1000L, layerRiver);
        layerRiver = new GenLayerAtumRiverMix(100L, layerVoronoi, layerRiver);

        biomeIndexLayer = layerRiver;
    }

    /*@Override
    public float[] getRainfall(float[] listToReuse, int x, int z, int width, int length) { //TODO
        if (listToReuse == null || listToReuse.length < width * length) {
            listToReuse = new float[width * length];
        }

        // easy cheatsy way
        // Arrays.fill(list, 0, width * length, 0.0F);

        final int[] cache = biomeIndexLayer.getInts(x, z, width, length);

        for (int k = 0; k < length * width; ++k) {
            // NB: reference wraps this in a try/catch with crash reporting
            final float rain = Biome.getBiome(cache[k]).getRainfall() / 65536.0F;
            listToReuse[k] = (rain > 1.0F ? 1.0F : rain);
        }

        return listToReuse;
    }*/

    @Override
    public Biome[] getBiomesForGeneration(Biome[] biomes, int x, int z, int width, int height) {
        if (biomes == null || biomes.length < width * height) {
            biomes = new Biome[width * height];
        }

        final int[] cache = biomeIndexLayer.getInts(x, z, width, height);

        // NB: reference wraps this in a try/catch with crash reporting
        for (int k = 0; k < height * width; ++k) {
            biomes[k] = Biome.getBiome(cache[k]);
        }

        return biomes;
    }


    @Override
    public Biome[] getBiomes(Biome[] listToReuse, int x, int z, int width, int length, boolean cacheFlag) {
        IntCache.resetIntCache();

        if (listToReuse == null || listToReuse.length < width * length) {
            listToReuse = new Biome[width * length];
        }

    	/*
    	 * Irritating scope on biomeCache, have to do it hard way :(
    	 * 
        if (checkCache && width == 16 && length == 16 && (x & 15) == 0 && (z & 15) == 0) {
            Biome[] abiomegenbase1 = this.biomeCache.getCachedBiomes(x, z);
            System.arraycopy(abiomegenbase1, 0, list, 0, width * length);
            return list;
        } else {
        	*/
        int[] cache = this.biomeIndexLayer.getInts(x, z, width, length);

        for (int k = 0; k < width * length; ++k) {
            listToReuse[k] = Biome.getBiome(cache[k]);
        }

        return listToReuse;
        /*}*/
    }

    @Override
    public BlockPos findBiomePosition(int x, int z, int range, List<Biome> biomes, Random random) {
        IntCache.resetIntCache();
        int i = x - range >> 2;
        int j = z - range >> 2;
        int k = x + range >> 2;
        int l = z + range >> 2;
        int i1 = k - i + 1;
        int j1 = l - j + 1;
        int[] aint = this.biomeIndexLayer.getInts(i, j, i1, j1);
        BlockPos pos = null;
        int k1 = 0;

        for (int l1 = 0; l1 < i1 * j1; ++l1) {
            int i2 = i + l1 % i1 << 2;
            int j2 = j + l1 / i1 << 2;
            Biome biomeGenBase = Biome.getBiome(aint[l1]);
            if (biomes.contains(biomeGenBase) && (pos == null || random.nextInt(k1 + 1) == 0)) {
                pos = new BlockPos(i2, 0, j2);
                ++k1;
            }
        }

        return pos;
    }

    @Override
    public boolean areBiomesViable(int x, int z, int range, List<Biome> biomes) {
        // TODO: don't be lazy
        return true;

        //return p_76940_4_.contains(this.biomeGenerator);
    }
}