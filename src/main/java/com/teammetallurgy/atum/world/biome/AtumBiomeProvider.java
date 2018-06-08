package com.teammetallurgy.atum.world.biome;

import com.google.common.collect.Lists;
import com.teammetallurgy.atum.init.AtumBiomes;
import com.teammetallurgy.atum.utils.AtumRegistry;
import com.teammetallurgy.atum.world.gen.layer.GenLayerAtumBiome;
import com.teammetallurgy.atum.world.gen.layer.GenLayerAtumRiver;
import com.teammetallurgy.atum.world.gen.layer.GenLayerAtumRiverMix;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.gen.ChunkGeneratorSettings;
import net.minecraft.world.gen.layer.*;
import net.minecraft.world.storage.WorldInfo;

import javax.annotation.Nonnull;
import java.util.List;

public class AtumBiomeProvider extends BiomeProvider {

    public AtumBiomeProvider(WorldInfo info) {
        super();
        GenLayer[] genLayers = initializeAllBiomeGenerators(info.getSeed(), info.getTerrainType(), this.settings);
        this.genBiomes = genLayers[0];
        this.biomeIndexLayer = genLayers[1];
    }

    public GenLayer[] initializeAllBiomeGenerators(long seed, WorldType worldType, ChunkGeneratorSettings settings) {
        GenLayer layer = new GenLayerIsland(seed);
        layer = new GenLayerFuzzyZoom(2000L, layer);
        GenLayer layerZoom = new GenLayerZoom(2001L, layer);
        GenLayer layerSand = new GenLayerAddSnow(2L, layerZoom); //TODO Replace with GenLayerAddSandLayer
        GenLayer layerEdge = new GenLayerEdge(2L, layerSand, GenLayerEdge.Mode.COOL_WARM);
        layerEdge = new GenLayerEdge(3L, layerEdge, GenLayerEdge.Mode.SPECIAL);
        GenLayer layerZoom2 = new GenLayerZoom(2002L, layerEdge);
        layerZoom2 = new GenLayerZoom(2003L, layerZoom2);
        GenLayer magnify = GenLayerZoom.magnify(1000L, layerZoom2, 0);
        int biomeSize = 4;
        for (AtumBiome biome : AtumRegistry.BIOMES) {
            if (biome.getSize() != 0) {
                System.out.println(biome.getBiomeName() + " size: " + biome.getSize());
                biomeSize = biome.getSize();
            }
        }
        int riverSize = biomeSize;

        if (settings != null) {
            biomeSize = settings.biomeSize;
            riverSize = settings.riverSize;
        }

        if (worldType == WorldType.LARGE_BIOMES) {
            biomeSize = 6;
        }

        GenLayer riverMagnify = GenLayerZoom.magnify(1000L, magnify, 0);
        GenLayer layerRiverInit = new GenLayerRiverInit(100L, riverMagnify);
        GenLayer layerBiomes = getBiomeLayer(worldType, seed, settings);
        GenLayer hillMagnify = GenLayerZoom.magnify(1000L, layerRiverInit, 2);
        GenLayer layerHills = new GenLayerHills(1000L, layerBiomes, hillMagnify);
        GenLayer riverMagnify2 = GenLayerZoom.magnify(1000L, layerRiverInit, 2);
        riverMagnify2 = GenLayerZoom.magnify(1000L, riverMagnify2, riverSize);
        GenLayer layerRiver = new GenLayerAtumRiver(1L, riverMagnify2);
        GenLayer layerSmooth = new GenLayerSmooth(1000L, layerRiver);
        layerHills = new GenLayerRareBiome(1001L, layerHills);

        System.out.println("Beep " + biomeSize);
        for (int k = 0; k < biomeSize; ++k) {
            layerHills = new GenLayerZoom((long) (1000 + k), layerHills);
        }

        GenLayer layerSmoothRiver = new GenLayerSmooth(1000L, layerHills);
        GenLayer layerRiverMix = new GenLayerAtumRiverMix(100L, layerSmoothRiver, layerSmooth);
        GenLayer layerVoronoi = new GenLayerVoronoiZoom(10L, layerRiverMix);
        layerRiverMix.initWorldGenSeed(seed);
        layerVoronoi.initWorldGenSeed(seed);
        return new GenLayer[]{layerRiverMix, layerVoronoi, layerRiverMix};
    }

    public GenLayer getBiomeLayer(WorldType worldType, long worldSeed, ChunkGeneratorSettings chunkSettings) {
        GenLayer ret = new GenLayerAtumBiome(worldSeed /*200L*/, worldType, chunkSettings);
        ret = GenLayerZoom.magnify(1000L, ret, 2);
        ret = new GenLayerBiomeEdge(1000L, ret);
        return ret;
    }

    @Override
    @Nonnull
    public List<Biome> getBiomesToSpawnIn() {
        return Lists.newArrayList(AtumBiomes.SAND_PLAINS); //TODO Add biomes
    }
}