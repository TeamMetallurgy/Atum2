package com.teammetallurgy.atum.world.biome;

import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumFeatures;
import com.teammetallurgy.atum.world.gen.AtumSurfaceBuilders;
import com.teammetallurgy.atum.world.gen.feature.config.DoubleBlockStateFeatureConfig;
import net.minecraft.block.Blocks;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.placement.AtSurfaceWithExtraConfig;
import net.minecraft.world.gen.placement.ChanceConfig;
import net.minecraft.world.gen.placement.FrequencyConfig;
import net.minecraft.world.gen.placement.Placement;

import static net.minecraft.world.gen.GenerationStage.Decoration.VEGETAL_DECORATION;

public class DeadOasisBiome extends AtumBiome {

    public DeadOasisBiome() {
        super(new Builder("dead_oasis", 0).setHeightVariation(0.0F).setBiomeBlocks(AtumSurfaceBuilders.GRAVEL_CRACKED));
        this.deadwoodRarity = 0.0D;
        super.addDefaultSpawns(this);
        super.addCamelSpawning(this);
        this.addFeature(GenerationStage.Decoration.LOCAL_MODIFICATIONS, AtumFeatures.OASIS_POND.withConfiguration(new DoubleBlockStateFeatureConfig(Blocks.AIR.getDefaultState(), AtumBlocks.LIMESTONE_GRAVEL.getDefaultState())).withPlacement(Placement.WATER_LAKE.configure(new ChanceConfig(1))));
        this.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration(AtumFeatures.DEAD_GRASS_CONFIG).withPlacement(Placement.COUNT_HEIGHTMAP.configure(new FrequencyConfig(5))));
        this.addFeature(VEGETAL_DECORATION, AtumFeatures.PALM_TREE.withConfiguration(AtumFeatures.DEAD_PALM_TREE_CONFIG).withPlacement(Placement.COUNT_EXTRA_HEIGHTMAP.configure(new AtSurfaceWithExtraConfig(1, 0.1F, 1))));
        AtumFeatures.Default.addCarvers(this);
        AtumFeatures.Default.addSprings(this);
        AtumFeatures.Default.addMaterialPockets(this);
        AtumFeatures.Default.addStoneVariants(this);
        AtumFeatures.Default.addOres(this);
        AtumFeatures.Default.addInfestedLimestone(this);
        AtumFeatures.Default.addShrubs(this);
        AtumFeatures.Default.addFossils(this);
        AtumFeatures.Default.addDungeon(this);
    }

    @Override
    public int getFoliageColor() {
        return 10189386;
    }

    @Override
    public int getGrassColor(double x, double z) {
        return 10189386;
    }
}