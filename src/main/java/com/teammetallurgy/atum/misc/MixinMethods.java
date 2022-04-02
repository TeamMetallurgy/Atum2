package com.teammetallurgy.atum.misc;

import com.mojang.serialization.Lifecycle;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.world.biome.AtumBiomeSource;
import net.minecraft.core.*;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

import java.util.OptionalInt;

public class MixinMethods {

    public static void registerAtumLevelStem(RegistryAccess registryAccess, long seed) {
        WritableRegistry<LevelStem> registry = new MappedRegistry<>(Registry.LEVEL_STEM_REGISTRY, Lifecycle.experimental(), null);
        LevelStem dimension = createAtumDimension(registryAccess.registryOrThrow(Registry.DIMENSION_TYPE_REGISTRY),
                registryAccess.registryOrThrow(Registry.STRUCTURE_SET_REGISTRY),
                registryAccess.registryOrThrow(Registry.BIOME_REGISTRY),
                registryAccess.registryOrThrow(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY),
                registryAccess.registryOrThrow(Registry.NOISE_REGISTRY),
                seed
        );
        registry.registerOrOverride(OptionalInt.empty(), Atum.LEVEL_STEM, dimension, Lifecycle.stable());
    }

    public static LevelStem createAtumDimension(Registry<DimensionType> dimensionTypeRegistry, Registry<StructureSet> structureSetRegistry, Registry<Biome> biomeRegistry, Registry<NoiseGeneratorSettings> dimensionSettingsRegistry, Registry<NormalNoise.NoiseParameters> paramRegistry, long seed) {
        Holder<DimensionType> dimensionType = dimensionTypeRegistry.getHolderOrThrow(Atum.DIMENSION_TYPE);
        ChunkGenerator generator = new NoiseBasedChunkGenerator(structureSetRegistry, paramRegistry, new AtumBiomeSource(seed, biomeRegistry), seed, dimensionSettingsRegistry.getOrCreateHolder(NoiseGeneratorSettings.OVERWORLD));

        return new LevelStem(dimensionType, generator, true);
    }
}
