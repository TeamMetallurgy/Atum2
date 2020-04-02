package com.teammetallurgy.atum.world.biome;

import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.world.gen.AtumSurfaceBuilders;
import com.teammetallurgy.atum.world.gen.feature.AtumFeatures;
import com.teammetallurgy.atum.world.gen.feature.config.DoubleBlockStateFeatureConfig;
import net.minecraft.block.Blocks;
import net.minecraft.world.biome.DefaultBiomeFeatures;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.placement.ChanceConfig;
import net.minecraft.world.gen.placement.FrequencyConfig;
import net.minecraft.world.gen.placement.Placement;

public class OasisBiome extends AtumBiome {

    public OasisBiome() {
        super(new Builder("oasis", 0).setHeightVariation(0.0F).setBiomeBlocks(AtumSurfaceBuilders.OASIS));
        //this.decorator.waterlilyPerChunk = 100;
        this.deadwoodRarity = 0.0D;
        super.addCamelSpawning(this);
        this.addFeature(GenerationStage.Decoration.LOCAL_MODIFICATIONS, AtumFeatures.OASIS_POND.withConfiguration(new DoubleBlockStateFeatureConfig(Blocks.WATER.getDefaultState(), AtumBlocks.FERTILE_SOIL.getDefaultState())).withPlacement(Placement.WATER_LAKE.configure(new ChanceConfig(1))));
        this.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration(AtumFeatures.OASIS_GRASS_CONFIG).withPlacement(Placement.COUNT_HEIGHTMAP.configure(new FrequencyConfig(3))));
        this.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration(AtumFeatures.PAPYRUS_CONFIG).withPlacement(Placement.COUNT_HEIGHTMAP_DOUBLE.configure(new FrequencyConfig(250))));
        this.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration(DefaultBiomeFeatures.LILY_PAD_CONFIG).withPlacement(Placement.COUNT_HEIGHTMAP_DOUBLE.configure(new FrequencyConfig(5))));
    }

    /*@Override
    public void decorate(@Nonnull World world, @Nonnull Random random, @Nonnull BlockPos pos) {
        if (random.nextFloat() <= 0.98F) {
            new WorldGenPalm(true, random.nextInt(4) + 5, true).generate(world, random, height);
        }
    }
    */

    @Override
    public int getFoliageColor() {
        return 11987573;
    }

    @Override
    public int getGrassColor(double x, double z) {
        return 11987573;
    }
}