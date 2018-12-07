package com.teammetallurgy.atum.world.biome;

import com.teammetallurgy.atum.world.biome.base.AtumBiome;

public class BiomeDeadwoodForest extends AtumBiome {

    public BiomeDeadwoodForest(AtumBiomeProperties properties) {
        super(properties);

        this.deadwoodRarity = 1.0D;

        this.addDefaultSpawns();
    }
}