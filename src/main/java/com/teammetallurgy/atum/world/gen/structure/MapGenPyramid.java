package com.teammetallurgy.atum.world.gen.structure;

import com.teammetallurgy.atum.init.AtumBiomes;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.world.ChunkGeneratorAtum;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureStart;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

public class MapGenPyramid extends MapGenStructure {
    private static final NonNullList<Biome> ALLOWED_BIOMES = NonNullList.from(AtumBiomes.SAND_PLAINS, AtumBiomes.SAND_DUNES, AtumBiomes.LIMESTONE_CRAGS, AtumBiomes.DEADWOOD_FOREST);
    private final ChunkGeneratorAtum chunkGenerator;

    public MapGenPyramid(ChunkGeneratorAtum chunkGenerator) {
        this.chunkGenerator = chunkGenerator;
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
        return findNearestStructurePosBySpacing(world, this, pos, 80, 32, 10387319, true, 100, findUnexplored);
    }

    @Override
    protected boolean canSpawnStructureAtCoords(int chunkX, int chunkZ) {
        /*int x = chunkX;
        int z = chunkZ;

        if (chunkX < 0) {
            x = chunkX - 79;
        }
        if (chunkZ < 0) {
            z = chunkZ - 79;
        }

        int xSpacing = x / 80;
        int zSpacing = z / 80;
        Random random = this.world.setRandomSeed(xSpacing, zSpacing, 10387319);
        xSpacing = xSpacing * 80;
        zSpacing = zSpacing * 80;
        //xSpacing = xSpacing + (random.nextInt(60) + random.nextInt(60)) / 2; //Rarity
        //zSpacing = zSpacing + (random.nextInt(60) + random.nextInt(60)) / 2; //Rarity

        if (chunkX == xSpacing && chunkZ == zSpacing) {
            return this.world.getBiomeProvider().areBiomesViable(chunkX * 16 + 8, chunkZ * 16 + 8, 32, ALLOWED_BIOMES);
        }*/
        return this.world.getBiomeProvider().areBiomesViable(chunkX * 16 + 8, chunkZ * 16 + 8, 32, ALLOWED_BIOMES);
    }

    @Override
    @Nonnull
    protected StructureStart getStructureStart(int chunkX, int chunkZ) {
        return new Start(this.world, this.chunkGenerator, this.rand, chunkX, chunkZ);
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
            ChunkPrimer chunkPrimer = new ChunkPrimer();
            chunkGenerator.setBlocksInChunk(x, z, chunkPrimer);
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

            int k = chunkPrimer.findGroundBlockIdx(7, 7);
            int l = chunkPrimer.findGroundBlockIdx(7, 7 + j);
            int i1 = chunkPrimer.findGroundBlockIdx(7 + i, 7);
            int j1 = chunkPrimer.findGroundBlockIdx(7 + i, 7 + j);
            int k1 = Math.min(Math.min(k, l), Math.min(i1, j1));

            if (k1 < 60) {
                this.isValid = false;
            } else {
                //int yChance = MathHelper.getInt(random, 10, 40);
                BlockPos pos = new BlockPos(x * 16 + 8, k1 - 10, z * 16 + 8);
                PyramidTemplate pyramidTemplate = new PyramidTemplate(world.getSaveHandler().getStructureTemplateManager(), pos, rotation);
                this.components.add(pyramidTemplate);
                this.updateBoundingBox();
                this.isValid = true;
            }
        }

        @Override
        public void generateStructure(@Nonnull World world, @Nonnull Random rand, @Nonnull StructureBoundingBox box) {
            super.generateStructure(world, rand, box);
            int y = this.boundingBox.minY;

            for (int x = box.minX; x <= box.maxX; ++x) {
                for (int z = box.minZ; z <= box.maxZ; ++z) {
                    BlockPos pos = new BlockPos(x, y, z);

                    if (!world.isAirBlock(pos) && this.boundingBox.isVecInside(pos)) {
                        boolean isVecInside = false;

                        for (StructureComponent component : this.components) {
                            if (component.getBoundingBox().isVecInside(pos)) {
                                isVecInside = true;
                                break;
                            }
                        }

                        if (isVecInside) {
                            for (int pyramidY = y - 1; pyramidY > 1; --pyramidY) {
                                BlockPos pyramidPos = new BlockPos(x, pyramidY, z);

                                if (!world.isAirBlock(pyramidPos) && !world.getBlockState(pyramidPos).getMaterial().isLiquid()) {
                                    break;
                                }
                                world.setBlockState(pyramidPos, AtumBlocks.LIMESTONE.getDefaultState(), 2);
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