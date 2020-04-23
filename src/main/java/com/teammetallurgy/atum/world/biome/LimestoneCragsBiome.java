package com.teammetallurgy.atum.world.biome;

import com.teammetallurgy.atum.init.AtumFeatures;
import net.minecraft.world.biome.DefaultBiomeFeatures;

public class LimestoneCragsBiome extends AtumBiome {

    public LimestoneCragsBiome() {
        super(new Builder("limestone_crags", 3).setBaseHeight(0.225F).setHeightVariation(0.45000002F));
        this.deadwoodRarity = 0.12D;
        super.addDefaultSpawns(this);
        super.addDesertWolfSpawning(this);
        AtumFeatures.Default.addCarvers(this);
        AtumFeatures.Default.addSprings(this);
    }

  /*  @Override
    public void decorate(@Nonnull World world, @Nonnull Random random, @Nonnull BlockPos pos) {
        for (int i = 0; i < 3; ++i) {
            int j = random.nextInt(16) + 8;
            int k = random.nextInt(16) + 8;
            this.genSpikes.generate(world, random, world.getHeight(pos.add(j, 0, k)));
        }
        super.decorate(world, random, pos);
    }

    *//**
     * Adapted from {@link net.minecraft.world.gen.feature.WorldGenIceSpike}
     *//*
    public class WorldGenLimestoneSpike extends WorldGenerator {
        private final Block spikeBlock = AtumBlocks.LIMESTONE;
        private final Block groundBlock = AtumBlocks.SAND;

        @Override
        public boolean generate(@Nonnull World world, @Nonnull Random rand, @Nonnull BlockPos pos) {
            while (world.isAirBlock(pos) && pos.getY() > 1 || world.isAirBlock(pos.down())) {
                pos = pos.down();
            }

            if (world.getBlockState(pos).getBlock() != groundBlock) {
                return false;
            } else {
                int i = rand.nextInt(4) + 7;
                int j = i / 4 + rand.nextInt(2);

                for (int k = 0; k < i; ++k) {
                    float f = (1.0F - (float) k / (float) i) * (float) j;
                    int l = MathHelper.ceil(f);

                    for (int i1 = -l; i1 <= l; ++i1) {
                        float f1 = (float) MathHelper.abs(i1) - 0.25F;

                        for (int j1 = -l; j1 <= l; ++j1) {
                            float f2 = (float) MathHelper.abs(j1) - 0.25F;

                            if ((i1 == 0 && j1 == 0 || f1 * f1 + f2 * f2 <= f * f) && (i1 != -l && i1 != l && j1 != -l && j1 != l || rand.nextFloat() <= 0.75F)) {
                                BlockState state = world.getBlockState(pos.add(i1, k, j1));
                                Block block = state.getBlock();

                                if (block.isAir(state, world, pos.add(i1, k, j1)) || block == groundBlock) {
                                    this.setBlockAndNotifyAdequately(world, pos.add(i1, k, j1), spikeBlock.getDefaultState());
                                }

                                if (k != 0 && l > 1) {
                                    state = world.getBlockState(pos.add(i1, -k, j1));
                                    block = state.getBlock();

                                    if (block.isAir(state, world, pos.add(i1, -k, j1)) || block == groundBlock) {
                                        this.setBlockAndNotifyAdequately(world, pos.add(i1, -k, j1), spikeBlock.getDefaultState());
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
                        BlockPos pos1 = pos.add(l1, -1, i2);
                        int j2 = 50;

                        if (Math.abs(l1) == 1 && Math.abs(i2) == 1) {
                            j2 = rand.nextInt(5);
                        }

                        while (pos1.getY() > 50) {
                            BlockState state = world.getBlockState(pos1);
                            Block block1 = state.getBlock();

                            if (!state.getBlock().isAir(state, world, pos1) && block1 != groundBlock) {
                                break;
                            }
                            this.setBlockAndNotifyAdequately(world, pos1, spikeBlock.getDefaultState());
                            pos1 = pos1.down();
                            --j2;

                            if (j2 <= 0) {
                                pos1 = pos1.down(rand.nextInt(5) + 1);
                                j2 = rand.nextInt(5);
                            }
                        }
                    }
                }
                return true;
            }
        }
    }*/
}