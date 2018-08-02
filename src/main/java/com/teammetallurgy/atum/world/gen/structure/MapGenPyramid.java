package com.teammetallurgy.atum.world.gen.structure;

import com.teammetallurgy.atum.utils.Constants;
import com.teammetallurgy.atum.world.ChunkGeneratorAtum;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureStart;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

public class MapGenPyramid extends MapGenStructure {
    private final ChunkGeneratorAtum provider;
    private double chance = 0.10D;

    public MapGenPyramid(ChunkGeneratorAtum chunkGenerator) {
        this.provider = chunkGenerator;
    }

    @Override
    @Nonnull
    public String getStructureName() {
        return String.valueOf(PyramidTemplate.PYRAMID);
    }

    @Nullable
    @Override
    public BlockPos getNearestStructurePos(@Nonnull World world, @Nonnull BlockPos pos, boolean findUnexplored) {
        this.world = world;
        BiomeProvider biomeprovider = world.getBiomeProvider();
        return biomeprovider.isFixedBiome()  ? null : findNearestStructurePosBySpacing(world, this, pos, 80, 20, 10387319, true, 100, findUnexplored);
    }

    @Override
    protected boolean canSpawnStructureAtCoords(int chunkX, int chunkZ) {
        return this.rand.nextDouble() < this.chance && this.rand.nextInt(80) < Math.max(Math.abs(chunkX), Math.abs(chunkZ));
    }

    @Override
    @Nonnull
    protected StructureStart getStructureStart(int chunkX, int chunkZ) {
        return new Start(this.world, this.provider, this.rand, chunkX, chunkZ);
    }

    public static class Start extends StructureStart {
        private boolean isValid;

        public Start() {
        }

        Start(World world, ChunkGeneratorAtum chunkGenerator, Random random, int x, int z) {
            super(x, z);
            this.create(world, chunkGenerator, random, x, z);
        }

        private void create(World world, ChunkGeneratorAtum chunkGenerator, Random random, int x, int z) {
            Rotation rotation = Rotation.values()[random.nextInt(Rotation.values().length)];
            ChunkPrimer chunkprimer = new ChunkPrimer();
            chunkGenerator.setBlocksInChunk(x, z, chunkprimer);
            int i = 5;
            int j = 5;

            if (rotation == Rotation.CLOCKWISE_90) {
                i = -5;
            } else if (rotation == Rotation.CLOCKWISE_180) {
                i = -5;
                j = -5;
            } else if (rotation == Rotation.COUNTERCLOCKWISE_90) {
                j = -5;
            }

            int k = chunkprimer.findGroundBlockIdx(7, 7);
            int l = chunkprimer.findGroundBlockIdx(7, 7 + j);
            int i1 = chunkprimer.findGroundBlockIdx(7 + i, 7);
            int j1 = chunkprimer.findGroundBlockIdx(7 + i, 7 + j);
            int k1 = Math.min(Math.min(k, l), Math.min(i1, j1));

            if (k1 < 60) {
                this.isValid = false;
            } else {
                BlockPos pos = new BlockPos(x * 16 + 8, k1 + 1, z * 16 + 8);
                PyramidTemplate pyramidTemplate = new PyramidTemplate(world.getSaveHandler().getStructureTemplateManager(), pos, rotation);
                this.components.add(pyramidTemplate);
                this.updateBoundingBox();
                System.out.println("Generated Pyramid at pos: " + pos);
                this.isValid = true;
            }
        }

        @Override
        public void generateStructure(@Nonnull World world, @Nonnull Random rand, @Nonnull StructureBoundingBox structurebb) {
            super.generateStructure(world, rand, structurebb);
            int i = this.boundingBox.minY;

            for (int j = structurebb.minX; j <= structurebb.maxX; ++j) {
                for (int k = structurebb.minZ; k <= structurebb.maxZ; ++k) {
                    BlockPos blockpos = new BlockPos(j, i, k);

                    if (!world.isAirBlock(blockpos) && this.boundingBox.isVecInside(blockpos)) {
                        boolean flag = false;

                        for (StructureComponent structurecomponent : this.components) {
                            if (structurecomponent.getBoundingBox().isVecInside(blockpos)) {
                                flag = true;
                                break;
                            }
                        }

                        if (flag) {
                            for (int l = i - 1; l > 1; --l) {
                                BlockPos blockpos1 = new BlockPos(j, l, k);

                                if (!world.isAirBlock(blockpos1) && !world.getBlockState(blockpos1).getMaterial().isLiquid()) {
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }

        @Override
        public boolean isSizeableStructure() {
            return this.isValid;
        }
    }
}