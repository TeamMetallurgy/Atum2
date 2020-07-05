package com.teammetallurgy.atum.world.biome;

import com.teammetallurgy.atum.init.AtumFeatures;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.placement.FrequencyConfig;
import net.minecraft.world.gen.placement.HeightWithChanceConfig;
import net.minecraft.world.gen.placement.Placement;

public class DeadwoodForestBiome extends AtumBiome {

    public DeadwoodForestBiome() {
        super(new Builder("deadwood_forest", 10));
        super.addDefaultSpawns(this);
        //TODO Fix Anput's Fingers not generating
        this.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration(AtumFeatures.DEAD_GRASS_CONFIG).withPlacement(Placement.COUNT_HEIGHTMAP.configure(new FrequencyConfig(10))));
        this.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration(AtumFeatures.ANPUTS_FINGERS_CONFIG).withPlacement(Placement.COUNT_CHANCE_HEIGHTMAP.configure(new HeightWithChanceConfig(1, 0.10F))));
        AtumFeatures.Default.addDeadwoodTrees(this, 20, 0.25F, 3);
        AtumFeatures.Default.addCarvers(this);
        AtumFeatures.Default.addSprings(this);
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