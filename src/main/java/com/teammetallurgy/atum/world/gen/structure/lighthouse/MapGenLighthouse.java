package com.teammetallurgy.atum.world.gen.structure.lighthouse;

import com.google.common.collect.Lists;
import com.teammetallurgy.atum.entity.efreet.SunspeakerEntity;
import com.teammetallurgy.atum.init.AtumBiomes;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.world.ChunkGeneratorAtum;
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
import java.util.List;
import java.util.Random;

public class MapGenLighthouse extends MapGenStructure {
    private static final List<Biome.SpawnListEntry> SUNSPEAKERS = Lists.newArrayList();
    private final ChunkGeneratorAtum chunkGenerator;
    private int seed = 10387600;
    private int spacing = 10;
    private int separation = 4;

    public MapGenLighthouse(ChunkGeneratorAtum chunkGenerator) {
        this.chunkGenerator = chunkGenerator;
    }

    public List<Biome.SpawnListEntry> getNaturalSpawns() {
        return SUNSPEAKERS;
    }

    static {
        SUNSPEAKERS.add(new Biome.SpawnListEntry(SunspeakerEntity.class, 1, 1, 1));
    }

    @Override
    @Nonnull
    public String getStructureName() {
        return String.valueOf(LighthousePieces.LIGHTHOUSE);
    }

    @Nullable
    @Override
    public BlockPos getNearestStructurePos(@Nonnull World world, @Nonnull BlockPos pos, boolean findUnexplored) {
        this.world = world;
        return findNearestStructurePosBySpacing(world, this, pos, this.spacing, this.separation, this.seed, true, 100, findUnexplored);
    }

    @Override
    protected boolean canSpawnStructureAtCoords(int chunkX, int chunkZ) {
        int x = chunkX;
        int z = chunkZ;

        if (chunkX < 0) {
            chunkX -= this.spacing - 1;
        }
        if (chunkZ < 0) {
            chunkZ -= this.spacing - 1;
        }
        int xSpacing = chunkX / this.spacing;
        int zSpacing = chunkZ / this.spacing;
        Random random = this.world.setRandomSeed(xSpacing, zSpacing, this.seed);
        xSpacing = xSpacing * this.spacing;
        zSpacing = zSpacing * this.spacing;
        xSpacing = xSpacing + (random.nextInt(this.spacing - this.separation) + random.nextInt(this.spacing - this.separation)) / 2;
        zSpacing = zSpacing + (random.nextInt(this.spacing - this.separation) + random.nextInt(this.spacing - this.separation)) / 2;

        if (x == xSpacing && z == zSpacing) {
            BlockPos pos = new BlockPos(x * 16 + 8, 0, z * 16 + 8);
            return this.world.getBiome(pos) == AtumBiomes.LIMESTONE_MOUNTAINS;
        }
        return false;
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

        private void create(World world, ChunkGeneratorAtum chunkGenerator, Random random, int chunkX, int chunkZ) {
            Rotation rotation = Rotation.values()[random.nextInt(Rotation.values().length)];
            ChunkPrimer chunkPrimer = new ChunkPrimer();
            chunkGenerator.setBlocksInChunk(chunkX, chunkZ, chunkPrimer);
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

            int ground = chunkPrimer.findGroundBlockIdx(7, 7);
            int groundZ = chunkPrimer.findGroundBlockIdx(7, 7 + z);
            int groundX = chunkPrimer.findGroundBlockIdx(7 + x, 7);
            int groundXZ = chunkPrimer.findGroundBlockIdx(7 + x, 7 + z);
            int y = Math.min(Math.min(ground, groundZ), Math.min(groundX, groundXZ));

            if (y < 90) {
                this.isValid = false;
            } else {
                BlockPos pos = new BlockPos(chunkX * 16 + 8, y - 1, chunkZ * 16 + 8);
                LighthousePieces.LighthouseTemplate lighthouse = new LighthousePieces.LighthouseTemplate(world.getSaveHandler().getStructureTemplateManager(), pos, rotation);
                this.components.add(lighthouse);
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
                                component.addComponentParts(world, rand, box);
                                isVecInside = true;
                                break;
                            }
                        }

                        if (isVecInside) {
                            for (int lighthouseY = y - 1; lighthouseY > 1; --lighthouseY) {
                                BlockPos pyramidPos = new BlockPos(x, lighthouseY, z);

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