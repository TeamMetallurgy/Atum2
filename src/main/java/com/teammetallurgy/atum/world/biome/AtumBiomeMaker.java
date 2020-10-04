package com.teammetallurgy.atum.world.biome;

import com.teammetallurgy.atum.blocks.SandLayersBlock;
import com.teammetallurgy.atum.init.AtumBiomes;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumEntities;
import com.teammetallurgy.atum.init.AtumFeatures;
import com.teammetallurgy.atum.misc.AtumConfig;
import com.teammetallurgy.atum.world.gen.AtumSurfaceBuilders;
import com.teammetallurgy.atum.world.gen.carver.AtumCarvers;
import com.teammetallurgy.atum.world.gen.feature.config.DoubleBlockStateFeatureConfig;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.biome.*;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.blockplacer.SimpleBlockPlacer;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placement.AtSurfaceWithExtraConfig;
import net.minecraft.world.gen.placement.ChanceConfig;
import net.minecraft.world.gen.placement.Placement;

import javax.annotation.Nonnull;

import static net.minecraft.world.gen.GenerationStage.Decoration.VEGETAL_DECORATION;

public class AtumBiomeMaker { //TODO Clean this up

    public static Biome makeDeadOasis() {
        BiomeGenerationSettings.Builder builder = (new BiomeGenerationSettings.Builder().withSurfaceBuilder(AtumSurfaceBuilders.GRAVEL_CRACKED));
        builder.withFeature(GenerationStage.Decoration.LOCAL_MODIFICATIONS, AtumFeatures.OASIS_POND.withConfiguration(new DoubleBlockStateFeatureConfig(Blocks.AIR.getDefaultState(), AtumBlocks.LIMESTONE_GRAVEL.getDefaultState())).withPlacement(Placement.WATER_LAKE.configure(new ChanceConfig(1))));
        builder.withFeature(VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration(AtumFeatures.DEAD_GRASS_CONFIG).withPlacement(Features.Placements.PATCH_PLACEMENT).func_242731_b(5));
        builder.withFeature(VEGETAL_DECORATION, AtumFeatures.PALM_TREE.withConfiguration(AtumFeatures.DEAD_PALM_TREE_CONFIG).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).withPlacement(Placement.field_242902_f.configure(new AtSurfaceWithExtraConfig(1, 0.1F, 1))));
        addCamelSpawning(new MobSpawnInfo.Builder()); //TODO Test
        AtumFeatures.Default.addCarvers(builder);
        AtumFeatures.Default.addSprings(builder);
        AtumFeatures.Default.addMaterialPockets(builder);
        AtumFeatures.Default.addStoneVariants(builder);
        AtumFeatures.Default.addOres(builder);
        AtumFeatures.Default.addInfestedLimestone(builder);
        AtumFeatures.Default.addShrubs(builder);
        AtumFeatures.Default.addFossils(builder);
        AtumFeatures.Default.addDungeon(builder);
        AtumFeatures.Default.addTomb(builder);
        AtumFeatures.Default.addPyramid(builder);
        AtumFeatures.Default.addRuins(builder);
        AtumFeatures.Default.addMineshaft(builder, false);

