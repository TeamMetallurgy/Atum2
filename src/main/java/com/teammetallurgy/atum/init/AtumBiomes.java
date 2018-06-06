package com.teammetallurgy.atum.init;

import com.teammetallurgy.atum.utils.AtumRegistry;
import com.teammetallurgy.atum.utils.Constants;
import com.teammetallurgy.atum.world.biome.*;
import com.teammetallurgy.atum.world.biome.AtumBiome.AtumBiomeProperties;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;

import static com.teammetallurgy.atum.utils.AtumRegistry.registerBiome;

@GameRegistry.ObjectHolder(value = Constants.MOD_ID)
public class AtumBiomes {
    public static final AtumBiome SAND_PLAINS = registerBiome(new BiomeGenSandPlains(new AtumBiomeProperties("Sand Plains", 20)), "sand_plains");
    public static final AtumBiome SAND_DUNES = registerBiome(new BiomeGenSandDunes(new AtumBiomeProperties("Sand Dunes", 10).setBaseHeight(0.175F).setHeightVariation(0.2F)), "sand_dunes");
    public static final AtumBiome SAND_HILLS = registerBiome(new BiomeGenSandHills(new AtumBiomeProperties("Sand Hills", 7).setBaseHeight(0.3F).setHeightVariation(0.3F)), "sand_hills");
    public static final AtumBiome LIMESTONE_MOUNTAINS = registerBiome(new BiomeGenLimestoneMountains(new AtumBiomeProperties("Limestone Mountains", 7).setBaseHeight(0.9F).setHeightVariation(0.5F)), "limestone_mountains");
    public static final AtumBiome LIMESTONE_CRAGS = registerBiome(new BiomeGenLimestoneCrags(new AtumBiomeProperties("Limestone Crags", 5).setBaseHeight(0.225F).setHeightVariation(0.45000002F)), "limestone_crags");
    public static final AtumBiome OASIS = registerBiome(new BiomeGenOasis(new AtumBiomeProperties("Oasis", 3).setHeightVariation(0.0F)), "oasis");
    //public static final AtumBiome DEAD_OASIS = registerBiome(new BiomeGenDeadOasis(new AtumBiomeProperties("Dead Oasis", 2)), "dead_oasis");
    public static final AtumBiome DRIED_RIVER = registerBiome(new BiomeGenDriedRiver(new AtumBiomeProperties("Dried River", 0).setBaseHeight(-0.5F).setHeightVariation(0.0F)), "dried_river");
    public static final AtumBiome RUINED_CITY = registerBiome(new BiomeGenRuinedCity(new AtumBiomeProperties("Ruined City", 4)), "ruined_city");

    public static void registerBiomes() {
        for (AtumBiome biome : AtumRegistry.BIOMES) {
            ForgeRegistries.BIOMES.register(biome);
            BiomeDictionary.addTypes(biome, BiomeDictionary.Type.HOT);
        }
    }
}