package com.teammetallurgy.atum.world.gen.structure.girafitomb;

import com.mojang.datafixers.Dynamic;
import com.teammetallurgy.atum.init.AtumBlocks;
import net.minecraft.util.Rotation;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeManager;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;

import javax.annotation.Nonnull;
import java.util.Random;
import java.util.function.Function;

public class GirafiTombStructure extends Structure<NoFeatureConfig> {

    public GirafiTombStructure(Function<Dynamic<?>, ? extends NoFeatureConfig> config) {
        super(config);
    }

    @Override
    @Nonnull
    protected ChunkPos getStartPositionForPosition(ChunkGenerator<?> chunkGenerator, Random random, int x, int z, int spacingOffsetsX, int spacingOffsetsZ) {
        int spacing = 160;
        int separation = 40;
        int k = x + spacing * spacingOffsetsX;
        int l = z + spacing * spacingOffsetsZ;
        int i1 = k < 0 ? k - spacing + 1 : k;
        int j1 = l < 0 ? l - spacing + 1 : l;
        int k1 = i1 / spacing;
        int l1 = j1 / spacing;
        ((SharedSeedRandom) random).setLargeFeatureSeedWithSalt(chunkGenerator.getSeed(), k1, l1, 10387999);
        k1 = k1 * spacing;
        l1 = l1 * spacing;
        k1 = k1 + (random.nextInt(spacing - separation) + random.nextInt(spacing - separation)) / 2;
        l1 = l1 + (random.nextInt(spacing - separation) + random.nextInt(spacing - separation)) / 2;
        return new ChunkPos(k1, l1);
    }

    @Override
    public boolean func_225558_a_(@Nonnull BiomeManager manager, @Nonnull ChunkGenerator<?> generator, @Nonnull Random rand, int chunkX, int chunkZ, @Nonnull Biome biome) {
        ChunkPos chunkpos = this.getStartPositionForPosition(generator, rand, chunkX, chunkZ, 0, 0);
        if (chunkX == chunkpos.x && chunkZ == chunkpos.z) {
            for (Biome checkBiome : generator.getBiomeProvider().getBiomes(chunkX * 16 + 9, generator.getSeaLevel(), chunkZ * 16 + 9, 32)) {
                if (!generator.hasStructure(checkBiome, this)) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    @Nonnull
    public IStartFactory getStartFactory() {
        return Start::new;
    }

    @Override
    @Nonnull
    public String getStructureName() {
        return String.valueOf(GirafiTombPieces.GIRAFI_TOMB);
    }

    @Override
    public int getSize() {
        return 1; //TODO
    }

    public static class Start extends StructureStart {

        public Start(Structure<?> structure, int chunkPosX, int chunkPosZ, MutableBoundingBox box, int references, long seed) {
            super(structure, chunkPosX, chunkPosZ, box, references, seed);
        }

        @Override
        public void init(@Nonnull ChunkGenerator<?> generator, @Nonnull TemplateManager manager, int chunkX, int chunkZ, @Nonnull Biome biome) {
            Rotation rotation = Rotation.values()[this.rand.nextInt(Rotation.values().length)];
            int x = 5;
            int z = 5;
            if (rotation == Rotation.CLOCKWISE_90) {
                x = -5;
            } else if (rotation == Rotation.CLOCKWISE_180) {
                x = -5;
                z = -5;
            } else if (rotation == Rotation.COUNTERCLOCKWISE_90) {
                z = -5;
            }

            int k = (chunkX << 4) + 7;
            int l = (chunkZ << 4) + 7;
            int i1 = generator.func_222531_c(k, l, Heightmap.Type.WORLD_SURFACE_WG);
            int j1 = generator.func_222531_c(k, l + z, Heightmap.Type.WORLD_SURFACE_WG);
            int k1 = generator.func_222531_c(k + x, l, Heightmap.Type.WORLD_SURFACE_WG);
            int l1 = generator.func_222531_c(k + x, l + z, Heightmap.Type.WORLD_SURFACE_WG);
            int y = Math.min(Math.min(i1, j1), Math.min(k1, l1));

            if (y >= 60) {
                BlockPos pos = new BlockPos(chunkX * 16 + 8, y, chunkZ * 16 + 8);
                GirafiTombPieces.GirafiTombTemplate girafiTomb = new GirafiTombPieces.GirafiTombTemplate(manager, pos, rotation);
                this.components.add(girafiTomb);
                this.recalculateStructureSize();
            }
        }

        @Override
        public void generateStructure(@Nonnull IWorld world, @Nonnull ChunkGenerator<?> generator, @Nonnull Random rand, @Nonnull MutableBoundingBox box, @Nonnull ChunkPos chunkPos) {
            super.generateStructure(world, generator, rand, box, chunkPos);
            int y = this.bounds.minY;

            for (int x = box.minX; x <= box.maxX; ++x) {
                for (int z = box.minZ; z <= box.maxZ; ++z) {
                    BlockPos pos = new BlockPos(x, y, z);

                    if (!world.isAirBlock(pos) && this.bounds.isVecInside(pos)) {
                        boolean isVecInside = false;

                        for (StructurePiece piece : this.components) {
                            if (piece.getBoundingBox().isVecInside(pos)) {
                                isVecInside = true;
                                break;
                            }
                        }

                        if (isVecInside) {
                            for (int pyramidY = y - 1; pyramidY > 1; --pyramidY) {
                                BlockPos tombPos = new BlockPos(x, pyramidY, z);

                                if (!world.isAirBlock(tombPos) && !world.getBlockState(tombPos).getMaterial().isLiquid()) {
                                    break;
                                }
                                world.setBlockState(tombPos, AtumBlocks.LIMESTONE_BRICK_LARGE.getDefaultState(), 2);
                            }
                        }
                    }
                }
            }
        }
    }
}