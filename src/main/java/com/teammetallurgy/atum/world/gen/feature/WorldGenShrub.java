package com.teammetallurgy.atum.world.gen.feature;

import com.teammetallurgy.atum.blocks.vegetation.BlockShrub;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import javax.annotation.Nonnull;
import java.util.Random;

public class WorldGenShrub extends WorldGenerator {
    private Block shrub;
    private int groupSize;

    public WorldGenShrub(Block block, int size) {
        this.shrub = block;
        this.groupSize = size;
    }

    @Override
    public boolean generate(@Nonnull World world, @Nonnull Random random, @Nonnull BlockPos pos) {
        int size = random.nextInt(this.groupSize / 2) + this.groupSize / 2;
        do {
            BlockState state = world.getBlockState(pos);
            if (!state.getBlock().isAir(state, world, pos) && !state.getBlock().isLeaves(state, world, pos)) break;
            pos = pos.down();
        } while (pos.getY() > 0);

        for (int i = 0; i < size; ++i) {
            BlockPos blockpos = pos.add(random.nextInt(8) - random.nextInt(8), random.nextInt(4) - random.nextInt(4), random.nextInt(8) - random.nextInt(8));

            BlockShrub blockShrub = (BlockShrub) shrub;

            if (world.isAirBlock(blockpos) && blockShrub.canBlockStay(world, blockpos, blockShrub.getDefaultState())) {
                world.setBlockState(blockpos, blockShrub.getDefaultState(), 2);
            }
        }
        return true;
    }
}