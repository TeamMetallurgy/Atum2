package com.teammetallurgy.atum.world.biome;

import com.teammetallurgy.atum.world.gen.AtumSurfaceBuilders;

public class LimestoneMountainsBiome extends AtumBiome {

    public LimestoneMountainsBiome() {
        super(new Builder("limestone_mountains", 5).setBaseHeight(1.5F).setHeightVariation(0.6F).setBiomeBlocks(AtumSurfaceBuilders.SANDY_LIMESTONE));
        super.addDefaultSpawns(this);
        super.addDesertWolfSpawning(this);
    }
}