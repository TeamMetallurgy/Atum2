package com.teammetallurgy.atum.world.biome;

import com.teammetallurgy.atum.entity.animal.CamelEntity;
import com.teammetallurgy.atum.world.biome.base.AtumBiome;
import net.minecraft.entity.EnumCreatureType;

public class BiomeSandPlains extends AtumBiome {

    public BiomeSandPlains(AtumBiomeProperties properties) {
        super(properties);

        this.deadwoodRarity = 0.01D;

        this.addDefaultSpawns();
    }

    @Override
    protected void addDefaultSpawns() {
        super.addDefaultSpawns();

        addSpawn(CamelEntity.class, 6, 2, 6, EnumCreatureType.CREATURE);
    }
}