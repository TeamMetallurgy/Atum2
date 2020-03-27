package com.teammetallurgy.atum.init;

import com.teammetallurgy.atum.utils.AtumRegistry;
import com.teammetallurgy.atum.utils.Constants;
import com.teammetallurgy.atum.world.biome.*;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.registries.ObjectHolder;

import static com.teammetallurgy.atum.utils.AtumRegistry.registerBiome;

@ObjectHolder(value = Constants.MOD_ID)
public class AtumBiomes { //TODO
    public static final AtumBiome DEAD_OASIS = registerBiome(new BiomeDeadOasis(), "dead_oasis");
    public static final AtumBiome DEADWOOD_FOREST = registerBiome(new BiomeDeadwoodForest(), "deadwood_forest");
    public static final AtumBiome DRIED_RIVER = registerBiome(new BiomeDriedRiver(), "dried_river");
    public static final AtumBiome LIMESTONE_CRAGS = registerBiome(new BiomeLimestoneCrags(), "limestone_crags");
    public static final AtumBiome LIMESTONE_MOUNTAINS = registerBiome(new BiomeLimestoneMountains(), "limestone_mountains");
    public static final AtumBiome OASIS = registerBiome(new BiomeOasis(), "oasis");
    public static final AtumBiome SAND_DUNES = registerBiome(new BiomeSandDunes(), "sand_dunes");
    public static final AtumBiome SAND_HILLS = registerBiome(new BiomeSandHills(), "sand_hills");
    public static final AtumBiome SAND_PLAINS = registerBiome(new BiomeSandPlains(), "sand_plains");

    public static void addBiomeTags() {
        for (AtumBiome biome : AtumRegistry.BIOMES) {
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