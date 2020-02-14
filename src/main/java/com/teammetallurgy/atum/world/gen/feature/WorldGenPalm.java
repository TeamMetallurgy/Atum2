/*
package com.teammetallurgy.atum.world.gen.feature;

import com.teammetallurgy.atum.blocks.vegetation.BlockDate;
import com.teammetallurgy.atum.blocks.wood.BlockAtumPlank;
import com.teammetallurgy.atum.blocks.wood.PalmLeavesBlock;
import com.teammetallurgy.atum.blocks.wood.PalmSaplingBlock;
import com.teammetallurgy.atum.init.AtumBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockVine;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

import javax.annotation.Nonnull;
import java.util.Random;

public class WorldGenPalm extends WorldGenAbstractTree {
    private static final BlockState BLOCK_LOG = AtumBlocks.PALM_LOG.getDefaultState();
    private static final BlockState BLOCK_LEAVES = PalmLeavesBlock.getLeave(BlockAtumPlank.WoodType.PALM).getDefaultState().with(PalmLeavesBlock.CHECK_DECAY, false);
    private final int minTreeHeight;
    private final BlockState stateWood;
    private final BlockState stateLeaves;
    private boolean generateOphidiansTongue;

    public WorldGenPalm(boolean notify) {
        this(notify, 5, BLOCK_LOG, BLOCK_LEAVES, false);
    }

    public WorldGenPalm(boolean notify, int minTreeHeight, boolean generateOphidiansTongue) {
        this(notify, minTreeHeight, BLOCK_LOG, BLOCK_LEAVES, generateOphidiansTongue);
    }

    public WorldGenPalm(boolean notify, int minTreeHeight, BlockState wood, BlockState leaves, boolean generateOphidiansTongue) {
        super(notify);
        this.minTreeHeight = minTreeHeight;
        this.stateWood = wood;
        this.stateLeaves = leaves;
        this.generateOphidiansTongue = generateOphidiansTongue;
    }

    @Override
    public boolean generate(@Nonnull World world, @Nonnull Random random, @Nonnull BlockPos pos) {
        int treeHeight = random.nextInt(3) + this.minTreeHeight;
        boolean flag = true;
        BlockState soil = world.getBlockState(pos.down());
        boolean isSoil = stateWood.getBlock() == AtumBlocks.PALM_LOG ? soil.getBlock().canSustainPlant(soil, world, pos.down(), Direction.UP, (PalmSaplingBlock) PalmSaplingBlock.getSapling(BlockAtumPlank.WoodType.PALM)) && pos.getY() >= 1 && pos.getY() + treeHeight + 1 <= 256 : soil.getBlock() == AtumBlocks.LIMESTONE_GRAVEL;
        if (isSoil) {
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
                BlockState stateDown = world.getBlockState(down);

                if (pos.getY() >= 256 - treeHeight - 1) {
                    return false;
                } else {
                    stateDown.getBlock().onPlantGrow(stateDown, world, down, pos);
                    int i3 = pos.getX();
                    int j1 = pos.getZ();
                    int k1 = pos.getY() + treeHeight;

                    BlockPos leafPos = new BlockPos(i3, k1, j1);

                    this.spawnLeaf(world, leafPos.add(0, 1, 0));

                    for (BlockPos.MutableBlockPos baseLeafPos : BlockPos.MutableBlockPos.getAllInBoxMutable(leafPos.add(-1, 0, -1), leafPos.add(1, 0, 1))) {
                        this.spawnLeaf(world, baseLeafPos);
                    }

                    this.spawnLeaf(world, leafPos.add(2, 0, 0));
                    this.spawnLeaf(world, leafPos.add(-2, 0, 0));
                    this.spawnLeaf(world, leafPos.add(0, 0, 2));
                    this.spawnLeaf(world, leafPos.add(0, 0, -2));
                    this.spawnLeaf(world, leafPos.add(0, -1, -2));
                    this.spawnLeaf(world, leafPos.add(0, -1, 2));
                    this.spawnLeaf(world, leafPos.add(2, -1, 0));
                    this.spawnLeaf(world, leafPos.add(-2, -1, 0));
                    this.spawnLeaf(world, leafPos.add(0, -1, -3));
                    this.spawnLeaf(world, leafPos.add(0, -1, 3));
                    this.spawnLeaf(world, leafPos.add(3, -1, 0));
                    this.spawnLeaf(world, leafPos.add(-3, -1, 0));

                    if (this.generateOphidiansTongue) {
                        if (random.nextDouble() <= 0.50D) {
                            for (int height = 0; height < treeHeight - 1; ++height) {
                                BlockPos upN = pos.up(height);
                                stateDown = world.getBlockState(upN);

                                if (stateDown.getBlock().isAir(stateDown, world, upN) || stateDown.getBlock().isLeaves(stateDown, world, upN) || stateDown.getMaterial() == Material.VINE) {
                                    this.setBlockAndNotifyAdequately(world, pos.up(height), BLOCK_LOG);
                                    if (height > 0) {
                                        height += 1;
                                        int randAmount = MathHelper.getInt(random, 1, 3);
                                        if (random.nextInt(25) == 0 && world.isAirBlock(pos.add(-1, height, 0))) {
                                            for (int amount = 0; amount <= randAmount; amount++) {
                                                this.addOphidianTongue(world, pos.add(-1, height - amount, 0), BlockVine.EAST);
                                            }
                                        }
                                        if (random.nextInt(25) == 0 && world.isAirBlock(pos.add(1, height, 0))) {
                                            for (int amount = 0; amount <= randAmount; amount++) {
                                                this.addOphidianTongue(world, pos.add(1, height - amount, 0), BlockVine.WEST);
                                            }
                                        }
                                        if (random.nextInt(25) == 0 && world.isAirBlock(pos.add(0, height, -1))) {
                                            for (int amount = 0; amount <= randAmount; amount++) {
                                                this.addOphidianTongue(world, pos.add(0, height - amount, -1), BlockVine.SOUTH);
                                            }
                                        }
                                        if (random.nextInt(25) == 0 && world.isAirBlock(pos.add(0, height, 1))) {
                                            for (int amount = 0; amount <= randAmount; amount++) {
                                                this.addOphidianTongue(world, pos.add(0, height - amount, 1), BlockVine.NORTH);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    BlockPos datePos = leafPos.down().offset(Direction.Plane.HORIZONTAL.random(random));
                    if (this.stateLeaves.getBlock() == BLOCK_LEAVES.getBlock() && random.nextFloat() <= 0.10F) {
                        world.setBlockState(datePos, AtumBlocks.DATE_BLOCK.getDefaultState().with(BlockDate.AGE, MathHelper.getInt(random, 0, 7)), 2);
                    }
                }

                for (int height = 0; height < treeHeight; ++height) {
                    BlockPos upN = pos.up(height);
                    BlockState stateUpN = world.getBlockState(upN);

                    if (stateUpN.getBlock().isAir(stateUpN, world, upN) || stateUpN.getBlock().isLeaves(stateUpN, world, upN)) {
                        this.setBlockAndNotifyAdequately(world, pos.up(height), this.stateWood);
                    }
                }
                return true;
            }
        } else {
            return false;
        }
    }

    private void spawnLeaf(World world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        if (world.isAirBlock(pos) || state.getBlock().canBeReplacedByLeaves(state, world, pos)) {
            this.setBlockAndNotifyAdequately(world, pos, this.stateLeaves);
        }
    }

    private void addOphidianTongue(World world, BlockPos pos, PropertyBool prop) {
        this.setBlockAndNotifyAdequately(world, pos, AtumBlocks.OPHIDIAN_TONGUE.getDefaultState().with(prop, Boolean.TRUE));
    }
}*/
