package com.teammetallurgy.atum.world.gen.feature;

import com.mojang.serialization.Codec;
import com.teammetallurgy.atum.init.AtumBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

import javax.annotation.Nonnull;
import java.util.Random;

public class LimestoneSpikeFeature extends Feature<NoFeatureConfig> { //Based on IceSpikeFeature

    public LimestoneSpikeFeature(Codec<NoFeatureConfig> config) {
        super(config);
    }

    @Override
    public boolean func_241855_a(@Nonnull ISeedReader seedReader, @Nonnull ChunkGenerator generator, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull NoFeatureConfig config) {
        while (seedReader.isAirBlock(pos) && pos.getY() > 2) {
            pos = pos.down();
        }

        if (seedReader.getBlockState(pos).getBlock() != AtumBlocks.SAND) {
            return false;
        } else {
            pos = pos.up(rand.nextInt(4));
            int i = rand.nextInt(4) + 7;
            int j = i / 4 + rand.nextInt(2);

            for (int k = 0; k < i; ++k) {
                float f = (1.0F - (float) k / (float) i) * (float) j;
                int l = MathHelper.ceil(f);

                for (int i1 = -l; i1 <= l; ++i1) {
                    float f1 = (float) MathHelper.abs(i1) - 0.25F;

                    for (int j1 = -l; j1 <= l; ++j1) {
                        float f2 = (float) MathHelper.abs(j1) - 0.25F;
                        if ((i1 == 0 && j1 == 0 || !(f1 * f1 + f2 * f2 > f * f)) && (i1 != -l && i1 != l && j1 != -l && j1 != l || !(rand.nextFloat() > 0.75F))) {
                            BlockState blockstate = seedReader.getBlockState(pos.add(i1, k, j1));
                            Block block = blockstate.getBlock();
                            if (blockstate.isAir(seedReader, pos.add(i1, k, j1)) || block == AtumBlocks.SAND) {
                                this.setBlockState(seedReader, pos.add(i1, k, j1), AtumBlocks.LIMESTONE.getDefaultState());
                            }

                            if (k != 0 && l > 1) {
                                blockstate = seedReader.getBlockState(pos.add(i1, -k, j1));
                                block = blockstate.getBlock();
                                if (blockstate.isAir(seedReader, pos.add(i1, -k, j1)) || block == AtumBlocks.SAND) {
                                    this.setBlockState(seedReader, pos.add(i1, -k, j1), AtumBlocks.LIMESTONE.getDefaultState());
                                }
                            }
                        }
                    }
                }
            }

            int k1 = j - 1;
            if (k1 < 0) {
                k1 = 0;
            } else if (k1 > 1) {
                k1 = 1;
            }

            for (int l1 = -k1; l1 <= k1; ++l1) {
                for (int i2 = -k1; i2 <= k1; ++i2) {
                    BlockPos blockpos = pos.add(l1, -1, i2);
                    int j2 = 50;
                    if (Math.abs(l1) == 1 && Math.abs(i2) == 1) {
                        j2 = rand.nextInt(5);
                    }

                    while (blockpos.getY() > 50) {
                        BlockState blockstate1 = seedReader.getBlockState(blockpos);
                        Block block1 = blockstate1.getBlock();
                        if (!blockstate1.isAir(seedReader, blockpos) && block1 != AtumBlocks.SAND) {
                            break;
                        }

                        this.setBlockState(seedReader, blockpos, AtumBlocks.LIMESTONE.getDefaultState());
                        blockpos = blockpos.down();
                        --j2;
                        if (j2 <= 0) {
                            blockpos = blockpos.down(rand.nextInt(5) + 1);
                            j2 = rand.nextInt(5);
                        }
                    }
                }
            }
            return true;
        }
    }
}