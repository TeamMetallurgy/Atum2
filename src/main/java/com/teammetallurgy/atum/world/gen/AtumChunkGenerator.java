package com.teammetallurgy.atum.world.gen;

import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.NoiseChunkGenerator;
import net.minecraft.world.gen.OctavesNoiseGenerator;

import javax.annotation.Nonnull;

public class AtumChunkGenerator extends NoiseChunkGenerator<AtumGenSettings> {
    private static final float[] BIOME_WEIGHTS = Util.make(new float[25], (weights) -> { //Copied from OverworldChunkGenerator
        for (int x = -2; x <= 2; ++x) {
            for (int z = -2; z <= 2; ++z) {
                float weight = 10.0F / MathHelper.sqrt((float) (x * x + z * z) + 0.2F);
                weights[x + 2 + (z + 2) * 5] = weight;
            }
        }
    });
    private final OctavesNoiseGenerator depthNoise;

    public AtumChunkGenerator(IWorld world, BiomeProvider biomeProvider, AtumGenSettings settings) {
        super(world, biomeProvider, 4, 8, 256, settings, true);
        this.depthNoise = new OctavesNoiseGenerator(this.randomSeed, 15, 0);
    }

    @Override
    @Nonnull
    protected double[] getBiomeNoiseColumn(int noiseX, int noiseZ) { //Copied from OverworldChunkGenerator
        double[] noise = new double[2];
        float lvt_4_1_ = 0.0F;
        float lvt_5_1_ = 0.0F;
        float lvt_6_1_ = 0.0F;
        int seaLevel = this.getSeaLevel();
        float lvt_9_1_ = this.biomeProvider.getNoiseBiome(noiseX, seaLevel, noiseZ).getDepth();

        for (int x = -2; x <= 2; ++x) {
            for (int z = -2; z <= 2; ++z) {
                Biome biome = this.biomeProvider.getNoiseBiome(noiseX + x, seaLevel, noiseZ + z);
                float depth = biome.getDepth();
                float scale = biome.getScale();

                float weight = BIOME_WEIGHTS[x + 2 + (z + 2) * 5] / (depth + 2.0F);
                if (biome.getDepth() > lvt_9_1_) {
                    weight /= 2.0F;
                }

                lvt_4_1_ += scale * weight;
                lvt_5_1_ += depth * weight;
                lvt_6_1_ += weight;
            }
        }

        lvt_4_1_ /= lvt_6_1_;
        lvt_5_1_ /= lvt_6_1_;
        lvt_4_1_ = lvt_4_1_ * 0.9F + 0.1F;
        lvt_5_1_ = (lvt_5_1_ * 4.0F - 1.0F) / 8.0F;
        noise[0] = (double) lvt_5_1_ + this.getNoiseDepthAt(noiseX, noiseZ);
        noise[1] = lvt_4_1_;
        return noise;
    }

    private double getNoiseDepthAt(int x, int z) { //Copied from OverworldChunkGenerator
        double depth = this.depthNoise.getValue(x * 200, 10.0D, z * 200, 1.0D, 0.0D, true) * 65535.0D / 8000.0D;
        if (depth < 0.0D) {
            depth = -depth * 0.3D;
        }

        depth = depth * 3.0D - 2.0D;
        if (depth < 0.0D) {
            depth /= 28.0D;
        } else {
            if (depth > 1.0D) {
                depth = 1.0D;
            }
            depth /= 40.0D;
        }
        return depth;
    }

    @Override
    protected double func_222545_a(double depth, double scale, int yy) { //Y Offset
        double baseSize = 8.5D;
        double yOffset = ((double) yy - (baseSize + depth * baseSize / 8.0D * 4.0D)) * 12.0D * 128.0D / 256.0D / scale;
        if (yOffset < 0.0D) {
            yOffset *= 4.0D;
        }
        return yOffset;
    }

    @Override
    protected void fillNoiseColumn(@Nonnull double[] noiseColumn, int noiseX, int noiseZ) {
        double xzScale = 684.4119873046875D;
        double yScale = 684.4119873046875D;
        double xzOtherScale = 8.555149841308594D;
        double yOtherScale = 4.277574920654297D;

        this.calcNoiseColumn(noiseColumn, noiseX, noiseZ, xzScale, yScale, xzOtherScale, yOtherScale, 3, -10);
    }

    @Override
    public int getGroundHeight() { //Spawn Height
        return 64;
    }
}