package com.teammetallurgy.atum.world.decorators;

import com.teammetallurgy.atum.blocks.BlockAtumLog;
import com.teammetallurgy.atum.blocks.BlockAtumPlank;
import com.teammetallurgy.atum.blocks.BlockLeave;
import com.teammetallurgy.atum.init.AtumBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

import javax.annotation.Nonnull;
import java.util.Random;

public class WorldGenPalm extends WorldGenAbstractTree {
    private static final IBlockState BLOCK_LOG = BlockAtumLog.getLog(BlockAtumPlank.WoodType.PALM).getDefaultState();
    private static final IBlockState BLOCK_LEAVES = BlockLeave.getLeave(BlockAtumPlank.WoodType.PALM).getDefaultState().withProperty(BlockLeave.CHECK_DECAY, false);
    private final int minTreeHeight;
    private final IBlockState stateWood;
    private final IBlockState stateLeaves;

    public WorldGenPalm(boolean notify) {
        this(notify, 5, BLOCK_LOG, BLOCK_LEAVES);
    }

    public WorldGenPalm(boolean notify, int minTreeHeight) {
        this(notify, minTreeHeight, BLOCK_LOG, BLOCK_LEAVES);
    }

    public WorldGenPalm(boolean notify, int minTreeHeight, IBlockState wood, IBlockState leaves) {
        super(notify);
        this.minTreeHeight = minTreeHeight;
        this.stateWood = wood;
        this.stateLeaves = leaves;
    }

    @Override
    public boolean generate(@Nonnull World world, @Nonnull Random random, @Nonnull BlockPos pos) {
        int treeHeight = random.nextInt(3) + this.minTreeHeight;
        boolean flag = true;
        Block blocks = world.getBlockState(pos.down()).getBlock();
        if ((blocks == AtumBlocks.SAND || blocks == AtumBlocks.FERTILE_SOIL || blocks == Blocks.DIRT) && pos.getY() >= 1 && pos.getY() + treeHeight + 1 <= 256) {
            for (int j = pos.getY(); j <= pos.getY() + 1 + treeHeight; ++j) {

                int k = 1;
                if (j == pos.getY()) {
                    k = 0;
                }

                if (j >= pos.getY() + 1 + treeHeight - 2) {
                    k = 2;
                }

                BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();

                for (int l = pos.getX() - k; l <= pos.getX() + k && flag; ++l) {
                    for (int i1 = pos.getZ() - k; i1 <= pos.getZ() + k && flag; ++i1) {
                        if (j >= 0 && j < 256) {
                            if (!this.isReplaceable(world, mutableBlockPos.setPos(l, j, i1))) {
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
                IBlockState stateDown = world.getBlockState(down);

                if (pos.getY() >= 256 - treeHeight - 1) {
                    return false;
                } else {
                    stateDown.getBlock().onPlantGrow(stateDown, world, down, pos);
                    int i3 = pos.getX();
                    int j1 = pos.getZ();
                    int k1 = pos.getY() + treeHeight;

                    BlockPos pos1 = new BlockPos(i3, k1, j1);
                    

                    this.spawnLeaf(world, pos1.add(0, 1, 0));

                    for (int block = -1; block <= 1; ++block) {
                        for (int z = -1; z <= 1; ++z) {
                            if (block != 0 || z != 0) {
                                this.spawnLeaf(world, pos1.add(block,0,z));
                            }
                        }
                    }

                    this.spawnLeaf(world, pos1.add(2, 0, 0));
                    this.spawnLeaf(world, pos1.add(-2, 0, 0));
                    this.spawnLeaf(world, pos1.add(0, 0, 2));
                    this.spawnLeaf(world, pos1.add(0, 0, -2));
                    this.spawnLeaf(world, pos1.add(0, -1, -2));
                    this.spawnLeaf(world, pos1.add(0, -1, 2));
                    this.spawnLeaf(world, pos1.add(2, -1, 0));
                    this.spawnLeaf(world, pos1.add(-2, -1, 0));
                    this.spawnLeaf(world, pos1.add(0, -1, -3));
                    this.spawnLeaf(world, pos1.add(0, -1, 3));
                    this.spawnLeaf(world, pos1.add(3, -1, 0));
                    this.spawnLeaf(world, pos1.add(-3, -1, 0));
                    if (random.nextInt(100) < 15) {
                        world.setBlockState(pos1.add(1, -1, 0), AtumBlocks.DATE_BLOCK.getDefaultState(), 2);
                    }

                    if (random.nextInt(100) < 15) {
                        world.setBlockState(pos1.add(-1, -1, 0), AtumBlocks.DATE_BLOCK.getDefaultState(), 2);
                    }

                    if (random.nextInt(100) < 15) {
                        world.setBlockState(pos1.add(0, -1, 1), AtumBlocks.DATE_BLOCK.getDefaultState(), 2);
                    }

                    if (random.nextInt(100) < 15) {
                        world.setBlockState(pos1.add(0, -1, -1), AtumBlocks.DATE_BLOCK.getDefaultState(), 2);
                    }
                }

                for (int j3 = 0; j3 < treeHeight; ++j3) {
                    BlockPos upN = pos.up(j3);
                    IBlockState stateUpN = world.getBlockState(upN);

                    if (stateUpN.getBlock().isAir(stateUpN, world, upN) || stateUpN.getBlock().isLeaves(stateUpN, world, upN)) {
                        this.setBlockAndNotifyAdequately(world, pos.up(j3), this.stateWood);
                    }
                }
                return true;
            }
        } else {
            return false;
        }
    }

    private void spawnLeaf(World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        if (world.isAirBlock(pos) || state.getBlock().canBeReplacedByLeaves(state, world, pos)) {
            this.setBlockAndNotifyAdequately(world, pos, this.stateLeaves);
        }
    }
}