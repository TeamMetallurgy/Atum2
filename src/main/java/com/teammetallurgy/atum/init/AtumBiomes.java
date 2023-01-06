package com.teammetallurgy.atum.init;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.misc.AtumConfig;
import com.teammetallurgy.atum.world.biome.AtumBiomeMaker;
import com.teammetallurgy.atum.world.biome.BiomeRegion;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AtumBiomes {
    public static final DeferredRegister<Biome> BIOME_DEFERRED = DeferredRegister.create(ForgeRegistries.BIOMES, Atum.MOD_ID);
    private static final HashMap<ResourceKey<Biome>, BiomeRegion> BIOME_KEYS = new HashMap<>();
    public static final Set<ResourceKey<Biome>> BIOMES = new HashSet<>();
    public static final ResourceKey<Biome> DEAD_OASIS = registerBiome(AtumBiomeMaker.makeDeadOasis("dead_oasis"), "dead_oasis", BiomeRegion.STRANGE_SANDS);  //Sub Biome
    public static final ResourceKey<Biome> DENSE_WOODS = registerBiome(AtumBiomeMaker.makeDenseWoods("dense_woods"), "dense_woods", 10, BiomeRegion.DESSICATED_WOODS);
    public static final ResourceKey<Biome> SPARSE_WOODS = registerBiome(AtumBiomeMaker.makeSparseWoods("sparse_woods"), "sparse_woods", 10, BiomeRegion.DESSICATED_WOODS);
    public static final ResourceKey<Biome> DRIED_RIVER = registerBiome(AtumBiomeMaker.makeDriedRiver("dried_river"), "dried_river", BiomeRegion.STRANGE_SANDS); //River
    public static final ResourceKey<Biome> LIMESTONE_CRAGS = registerBiome(AtumBiomeMaker.makeLimestoneCrags("limestone_crags"), "limestone_crags", 3, BiomeRegion.LIMESTONE_PEAKS);
    public static final ResourceKey<Biome> LIMESTONE_MOUNTAINS = registerBiome(AtumBiomeMaker.makeLimestoneMountain("limestone_mountains"), "limestone_mountains", 5, BiomeRegion.LIMESTONE_PEAKS);
    public static final ResourceKey<Biome> OASIS = registerBiome(AtumBiomeMaker.makeOasis("oasis"), "oasis", BiomeRegion.STRANGE_SANDS); //Sub Biome
    public static final ResourceKey<Biome> SAND_DUNES = registerBiome(AtumBiomeMaker.makeSandDunes("sand_dunes"), "sand_dunes", 15, BiomeRegion.STRANGE_SANDS);
    public static final ResourceKey<Biome> SAND_HILLS = registerBiome(AtumBiomeMaker.makeSandHills("sand_hills"), "sand_hills", 10, BiomeRegion.STRANGE_SANDS);
    public static final ResourceKey<Biome> SAND_PLAINS = registerBiome(AtumBiomeMaker.makeSandPlains("sand_plains"), "sand_plains", 30, BiomeRegion.STRANGE_SANDS);
    public static final ResourceKey<Biome> KARST_CAVES = registerBiome(AtumBiomeMaker.makeKarstCaves("karst_caves"), "karst_caves", 30, BiomeRegion.DEPTHS);

    public static ResourceKey<Biome> registerBiome(Biome biome, String biomeName, BiomeRegion biomeRegion) {
        return registerBiome(biome, biomeName, 0, biomeRegion);
    }

    public static ResourceKey<Biome> registerBiome(Biome biome, String biomeName, int weight, BiomeRegion biomeRegion) {
        registerBiome(biome, biomeName);
        if (weight > 0) {
            new AtumConfig.Biome(AtumConfig.BUILDER, biomeName, weight); //Write config
        }
        return registerBiomeKey(biomeName, biomeRegion);
    }

    /*public static void addBiomeTags() { //TODO Move to json tags
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

    /*@SubscribeEvent
    public static void registerBiomes(RegistryEvent.Register<Biome> event) { //TODO Figure out how this is done now
        Atum.ATUM_MULTI_NOISE = Registry.register(Registry.BIOME_SOURCE, new ResourceLocation(Atum.MOD_ID, "atum_multi_noise"), AtumBiomeSource.CODEC);
        Registry.register(Registry.CHUNK_GENERATOR, new ResourceLocation(Atum.MOD_ID, "atum"), AtumChunkGenerator.CODEC);
        NoiseGeneratorSettings.register(Atum.NOISE_SETTINGS, NoiseGeneratorHelper.atum(false, false));
    }*/

    /**
     * Registers a biome
     *
     * @param biome The biome to be registered
     * @param name  The name to register the biome with
     */
    public static void registerBiome(Biome biome, String name) {
        BIOME_DEFERRED.register(name, () -> biome);
    }

    /**
     * Registers a biome key
     *
     * @param biomeName The name to register the biome key with
     * @return The Biome key that was registered
     */
    public static ResourceKey<Biome> registerBiomeKey(String biomeName, BiomeRegion biomeRegion) {
        ResourceKey<Biome> biomeKey = ResourceKey.create(ForgeRegistries.Keys.BIOMES, new ResourceLocation(Atum.MOD_ID, biomeName));
        BIOME_KEYS.put(biomeKey, biomeRegion);
        BIOMES.add(biomeKey);
        return biomeKey;
    }

    /*public static class BiomeTags { //TODO Add new biome tags in json
        public static final BiomeDictionary.Type ATUM = BiomeDictionary.Type.getType("ATUM");
        public static final BiomeDictionary.Type OASIS = BiomeDictionary.Type.getType("OASIS");
    }*/
}