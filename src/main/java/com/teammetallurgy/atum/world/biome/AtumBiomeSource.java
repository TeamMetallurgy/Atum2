package com.teammetallurgy.atum.world.biome;

/*public class AtumBiomeSource extends BiomeSource { //TODO
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

        ImmutableList.Builder<Pair<Climate.ParameterPoint, Holder<Biome>>> builder = ImmutableList.builder();
        new AtumBiomeBuilder().addBiomes(consumer -> builder.add(consumer.mapSecond(biomeRegistry::getOrCreateHolder)));

        this.parameters = new Climate.ParameterList<>(builder.build());
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
}*/