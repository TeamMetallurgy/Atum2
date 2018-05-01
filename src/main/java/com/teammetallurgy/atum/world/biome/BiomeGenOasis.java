package com.teammetallurgy.atum.world.biome;

import com.teammetallurgy.atum.init.AtumBlocks;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.feature.WorldGenLakes;

import java.util.Random;

public class BiomeGenOasis extends AtumBiome {
    protected final int lakeRarity = 3;
    protected final int waterLevel = 64;

    public BiomeGenOasis(BiomeProperties properties) {
        super(properties);

        super.topBlock = AtumBlocks.FERTILE_SOIL.getDefaultState();

        // no hostile spawns here

        super.decorator.reedsPerChunk = 10;
        super.decorator.clayPerChunk = 1;
        super.decorator.waterlilyPerChunk = 2;

        super.palmRarity = 3;
        super.pyramidRarity = -1;
        super.deadwoodRarity = -1;
    }

    @Override
    public void decorate(World world, Random random, BlockPos pos) {
        int x = random.nextInt(16) + 8;
        int z = random.nextInt(16) + 8;
        int height = random.nextInt(random.nextInt(248) + 8);

        if (height < waterLevel || random.nextInt(lakeRarity) == 0) {
            (new WorldGenLakes(Blocks.WATER)).generate(world, random, pos.add(x, height, z));
        }
        super.decorate(world, random, pos);
    }

    @Override
    public void genTerrainBlocks(World world, Random random, ChunkPrimer chunkPrimer, int x, int z, double stoneNoise) { //TODO Figure out what this is based of
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
                            yy = yy +1;
                            state = Blocks.WATERLILY.getDefaultState(); //TODO
                        }
                        makingPond = true;
                    }
                    chunkPrimer.setBlockState(zz, yy, xx, state);
                }
            }
        }
        super.genTerrainBlocks(world, random, chunkPrimer, x, z, stoneNoise);
    }
}