/*package com.teammetallurgy.atum.world;

public class ChunkGeneratorAtum implements IChunkGenerator {

    @Override
    public void populate(int x, int z) {
        BlockFalling.fallInstantly = true;
        int i = x * 16;
        int j = z * 16;
        BlockPos blockpos = new BlockPos(i, 0, j);
        Biome biome = this.world.getBiome(blockpos.add(16, 0, 16));
        this.rand.setSeed(this.world.getSeed());
        long k = this.rand.nextLong() / 2L * 2L + 1L;
        long l = this.rand.nextLong() / 2L * 2L + 1L;
        this.rand.setSeed((long) x * k + (long) z * l ^ this.world.getSeed());
        ChunkPos chunkpos = new ChunkPos(x, z);

        if (this.mapFeaturesEnabled) {
            if (this.settings.useMineShafts) {
                this.mineshaftGenerator.generateStructure(this.world, this.rand, chunkpos);
            }
            if (AtumConfig.WORLD_GEN.pyramidEnabled.get()) {
                this.pyramidGenerator.generateStructure(this.world, this.rand, chunkpos);
            }
            this.ruinGenerator.generateStructure(this.world, this.rand, chunkpos);
        }

        biome.decorate(this.world, this.rand, new BlockPos(i, 0, j));
        blockpos = blockpos.add(8, 0, 8);

        if (TerrainGen.populate(this, this.world, this.rand, x, z, false, PopulateChunkEvent.Populate.EventType.CUSTOM)) {
            for (int k2 = 0; k2 < 16; ++k2) {
                for (int j3 = 0; j3 < 16; ++j3) {
                    BlockPos blockpos1 = this.world.getPrecipitationHeight(blockpos.add(k2, 0, j3));

                    if (canPlaceSandLayer(world, blockpos1, biome) && world.isAirBlock(blockpos1)) {
                        for (Direction facing : Direction.HORIZONTALS) {
                            BlockPos posOffset = blockpos1.offset(facing);
                            if (world.getBlockState(posOffset).isSideSolid(world, posOffset, Direction.UP)) {
                                int layers = MathHelper.getInt(rand, 1, 3);
                                this.world.setBlockState(blockpos1, AtumBlocks.SAND_LAYERED.getDefaultState().with(BlockSandLayers.LAYERS, layers), 2);
                            }
                        }
                    }
                }
            }
        }
        BlockFalling.fallInstantly = false;
    }

    static boolean canPlaceSandLayer(World world, BlockPos pos, Biome biome) {
        BlockState stateDown = world.getBlockState(pos.down());
        return biome != AtumBiomes.OASIS
                && stateDown.getBlock() != AtumBlocks.LIMESTONE_CRACKED
                && stateDown.isSideSolid(world, pos, Direction.UP)
                && !(stateDown.getBlock() instanceof BlockSandLayers)
                && !(world.getBlockState(pos).getBlock() instanceof BlockSandLayers);
    }

    @Override
    public void recreateStructures(@Nonnull Chunk chunk, int x, int z) {
        if (this.mapFeaturesEnabled) {
            if (this.settings.useMineShafts) {
                this.mineshaftGenerator.generate(this.world, x, z, null);
            }
            if (AtumConfig.WORLD_GEN.pyramidEnabled.get()) {
                this.pyramidGenerator.generate(this.world, x, z, null);
            }
            this.ruinGenerator.generate(this.world, x, z, null);
        }
    }
}*/