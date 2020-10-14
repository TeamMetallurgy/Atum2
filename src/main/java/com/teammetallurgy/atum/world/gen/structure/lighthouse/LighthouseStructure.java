package com.teammetallurgy.atum.world.gen.structure.lighthouse;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumEntities;
import com.teammetallurgy.atum.world.gen.structure.StructureHelper;
import net.minecraft.util.Rotation;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Random;

public class LighthouseStructure extends Structure<NoFeatureConfig> {
    private static final List<MobSpawnInfo.Spawners> SUNSPEAKERS = Lists.newArrayList(new MobSpawnInfo.Spawners(AtumEntities.SUNSPEAKER, 2, 1, 1));

    public LighthouseStructure(Codec<NoFeatureConfig> config) {
        super(config);
    }

    @Override
    @Nonnull
    public List<MobSpawnInfo.Spawners> getDefaultSpawnList() {
        return SUNSPEAKERS;
    }

    @Override
    public boolean getDefaultRestrictsSpawnsToInside() {
        return true;
    }

    @Override
    protected boolean func_230363_a_(@Nonnull ChunkGenerator generator, @Nonnull BiomeProvider provider, long seed, @Nonnull SharedSeedRandom seedRandom, int chunkX, int chunkZ, @Nonnull Biome biome, @Nonnull ChunkPos chunkPos, @Nonnull NoFeatureConfig config) {
        for (Biome b : provider.getBiomes(chunkX * 16 + 9, generator.func_230356_f_(), chunkZ * 16 + 9, 12)) {
            if (!b.getGenerationSettings().hasStructure(this)) {
                return false;
            } else {
                return StructureHelper.getYPosForStructure(chunkX, chunkZ, generator, null) >= 90;
            }
        }
        return false;
    }

    @Override
    @Nonnull
    public IStartFactory<NoFeatureConfig> getStartFactory() {
        return Start::new;
    }

    public static class Start extends StructureStart<NoFeatureConfig> {

        public Start(Structure<NoFeatureConfig> structure, int chunkPosX, int chunkPosZ, MutableBoundingBox box, int references, long seed) {
            super(structure, chunkPosX, chunkPosZ, box, references, seed);
        }

        @Override
        public void func_230364_a_(@Nonnull DynamicRegistries registries, @Nonnull ChunkGenerator generator, @Nonnull TemplateManager manager, int chunkX, int chunkZ, @Nonnull Biome biome, @Nonnull NoFeatureConfig config) {
            Rotation rotation = Rotation.values()[this.rand.nextInt(Rotation.values().length)];
            int y = StructureHelper.getYPosForStructure(chunkX, chunkZ, generator, rotation);

            if (y >= 90) {
                BlockPos pos = new BlockPos(chunkX * 16 + 8, y, chunkZ * 16 + 8);
                LighthousePieces.LighthouseTemplate lighthouse = new LighthousePieces.LighthouseTemplate(manager, pos, rotation);
                this.components.add(lighthouse);
                this.recalculateStructureSize();
            }
        }

        @Override
        public void func_230366_a_(@Nonnull ISeedReader seedReader, @Nonnull StructureManager manager, @Nonnull ChunkGenerator generator, @Nonnull Random rand, @Nonnull MutableBoundingBox box, @Nonnull ChunkPos chunkPos) {
            super.func_230366_a_(seedReader, manager, generator, rand, box, chunkPos);
            int y = this.bounds.minY;

            for (int x = box.minX; x <= box.maxX; ++x) {
                for (int z = box.minZ; z <= box.maxZ; ++z) {
                    BlockPos pos = new BlockPos(x, y, z);

                    if (!seedReader.isAirBlock(pos) && this.bounds.isVecInside(pos)) {
                        boolean isVecInside = false;

                        for (StructurePiece piece : this.components) {
                            if (piece.getBoundingBox().isVecInside(pos)) {
                                isVecInside = true;
                                break;
                            }
                        }

                        if (isVecInside) {
                            for (int lighthouseY = y - 1; lighthouseY > 1; --lighthouseY) {
                                BlockPos lighthousePos = new BlockPos(x, lighthouseY, z);

                                if (!seedReader.isAirBlock(lighthousePos) && !seedReader.getBlockState(lighthousePos).getMaterial().isLiquid()) {
                                    break;
                                }
                                seedReader.setBlockState(lighthousePos, AtumBlocks.LIMESTONE.getDefaultState(), 2);
                            }
                        }
                    }
                }
            }
        }
    }
}