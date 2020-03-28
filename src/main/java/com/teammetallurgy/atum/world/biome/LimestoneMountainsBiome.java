package com.teammetallurgy.atum.world.biome;

import com.teammetallurgy.atum.init.AtumEntities;
import com.teammetallurgy.atum.world.gen.AtumSurfaceBuilders;
import net.minecraft.entity.EntityClassification;

public class LimestoneMountainsBiome extends AtumBiome {

    public LimestoneMountainsBiome() {
        super(new Builder("Limestone Mountains", 5).setBaseHeight(1.5F).setHeightVariation(0.6F).setBiomeBlocks(AtumSurfaceBuilders.SANDY_LIMESTONE));

        this.addDefaultSpawns();
    }

    @Override
    protected void addDefaultSpawns() {
        super.addDefaultSpawns();

        addSpawn(AtumEntities.DESERT_WOLF, 5, 2, 4, EntityClassification.CREATURE);
    }
}