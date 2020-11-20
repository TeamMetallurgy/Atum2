package com.teammetallurgy.atum.world.biome;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumEntities;
import com.teammetallurgy.atum.init.AtumFeatures;
import com.teammetallurgy.atum.init.AtumStructures;
import com.teammetallurgy.atum.misc.AtumConfig;
import com.teammetallurgy.atum.world.DimensionHelper;
import com.teammetallurgy.atum.world.gen.AtumDefaultFeatures;
import com.teammetallurgy.atum.world.gen.AtumSurfaceBuilders;
import com.teammetallurgy.atum.world.gen.carver.AtumCarvers;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.*;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.blockplacer.SimpleBlockPlacer;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.structure.StructureFeatures;
import net.minecraft.world.gen.foliageplacer.AcaciaFoliagePlacer;
import net.minecraft.world.gen.placement.*;
import net.minecraft.world.gen.trunkplacer.ForkyTrunkPlacer;

import static net.minecraft.world.gen.GenerationStage.Decoration.*;

public class AtumBiomeMaker {

    public static Biome makeDeadOasis(String biomeName) {
        BiomeGenerationSettings.Builder biomeGen = (new BiomeGenerationSettings.Builder().withSurfaceBuilder(AtumSurfaceBuilders.GRAVEL_CRACKED));
        biomeGen.withFeature(VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration(AtumFeatures.SPARSE_DRY_GRASS_CONFIG).withPlacement(Features.Placements.PATCH_PLACEMENT).func_242731_b(5));
        biomeGen.withFeature(VEGETAL_DECORATION, AtumFeatures.ATUM_TREE.withConfiguration(AtumFeatures.DEAD_PALM_TREE_CONFIG).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).withPlacement(Placement.COUNT_EXTRA.configure(new AtSurfaceWithExtraConfig(1, 0.1F, 1))));
        addDefaultSpawns(biomeName);
        addCamelSpawning(biomeName);
        AtumDefaultFeatures.addCarvers(biomeGen);
        AtumDefaultFeatures.addSandLayer(biomeGen);
        AtumDefaultFeatures.addSprings(biomeGen);
        AtumDefaultFeatures.addSurfaceLavaLake(biomeGen);
        AtumDefaultFeatures.addMaterialPockets(biomeGen);
        AtumDefaultFeatures.addStoneVariants(biomeGen);
        AtumDefaultFeatures.addOres(biomeGen);
        AtumDefaultFeatures.addInfestedLimestone(biomeGen);
        AtumDefaultFeatures.addShrubs(biomeGen);
        AtumDefaultFeatures.addFossils(biomeGen);
        AtumDefaultFeatures.addDungeon(biomeGen);
        AtumDefaultFeatures.addTomb(biomeGen);
        AtumDefaultFeatures.addPyramid(biomeGen);
        AtumDefaultFeatures.addRuins(biomeGen);
        AtumDefaultFeatures.addMineshaft(biomeGen, false);

