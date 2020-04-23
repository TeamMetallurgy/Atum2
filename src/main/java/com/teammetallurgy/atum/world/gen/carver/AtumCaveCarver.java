package com.teammetallurgy.atum.world.gen.carver;

import com.mojang.datafixers.Dynamic;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.feature.ProbabilityConfig;

import javax.annotation.Nonnull;
import java.util.BitSet;
import java.util.Random;
import java.util.function.Function;

public class AtumCaveCarver extends AtumCarver<ProbabilityConfig> { //Copied from CaveWorldCarver

    public AtumCaveCarver(Function<Dynamic<?>, ? extends ProbabilityConfig> config, int maxHeight) {
        super(config, maxHeight);
    }

    @Override
    public boolean shouldCarve(Random rand, int chunkX, int chunkZ, ProbabilityConfig config) {
        return rand.nextFloat() <= config.probability;
    }

    @Override
    public boolean func_225555_a_(@Nonnull IChunk chunk, @Nonnull Function<BlockPos, Biome> biomePos, Random rand, int i2, int i3, int i4, int i5, int i6, @Nonnull BitSet bitset, @Nonnull ProbabilityConfig config) {
        int i = (this.func_222704_c() * 2 - 1) * 16;
        int j = rand.nextInt(rand.nextInt(rand.nextInt(this.func_222724_a()) + 1) + 1);

        for (int k = 0; k < j; ++k) {
            double d0 = i3 * 16 + rand.nextInt(16);
            double d1 = this.generateCaveStartY(rand);
            double d2 = i4 * 16 + rand.nextInt(16);
            int l = 1;
            if (rand.nextInt(4) == 0) {
                float f1 = 1.0F + rand.nextFloat() * 6.0F;
                this.func_227205_a_(chunk, biomePos, rand.nextLong(), i2, i5, i6, d0, d1, d2, f1, 0.5D, bitset);
                l += rand.nextInt(4);
            }
            for (int k1 = 0; k1 < l; ++k1) {
                float f = rand.nextFloat() * ((float) Math.PI * 2F);
                float f3 = (rand.nextFloat() - 0.5F) / 4.0F;
                float f2 = this.generateCaveRadius(rand);
                int i1 = i - rand.nextInt(i / 4);
                this.func_227206_a_(chunk, biomePos, rand.nextLong(), i2, i5, i6, d0, d1, d2, f2, f, f3, 0, i1, this.func_222725_b(), bitset);
            }
        }
        return true;
    }

    protected int func_222724_a() {
        return 15;
    }

    protected float generateCaveRadius(Random rand) {
        float f = rand.nextFloat() * 2.0F + rand.nextFloat();
        if (rand.nextInt(10) == 0) {
            f *= rand.nextFloat() * rand.nextFloat() * 3.0F + 1.0F;
        }

        return f;
    }

    protected double func_222725_b() {
        return 1.0D;
    }

    protected int generateCaveStartY(Random rand) {
        return rand.nextInt(rand.nextInt(120) + 8);
    }

    protected void func_227205_a_(IChunk chunk, Function<BlockPos, Biome> biomePos, long seed, int i, int i1, int i2, double d2, double d3, double d4, float d5, double d6, BitSet bitSet) {
        double d0 = 1.5D + (double) (MathHelper.sin(((float) Math.PI / 2F)) * d5);
        double d1 = d0 * d6;
        this.func_227208_a_(chunk, biomePos, seed, i, i1, i2, d2 + 1.0D, d3, d4, d0, d1, bitSet);
    }

    protected void func_227206_a_(IChunk chunk, Function<BlockPos, Biome> biomesPos, long seed, int i1, int i2, int i3, double d2, double d3, double d4, float f3, float f4, float f5, int i4, int i5, double d5, BitSet bitSet) {
        Random random = new Random(seed);
        int i = random.nextInt(i5 / 2) + i5 / 4;
        boolean flag = random.nextInt(6) == 0;
        float f = 0.0F;
        float f1 = 0.0F;

        for (int j = i4; j < i5; ++j) {
            double d0 = 1.5D + (double) (MathHelper.sin((float) Math.PI * (float) j / (float) i5) * f3);
            double d1 = d0 * d5;
            float f2 = MathHelper.cos(f5);
            d2 += MathHelper.cos(f4) * f2;
            d3 += MathHelper.sin(f5);
            d4 += MathHelper.sin(f4) * f2;
            f5 = f5 * (flag ? 0.92F : 0.7F);
            f5 = f5 + f1 * 0.1F;
            f4 += f * 0.1F;
            f1 = f1 * 0.9F;
            f = f * 0.75F;
            f1 = f1 + (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 2.0F;
            f = f + (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 4.0F;
            if (j == i && f3 > 1.0F) {
                this.func_227206_a_(chunk, biomesPos, random.nextLong(), i1, i2, i3, d2, d3, d4, random.nextFloat() * 0.5F + 0.5F, f4 - ((float) Math.PI / 2F), f5 / 3.0F, j, i5, 1.0D, bitSet);
                this.func_227206_a_(chunk, biomesPos, random.nextLong(), i1, i2, i3, d2, d3, d4, random.nextFloat() * 0.5F + 0.5F, f4 + ((float) Math.PI / 2F), f5 / 3.0F, j, i5, 1.0D, bitSet);
                return;
            }
            if (random.nextInt(4) != 0) {
                if (!this.func_222702_a(i2, i3, d2, d4, j, i5, f3)) {
                    return;
                }
                this.func_227208_a_(chunk, biomesPos, seed, i1, i2, i3, d2, d3, d4, d0, d1, bitSet);
            }
        }

    }

    @Override
    protected boolean func_222708_a(double p_222708_1_, double p_222708_3_, double p_222708_5_, int p_222708_7_) {
        return p_222708_3_ <= -0.7D || p_222708_1_ * p_222708_1_ + p_222708_3_ * p_222708_3_ + p_222708_5_ * p_222708_5_ >= 1.0D;
    }
}