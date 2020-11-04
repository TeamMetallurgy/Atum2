package com.teammetallurgy.atum.world.gen;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;

import javax.annotation.Nonnull;
import java.util.Random;

public class OasisSurfaceBuilder extends SurfaceBuilder<SurfaceBuilderConfig> {

    public OasisSurfaceBuilder(Codec<SurfaceBuilderConfig> config) {
        super(config);
    }

    @Override
    public void buildSurface(@Nonnull Random random, @Nonnull IChunk chunk, @Nonnull Biome biome, int x, int z, int startHeight, double noise, @Nonnull BlockState defaultBlock, @Nonnull BlockState defaultFluid, int seaLevel, long seed, @Nonnull SurfaceBuilderConfig config) {
        BlockPos.Mutable mutablePos = new BlockPos.Mutable();
        int i = x & 15;
        int j = z & 15;
        int y = chunk.getTopBlockY(Heightmap.Type.WORLD_SURFACE_WG, x, z);

        while (y < seaLevel) {
            mutablePos.setPos(i, y, j);
            y++;
            if (chunk.getBlockState(mutablePos).isAir()) {
                chunk.setBlockState(mutablePos, Blocks.WATER.getDefaultState(), true);
                chunk.tic
            }
        }

        SurfaceBuilder.DEFAULT.buildSurface(random, chunk, biome, x, z, startHeight, noise, defaultBlock, defaultFluid, seaLevel, seed, AtumSurfaceBuilders.OASIS_CONFIG);
    }
}