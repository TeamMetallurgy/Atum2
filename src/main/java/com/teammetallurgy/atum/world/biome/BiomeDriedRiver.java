package com.teammetallurgy.atum.world.biome;

import com.teammetallurgy.atum.init.AtumBlocks;

public class BiomeDriedRiver extends AtumBiome {

    public BiomeDriedRiver(AtumBiomeProperties properties) {
        super(properties);

        this.topBlock = AtumBlocks.LIMESTONE_GRAVEL.getDefaultState();
        this.fillerBlock = AtumBlocks.LIMESTONE_CRACKED.getDefaultState();

        this.deadwoodRarity = 0.0D;
    }
}