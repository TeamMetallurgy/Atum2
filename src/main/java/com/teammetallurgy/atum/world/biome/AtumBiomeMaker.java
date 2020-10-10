package com.teammetallurgy.atum.world.biome;

import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumEntities;
import com.teammetallurgy.atum.init.AtumFeatures;
import com.teammetallurgy.atum.init.AtumStructures;
import com.teammetallurgy.atum.misc.AtumConfig;
import com.teammetallurgy.atum.world.DimensionHelper;
import com.teammetallurgy.atum.world.gen.AtumSurfaceBuilders;
import com.teammetallurgy.atum.world.gen.carver.AtumCarvers;
import com.teammetallurgy.atum.world.gen.feature.config.DoubleBlockStateFeatureConfig;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeAmbience;
import net.minecraft.world.biome.BiomeGenerationSettings;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.blockplacer.SimpleBlockPlacer;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.BlockClusterFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.Features;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.placement.AtSurfaceWithExtraConfig;
import net.minecraft.world.gen.placement.ChanceConfig;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.placement.Placement;

import javax.annotation.Nonnull;

import static net.minecraft.world.gen.GenerationStage.Decoration.VEGETAL_DECORATION;

public class AtumBiomeMaker { //TODO Clean this up

    public static Biome makeDeadOasis() {
        BiomeGenerationSettings.Builder biomeGen = (new BiomeGenerationSettings.Builder().withSurfaceBuilder(AtumSurfaceBuilders.GRAVEL_CRACKED));
        biomeGen.withFeature(GenerationStage.Decoration.LOCAL_MODIFICATIONS, AtumFeatures.OASIS_POND.withConfiguration(new DoubleBlockStateFeatureConfig(Blocks.AIR.getDefaultState(), AtumBlocks.LIMESTONE_GRAVEL.getDefaultState())).withPlacement(Placement.WATER_LAKE.configure(new ChanceConfig(1))));
        biomeGen.withFeature(VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration(AtumFeatures.DEAD_GRASS_CONFIG).withPlacement(Features.Placements.PATCH_PLACEMENT).func_242731_b(5));
        biomeGen.withFeature(VEGETAL_DECORATION, AtumFeatures.PALM_TREE.withConfiguration(AtumFeatures.DEAD_PALM_TREE_CONFIG).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).withPlacement(Placement.field_242902_f.configure(new AtSurfaceWithExtraConfig(1, 0.1F, 1))));
        MobSpawnInfo.Builder spawnInfo = new MobSpawnInfo.Builder();
        addDefaultSpawns(spawnInfo);
        addCamelSpawning(spawnInfo); //TODO Test
        AtumFeatures.Default.addCarvers(biomeGen);
        AtumFeatures.Default.addSprings(biomeGen);
        AtumFeatures.Default.addMaterialPockets(biomeGen);
        AtumFeatures.Default.addStoneVariants(biomeGen);
        AtumFeatures.Default.addOres(biomeGen);
        AtumFeatures.Default.addInfestedLimestone(biomeGen);
        AtumFeatures.Default.addShrubs(biomeGen);
        AtumFeatures.Default.addFossils(biomeGen);
        AtumFeatures.Default.addDungeon(biomeGen);
        AtumFeatures.Default.addTomb(biomeGen);
        AtumFeatures.Default.addPyramid(biomeGen);
        AtumFeatures.Default.addRuins(biomeGen);
        AtumFeatures.Default.addMineshaft(biomeGen, false);

