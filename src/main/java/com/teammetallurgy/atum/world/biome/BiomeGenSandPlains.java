package com.teammetallurgy.atum.world.biome;

public class BiomeGenSandPlains extends AtumBiome {

    public BiomeGenSandPlains(AtumBiomeProperties properties) {
        super(properties);

        this.palmRarity = -1;

        this.addDefaultSpawns();
    }
}