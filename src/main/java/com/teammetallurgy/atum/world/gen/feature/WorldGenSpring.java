package com.teammetallurgy.atum.world.gen.feature;

import com.teammetallurgy.atum.init.AtumBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import javax.annotation.Nonnull;
import java.util.Random;

public class WorldGenSpring extends WorldGenerator {
    private final Block block;

    public WorldGenSpring(Block blockIn) {
        this.block = blockIn;
    }

    @Override
    public boolean generate(@Nonnull World world, @Nonnull Random rand, @Nonnull BlockPos pos) {
        if (world.getBlockState(pos.up()).getBlock() != AtumBlocks.LIMESTONE) {
            return false;
        } else if (world.getBlockState(pos.down()).getBlock() != AtumBlocks.LIMESTONE) {
            return false;
        } else if (pos.getY() >= 50) {
            return false;
        } else {
            BlockState state = world.getBlockState(pos);

            if (!state.getBlock().isAir(state, world, pos) && state.getBlock() != AtumBlocks.LIMESTONE) {
                return false;
            } else {
                int limestoneCheck = 0;
                if (world.getBlockState(pos.west()).getBlock() == AtumBlocks.LIMESTONE) {
                    ++limestoneCheck;
                }
                if (world.getBlockState(pos.east()).getBlock() == AtumBlocks.LIMESTONE) {
                    ++limestoneCheck;
                }
                if (world.getBlockState(pos.north()).getBlock() == AtumBlocks.LIMESTONE) {
                    ++limestoneCheck;
                }
                if (world.getBlockState(pos.south()).getBlock() == AtumBlocks.LIMESTONE) {
                    ++limestoneCheck;
                }
                int airCheck = 0;

                if (world.isAirBlock(pos.west())) {
                    ++airCheck;
                }
                if (world.isAirBlock(pos.east())) {
                    ++airCheck;
                }
                if (world.isAirBlock(pos.north())) {
                    ++airCheck;
                }
                if (world.isAirBlock(pos.south())) {
                    ++airCheck;
                }

                if (limestoneCheck == 3 && airCheck == 1) {
                    BlockState blockState = this.block.getDefaultState();
                    world.setBlockState(pos, blockState, 2);
                    world.immediateBlockTick(pos, blockState, rand);
                }
                return true;
            }
        }
    }
}