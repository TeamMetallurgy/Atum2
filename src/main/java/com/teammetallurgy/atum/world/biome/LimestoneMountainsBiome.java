package com.teammetallurgy.atum.world.biome;

import com.teammetallurgy.atum.init.AtumFeatures;
import com.teammetallurgy.atum.world.gen.AtumSurfaceBuilders;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.IFeatureConfig;

public class LimestoneMountainsBiome extends AtumBiome {

    public LimestoneMountainsBiome() {
        super(new Builder("limestone_mountains", 5).setBaseHeight(1.5F).setHeightVariation(0.6F).setBiomeBlocks(AtumSurfaceBuilders.SANDY_LIMESTONE));
        super.addDefaultSpawns(this);
        super.addDesertWolfSpawning(this);
        this.addStructure(AtumFeatures.LIGHTHOUSE.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
        this.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, AtumFeatures.LIGHTHOUSE.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
        AtumFeatures.Default.addDeadwoodTrees(this, 0, 0.4F, 1);
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
        AtumFeatures.Default.addMineshaft(this, true);
    }
}