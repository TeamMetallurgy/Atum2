package com.teammetallurgy.atum.world.biome;

import com.teammetallurgy.atum.init.AtumBlocks;

public class BiomeGenDriedRiver extends AtumBiome {

    public BiomeGenDriedRiver(AtumBiomeProperties properties) {
        super(properties);

        this.topBlock = AtumBlocks.LIMESTONE_GRAVEL.getDefaultState();
        this.fillerBlock = AtumBlocks.LIMESTONE_CRACKED.getDefaultState();

        this.palmRarity = -1;
        this.pyramidRarity = -1;
        this.deadwoodRarity = -1;
    }
}