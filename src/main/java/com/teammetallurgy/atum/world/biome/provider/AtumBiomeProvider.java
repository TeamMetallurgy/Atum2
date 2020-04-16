package com.teammetallurgy.atum.world.biome.provider;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.teammetallurgy.atum.init.AtumBiomes;
import com.teammetallurgy.atum.misc.AtumRegistry;
import com.teammetallurgy.atum.world.gen.layer.AtumLayerUtil;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.layer.Layer;

import javax.annotation.Nonnull;
import java.util.List;

public class AtumBiomeProvider extends BiomeProvider {
    private final Layer noiseLayer;

    public AtumBiomeProvider(AtumBiomeProviderSettings settings) {
        super(Sets.newHashSet(AtumRegistry.BIOMES)); //Biomes to check if it can generate structures
        this.noiseLayer = AtumLayerUtil.getNoiseLayer(settings.getSeed(), settings.getWorldType(), settings.getGeneratorSettings());
    }

    @Override
    @Nonnull
    public Biome getNoiseBiome(int x, int y, int z) {
        return this.noiseLayer.func_215738_a(x, z);
    }

    @Override
    @Nonnull
    public List<Biome> getBiomesToSpawnIn() {
        return Lists.newArrayList(AtumBiomes.DEAD_OASIS, AtumBiomes.DEADWOOD_FOREST, AtumBiomes.LIMESTONE_MOUNTAINS, AtumBiomes.OASIS, AtumBiomes.SAND_DUNES, AtumBiomes.SAND_HILLS, AtumBiomes.SAND_PLAINS);
    }
}