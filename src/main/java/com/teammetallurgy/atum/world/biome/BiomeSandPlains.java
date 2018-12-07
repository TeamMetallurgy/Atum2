package com.teammetallurgy.atum.world.biome;

import com.teammetallurgy.atum.world.biome.base.AtumBiome;

public class BiomeSandPlains extends AtumBiome {

    public BiomeSandPlains(AtumBiomeProperties properties) {
        super(properties);

        this.deadwoodRarity = 0.01D;

        this.addDefaultSpawns();
    }
}