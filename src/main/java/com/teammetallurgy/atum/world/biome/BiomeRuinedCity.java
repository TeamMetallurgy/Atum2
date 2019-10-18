package com.teammetallurgy.atum.world.biome;

public class BiomeRuinedCity extends AtumBiome {

    public BiomeRuinedCity(AtumBiomeProperties properties) {
        super(properties);

        this.deadwoodRarity = 0.90D;

        this.addDefaultSpawns();
    }
}