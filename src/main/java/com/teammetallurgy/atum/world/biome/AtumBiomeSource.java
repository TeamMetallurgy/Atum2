package com.teammetallurgy.atum.world.biome;

import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.VisibleForDebug;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.MultiNoiseBiomeSource;

import javax.annotation.Nonnull;
import java.util.Map;
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
    public static final Codec<AtumBiomeSource> CODEC = Codec.mapEither(PresetInstance.CODEC, DIRECT_CODEC).xmap((e) -> {
        return e.map(PresetInstance::biomeSource, Function.identity());
    }, (biomeSource) -> {
        return biomeSource.preset().map(Either::<PresetInstance, AtumBiomeSource>left).orElseGet(() -> {
            return Either.right(biomeSource);
        });
    }).codec();
    private final Climate.ParameterList<Holder<Biome>> parameters;
    private final Optional<PresetInstance> preset;

    private AtumBiomeSource(Climate.ParameterList<Holder<Biome>> parameters) {
        this(parameters, Optional.empty());
    }

    AtumBiomeSource(Climate.ParameterList<Holder<Biome>> parameters, Optional<PresetInstance> preset) {
        super(parameters.values().stream().map(Pair::getSecond));
        this.preset = preset;
        this.parameters = parameters;
    }

    @Override
    @Nonnull
    protected Codec<? extends BiomeSource> codec() {
        return CODEC;
    }

    @Override
    @Nonnull
    public BiomeSource withSeed(long seed) {
        return this;
    }

    private Optional<PresetInstance> preset() {
        return this.preset;
    }

    public boolean stable(Preset p_187064_) {
        return this.preset.isPresent() && Objects.equals(this.preset.get().preset(), p_187064_);
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

    public static class Preset {
        static final Map<ResourceLocation, Preset> BY_NAME = Maps.newHashMap();
        final ResourceLocation name;
        private final Function<Registry<Biome>, Climate.ParameterList<Holder<Biome>>> parameterSource;

        public Preset(ResourceLocation resourceLocation, Function<Registry<Biome>, Climate.ParameterList<Holder<Biome>>> parameterSource) {
            this.name = resourceLocation;
            this.parameterSource = parameterSource;
            BY_NAME.put(resourceLocation, this);
        }

        AtumBiomeSource biomeSource(PresetInstance presetInstance, boolean b) {
            Climate.ParameterList<Holder<Biome>> parameterlist = this.parameterSource.apply(presetInstance.biomes());
            return new AtumBiomeSource(parameterlist, b ? Optional.of(presetInstance) : Optional.empty());
        }

        public AtumBiomeSource biomeSource(Registry<Biome> registry, boolean b) {
            return this.biomeSource(new PresetInstance(this, registry), b);
        }

        public AtumBiomeSource biomeSource(Registry<Biome> registry) {
            return this.biomeSource(registry, true);
        }
    }

    static record PresetInstance(Preset preset, Registry<Biome> biomes) {
        public static final MapCodec<PresetInstance> CODEC = RecordCodecBuilder.mapCodec((p_48558_) -> {
            return p_48558_.group(ResourceLocation.CODEC.flatXmap((p_151869_) -> {
                return Optional.ofNullable(Preset.BY_NAME.get(p_151869_)).map(DataResult::success).orElseGet(() -> {
                    return DataResult.error("Unknown preset: " + p_151869_);
                });
            }, (p_151867_) -> {
                return DataResult.success(p_151867_.name);
            }).fieldOf("preset").stable().forGetter(PresetInstance::preset), RegistryOps.retrieveRegistry(Registry.BIOME_REGISTRY).forGetter(PresetInstance::biomes)).apply(p_48558_, p_48558_.stable(PresetInstance::new));
        });

        public AtumBiomeSource biomeSource() {
            return this.preset.biomeSource(this, true);
        }
    }
}