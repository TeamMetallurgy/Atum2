/*package com.teammetallurgy.atum.world.gen;

public class AtumChunkGenerator extends NoiseChunkGenerator {

    @Override
    @Nonnull
    public List<Biome.SpawnListEntry> getPossibleCreatures(@Nonnull EntityClassification type, @Nonnull BlockPos pos) { //TODO
        if (AtumFeatures.LIGHTHOUSE.isPositionInStructure(this.world, pos)) {
            if (type == EntityClassification.AMBIENT) {
                return AtumFeatures.LIGHTHOUSE.getSpawnList();
            }
        }
        return super.getPossibleCreatures(type, pos);
    }
}*/