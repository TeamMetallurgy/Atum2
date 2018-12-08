package com.teammetallurgy.atum.world.biome;

import com.teammetallurgy.atum.entity.EntityDesertWolf;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.world.biome.base.AtumBiome;
import net.minecraft.entity.EnumCreatureType;

public class BiomeLimestoneMountains extends AtumBiome {

    public BiomeLimestoneMountains(AtumBiomeProperties properties) {
        super(properties);

        this.fillerBlock = AtumBlocks.LIMESTONE.getDefaultState();

        this.addDefaultSpawns();
    }

    @Override
    protected void addDefaultSpawns() {
        super.addDefaultSpawns();
        
        addSpawn(EntityDesertWolf.class, 5, 2, 4, EnumCreatureType.CREATURE);
    }
}