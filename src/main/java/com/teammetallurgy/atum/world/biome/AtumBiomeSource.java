package com.teammetallurgy.atum.world.biome;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teammetallurgy.atum.init.AtumBiomes;
import com.teammetallurgy.atum.world.level.biome.AtumBiomeBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.RegistryOps;
import net.minecraft.util.VisibleForDebug;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.function.Supplier;

public class AtumBiomeSource extends BiomeSource {
    public static final Codec<AtumBiomeSource> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(
                Codec.LONG.fieldOf("seed").stable().forGetter(b -> b.seed),
                RegistryOps.retrieveRegistry(Registry.BIOME_REGISTRY).forGetter(b -> b.biomeRegistry)
        ).apply(instance, instance.stable(AtumBiomeSource::new));
    });
    private final Climate.ParameterList<Holder<Biome>> parameters;
    private final Registry<Biome> biomeRegistry;
    private final long seed;

    public AtumBiomeSource(long seed, Registry<Biome> biomeRegistry) {
        super(AtumBiomes.BIOMES.stream().filter(Objects::nonNull).map(biomeRegistry::getOrCreateHolder));
        this.seed = seed;
        this.biomeRegistry = biomeRegistry;

        ImmutableList.Builder<Pair<Climate.ParameterPoint, Supplier<Biome>>> builder = ImmutableList.builder();
        new AtumBiomeBuilder().addBiomes(consumer -> builder.add(consumer.mapSecond(key -> () -> biomeRegistry.getOrThrow(key))));

        this.parameters = new Climate.ParameterList(builder.build());
    }

    @Override
    @Nonnull
    protected Codec<? extends BiomeSource> codec() {
        return CODEC;
    }

    @Override
    @Nonnull
    public BiomeSource withSeed(long seed) {
        return new AtumBiomeSource(seed, this.biomeRegistry);
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