        return new Builder().depth(-0.18F).scale(0.0F).withGenerationSettings(biomeGen.build()).withMobSpawnSettings(new MobSpawnInfo.Builder().isValidSpawnBiomeForPlayer().copy()).setEffects(Builder.getBaseEffects().withFoliageColor(10189386).withGrassColor(10189386).build()).build();
    }

    public static Biome makeSparseWoods(String biomeName) {
        BiomeGenerationSettings.Builder biomeGen = (new BiomeGenerationSettings.Builder().withSurfaceBuilder(AtumSurfaceBuilders.SANDY));
        biomeGen.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration(AtumFeatures.SPARSE_DRY_GRASS_CONFIG).withPlacement(Features.Placements.PATCH_PLACEMENT).withPlacement(Placement.COUNT_NOISE.configure(new NoiseDependant(-0.8D, 2, 3))));
        biomeGen.withFeature(VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration(AtumFeatures.SPARSE_TALL_GRASS_CONFIG).withPlacement(Features.Placements.VEGETATION_PLACEMENT).withPlacement(Features.Placements.FLOWER_TALL_GRASS_PLACEMENT).square().withPlacement(Placement.COUNT_NOISE.configure(new NoiseDependant(-0.8D, 0, 7))));
        AtumDefaultFeatures.addDeadwoodTrees(biomeGen, 3, 0.1F, 1);
        return makeBaseWoods(biomeName, biomeGen);
    }

    public static Biome makeDenseWoods(String biomeName) {
        BiomeGenerationSettings.Builder biomeGen = (new BiomeGenerationSettings.Builder().withSurfaceBuilder(AtumSurfaceBuilders.SANDY));
        biomeGen.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration(AtumFeatures.DENSE_DRY_GRASS_CONFIG).withPlacement(Features.Placements.PATCH_PLACEMENT).withPlacement(Features.Placements.PATCH_PLACEMENT).withPlacement(Placement.COUNT_NOISE.configure(new NoiseDependant(-0.8D, 5, 10))));
        biomeGen.withFeature(VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration(AtumFeatures.DRY_TALL_GRASS_CONFIG).withPlacement(Features.Placements.VEGETATION_PLACEMENT).withPlacement(Features.Placements.FLOWER_TALL_GRASS_PLACEMENT).square().withPlacement(Placement.COUNT_NOISE.configure(new NoiseDependant(-0.8D, 0, 7))));
        AtumDefaultFeatures.addDeadwoodTrees(biomeGen, 20, 0.25F, 3);
        return makeBaseWoods(biomeName, biomeGen);
    }

    private static Biome makeBaseWoods(String biomeName, BiomeGenerationSettings.Builder biomeGen) {
        biomeGen.withFeature(VEGETAL_DECORATION, AtumFeatures.ANPUTS_FINGERS.withConfiguration(AtumFeatures.ANPUTS_FINGERS_CONFIG).withPlacement(Features.Placements.PATCH_PLACEMENT).withPlacement(Placement.HEIGHTMAP_WORLD_SURFACE.configure(IPlacementConfig.NO_PLACEMENT_CONFIG)).func_242731_b(16));
        addDefaultSpawns(biomeName);
        AtumDefaultFeatures.addCarvers(biomeGen);
        AtumDefaultFeatures.addSandLayer(biomeGen);
        AtumDefaultFeatures.addSprings(biomeGen);
        AtumDefaultFeatures.addSurfaceLavaLake(biomeGen);
        AtumDefaultFeatures.addMaterialPockets(biomeGen);
        AtumDefaultFeatures.addStoneVariants(biomeGen);
        AtumDefaultFeatures.addOres(biomeGen);
        AtumDefaultFeatures.addInfestedLimestone(biomeGen);
        AtumDefaultFeatures.addShrubs(biomeGen);
        AtumDefaultFeatures.addFossils(biomeGen);
        AtumDefaultFeatures.addDungeon(biomeGen);
        AtumDefaultFeatures.addTomb(biomeGen);
        AtumDefaultFeatures.addPyramid(biomeGen);
        AtumDefaultFeatures.addRuins(biomeGen);
        AtumDefaultFeatures.addMineshaft(biomeGen, false);
        return new Builder().withGenerationSettings(biomeGen.build()).withMobSpawnSettings(new MobSpawnInfo.Builder().isValidSpawnBiomeForPlayer().copy()).build();
    }

    public static Biome makeDriedRiver(String biomeName) {
        BiomeGenerationSettings.Builder biomeGen = (new BiomeGenerationSettings.Builder().withSurfaceBuilder(AtumSurfaceBuilders.GRAVEL_CRACKED));
        biomeGen.withFeature(UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(AtumDefaultFeatures.LIMESTONE_CRACKED, AtumBlocks.MARL.getDefaultState(), 32)).withPlacement(Placement.RANGE.configure(new TopSolidRangeConfig(55, 0, 67))).func_242731_b(90));
        AtumDefaultFeatures.addCarvers(biomeGen);
        AtumDefaultFeatures.addSandLayer(biomeGen);
        AtumDefaultFeatures.addSprings(biomeGen);
        AtumDefaultFeatures.addSurfaceLavaLake(biomeGen);
        AtumDefaultFeatures.addStoneVariants(biomeGen);
        AtumDefaultFeatures.addOres(biomeGen);
        AtumDefaultFeatures.addMaterialPockets(biomeGen);
        AtumDefaultFeatures.addInfestedLimestone(biomeGen);
        AtumDefaultFeatures.addShrubs(biomeGen);
        AtumDefaultFeatures.addFossils(biomeGen);
        AtumDefaultFeatures.addDungeon(biomeGen);
        AtumDefaultFeatures.addTomb(biomeGen);
        AtumDefaultFeatures.addMineshaft(biomeGen, false);
        return new Builder().withGenerationSettings(biomeGen.build()).withMobSpawnSettings(MobSpawnInfo.EMPTY).depth(-0.28F).scale(0.0F).build();
    }

    public static Biome makeLimestoneCrags(String biomeName) {
        BiomeGenerationSettings.Builder biomeGen = (new BiomeGenerationSettings.Builder().withSurfaceBuilder(AtumSurfaceBuilders.SANDY));
        biomeGen.withFeature(SURFACE_STRUCTURES, AtumFeatures.LIMESTONE_SPIKE.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).func_242731_b(3));
        AtumDefaultFeatures.addDeadwoodTrees(biomeGen, 0, 0.2F, 1);
        addDefaultSpawns(biomeName);
        addDesertWolfSpawning(biomeName);
        AtumDefaultFeatures.addCarvers(biomeGen);
        AtumDefaultFeatures.addSandLayer(biomeGen);
        AtumDefaultFeatures.addSprings(biomeGen);
        AtumDefaultFeatures.addSurfaceLavaLake(biomeGen);
        AtumDefaultFeatures.addMaterialPockets(biomeGen);
        AtumDefaultFeatures.addStoneVariants(biomeGen);
        AtumDefaultFeatures.addOres(biomeGen);
        AtumDefaultFeatures.addEmeraldOre(biomeGen);
        AtumDefaultFeatures.addInfestedLimestone(biomeGen);
        AtumDefaultFeatures.addFossils(biomeGen);
        AtumDefaultFeatures.addDungeon(biomeGen);
        AtumDefaultFeatures.addTomb(biomeGen);
        AtumDefaultFeatures.addMineshaft(biomeGen, false);
        return new Builder().withGenerationSettings(biomeGen.build()).withMobSpawnSettings(MobSpawnInfo.EMPTY).depth(0.75F).scale(0.45F).build();
    }

    public static Biome makeLimestoneMountain(String biomeName) {
        BiomeGenerationSettings.Builder biomeGen = (new BiomeGenerationSettings.Builder().withSurfaceBuilder(AtumSurfaceBuilders.SANDY_LIMESTONE));
        /*if (AtumConfig.WORLD_GEN.lighthouseEnabled.get()) {
            biomeGen.withStructure(AtumStructures.LIGHTHOUSE_FEATURE);
        }*/
        AtumDefaultFeatures.addDeadwoodTrees(biomeGen, 0, 0.1F, 1);
        addDefaultSpawns(biomeName);
        addDesertWolfSpawning(biomeName);
        AtumDefaultFeatures.addCarvers(biomeGen);
        AtumDefaultFeatures.addSandLayer(biomeGen);
        AtumDefaultFeatures.addSprings(biomeGen);
        AtumDefaultFeatures.addMaterialPockets(biomeGen);
        AtumDefaultFeatures.addStoneVariants(biomeGen);
        AtumDefaultFeatures.addOres(biomeGen);
        AtumDefaultFeatures.addEmeraldOre(biomeGen);
        AtumDefaultFeatures.addInfestedLimestone(biomeGen);
        AtumDefaultFeatures.addFossils(biomeGen);
        AtumDefaultFeatures.addDungeon(biomeGen);
        AtumDefaultFeatures.addTomb(biomeGen);
        AtumDefaultFeatures.addRuins(biomeGen);
        AtumDefaultFeatures.addMineshaft(biomeGen, true);
        return new Builder().withGenerationSettings(biomeGen.build()).withMobSpawnSettings(MobSpawnInfo.EMPTY).depth(1.5F).scale(0.6F).build();
    }

    public static Biome makeOasis(String biomeName) {
        BiomeGenerationSettings.Builder biomeGen = (new BiomeGenerationSettings.Builder().withSurfaceBuilder(AtumSurfaceBuilders.OASIS));
        biomeGen.withCarver(GenerationStage.Carving.AIR, AtumCarvers.CAVE_CONFIGURED);
        biomeGen.withFeature(VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration(AtumFeatures.OASIS_GRASS_CONFIG).withPlacement(Features.Placements.PATCH_PLACEMENT).func_242731_b(16));
        biomeGen.withFeature(VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration(AtumFeatures.PAPYRUS_CONFIG).withPlacement(Features.Placements.PATCH_PLACEMENT).func_242731_b(3));
        biomeGen.withFeature(VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration((new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(Blocks.LILY_PAD.getDefaultState()), SimpleBlockPlacer.PLACER)).tries(7).build()).withPlacement(Features.Placements.PATCH_PLACEMENT).func_242731_b(2));
        biomeGen.withFeature(VEGETAL_DECORATION, AtumFeatures.ATUM_TREE.withConfiguration(AtumFeatures.PALM_TREE_CONFIG).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).withPlacement(Placement.COUNT_EXTRA.configure(new AtSurfaceWithExtraConfig(9, 0.25F, 2))));
        biomeGen.withFeature(VEGETAL_DECORATION, AtumFeatures.ATUM_TREE.withConfiguration(new BaseTreeFeatureConfig.Builder(new SimpleBlockStateProvider(Blocks.ACACIA_LOG.getDefaultState()), new SimpleBlockStateProvider(Blocks.ACACIA_LEAVES.getDefaultState()), new AcaciaFoliagePlacer(FeatureSpread.func_242252_a(2), FeatureSpread.func_242252_a(0)), new ForkyTrunkPlacer(5, 2, 2), new TwoLayerFeature(1, 0, 2)).setIgnoreVines().build()).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).withPlacement(Placement.COUNT_EXTRA.configure(new AtSurfaceWithExtraConfig(1, 0.1F, 1))));
        addCamelSpawning(biomeName);
        addSpawn(biomeName, AtumEntities.QUAIL, 8, 2, 5, EntityClassification.CREATURE);
        biomeGen.withFeature(VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration(AtumFeatures.DENSE_DRY_GRASS_CONFIG).withPlacement(Features.Placements.PATCH_PLACEMENT).withPlacement(Placement.COUNT_NOISE.configure(new NoiseDependant(-0.8D, 5, 10))));
        biomeGen.withFeature(VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration(AtumFeatures.DRY_TALL_GRASS_CONFIG).withPlacement(Features.Placements.VEGETATION_PLACEMENT).withPlacement(Features.Placements.FLOWER_TALL_GRASS_PLACEMENT).square().withPlacement(Placement.COUNT_NOISE.configure(new NoiseDependant(-0.8D, 0, 7))));
        AtumDefaultFeatures.addCarvers(biomeGen);
        AtumDefaultFeatures.addMaterialPockets(biomeGen);
        AtumDefaultFeatures.addStoneVariants(biomeGen);
        AtumDefaultFeatures.addOres(biomeGen);
        AtumDefaultFeatures.addEmeraldOre(biomeGen);
        AtumDefaultFeatures.addInfestedLimestone(biomeGen);
        AtumDefaultFeatures.addFossils(biomeGen);
        AtumDefaultFeatures.addMineshaft(biomeGen, false);
        return new Builder().depth(-0.45F).scale(0.0F).temperature(1.85F).withGenerationSettings(biomeGen.build()).withMobSpawnSettings(new MobSpawnInfo.Builder().isValidSpawnBiomeForPlayer().copy()).setEffects(Builder.getBaseEffects().withFoliageColor(11987573).withGrassColor(11987573).setWaterColor(3394764).setWaterFogColor(39321).build()).build();
    }

    public static Biome makeSandDunes(String biomeName) {
        BiomeGenerationSettings.Builder biomeGen = (new BiomeGenerationSettings.Builder().withSurfaceBuilder(AtumSurfaceBuilders.SANDY));
        AtumDefaultFeatures.addDeadwoodTrees(biomeGen, 0, 0.01F, 1);
        addDefaultSpawns(biomeName);
        addCamelSpawning(biomeName);
        AtumDefaultFeatures.addCarvers(biomeGen);
        AtumDefaultFeatures.addSandLayer(biomeGen);
        AtumDefaultFeatures.addSprings(biomeGen);
        AtumDefaultFeatures.addSurfaceLavaLake(biomeGen);
        AtumDefaultFeatures.addMaterialPockets(biomeGen);
        AtumDefaultFeatures.addStoneVariants(biomeGen);
        AtumDefaultFeatures.addOres(biomeGen);
        AtumDefaultFeatures.addInfestedLimestone(biomeGen);
        AtumDefaultFeatures.addShrubs(biomeGen);
        AtumDefaultFeatures.addFossils(biomeGen);
        AtumDefaultFeatures.addDungeon(biomeGen);
        AtumDefaultFeatures.addTomb(biomeGen);
        AtumDefaultFeatures.addPyramid(biomeGen);
        AtumDefaultFeatures.addRuins(biomeGen);
        AtumDefaultFeatures.addMineshaft(biomeGen, false);
        return new Builder().withGenerationSettings(biomeGen.build()).withMobSpawnSettings(new MobSpawnInfo.Builder().isValidSpawnBiomeForPlayer().copy()).depth(0.175F).scale(0.2F).build();
    }

    public static Biome makeSandHills(String biomeName) {
        BiomeGenerationSettings.Builder biomeGen = (new BiomeGenerationSettings.Builder().withSurfaceBuilder(AtumSurfaceBuilders.SANDY));
        AtumDefaultFeatures.addDeadwoodTrees(biomeGen, 0, 0.08F, 1);
        addDefaultSpawns(biomeName);
        addDesertWolfSpawning(biomeName);
        AtumDefaultFeatures.addCarvers(biomeGen);
        AtumDefaultFeatures.addSandLayer(biomeGen);
        AtumDefaultFeatures.addSprings(biomeGen);
        AtumDefaultFeatures.addMaterialPockets(biomeGen);
        AtumDefaultFeatures.addStoneVariants(biomeGen);
        AtumDefaultFeatures.addOres(biomeGen);
        AtumDefaultFeatures.addEmeraldOre(biomeGen);
        AtumDefaultFeatures.addInfestedLimestone(biomeGen);
        AtumDefaultFeatures.addFossils(biomeGen);
        AtumDefaultFeatures.addDungeon(biomeGen);
        AtumDefaultFeatures.addTomb(biomeGen);
        AtumDefaultFeatures.addRuins(biomeGen);
        AtumDefaultFeatures.addMineshaft(biomeGen, false);
        return new Builder().withGenerationSettings(biomeGen.build()).withMobSpawnSettings(MobSpawnInfo.EMPTY).depth(0.3F).scale(0.3F).build();
    }

    public static Biome makeSandPlains(String biomeName) {
        BiomeGenerationSettings.Builder biomeGen = (new BiomeGenerationSettings.Builder().withSurfaceBuilder(AtumSurfaceBuilders.SANDY));
        biomeGen.withStructure(AtumStructures.GIRAFI_TOMB_FEATURE);
        AtumDefaultFeatures.addDeadwoodTrees(biomeGen, 0, 0.025F, 1);
        addDefaultSpawns(biomeName);
        addCamelSpawning(biomeName);
        biomeGen.withStructure(StructureFeatures.VILLAGE_DESERT); //TODO Remove, Temporary for testing
        AtumDefaultFeatures.addCarvers(biomeGen);
        AtumDefaultFeatures.addSandLayer(biomeGen);
        AtumDefaultFeatures.addSprings(biomeGen);
        AtumDefaultFeatures.addSurfaceLavaLake(biomeGen);
        AtumDefaultFeatures.addMaterialPockets(biomeGen);
        AtumDefaultFeatures.addStoneVariants(biomeGen);
        AtumDefaultFeatures.addOres(biomeGen);
        AtumDefaultFeatures.addInfestedLimestone(biomeGen);
        AtumDefaultFeatures.addShrubs(biomeGen);
        AtumDefaultFeatures.addFossils(biomeGen);
        AtumDefaultFeatures.addDungeon(biomeGen);
        AtumDefaultFeatures.addTomb(biomeGen);
        AtumDefaultFeatures.addPyramid(biomeGen);
        AtumDefaultFeatures.addRuins(biomeGen);
        AtumDefaultFeatures.addMineshaft(biomeGen, false);
        return new Builder().withGenerationSettings(biomeGen.build()).withMobSpawnSettings(new MobSpawnInfo.Builder().isValidSpawnBiomeForPlayer().copy()).build();
    }

    public static void addDefaultSpawns(String biomeName) {
        //Animals
        addSpawn(biomeName, AtumEntities.DESERT_RABBIT, 5, 2, 3, EntityClassification.CREATURE);
        addSpawn(biomeName, EntityType.BAT, 4, 4, 8, EntityClassification.AMBIENT);
        addSpawn(biomeName, AtumEntities.QUAIL, 3, 2, 4, EntityClassification.CREATURE);

        //Undead
        addSpawn(biomeName, AtumEntities.BONESTORM, 5, 1, 2, EntityClassification.MONSTER);
        addSpawn(biomeName, AtumEntities.FORSAKEN, 22, 1, 4, EntityClassification.MONSTER);
        addSpawn(biomeName, AtumEntities.MUMMY, 30, 1, 3, EntityClassification.MONSTER);
        addSpawn(biomeName, AtumEntities.WRAITH, 10, 1, 2, EntityClassification.MONSTER);

        //Underground
        addSpawn(biomeName, AtumEntities.STONEGUARD, 34, 1, 2, EntityClassification.MONSTER);
        addSpawn(biomeName, AtumEntities.TARANTULA, 20, 1, 3, EntityClassification.MONSTER);
    }

    public static void addSpawn(String biomeName, EntityType<?> entityType, int weight, int min, int max, EntityClassification classification) {
        ResourceLocation location = entityType.getRegistryName();
        if (location != null) {
            new AtumConfig.Mobs(AtumConfig.BUILDER, location.getPath(), min, max, weight, entityType, classification, new ResourceLocation(Atum.MOD_ID, biomeName)); //Write config
        }
    }

    public static void addCamelSpawning(String biomeName) {
        addSpawn(biomeName, AtumEntities.CAMEL, 6, 2, 6, EntityClassification.CREATURE);
    }

    public static void addDesertWolfSpawning(String biomeName) {
        addSpawn(biomeName, AtumEntities.DESERT_WOLF, 6, 2, 4, EntityClassification.CREATURE);
    }

    public static class Builder extends Biome.Builder {

        public Builder() {
            this.precipitation(Biome.RainType.NONE);
            this.category(Biome.Category.DESERT);
            this.temperature(2.0F);
            this.downfall(0.0F);
            this.setEffects(getBaseEffects().build());
            this.depth(0.135F);
            this.scale(0.05F);
        }

        public static BiomeAmbience.Builder getBaseEffects() {
            return new BiomeAmbience.Builder().setFogColor(13876389).setWaterColor(7036242).setWaterFogColor(7036242).withSkyColor(DimensionHelper.getSkyColorWithTemperatureModifier(2.0F)).withGrassColor(12889745).withFoliageColor(12889745).setMoodSound(MoodSoundAmbience.DEFAULT_CAVE);
        }
    }
}