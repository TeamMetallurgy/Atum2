package com.teammetallurgy.atum.world.biome;

import com.teammetallurgy.atum.init.AtumFeatures;
import com.teammetallurgy.atum.world.gen.AtumSurfaceBuilders;

public class DriedRiverBiome extends AtumBiome {

    public DriedRiverBiome() {
        super(new Builder("dried_river", 0).setBaseHeight(-0.5F).setHeightVariation(0.0F).setBiomeBlocks(AtumSurfaceBuilders.GRAVEL_CRACKED));
        AtumFeatures.Default.addCarvers(this);
        AtumFeatures.Default.addSprings(this);
        AtumFeatures.Default.addStoneVariants(this);
        AtumFeatures.Default.addOres(this);
        AtumFeatures.Default.addMaterialPockets(this);
        AtumFeatures.Default.addInfestedLimestone(this);
        AtumFeatures.Default.addShrubs(this);
        AtumFeatures.Default.addFossils(this);
        AtumFeatures.Default.addDungeon(this);
        AtumFeatures.Default.addTomb(this);
    }
}