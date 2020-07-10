package com.teammetallurgy.atum.world.gen.feature.config;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.trees.Tree;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.Random;

public abstract class PalmTreeBase extends Tree {

    @Override
    @Nullable
    protected ConfiguredFeature<TreeFeatureConfig, ?> getTreeFeature(@Nonnull Random random, boolean b) { //DO NOT OVERRIDE
        return null;
    }

    @Nullable
    protected abstract ConfiguredFeature<PalmConfig, ?> getPalmFeature(Random random, boolean b);

    @Override
    public boolean place(@Nonnull IWorld world, @Nonnull ChunkGenerator<?> chunkGenerator, BlockPos pos, @Nonnull BlockState state, @Nonnull Random rand) {
        ConfiguredFeature<PalmConfig, ?> configFeature = this.getPalmFeature(rand, this.findPos(world, pos));
        if (configFeature == null) {
            return false;
        } else {
            world.setBlockState(pos, Blocks.AIR.getDefaultState(), 4);
            configFeature.config.forcePlacement();
            if (configFeature.place(world, chunkGenerator, rand, pos)) {
                return true;
            } else {
                world.setBlockState(pos, state, 4);
                return false;
            }
        }
    }

    private boolean findPos(IWorld world, BlockPos pos) {
        Iterator<BlockPos> positions = BlockPos.Mutable.getAllInBoxMutable(pos.down().north(2).west(2), pos.up().south(2).east(2)).iterator();

        BlockPos checkPos;
        do {
            if (!positions.hasNext()) {
                return false;
            }
            checkPos = positions.next();
        } while (!world.getBlockState(checkPos).isIn(BlockTags.FLOWERS));
        return true;
    }
}