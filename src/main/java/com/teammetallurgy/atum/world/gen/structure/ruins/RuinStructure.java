package com.teammetallurgy.atum.world.gen.structure.ruins;

import com.mojang.serialization.Codec;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.world.DimensionHelper;
import com.teammetallurgy.atum.world.gen.structure.StructureHelper;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;

import javax.annotation.Nonnull;
import java.util.Random;

import net.minecraft.world.level.levelgen.feature.StructureFeature.StructureStartFactory;

public class RuinStructure extends StructureFeature<NoneFeatureConfiguration> {

    public RuinStructure(Codec<NoneFeatureConfiguration> config) {
        super(config);
    }

    @Override
    protected boolean isFeatureChunk(@Nonnull ChunkGenerator generator, @Nonnull BiomeSource provider, long seed, @Nonnull WorldgenRandom seedRandom, int chunkX, int chunkZ, @Nonnull Biome biome, @Nonnull ChunkPos chunkPos, @Nonnull NoneFeatureConfiguration config) {
        for (Biome b : provider.getBiomesWithin(chunkX * 16 + 9, DimensionHelper.GROUND_LEVEL, chunkZ * 16 + 9, 17)) {
            if (!b.getGenerationSettings().isValidStart(this)) {
                return false;
            }
        }
        return true;
    }

    @Override
    @Nonnull
    public StructureStartFactory<NoneFeatureConfiguration> getStartFactory() {
        return Start::new;
    }

    public static class Start extends StructureStart<NoneFeatureConfiguration> {

        public Start(StructureFeature<NoneFeatureConfiguration> structure, int chunkPosX, int chunkPosZ, BoundingBox box, int references, long seed) {
            super(structure, chunkPosX, chunkPosZ, box, references, seed);
        }

        @Override
        public void generatePieces(@Nonnull RegistryAccess registries, @Nonnull ChunkGenerator generator, @Nonnull StructureManager manager, int chunkX, int chunkZ, @Nonnull Biome biome, @Nonnull NoneFeatureConfiguration config) {
            Rotation rotation = Rotation.getRandom(this.random);
            int y = StructureHelper.getYPosForStructure(chunkX, chunkZ, generator, rotation);

            if (y > 60 && y < 85) {
                BlockPos pos = new BlockPos(chunkX * 16, y, chunkZ * 16);
                this.pieces.add(new RuinPieces.RuinTemplate(manager, pos, this.random, rotation));
                this.calculateBoundingBox();
            }
        }

        @Override
        public void placeInChunk(@Nonnull WorldGenLevel seedReader, @Nonnull StructureFeatureManager manager, @Nonnull ChunkGenerator generator, @Nonnull Random rand, @Nonnull BoundingBox box, @Nonnull ChunkPos chunkPos) {
            super.placeInChunk(seedReader, manager, generator, rand, box, chunkPos);
            int y = this.boundingBox.y0;

            for (int x = box.x0; x <= box.x1; ++x) {
                for (int z = box.z0; z <= box.z1; ++z) {
                    BlockPos pos = new BlockPos(x, y, z);

                    if (!StructureHelper.doesChunkHaveStructure(seedReader, pos, StructureFeature.VILLAGE)) {
                        if (!seedReader.isEmptyBlock(pos) && this.boundingBox.isInside(pos)) {
                            boolean isVecInside = false;

                            for (StructurePiece piece : this.pieces) {
                                if (piece.getBoundingBox().isInside(pos)) {
                                    isVecInside = true;
                                    break;
                                }
                            }

                            if (isVecInside) {
                                for (int ruinY = y - 1; ruinY > 1; --ruinY) {
                                    BlockPos ruinPos = new BlockPos(x, ruinY, z);

                                    if (!seedReader.isEmptyBlock(ruinPos) && !seedReader.getBlockState(ruinPos).getMaterial().isLiquid()) {
                                        break;
                                    }
                                    Block brick = AtumBlocks.LIMESTONE_BRICK_LARGE;
                                    if (rand.nextDouble() <= 0.20D) {
                                        brick = AtumBlocks.LIMESTONE_BRICK_CRACKED_BRICK;
                                    } else if (rand.nextDouble() >= 0.80D) {
                                        brick = AtumBlocks.LIMESTONE_BRICK_SMALL;
                                    }
                                    seedReader.setBlock(ruinPos, brick.defaultBlockState(), 2);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}