package com.teammetallurgy.atum.world.biome;

import com.teammetallurgy.atum.world.biome.base.AtumBiome;

public class BiomeRuinedCity extends AtumBiome {

    public BiomeRuinedCity(AtumBiomeProperties properties) {
        super(properties);

        this.deadwoodRarity = 2;

        this.addDefaultSpawns();
    }
}