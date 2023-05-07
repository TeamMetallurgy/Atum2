package com.teammetallurgy.atum.world.biome;

import com.teammetallurgy.atum.world.DimensionHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.*;

public class AtumBiomeMaker { //TODO Remove once all things have been fully moved to json

    public static Biome makeKarstCaves(String biomeName) {
        BiomeGenerationSettings.Builder biomeGen = null;
        addDefaultSpawns(biomeName);
        return new Builder().generationSettings(biomeGen.build()).mobSpawnSettings(new MobSpawnSettings.Builder().build()).build();
    }
    
    public static Biome makeDeadOasis(String biomeName) {
        BiomeGenerationSettings.Builder biomeGen = (null/*.surfaceBuilder(AtumSurfaceBuilders.GRAVEL_CRACKED)*/);
        addDefaultSpawns(biomeName);
        addCamelSpawning(biomeName);
        /*biomeGen.addFeature(VEGETAL_DECORATION, AtumFeatures.SPARSE_DRY_GRASS_SPREAD_5);
        biomeGen.addFeature(VEGETAL_DECORATION, AtumFeatures.DEAD_PALM_TREE);
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
        AtumDefaultFeatures.addMineshaft(biomeGen, false);*/

        return new Builder()/*.depth(-0.18F).scale(0.0F)*/.generationSettings(biomeGen.build()).mobSpawnSettings(new MobSpawnSettings.Builder().build()).specialEffects(Builder.getBaseEffects().foliageColorOverride(10189386).grassColorOverride(10189386).build()).build();
    }

    public static Biome makeSparseWoods(String biomeName) {
        BiomeGenerationSettings.Builder biomeGen = null;
        /*biomeGen.addFeature(VEGETAL_DECORATION, AtumFeatures.SPARSE_DRY_GRASS_NOISE_08_2_3);
        biomeGen.addFeature(VEGETAL_DECORATION, AtumFeatures.SPARSE_TALL_GRASS);
        biomeGen.addFeature(VEGETAL_DECORATION, AtumFeatures.DEADWOOD_3_01_1);*/
        return makeBaseWoods(biomeName, biomeGen);
    }

    public static Biome makeDenseWoods(String biomeName) {
        BiomeGenerationSettings.Builder biomeGen = null;
        /*biomeGen.addFeature(VEGETAL_DECORATION, AtumFeatures.DENSE_DRY_GRASS_NOISE_08_5_10);
        biomeGen.addFeature(VEGETAL_DECORATION, AtumFeatures.DRY_TALL_GRASS);
        biomeGen.addFeature(VEGETAL_DECORATION, AtumFeatures.DEADWOOD_20_025_3);*/
        return makeBaseWoods(biomeName, biomeGen);
    }

    private static Biome makeBaseWoods(String biomeName, BiomeGenerationSettings.Builder biomeGen) {
        addDefaultSpawns(biomeName);
        /*biomeGen.addFeature(VEGETAL_DECORATION, AtumFeatures.ANPUTS_FINGERS_CONFIGURED);
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
        AtumDefaultFeatures.addMineshaft(biomeGen, false);*/
        return new Builder().generationSettings(biomeGen.build()).mobSpawnSettings(new MobSpawnSettings.Builder().build()).build();
    }

    public static Biome makeDriedRiver(String biomeName) {
        BiomeGenerationSettings.Builder biomeGen = (null/*.surfaceBuilder(AtumSurfaceBuilders.GRAVEL_CRACKED)*/);
        /*biomeGen.addFeature(UNDERGROUND_ORES, AtumFeatures.MARL_DRIED_RIVER);
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
        AtumDefaultFeatures.addMineshaft(biomeGen, false);*/
        return new Builder().generationSettings(biomeGen.build()).mobSpawnSettings(MobSpawnSettings.EMPTY)/*.depth(-0.28F).scale(0.0F)*/.build();
    }

    public static Biome makeLimestoneCrags(String biomeName) {
        BiomeGenerationSettings.Builder biomeGen = null;
        addDefaultSpawns(biomeName);
        addDesertWolfSpawning(biomeName);
        /*biomeGen.addFeature(SURFACE_STRUCTURES, AtumFeatures.LIMESTONE_SPIKE_CONFIGURED);
        biomeGen.addFeature(VEGETAL_DECORATION, AtumFeatures.DEADWOOD_0_02_1);
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
        AtumDefaultFeatures.addMineshaft(biomeGen, false);*/
        return new Builder().generationSettings(biomeGen.build()).mobSpawnSettings(MobSpawnSettings.EMPTY)/*.depth(0.75F).scale(0.45F)*/.build();
    }

    public static Biome makeLimestoneMountain(String biomeName) {
        BiomeGenerationSettings.Builder biomeGen = (null/*.surfaceBuilder(AtumSurfaceBuilders.SANDY_LIMESTONE)*/);
        addDefaultSpawns(biomeName);
        addDesertWolfSpawning(biomeName);
        /*biomeGen.addFeature(VEGETAL_DECORATION, AtumFeatures.DEADWOOD_0_01_1);
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
        AtumDefaultFeatures.addMineshaft(biomeGen, true);*/
        return new Builder().generationSettings(biomeGen.build()).mobSpawnSettings(MobSpawnSettings.EMPTY)/*.depth(1.5F).scale(0.6F)*/.build();
    }

