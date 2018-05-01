package com.teammetallurgy.atum.world.biome;

import net.minecraft.world.biome.Biome;

public class BiomeGenSandHills extends AtumBiome {

    public BiomeGenSandHills(Biome.BiomeProperties properties) {
        super(properties);

        super.palmRarity *= 4;
        super.pyramidRarity = -1;
        super.deadwoodRarity = -1;

        super.addDefaultSpawns();
    }
}