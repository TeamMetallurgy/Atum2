package com.teammetallurgy.atum.world.biome;

public class BiomeGenSandHills extends AtumBiome {

    public BiomeGenSandHills(AtumBiomeProperties properties) {
        super(properties);

        super.palmRarity *= 4;
        super.pyramidRarity = -1;
        super.deadwoodRarity = -1;

        super.addDefaultSpawns();
    }
}