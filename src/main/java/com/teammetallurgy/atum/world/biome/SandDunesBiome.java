package com.teammetallurgy.atum.world.biome;

import com.teammetallurgy.atum.init.AtumFeatures;
import net.minecraft.world.biome.DefaultBiomeFeatures;

public class SandDunesBiome extends AtumBiome {

    public SandDunesBiome() {
        super(new Builder("sand_dunes", 15).setBaseHeight(0.175F).setHeightVariation(0.2F));
        this.deadwoodRarity = 0.01D;
        super.addDefaultSpawns(this);
        super.addCamelSpawning(this);
        AtumFeatures.Default.addCarvers(this);
        AtumFeatures.Default.addSprings(this);
    }
}