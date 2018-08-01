package com.teammetallurgy.atum.world.gen.feature;

import com.teammetallurgy.atum.init.AtumBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import javax.annotation.Nonnull;
import java.util.Random;

public class WorldGenOasisGrass extends WorldGenerator {
    private final IBlockState grassState;

    public WorldGenOasisGrass(IBlockState state) {
        this.grassState = state;
    }

    @Override
    public boolean generate(@Nonnull World world, @Nonnull Random rand, @Nonnull BlockPos pos) {
        for (IBlockState state = world.getBlockState(pos); (state.getBlock().isAir(state, world, pos) || state.getBlock().isLeaves(state, world, pos)) && pos.getY() > 0; state = world.getBlockState(pos)) {
            pos = pos.down();
        }
        for (int i = 0; i < 128; ++i) {
            BlockPos checkPos = pos.add(rand.nextInt(8) - rand.nextInt(8), rand.nextInt(4) - rand.nextInt(4), rand.nextInt(8) - rand.nextInt(8));

            if (world.isAirBlock(checkPos) && AtumBlocks.OASIS_GRASS.canBlockStay(world, checkPos, this.grassState)) {
                world.setBlockState(checkPos, this.grassState, 2);
            }
        }
        return true;
    }
}