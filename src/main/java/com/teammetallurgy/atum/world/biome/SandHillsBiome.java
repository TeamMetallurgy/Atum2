package com.teammetallurgy.atum.world.biome;

import com.teammetallurgy.atum.init.AtumFeatures;
import net.minecraft.world.biome.DefaultBiomeFeatures;

public class SandHillsBiome extends AtumBiome {

    public SandHillsBiome() {
        super(new Builder("sand_hills", 10).setBaseHeight(0.3F).setHeightVariation(0.3F));
        this.deadwoodRarity = 0.08D;
        super.addDefaultSpawns(this);
        super.addDesertWolfSpawning(this);
        AtumFeatures.Default.addCarvers(this);
        AtumFeatures.Default.addSprings(this);
    }
}