        return makeBaseAtumBiome(new Builder().setHeightVariation(0.0F).setEffects(new BiomeAmbience.Builder().withFoliageColor(10189386).withGrassColor(10189386).build()),builder).build();
    }

    public static Biome makeDeadwoodForest() {
        BiomeGenerationSettings.Builder builder = (new BiomeGenerationSettings.Builder());
        builder.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration(AtumFeatures.DEAD_GRASS_CONFIG).withPlacement(Placement.COUNT_HEIGHTMAP.configure(new FrequencyConfig(10))));
        builder.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, AtumFeatures.ANPUTS_FINGERS.withConfiguration(AtumFeatures.ANPUTS_FINGERS_CONFIG).withPlacement(Placement.COUNT_HEIGHTMAP_32.configure(new FrequencyConfig(100))));
        AtumFeatures.Default.addDeadwoodTrees(builder, 20, 0.25F, 3);
        AtumFeatures.Default.addCarvers(builder);
        AtumFeatures.Default.addSprings(builder);
        AtumFeatures.Default.addMaterialPockets(builder);
        AtumFeatures.Default.addStoneVariants(builder);
        AtumFeatures.Default.addOres(builder);
        AtumFeatures.Default.addInfestedLimestone(builder);
        AtumFeatures.Default.addShrubs(builder);
        AtumFeatures.Default.addFossils(builder);
        AtumFeatures.Default.addDungeon(builder);
        AtumFeatures.Default.addTomb(builder);
        AtumFeatures.Default.addPyramid(builder);
        AtumFeatures.Default.addRuins(builder);
        AtumFeatures.Default.addMineshaft(builder, false);
        return makeBaseAtumBiome(new Builder(), builder).build();
    }

    public static Biome makeDriedRiver() {
        BiomeGenerationSettings.Builder builder = (new BiomeGenerationSettings.Builder().withSurfaceBuilder(AtumSurfaceBuilders.GRAVEL_CRACKED));
        AtumFeatures.Default.addCarvers(builder);
        AtumFeatures.Default.addSprings(builder);
        AtumFeatures.Default.addStoneVariants(builder);
        AtumFeatures.Default.addOres(builder);
        AtumFeatures.Default.addMaterialPockets(builder);
        AtumFeatures.Default.addInfestedLimestone(builder);
        AtumFeatures.Default.addShrubs(builder);
        AtumFeatures.Default.addFossils(builder);
        AtumFeatures.Default.addDungeon(builder);
        AtumFeatures.Default.addTomb(builder);
        AtumFeatures.Default.addMineshaft(builder, false);
        return makeBaseAtumBiome(new Builder().setBaseHeight(-0.5F).setHeightVariation(0.0F), builder).build();
    }

    public static Biome makeLimestoneCrags() {
        BiomeGenerationSettings.Builder builder = (new BiomeGenerationSettings.Builder());
        builder.withFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, AtumFeatures.LIMESTONE_SPIKE.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.COUNT_HEIGHTMAP.configure(new FrequencyConfig(3))));
        AtumFeatures.Default.addDeadwoodTrees(builder, 0, 0.2F, 1);
        addDesertWolfSpawning(new MobSpawnInfo.Builder()); //TODO Test
        AtumFeatures.Default.addCarvers(builder);
        AtumFeatures.Default.addSprings(builder);
        AtumFeatures.Default.addMaterialPockets(builder);
        AtumFeatures.Default.addStoneVariants(builder);
        AtumFeatures.Default.addOres(builder);
        AtumFeatures.Default.addEmeraldOre(builder);
        AtumFeatures.Default.addInfestedLimestone(builder);
        AtumFeatures.Default.addFossils(builder);
        AtumFeatures.Default.addDungeon(builder);
        AtumFeatures.Default.addTomb(builder);
        AtumFeatures.Default.addMineshaft(builder, false);
        return makeBaseAtumBiome(new Builder().setBaseHeight(0.225F).setHeightVariation(0.45F), builder).build();
    }

    public static Biome makeLimestoneMountain() {
        BiomeGenerationSettings.Builder builder = (new BiomeGenerationSettings.Builder().withSurfaceBuilder(AtumSurfaceBuilders.SANDY_LIMESTONE));
        if (AtumConfig.WORLD_GEN.lighthouseEnabled.get()) {
            builder.withStructure(AtumFeatures.LIGHTHOUSE_FEATURE);
        }
        AtumFeatures.Default.addDeadwoodTrees(builder, 0, 0.1F, 1);
        addDesertWolfSpawning(new MobSpawnInfo.Builder()); //TODO Test
        AtumFeatures.Default.addCarvers(builder);
        AtumFeatures.Default.addSprings(builder);
        AtumFeatures.Default.addMaterialPockets(builder);
        AtumFeatures.Default.addStoneVariants(builder);
        AtumFeatures.Default.addOres(builder);
        AtumFeatures.Default.addEmeraldOre(builder);
        AtumFeatures.Default.addInfestedLimestone(builder);
        AtumFeatures.Default.addFossils(builder);
        AtumFeatures.Default.addDungeon(builder);
        AtumFeatures.Default.addTomb(builder);
        AtumFeatures.Default.addRuins(builder);
        AtumFeatures.Default.addMineshaft(builder, true);
        return makeBaseAtumBiome(new Builder().setBaseHeight(1.5F).setHeightVariation(0.6F), builder).build();
    }

    public static Biome makeOasis() {
        BiomeGenerationSettings.Builder builder = (new BiomeGenerationSettings.Builder().withSurfaceBuilder(AtumSurfaceBuilders.OASIS)));
        builder.withFeature(GenerationStage.Decoration.LOCAL_MODIFICATIONS, AtumFeatures.OASIS_POND.withConfiguration(new DoubleBlockStateFeatureConfig(Blocks.WATER.getDefaultState(), AtumBlocks.FERTILE_SOIL.getDefaultState())).withPlacement(Placement.WATER_LAKE.configure(new ChanceConfig(1))));
        builder.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration(AtumFeatures.OASIS_GRASS_CONFIG).withPlacement(Placement.COUNT_HEIGHTMAP.configure(new FrequencyConfig(7))));
        builder.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration(AtumFeatures.PAPYRUS_CONFIG).withPlacement(Placement.COUNT_HEIGHTMAP_DOUBLE.configure(new FrequencyConfig(250))));
        builder.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration((new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(Blocks.LILY_PAD.getDefaultState()), SimpleBlockPlacer.PLACER)).tries(11).build()).withPlacement(Features.Placements.PATCH_PLACEMENT).func_242731_b(5));
        builder.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, AtumFeatures.PALM_TREE.withConfiguration(AtumFeatures.PALM_TREE_CONFIG).withPlacement(Placement.COUNT_EXTRA_HEIGHTMAP.configure(new AtSurfaceWithExtraConfig(1, 0.1F, 1))));
        builder.withCarver(GenerationStage.Carving.AIR, AtumCarvers.CAVE_CONFIGURED);
        addCamelSpawning(new MobSpawnInfo.Builder()); //TODO Test
        AtumFeatures.Default.addCarvers(builder);
        AtumFeatures.Default.addMaterialPockets(builder);
        AtumFeatures.Default.addStoneVariants(builder);
        AtumFeatures.Default.addOres(builder);
        AtumFeatures.Default.addEmeraldOre(builder);
        AtumFeatures.Default.addInfestedLimestone(builder);
        AtumFeatures.Default.addFossils(builder);
        AtumFeatures.Default.addMineshaft(builder, false);
        return makeBaseAtumBiome(new Builder().setHeightVariation(0.0F).setEffects(new BiomeAmbience.Builder().withFoliageColor(11987573).withGrassColor(11987573).build()),builder).build();
    }

    public static Biome makeSandDunes() {
        BiomeGenerationSettings.Builder builder = (new BiomeGenerationSettings.Builder());
        AtumFeatures.Default.addDeadwoodTrees(builder, 0, 0.01F, 1);
        addCamelSpawning(new MobSpawnInfo.Builder()); //TODO Test
        AtumFeatures.Default.addCarvers(builder);
        AtumFeatures.Default.addSprings(builder);
        AtumFeatures.Default.addMaterialPockets(builder);
        AtumFeatures.Default.addStoneVariants(builder);
        AtumFeatures.Default.addOres(builder);
        AtumFeatures.Default.addInfestedLimestone(builder);
        AtumFeatures.Default.addShrubs(builder);
        AtumFeatures.Default.addFossils(builder);
        AtumFeatures.Default.addDungeon(builder);
        AtumFeatures.Default.addTomb(builder);
        AtumFeatures.Default.addPyramid(builder);
        AtumFeatures.Default.addRuins(builder);
        AtumFeatures.Default.addMineshaft(builder, false);
        return makeBaseAtumBiome(new Builder().setBaseHeight(0.175F).setHeightVariation(0.2F), builder).build();
    }

    public static Biome makeSandHills() {
        BiomeGenerationSettings.Builder builder = (new BiomeGenerationSettings.Builder());
        AtumFeatures.Default.addDeadwoodTrees(builder, 0, 0.08F, 1);
        addDesertWolfSpawning(new MobSpawnInfo.Builder()); //TODO Test
        AtumFeatures.Default.addCarvers(builder);
        AtumFeatures.Default.addSprings(builder);
        AtumFeatures.Default.addMaterialPockets(builder);
        AtumFeatures.Default.addStoneVariants(builder);
        AtumFeatures.Default.addOres(builder);
        AtumFeatures.Default.addEmeraldOre(builder);
        AtumFeatures.Default.addInfestedLimestone(builder);
        AtumFeatures.Default.addFossils(builder);
        AtumFeatures.Default.addDungeon(builder);
        AtumFeatures.Default.addTomb(builder);
        AtumFeatures.Default.addRuins(builder);
        AtumFeatures.Default.addMineshaft(builder, false);
        return makeBaseAtumBiome(new Builder().setBaseHeight(0.3F).setHeightVariation(0.3F), builder).build();
    }

    public static Biome makeSandPlains() {
        BiomeGenerationSettings.Builder builder = (new BiomeGenerationSettings.Builder());
        builder.withStructure(AtumFeatures.GIRAFI_TOMB_FEATURE);
        AtumFeatures.Default.addDeadwoodTrees(builder, 0, 0.025F, 1);
        addCamelSpawning(new MobSpawnInfo.Builder()); //TODO Test
        AtumFeatures.Default.addCarvers(builder);
        AtumFeatures.Default.addSprings(builder);
        AtumFeatures.Default.addMaterialPockets(builder);
        AtumFeatures.Default.addStoneVariants(builder);
        AtumFeatures.Default.addOres(builder);
        AtumFeatures.Default.addInfestedLimestone(builder);
        AtumFeatures.Default.addShrubs(builder);
        AtumFeatures.Default.addFossils(builder);
        AtumFeatures.Default.addDungeon(builder);
        AtumFeatures.Default.addTomb(builder);
        AtumFeatures.Default.addPyramid(builder);
        AtumFeatures.Default.addRuins(builder);
        AtumFeatures.Default.addMineshaft(builder, false);
        return makeBaseAtumBiome(new Builder(), builder).build();
    }

    public static Biome.Builder makeBaseAtumBiome(Builder builder, BiomeGenerationSettings.Builder genBuilder) {
        genBuilder.withSurfaceBuilder(AtumSurfaceBuilders.SANDY);
        return builder.withMobSpawnSettings(addDefaultSpawns(new MobSpawnInfo.Builder()).copy()).withGenerationSettings(genBuilder.build());
    }

    public static MobSpawnInfo.Builder addDefaultSpawns(MobSpawnInfo.Builder builder) { //TODO Test
        //Animals
        addSpawn(builder, AtumEntities.DESERT_RABBIT, 5, 2, 3, EntityClassification.CREATURE);
        addSpawn(builder, EntityType.BAT, 5, 8, 8, EntityClassification.AMBIENT);

        //Undead
        addSpawn(builder, AtumEntities.BONESTORM, 5, 1, 2, EntityClassification.MONSTER);
        addSpawn(builder, AtumEntities.FORSAKEN, 22, 1, 4, EntityClassification.MONSTER);
        addSpawn(builder, AtumEntities.MUMMY, 30, 1, 3, EntityClassification.MONSTER);
        addSpawn(builder, AtumEntities.WRAITH, 10, 1, 2, EntityClassification.MONSTER);

        //Underground
        addSpawn(builder, AtumEntities.STONEGUARD, 34, 1, 2, EntityClassification.MONSTER);
        addSpawn(builder, AtumEntities.TARANTULA, 20, 1, 3, EntityClassification.MONSTER);
        return builder;
    }

    public static void addSpawn(MobSpawnInfo.Builder builder, EntityType<?> entityType, int weight, int min, int max, EntityClassification classification) {
        ResourceLocation location = entityType.getRegistryName();
        if (location != null) {
            new AtumConfig.Mobs(AtumConfig.BUILDER, location.getPath(), min, max, weight, entityType, classification, builder); //Write config
        }
    }

    public static void addCamelSpawning(MobSpawnInfo.Builder builder) {
        addSpawn(builder, AtumEntities.CAMEL, 6, 2, 6, EntityClassification.CREATURE);
    }

    public static void addDesertWolfSpawning(MobSpawnInfo.Builder builder) {
        addSpawn(builder, AtumEntities.DESERT_WOLF, 5, 2, 4, EntityClassification.CREATURE);
    }

    public static void initMobSpawns(MobSpawnInfo.Builder builder, EntityType<?> entityType) {
        String baseCategory = AtumConfig.Mobs.MOBS;
        EntityClassification classification = AtumConfig.Mobs.ENTITY_CLASSIFICATION.get(entityType);
        if (entityType != null && entityType.getRegistryName() != null) {
            String mobName = entityType.getRegistryName().getPath();
            int weight = AtumConfig.Helper.get(baseCategory, mobName, "weight");
            int min = AtumConfig.Helper.get(baseCategory, mobName, "min");
            int max = AtumConfig.Helper.get(baseCategory, mobName, "max");
            builder.withSpawner(classification, new MobSpawnInfo.Spawners(entityType, weight, min, max)); //TODO Test
        }
    }

    //TODO Make sure all this is reimplmented correctly
    /*
    @Override
    public void decorate(@Nonnull GenerationStage.Decoration stage, @Nonnull ChunkGenerator<? extends GenerationSettings> chunkGenerator, @Nonnull IWorld world, long seed, @Nonnull SharedSeedRandom random, @Nonnull BlockPos pos) {
        super.decorate(stage, chunkGenerator, world, seed, random, pos);

        if (AtumConfig.WORLD_GEN.sandLayerEdge.get()) {
            for (int x = 0; x < 16; ++x) {
                for (int z = 0; z < 16; ++z) {
                    BlockPos height = world.getHeight(Heightmap.Type.MOTION_BLOCKING, pos.add(x, 0, z));

                    if (canPlaceSandLayer(world, height)) {
                        for (Direction facing : Direction.Plane.HORIZONTAL) {
                            BlockPos posOffset = height.offset(facing);
                            if (world.getBlockState(posOffset).isSolidSide(world, posOffset, Direction.UP)) {
                                int layers = MathHelper.nextInt(random, 1, 3);
                                world.setBlockState(height, AtumBlocks.SAND_LAYERED.getDefaultState().with(SandLayersBlock.LAYERS, layers), 2);
                            }
                        }
                    }
                }
            }
        }
    }*/

    public boolean canPlaceSandLayer(RegistryKey<Biome> biome, IWorldReader world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        BlockState stateDown = world.getBlockState(pos.down());
        return biome != AtumBiomes.OASIS
                && state.getMaterial().isReplaceable()
                && stateDown.getBlock() != AtumBlocks.LIMESTONE_CRACKED
                && Block.hasSolidSideOnTop(world, pos.down())
                && !(stateDown.getBlock() instanceof SandLayersBlock)
                && !(state.getBlock() instanceof SandLayersBlock);
    }

    public static class Builder extends Biome.Builder {

        public Builder() {
            this.precipitation(Biome.RainType.NONE);
            this.category(Biome.Category.DESERT);
            this.temperature(2.0F);
            this.downfall(0.0F);
            this.setEffects(getBaseEffects().build());
            this.setBaseHeight(0.135F);
            this.setHeightVariation(0.05F);
        }

        public static BiomeAmbience.Builder getBaseEffects() {
            return new BiomeAmbience.Builder().withGrassColor(12889745).withFoliageColor(12889745).setWaterColor(4159204).setWaterFogColor(329011);
        }

        public Builder setBaseHeight(float height) {
            this.depth(height);
            return this;
        }

        public Builder setHeightVariation(float variation) {
            this.scale(variation);
            return this;
        }

        @Override
        @Nonnull
        public Builder setEffects(@Nonnull BiomeAmbience effects) {
            super.setEffects(effects);
            return this;
        }
    }
}