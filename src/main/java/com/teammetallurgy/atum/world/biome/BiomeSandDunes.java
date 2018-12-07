package com.teammetallurgy.atum.world.biome;

import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.world.biome.base.AtumBiome;

public class BiomeSandDunes extends AtumBiome {

    public BiomeSandDunes(AtumBiomeProperties properties) {
        super(properties);

        this.fillerBlock = AtumBlocks.SAND.getDefaultState();

        this.deadwoodRarity = 0.06D;

        this.addDefaultSpawns();
    }
}