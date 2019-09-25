package com.teammetallurgy.atum.world.biome;

import com.teammetallurgy.atum.entity.animal.DesertWolfEntity;
import com.teammetallurgy.atum.world.biome.base.AtumBiome;
import net.minecraft.entity.EnumCreatureType;

public class BiomeSandHills extends AtumBiome {

    public BiomeSandHills(AtumBiomeProperties properties) {
        super(properties);

        this.addDefaultSpawns();

        this.deadwoodRarity = 0.08D;
    }

    @Override
    protected void addDefaultSpawns() {
        super.addDefaultSpawns();
        
        addSpawn(DesertWolfEntity.class, 5, 2, 4, EnumCreatureType.CREATURE);
    }
}