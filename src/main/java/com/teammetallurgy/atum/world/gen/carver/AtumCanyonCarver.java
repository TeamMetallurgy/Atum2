/*
package com.teammetallurgy.atum.world.gen.carver;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.feature.configurations.ProbabilityFeatureConfiguration;

import javax.annotation.Nonnull;
import java.util.BitSet;
import java.util.Random;
import java.util.function.Function;

public class AtumCanyonCarver extends AtumCarver<ProbabilityFeatureConfiguration> { //Copied from CanyonWorldCarver
    private final float[] size = new float[1024];

    public AtumCanyonCarver(Codec<ProbabilityFeatureConfiguration> codec) { //TODO. Is this still needed, or can vanilla carver be used?
        super(codec, 256);
    }

    @Override
    public boolean isStartChunk(Random rand, int chunkX, int chunkZ, ProbabilityFeatureConfiguration config) {
        return rand.nextFloat() <= config.probability;
    }

    @Override
    public boolean carve(@Nonnull ChunkAccess chunk, @Nonnull Function<BlockPos, Biome> biomePos, Random rand, int i1, int i2, int i3, int i4, int i5, @Nonnull BitSet bitSet, @Nonnull ProbabilityFeatureConfiguration config) {
        int i = (this.getRange() * 2 - 1) * 16;
        double d0 = i2 * 16 + rand.nextInt(16);
        double d1 = rand.nextInt(rand.nextInt(40) + 8) + 20;
        double d2 = i3 * 16 + rand.nextInt(16);
        float f = rand.nextFloat() * ((float)Math.PI * 2F);
        float f1 = (rand.nextFloat() - 0.5F) * 2.0F / 8.0F;
        float f2 = (rand.nextFloat() * 2.0F + rand.nextFloat()) * 2.0F;
        int j = i - rand.nextInt(i / 4);
        this.genCanyon(chunk, biomePos, rand.nextLong(), i1, i4, i5, d0, d1, d2, f2, f, f1, 0, j, 3.0D, bitSet);
        return true;
    }

    private void genCanyon(ChunkAccess chunk, Function<BlockPos, Biome> biomePos, long seed, int i1, int i2, int i3, double d2, double d3, double d4, float f5, float f6, float f7, int i4, int i5, double d5, BitSet bitSet) {
        Random random = new Random(seed);
        float f = 1.0F;

        for(int i = 0; i < 256; ++i) {
            if (i == 0 || random.nextInt(3) == 0) {
                f = 1.0F + random.nextFloat() * random.nextFloat();
            }

            this.size[i] = f * f;
        }

        float f4 = 0.0F;
        float f1 = 0.0F;

        for(int j = i4; j < i5; ++j) {
            double d0 = 1.5D + (double)(Mth.sin((float)j * (float)Math.PI / (float)i5) * f5);
            double d1 = d0 * d5;
            d0 = d0 * ((double)random.nextFloat() * 0.25D + 0.75D);
            d1 = d1 * ((double)random.nextFloat() * 0.25D + 0.75D);
            float f2 = Mth.cos(f7);
            float f3 = Mth.sin(f7);
            d2 += Mth.cos(f6) * f2;
            d3 += f3;
            d4 += Mth.sin(f6) * f2;
            f7 = f7 * 0.7F;
            f7 = f7 + f1 * 0.05F;
            f6 += f4 * 0.05F;
            f1 = f1 * 0.8F;
            f4 = f4 * 0.5F;
            f1 = f1 + (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 2.0F;
            f4 = f4 + (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 4.0F;
            if (random.nextInt(4) != 0) {
                if (!this.canReach(i2, i3, d2, d4, j, i5, f5)) {
                    return;
                }
                this.carveSphere(chunk, biomePos, seed, i1, i2, i3, d2, d3, d4, d0, d1, bitSet);
            }
        }

    }

    @Override
    protected boolean skip(double d, double d1, double d2, int i) {
        return (d * d + d2 * d2) * (double)this.size[i - 1] + d1 * d1 / 6.0D >= 1.0D;
    }
}*/
