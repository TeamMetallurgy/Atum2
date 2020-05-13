package com.teammetallurgy.atum.world.gen;

import com.teammetallurgy.atum.init.AtumFeatures;
import com.teammetallurgy.atum.world.spawner.BanditPatrolSpawner;
import net.minecraft.entity.EntityClassification;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.NoiseChunkGenerator;
import net.minecraft.world.gen.OctavesNoiseGenerator;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;
import java.util.List;

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
    private final BanditPatrolSpawner banditSpawner = new BanditPatrolSpawner();

    public AtumChunkGenerator(IWorld world, BiomeProvider biomeProvider, AtumGenSettings settings) {
        super(world, biomeProvider, 4, 8, 256, settings, true);
        this.depthNoise = new OctavesNoiseGenerator(this.randomSeed, 15, 0);
    }

    public IWorld getWorld() {
        return this.world;
    }

    @Override
    @Nonnull
    protected double[] getBiomeNoiseColumn(int noiseX, int noiseZ) { //Copied from OverworldChunkGenerator
        double[] noise = new double[2];
        float f = 0.0F;
        float f1 = 0.0F;
        float f2 = 0.0F;
        int seaLevel = this.getSeaLevel();
        float biomeNoise = this.biomeProvider.getNoiseBiome(noiseX, seaLevel, noiseZ).getDepth();

        for (int x = -2; x <= 2; ++x) {
            for (int z = -2; z <= 2; ++z) {
                Biome biome = this.biomeProvider.getNoiseBiome(noiseX + x, seaLevel, noiseZ + z);
                float depth = biome.getDepth();
                float scale = biome.getScale();

                float weight = BIOME_WEIGHTS[x + 2 + (z + 2) * 5] / (depth + 2.0F);
                if (biome.getDepth() > biomeNoise) {
                    weight /= 2.0F;
                }
                f += scale * weight;
                f1 += depth * weight;
                f2 += weight;
            }
        }
        f /= f2;
        f1 /= f2;
        f = f * 0.9F + 0.1F;
        f1 = (f1 * 4.0F - 1.0F) / 8.0F;
        noise[0] = (double) f1 + this.getNoiseDepthAt(noiseX, noiseZ);
        noise[1] = f;
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

    @Override
    @Nonnull
    public List<Biome.SpawnListEntry> getPossibleCreatures(@Nonnull EntityClassification type, @Nonnull BlockPos pos) {
        if (AtumFeatures.LIGHTHOUSE.isPositionInStructure(this.world, pos)) {
            if (type == EntityClassification.AMBIENT) {
                return AtumFeatures.LIGHTHOUSE.getSpawnList();
            }
        }
        return super.getPossibleCreatures(type, pos);
    }

    @Override
    public void spawnMobs(@Nonnull ServerWorld serverWorld, boolean spawnHostileMobs, boolean spawnPeacefulMobs) {
        this.banditSpawner.tick(serverWorld, spawnHostileMobs);
    }
}