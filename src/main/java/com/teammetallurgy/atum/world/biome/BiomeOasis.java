package com.teammetallurgy.atum.world.biome;

import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.world.biome.base.AtumBiome;
import com.teammetallurgy.atum.world.gen.feature.WorldGenPalm;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenLakes;

import javax.annotation.Nonnull;
import java.util.Random;

public class BiomeOasis extends AtumBiome {

    public BiomeOasis(AtumBiomeProperties properties) {
        super(properties);

        this.topBlock = AtumBlocks.FERTILE_SOIL.getDefaultState();

        // no hostile spawns here

        this.decorator.deadBushPerChunk = 0;
        this.atumDecorator.shrubChance = 0;
        this.decorator.grassPerChunk = 3;

        this.pyramidRarity = -1;
        this.deadwoodRarity = -1;
    }

    @Override
    public void decorate(@Nonnull World world, @Nonnull Random random, @Nonnull BlockPos pos) {
        int x = random.nextInt(16) + 8;
        int z = random.nextInt(16) + 8;

        int i1 = random.nextInt(16) + 8;
        int j1 = random.nextInt(256);
        int k1 = random.nextInt(16) + 8;
        (new WorldGenLakes(Blocks.WATER)).generate(world, random, pos.add(i1, j1, k1));

        if (random.nextFloat() <= 0.70F) {
            new WorldGenPalm(true, random.nextInt(4) + 5).generate(world, random, world.getHeight(pos.add(x, 0, z)));
        }

        super.decorate(world, random, pos);
    }

    /*@Override
    public void genTerrainBlocks(World world, Random random, @Nonnull ChunkPrimer chunkPrimer, int x, int z, double stoneNoise) { //TODO Figure out what this is based of
        double noise = GRASS_COLOR_NOISE.getValue((double) x * 0.25D, (double) z * 0.25D);

        boolean makingPond = false;
        int depth = 1 + random.nextInt(5);

        if (noise > -0.0D) {
            int xx = x & 15;
            int zz = z & 15;

            // if the block is visible to the sky and there is ground at "waterLevel",
            // turn it into shallow water instead:
            for (int yy = 255; yy >= 0; --yy) {

                IBlockState state = chunkPrimer.getBlockState(zz, yy, xx);

                if (state.getMaterial() == Material.AIR) {
                    if (makingPond) {
                        if (--depth == 0) break;
                        state = Blocks.WATER.getDefaultState();
                    } else if (yy <= waterLevel && state == this.topBlock) {
                        state = Blocks.WATER.getDefaultState();
                        if (noise < 0.12D) {
                            yy = yy + 1;
                        }
                        makingPond = true;
                    }
                    chunkPrimer.setBlockState(zz, yy, xx, state);
                }
            }
        }
        super.genTerrainBlocks(world, random, chunkPrimer, x, z, stoneNoise);
    }*/
}