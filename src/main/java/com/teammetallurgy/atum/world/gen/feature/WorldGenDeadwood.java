package com.teammetallurgy.atum.world.gen.feature;

import com.teammetallurgy.atum.blocks.wood.BlockAtumLog;
import com.teammetallurgy.atum.blocks.wood.BlockAtumPlank;
import com.teammetallurgy.atum.init.AtumBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

import javax.annotation.Nonnull;
import java.util.Random;

public class WorldGenDeadwood extends WorldGenAbstractTree { //TODO Change how the branches generate
    private static final IBlockState LOG = BlockAtumLog.getLog(BlockAtumPlank.WoodType.DEADWOOD).getDefaultState();
    private int minTreeHeight = 6;

    public WorldGenDeadwood(boolean doBlockNotify) {
        super(doBlockNotify);
    }

    public WorldGenDeadwood(boolean doBlockNotify, int minimumHeight) {
        super(doBlockNotify);
        minTreeHeight = minimumHeight;
    }

    @Override
    public boolean generate(@Nonnull World world, @Nonnull Random rand, @Nonnull BlockPos pos) {
        int height = rand.nextInt(3) + rand.nextInt(3) + minTreeHeight;
        boolean flag = true;

        if (pos.getY() >= 1 && pos.getY() + height + 1 <= 256) {
            for (int j = pos.getY(); j <= pos.getY() + 1 + height; ++j) {
                int k = 1;

                if (j == pos.getY()) {
                    k = 0;
                }

                if (j >= pos.getY() + 1 + height - 2) {
                    k = 2;
                }

                BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

                for (int l = pos.getX() - k; l <= pos.getX() + k && flag; ++l) {
                    for (int i1 = pos.getZ() - k; i1 <= pos.getZ() + k && flag; ++i1) {
                        if (j >= 0 && j < 256) {
                            if (!this.isReplaceable(world, mutablePos.setPos(l, j, i1))) {
                                flag = false;
                            }
                        } else {
                            flag = false;
                        }
                    }
                }
            }

            if (!flag) {
                return false;
            } else {
                BlockPos down = pos.down();
                IBlockState state = world.getBlockState(down);
                boolean isSoil = state.getBlock() == AtumBlocks.SAND;

                if (isSoil && pos.getY() < world.getHeight() - height - 1) {
                    state.getBlock().onPlantGrow(state, world, down, pos);
                    EnumFacing facing = EnumFacing.Plane.HORIZONTAL.random(rand);
                    int k2 = height - rand.nextInt(4) - 1;
                    int l2 = 3 - rand.nextInt(3);
                    int i3 = pos.getX();
                    int j1 = pos.getZ();
                    
                    for (int l1 = 0; l1 < height; ++l1) {
                        int i2 = pos.getY() + l1;

                        if (l1 >= k2 && l2 > 0) {
                            i3 += facing.getFrontOffsetX();
                            j1 += facing.getFrontOffsetZ();
                            --l2;
                        }

                        BlockPos blockpos = new BlockPos(i3, i2, j1);
                        state = world.getBlockState(blockpos);

                        if (state.getBlock().isAir(state, world, blockpos) || state.getBlock().isLeaves(state, world, blockpos)) {
                            this.placeLogAt(world, blockpos);
                        }
                    }
                    i3 = pos.getX();
                    j1 = pos.getZ();
                    EnumFacing facing1 = EnumFacing.Plane.HORIZONTAL.random(rand);

                    if (facing1 != facing) {
                        int l3 = k2 - rand.nextInt(2) - 1;
                        int k4 = 1 + rand.nextInt(3);

                        for (int l4 = l3; l4 < height && k4 > 0; --k4) {
                            if (l4 >= 1) {
                                int j2 = pos.getY() + l4;
                                i3 += facing1.getFrontOffsetX();
                                j1 += facing1.getFrontOffsetZ();
                                BlockPos blockpos1 = new BlockPos(i3, j2, j1);
                                state = world.getBlockState(blockpos1);

                                if (state.getBlock().isAir(state, world, blockpos1) || state.getBlock().isLeaves(state, world, blockpos1)) {
                                    this.placeLogAt(world, blockpos1);
                                }
                            }
                            ++l4;
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

    private void placeLogAt(World world, BlockPos pos) {
        this.setBlockAndNotifyAdequately(world, pos, LOG);
    }
}