package com.teammetallurgy.atum.world.biome;

import com.teammetallurgy.atum.init.AtumEntities;
import com.teammetallurgy.atum.world.gen.AtumSurfaceBuilders;
import net.minecraft.entity.EntityClassification;

public class OasisBiome extends AtumBiome {

    public OasisBiome() {
        super(new Builder("oasis", 0).setHeightVariation(0.0F).setBiomeBlocks(AtumSurfaceBuilders.OASIS));
        //this.decorator.deadBushPerChunk = 0;
        //this.atumDecorator.shrubChance = 0;
        //this.decorator.grassPerChunk = 3;
        //this.decorator.waterlilyPerChunk = 100;
        this.deadwoodRarity = 0.0D;
        addSpawn(AtumEntities.CAMEL, 6, 2, 6, EntityClassification.CREATURE);
    }

    /*@Override
    public void decorate(@Nonnull World world, @Nonnull Random random, @Nonnull BlockPos pos) {
        int x = random.nextInt(16) + 8;
        int z = random.nextInt(16) + 8;
        BlockPos height = world.getHeight(pos.add(x, 0, z));
        ChunkPos chunkPos = new ChunkPos(pos);

        new WorldGenOasisPond().generate(world, random, height);

        if (random.nextFloat() <= 0.98F) {
            new WorldGenPalm(true, random.nextInt(4) + 5, true).generate(world, random, height);
        }

        if (TerrainGen.decorate(world, random, chunkPos, DecorateBiomeEvent.Decorate.EventType.REED)) {
            int reedsPerChunk = 50;
            for (int reeds = 0; reeds < reedsPerChunk; ++reeds) {
                int y = height.getY() * 2;

                if (y > 0) {
                    int randomY = random.nextInt(y);
                    new WorldGenPapyrus().generate(world, random, pos.add(x, randomY, z));
                }
            }
        }
        if (TerrainGen.decorate(world, random, chunkPos, DecorateBiomeEvent.Decorate.EventType.LILYPAD)) {
            for (int amount = 0; amount < 2; ++amount) {
                int y = world.getHeight(pos.add(x, 0, z)).getY() * 2;
                if (y > 0) {
                    int randomY = random.nextInt(y);
                    BlockPos lilyPos;
                    BlockPos waterPos;
                    for (lilyPos = pos.add(x, randomY, z); lilyPos.getY() > 0; lilyPos = waterPos) {
                        waterPos = lilyPos.down();
                        if (!world.isAirBlock(waterPos)) {
                            break;
                        }
                    }
                    new WorldGenWaterlily().generate(world, random, lilyPos);
                }
            }
        }
        super.decorate(world, random, pos);
    }

    @Override
    public int getModdedBiomeFoliageColor(int original) {
        return 11987573;
    }

    @Override
    public int getModdedBiomeGrassColor(int original) {
        return 11987573;
    }*/
}