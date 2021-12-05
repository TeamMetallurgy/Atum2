package com.teammetallurgy.atum.world.gen.feature;

import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import com.teammetallurgy.atum.blocks.wood.DeadwoodBranchBlock;
import com.teammetallurgy.atum.blocks.wood.DeadwoodLogBlock;
import com.teammetallurgy.atum.init.AtumBlocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.LevelSimulatedRW;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.util.*;

public class DeadwoodFeature extends Feature<NoneFeatureConfiguration> {
    private static final BlockState LOG = AtumBlocks.DEADWOOD_LOG.defaultBlockState().setValue(DeadwoodLogBlock.HAS_SCARAB, true);
    private static final BlockState BRANCH = AtumBlocks.DEADWOOD_BRANCH.defaultBlockState();

    public DeadwoodFeature(Codec<NoneFeatureConfiguration> config) {
        super(config);
    }

    @Override
    public boolean place(@Nonnull WorldGenLevel genReader, @Nonnull ChunkGenerator generator, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull NoneFeatureConfiguration config) {
        if (genReader instanceof WorldGenRegion) {
            WorldGenRegion world = (WorldGenRegion) genReader;
            Set<BlockPos> logs = Sets.newHashSet();
            int baseHeight = rand.nextInt(5) + 3;
            boolean generate = true;

            if (pos.getY() >= 0 && pos.getY() + baseHeight + 1 <= world.getMaxBuildHeight()) {
                for (int y = pos.getY(); y <= pos.getY() + 1 + baseHeight; ++y) {
                    int k = 1;

                    if (y == pos.getY()) {
                        k = 0;
                    }

                    if (y >= pos.getY() + 1 + baseHeight - 2) {
                        k = 2;
                    }
                    BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();

                    for (int x = pos.getX() - k; x <= pos.getX() + k && generate; ++x) {
                        for (int z = pos.getZ() - k; z <= pos.getZ() + k && generate; ++z) {
                            if (y >= 0 && y < world.getMaxBuildHeight()) {
                                BlockPos checkPos = mutable.set(x, y, z);
                                if (!TreeFeature.isAirOrLeaves(world, checkPos)) {
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
                    BlockPos down = pos.below();
                    BlockState state = world.getBlockState(down);
                    boolean isSoil = state.getBlock() == AtumBlocks.SAND;

                    if (genReader.isAreaLoaded(pos, 16)) {
                        if (isSoil && pos.getY() < world.getMaxBuildHeight() - baseHeight - 1) {
                            for (int height = 0; height < baseHeight; ++height) {
                                BlockPos upN = pos.above(height);

                                if (TreeFeature.isAirOrLeaves(genReader, upN)) {
                                    genReader.setBlock(pos.above(height), LOG, 19);
                                    if (height > 1) {
                                        logs.add(pos.above(height));
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

    public void buildBranches(LevelReader world, LevelSimulatedRW genReader, Set<BlockPos> logs, Random random) {
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
                if (curr.getBlock() == BRANCH.getBlock() && curr.getValue(DeadwoodBranchBlock.FACING) == facing.getOpposite()) {
                    BlockState prev = world.getBlockState(pos.offset(facing.getOpposite().getNormal()));
                    if (prev.getBlock() == BRANCH.getBlock() && prev.getValue(DeadwoodBranchBlock.FACING) == facing.getOpposite()) {
                        continue;
                    }
                }

                double dist = baseLog.distSqr(new Vec3i(pos.getX(), pos.getY(), pos.getZ()));
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
                    BlockPos nextPos = pos.offset(facing.getNormal());
                    if (world.isEmptyBlock(nextPos) && world.isAreaLoaded(nextPos, 8)) {
                        DeadwoodBranchBlock branch = (DeadwoodBranchBlock) BRANCH.getBlock();
                        this.setBlock(genReader, nextPos, branch.makeConnections(world, nextPos, facing));
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
                if (!genReader.isStateAtPosition(placedLocation, BlockState::isAir)) {
                    DeadwoodBranchBlock branch = (DeadwoodBranchBlock) BRANCH.getBlock();
                    this.setBlock(genReader, placedLocation, branch.makeConnections(world, placedLocation));
                }
            }

            // Failsafe, if the stack has gotten this large, something has probably gone wrong.
            if (queue.size() > 100) {
                break;
            }
        }
    }
}