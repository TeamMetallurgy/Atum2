package com.teammetallurgy.atum.world.biome;

public class SandDunesBiome extends AtumBiome {

    public SandDunesBiome() {
        super(new Builder("sand_dunes", 15).setBaseHeight(0.175F).setHeightVariation(0.2F));
        this.deadwoodRarity = 0.01D;
        super.addDefaultSpawns(this);
        super.addCamelSpawning(this);
    }
}