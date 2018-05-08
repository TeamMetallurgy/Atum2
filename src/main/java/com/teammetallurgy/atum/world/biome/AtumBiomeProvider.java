package com.teammetallurgy.atum.world.biome;

import com.google.common.collect.Lists;
import com.teammetallurgy.atum.init.AtumBiomes;
import com.teammetallurgy.atum.world.gen.layer.GenLayerAtumBiome;
import com.teammetallurgy.atum.world.gen.layer.GenLayerAtumRiver;
import com.teammetallurgy.atum.world.gen.layer.GenLayerAtumRiverMix;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.init.Biomes;
import net.minecraft.util.ReportedException;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeCache;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.gen.layer.*;
import net.minecraft.world.storage.WorldInfo;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Random;

public class AtumBiomeProvider extends BiomeProvider {
    private GenLayer genBiomes;
    private final BiomeCache biomeCache;
    private final List<Biome> biomesToSpawnIn;
    public static final int BIOME_SCALE = 4;

    public AtumBiomeProvider(WorldInfo info) {
        this.biomesToSpawnIn = Lists.newArrayList(AtumBiomes.SAND_PLAINS); //TODO

        this.biomeCache = new BiomeCache(this);
        this.genBiomes = initializeAllBiomeGenerators(info)[0];
    }

    public GenLayer[] initializeAllBiomeGenerators(WorldInfo info) { //TODO Atum biome height lays somewhere within this method
        GenLayer layerBiome = new GenLayerAtumBiome(info.getSeed());
        for (int k = 0; k < BIOME_SCALE; ++k) {
            layerBiome = new GenLayerZoom((long) (1000 + k), layerBiome);
        }
        GenLayer layerSmooth = new GenLayerSmooth(1000L, layerBiome);
        GenLayer layerVoronoi = new GenLayerVoronoiZoom(10L, layerSmooth);
        layerVoronoi.initWorldGenSeed(info.getSeed());

        GenLayer layerRiver = new GenLayerRiverInit(100L, layerSmooth);
        layerRiver = GenLayerZoom.magnify(1000L, layerRiver, 2);
        layerRiver = GenLayerZoom.magnify(1000L, layerRiver, 0);
        layerRiver = new GenLayerAtumRiver(1L, layerRiver);
        layerRiver = new GenLayerSmooth(1000L, layerRiver);
        layerRiver = new GenLayerAtumRiverMix(100L, layerVoronoi, layerRiver);

        return new GenLayer[]{layerRiver, layerVoronoi, layerRiver};
    }

    @Override
    @Nonnull
    public List<Biome> getBiomesToSpawnIn() {
        return this.biomesToSpawnIn;
    }

    @Override
    @Nonnull
    public Biome getBiome(BlockPos pos, @Nonnull Biome defaultBiome) {
        return this.biomeCache.getBiome(pos.getX(), pos.getZ(), defaultBiome);
    }

    @Override
    @Nonnull
    public Biome[] getBiomesForGeneration(Biome[] biomes, int x, int z, int width, int height) {
        IntCache.resetIntCache();

        if (biomes == null || biomes.length < width * height) {
            biomes = new Biome[width * height];
        }

        final int[] cache = this.genBiomes.getInts(x, z, width, height);

        try {
            for (int i = 0; i < width * height; ++i) {
                biomes[i] = Biome.getBiome(cache[i], Biomes.DEFAULT);
            }
            return biomes;
        } catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Invalid Biome id");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("RawBiomeBlock");
            crashreportcategory.addCrashSection("biomes[] size", biomes.length);
            crashreportcategory.addCrashSection("x", x);
            crashreportcategory.addCrashSection("z", z);
            crashreportcategory.addCrashSection("w", width);
            crashreportcategory.addCrashSection("h", height);
            throw new ReportedException(crashreport);
        }
    }

    @Override
    @Nonnull
    public Biome[] getBiomes(Biome[] listToReuse, int x, int z, int width, int length, boolean cacheFlag) {
        IntCache.resetIntCache();

        if (listToReuse == null || listToReuse.length < width * length) {
            listToReuse = new Biome[width * length];
        }

        if (cacheFlag && width == 16 && length == 16 && (x & 15) == 0 && (z & 15) == 0) {
            Biome[] abiome = this.biomeCache.getCachedBiomes(x, z);
            System.arraycopy(abiome, 0, listToReuse, 0, width * length);
            return listToReuse;
        } else {
            int[] cache = this.genBiomes.getInts(x, z, width, length);

            for (int i = 0; i < width * length; ++i) {
                listToReuse[i] = Biome.getBiome(cache[i], Biomes.DEFAULT);
            }

            return listToReuse;
        }
    }

    @Override
    public boolean areBiomesViable(int x, int z, int radius, @Nonnull List<Biome> allowed) {
        IntCache.resetIntCache();
        int i = x - radius >> 2;
        int j = z - radius >> 2;
        int k = x + radius >> 2;
        int l = z + radius >> 2;
        int i1 = k - i + 1;
        int j1 = l - j + 1;
        int[] aint = this.genBiomes.getInts(i, j, i1, j1);

        try {
            for (int k1 = 0; k1 < i1 * j1; ++k1) {
                Biome biome = Biome.getBiome(aint[k1]);

                if (!allowed.contains(biome)) {
                    return false;
                }
            }
            return true;
        } catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Invalid Biome id");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Layer");
            crashreportcategory.addCrashSection("Layer", this.genBiomes.toString());
            crashreportcategory.addCrashSection("x", x);
            crashreportcategory.addCrashSection("z", z);
            crashreportcategory.addCrashSection("radius", radius);
            crashreportcategory.addCrashSection("allowed", allowed);
            throw new ReportedException(crashreport);
        }
    }

    @Override
    public BlockPos findBiomePosition(int x, int z, int range, @Nonnull List<Biome> biomes, @Nonnull Random random) {
        IntCache.resetIntCache();
        int i = x - range >> 2;
        int j = z - range >> 2;
        int k = x + range >> 2;
        int l = z + range >> 2;
        int i1 = k - i + 1;
        int j1 = l - j + 1;
        int[] aint = this.genBiomes.getInts(i, j, i1, j1);
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
    public void cleanupCache() {
        this.biomeCache.cleanupCache();
    }
}