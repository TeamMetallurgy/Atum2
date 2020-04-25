package com.teammetallurgy.atum.world.biome;

import com.teammetallurgy.atum.init.AtumFeatures;
import com.teammetallurgy.atum.world.gen.AtumSurfaceBuilders;

public class LimestoneMountainsBiome extends AtumBiome {

    public LimestoneMountainsBiome() {
        super(new Builder("limestone_mountains", 5).setBaseHeight(1.5F).setHeightVariation(0.6F).setBiomeBlocks(AtumSurfaceBuilders.SANDY_LIMESTONE));
        super.addDefaultSpawns(this);
        super.addDesertWolfSpawning(this);
        AtumFeatures.Default.addCarvers(this);
        AtumFeatures.Default.addSprings(this);
        AtumFeatures.Default.addMaterialPockets(this);
        AtumFeatures.Default.addStoneVariants(this);
        AtumFeatures.Default.addOres(this);
        AtumFeatures.Default.addEmeraldOre(this);
        AtumFeatures.Default.addInfestedLimestone(this);
        AtumFeatures.Default.addFossils(this);
        AtumFeatures.Default.addDungeon(this);
    }
}