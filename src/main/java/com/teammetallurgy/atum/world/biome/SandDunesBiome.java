package com.teammetallurgy.atum.world.biome;

import com.teammetallurgy.atum.init.AtumFeatures;

public class SandDunesBiome extends AtumBiome {

    public SandDunesBiome() {
        super(new Builder("sand_dunes", 15).setBaseHeight(0.175F).setHeightVariation(0.2F));
        super.addDefaultSpawns(this);
        super.addCamelSpawning(this);
        AtumFeatures.Default.addDeadwoodTrees(this, 0, 0.05F, 1);
        AtumFeatures.Default.addCarvers(this);
        AtumFeatures.Default.addSprings(this);
        AtumFeatures.Default.addLavaLakes(this);
        AtumFeatures.Default.addMaterialPockets(this);
        AtumFeatures.Default.addStoneVariants(this);
        AtumFeatures.Default.addOres(this);
        AtumFeatures.Default.addInfestedLimestone(this);
        AtumFeatures.Default.addShrubs(this);
        AtumFeatures.Default.addFossils(this);
        AtumFeatures.Default.addDungeon(this);
        AtumFeatures.Default.addTomb(this);
        AtumFeatures.Default.addPyramid(this);
        AtumFeatures.Default.addRuins(this);
        AtumFeatures.Default.addMineshaft(this, false);
    }
}