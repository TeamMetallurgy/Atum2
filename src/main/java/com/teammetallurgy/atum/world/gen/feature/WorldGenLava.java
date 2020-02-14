/*
package com.teammetallurgy.atum.world.gen.feature;

import com.teammetallurgy.atum.init.AtumBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenLakes;

import javax.annotation.Nonnull;
import java.util.Random;

public class WorldGenLava extends WorldGenLakes {
    private final Block block;

    public WorldGenLava(Block block) {
        super(block);
        this.block = block;
    }

    @Override
    public boolean generate(@Nonnull World world, @Nonnull Random rand, BlockPos position) {
        for (position = position.add(-8, 0, -8); position.getY() > 5 && world.isAirBlock(position); position = position.down()) {
            //Do nothing
        }
        if (position.getY() <= 4) {
            return false;
        } else {
            position = position.down(4);
            boolean[] aboolean = new boolean[2048];
            int i = rand.nextInt(4) + 4;

            for (int j = 0; j < i; ++j) {
                double d0 = rand.nextDouble() * 6.0D + 3.0D;
                double d1 = rand.nextDouble() * 4.0D + 2.0D;
                double d2 = rand.nextDouble() * 6.0D + 3.0D;
                double d3 = rand.nextDouble() * (16.0D - d0 - 2.0D) + 1.0D + d0 / 2.0D;
                double d4 = rand.nextDouble() * (8.0D - d1 - 4.0D) + 2.0D + d1 / 2.0D;
                double d5 = rand.nextDouble() * (16.0D - d2 - 2.0D) + 1.0D + d2 / 2.0D;

                for (int l = 1; l < 15; ++l) {
                    for (int i1 = 1; i1 < 15; ++i1) {
                        for (int j1 = 1; j1 < 7; ++j1) {
                            double d6 = ((double) l - d3) / (d0 / 2.0D);
                            double d7 = ((double) j1 - d4) / (d1 / 2.0D);
                            double d8 = ((double) i1 - d5) / (d2 / 2.0D);
                            double d9 = d6 * d6 + d7 * d7 + d8 * d8;

                            if (d9 < 1.0D) {
                                aboolean[(l * 16 + i1) * 8 + j1] = true;
                            }
                        }
                    }
                }
            }

            for (int k1 = 0; k1 < 16; ++k1) {
                for (int l2 = 0; l2 < 16; ++l2) {
                    for (int k = 0; k < 8; ++k) {
                        boolean flag = !aboolean[(k1 * 16 + l2) * 8 + k] && (k1 < 15 && aboolean[((k1 + 1) * 16 + l2) * 8 + k] || k1 > 0 && aboolean[((k1 - 1) * 16 + l2) * 8 + k] || l2 < 15 && aboolean[(k1 * 16 + l2 + 1) * 8 + k] || l2 > 0 && aboolean[(k1 * 16 + (l2 - 1)) * 8 + k] || k < 7 && aboolean[(k1 * 16 + l2) * 8 + k + 1] || k > 0 && aboolean[(k1 * 16 + l2) * 8 + (k - 1)]);

                        if (flag) {
                            Material material = world.getBlockState(position.add(k1, k, l2)).getMaterial();

                            if (k >= 4 && material.isLiquid()) {
                                return false;
                            }

                            if (k < 4 && !material.isSolid() && world.getBlockState(position.add(k1, k, l2)).getBlock() != this.block) {
                                return false;
                            }
                        }
                    }
                }
            }

            for (int l1 = 0; l1 < 16; ++l1) {
                for (int i3 = 0; i3 < 16; ++i3) {
                    for (int i4 = 0; i4 < 8; ++i4) {
                        if (aboolean[(l1 * 16 + i3) * 8 + i4]) {
                            world.setBlockState(position.add(l1, i4, i3), i4 >= 4 ? Blocks.AIR.getDefaultState() : this.block.getDefaultState(), 2);
                        }
                    }
                }
            }

            if (this.block.getDefaultState().getMaterial() == Material.LAVA) {
                for (int j2 = 0; j2 < 16; ++j2) {
                    for (int k3 = 0; k3 < 16; ++k3) {
                        for (int k4 = 0; k4 < 8; ++k4) {
                            boolean flag1 = !aboolean[(j2 * 16 + k3) * 8 + k4] && (j2 < 15 && aboolean[((j2 + 1) * 16 + k3) * 8 + k4] || j2 > 0 && aboolean[((j2 - 1) * 16 + k3) * 8 + k4] || k3 < 15 && aboolean[(j2 * 16 + k3 + 1) * 8 + k4] || k3 > 0 && aboolean[(j2 * 16 + (k3 - 1)) * 8 + k4] || k4 < 7 && aboolean[(j2 * 16 + k3) * 8 + k4 + 1] || k4 > 0 && aboolean[(j2 * 16 + k3) * 8 + (k4 - 1)]);

                            if (flag1 && (k4 < 4 || rand.nextInt(2) != 0) && world.getBlockState(position.add(j2, k4, k3)).getMaterial().isSolid()) {
                                world.setBlockState(position.add(j2, k4, k3), AtumBlocks.LIMESTONE.getDefaultState(), 2);
                            }
                        }
                    }
                }
            }
            return true;
        }
    }
}*/
