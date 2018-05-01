package com.teammetallurgy.atum.world.decorators;

import com.teammetallurgy.atum.blocks.BlockAtumLog;
import com.teammetallurgy.atum.blocks.BlockAtumPlank;
import com.teammetallurgy.atum.blocks.BlockAtumSapling;
import com.teammetallurgy.atum.init.AtumBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

import java.util.Random;

public class WorldGenDeadwood extends WorldGenAbstractTree {
    private final IBlockState blockLog = BlockAtumLog.getLog(BlockAtumPlank.WoodType.DEADWOOD).getDefaultState();
    private int minTreeHeight = 6;

    public WorldGenDeadwood(boolean doBlockNotify) {
        super(doBlockNotify);
    }

    public WorldGenDeadwood(boolean doBlockNotify, int minimunHeight) {
        super(doBlockNotify);
        minTreeHeight = minimunHeight;
    }

    @Override
    public boolean generate(World world, Random random, BlockPos pos) { //TODO figure out how to do the Direction offset (EnumFacing) properly
        int i = random.nextInt(3) + this.minTreeHeight;
        boolean flag = true;
        if (pos.getY() >= 1 && pos.getY() + i + 1 <= 256) {
            for (int spaceY = pos.getY(); spaceY <= pos.getY() + 1 + i; spaceY++) {

                int extraGirth = 1;
                if (spaceY == pos.getY()) {
                    extraGirth = 0;
                }
                if (spaceY >= pos.getY() + 1 + i - 2) {
                    extraGirth = 2;
                }

                BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();

                for (int spaceX = pos.getX() - extraGirth; spaceX <= pos.getX() + extraGirth && flag; spaceX++) {
                    for (int spaceZ = pos.getZ() - extraGirth; spaceZ <= pos.getZ() + extraGirth && flag; spaceZ++) {

                        if (spaceY < 0 || spaceY >= 256) {
                            flag = false;
                            continue;
                        }

                        if (!this.isReplaceable(world, mutableBlockPos.add(spaceX, spaceY, spaceZ))) {
                            flag = false;
                        }
                    }
                }
            }

            if (!flag) {
                return false;
            } else {
                IBlockState state = world.getBlockState(pos.down());

                if (state.getBlock().canSustainPlant(state, world, pos.down(), EnumFacing.UP, (BlockAtumSapling) BlockAtumSapling.getSapling(BlockAtumPlank.WoodType.DEADWOOD)) && pos.getY() < 256 - 1 - i) {
                    setStrangeSandAt(world, pos.down());

                    int splitStartfromTop = 4;
                    int logX = pos.getX();
                    int logZ = pos.getZ();
                    int branchStart = i - random.nextInt(splitStartfromTop + 1) - 1;
                    int branchHeight = splitStartfromTop - random.nextInt(splitStartfromTop);
                    int branchDirection = random.nextInt(3);

                    // First branch
                    for (int logY = pos.getY(); logY < i + pos.getY(); logY++) {

                        if (logY >= branchStart + pos.getY() && branchHeight > 0) {
                        /*logX += Direction.offsetX[branchDirection];
                        logZ += Direction.offsetZ[branchDirection];*/ //TODO
                            branchHeight--;
                        }

                        BlockPos currentPos = new BlockPos(logX, logY, logZ);
                        Block currentBlock = world.getBlockState(currentPos).getBlock();
                        if (currentBlock.isAir(state, world, currentPos) || currentBlock.isLeaves(state, world, currentPos)) {
                            setBlockAndNotifyAdequately(world, currentPos, this.blockLog);
                        }

                        // Trunk base
                        if (logY == pos.getY()) {
                            int numberOfBaseSides = random.nextInt(3) + 1;
                            for (int j = 0; j < numberOfBaseSides; j++) {
                                //int baseDirection = random.nextInt(3);
                            /*int baseX = Direction.offsetX[baseDirection] + logX;
                            int baseZ = Direction.offsetZ[baseDirection] + logZ;*/ //TODO

                                //BlockPos currentBasePos = new BlockPos(baseX, logY, baseZ); //TODO
                                BlockPos currentBasePos = new BlockPos(logX, logY, logZ); //Temp
                                Block currentBaseBlock = world.getBlockState(currentPos).getBlock();
                                Block lowerBaseBlock = world.getBlockState(currentPos.down()).getBlock();
                                if ((currentBaseBlock.isAir(state, world, currentBasePos) || currentBaseBlock.isLeaves(state, world, currentBasePos)) && lowerBaseBlock == AtumBlocks.SAND.getDefaultState()) {
                                    setBlockAndNotifyAdequately(world, currentBasePos, this.blockLog);
                                }
                            }
                        }
                    }

                    // Second branch
                    logX = pos.getX();
                    logZ = pos.getZ();
                    int branch2Direction = random.nextInt(3);
                    if (branchDirection != branch2Direction) {
                        int branch2Start = branchStart - random.nextInt(splitStartfromTop - 1) - 1;
                        int branch2Height = 1 + random.nextInt(splitStartfromTop);
                        boolean firstRun = true;
                        for (int logY = branch2Start + pos.getY(); logY < i + pos.getY() && branch2Height > 0; logY++) {

                            if (!firstRun) {
                            /*logX += Direction.offsetX[branch2Direction];
                            logZ += Direction.offsetZ[branch2Direction];*/ //TODO

                                BlockPos currentPos = new BlockPos(logX, logY, logZ);
                                Block currentBlock = world.getBlockState(currentPos).getBlock();
                                if (currentBlock.isAir(state, world, currentPos) || currentBlock.isLeaves(state, world, currentPos)) {
                                    setBlockAndNotifyAdequately(world, currentPos, this.blockLog);
                                }
                            }
                            firstRun = false;
                            branch2Height--;
                        }

                    }
                }
                return true;
            }
        }
        return false;
    }

    protected void setStrangeSandAt(World worldIn, BlockPos pos) {
        if (worldIn.getBlockState(pos).getBlock() != AtumBlocks.SAND) {
            this.setBlockAndNotifyAdequately(worldIn, pos, AtumBlocks.SAND.getDefaultState());
        }
    }
}