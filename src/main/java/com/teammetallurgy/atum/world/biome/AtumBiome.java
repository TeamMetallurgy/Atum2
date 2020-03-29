package com.teammetallurgy.atum.world.biome;

import com.teammetallurgy.atum.init.AtumBiomes;
import com.teammetallurgy.atum.init.AtumEntities;
import com.teammetallurgy.atum.utils.AtumConfig;
import com.teammetallurgy.atum.world.gen.AtumSurfaceBuilders;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;

import javax.annotation.Nonnull;

public class AtumBiome extends Biome {
    private static EntityClassification UNDERGROUND; //TODO Revisit in 1.13
    private static EntityClassification SURFACE; //TODO Revisit in 1.13
    //protected BiomeDecoratorAtum atumDecorator;
    protected double deadwoodRarity = 0.1D;
    private int defaultWeight;

    public AtumBiome(Builder builder) {
        super(builder);
        this.defaultWeight = builder.defaultWeight;
        //this.atumDecorator = (BiomeDecoratorAtum) this.createBiomeDecorator();

        //this.spawnableMonsterList.clear();
        //this.spawnableCreatureList.clear();
        //this.spawnableWaterCreatureList.clear();
        //this.spawnableCaveCreatureList.clear();

        //this.topBlock = AtumBlocks.SAND.getDefaultState();
        //this.fillerBlock = AtumBlocks.LIMESTONE.getDefaultState();
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

    public int getDefaultWeight() {
        return this.defaultWeight;
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
    public int getModdedBiomeGrassColor(int original) {
        return 12889745;
    }

    @Override
    public int getModdedBiomeFoliageColor(int original) {
        return 12889745;
    }*/

    @Override
    @Nonnull
    public Biome getRiver() {
        return AtumBiomes.DRIED_RIVER;
    }

    public static class Builder extends Biome.Builder {
        private int defaultWeight;

        public Builder(String biomeName, int weight) {
            this.precipitation(RainType.NONE);
            this.downfall(0.0F);
            this.temperature(2.0F);
            this.waterColor(4159204);
            this.waterFogColor(329011); //TODO Figure out what this is. Value copied from vanilla
            this.setBaseHeight(0.135F);
            this.setHeightVariation(0.05F);
            this.parent(null);
            this.category(Category.DESERT);
            this.surfaceBuilder(SurfaceBuilder.DEFAULT, AtumSurfaceBuilders.SANDY);
            this.defaultWeight = weight;
            if (weight > 0) {
                new AtumConfig.Biome(AtumConfig.BUILDER, biomeName, weight); //Write config
            }
        }

        public Builder setBaseHeight(float height) {
            this.depth(height);
            return this;
        }

        public Builder setHeightVariation(float variation) {
            this.scale(variation);
            return this;
        }

        public Builder setBiomeBlocks(SurfaceBuilderConfig builderConfig) {
            this.surfaceBuilder(SurfaceBuilder.DEFAULT, builderConfig);
            return this;
        }
    }
}