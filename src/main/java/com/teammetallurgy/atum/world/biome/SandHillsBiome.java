package com.teammetallurgy.atum.world.biome;

import com.teammetallurgy.atum.init.AtumFeatures;

public class SandHillsBiome extends AtumBiome {

    public SandHillsBiome() {
        super(new Builder("sand_hills", 10).setBaseHeight(0.3F).setHeightVariation(0.3F));
        super.addDefaultSpawns(this);
        super.addDesertWolfSpawning(this);
        AtumFeatures.Default.addDeadwoodTrees(this, 0, 0.08F, 1);
        AtumFeatures.Default.addCarvers(this);
        AtumFeatures.Default.addSprings(this);
        AtumFeatures.Default.addMaterialPockets(this);
        AtumFeatures.Default.addStoneVariants(this);
        AtumFeatures.Default.addOres(this);
        AtumFeatures.Default.addEmeraldOre(this);
        AtumFeatures.Default.addInfestedLimestone(this);
        AtumFeatures.Default.addFossils(this);
        AtumFeatures.Default.addDungeon(this);
        AtumFeatures.Default.addTomb(this);
        AtumFeatures.Default.addRuins(this);
        AtumFeatures.Default.addMineshaft(this, false);
    }
}