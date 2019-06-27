package com.teammetallurgy.atum.init;

import com.teammetallurgy.atum.utils.AtumRegistry;
import com.teammetallurgy.atum.utils.Constants;
import com.teammetallurgy.atum.world.biome.*;
import com.teammetallurgy.atum.world.biome.base.AtumBiome;
import com.teammetallurgy.atum.world.biome.base.AtumBiome.AtumBiomeProperties;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;

import static com.teammetallurgy.atum.utils.AtumRegistry.registerBiome;

@GameRegistry.ObjectHolder(value = Constants.MOD_ID)
public class AtumBiomes {
    public static final AtumBiome DEAD_OASIS = registerBiome(new BiomeDeadOasis(new AtumBiomeProperties("Dead Oasis", 0).setHeightVariation(0.0F)), "dead_oasis");
    public static final AtumBiome DEADWOOD_FOREST = registerBiome(new BiomeDeadwoodForest(new AtumBiomeProperties("Deadwood Forest", 10)), "deadwood_forest");
    public static final AtumBiome DRIED_RIVER = registerBiome(new BiomeDriedRiver(new AtumBiomeProperties("Dried River", 0).setBaseHeight(-0.5F).setHeightVariation(0.0F)), "dried_river");
    public static final AtumBiome LIMESTONE_CRAGS = registerBiome(new BiomeLimestoneCrags(new AtumBiomeProperties("Limestone Crags", 3).setBaseHeight(0.225F).setHeightVariation(0.45000002F)), "limestone_crags");
    public static final AtumBiome LIMESTONE_MOUNTAINS = registerBiome(new BiomeLimestoneMountains(new AtumBiomeProperties("Limestone Mountains", 5).setBaseHeight(1.5F).setHeightVariation(0.6F)), "limestone_mountains");
    public static final AtumBiome OASIS = registerBiome(new BiomeOasis(new AtumBiomeProperties("Oasis", 0).setHeightVariation(0.0F)), "oasis");
    //public static final AtumBiome RUINED_CITY = registerBiome(new BiomeRuinedCity(new AtumBiomeProperties("Ruined City", 0)), "ruined_city");
    public static final AtumBiome SAND_DUNES = registerBiome(new BiomeSandDunes(new AtumBiomeProperties("Sand Dunes", 15).setBaseHeight(0.175F).setHeightVariation(0.2F)), "sand_dunes");
    public static final AtumBiome SAND_HILLS = registerBiome(new BiomeSandHills(new AtumBiomeProperties("Sand Hills", 10).setBaseHeight(0.3F).setHeightVariation(0.3F)), "sand_hills");
    public static final AtumBiome SAND_PLAINS = registerBiome(new BiomeSandPlains(new AtumBiomeProperties("Sand Plains", 30)), "sand_plains");

    public static void registerBiomes() {
        for (AtumBiome biome : AtumRegistry.BIOMES) {
            ForgeRegistries.BIOMES.register(biome);
            if (biome != AtumBiomes.OASIS) {
                BiomeDictionary.addTypes(biome, BiomeTags.ATUM, BiomeDictionary.Type.HOT, BiomeDictionary.Type.SANDY, BiomeDictionary.Type.SPARSE, BiomeDictionary.Type.DRY);
            }
        }
        BiomeDictionary.addTypes(DEAD_OASIS, BiomeDictionary.Type.DEAD, BiomeDictionary.Type.RARE);
        BiomeDictionary.addTypes(DEADWOOD_FOREST, BiomeDictionary.Type.FOREST);
        BiomeDictionary.addTypes(DRIED_RIVER, BiomeDictionary.Type.RIVER);
        BiomeDictionary.addTypes(LIMESTONE_CRAGS, BiomeDictionary.Type.HILLS, BiomeDictionary.Type.RARE);
        BiomeDictionary.addTypes(LIMESTONE_MOUNTAINS, BiomeDictionary.Type.MOUNTAIN);
        BiomeDictionary.addTypes(OASIS, BiomeTags.OASIS, BiomeDictionary.Type.LUSH, BiomeDictionary.Type.WET, BiomeDictionary.Type.RARE);
        BiomeDictionary.addTypes(SAND_DUNES, BiomeDictionary.Type.HILLS);
        BiomeDictionary.addTypes(SAND_HILLS, BiomeDictionary.Type.HILLS);
        BiomeDictionary.addTypes(SAND_PLAINS, BiomeDictionary.Type.PLAINS);
    }

    public static class BiomeTags {
        public static final BiomeDictionary.Type ATUM = BiomeDictionary.Type.getType("ATUM");
        public static final BiomeDictionary.Type OASIS = BiomeDictionary.Type.getType("OASIS");
    }
}