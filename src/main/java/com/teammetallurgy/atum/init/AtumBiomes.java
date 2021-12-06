package com.teammetallurgy.atum.init;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.misc.AtumConfig;
import com.teammetallurgy.atum.misc.AtumRegistry;
import com.teammetallurgy.atum.world.biome.AtumBiomeMaker;
import com.teammetallurgy.atum.world.biome.BiomeRegion;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID)
public class AtumBiomes {
    public static final ResourceKey<Biome> DEAD_OASIS = registerBiome(AtumBiomeMaker.makeDeadOasis("dead_oasis"), "dead_oasis", BiomeRegion.STRANGE_SANDS);
    public static final ResourceKey<Biome> DENSE_WOODS = registerBiome(AtumBiomeMaker.makeDenseWoods("dense_woods"), "dense_woods", 10, BiomeRegion.DESSICATED_WOODS);
    public static final ResourceKey<Biome> SPARSE_WOODS = registerBiome(AtumBiomeMaker.makeSparseWoods("sparse_woods"), "sparse_woods", 10, BiomeRegion.DESSICATED_WOODS);
    public static final ResourceKey<Biome> DRIED_RIVER = registerBiome(AtumBiomeMaker.makeDriedRiver("dried_river"), "dried_river", BiomeRegion.STRANGE_SANDS);
    public static final ResourceKey<Biome> LIMESTONE_CRAGS = registerBiome(AtumBiomeMaker.makeLimestoneCrags("limestone_crags"), "limestone_crags", 3, BiomeRegion.LIMESTONE_PEAKS);
    public static final ResourceKey<Biome> LIMESTONE_MOUNTAINS = registerBiome(AtumBiomeMaker.makeLimestoneMountain("limestone_mountains"), "limestone_mountains", 5, BiomeRegion.LIMESTONE_PEAKS);
    public static final ResourceKey<Biome> OASIS = registerBiome(AtumBiomeMaker.makeOasis("oasis"), "oasis", BiomeRegion.STRANGE_SANDS);
    public static final ResourceKey<Biome> SAND_DUNES = registerBiome(AtumBiomeMaker.makeSandDunes("sand_dunes"), "sand_dunes", 15, BiomeRegion.STRANGE_SANDS);
    public static final ResourceKey<Biome> SAND_HILLS = registerBiome(AtumBiomeMaker.makeSandHills("sand_hills"), "sand_hills", 10, BiomeRegion.STRANGE_SANDS);
    public static final ResourceKey<Biome> SAND_PLAINS = registerBiome(AtumBiomeMaker.makeSandPlains("sand_plains"), "sand_plains", 30, BiomeRegion.STRANGE_SANDS);

    public static ResourceKey<Biome> registerBiome(Biome biome, String biomeName, BiomeRegion biomeRegion) {
        return registerBiome(biome, biomeName, 0, biomeRegion);
    }

    public static ResourceKey<Biome> registerBiome(Biome biome, String biomeName, int weight, BiomeRegion biomeRegion) {
        AtumRegistry.registerBiome(biome, biomeName);
        if (weight > 0) {
            new AtumConfig.Biome(AtumConfig.BUILDER, biomeName, weight); //Write config
        }
        return AtumRegistry.registerBiomeKey(biomeName);
    }

    public static void addBiomeTags() {
        for (ResourceKey<Biome> biome : AtumRegistry.BIOME_KEYS) {
            BiomeDictionary.addTypes(biome, BiomeTags.ATUM);
            if (biome != AtumBiomes.OASIS) {
                BiomeDictionary.addTypes(biome, BiomeDictionary.Type.HOT, BiomeDictionary.Type.SANDY, BiomeDictionary.Type.SPARSE, BiomeDictionary.Type.DRY);
            }
        }
        BiomeDictionary.addTypes(DEAD_OASIS, BiomeTags.OASIS, BiomeDictionary.Type.DEAD, BiomeDictionary.Type.RARE);
        BiomeDictionary.addTypes(DENSE_WOODS, BiomeDictionary.Type.FOREST);
        BiomeDictionary.addTypes(SPARSE_WOODS, BiomeDictionary.Type.FOREST);
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