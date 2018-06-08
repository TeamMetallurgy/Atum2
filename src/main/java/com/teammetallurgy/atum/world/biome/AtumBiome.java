package com.teammetallurgy.atum.world.biome;

import com.google.common.collect.Lists;
import com.teammetallurgy.atum.entity.EntityDesertWolf;
import com.teammetallurgy.atum.entity.EntityTarantula;
import com.teammetallurgy.atum.entity.bandit.EntityBarbarian;
import com.teammetallurgy.atum.entity.bandit.EntityBrigand;
import com.teammetallurgy.atum.entity.bandit.EntityNomad;
import com.teammetallurgy.atum.entity.stone.EntityStoneBase;
import com.teammetallurgy.atum.entity.stone.EntityStoneguard;
import com.teammetallurgy.atum.entity.undead.EntityBonestorm;
import com.teammetallurgy.atum.entity.undead.EntityForsaken;
import com.teammetallurgy.atum.entity.undead.EntityMummy;
import com.teammetallurgy.atum.entity.undead.EntityWraith;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.world.gen.feature.WorldGenDeadwood;
import com.teammetallurgy.atum.world.gen.feature.WorldGenPalm;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.chunk.ChunkPrimer;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Random;

public class AtumBiome extends Biome {
    private List<SpawnListEntry> undergroundMonsterList = Lists.newArrayList();
    private int weight;
    private int size;
    int deadwoodRarity = 5;
    int palmRarity = 5;
    int pyramidRarity = 240;

    public AtumBiome(AtumBiomeProperties properties) {
        super(properties);
        this.weight = properties.weight;
        this.size = properties.size;
        this.decorator = createBiomeDecorator();

        this.spawnableMonsterList.clear();
        this.spawnableCreatureList.clear();
        this.spawnableWaterCreatureList.clear();
        this.spawnableCaveCreatureList.clear();

        this.topBlock = AtumBlocks.SAND.getDefaultState();
        this.fillerBlock = AtumBlocks.LIMESTONE.getDefaultState();
    }

    public int getWeight() {
        return weight;
    }

    public int getSize() {
        return size;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    void addDefaultSpawns() { //TODO Fix weights
        this.spawnableCreatureList.add(new SpawnListEntry(EntityDesertWolf.class, 4, 1, 4));
        this.spawnableMonsterList.add(new SpawnListEntry(EntityBarbarian.class, 2, 1, 4));
        this.spawnableMonsterList.add(new SpawnListEntry(EntityBonestorm.class, 6, 1, 3));
        this.spawnableMonsterList.add(new SpawnListEntry(EntityBrigand.class, 6, 2, 2));
        this.spawnableMonsterList.add(new SpawnListEntry(EntityForsaken.class, 6, 1, 4));
        this.spawnableMonsterList.add(new SpawnListEntry(EntityMummy.class, 6, 1, 4));
        this.spawnableMonsterList.add(new SpawnListEntry(EntityNomad.class, 6, 1, 4));
        this.spawnableMonsterList.add(new SpawnListEntry(EntityWraith.class, 6, 1, 4));
        this.undergroundMonsterList.add(new SpawnListEntry(EntityStoneguard.class, 7, 1, 2));
        this.undergroundMonsterList.add(new SpawnListEntry(EntityTarantula.class, 10, 1, 3));
    }

    /*@Override
    @Nonnull
    public List<Biome.SpawnListEntry> getSpawnableList(EnumCreatureType creatureType) {
        if (creatureType == EnumCreatureType.MONSTER) {
            return this.spawnableMonsterList;
        } else if (creatureType == EnumCreatureType.CREATURE) {
            return this.spawnableCreatureList;
        } else if (creatureType == EntityStoneBase.STONE) {
            return this.undergroundMonsterList;
        } else if (this.modSpawnableLists != null) {
            if (!this.modSpawnableLists.containsKey(creatureType)) {
                this.modSpawnableLists.put(creatureType, Lists.newArrayList());
            }
            return this.modSpawnableLists.get(creatureType);
        } else {
            return super.getSpawnableList(creatureType);
        }
    }*/

    @Override
    @Nonnull
    public BiomeDecorator createBiomeDecorator() {
        final BiomeDecorator dec = new BiomeDecoratorAtum();
        dec.deadBushPerChunk = 5;
        dec.reedsPerChunk = 0;
        dec.cactiPerChunk = 0;

        return dec;
    }

    @Override
    public void decorate(@Nonnull World world, @Nonnull Random random, @Nonnull BlockPos pos) {
        int x = random.nextInt(4) + 4;
        int z = random.nextInt(4) + 4;

        BlockPos height = world.getHeight(pos.add(x, 0, z));
        if (palmRarity > 0 && random.nextInt(palmRarity) == 0) {
            new WorldGenPalm(true, random.nextInt(4) + 5).generate(world, random, height);
        } else if (pyramidRarity > 0 && random.nextInt(pyramidRarity) == 0) {
            //new WorldGenPyramid().generate(world, random, pos.add(x, 0, z));
        } else if (deadwoodRarity > 0 && random.nextInt(deadwoodRarity) == 0) {
            new WorldGenDeadwood(true, random.nextInt(1) + 6).generate(world, random, height);
        }
        super.decorate(world, random, pos);
    }

    @Override
    public void genTerrainBlocks(World world, Random random, @Nonnull ChunkPrimer chunkPrimer, int x, int z, double stoneNoise) {
        int height = 63;
        IBlockState stateTop = this.topBlock;
        IBlockState stateFiller = this.fillerBlock;
        int flag = -1;
        int elevation = (int) (stoneNoise / 3.0D + 3.0D + random.nextDouble() * 0.25D);
        int xx = x & 15;
        int zz = z & 15;

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
                            stateTop = Blocks.AIR.getDefaultState();
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

                        if (flag == 0 && stateFiller.getBlock() == AtumBlocks.SAND && elevation > 1) {
                            flag = random.nextInt(4) + Math.max(0, zz - height);
                            stateFiller = AtumBlocks.LIMESTONE.getDefaultState();
                        }
                    }
                }
            }
        }
    }

    public static class AtumBiomeProperties extends BiomeProperties {
        private int weight;
        private int size;

        public AtumBiomeProperties(String biomeName, int weight) {
            super(biomeName);
            this.setBaseHeight(0.135F);
            this.setHeightVariation(0.05F);
            this.setRainfall(0.0F);
            this.setRainDisabled();
            this.setTemperature(2.0F);
            this.setWaterColor(16421912);
            this.weight = weight;
            this.size = 4;
        }

        public AtumBiomeProperties setSize(int size) {
            this.size = size;
            return this;
        }

        @Override
        @Nonnull
        public AtumBiomeProperties setBaseHeight(float height) {
            super.setBaseHeight(height);
            return this;
        }

        @Override
        @Nonnull
        public AtumBiomeProperties setHeightVariation(float variation) {
            super.setHeightVariation(variation);
            return this;
        }
    }
}