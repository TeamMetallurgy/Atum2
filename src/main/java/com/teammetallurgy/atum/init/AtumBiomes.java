package com.teammetallurgy.atum.init;

import com.teammetallurgy.atum.Atum;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;

import java.util.HashSet;
import java.util.Set;

public class AtumBiomes {
    public static final ResourceKey<Biome> DEAD_OASIS = registerBiome("dead_oasis");  //Sub Biome
    public static final ResourceKey<Biome> DENSE_WOODS = registerBiome("dense_woods");
    public static final ResourceKey<Biome> SPARSE_WOODS = registerBiome("sparse_woods");
    public static final ResourceKey<Biome> DRIED_RIVER = registerBiome("dried_river"); //River
    public static final ResourceKey<Biome> LIMESTONE_CRAGS = registerBiome("limestone_crags");
    public static final ResourceKey<Biome> LIMESTONE_MOUNTAINS = registerBiome("limestone_mountains");
    public static final ResourceKey<Biome> OASIS = registerBiome("oasis"); //Sub Biome
    public static final ResourceKey<Biome> SAND_DUNES = registerBiome("sand_dunes");
    public static final ResourceKey<Biome> SAND_HILLS = registerBiome("sand_hills");
    public static final ResourceKey<Biome> SAND_PLAINS = registerBiome("sand_plains");
    public static final ResourceKey<Biome> KARST_CAVES = registerBiome("karst_caves");

    public static ResourceKey<Biome> registerBiome(String biomeName) {
        return ResourceKey.create(Registries.BIOME, new ResourceLocation(Atum.MOD_ID, biomeName));
    }

    /*public static void addBiomeTags() { //TODO Move to json tags, once it's decided on what biomes will be in
        for (ResourceKey<Biome> biome : BIOME_KEYS.keySet()) {
            BiomeDictionary.addTypes(biome, BiomeTags.ATUM);
            if (biome != AtumBiomes.OASIS) {
                BiomeDictionary.addTypes(biome, BiomeDictionary.Type.HOT, BiomeDictionary.Type.SPARSE, BiomeDictionary.Type.DRY);
            }
            if (BIOME_KEYS.get(biome) == BiomeRegion.STRANGE_SANDS) {
                BiomeDictionary.addTypes(biome, BiomeDictionary.Type.SANDY);
            }
        }
        BiomeDictionary.addTypes(DEAD_OASIS, BiomeTags.OASIS, BiomeDictionary.Type.DEAD, BiomeDictionary.Type.RARE);
        BiomeDictionary.addTypes(DENSE_WOODS, BiomeDictionary.Type.FOREST);
        BiomeDictionary.addTypes(SPARSE_WOODS, BiomeDictionary.Type.FOREST);
        BiomeDictionary.addTypes(DRIED_RIVER, BiomeDictionary.Type.RIVER);
        BiomeDictionary.addTypes(LIMESTONE_CRAGS, BiomeDictionary.Type.HILLS, BiomeDictionary.Type.RARE);
        BiomeDictionary.addTypes(LIMESTONE_MOUNTAINS, BiomeDictionary.Type.PEAK);
        BiomeDictionary.addTypes(OASIS, BiomeTags.OASIS, BiomeDictionary.Type.LUSH, BiomeDictionary.Type.WET, BiomeDictionary.Type.RARE);
        BiomeDictionary.addTypes(SAND_DUNES, BiomeDictionary.Type.HILLS);
        BiomeDictionary.addTypes(SAND_HILLS, BiomeDictionary.Type.HILLS);
        BiomeDictionary.addTypes(SAND_PLAINS, BiomeDictionary.Type.PLAINS);
        BiomeDictionary.addTypes(KARST_CAVES, BiomeDictionary.Type.UNDERGROUND);
    }*/

/*   /**
     * Registers a biome
     *
     * @param biome The biome to be registered
     * @param name  The name to register the biome with
     * /
    public static void registerBiome(Biome biome, String name) {
        BIOME_DEFERRED.register(name, () -> biome);
    }

    /**
     * Registers a biome key
     *
     * @param biomeName The name to register the biome key with
     * @return The Biome key that was registered
     * /
    public static ResourceKey<Biome> registerBiomeKey(String biomeName, BiomeRegion biomeRegion) {
        ResourceKey<Biome> biomeKey = ResourceKey.create(ForgeRegistries.Keys.BIOMES, new ResourceLocation(Atum.MOD_ID, biomeName));
        BIOME_KEYS.put(biomeKey, biomeRegion);
        BIOMES.add(biomeKey);
        return biomeKey;
    }

    public static class BiomeTags { //TODO Add new biome tags in json
        public static final BiomeDictionary.Type ATUM = BiomeDictionary.Type.getType("ATUM");
        public static final BiomeDictionary.Type OASIS = BiomeDictionary.Type.getType("OASIS");
    }*/
}