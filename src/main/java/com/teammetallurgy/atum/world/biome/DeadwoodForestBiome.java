package com.teammetallurgy.atum.world.biome;

public class DeadwoodForestBiome extends AtumBiome {
    //private WorldGenerator anputsFingersGen = new WorldGenBush(AtumBlocks.ANPUTS_FINGERS);

    public DeadwoodForestBiome() {
        super(new Builder("deadwood_forest", 10));
        this.deadwoodRarity = 1.0D;
        //this.decorator.grassPerChunk = 1;
        this.addDefaultSpawns();
    }

    /*@Override
    public void decorate(@Nonnull World world, @Nonnull Random random, @Nonnull BlockPos pos) {
        super.decorate(world, random, pos);

        if (TerrainGen.decorate(world, random, new ChunkPos(pos), DecorateBiomeEvent.Decorate.EventType.SHROOM)) {
            for (int amount = 0; amount < 10; ++amount) {
                if (random.nextInt(10) == 0) {
                    int x = random.nextInt(16) + 8;
                    int z = random.nextInt(16) + 8;
                    BlockPos genPos = world.getHeight(pos.add(x, 0, z));
                    this.anputsFingersGen.generate(world, random, genPos);
                }
            }
        }
    }*/
}