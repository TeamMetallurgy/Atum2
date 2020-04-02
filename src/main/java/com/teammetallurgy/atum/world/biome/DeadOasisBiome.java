package com.teammetallurgy.atum.world.biome;

import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.world.gen.AtumSurfaceBuilders;
import com.teammetallurgy.atum.world.gen.feature.AtumFeatures;
import com.teammetallurgy.atum.world.gen.feature.config.DoubleBlockStateFeatureConfig;
import net.minecraft.block.Blocks;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.placement.ChanceConfig;
import net.minecraft.world.gen.placement.FrequencyConfig;
import net.minecraft.world.gen.placement.Placement;

public class DeadOasisBiome extends AtumBiome {

    public DeadOasisBiome() {
        super(new Builder("dead_oasis", 0).setHeightVariation(0.0F).setBiomeBlocks(AtumSurfaceBuilders.GRAVEL_CRACKED));
        this.deadwoodRarity = 0.0D;
        super.addDefaultSpawns(this);
        super.addCamelSpawning(this);
        this.addFeature(GenerationStage.Decoration.LOCAL_MODIFICATIONS, AtumFeatures.OASIS_POND.withConfiguration(new DoubleBlockStateFeatureConfig(Blocks.AIR.getDefaultState(), AtumBlocks.LIMESTONE_GRAVEL.getDefaultState())).withPlacement(Placement.WATER_LAKE.configure(new ChanceConfig(1))));
        this.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration(AtumFeatures.DEAD_OASIS_GRASS_CONFIG).withPlacement(Placement.COUNT_HEIGHTMAP.configure(new FrequencyConfig(2))));

    }

    /*@Override
    public void decorate(@Nonnull World world, @Nonnull Random random, @Nonnull BlockPos pos) {
        if (random.nextFloat() <= 0.70F) {
            new WorldGenPalm(true, 5, AtumBlocks.DEADWOOD_LOG.getDefaultState().with(BlockDeadwood.HAS_SCARAB, true), PalmLeavesBlock.getLeave(BlockAtumPlank.WoodType.DEADWOOD).getDefaultState().with(PalmLeavesBlock.CHECK_DECAY, false), false).generate(world, random, world.getHeight(pos.add(x, 0, z)));
        }
    }
    */

    @Override
    public int getFoliageColor() {
        return 10189386;
    }

    @Override
    public int getGrassColor(double x, double z) {
        return 10189386;
    }
}