package com.teammetallurgy.atum.world.gen.feature;

import com.teammetallurgy.atum.blocks.wood.BlockBranch;
import com.teammetallurgy.atum.blocks.wood.BlockDeadwood;
import com.teammetallurgy.atum.init.AtumBlocks;
import net.minecraft.block.BlockLog;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.util.*;

public class WorldGenDeadwood extends WorldGenAbstractTree {
    private static final IBlockState LOG = AtumBlocks.DEADWOOD_LOG.getDefaultState()
            .withProperty(BlockDeadwood.LOG_AXIS, BlockLog.EnumAxis.NONE);
    private static final IBlockState BRANCH = AtumBlocks.DEADWOOD_BRANCH.getDefaultState();

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
                    List<BlockPos> logs = new ArrayList<BlockPos>();
                    for (int height = 0; height < baseHeight; ++height) {
                        BlockPos upN = pos.up(height);
                        IBlockState state2 = world.getBlockState(upN);

                        if (state2.getBlock().isAir(state2, world, upN)
                                || state2.getBlock().isLeaves(state2, world, upN)) {
                            this.setBlockAndNotifyAdequately(world, pos.up(height), LOG);
                            if (height > 1)
                                logs.add(pos.up(height));
                        }
                    }
                    buildBranches(world, logs, rand);
                    return true;
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }
    }

    public void buildBranches(World world, List<BlockPos> logs, Random random) {
        // Keep track of branches to be generated using a Queue
        Queue<Pair<BlockPos, Integer>> queue = new LinkedList<>();

        for (BlockPos pos : logs) {
            queue.add(new ImmutablePair<>(pos, 0));
        }
        BlockPos baseLog = queue.peek().getLeft();
        int count = 0;

        Set<EnumFacing> choosenFacings = new HashSet<>();
        Map<EnumFacing, Integer> balancingCount = new HashMap<>();
        for (EnumFacing facing : EnumFacing.VALUES) {
            balancingCount.put(facing, 0);
        }

        while (!queue.isEmpty()) {
            Pair<BlockPos, Integer> pair = queue.poll();
            if (pair.getLeft() == null || pair.getRight() == null) {
                continue;
            }
            BlockPos pos = pair.getLeft();
            int branchLength = pair.getRight();

            choosenFacings.clear();
            for (EnumFacing facing : EnumFacing.VALUES) {

                // Prevent a split from splitting in both directions
                if (choosenFacings.contains(facing.getOpposite())) {
                    continue;
                }

                // Prevent branches 3 blocks in the same direction
                IBlockState curr = world.getBlockState(pos);
                if (curr.getBlock() == BRANCH.getBlock() && curr.getValue(BlockBranch.FACING) == facing.getOpposite()) {
                    IBlockState prev = world.getBlockState(pos.add(facing.getOpposite().getDirectionVec()));
                    if (prev.getBlock() == BRANCH.getBlock() && prev.getValue(BlockBranch.FACING) == facing.getOpposite()) {
                        continue;
                    }
                }

                double dist = baseLog.getDistance(pos.getX(), pos.getY(), pos.getZ());
                float probability = 0.8f;
                if (facing == EnumFacing.UP) {
                    probability *= 1.5;

                    // Stop branches from growing from the top of logs
                    if (world.getBlockState(pos).getBlock() == LOG.getBlock()) {
                        probability = 0;
                    }
                } else if (facing == EnumFacing.DOWN) {
                    probability = 0.00f;
                }

                probability -= 0.05 * dist;
                probability -= 0.002 * count;
                probability -= 0.1 * branchLength;
                probability += 0.25 * balancingCount.get(facing);

                if (random.nextFloat() < probability) {
                    BlockPos nextPos = pos.add(facing.getDirectionVec());
                    if (world.isAirBlock(nextPos)) {
                        world.setBlockState(nextPos, BRANCH.withProperty(BlockBranch.FACING, facing.getOpposite()));

                        // Add this branch onto the queue to spawn new branches from
                        queue.add(new ImmutablePair<BlockPos, Integer>(nextPos, branchLength + 1));

                        // Keep track of how many branches have been generated
                        count++;

                        // Store the chosen facing for this branch to prevent odd splitting
                        choosenFacings.add(facing);

                        // Store the count for the direction chosen to help with balancing the tree
                        if (facing != EnumFacing.UP && facing != EnumFacing.DOWN) {
                            if (balancingCount.get(facing) > 0) {
                                balancingCount.put(facing, balancingCount.get(facing) - 1);
                            } else {
                                balancingCount.put(facing.getOpposite(),
                                        balancingCount.get(facing.getOpposite()) + 1);
                            }
                        }
                    }
                }
            }

            // Failsafe, if the stack has gotten this large, something has probably gone wrong.
            if (queue.size() > 100) {
                break;
            }
        }
    }
}