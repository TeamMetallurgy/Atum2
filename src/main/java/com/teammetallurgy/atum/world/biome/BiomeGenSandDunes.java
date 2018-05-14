package com.teammetallurgy.atum.world.biome;

import com.teammetallurgy.atum.init.AtumBlocks;

public class BiomeGenSandDunes extends AtumBiome {

    public BiomeGenSandDunes(AtumBiomeProperties properties) {
        super(properties);

        this.fillerBlock = AtumBlocks.SAND.getDefaultState();

        this.palmRarity = -1;
        this.deadwoodRarity = 5;

        this.addDefaultSpawns();
    }
}