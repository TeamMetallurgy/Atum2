package com.teammetallurgy.atum.world.biome;

import com.teammetallurgy.atum.init.AtumBlocks;

public class BiomeGenDeadOasis extends AtumBiome {

    public BiomeGenDeadOasis(AtumBiomeProperties properties) {
        super(properties);

        this.topBlock = AtumBlocks.LIMESTONE_CRACKED.getDefaultState();

        //no hostile spawns here

        this.palmRarity = -1;
        this.pyramidRarity = -1;
        this.deadwoodRarity = 6;
    }
}