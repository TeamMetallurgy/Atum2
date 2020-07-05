package com.teammetallurgy.atum.world.biome;

import com.teammetallurgy.atum.blocks.SandLayersBlock;
import com.teammetallurgy.atum.init.AtumBiomes;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumEntities;
import com.teammetallurgy.atum.misc.AtumConfig;
import com.teammetallurgy.atum.world.gen.AtumSurfaceBuilders;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;

import javax.annotation.Nonnull;

public class AtumBiome extends Biome {
    private final int defaultWeight;

    public AtumBiome(Builder builder) {
        super(builder);
        this.defaultWeight = builder.defaultWeight;
    }

    protected void addDefaultSpawns(Biome biome) {
        //Animals
        addSpawn(biome, AtumEntities.DESERT_RABBIT, 5, 2, 3, EntityClassification.CREATURE);
        addSpawn(biome, EntityType.BAT, 5, 8, 8, EntityClassification.AMBIENT);

        //Undead
        addSpawn(biome, AtumEntities.BONESTORM, 5, 1, 2, EntityClassification.MONSTER);
        addSpawn(biome, AtumEntities.FORSAKEN, 22, 1, 4, EntityClassification.MONSTER);
        addSpawn(biome, AtumEntities.MUMMY, 30, 1, 3, EntityClassification.MONSTER);
        addSpawn(biome, AtumEntities.WRAITH, 10, 1, 2, EntityClassification.MONSTER);

        //Underground
        addSpawn(biome, AtumEntities.STONEGUARD, 34, 1, 2, EntityClassification.MONSTER);
        addSpawn(biome, AtumEntities.TARANTULA, 20, 1, 3, EntityClassification.MONSTER);
    }

    protected void addSpawn(Biome biome, EntityType<?> entityType, int weight, int min, int max, EntityClassification classification) {
        ResourceLocation location = entityType.getRegistryName();
        if (location != null) {
            new AtumConfig.Mobs(AtumConfig.BUILDER, location.getPath(), min, max, weight, entityType, classification, biome); //Write config
        }
    }

    protected void addCamelSpawning(Biome biome) {
        this.addSpawn(biome, AtumEntities.CAMEL, 6, 2, 6, EntityClassification.CREATURE);
    }

    protected void addDesertWolfSpawning(Biome biome) {
        this.addSpawn(biome, AtumEntities.DESERT_WOLF, 5, 2, 4, EntityClassification.CREATURE);
    }

    public static void initMobSpawns(Biome biome, EntityType<?> entityType) {
        String baseCategory = AtumConfig.Mobs.MOBS;
        EntityClassification classification = AtumConfig.Mobs.ENTITY_CLASSIFICATION.get(entityType);
        if (entityType != null && entityType.getRegistryName() != null) {
            String mobName = entityType.getRegistryName().getPath();
            int weight = AtumConfig.Helper.get(baseCategory, mobName, "weight");
            int min = AtumConfig.Helper.get(baseCategory, mobName, "min");
            int max = AtumConfig.Helper.get(baseCategory, mobName, "max");
            biome.getSpawns(classification).add(new SpawnListEntry(entityType, weight, min, max));
        }
    }

    public int getDefaultWeight() {
        return this.defaultWeight;
    }

    @Override
    public int getFoliageColor() {
        return 12889745;
    }

    @Override
    public int getGrassColor(double x, double z) {
        return 12889745;
    }

    @Override
    @Nonnull
    public Biome getRiver() {
        return AtumBiomes.DRIED_RIVER;
    }

    @Override
    public void decorate(@Nonnull GenerationStage.Decoration stage, @Nonnull ChunkGenerator<? extends GenerationSettings> chunkGenerator, @Nonnull IWorld world, long seed, @Nonnull SharedSeedRandom random, @Nonnull BlockPos pos) {
        super.decorate(stage, chunkGenerator, world, seed, random, pos);

        if (AtumConfig.WORLD_GEN.sandLayerEdge.get()) {
            for (int x = 0; x < 16; ++x) {
                for (int z = 0; z < 16; ++z) {
                    BlockPos height = world.getHeight(Heightmap.Type.MOTION_BLOCKING, pos.add(x, 0, z));

                    if (canPlaceSandLayer(world, height)) {
                        for (Direction facing : Direction.Plane.HORIZONTAL) {
                            BlockPos posOffset = height.offset(facing);
                            if (world.getBlockState(posOffset).isSolidSide(world, posOffset, Direction.UP)) {
                                int layers = MathHelper.nextInt(random, 1, 3);
                                world.setBlockState(height, AtumBlocks.SAND_LAYERED.getDefaultState().with(SandLayersBlock.LAYERS, layers), 2);
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean canPlaceSandLayer(IWorldReader world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        BlockState stateDown = world.getBlockState(pos.down());
        return this != AtumBiomes.OASIS
                && state.getMaterial().isReplaceable()
                && stateDown.getBlock() != AtumBlocks.LIMESTONE_CRACKED
                && Block.hasSolidSideOnTop(world, pos.down())
                && !(stateDown.getBlock() instanceof SandLayersBlock)
                && !(state.getBlock() instanceof SandLayersBlock);
    }

    public static class Builder extends Biome.Builder {
        private final int defaultWeight;

        public Builder(String biomeName, int weight) {
            this.precipitation(RainType.NONE);
            this.downfall(0.0F);
            this.temperature(2.0F);
            this.waterColor(4159204);
            this.waterFogColor(329011);
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