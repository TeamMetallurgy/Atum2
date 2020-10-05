package com.teammetallurgy.atum.world.gen.feature.config;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.trees.Tree;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

public abstract class PalmTreeBase extends Tree {

    @Override
    @Deprecated
    protected ConfiguredFeature<BaseTreeFeatureConfig, ?> getTreeFeature(@Nonnull Random random, boolean largeHive) { //DO NOT OVERRIDE
        return null;
    }

    @Nullable
    protected abstract ConfiguredFeature<PalmConfig, ?> getPalmFeature(Random random, boolean b);

    @Override
    public boolean attemptGrowTree(@Nonnull ServerWorld world, @Nonnull ChunkGenerator chunkGenerator, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull Random rand) {
        ConfiguredFeature<PalmConfig, ?> configFeature = this.getPalmFeature(rand, this.hasNearbyFlora(world, pos));
        if (configFeature == null) {
            return false;
        } else {
            world.setBlockState(pos, Blocks.AIR.getDefaultState(), 4);
            configFeature.config.forcePlacement();
            if (configFeature.func_242765_a(world, chunkGenerator, rand, pos)) {
                return true;
            } else {
                world.setBlockState(pos, state, 4);
                return false;
            }
        }
    }

    private boolean hasNearbyFlora(IWorld world, BlockPos pos) {
        for (BlockPos blockpos : BlockPos.Mutable.getAllInBoxMutable(pos.down().north(2).west(2), pos.up().south(2).east(2))) {
            if (world.getBlockState(blockpos).isIn(BlockTags.FLOWERS)) {
                return true;
            }
        }
        return false;
    }
}