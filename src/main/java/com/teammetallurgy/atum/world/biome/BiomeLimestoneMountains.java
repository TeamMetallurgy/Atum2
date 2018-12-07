package com.teammetallurgy.atum.world.biome;

import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.world.biome.base.AtumBiome;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;

import javax.annotation.Nonnull;
import java.util.Random;

public class BiomeLimestoneMountains extends AtumBiome {

    public BiomeLimestoneMountains(AtumBiomeProperties properties) {
        super(properties);

        this.fillerBlock = AtumBlocks.LIMESTONE.getDefaultState();

        this.deadwoodRarity = 0.20D;

        this.addDefaultSpawns();
    }

    @Override
    public void genTerrainBlocks(World world, Random random, @Nonnull ChunkPrimer chunkPrimer, int x, int z, double stoneNoise) {
        final int y = world.getHeight(x, z);

        if (y <= 72 || stoneNoise < 1.0D) {
            super.topBlock = AtumBlocks.SAND.getDefaultState();
        } else {
            super.topBlock = AtumBlocks.LIMESTONE.getDefaultState();
        }
        // something weird's going on here...
        super.genTerrainBlocks(world, random, chunkPrimer, x, z, stoneNoise);
    }
}