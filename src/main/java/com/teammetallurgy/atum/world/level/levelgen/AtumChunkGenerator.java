//package com.teammetallurgy.atum.world.level.levelgen;
//
//import com.mojang.serialization.Codec;
//import com.mojang.serialization.codecs.RecordCodecBuilder;
//import net.minecraft.core.Holder;
//import net.minecraft.core.Registry;
//import net.minecraft.resources.RegistryOps;
//import net.minecraft.world.level.biome.BiomeSource;
//import net.minecraft.world.level.chunk.ChunkGenerator;
//import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
//import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
//import net.minecraft.world.level.levelgen.structure.StructureSet;
//import net.minecraft.world.level.levelgen.synth.NormalNoise;
//
//import javax.annotation.Nonnull;
//
//public class AtumChunkGenerator extends NoiseBasedChunkGenerator { //TODO
//    public static final Codec<AtumChunkGenerator> CODEC = RecordCodecBuilder.create((instance) -> {
//        return commonCodec(instance).and(instance.group(RegistryOps.retrieveRegistry(Registry.NOISE_REGISTRY).forGetter((p_188716_) -> {
//            return p_188716_.noises;
//        }), BiomeSource.CODEC.fieldOf("biome_source").forGetter((p_188711_) -> {
//            return p_188711_.biomeSource;
//        }), Codec.LONG.fieldOf("seed").stable().forGetter((p_188690_) -> {
//            return p_188690_.seed;
//        }), NoiseGeneratorSettings.CODEC.fieldOf("settings").forGetter((p_204585_) -> {
//            return p_204585_.settings;
//        }))).apply(instance, instance.stable(AtumChunkGenerator::new));
//    });
//
//    public AtumChunkGenerator(Registry<StructureSet> structureSetRegistry, Registry<NormalNoise.NoiseParameters> noiseParametersRegistry, BiomeSource biomeSource, long seed, Holder<NoiseGeneratorSettings> noiseGeneratorSettingsHolder) {
//        super(structureSetRegistry, noiseParametersRegistry, biomeSource, seed, noiseGeneratorSettingsHolder);
//    }
//
//    @Override
//    @Nonnull
//    protected Codec<? extends ChunkGenerator> codec() {
//        return CODEC;
//    }
//
//    @Override
//    @Nonnull
//    public ChunkGenerator withSeed(long seed) {
//        return new NoiseBasedChunkGenerator(this.structureSets, this.noises, this.biomeSource.withSeed(seed), seed, this.settings);
//    }
//}