package com.teammetallurgy.atum.world.biome;

import com.teammetallurgy.atum.init.AtumEntities;
import net.minecraft.entity.EntityClassification;

public class BiomeSandHills extends AtumBiome {

    public BiomeSandHills(AtumBiomeProperties properties) {
        super(properties);

        this.addDefaultSpawns();

        this.deadwoodRarity = 0.08D;
    }

    @Override
    protected void addDefaultSpawns() {
        super.addDefaultSpawns();
        
        addSpawn(AtumEntities.DESERT_WOLF, 5, 2, 4, EntityClassification.CREATURE);
    }
}