    public static Biome makeOasis(String biomeName) {
        BiomeGenerationSettings.Builder biomeGen = (null/*.surfaceBuilder(AtumSurfaceBuilders.OASIS)*/);
        addCamelSpawning(biomeName);
        /*biomeGen.addCarver(GenerationStep.Carving.AIR, AtumCarvers.CAVE_CONFIGURED);
        biomeGen.addFeature(VEGETAL_DECORATION, AtumFeatures.OASIS_GRASS);
        biomeGen.addFeature(VEGETAL_DECORATION, AtumFeatures.PAPYRUS);
        biomeGen.addFeature(VEGETAL_DECORATION, AtumFeatures.LILY_PAD);
        biomeGen.addFeature(VEGETAL_DECORATION, AtumFeatures.PALM_TREE);
        biomeGen.addFeature(VEGETAL_DECORATION, AtumFeatures.ACACIA_TREE);
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
        AtumDefaultFeatures.addMineshaft(biomeGen, false);*/
        return new Builder()/*.depth(-0.45F).scale(0.0F)*/.temperature(1.85F).generationSettings(biomeGen.build()).mobSpawnSettings(new MobSpawnSettings.Builder().build()).specialEffects(Builder.getBaseEffects().foliageColorOverride(11987573).grassColorOverride(11987573).waterColor(3394764).waterFogColor(39321).build()).build();
    }

    public static Biome makeSandDunes(String biomeName) {
        BiomeGenerationSettings.Builder biomeGen = null;
        addDefaultSpawns(biomeName);
        addCamelSpawning(biomeName);
        /*iomeGen.addFeature(VEGETAL_DECORATION, AtumFeatures.DEADWOOD_0_01_1);
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
        AtumDefaultFeatures.addMineshaft(biomeGen, false);*/
        return new Builder().generationSettings(biomeGen.build()).mobSpawnSettings(new MobSpawnSettings.Builder().build())/*.depth(0.175F).scale(0.2F)*/.build();
    }

    public static Biome makeSandHills(String biomeName) {
        BiomeGenerationSettings.Builder biomeGen = null;
        addDefaultSpawns(biomeName);
        addDesertWolfSpawning(biomeName);
        /*biomeGen.addFeature(VEGETAL_DECORATION, AtumFeatures.DEADWOOD_0_08_1);
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
        AtumDefaultFeatures.addMineshaft(biomeGen, false);*/
        return new Builder().generationSettings(biomeGen.build()).mobSpawnSettings(MobSpawnSettings.EMPTY)/*.depth(0.3F).scale(0.3F)*/.build();
    }

    public static Biome makeSandPlains(String biomeName) {
        BiomeGenerationSettings.Builder biomeGen = null;
        addDefaultSpawns(biomeName);
        addCamelSpawning(biomeName);
        /*biomeGen.addStructureStart(AtumStructures.GIRAFI_TOMB_FEATURE);
        biomeGen.addFeature(VEGETAL_DECORATION, AtumFeatures.DEADWOOD_0_025_1);
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
        AtumDefaultFeatures.addGenericVillage(biomeGen);*/
        return new Builder().generationSettings(biomeGen.build()).mobSpawnSettings(new MobSpawnSettings.Builder().build()).build();
    }

    public static void addDefaultSpawns(String biomeName) {
        /*//Animals
        addSpawn(biomeName, AtumEntities.DESERT_RABBIT.get(), 5, 2, 3, MobCategory.CREATURE); //TODO Move to new mob spawn jsons
        addSpawn(biomeName, EntityType.BAT, 4, 4, 8, MobCategory.AMBIENT);
        addSpawn(biomeName, AtumEntities.QUAIL.get(), 3, 2, 4, MobCategory.CREATURE);

        //Undead
        addSpawn(biomeName, AtumEntities.BONESTORM.get(), 5, 1, 2, MobCategory.MONSTER);
        addSpawn(biomeName, AtumEntities.FORSAKEN.get(), 22, 1, 4, MobCategory.MONSTER);
        addSpawn(biomeName, AtumEntities.MUMMY.get(), 30, 1, 3, MobCategory.MONSTER);
        addSpawn(biomeName, AtumEntities.WRAITH.get(), 10, 1, 2, MobCategory.MONSTER);

        //Underground
        addSpawn(biomeName, AtumEntities.STONEGUARD.get(), 34, 1, 2, MobCategory.MONSTER);
        addSpawn(biomeName, AtumEntities.TARANTULA.get(), 20, 1, 3, MobCategory.MONSTER);*/
    }

    public static void addCamelSpawning(String biomeName) {
        //addSpawn(biomeName, AtumEntities.CAMEL.get(), 6, 2, 6, MobCategory.CREATURE);
    }

    public static void addDesertWolfSpawning(String biomeName) {
        //addSpawn(biomeName, AtumEntities.DESERT_WOLF.get(), 6, 2, 4, MobCategory.CREATURE);
    }

    public static class Builder extends Biome.BiomeBuilder {

        public Builder() {
            //this.precipitation(Biome.Precipitation.NONE);
            this.temperature(2.0F);
            this.downfall(0.0F);
            this.specialEffects(getBaseEffects().build());
        }

        public static BiomeSpecialEffects.Builder getBaseEffects() {
            return new BiomeSpecialEffects.Builder().fogColor(13876389).waterColor(7036242).waterFogColor(7036242).skyColor(DimensionHelper.getSkyColorWithTemperatureModifier(2.0F)).grassColorOverride(12889745).foliageColorOverride(12889745).ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS);
        }
    }
}