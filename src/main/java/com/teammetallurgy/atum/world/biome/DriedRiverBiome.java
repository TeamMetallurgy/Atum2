package com.teammetallurgy.atum.world.biome;

import com.teammetallurgy.atum.world.gen.AtumSurfaceBuilders;

public class DriedRiverBiome extends AtumBiome {

    public DriedRiverBiome() {
        super(new Builder("Dried River", 0).setBaseHeight(-0.5F).setHeightVariation(0.0F).setBiomeBlocks(AtumSurfaceBuilders.GRAVEL_CRACKED));

        this.deadwoodRarity = 0.0D;
    }
}