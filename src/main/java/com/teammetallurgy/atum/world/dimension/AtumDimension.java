package com.teammetallurgy.atum.world.dimension;

import com.teammetallurgy.atum.world.gen.AtumChunkGeneratorType;
import net.minecraft.block.BlockState;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.ChunkGeneratorType;
import net.minecraft.world.gen.Heightmap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class AtumDimension extends Dimension {
    
    public AtumDimension(World world, DimensionType dimensionType) {
        super(world, dimensionType, 0.0F /*Brightness. Look into?*/);
    }

    @Override
    @Nonnull
    public ChunkGenerator<?> createChunkGenerator() {
        BiomeProviderType<AtumBiomeProviderSettings , AtumBiomeProvider> biomeType = AtumBiomeProviderTypes.ATUM;
        ChunkGeneratorType<AtumGenSettings, AtumChunkGenerato> chunkType = AtumChunkGeneratorType.ATUM;
        AtumGenSettings genSettings = (AtumGenSettings) chunkType.createSettings();
        AtumBiomeProviderSettings  biomeSettings = biomeType.createSettings().setWorldInfo(this.world.getWorldInfo()).setGeneratorSettings(genSettings);
        return chunkType.create(this.world, biomeType.create(biomeSettings), genSettings);
    }

    @Override
    @Nullable
    public BlockPos findSpawn(@Nonnull ChunkPos chunkPos, boolean checkValid) { //Copied from OverworldDimension
        for(int i = chunkPos.getXStart(); i <= chunkPos.getXEnd(); ++i) {
            for(int j = chunkPos.getZStart(); j <= chunkPos.getZEnd(); ++j) {
                BlockPos blockpos = this.findSpawn(i, j, checkValid);
                if (blockpos != null) {
                    return blockpos;
                }
            }
        }

        return null;
    }

    @Override
    @Nullable
    public BlockPos findSpawn(int posX, int posZ, boolean checkValid) { //Copied from OverworldDimension. Else if removed
        BlockPos.Mutable mutablePos = new BlockPos.Mutable(posX, 0, posZ);
        Biome biome = this.world.getBiome(mutablePos);
        BlockState state = biome.getSurfaceBuilderConfig().getTop();
        if (checkValid && !state.getBlock().isIn(BlockTags.VALID_SPAWN)) {
            return null;
        } else {
            Chunk chunk = this.world.getChunk(posX >> 4, posZ >> 4);
            int i = chunk.getTopBlockY(Heightmap.Type.MOTION_BLOCKING, posX & 15, posZ & 15);
            if (i < 0) {
                return null;
            } else {
                for (int j = i + 1; j >= 0; --j) {
                    mutablePos.setPos(posX, j, posZ);
                    BlockState stateMutable = this.world.getBlockState(mutablePos);
                    if (!stateMutable.getFluidState().isEmpty()) {
                        break;
                    }
                    if (stateMutable.equals(state)) {
                        return mutablePos.up().toImmutable();
                    }
                }
                return null;
            }
        }
    }

    @Override
    public float calculateCelestialAngle(long worldTime, float partialTicks) { //Copied from OverworldDimension
        double d0 = MathHelper.frac((double) worldTime / 24000.0D - 0.25D);
        double d1 = 0.5D - Math.cos(d0 * Math.PI) / 2.0D;
        return (float) (d0 * 2.0D + d1) / 3.0F;
    }

    @Override
    public boolean isSurfaceWorld() {
        return true;
    }

    @Override
    @Nonnull
    public Vec3d getFogColor(float celestialAngle, float partialTicks) {
        float f = MathHelper.cos((float) (celestialAngle * Math.PI * 2.0F)) * 2.0F + 0.5F;
        if (f < 0.2F) {
            f = 0.2F;
        }

        if (f > 1.0F) {
            f = 1.0F;
        }

        // Darken fog as sandstorm builds
        // f *= (1 - this.stormStrength) * 0.8 + 0.2;

        float f1 = 0.9F * f;
        float f2 = 0.75F * f;
        float f3 = 0.6F * f;
        return new Vec3d(f1, f2, f3);
    }

    @Override
    public boolean canRespawnHere() {
        return true;
    }

    @Override
    public boolean doesXZShowFog(int x, int z) {
        return true; //TODO Test
    }
}