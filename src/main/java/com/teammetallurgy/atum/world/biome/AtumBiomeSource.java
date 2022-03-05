package com.teammetallurgy.atum.world.biome;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teammetallurgy.atum.Atum;
import net.minecraft.core.Registry;
import net.minecraft.resources.RegistryLookupCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.VisibleForDebug;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public class AtumBiomeSource extends BiomeSource {
    public static final MapCodec<AtumBiomeSource> DIRECT_CODEC = RecordCodecBuilder.mapCodec((p_187070_) -> {
        return p_187070_.group(ExtraCodecs.nonEmptyList(RecordCodecBuilder.<Pair<Climate.ParameterPoint, Supplier<Biome>>>create((p_187078_) -> {
            return p_187078_.group(Climate.ParameterPoint.CODEC.fieldOf("parameters").forGetter(Pair::getFirst), Biome.CODEC.fieldOf("biome").forGetter(Pair::getSecond)).apply(p_187078_, Pair::of);
        }).listOf()).xmap(Climate.ParameterList::new, Climate.ParameterList::values).fieldOf("biomes").forGetter((p_187080_) -> {
            return p_187080_.parameters;
        })).apply(p_187070_, AtumBiomeSource::new);
    });
    public static final Codec<AtumBiomeSource> CODEC = Codec.mapEither(PresetInstance.CODEC, DIRECT_CODEC).xmap((p_187068_) -> {
        return p_187068_.map(PresetInstance::biomeSource, Function.identity());
    }, (p_187066_) -> {
        return p_187066_.preset().map(Either::<PresetInstance, AtumBiomeSource>left).orElseGet(() -> {
            return Either.right(p_187066_);
        });
    }).codec();
    private final Climate.ParameterList<Supplier<Biome>> parameters;
    private final Optional<PresetInstance> preset;

    private AtumBiomeSource(Climate.ParameterList<Supplier<Biome>> parameters) {
        this(parameters, Optional.empty());
    }

    AtumBiomeSource(Climate.ParameterList<Supplier<Biome>> parameters, Optional<PresetInstance> preset) {
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

    @Override
    @Nonnull
    public Biome getNoiseBiome(int i, int i1, int i2, Climate.Sampler sampler) {
        return this.getNoiseBiome(sampler.sample(i, i1, i2));
    }

    private Optional<PresetInstance> preset() {
        return this.preset;
    }

    public boolean stable(Preset p_187064_) {
        return this.preset.isPresent() && Objects.equals(this.preset.get().preset(), p_187064_);
    }

    @VisibleForDebug
    public Biome getNoiseBiome(Climate.TargetPoint targetPoint) {
        return this.parameters.findValue(targetPoint, () -> net.minecraft.data.worldgen.biome.Biomes.THE_VOID).get();
    }

    public static class Preset {
        static final Map<ResourceLocation, Preset> BY_NAME = Maps.newHashMap();
        public static final Preset ATUM = new Preset(new ResourceLocation(Atum.MOD_ID, "atum"), (p_187108_) -> {
            ImmutableList.Builder<Pair<Climate.ParameterPoint, Supplier<Biome>>> builder = ImmutableList.builder();
            (new AtumBiomeBuilder()).addBiomes((p_187098_) -> {
                builder.add(p_187098_.mapSecond((p_187103_) -> () -> p_187108_.getOrThrow(p_187103_)));
            });
            return new Climate.ParameterList<>(builder.build());
        });
        final ResourceLocation name;
        private final Function<Registry<Biome>, Climate.ParameterList<Supplier<Biome>>> parameterSource;

        public Preset(ResourceLocation p_187090_, Function<Registry<Biome>, Climate.ParameterList<Supplier<Biome>>> p_187091_) {
            this.name = p_187090_;
            this.parameterSource = p_187091_;
            BY_NAME.put(p_187090_, this);
        }

        AtumBiomeSource biomeSource(PresetInstance p_187093_, boolean p_187094_) {
            Climate.ParameterList<Supplier<Biome>> parameterlist = this.parameterSource.apply(p_187093_.biomes());
            return new AtumBiomeSource(parameterlist, p_187094_ ? Optional.of(p_187093_) : Optional.empty());
        }

        public AtumBiomeSource biomeSource(Registry<Biome> p_187105_, boolean p_187106_) {
            return this.biomeSource(new PresetInstance(this, p_187105_), p_187106_);
        }

        public AtumBiomeSource biomeSource(Registry<Biome> p_187100_) {
            return this.biomeSource(p_187100_, true);
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
            }).fieldOf("preset").stable().forGetter(PresetInstance::preset), RegistryLookupCodec.create(Registry.BIOME_REGISTRY).forGetter(PresetInstance::biomes)).apply(p_48558_, p_48558_.stable(PresetInstance::new));
        });

        public AtumBiomeSource biomeSource() {
            return this.preset.biomeSource(this, true);
        }
    }
}