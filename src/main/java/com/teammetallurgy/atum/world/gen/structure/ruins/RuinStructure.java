package com.teammetallurgy.atum.world.gen.structure.ruins;

import com.mojang.serialization.Codec;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumStructures;
import com.teammetallurgy.atum.world.gen.structure.StructureHelper;
import net.minecraft.util.Rotation;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;

import javax.annotation.Nonnull;
import java.util.Random;

public class RuinStructure extends Structure<NoFeatureConfig> {

    public RuinStructure(Codec<NoFeatureConfig> config) {
        super(config);
    }

    @Override
    protected boolean func_230363_a_(@Nonnull ChunkGenerator generator, @Nonnull BiomeProvider provider, long seed, @Nonnull SharedSeedRandom seedRandom, int chunkX, int chunkZ, @Nonnull Biome biome, @Nonnull ChunkPos chunkPos, @Nonnull NoFeatureConfig config) {
        for (Biome b : provider.getBiomes(chunkX * 16 + 9, generator.func_230356_f_(), chunkZ * 16 + 9, 17)) {
            if (!b.getGenerationSettings().hasStructure(this)) {
                return false;
            } else {
                int y = StructureHelper.getYPosForStructure(chunkX, chunkZ, generator, null);
                return y > 60 && y < 85;
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

            if (y > 60 && y < 85) {
                BlockPos pos = new BlockPos(chunkX * 16 + 8, y - 1, chunkZ * 16 + 8);
                RuinPieces.RuinTemplate ruin = new RuinPieces.RuinTemplate(manager, pos, this.rand, rotation);
                this.components.add(ruin);
                this.recalculateStructureSize();
            }
        }

        @Override
        public void func_230366_a_(@Nonnull ISeedReader seedReader, @Nonnull StructureManager manager, @Nonnull ChunkGenerator generator, @Nonnull Random rand, @Nonnull MutableBoundingBox box, @Nonnull ChunkPos chunkPos) {
            BlockPos checkPos = new BlockPos(chunkPos.x, this.bounds.minY + 1, chunkPos.z);
            boolean doesChunkHaveStructure = StructureHelper.doesChunkHaveStructure(seedReader, checkPos, AtumStructures.PYRAMID_STRUCTURE) || StructureHelper.doesChunkHaveStructure(seedReader, checkPos, AtumStructures.GIRAFI_TOMB_STRUCTURE); //TODO Test

            if (!doesChunkHaveStructure) {
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
                                for (int ruinY = y - 1; ruinY > 1; --ruinY) {
                                    BlockPos tombPos = new BlockPos(x, ruinY, z);

                                    if (!seedReader.isAirBlock(tombPos) && !seedReader.getBlockState(tombPos).getMaterial().isLiquid()) {
                                        break;
                                    }
                                    seedReader.setBlockState(tombPos, AtumBlocks.LIMESTONE.getDefaultState(), 2);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}