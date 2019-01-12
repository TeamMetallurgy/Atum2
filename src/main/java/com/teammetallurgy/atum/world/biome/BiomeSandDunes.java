package com.teammetallurgy.atum.world.biome;

import com.teammetallurgy.atum.entity.animal.EntityCamel;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.world.biome.base.AtumBiome;
import net.minecraft.entity.EnumCreatureType;

public class BiomeSandDunes extends AtumBiome {

    public BiomeSandDunes(AtumBiomeProperties properties) {
        super(properties);

        this.fillerBlock = AtumBlocks.SAND.getDefaultState();

        this.deadwoodRarity = 0.01D;

        this.addDefaultSpawns();
    }

    @Override
    protected void addDefaultSpawns() {
        super.addDefaultSpawns();

        addSpawn(EntityCamel.class, 6, 2, 6, EnumCreatureType.CREATURE);
    }
}