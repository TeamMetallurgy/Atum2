package com.teammetallurgy.atum.world.biome;

import com.teammetallurgy.atum.init.AtumEntities;
import com.teammetallurgy.atum.world.gen.AtumSurfaceBuilders;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;

import javax.annotation.Nonnull;

public class AtumBiome extends Biome {
    private static EntityClassification UNDERGROUND; //TODO Revisit in 1.13
    private static EntityClassification SURFACE; //TODO Revisit in 1.13
    //protected BiomeDecoratorAtum atumDecorator;
    private int weight;
    protected double deadwoodRarity = 0.1D;

    public AtumBiome(Builder builder) {
        super(builder);
        this.weight = builder.weight;
        //this.atumDecorator = (BiomeDecoratorAtum) this.createBiomeDecorator();

        //this.spawnableMonsterList.clear();
        //this.spawnableCreatureList.clear();
        //this.spawnableWaterCreatureList.clear();
        //this.spawnableCaveCreatureList.clear();

        //this.topBlock = AtumBlocks.SAND.getDefaultState();
        //this.fillerBlock = AtumBlocks.LIMESTONE.getDefaultState();
    }

    public int getWeight() {
        return weight;
    }

    protected void addDefaultSpawns() {
        //Animals
        addSpawn(AtumEntities.DESERT_RABBIT, 5, 2, 3, EntityClassification.CREATURE);
        addSpawn(EntityType.BAT, 5, 8, 8, EntityClassification.AMBIENT);

        //Bandits
        addSpawn(AtumEntities.ASSASSIN, 1, 1, 1, EntityClassification.MONSTER);
        addSpawn(AtumEntities.BARBARIAN, 8, 1, 2, EntityClassification.MONSTER);
        addSpawn(AtumEntities.BRIGAND, 30, 2, 3, EntityClassification.MONSTER);
        addSpawn(AtumEntities.NOMAD, 22, 1, 4, EntityClassification.MONSTER);

        //Undead
        addSpawn(AtumEntities.BONESTORM, 5, 1, 2, EntityClassification.MONSTER);
        addSpawn(AtumEntities.FORSAKEN, 22, 1, 4, EntityClassification.MONSTER);
        addSpawn(AtumEntities.MUMMY, 30, 1, 3, EntityClassification.MONSTER);
        addSpawn(AtumEntities.WRAITH, 10, 1, 2, EntityClassification.MONSTER);

        //Underground
        addSpawn(AtumEntities.STONEGUARD, 34, 1, 2, EntityClassification.MONSTER);
        addSpawn(AtumEntities.STONEWARDEN, 1, 1, 1, EntityClassification.MONSTER);
        addSpawn(AtumEntities.TARANTULA, 20, 1, 3, EntityClassification.MONSTER);
    }

    protected void addSpawn(EntityType<?> entityType, int weight, int min, int max, EntityClassification type) {
       /* String category = AtumConfig.MOBS + Configuration.CATEGORY_SPLITTER + AtumUtils.toRegistryName(entityClass.getSimpleName()).replace("entity_", "").replace("_", " ");
        weight = AtumConfig.config.get(category, "weight", weight).getInt();
        min = AtumConfig.config.get(category, "min", min).getInt();
        max = AtumConfig.config.get(category, "max", max).getInt();*/
        this.getSpawns(type).add(new SpawnListEntry(entityType, weight, min, max));
    }

    public static void initCreatureTypes() { //TODO Revisit in 1.13
       /* UNDERGROUND = Objects.requireNonNull(EnumHelper.addCreatureType("UNDERGROUND", IUnderground.class, 20, Material.AIR, false, false));
        SURFACE = Objects.requireNonNull(EnumHelper.addCreatureType("SURFACE", IMob.class, 45, Material.AIR, false, false));*/
    }

    /*@Override
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
        if (this.deadwoodRarity > 0 && random.nextDouble() <= this.deadwoodRarity) {
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
        BlockState stateTop = this.topBlock;
        BlockState stateFiller = this.fillerBlock;
        int flag = -1;
        int elevation = (int) (stoneNoise / 3.0D + 3.0D + random.nextDouble() * 0.25D);
        int xx = x & 15;
        int zz = z & 15;

        for (int yy = 255; yy >= 0; --yy) {
            if (yy <= random.nextInt(5)) {
                chunkPrimer.setBlockState(zz, yy, xx, Blocks.BEDROCK.getDefaultState());
            } else {
                BlockState existingState = chunkPrimer.getBlockState(zz, yy, xx);

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
    }*/

    public static class Builder extends Biome.Builder {
        private int weight;

        public Builder(String biomeName, int weight) {
            this.weight = weight;
            this.precipitation(RainType.NONE);
            this.downfall(0.0F);
            this.temperature(2.0F);
            this.waterColor(16421912);
            this.waterFogColor(329011); //TODO Figure out what this is. Value copied from vanilla
            this.setBaseHeight(0.135F);
            this.setHeightVariation(0.05F);
            this.parent(null);
            this.category(Category.NONE); //TODO Figure out what setting Biome Category actually does
            this.surfaceBuilder(SurfaceBuilder.DEFAULT, AtumSurfaceBuilders.SANDY); //TODO add custom SurfaceBuilder
            /*this.weight = weight != 0 ? AtumConfig.config.get(AtumConfig.BIOME + Configuration.CATEGORY_SPLITTER + biomeName, "weight", weight).getInt() : 0;

            AtumConfig.config.save();*/
        }

        @Nonnull
        public Builder setBaseHeight(float height) {
            this.depth(height);
            return this;
        }

        @Nonnull
        public Builder setHeightVariation(float variation) {
            this.scale(variation);
            return this;
        }
    }
}