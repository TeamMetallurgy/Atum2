package com.teammetallurgy.atum.init;

import com.teammetallurgy.atum.misc.AtumConfig;
import com.teammetallurgy.atum.misc.AtumRegistry;
import com.teammetallurgy.atum.world.biome.AtumBiomeMaker;
import net.minecraft.util.RegistryKey;
import net.minecraft.world.biome.Biome;

public class AtumBiomes {
    public static final RegistryKey<Biome> DEAD_OASIS = registerBiome(AtumBiomeMaker.makeDeadOasis(), "dead_oasis");
    public static final RegistryKey<Biome> DEADWOOD_FOREST = registerBiome(AtumBiomeMaker.makeDeadwoodForest(), "deadwood_forest", 10);
    public static final RegistryKey<Biome> DRIED_RIVER = registerBiome(AtumBiomeMaker.makeDriedRiver(), "dried_river");
    public static final RegistryKey<Biome> LIMESTONE_CRAGS = registerBiome(AtumBiomeMaker.makeLimestoneCrags(), "limestone_crags", 3);
    public static final RegistryKey<Biome> LIMESTONE_MOUNTAINS = registerBiome(AtumBiomeMaker.makeLimestoneMountain(), "limestone_mountains", 5);
    public static final RegistryKey<Biome> OASIS = registerBiome(AtumBiomeMaker.makeOasis(), "oasis");
    public static final RegistryKey<Biome> SAND_DUNES = registerBiome(AtumBiomeMaker.makeSandDunes(), "sand_dunes", 15);
    public static final RegistryKey<Biome> SAND_HILLS = registerBiome(AtumBiomeMaker.makeSandHills(), "sand_hills", 10);
    public static final RegistryKey<Biome> SAND_PLAINS = registerBiome(AtumBiomeMaker.makeSandPlains(), "sand_plains", 30);

    public static RegistryKey<Biome> registerBiome(Biome biome, String biomeName) {
        return registerBiome(biome, biomeName, 0);
    }

    public static RegistryKey<Biome> registerBiome(Biome biome, String biomeName, int weight) {
        AtumRegistry.registerBiome(biome, biomeName);
        if (weight > 0) {
            new AtumConfig.Biome(AtumConfig.BUILDER, biomeName, weight); //Write config
        }
        return AtumRegistry.registerBiomeKey(biomeName);
    }
}