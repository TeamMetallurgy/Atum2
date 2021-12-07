/*
package com.teammetallurgy.atum.world.biome;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.init.AtumEntities;
import com.teammetallurgy.atum.init.AtumFeatures;
import com.teammetallurgy.atum.init.AtumStructures;
import com.teammetallurgy.atum.misc.AtumConfig;
import com.teammetallurgy.atum.world.DimensionHelper;
import com.teammetallurgy.atum.world.gen.AtumDefaultFeatures;
import com.teammetallurgy.atum.world.gen.AtumSurfaceBuilders;
import com.teammetallurgy.atum.world.gen.carver.AtumCarvers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.levelgen.GenerationStep;

import static net.minecraft.world.level.levelgen.GenerationStep.Decoration.*;

public class AtumBiomeMaker { //TODO

    public static Biome makeDeadOasis(String biomeName) {
        BiomeGenerationSettings.Builder biomeGen = (new BiomeGenerationSettings.Builder().surfaceBuilder(AtumSurfaceBuilders.GRAVEL_CRACKED));
        biomeGen.addFeature(VEGETAL_DECORATION, AtumFeatures.SPARSE_DRY_GRASS_SPREAD_5);
        biomeGen.addFeature(VEGETAL_DECORATION, AtumFeatures.DEAD_PALM_TREE);
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

        return new Builder().depth(-0.18F).scale(0.0F).generationSettings(biomeGen.build()).mobSpawnSettings(new MobSpawnSettings.Builder().setPlayerCanSpawn().build()).specialEffects(Builder.getBaseEffects().foliageColorOverride(10189386).grassColorOverride(10189386).build()).build();
    }

    public static Biome makeSparseWoods(String biomeName) {
        BiomeGenerationSettings.Builder biomeGen = (new BiomeGenerationSettings.Builder().surfaceBuilder(AtumSurfaceBuilders.SANDY));
        biomeGen.addFeature(VEGETAL_DECORATION, AtumFeatures.SPARSE_DRY_GRASS_NOISE_08_2_3);
        biomeGen.addFeature(VEGETAL_DECORATION, AtumFeatures.SPARSE_TALL_GRASS);
        biomeGen.addFeature(VEGETAL_DECORATION, AtumFeatures.DEADWOOD_3_01_1);
        return makeBaseWoods(biomeName, biomeGen);
    }

    public static Biome makeDenseWoods(String biomeName) {
        BiomeGenerationSettings.Builder biomeGen = (new BiomeGenerationSettings.Builder().surfaceBuilder(AtumSurfaceBuilders.SANDY));
        biomeGen.addFeature(VEGETAL_DECORATION, AtumFeatures.DENSE_DRY_GRASS_NOISE_08_5_10);
        biomeGen.addFeature(VEGETAL_DECORATION, AtumFeatures.DRY_TALL_GRASS);
        biomeGen.addFeature(VEGETAL_DECORATION, AtumFeatures.DEADWOOD_20_025_3);
        return makeBaseWoods(biomeName, biomeGen);
    }

    private static Biome makeBaseWoods(String biomeName, BiomeGenerationSettings.Builder biomeGen) {
        biomeGen.addFeature(VEGETAL_DECORATION, AtumFeatures.ANPUTS_FINGERS_CONFIGURED);
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
        AtumDefaultFeatures.addGatehouse(biomeGen);
        AtumDefaultFeatures.addMineshaft(biomeGen, false);
        return new Builder().generationSettings(biomeGen.build()).mobSpawnSettings(new MobSpawnSettings.Builder().setPlayerCanSpawn().build()).build();
    }

    public static Biome makeDriedRiver(String biomeName) {
        BiomeGenerationSettings.Builder biomeGen = (new BiomeGenerationSettings.Builder().surfaceBuilder(AtumSurfaceBuilders.GRAVEL_CRACKED));
        biomeGen.addFeature(UNDERGROUND_ORES, AtumFeatures.MARL_DRIED_RIVER);
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
        return new Builder().generationSettings(biomeGen.build()).mobSpawnSettings(MobSpawnSettings.EMPTY).depth(-0.28F).scale(0.0F).build();
    }

    public static Biome makeLimestoneCrags(String biomeName) {
        BiomeGenerationSettings.Builder biomeGen = (new BiomeGenerationSettings.Builder().surfaceBuilder(AtumSurfaceBuilders.SANDY));
        biomeGen.addFeature(SURFACE_STRUCTURES, AtumFeatures.LIMESTONE_SPIKE_CONFIGURED);
        biomeGen.addFeature(VEGETAL_DECORATION, AtumFeatures.DEADWOOD_0_02_1);
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
        return new Builder().generationSettings(biomeGen.build()).mobSpawnSettings(MobSpawnSettings.EMPTY).depth(0.75F).scale(0.45F).build();
    }

    public static Biome makeLimestoneMountain(String biomeName) {
        BiomeGenerationSettings.Builder biomeGen = (new BiomeGenerationSettings.Builder().surfaceBuilder(AtumSurfaceBuilders.SANDY_LIMESTONE));
        biomeGen.addFeature(VEGETAL_DECORATION, AtumFeatures.DEADWOOD_0_01_1);
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
        return new Builder().generationSettings(biomeGen.build()).mobSpawnSettings(MobSpawnSettings.EMPTY).depth(1.5F).scale(0.6F).build();
    }

    public static Biome makeOasis(String biomeName) {
        BiomeGenerationSettings.Builder biomeGen = (new BiomeGenerationSettings.Builder().surfaceBuilder(AtumSurfaceBuilders.OASIS));
        biomeGen.addCarver(GenerationStep.Carving.AIR, AtumCarvers.CAVE_CONFIGURED);
        biomeGen.addFeature(VEGETAL_DECORATION, AtumFeatures.OASIS_GRASS);
        biomeGen.addFeature(VEGETAL_DECORATION, AtumFeatures.PAPYRUS);
        biomeGen.addFeature(VEGETAL_DECORATION, AtumFeatures.LILY_PAD);
        biomeGen.addFeature(VEGETAL_DECORATION, AtumFeatures.PALM_TREE);
        biomeGen.addFeature(VEGETAL_DECORATION, AtumFeatures.ACACIA_TREE);
        addCamelSpawning(biomeName);
        addSpawn(biomeName, AtumEntities.QUAIL, 8, 2, 5, MobCategory.CREATURE);
        biomeGen.addFeature(VEGETAL_DECORATION, AtumFeatures.DENSE_DRY_GRASS_NOISE_08_5_10);
        biomeGen.addFeature(VEGETAL_DECORATION, AtumFeatures.DRY_TALL_GRASS);
        AtumDefaultFeatures.addCarvers(biomeGen);
        AtumDefaultFeatures.addMaterialPockets(biomeGen);
        AtumDefaultFeatures.addStoneVariants(biomeGen);
        AtumDefaultFeatures.addOres(biomeGen);
        AtumDefaultFeatures.addEmeraldOre(biomeGen);
        AtumDefaultFeatures.addInfestedLimestone(biomeGen);
        AtumDefaultFeatures.addFossils(biomeGen);
        AtumDefaultFeatures.addMineshaft(biomeGen, false);
        return new Builder().depth(-0.45F).scale(0.0F).temperature(1.85F).generationSettings(biomeGen.build()).mobSpawnSettings(new MobSpawnSettings.Builder().setPlayerCanSpawn().build()).specialEffects(Builder.getBaseEffects().foliageColorOverride(11987573).grassColorOverride(11987573).waterColor(3394764).waterFogColor(39321).build()).build();
    }

    public static Biome makeSandDunes(String biomeName) {
        BiomeGenerationSettings.Builder biomeGen = (new BiomeGenerationSettings.Builder().surfaceBuilder(AtumSurfaceBuilders.SANDY));
        biomeGen.addFeature(VEGETAL_DECORATION, AtumFeatures.DEADWOOD_0_01_1);
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
        return new Builder().generationSettings(biomeGen.build()).mobSpawnSettings(new MobSpawnSettings.Builder().setPlayerCanSpawn().build()).depth(0.175F).scale(0.2F).build();
    }

    public static Biome makeSandHills(String biomeName) {
        BiomeGenerationSettings.Builder biomeGen = (new BiomeGenerationSettings.Builder().surfaceBuilder(AtumSurfaceBuilders.SANDY));
        biomeGen.addFeature(VEGETAL_DECORATION, AtumFeatures.DEADWOOD_0_08_1);
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
        return new Builder().generationSettings(biomeGen.build()).mobSpawnSettings(MobSpawnSettings.EMPTY).depth(0.3F).scale(0.3F).build();
    }

    public static Biome makeSandPlains(String biomeName) {
        BiomeGenerationSettings.Builder biomeGen = (new BiomeGenerationSettings.Builder().surfaceBuilder(AtumSurfaceBuilders.SANDY));
        biomeGen.addStructureStart(AtumStructures.GIRAFI_TOMB_FEATURE);
        biomeGen.addFeature(VEGETAL_DECORATION, AtumFeatures.DEADWOOD_0_025_1);
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
        AtumDefaultFeatures.addGatehouse(biomeGen);
        AtumDefaultFeatures.addMineshaft(biomeGen, false);
        AtumDefaultFeatures.addGenericVillage(biomeGen);
        return new Builder().generationSettings(biomeGen.build()).mobSpawnSettings(new MobSpawnSettings.Builder().setPlayerCanSpawn().build()).build();
    }

    public static void addDefaultSpawns(String biomeName) {
        //Animals
        addSpawn(biomeName, AtumEntities.DESERT_RABBIT, 5, 2, 3, MobCategory.CREATURE);
        addSpawn(biomeName, EntityType.BAT, 4, 4, 8, MobCategory.AMBIENT);
        addSpawn(biomeName, AtumEntities.QUAIL, 3, 2, 4, MobCategory.CREATURE);

        //Undead
        addSpawn(biomeName, AtumEntities.BONESTORM, 5, 1, 2, MobCategory.MONSTER);
        addSpawn(biomeName, AtumEntities.FORSAKEN, 22, 1, 4, MobCategory.MONSTER);
        addSpawn(biomeName, AtumEntities.MUMMY, 30, 1, 3, MobCategory.MONSTER);
        addSpawn(biomeName, AtumEntities.WRAITH, 10, 1, 2, MobCategory.MONSTER);

        //Underground
        addSpawn(biomeName, AtumEntities.STONEGUARD, 34, 1, 2, MobCategory.MONSTER);
        addSpawn(biomeName, AtumEntities.TARANTULA, 20, 1, 3, MobCategory.MONSTER);
    }

    public static void addSpawn(String biomeName, EntityType<?> entityType, int weight, int min, int max, MobCategory classification) {
        ResourceLocation location = entityType.getRegistryName();
        if (location != null) {
            new AtumConfig.Mobs(AtumConfig.BUILDER, location.getPath(), min, max, weight, entityType, classification, new ResourceLocation(Atum.MOD_ID, biomeName)); //Write config
        }
    }

    public static void addCamelSpawning(String biomeName) {
        addSpawn(biomeName, AtumEntities.CAMEL, 6, 2, 6, MobCategory.CREATURE);
    }

    public static void addDesertWolfSpawning(String biomeName) {
        addSpawn(biomeName, AtumEntities.DESERT_WOLF, 6, 2, 4, MobCategory.CREATURE);
    }

    public static class Builder extends Biome.BiomeBuilder {

        public Builder() {
            this.precipitation(Biome.Precipitation.NONE);
            this.biomeCategory(Biome.BiomeCategory.DESERT);
            this.temperature(2.0F);
            this.downfall(0.0F);
            this.specialEffects(getBaseEffects().build());
            this.depth(0.135F);
            this.scale(0.05F);
        }

        public static BiomeSpecialEffects.Builder getBaseEffects() {
            return new BiomeSpecialEffects.Builder().fogColor(13876389).waterColor(7036242).waterFogColor(7036242).skyColor(DimensionHelper.getSkyColorWithTemperatureModifier(2.0F)).grassColorOverride(12889745).foliageColorOverride(12889745).ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS);
        }
    }
}*/
