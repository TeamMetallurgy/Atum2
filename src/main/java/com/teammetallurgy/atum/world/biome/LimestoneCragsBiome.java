package com.teammetallurgy.atum.world.biome;

import com.teammetallurgy.atum.init.AtumFeatures;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.placement.FrequencyConfig;
import net.minecraft.world.gen.placement.Placement;

public class LimestoneCragsBiome extends AtumBiome {

    public LimestoneCragsBiome() {
        super(new Builder("limestone_crags", 3).setBaseHeight(0.225F).setHeightVariation(0.45000002F));
        this.deadwoodRarity = 0.12D;
        this.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, AtumFeatures.LIMESTONE_SPIKE.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.COUNT_HEIGHTMAP.configure(new FrequencyConfig(3))));
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
        AtumFeatures.Default.addTomb(this);
        AtumFeatures.Default.addPyramid(this);
    }
}