package com.teammetallurgy.atum.world.biome;

import com.teammetallurgy.atum.entity.*;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.world.decorators.WorldGenDeadwood;
import com.teammetallurgy.atum.world.decorators.WorldGenPalm;
import com.teammetallurgy.atum.world.decorators.WorldGenPyramid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.chunk.ChunkPrimer;

import java.util.Random;

public class AtumBiome extends Biome {
    private int weight = AtumBiomes.DEFAULT_BIOME_WEIGHT;
    protected AtumBiomes.BiomeType biomeType;
    protected int deadwoodRarity = 5;
    protected int palmRarity = 5;
    protected int pyramidRarity = 240;

    public AtumBiome(Biome.BiomeProperties properties) {
        super(properties);

        super.spawnableMonsterList.clear();
        super.spawnableCreatureList.clear();
        super.spawnableWaterCreatureList.clear();
        super.spawnableCaveCreatureList.clear();

        this.topBlock = AtumBlocks.SAND.getDefaultState();
        this.fillerBlock = AtumBlocks.LIMESTONE.getDefaultState();
    }

    public int getWeight() {
        return weight;
    }

    protected void addDefaultSpawns() {
        super.spawnableMonsterList.add(new SpawnListEntry(EntityMummy.class, 6, 4, 4));
        super.spawnableMonsterList.add(new SpawnListEntry(EntityBrigand.class, 6, 2, 2));
        super.spawnableMonsterList.add(new SpawnListEntry(EntityBarbarian.class, 2, 4, 4));
        super.spawnableMonsterList.add(new SpawnListEntry(EntityNomad.class, 6, 4, 4));
        super.spawnableMonsterList.add(new SpawnListEntry(EntityForsaken.class, 6, 4, 4));
        super.spawnableMonsterList.add(new SpawnListEntry(EntityWraith.class, 6, 4, 4));
        super.spawnableMonsterList.add(new SpawnListEntry(EntityDesertWolf.class, 4, 1, 4));
        super.spawnableMonsterList.add(new SpawnListEntry(EntityStoneguard.class, 6, 4, 4));
        super.spawnableMonsterList.add(new SpawnListEntry(EntityBonestorm.class, 6, 4, 4));
    }

    @Override
    public BiomeDecorator createBiomeDecorator() {
        final BiomeDecorator dec = new BiomeDecoratorAtum();
        dec.deadBushPerChunk = 5;
        dec.reedsPerChunk = 0;
        dec.cactiPerChunk = 0;

        return dec;
    }

    @Override
    public void decorate(World world, Random random, BlockPos pos) {
        super.decorate(world, random, pos);

        int x = random.nextInt(16) + 8;
        int z = random.nextInt(16) + 8;
        int height;

        if (palmRarity > 0 && random.nextInt(palmRarity) == 0) {
            height = random.nextInt(4) + 5;
            (new WorldGenPalm(true, height)).generate(world, random, pos.add(x, height, z));
        } else if (pyramidRarity > 0 && random.nextInt(pyramidRarity) == 0) {
            (new WorldGenPyramid()).generate(world, random, pos.add(x, 0, z));
        } else if (deadwoodRarity > 0 && random.nextInt(deadwoodRarity) == 0) {
            height = random.nextInt(1) + 6;
            (new WorldGenDeadwood(true, height)).generate(world, random, pos.add(x, height, z));
        }

    }

    @Override
    public void genTerrainBlocks(World world, Random random, ChunkPrimer chunkPrimer, int x, int z, double stoneNoise) {
        int height = 63;
        IBlockState stateTop = this.topBlock;
        IBlockState stateFiller = this.fillerBlock;
        int flag = -1;
        int elevation = (int) (stoneNoise / 3.0D + 3.0D + random.nextDouble() * 0.25D);
        final int xx = x & 15;
        final int zz = z & 15;

        for (int yy = 255; yy >= 0; --yy) {
            if (yy <= random.nextInt(5)) {
                chunkPrimer.setBlockState(zz, yy, xx, Blocks.BEDROCK.getDefaultState());
            } else {
                IBlockState existingState = chunkPrimer.getBlockState(zz, yy, xx);

                if (existingState.getMaterial() == Material.AIR) {
                    flag = -1;
                } else if (existingState == AtumBlocks.LIMESTONE.getDefaultState()) {
                    if (flag == -1) {
                        if (elevation <= 0) {
                            stateTop = null;
                            stateFiller = AtumBlocks.LIMESTONE.getDefaultState();
                        } else if (yy >= height - 4 && yy <= height + 1) {
                            stateTop = this.topBlock;
                            stateFiller = this.fillerBlock;
                        }

                        flag = elevation;
                        if (yy >= height - 1) {
                            chunkPrimer.setBlockState(zz, yy, xx, stateTop);
                        } else {
                            chunkPrimer.setBlockState(zz, yy, xx, stateFiller);
                        }
                    } else if (flag > 0) {
                        --flag;
                        chunkPrimer.setBlockState(zz, yy, xx, stateFiller);

                        if (flag == 0 && stateFiller.getBlock() == AtumBlocks.SAND) {
                            flag = random.nextInt(4) + Math.max(0, zz - height);
                            stateFiller = AtumBlocks.LIMESTONE.getDefaultState();
                        }
                    }
                }
            }
        }
    }

    public static class AtumBiomeProperties extends BiomeProperties {

        public AtumBiomeProperties(String biomeName) {
            super(biomeName);
            this.setBaseHeight(0.125F); // same as plains
            this.setHeightVariation(0.05F);
            this.setRainDisabled();
            //this.weight = biomeType.getWeight(); //TODO Fix Weight
            //biomeType.setGen(new AtumBiome(this));
            //this.setColor(16421912); //TODO
        }

        /*public AtumBiomeProperties setWeight(int weight) {
            this.weight = weight;
            return this;
        }*/
    }
}