        return new Builder().setHeightVariation(0.0F).withGenerationSettings(biomeGen.build()).withMobSpawnSettings(spawnInfo.copy()).setEffects(Builder.getBaseEffects().withFoliageColor(10189386).withGrassColor(10189386).build()).build();
    }

    public static Biome makeDeadwoodForest() {
        BiomeGenerationSettings.Builder biomeGen = (new BiomeGenerationSettings.Builder().withSurfaceBuilder(AtumSurfaceBuilders.SANDY));
        biomeGen.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, AtumFeatures.ANPUTS_FINGERS.withConfiguration(AtumFeatures.ANPUTS_FINGERS_CONFIG).withPlacement(Features.Placements.PATCH_PLACEMENT).withPlacement(Placement.field_242906_k.configure(IPlacementConfig.NO_PLACEMENT_CONFIG)).func_242731_b(100)); //TODO TEST
        MobSpawnInfo.Builder spawnInfo = new MobSpawnInfo.Builder();
        addDefaultSpawns(spawnInfo);
        AtumFeatures.Default.addDeadwoodTrees(biomeGen, 20, 0.25F, 3);
        AtumFeatures.Default.addCarvers(biomeGen);
        AtumFeatures.Default.addSprings(biomeGen);
        AtumFeatures.Default.addMaterialPockets(biomeGen);
        AtumFeatures.Default.addStoneVariants(biomeGen);
        AtumFeatures.Default.addOres(biomeGen);
        AtumFeatures.Default.addInfestedLimestone(biomeGen);
        AtumFeatures.Default.addShrubs(biomeGen);
        AtumFeatures.Default.addFossils(biomeGen);
        AtumFeatures.Default.addDungeon(biomeGen);
        AtumFeatures.Default.addTomb(biomeGen);
        AtumFeatures.Default.addPyramid(biomeGen);
        AtumFeatures.Default.addRuins(biomeGen);
        AtumFeatures.Default.addMineshaft(biomeGen, false);
        return new Builder().withGenerationSettings(biomeGen.build()).withMobSpawnSettings(spawnInfo.copy()).build();
    }

    public static Biome makeDriedRiver() {
        BiomeGenerationSettings.Builder biomeGen = (new BiomeGenerationSettings.Builder().withSurfaceBuilder(AtumSurfaceBuilders.GRAVEL_CRACKED));
        AtumFeatures.Default.addCarvers(biomeGen);
        AtumFeatures.Default.addSprings(biomeGen);
        AtumFeatures.Default.addStoneVariants(biomeGen);
        AtumFeatures.Default.addOres(biomeGen);
        AtumFeatures.Default.addMaterialPockets(biomeGen);
        AtumFeatures.Default.addInfestedLimestone(biomeGen);
        AtumFeatures.Default.addShrubs(biomeGen);
        AtumFeatures.Default.addFossils(biomeGen);
        AtumFeatures.Default.addDungeon(biomeGen);
        AtumFeatures.Default.addTomb(biomeGen);
        AtumFeatures.Default.addMineshaft(biomeGen, false);
        return new Builder().withGenerationSettings(biomeGen.build()).withMobSpawnSettings(new MobSpawnInfo.Builder().copy()).setBaseHeight(-0.5F).setHeightVariation(0.0F).build();
    }

    public static Biome makeLimestoneCrags() {
        BiomeGenerationSettings.Builder biomeGen = (new BiomeGenerationSettings.Builder().withSurfaceBuilder(AtumSurfaceBuilders.SANDY));
        biomeGen.withFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, AtumFeatures.LIMESTONE_SPIKE.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).func_242731_b(3));
        AtumFeatures.Default.addDeadwoodTrees(biomeGen, 0, 0.2F, 1);
        MobSpawnInfo.Builder spawnInfo = new MobSpawnInfo.Builder();
        addDefaultSpawns(spawnInfo);
        addDesertWolfSpawning(new MobSpawnInfo.Builder()); //TODO Test
        AtumFeatures.Default.addCarvers(biomeGen);
        AtumFeatures.Default.addSprings(biomeGen);
        AtumFeatures.Default.addMaterialPockets(biomeGen);
        AtumFeatures.Default.addStoneVariants(biomeGen);
        AtumFeatures.Default.addOres(biomeGen);
        AtumFeatures.Default.addEmeraldOre(biomeGen);
        AtumFeatures.Default.addInfestedLimestone(biomeGen);
        AtumFeatures.Default.addFossils(biomeGen);
        AtumFeatures.Default.addDungeon(biomeGen);
        AtumFeatures.Default.addTomb(biomeGen);
        AtumFeatures.Default.addMineshaft(biomeGen, false);
        return new Builder().withGenerationSettings(biomeGen.build()).withMobSpawnSettings(spawnInfo.copy()).setBaseHeight(0.225F).setHeightVariation(0.45F).build();
    }

    public static Biome makeLimestoneMountain() {
        BiomeGenerationSettings.Builder biomeGen = (new BiomeGenerationSettings.Builder().withSurfaceBuilder(AtumSurfaceBuilders.SANDY_LIMESTONE));
        if (AtumConfig.WORLD_GEN.lighthouseEnabled.get()) {
            biomeGen.withStructure(AtumStructures.LIGHTHOUSE_FEATURE);
        }
        AtumFeatures.Default.addDeadwoodTrees(biomeGen, 0, 0.1F, 1);
        MobSpawnInfo.Builder spawnInfo = new MobSpawnInfo.Builder();
        addDefaultSpawns(spawnInfo);
        addDesertWolfSpawning(new MobSpawnInfo.Builder()); //TODO Test
        AtumFeatures.Default.addCarvers(biomeGen);
        AtumFeatures.Default.addSprings(biomeGen);
        AtumFeatures.Default.addMaterialPockets(biomeGen);
        AtumFeatures.Default.addStoneVariants(biomeGen);
        AtumFeatures.Default.addOres(biomeGen);
        AtumFeatures.Default.addEmeraldOre(biomeGen);
        AtumFeatures.Default.addInfestedLimestone(biomeGen);
        AtumFeatures.Default.addFossils(biomeGen);
        AtumFeatures.Default.addDungeon(biomeGen);
        AtumFeatures.Default.addTomb(biomeGen);
        AtumFeatures.Default.addRuins(biomeGen);
        AtumFeatures.Default.addMineshaft(biomeGen, true);
        return new Builder().withGenerationSettings(biomeGen.build()).withMobSpawnSettings(spawnInfo.copy()).setBaseHeight(1.5F).setHeightVariation(0.6F).build();
    }

    public static Biome makeOasis() {
        BiomeGenerationSettings.Builder biomeGen = (new BiomeGenerationSettings.Builder().withSurfaceBuilder(AtumSurfaceBuilders.OASIS));
        biomeGen.withFeature(GenerationStage.Decoration.LOCAL_MODIFICATIONS, AtumFeatures.OASIS_POND.withConfiguration(new DoubleBlockStateFeatureConfig(Blocks.WATER.getDefaultState(), AtumBlocks.FERTILE_SOIL.getDefaultState())).withPlacement(Placement.WATER_LAKE.configure(new ChanceConfig(1))));
        biomeGen.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration(AtumFeatures.OASIS_GRASS_CONFIG).withPlacement(Features.Placements.PATCH_PLACEMENT).func_242731_b(7));
        //TODO Fix Papyrus feature. Breaks Oasis when enabled
        //biomeGen.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration(AtumFeatures.PAPYRUS_CONFIG).withPlacement(Features.Placements.PATCH_PLACEMENT).func_242731_b(250));
        biomeGen.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration((new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(Blocks.LILY_PAD.getDefaultState()), SimpleBlockPlacer.PLACER)).tries(11).build()).withPlacement(Features.Placements.PATCH_PLACEMENT).func_242731_b(5));
        biomeGen.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, AtumFeatures.PALM_TREE.withConfiguration(AtumFeatures.PALM_TREE_CONFIG).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).withPlacement(Placement.field_242902_f.configure(new AtSurfaceWithExtraConfig(1, 0.1F, 1))));
        biomeGen.withCarver(GenerationStage.Carving.AIR, AtumCarvers.CAVE_CONFIGURED);
        MobSpawnInfo.Builder spawnInfo = new MobSpawnInfo.Builder();
        addCamelSpawning(spawnInfo); //TODO Test
        AtumFeatures.Default.addCarvers(biomeGen);
        AtumFeatures.Default.addMaterialPockets(biomeGen);
        AtumFeatures.Default.addStoneVariants(biomeGen);
        AtumFeatures.Default.addOres(biomeGen);
        AtumFeatures.Default.addEmeraldOre(biomeGen);
        AtumFeatures.Default.addInfestedLimestone(biomeGen);
        AtumFeatures.Default.addFossils(biomeGen);
        AtumFeatures.Default.addMineshaft(biomeGen, false);
        return new Builder().setHeightVariation(0.0F).withGenerationSettings(biomeGen.build()).withMobSpawnSettings(spawnInfo.copy()).setEffects(Builder.getBaseEffects().withFoliageColor(11987573).withGrassColor(11987573).build()).build();
    }

    public static Biome makeSandDunes() {
        BiomeGenerationSettings.Builder biomeGen = (new BiomeGenerationSettings.Builder().withSurfaceBuilder(AtumSurfaceBuilders.SANDY));
        AtumFeatures.Default.addDeadwoodTrees(biomeGen, 0, 0.01F, 1);
        MobSpawnInfo.Builder spawnInfo = new MobSpawnInfo.Builder();
        addDefaultSpawns(spawnInfo);
        addCamelSpawning(spawnInfo); //TODO Test
        AtumFeatures.Default.addCarvers(biomeGen);
        AtumFeatures.Default.addSprings(biomeGen);
        AtumFeatures.Default.addMaterialPockets(biomeGen);
        AtumFeatures.Default.addStoneVariants(biomeGen);
        AtumFeatures.Default.addOres(biomeGen);
        AtumFeatures.Default.addInfestedLimestone(biomeGen);
        AtumFeatures.Default.addShrubs(biomeGen);
        AtumFeatures.Default.addFossils(biomeGen);
        AtumFeatures.Default.addDungeon(biomeGen);
        AtumFeatures.Default.addTomb(biomeGen);
        AtumFeatures.Default.addPyramid(biomeGen);
        AtumFeatures.Default.addRuins(biomeGen);
        AtumFeatures.Default.addMineshaft(biomeGen, false);
        return new Builder().withGenerationSettings(biomeGen.build()).withMobSpawnSettings(spawnInfo.copy()).setBaseHeight(0.175F).setHeightVariation(0.2F).build();
    }

    public static Biome makeSandHills() {
        BiomeGenerationSettings.Builder biomeGen = (new BiomeGenerationSettings.Builder().withSurfaceBuilder(AtumSurfaceBuilders.SANDY));
        AtumFeatures.Default.addDeadwoodTrees(biomeGen, 0, 0.08F, 1);
        MobSpawnInfo.Builder spawnInfo = new MobSpawnInfo.Builder();
        addDefaultSpawns(spawnInfo);
        addDesertWolfSpawning(spawnInfo); //TODO Test
        AtumFeatures.Default.addCarvers(biomeGen);
        AtumFeatures.Default.addSprings(biomeGen);
        AtumFeatures.Default.addMaterialPockets(biomeGen);
        AtumFeatures.Default.addStoneVariants(biomeGen);
        AtumFeatures.Default.addOres(biomeGen);
        AtumFeatures.Default.addEmeraldOre(biomeGen);
        AtumFeatures.Default.addInfestedLimestone(biomeGen);
        AtumFeatures.Default.addFossils(biomeGen);
        AtumFeatures.Default.addDungeon(biomeGen);
        AtumFeatures.Default.addTomb(biomeGen);
        AtumFeatures.Default.addRuins(biomeGen);
        AtumFeatures.Default.addMineshaft(biomeGen, false);
        return new Builder().withGenerationSettings(biomeGen.build()).withMobSpawnSettings(spawnInfo.copy()).setBaseHeight(0.3F).setHeightVariation(0.3F).build();
    }

    public static Biome makeSandPlains() {
        BiomeGenerationSettings.Builder biomeGen = (new BiomeGenerationSettings.Builder().withSurfaceBuilder(AtumSurfaceBuilders.SANDY));
        biomeGen.withStructure(AtumStructures.GIRAFI_TOMB_FEATURE);
        AtumFeatures.Default.addDeadwoodTrees(biomeGen, 0, 0.025F, 1);
        MobSpawnInfo.Builder spawnInfo = new MobSpawnInfo.Builder();
        addDefaultSpawns(spawnInfo);
        addCamelSpawning(new MobSpawnInfo.Builder()); //TODO Test
        AtumFeatures.Default.addCarvers(biomeGen);
        AtumFeatures.Default.addSprings(biomeGen);
        AtumFeatures.Default.addMaterialPockets(biomeGen);
        AtumFeatures.Default.addStoneVariants(biomeGen);
        AtumFeatures.Default.addOres(biomeGen);
        AtumFeatures.Default.addInfestedLimestone(biomeGen);
        AtumFeatures.Default.addShrubs(biomeGen);
        AtumFeatures.Default.addFossils(biomeGen);
        AtumFeatures.Default.addDungeon(biomeGen);
        AtumFeatures.Default.addTomb(biomeGen);
        AtumFeatures.Default.addPyramid(biomeGen);
        AtumFeatures.Default.addRuins(biomeGen);
        AtumFeatures.Default.addMineshaft(biomeGen, false);
        return new Builder().withGenerationSettings(biomeGen.build()).withMobSpawnSettings(spawnInfo.copy()).build();
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
            return new BiomeAmbience.Builder().setFogColor(13876389).setWaterColor(4159204).setWaterFogColor(329011).withSkyColor(DimensionHelper.getSkyColorWithTemperatureModifier(2.0F)).withGrassColor(12889745).withFoliageColor(12889745);
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

        @Override
        @Nonnull
        public Builder withGenerationSettings(@Nonnull BiomeGenerationSettings biomeGen) {
            super.withGenerationSettings(biomeGen);
            return this;
        }

        @Override
        @Nonnull
        public Builder withMobSpawnSettings(@Nonnull MobSpawnInfo spawnInfo) {
            super.withMobSpawnSettings(spawnInfo);
            return this;
        }
    }
}