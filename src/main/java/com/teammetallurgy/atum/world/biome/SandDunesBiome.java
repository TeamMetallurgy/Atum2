package com.teammetallurgy.atum.world.biome;

import com.teammetallurgy.atum.init.AtumEntities;
import net.minecraft.entity.EntityClassification;

public class SandDunesBiome extends AtumBiome {

    public SandDunesBiome() {
        super(new Builder("sand_dunes", 15).setBaseHeight(0.175F).setHeightVariation(0.2F));
        this.deadwoodRarity = 0.01D;
        this.addDefaultSpawns();
    }

    @Override
    protected void addDefaultSpawns() {
        super.addDefaultSpawns();
        addSpawn(AtumEntities.CAMEL, 6, 2, 6, EntityClassification.CREATURE);
    }
}