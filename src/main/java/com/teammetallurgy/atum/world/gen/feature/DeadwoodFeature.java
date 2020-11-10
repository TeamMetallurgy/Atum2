package com.teammetallurgy.atum.world.gen.feature;

import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import com.teammetallurgy.atum.blocks.wood.DeadwoodBranchBlock;
import com.teammetallurgy.atum.blocks.wood.DeadwoodLogBlock;
import com.teammetallurgy.atum.init.AtumBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.WorldGenRegion;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.TreeFeature;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.util.*;

public class DeadwoodFeature extends Feature<NoFeatureConfig> {
    private static final BlockState LOG = AtumBlocks.DEADWOOD_LOG.getDefaultState().with(DeadwoodLogBlock.HAS_SCARAB, true);
    private static final BlockState BRANCH = AtumBlocks.DEADWOOD_BRANCH.getDefaultState();

    public DeadwoodFeature(Codec<NoFeatureConfig> config) {
        super(config);
    }

    @Override
    public boolean generate(@Nonnull ISeedReader genReader, @Nonnull ChunkGenerator generator, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull NoFeatureConfig config) {
        if (genReader instanceof WorldGenRegion) {
            WorldGenRegion world = (WorldGenRegion) genReader;
            Set<BlockPos> logs = Sets.newHashSet();
            int baseHeight = rand.nextInt(3) + 5;
            boolean generate = true;

            if (pos.getY() >= 0 && pos.getY() + baseHeight + 1 <= world.getHeight()) {
                for (int y = pos.getY(); y <= pos.getY() + 1 + baseHeight; ++y) {
                    int k = 1;

                    if (y == pos.getY()) {
                        k = 0;
                    }

                    if (y >= pos.getY() + 1 + baseHeight - 2) {
                        k = 2;
                    }
                    BlockPos.Mutable mutable = new BlockPos.Mutable();

                    for (int x = pos.getX() - k; x <= pos.getX() + k && generate; ++x) {
                        for (int z = pos.getZ() - k; z <= pos.getZ() + k && generate; ++z) {
                            if (y >= 0 && y < world.getHeight()) {
                                if (!TreeFeature.func_236410_c_(world, mutable.setPos(x, y, z))) {
                                    generate = false;
                                }
                            } else {
                                generate = false;
                            }
                        }
                    }
                }

                if (!generate) {
                    return false;
                } else {
                    BlockPos down = pos.down();
                    BlockState state = world.getBlockState(down);
                    boolean isSoil = state.getBlock() == AtumBlocks.SAND;

                    if (genReader.isAreaLoaded(pos, 16)) {
                        if (isSoil && pos.getY() < world.getHeight() - baseHeight - 1) {
                            for (int height = 0; height < baseHeight; ++height) {
                                BlockPos upN = pos.up(height);

                                if (TreeFeature.isAirOrLeavesAt(genReader, upN)) {
                                    genReader.setBlockState(pos.up(height), LOG, 19);
                                    if (height > 1) {
                                        logs.add(pos.up(height));
                                    }
                                }
                            }
                            buildBranches(world, genReader, logs, rand);
                            return true;
                        } else {
                            return false;
                        }
                    } else {
                        return false;
                    }
                }
            }
        }
        return false;
    }

    public void buildBranches(IWorldReader world, IWorldGenerationReader genReader, Set<BlockPos> logs, Random random) {
        // Keep track of branches to be generated using a Queue
        Queue<Pair<BlockPos, Integer>> queue = new LinkedList<>();
        List<BlockPos> placedBranches = new ArrayList<>();

        for (BlockPos pos : logs) {
            queue.add(new ImmutablePair<>(pos, 0));
        }
        BlockPos baseLog = queue.peek().getLeft();
        int count = 0;

        Set<Direction> choosenFacings = new HashSet<>();
        Map<Direction, Integer> balancingCount = new HashMap<>();
        for (Direction facing : Direction.values()) {
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
            for (Direction facing : Direction.values()) {

                // Prevent a split from splitting in both directions
                if (choosenFacings.contains(facing.getOpposite())) {
                    continue;
                }

                // Prevent branches 3 blocks in the same direction
                BlockState curr = world.getBlockState(pos);
                if (curr.getBlock() == BRANCH.getBlock() && curr.get(DeadwoodBranchBlock.FACING) == facing.getOpposite()) {
                    BlockState prev = world.getBlockState(pos.add(facing.getOpposite().getDirectionVec()));
                    if (prev.getBlock() == BRANCH.getBlock() && prev.get(DeadwoodBranchBlock.FACING) == facing.getOpposite()) {
                        continue;
                    }
                }

                double dist = baseLog.distanceSq(new Vector3i(pos.getX(), pos.getY(), pos.getZ()));
                float probability = 0.8f;
                if (facing == Direction.UP) {
                    probability *= 1.5;

                    // Stop branches from growing from the top of logs
                    if (world.getBlockState(pos).getBlock() == LOG.getBlock()) {
                        probability = 0;
                    }
                } else if (facing == Direction.DOWN) {
                    probability = 0.00f;
                }

                probability -= 0.05 * dist;
                probability -= 0.002 * count;
                probability -= 0.1 * branchLength;
                probability += 0.25 * balancingCount.get(facing);

                if (random.nextFloat() < probability) {
                    BlockPos nextPos = pos.add(facing.getDirectionVec());
                    if (world.isAirBlock(nextPos) && world.isAreaLoaded(nextPos, 8)) {
                        DeadwoodBranchBlock branch = (DeadwoodBranchBlock) BRANCH.getBlock();
                        this.setBlockState(genReader, nextPos, branch.makeConnections(world, nextPos, facing));
                        placedBranches.add(nextPos);

                        // Add this branch onto the queue to spawn new branches from
                        queue.add(new ImmutablePair<>(nextPos, branchLength + 1));

                        // Keep track of how many branches have been generated
                        count++;

                        // Store the chosen facing for this branch to prevent odd splitting
                        choosenFacings.add(facing);

                        // Store the count for the direction chosen to help with balancing the tree
                        if (facing != Direction.UP && facing != Direction.DOWN) {
                            if (balancingCount.get(facing) > 0) {
                                balancingCount.put(facing, balancingCount.get(facing) - 1);
                            } else {
                                balancingCount.put(facing.getOpposite(), balancingCount.get(facing.getOpposite()) + 1);
                            }
                        }
                    }
                }
            }

            for (BlockPos placedLocation : placedBranches) {
                if (!genReader.hasBlockState(placedLocation, BlockState::isAir)) {
                    DeadwoodBranchBlock branch = (DeadwoodBranchBlock) BRANCH.getBlock();
                    this.setBlockState(genReader, placedLocation, branch.makeConnections(world, placedLocation));
                }
            }

            // Failsafe, if the stack has gotten this large, something has probably gone wrong.
            if (queue.size() > 100) {
                break;
            }
        }
    }
}