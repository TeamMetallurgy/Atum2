package com.teammetallurgy.atum.blocks;

import com.teammetallurgy.atum.blocks.wood.BlockAtumTorchUnlit;
import com.teammetallurgy.atum.init.AtumBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Random;

public class WorldGenTestTorches extends WorldGenerator {
    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position) {
        for (IBlockState iblockstate = worldIn.getBlockState(position); (iblockstate.getBlock().isAir(iblockstate, worldIn, position) || iblockstate.getBlock().isLeaves(iblockstate, worldIn, position)) && position.getY() > 1; iblockstate = worldIn.getBlockState(position)) {
            position = position.down();
        }

        if (position.getY() < 1) {
            return false;
        } else {
            position = position.up();

            for (int i = 0; i < 4; ++i) {
                BlockPos blockpos = position.add(rand.nextInt(4) - rand.nextInt(4), rand.nextInt(3) - rand.nextInt(3), rand.nextInt(4) - rand.nextInt(4));

                if (worldIn.isAirBlock(blockpos) && worldIn.getBlockState(blockpos.down()).isSideSolid(worldIn, blockpos.down(), net.minecraft.util.EnumFacing.UP)) {
                    BlockPos blockpos1 = blockpos.east();
                    BlockPos blockpos2 = blockpos.west();
                    BlockPos blockpos3 = blockpos.north();
                    BlockPos blockpos4 = blockpos.south();

                    if (worldIn.isAirBlock(blockpos2) && worldIn.getBlockState(blockpos2.down()).isSideSolid(worldIn, blockpos2.down(), net.minecraft.util.EnumFacing.UP)) {
                        worldIn.setBlockState(blockpos2, BlockAtumTorchUnlit.getUnlitTorch(AtumBlocks.DEADWOOD_TORCH).getDefaultState(), 2);
                    }

                    if (worldIn.isAirBlock(blockpos1) && worldIn.getBlockState(blockpos1.down()).isSideSolid(worldIn, blockpos1.down(), net.minecraft.util.EnumFacing.UP)) {
                        worldIn.setBlockState(blockpos1, BlockAtumTorchUnlit.getUnlitTorch(AtumBlocks.DEADWOOD_TORCH).getDefaultState(), 2);
                    }

                    if (worldIn.isAirBlock(blockpos3) && worldIn.getBlockState(blockpos3.down()).isSideSolid(worldIn, blockpos3.down(), net.minecraft.util.EnumFacing.UP)) {
                        worldIn.setBlockState(blockpos3, BlockAtumTorchUnlit.getUnlitTorch(AtumBlocks.DEADWOOD_TORCH).getDefaultState(), 2);
                    }

                    if (worldIn.isAirBlock(blockpos4) && worldIn.getBlockState(blockpos4.down()).isSideSolid(worldIn, blockpos4.down(), net.minecraft.util.EnumFacing.UP)) {
                        worldIn.setBlockState(blockpos4, BlockAtumTorchUnlit.getUnlitTorch(AtumBlocks.DEADWOOD_TORCH).getDefaultState(), 2);
                    }
                    return true;
                }
            }
            return false;
        }
    }
}