package com.teammetallurgy.atum.world.gen.feature;

import com.mojang.serialization.Codec;
import com.teammetallurgy.atum.init.AtumBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import javax.annotation.Nonnull;

public class LimestoneSpikeFeature extends Feature<NoneFeatureConfiguration> { //Based on IceSpikeFeature

    public LimestoneSpikeFeature(Codec<NoneFeatureConfiguration> config) {
        super(config);
    }

    @Override
    public boolean place(@Nonnull FeaturePlaceContext<NoneFeatureConfiguration> placeContext) {
        WorldGenLevel genLevel = placeContext.level();
        BlockPos pos = placeContext.origin();
        RandomSource random = placeContext.random();

        while (genLevel.isEmptyBlock(pos) && pos.getY() > genLevel.getMinBuildHeight() + 2) {
            pos = pos.below();
        }

        if (genLevel.getBlockState(pos).getBlock() != AtumBlocks.STRANGE_SAND.get()) {
            return false;
        } else {
            pos = pos.above(random.nextInt(4));
            int i = random.nextInt(4) + 7;
            int j = i / 4 + random.nextInt(2);
            if (j > 1 && random.nextInt(60) == 0) {
                pos = pos.above(10 + random.nextInt(30));
            }

            for (int k = 0; k < i; ++k) {
                float f = (1.0F - (float) k / (float) i) * (float) j;
                int l = Mth.ceil(f);

                for (int i1 = -l; i1 <= l; ++i1) {
                    float f1 = (float) Mth.abs(i1) - 0.25F;

                    for (int j1 = -l; j1 <= l; ++j1) {
                        float f2 = (float) Mth.abs(j1) - 0.25F;
                        if ((i1 == 0 && j1 == 0 || !(f1 * f1 + f2 * f2 > f * f)) && (i1 != -l && i1 != l && j1 != -l && j1 != l || !(random.nextFloat() > 0.75F))) {
                            BlockState blockstate = genLevel.getBlockState(pos.offset(i1, k, j1));
                            Block block = blockstate.getBlock();
                            if (blockstate.isAir() || block == AtumBlocks.STRANGE_SAND.get()) {
                                this.setBlock(genLevel, pos.offset(i1, k, j1), AtumBlocks.LIMESTONE.get().defaultBlockState());
                            }

                            if (k != 0 && l > 1) {
                                blockstate = genLevel.getBlockState(pos.offset(i1, -k, j1));
                                block = blockstate.getBlock();
                                if (blockstate.isAir() || block == AtumBlocks.STRANGE_SAND.get()) {
                                    this.setBlock(genLevel, pos.offset(i1, -k, j1), AtumBlocks.LIMESTONE.get().defaultBlockState());
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
                        j2 = random.nextInt(5);
                    }

                    while (blockpos.getY() > 50) {
                        BlockState blockstate1 = genLevel.getBlockState(blockpos);
                        Block block1 = blockstate1.getBlock();
                        if (!blockstate1.isAir() && block1 != AtumBlocks.STRANGE_SAND.get()) {
                            break;
                        }

                        this.setBlock(genLevel, blockpos, AtumBlocks.LIMESTONE.get().defaultBlockState());
                        blockpos = blockpos.below();
                        --j2;
                        if (j2 <= 0) {
                            blockpos = blockpos.below(random.nextInt(5) + 1);
                            j2 = random.nextInt(5);
                        }
                    }
                }
            }
            return true;
        }
    }
}