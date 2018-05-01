package com.teammetallurgy.atum.world.biome;

import com.google.common.collect.Lists;
import com.teammetallurgy.atum.world.biome.AtumBiome.AtumBiomeProperties;

import java.util.List;

public class AtumBiomes {
    static List<AtumBiome> biomeRegistry = Lists.newArrayList();
    static final int DEFAULT_BIOME_WEIGHT = 20;

    public static void register() {
        registerBiome(new BiomeGenSandPlains(new AtumBiomeProperties("Sand Plains").setBaseHeight(0.125F * 0.8F).setHeightVariation(0.05F * 0.6F)));
        registerBiome(new BiomeGenSandDunes(new AtumBiomeProperties("Sand Dunes").setBaseHeight(0.225F).setHeightVariation(0.25F)));
        registerBiome(new BiomeGenSandHills(new AtumBiomeProperties("Sand Hills").setBaseHeight(0.45F).setHeightVariation(0.3F)));
        registerBiome(new BiomeGenLimestoneMountains(new AtumBiomeProperties("Limestone Mountains").setBaseHeight(1.0F).setHeightVariation(0.5F)));
        registerBiome(new BiomeGenLimestoneCrags(new AtumBiomeProperties("Limestone Crags").setBaseHeight(0.425F).setHeightVariation(0.45000002F)));
        // TODO: Disabled until fixed
        // registerBiome(new BiomeGenOasis(new AtumBiomeProperties("Oasis").setBaseHeight(-0.5F).setHeightVariation(0.0F)));
        // registerBiome(new BiomeGenDeadOasis(new AtumBiomeProperties("Dead Oasis").setBaseHeight(-0.5F).setHeightVariation(0.0F)));
        registerBiome(new BiomeGenDriedRiver(new AtumBiomeProperties("Dried River").setBaseHeight(0.1F).setHeightVariation(0.8F)));
        registerBiome(new BiomeGenRuinedCity(new AtumBiomeProperties("Ruined City").setBaseHeight(0.125F).setHeightVariation(0.05F)));
    }

    // in case we want to do anything extra at registration time...
    private static void registerBiome(AtumBiome genBase) {
        biomeRegistry.add(genBase);
    }

    public enum BiomeType {
        SAND_PLAINS(200, "Sand Plains", 2.0F),
        SAND_DUNES(201, "Sand Dunes", 1.0F),
        SAND_HILLS(202, "Sand Hills", 0.75F),
        LIMESTONE_MOUNTAINS(203, "Limestone Mountains", 0.75F),
        LIMESTONE_CRAGS(204, "Limestone Crags", 0.5F),
        OASIS(205, "Oasis", 0.25F),
        DEAD_OASIS(206, "Dead Oasis", 0.25F),
        DRIED_RIVER(207, "Dried River", -1F),
        RUINED_CITY(208, "Ruined City", 0.5F);

        private int id;
        private final String friendlyName;
        private int weight;
        private AtumBiome gen;

        private BiomeType(int defaultID, String friendlyName, float weightMultiplier) {
            this.id = defaultID;
            this.friendlyName = friendlyName;
            this.weight = (int) (DEFAULT_BIOME_WEIGHT * weightMultiplier);
        }

        public void setID(int id) {
            this.id = id;
        }

        public int getID() {
            return this.id;
        }

        public void setGen(AtumBiome gen) {
            this.gen = gen;
        }

        public AtumBiome getGen() {
            return this.gen;
        }

        @Override
        public String toString() {
            return friendlyName;
        }

        public int getWeight() {
            return weight;
        }
    }
}