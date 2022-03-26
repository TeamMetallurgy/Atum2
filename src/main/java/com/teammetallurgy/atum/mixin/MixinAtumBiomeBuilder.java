package com.teammetallurgy.atum.mixin;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Lifecycle;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.world.biome.AtumBiomeSource;
import net.minecraft.core.*;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.RegistryLoader;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.OptionalInt;

@Mixin(RegistryLoader.class)
public class MixinAtumBiomeBuilder {

    @Inject(method = "overrideElementFromResources(Lnet/minecraft/core/WritableRegistry;Lnet/minecraft/resources/ResourceKey;Lcom/mojang/serialization/Codec;Lnet/minecraft/resources/ResourceKey;Lcom/mojang/serialization/DynamicOps;)Lcom/mojang/serialization/DataResult;", at = @At("HEAD"))
    void register(WritableRegistry<?> writableRegistry, ResourceKey<? extends Registry<?>> registryResourceKey, Codec<?> codec, ResourceKey<?> resourceKey, DynamicOps<JsonElement> dynamicOps, CallbackInfoReturnable<DataResult<Holder<?>>> cir) {
        //System.out.println(writableRegistry.get(Atum.LOCATION));
        if (registryResourceKey == Registry.LEVEL_STEM_REGISTRY && writableRegistry.get(Atum.LOCATION) == null) {
            System.out.println("I GOT CALLED HALLEAWUASDHCSAHBdfsa HDSAH");
            this.addDimensions((MappedRegistry<LevelStem>) writableRegistry);
        }
    }

    private void addDimensions(MappedRegistry<LevelStem> registry) {
        RegistryAccess registryAccess = BuiltinRegistries.ACCESS;
        LevelStem dimension = createDimension(registryAccess.registryOrThrow(Registry.DIMENSION_TYPE_REGISTRY),
                registryAccess.registryOrThrow(Registry.STRUCTURE_SET_REGISTRY),
                registryAccess.registryOrThrow(Registry.BIOME_REGISTRY),
                registryAccess.registryOrThrow(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY),
                registryAccess.registryOrThrow(Registry.NOISE_REGISTRY)
        );
        registry.registerOrOverride(OptionalInt.empty(), Atum.LEVEL_STEM, dimension, Lifecycle.stable());
    }

    private LevelStem createDimension(Registry<DimensionType> dimensionTypeRegistry, Registry<StructureSet> structureSetRegistry, Registry<Biome> biomeRegistry, Registry<NoiseGeneratorSettings> dimensionSettingsRegistry, Registry<NormalNoise.NoiseParameters> paramRegistry) {
        Holder<DimensionType> dimensionType = dimensionTypeRegistry.getHolderOrThrow(Atum.DIMENSION_TYPE);
        ChunkGenerator generator = new NoiseBasedChunkGenerator(structureSetRegistry, paramRegistry, AtumBiomeSource.Preset.ATUM.biomeSource(biomeRegistry, true), 0L, dimensionSettingsRegistry.getOrCreateHolder(NoiseGeneratorSettings.OVERWORLD)); //TODO Seed for ChunkGenerator? Or is the one passed in LevelStem fine?

        return new LevelStem(dimensionType, generator, true);
    }
}