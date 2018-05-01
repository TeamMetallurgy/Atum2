package com.teammetallurgy.atum.world.biome;

import com.teammetallurgy.atum.init.AtumBlocks;

public class BiomeGenSandDunes extends AtumBiome {

    public BiomeGenSandDunes(BiomeProperties properties) {
        super(properties);

        super.fillerBlock = AtumBlocks.SAND.getDefaultState();

        super.palmRarity *= 2;
        super.deadwoodRarity = -1;

        super.addDefaultSpawns();
    }
}