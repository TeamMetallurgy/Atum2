package com.teammetallurgy.atum.world.biome;

import com.teammetallurgy.atum.world.biome.base.AtumBiome;

public class BiomeSandHills extends AtumBiome {

    public BiomeSandHills(AtumBiomeProperties properties) {
        super(properties);

        this.pyramidRarity = -1;
        this.deadwoodRarity = 4;

        this.addDefaultSpawns();
    }
}