package com.teammetallurgy.atum.world.biome;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teammetallurgy.atum.misc.AtumRegistry;
import com.teammetallurgy.atum.world.gen.layer.AtumLayerUtil;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryLookupCodec;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.layer.Layer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.stream.Collectors;

public class AtumBiomeProvider extends BiomeProvider {
    public static final Codec<AtumBiomeProvider> CODEC = RecordCodecBuilder.create((builder) -> {
        return builder.group(Codec.LONG.fieldOf("seed").stable().forGetter((atumBiomeProvider) -> {
            return atumBiomeProvider.seed;
        }), Codec.BOOL.fieldOf("large_biomes").orElse(false).stable().forGetter((atumBiomeProvider) -> {
            return atumBiomeProvider.largeBiomes;
        }), RegistryLookupCodec.getLookUpCodec(ForgeRegistries.Keys.BIOMES).forGetter((atumBiomeProvider) -> {
            return atumBiomeProvider.lookupRegistry;
        })).apply(builder, builder.stable(AtumBiomeProvider::new));
    });
    private final Layer genBiomes;
    private final long seed;
    private final boolean largeBiomes;
    private final Registry<Biome> lookupRegistry;

    public AtumBiomeProvider(long seed, boolean largeBiomes, Registry<Biome> lookupRegistry) {
        super(AtumRegistry.BIOME_KEYS.stream().map(AtumBiomeProvider::getBiome).collect(Collectors.toList()));
        this.seed = seed;
        this.largeBiomes = largeBiomes;
        this.lookupRegistry = lookupRegistry;
        this.genBiomes = AtumLayerUtil.getNoiseLayer(seed, largeBiomes ? 6 : 4, 6);
    }

    public static Biome getBiome(RegistryKey<Biome> key) {
        Biome biome = ForgeRegistries.BIOMES.getValue(key.getLocation());
        if (biome == null) {
            throw new RuntimeException("Attempted to get unregistered biome " + key);
        }
        return biome;
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