package com.teammetallurgy.atum.world.biome;

import com.teammetallurgy.atum.entity.EntityDesertWolf;
import com.teammetallurgy.atum.world.biome.base.AtumBiome;
import net.minecraft.entity.EnumCreatureType;

public class BiomeSandHills extends AtumBiome {

    public BiomeSandHills(AtumBiomeProperties properties) {
        super(properties);

        this.addDefaultSpawns();
    }
    
    protected void addDefaultSpawns() {
        super.addDefaultSpawns();
        
        addSpawn(EntityDesertWolf.class, 5, 2, 4, EnumCreatureType.CREATURE);
    }
}