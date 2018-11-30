package com.teammetallurgy.atum.world.gen.feature;

import com.teammetallurgy.atum.blocks.wood.BlockDeadwood;
import com.teammetallurgy.atum.init.AtumBlocks;
import net.minecraft.block.BlockLog;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

import javax.annotation.Nonnull;
import java.util.Random;

public class WorldGenDeadwood extends WorldGenAbstractTree {
    private static final IBlockState LOG = AtumBlocks.DEADWOOD_LOG.getDefaultState().withProperty(BlockDeadwood.LOG_AXIS, BlockLog.EnumAxis.NONE).withProperty(BlockDeadwood.HAS_SCARAB, true);

    public WorldGenDeadwood(boolean notify) {
        super(notify);
    }

    @Override
    public boolean generate(@Nonnull World world, @Nonnull Random rand, @Nonnull BlockPos pos) {
        int baseHeight = rand.nextInt(3) + 5;

        boolean doNotGenerate = true;

        if (pos.getY() >= 1 && pos.getY() + baseHeight + 1 <= 256) {
            for (int y = pos.getY(); y <= pos.getY() + 1 + baseHeight; ++y) {
                int k = 1;

                if (y == pos.getY()) {
                    k = 0;
                }

                if (y >= pos.getY() + 1 + baseHeight - 2) {
                    k = 2;
                }
                BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();

                for (int x = pos.getX() - k; x <= pos.getX() + k && doNotGenerate; ++x) {
                    for (int z = pos.getZ() - k; z <= pos.getZ() + k && doNotGenerate; ++z) {
                        if (y >= 0 && y < world.getHeight()) {
                            if (!this.isReplaceable(world, mutableBlockPos.setPos(x, y, z))) {
                                doNotGenerate = false;
                            }
                        } else {
                            doNotGenerate = false;
                        }
                    }
                }
            }

            if (!doNotGenerate) {
                return false;
            } else {
                BlockPos down = pos.down();
                IBlockState state = world.getBlockState(down);
                boolean isSoil = state.getBlock() == AtumBlocks.SAND;

                if (isSoil && pos.getY() < world.getHeight() - baseHeight - 1) {
                    for (int height = 0; height < baseHeight; ++height) {
                        BlockPos upN = pos.up(height);
                        IBlockState state2 = world.getBlockState(upN);

                        if (state2.getBlock().isAir(state2, world, upN) || state2.getBlock().isLeaves(state2, world, upN)) {
                            this.setBlockAndNotifyAdequately(world, pos.up(height), LOG);
                        }
                    }
                    return true;
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }
    }
}