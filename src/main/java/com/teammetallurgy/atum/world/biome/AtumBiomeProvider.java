package com.teammetallurgy.atum.world.biome;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teammetallurgy.atum.misc.AtumRegistry;
import com.teammetallurgy.atum.world.gen.layer.AtumLayerUtil;
import net.minecraft.SharedConstants;
import net.minecraft.Util;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.biome.Biomes;
import net.minecraft.resources.RegistryLookupCodec;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import java.util.stream.Collectors;

public class AtumBiomeProvider extends BiomeSource {
    public static final Codec<AtumBiomeProvider> CODEC = RecordCodecBuilder.create((builder) -> {
        return builder.group(Codec.BOOL.fieldOf("large_biomes").orElse(false).stable().forGetter((atumBiomeProvider) -> {
            return atumBiomeProvider.largeBiomes;
        }), RegistryLookupCodec.create(Registry.BIOME_REGISTRY).forGetter((atumBiomeProvider) -> {
            return atumBiomeProvider.lookupRegistry;
        }), Codec.LONG.fieldOf("seed").orElseGet(WorldSeedHolder::getSeed).forGetter((chunkGenerator) -> {
            return chunkGenerator.seed;
        })).apply(builder, builder.stable(AtumBiomeProvider::new));
    });
    private final Layer genBiomes;
    private final boolean largeBiomes;
    private final Registry<Biome> lookupRegistry;
    private final long seed;

    public AtumBiomeProvider(boolean largeBiomes, Registry<Biome> lookupRegistry, long seed) {
        super(AtumRegistry.BIOME_KEYS.stream().map(lookupRegistry::getOrThrow).collect(Collectors.toList()));

        this.seed = seed;
        this.largeBiomes = largeBiomes;
        this.lookupRegistry = lookupRegistry;
        this.genBiomes = AtumLayerUtil.getNoiseLayer(seed, largeBiomes ? 6 : 4, 6, lookupRegistry);
    }

    @Override
    @Nonnull
    protected Codec<? extends BiomeSource> codec() {
        return CODEC;
    }

    @Override
    @Nonnull
    @OnlyIn(Dist.CLIENT)
    public BiomeSource withSeed(long seed) {
        return new AtumBiomeProvider(this.largeBiomes, this.lookupRegistry, this.seed);
    }

    /**
     * Returns the correct dynamic registry biome instead of using get method
     * which actually returns the incorrect biome instance because it resolves the biome
     * with WorldGenRegistry first instead of the dynamic registry which is... bad.
     */
    @Override
    @Nonnull
    public Biome getNoiseBiome(int x, int y, int z) {
        int k = this.genBiomes.area.get(x, z);
        Biome biome = this.lookupRegistry.byId(k);

        if (biome != null) {
            // Dynamic Registry biome (this should always be returned ideally)
            return biome;
        } else {
            //fallback to WorldGenRegistry registry if dynamic registry doesn't have biome
            if (SharedConstants.IS_RUNNING_IN_IDE) {
                throw Util.pauseInIde(new IllegalStateException("Unknown biome id: " + k));
            } else {
                biome = this.lookupRegistry.get(Biomes.byId(0));
                if (biome == null) {
                    // If this is reached, it is the end of the world lol
                    return Biomes.THE_VOID;
                } else {
                    // WorldGenRegistry biome (this is not good but we need to return something)
                    return biome;
                }
            }
        }
    }
}