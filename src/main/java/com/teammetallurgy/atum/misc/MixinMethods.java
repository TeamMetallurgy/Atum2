package com.teammetallurgy.atum.misc;

import com.mojang.serialization.Lifecycle;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.world.biome.AtumBiomeSource;
import com.teammetallurgy.atum.world.level.levelgen.AtumChunkGenerator;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.WritableRegistry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

import java.util.OptionalInt;
import java.util.function.Supplier;

public class MixinMethods {

    public static void registerAtumLevelStem(RegistryAccess registryAccess, WritableRegistry<LevelStem> writableRegistry, long seed) {
        LevelStem dimension = createAtumDimension(registryAccess.registryOrThrow(Registry.DIMENSION_TYPE_REGISTRY),
                registryAccess.registryOrThrow(Registry.STRUCTURE_SET_REGISTRY),
                registryAccess.registryOrThrow(Registry.BIOME_REGISTRY),
                registryAccess.registryOrThrow(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY),
                registryAccess.registryOrThrow(Registry.NOISE_REGISTRY),
                seed
        );
        writableRegistry.registerOrOverride(OptionalInt.empty(), Atum.LEVEL_STEM, dimension, Lifecycle.stable());
    }

    public static LevelStem createAtumDimension(Registry<DimensionType> dimensionTypeRegistry, Registry<StructureSet> structureSetRegistry, Registry<Biome> biomeRegistry, Registry<NoiseGeneratorSettings> noiseGeneratorSettingsRegistry, Registry<NormalNoise.NoiseParameters> paramRegistry, long seed) {
        Holder<DimensionType> dimensionType = dimensionTypeRegistry.getHolderOrThrow(Atum.DIMENSION_TYPE);

        ChunkGenerator generator = new AtumChunkGenerator(structureSetRegistry, paramRegistry, new AtumBiomeSource(seed, biomeRegistry), seed, noiseGeneratorSettingsRegistry.getOrCreateHolder(Atum.NOISE_SETTINGS)); //TODO Settings
        return new LevelStem(dimensionType, generator, true);
    }
}