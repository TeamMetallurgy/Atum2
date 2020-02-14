package com.teammetallurgy.atum.world.biome;

import com.teammetallurgy.atum.init.AtumEntities;
import net.minecraft.entity.EntityClassification;

public class BiomeSandPlains extends AtumBiome {

    public BiomeSandPlains(AtumBiomeProperties properties) {
        super(properties);

        this.deadwoodRarity = 0.01D;

        this.addDefaultSpawns();
    }

    @Override
    protected void addDefaultSpawns() {
        super.addDefaultSpawns();

        addSpawn(AtumEntities.CAMEL, 6, 2, 6, EntityClassification.CREATURE);
    }
}