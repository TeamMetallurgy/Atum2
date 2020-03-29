package com.teammetallurgy.atum.world.biome;

public class SandPlainsBiome extends AtumBiome {

    public SandPlainsBiome() {
        super(new Builder("sand_plains", 30));
        this.deadwoodRarity = 0.01D;
        super.addDefaultSpawns(this);
        super.addCamelSpawning(this);
    }
}