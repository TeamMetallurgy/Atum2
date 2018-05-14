package com.teammetallurgy.atum.world.biome;

public class BiomeGenSandHills extends AtumBiome {

    public BiomeGenSandHills(AtumBiomeProperties properties) {
        super(properties);

        this.palmRarity = -1;
        this.pyramidRarity = -1;
        this.deadwoodRarity = 4;

        this.addDefaultSpawns();
    }
}