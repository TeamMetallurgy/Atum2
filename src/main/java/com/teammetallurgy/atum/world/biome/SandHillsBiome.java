package com.teammetallurgy.atum.world.biome;

public class SandHillsBiome extends AtumBiome {

    public SandHillsBiome() {
        super(new Builder("sand_hills", 10).setBaseHeight(0.3F).setHeightVariation(0.3F));
        this.deadwoodRarity = 0.08D;
        super.addDefaultSpawns(this);
        super.addDesertWolfSpawning(this);
    }
}