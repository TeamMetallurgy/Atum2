package com.teammetallurgy.atum.world.gen.feature;

import com.mojang.serialization.Codec;
import com.teammetallurgy.atum.init.AtumBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import javax.annotation.Nonnull;
import java.util.Random;

public class LimestoneSpikeFeature extends Feature<NoneFeatureConfiguration> { //Based on IceSpikeFeature

    public LimestoneSpikeFeature(Codec<NoneFeatureConfiguration> config) {
        super(config);
    }

    @Override
    public boolean place(@Nonnull WorldGenLevel seedReader, @Nonnull ChunkGenerator generator, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull NoneFeatureConfiguration config) {
        while (seedReader.isEmptyBlock(pos) && pos.getY() > 2) {
            pos = pos.below();
        }

        if (seedReader.getBlockState(pos).getBlock() != AtumBlocks.SAND) {
            return false;
        } else {
            pos = pos.above(rand.nextInt(4));
            int i = rand.nextInt(4) + 7;
            int j = i / 4 + rand.nextInt(2);

            for (int k = 0; k < i; ++k) {
                float f = (1.0F - (float) k / (float) i) * (float) j;
                int l = Mth.ceil(f);

                for (int i1 = -l; i1 <= l; ++i1) {
                    float f1 = (float) Mth.abs(i1) - 0.25F;

                    for (int j1 = -l; j1 <= l; ++j1) {
                        float f2 = (float) Mth.abs(j1) - 0.25F;
                        if ((i1 == 0 && j1 == 0 || !(f1 * f1 + f2 * f2 > f * f)) && (i1 != -l && i1 != l && j1 != -l && j1 != l || !(rand.nextFloat() > 0.75F))) {
                            BlockState blockstate = seedReader.getBlockState(pos.offset(i1, k, j1));
                            Block block = blockstate.getBlock();
                            if (blockstate.isAir(seedReader, pos.offset(i1, k, j1)) || block == AtumBlocks.SAND) {
                                this.setBlock(seedReader, pos.offset(i1, k, j1), AtumBlocks.LIMESTONE.defaultBlockState());
                            }

                            if (k != 0 && l > 1) {
                                blockstate = seedReader.getBlockState(pos.offset(i1, -k, j1));
                                block = blockstate.getBlock();
                                if (blockstate.isAir(seedReader, pos.offset(i1, -k, j1)) || block == AtumBlocks.SAND) {
                                    this.setBlock(seedReader, pos.offset(i1, -k, j1), AtumBlocks.LIMESTONE.defaultBlockState());
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
                    BlockPos blockpos = pos.offset(l1, -1, i2);
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

                        this.setBlock(seedReader, blockpos, AtumBlocks.LIMESTONE.defaultBlockState());
                        blockpos = blockpos.below();
                        --j2;
                        if (j2 <= 0) {
                            blockpos = blockpos.below(rand.nextInt(5) + 1);
                            j2 = rand.nextInt(5);
                        }
                    }
                }
            }
            return true;
        }
    }
}