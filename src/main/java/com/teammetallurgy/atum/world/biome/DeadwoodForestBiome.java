package com.teammetallurgy.atum.world.biome;

import com.teammetallurgy.atum.init.AtumFeatures;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.placement.FrequencyConfig;
import net.minecraft.world.gen.placement.Placement;

public class DeadwoodForestBiome extends AtumBiome {
    //private WorldGenerator anputsFingersGen = new WorldGenBush(AtumBlocks.ANPUTS_FINGERS);

    public DeadwoodForestBiome() {
        super(new Builder("deadwood_forest", 10));
        this.deadwoodRarity = 1.0D;
        super.addDefaultSpawns(this);
        this.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration(AtumFeatures.DEAD_GRASS_CONFIG).withPlacement(Placement.COUNT_HEIGHTMAP.configure(new FrequencyConfig(1))));
        AtumFeatures.Default.addCarvers(this);
        AtumFeatures.Default.addSprings(this);
        AtumFeatures.Default.addLavaLakes(this);
        AtumFeatures.Default.addMaterialPockets(this);
        AtumFeatures.Default.addStoneVariants(this);
        AtumFeatures.Default.addOres(this);
        AtumFeatures.Default.addInfestedLimestone(this);
        AtumFeatures.Default.addShrubs(this);
    }

    /*@Override
    public void decorate(@Nonnull World world, @Nonnull Random random, @Nonnull BlockPos pos) {
        super.decorate(world, random, pos);

        if (TerrainGen.decorate(world, random, new ChunkPos(pos), DecorateBiomeEvent.Decorate.EventType.SHROOM)) {
            for (int amount = 0; amount < 10; ++amount) {
                if (random.nextInt(10) == 0) {
                    int x = random.nextInt(16) + 8;
                    int z = random.nextInt(16) + 8;
                    BlockPos genPos = world.getHeight(pos.add(x, 0, z));
                    this.anputsFingersGen.generate(world, random, genPos); //TODO
                }
            }
        }
    }*/
}