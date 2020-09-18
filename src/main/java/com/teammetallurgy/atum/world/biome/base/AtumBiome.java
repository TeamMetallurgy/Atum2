package com.teammetallurgy.atum.world.biome.base;

import com.teammetallurgy.atum.entity.animal.EntityDesertRabbit;
import com.teammetallurgy.atum.entity.animal.EntityTarantula;
import com.teammetallurgy.atum.entity.bandit.EntityAssassin;
import com.teammetallurgy.atum.entity.bandit.EntityBarbarian;
import com.teammetallurgy.atum.entity.bandit.EntityBrigand;
import com.teammetallurgy.atum.entity.bandit.EntityNomad;
import com.teammetallurgy.atum.entity.stone.EntityStoneguard;
import com.teammetallurgy.atum.entity.stone.EntityStonewarden;
import com.teammetallurgy.atum.entity.undead.EntityBonestorm;
import com.teammetallurgy.atum.entity.undead.EntityForsaken;
import com.teammetallurgy.atum.entity.undead.EntityMummy;
import com.teammetallurgy.atum.entity.undead.EntityWraith;
import com.teammetallurgy.atum.init.AtumBiomes;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.utils.AtumConfig;
import com.teammetallurgy.atum.utils.AtumUtils;
import com.teammetallurgy.atum.world.gen.feature.WorldGenDeadwood;
import com.teammetallurgy.atum.world.gen.feature.WorldGenFossil;
import com.teammetallurgy.atum.world.gen.feature.WorldGenOasisGrass;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.event.terraingen.TerrainGen;

import javax.annotation.Nonnull;
import java.util.Random;

public class AtumBiome extends Biome {
    //private static EnumCreatureType UNDERGROUND; //TODO Revisit in 1.13
    //private static EnumCreatureType SURFACE; //TODO Revisit in 1.13
    protected BiomeDecoratorAtum atumDecorator;
    private int weight;
    protected double deadwoodRarity = 0.1D;

    public AtumBiome(AtumBiomeProperties properties) {
        super(properties);
        this.weight = properties.weight;
        this.atumDecorator = (BiomeDecoratorAtum) this.createBiomeDecorator();

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

    protected void addDefaultSpawns() {
        //Animals
        addSpawn(EntityDesertRabbit.class, 5, 2, 3, EnumCreatureType.CREATURE);
        addSpawn(EntityBat.class, 5, 8, 8, EnumCreatureType.AMBIENT);

        //Bandits
        addSpawn(EntityAssassin.class, 1, 1, 1, EnumCreatureType.MONSTER);
        addSpawn(EntityBarbarian.class, 8, 1, 2, EnumCreatureType.MONSTER);
        addSpawn(EntityBrigand.class, 30, 2, 3, EnumCreatureType.MONSTER);
        addSpawn(EntityNomad.class, 22, 1, 4, EnumCreatureType.MONSTER);

        //Undead
        addSpawn(EntityBonestorm.class, 5, 1, 2, EnumCreatureType.MONSTER);
        addSpawn(EntityForsaken.class, 22, 1, 4, EnumCreatureType.MONSTER);
        addSpawn(EntityMummy.class, 30, 1, 3, EnumCreatureType.MONSTER);
        addSpawn(EntityWraith.class, 10, 1, 2, EnumCreatureType.MONSTER);

        //Underground
        addSpawn(EntityStoneguard.class, 34, 1, 2, EnumCreatureType.MONSTER);
        addSpawn(EntityStonewarden.class, 1, 1, 1, EnumCreatureType.MONSTER);
        addSpawn(EntityTarantula.class, 20, 1, 3, EnumCreatureType.MONSTER);
    }

    protected void addSpawn(Class<? extends EntityLiving> entityClass, int weight, int min, int max, EnumCreatureType type) {
        String category = AtumConfig.MOBS + Configuration.CATEGORY_SPLITTER + AtumUtils.toRegistryName(entityClass.getSimpleName()).replace("entity_", "").replace("_", " ");
        weight = AtumConfig.config.get(category, "weight", weight).getInt();
        min = AtumConfig.config.get(category, "min", min).getInt();
        max = AtumConfig.config.get(category, "max", max).getInt();
        this.getSpawnableList(type).add(new SpawnListEntry(entityClass, weight, min, max));
    }

    public static void initCreatureTypes() { //TODO Revisit in 1.13
       /* UNDERGROUND = Objects.requireNonNull(EnumHelper.addCreatureType("UNDERGROUND", IUnderground.class, 20, Material.AIR, false, false));
        SURFACE = Objects.requireNonNull(EnumHelper.addCreatureType("SURFACE", IMob.class, 45, Material.AIR, false, false));*/
    }

    @Override
    @Nonnull
    public BiomeDecorator getModdedBiomeDecorator(@Nonnull BiomeDecorator original) {
        final BiomeDecorator dec = new BiomeDecoratorAtum();
        dec.deadBushPerChunk = 5;
        dec.reedsPerChunk = 0;
        dec.cactiPerChunk = 0;
        dec.grassPerChunk = 0;

        return dec;
    }

    @Override
    @Nonnull
    public WorldGenerator getRandomWorldGenForGrass(Random rand) {
        if (this == AtumBiomes.OASIS) {
            return new WorldGenOasisGrass(AtumBlocks.OASIS_GRASS);
        } else {
            return new WorldGenOasisGrass(AtumBlocks.DEAD_GRASS);
        }
    }

    @Override
    public void decorate(@Nonnull World world, @Nonnull Random random, @Nonnull BlockPos pos) {
        int x = random.nextInt(16) + 8;
        int z = random.nextInt(16) + 8;

        BlockPos height = world.getHeight(pos.add(x, 0, z));
        if (TerrainGen.decorate(world, random, new ChunkPos(pos), DecorateBiomeEvent.Decorate.EventType.TREE) && this.deadwoodRarity > 0 && random.nextDouble() <= this.deadwoodRarity) {
            new WorldGenDeadwood(false).generate(world, random, height);
        }
        if (TerrainGen.decorate(world, random, new ChunkPos(pos), DecorateBiomeEvent.Decorate.EventType.FOSSIL)) {
            if (random.nextInt(64) == 0) {
                (new WorldGenFossil()).generate(world, random, pos);
            }
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
                } else if (existingState.getBlock() == AtumBlocks.LIMESTONE || existingState.getBlock() == Blocks.STONE) {
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

    @Override
    public int getModdedBiomeGrassColor(int original) {
        return 12889745;
    }

    @Override
    public int getModdedBiomeFoliageColor(int original) {
        return 12889745;
    }

    public static class AtumBiomeProperties extends BiomeProperties {
        private int weight;

        public AtumBiomeProperties(String biomeName, int weight) {
            super(biomeName);
            this.setBaseHeight(0.135F);
            this.setHeightVariation(0.05F);
            this.setRainfall(0.0F);
            this.setRainDisabled();
            this.setTemperature(2.0F);
            this.setWaterColor(16421912);
            this.weight = weight != 0 ? AtumConfig.config.get(AtumConfig.BIOME + Configuration.CATEGORY_SPLITTER + biomeName, "weight", weight).getInt() : 0;

            AtumConfig.config.save();
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