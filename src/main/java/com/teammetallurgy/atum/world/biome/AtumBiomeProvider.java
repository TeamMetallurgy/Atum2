package com.teammetallurgy.atum.world.biome;

import com.google.common.collect.Lists;
import com.teammetallurgy.atum.init.AtumBiomes;
import com.teammetallurgy.atum.world.gen.layer.GenLayerAtumBiome;
import com.teammetallurgy.atum.world.gen.layer.GenLayerAtumRiver;
import com.teammetallurgy.atum.world.gen.layer.GenLayerAtumRiverMix;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.gen.layer.*;
import net.minecraft.world.storage.WorldInfo;

import javax.annotation.Nonnull;
import java.util.List;

public class AtumBiomeProvider extends BiomeProvider {
    public static final int BIOME_SCALE = 4;

    public AtumBiomeProvider(WorldInfo info) {
        super();
        this.genBiomes = initializeAllBiomeGenerators(info)[0];
        this.biomeIndexLayer = initializeAllBiomeGenerators(info)[1];
    }

    public GenLayer[] initializeAllBiomeGenerators(WorldInfo info) {
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
        return Lists.newArrayList(AtumBiomes.SAND_PLAINS); //TODO Add biomes
    }
}