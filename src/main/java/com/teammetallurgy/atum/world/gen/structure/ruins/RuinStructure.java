package com.teammetallurgy.atum.world.gen.structure.ruins;

import com.mojang.serialization.Codec;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumFeatures;
import com.teammetallurgy.atum.world.gen.structure.StructureHelper;
import net.minecraft.util.ResourceLocation;
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

    /*@Override
    @Nonnull
    protected ChunkPos getStartPositionForPosition(ChunkGenerator<?> chunkGenerator, Random random, int x, int z, int spacingOffsetsX, int spacingOffsetsZ) { //TODO. Look into StructureSeparationSettings
        int spacing = AtumConfig.WORLD_GEN.ruinsSpacing.get();
        int separation = AtumConfig.WORLD_GEN.ruinsSeparation.get();
        int k = x + spacing * spacingOffsetsX;
        int l = z + spacing * spacingOffsetsZ;
        int i1 = k < 0 ? k - spacing + 1 : k;
        int j1 = l < 0 ? l - spacing + 1 : l;
        int k1 = i1 / spacing;
        int l1 = j1 / spacing;
        ((SharedSeedRandom) random).setLargeFeatureSeed(chunkGenerator.getSeed(), k1, l1);
        k1 = k1 * spacing;
        l1 = l1 * spacing;
        k1 = k1 + (random.nextInt(spacing - separation) + random.nextInt(spacing - separation)) / 2;
        l1 = l1 + (random.nextInt(spacing - separation) + random.nextInt(spacing - separation)) / 2;
        return new ChunkPos(k1, l1);
    }*/

    @Override
    protected boolean func_230363_a_(@Nonnull ChunkGenerator generator, @Nonnull BiomeProvider provider, long seed, @Nonnull SharedSeedRandom seedRandom, int chunkX, int chunkZ, @Nonnull Biome biome, @Nonnull ChunkPos chunkPos, @Nonnull NoFeatureConfig config) {
        for (Biome b : provider.getBiomes(chunkX * 16 + 9, generator.func_230356_f_(), chunkZ * 16 + 9, 32)) {
            if (!b.getGenerationSettings().hasStructure(this)) {
                return false;
            } else {
                int y = StructureHelper.getYPosForStructure(chunkX, chunkZ, generator, null);
                return y > 60 && y < 85;
            }
        }
        return true;
    }

    @Override
    @Nonnull
    public String getStructureName() {
        return String.valueOf(new ResourceLocation(Atum.MOD_ID, "ruin"));
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
        public void func_230366_a_(@Nonnull ISeedReader seedReader, @Nonnull StructureManager manager, @Nonnull ChunkGenerator generator, @Nonnull Random rand, @Nonnull MutableBoundingBox box, ChunkPos chunkPos) {
            boolean doesChunkHaveStructure = StructureHelper.doesChunkHaveStructure(seedReader, chunkPos.x, chunkPos.z, AtumFeatures.PYRAMID_STRUCTURE) || StructureHelper.doesChunkHaveStructure(seedReader, chunkPos.x, chunkPos.z, AtumFeatures.GIRAFI_TOMB_STRUCTURE);

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