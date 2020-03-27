package com.teammetallurgy.atum.world.biome;

public class BiomeDriedRiver extends AtumBiome {

    public BiomeDriedRiver() {
        super(new Builder("Dried River", 0).setBaseHeight(-0.5F).setHeightVariation(0.0F));

        //this.topBlock = AtumBlocks.LIMESTONE_GRAVEL.getDefaultState();
        //this.fillerBlock = AtumBlocks.LIMESTONE_CRACKED.getDefaultState();

        this.deadwoodRarity = 0.0D;
    }
}