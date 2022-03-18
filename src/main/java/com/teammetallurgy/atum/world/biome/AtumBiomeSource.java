package com.teammetallurgy.atum.world.biome;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teammetallurgy.atum.world.level.biome.AtumBiomeBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.RegistryOps;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.VisibleForDebug;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.MultiNoiseBiomeSource;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public class AtumBiomeSource extends BiomeSource {
    public static final MapCodec<AtumBiomeSource> DIRECT_CODEC = RecordCodecBuilder.mapCodec((k) -> {
        return k.group(ExtraCodecs.nonEmptyList(RecordCodecBuilder.<Pair<Climate.ParameterPoint, Holder<Biome>>>create((p) -> {
            return p.group(Climate.ParameterPoint.CODEC.fieldOf("parameters").forGetter(Pair::getFirst), Biome.CODEC.fieldOf("biome").forGetter(Pair::getSecond)).apply(p, Pair::of);
        }).listOf()).xmap(Climate.ParameterList::new, Climate.ParameterList::values).fieldOf("biomes").forGetter((biomeSource) -> {
            return biomeSource.parameters;
        })).apply(k, AtumBiomeSource::new);
    });
    public static final Codec<AtumBiomeSource> ATUM_BIOME_SOURCE_CODEC = DIRECT_CODEC.codec();
    private final Climate.ParameterList<Holder<Biome>> parameters;

    AtumBiomeSource(Climate.ParameterList<Holder<Biome>> parameters) {
        super(parameters.values().stream().map(Pair::getSecond));
        this.parameters = parameters;

        ImmutableList.Builder<Pair<Climate.ParameterPoint, Supplier<Biome>>> builder = ImmutableList.builder();
        new AtumBiomeBuilder().addBiomes(consumer -> builder.add(consumer.mapSecond(key -> () -> BuiltinRegistries.BIOME.getOrThrow(key))));
    }

    @Override
    @Nonnull
    protected Codec<? extends BiomeSource> codec() {
        return ATUM_BIOME_SOURCE_CODEC;
    }

    @Override
    @Nonnull
    public BiomeSource withSeed(long seed) {
        return this;
    }

    @Override
    @Nonnull
    public Holder<Biome> getNoiseBiome(int i, int i1, int i2, Climate.Sampler sampler) {
        return this.getNoiseBiome(sampler.sample(i, i1, i2));
    }

    @VisibleForDebug
    public Holder<Biome> getNoiseBiome(Climate.TargetPoint targetPoint) {
        return this.parameters.findValue(targetPoint);
    }
}