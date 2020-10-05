package com.teammetallurgy.atum.world.biome;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teammetallurgy.atum.init.AtumBiomes;
import com.teammetallurgy.atum.world.gen.layer.AtumLayerUtil;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryLookupCodec;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.layer.Layer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import java.util.List;

public class AtumBiomeProvider extends BiomeProvider {
    public static final Codec<AtumBiomeProvider> CODEC = RecordCodecBuilder.create((builder) -> {
        return builder.group(Codec.LONG.fieldOf("seed").stable().forGetter((atumBiomeProvider) -> {
            return atumBiomeProvider.seed;
        }), Codec.BOOL.fieldOf("large_biomes").orElse(false).stable().forGetter((atumBiomeProvider) -> {
            return atumBiomeProvider.largeBiomes;
        }), RegistryLookupCodec.getLookUpCodec(Registry.BIOME_KEY).forGetter((atumBiomeProvider) -> {
            return atumBiomeProvider.lookupRegistry;
        })).apply(builder, builder.stable(AtumBiomeProvider::new));
    });
    private final Layer genBiomes;
    private static final List<RegistryKey<Biome>> biomes = Lists.newArrayList(AtumBiomes.DEAD_OASIS, AtumBiomes.DEADWOOD_FOREST, AtumBiomes.LIMESTONE_MOUNTAINS, AtumBiomes.OASIS, AtumBiomes.SAND_DUNES, AtumBiomes.SAND_HILLS, AtumBiomes.SAND_PLAINS);
    private final long seed;
    private final boolean largeBiomes;
    private final Registry<Biome> lookupRegistry;

    public AtumBiomeProvider(long seed, boolean largeBiomes, Registry<Biome> lookupRegistry) {
        super(biomes.stream().map((key) -> () -> lookupRegistry.getOrThrow(key))); //Biomes to check if it can generate structures
        this.seed = seed;
        this.largeBiomes = largeBiomes;
        this.lookupRegistry = lookupRegistry;
        this.genBiomes = AtumLayerUtil.getNoiseLayer(seed, largeBiomes ? 6 : 4, 4);
    }

    @Override
    @Nonnull
    protected Codec<? extends BiomeProvider> getBiomeProviderCodec() {
        return CODEC;
    }

    @Override
    @Nonnull
    @OnlyIn(Dist.CLIENT)
    public BiomeProvider getBiomeProvider(long seed) {
        return new AtumBiomeProvider(seed, this.largeBiomes, this.lookupRegistry);
    }

    @Override
    @Nonnull
    public Biome getNoiseBiome(int x, int y, int z) {
        return this.genBiomes.func_242936_a(this.lookupRegistry, x, z);
    }
}