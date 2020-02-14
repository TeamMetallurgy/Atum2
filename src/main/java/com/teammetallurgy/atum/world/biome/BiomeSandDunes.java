package com.teammetallurgy.atum.world.biome;

import com.teammetallurgy.atum.init.AtumEntities;
import net.minecraft.entity.EntityClassification;

public class BiomeSandDunes extends AtumBiome {

    public BiomeSandDunes(AtumBiomeProperties properties) {
        super(properties);

        //this.fillerBlock = AtumBlocks.SAND.getDefaultState();

        this.deadwoodRarity = 0.01D;

        this.addDefaultSpawns();
    }

    @Override
    protected void addDefaultSpawns() {
        super.addDefaultSpawns();

        addSpawn(AtumEntities.CAMEL, 6, 2, 6, EntityClassification.CREATURE);
    }
}