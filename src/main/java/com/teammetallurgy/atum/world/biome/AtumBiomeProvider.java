package com.teammetallurgy.atum.world.biome;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teammetallurgy.atum.misc.AtumRegistry;
import com.teammetallurgy.atum.world.gen.layer.AtumLayerUtil;
import net.minecraft.util.SharedConstants;
import net.minecraft.util.Util;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryLookupCodec;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeRegistry;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.layer.Layer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import java.util.Random;
import java.util.stream.Collectors;

public class AtumBiomeProvider extends BiomeProvider {
    public static final Codec<AtumBiomeProvider> CODEC = RecordCodecBuilder.create((builder) -> {
        return builder.group(Codec.BOOL.fieldOf("large_biomes").orElse(false).stable().forGetter((atumBiomeProvider) -> {
            return atumBiomeProvider.largeBiomes;
        }), RegistryLookupCodec.getLookUpCodec(Registry.BIOME_KEY).forGetter((atumBiomeProvider) -> {
            return atumBiomeProvider.lookupRegistry;
        })).apply(builder, builder.stable(AtumBiomeProvider::new));
    });
    private final Layer genBiomes;
    private final boolean largeBiomes;
    private final Registry<Biome> lookupRegistry;

    public AtumBiomeProvider(boolean largeBiomes, Registry<Biome> lookupRegistry) {
        super(AtumRegistry.BIOME_KEYS.stream().map(lookupRegistry::getOrThrow).collect(Collectors.toList()));
        long seed = (new Random()).nextLong(); //Workaround for vanilla bug, not applying seeds to biomes properly. TODO Revisit in 1.17
        this.largeBiomes = largeBiomes;
        this.lookupRegistry = lookupRegistry;
        this.genBiomes = AtumLayerUtil.getNoiseLayer(seed, largeBiomes ? 6 : 4, 6, lookupRegistry);
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
        return new AtumBiomeProvider(this.largeBiomes, this.lookupRegistry);
    }

    /**
     * Returns the correct dynamic registry biome instead of using func_242936_a method
     * which actually returns the incorrect biome instance because it resolves the biome
     * with WorldGenRegistry first instead of the dynamic registry which is... bad.
     */
    @Override
    @Nonnull
    public Biome getNoiseBiome(int x, int y, int z) {
        int k = this.genBiomes.field_215742_b.getValue(x, z);
        Biome biome = this.lookupRegistry.getByValue(k);

        if (biome != null) {
            // Dynamic Registry biome (this should always be returned ideally)
            return biome;
        }
        else {
            //fallback to WorldGenRegistry registry if dynamic registry doesn't have biome
            if (SharedConstants.developmentMode) {
                throw Util.pauseDevMode(new IllegalStateException("Unknown biome id: " + k));
            }
            else {
                biome = this.lookupRegistry.getValueForKey(BiomeRegistry.getKeyFromID(0));
                if(biome == null){
                    // If this is reached, it is the end of the world lol
                    return BiomeRegistry.THE_VOID;
                }
                else{
                    // WorldGenRegistry biome (this is not good but we need to return something)
                    return biome;
                }
            }
        }
    }
}