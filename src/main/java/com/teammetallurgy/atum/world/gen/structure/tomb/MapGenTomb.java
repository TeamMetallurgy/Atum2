/*
package com.teammetallurgy.atum.world.gen.structure.tomb;

import com.teammetallurgy.atum.world.ChunkGeneratorAtum;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureStart;

import javax.annotation.Nonnull;
import java.util.Random;

public class MapGenTomb extends MapGenStructure {
    private final ChunkGeneratorAtum chunkGenerator;
    private int seed = 10387666;
    private int spacing = 12;
    private int separation = 10;

    public MapGenTomb(ChunkGeneratorAtum chunkGenerator) {
        this.chunkGenerator = chunkGenerator;
    }

    @Override
    @Nonnull
    public String getStructureName() {
        return String.valueOf(TombPieces.TOMB);
    }

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

        return x == xSpacing && z == zSpacing;
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

            int y = MathHelper.getInt(random, 6, 55);
            BlockPos pos = new BlockPos(chunkX * 16 + 8, y, chunkZ * 16 + 8);

            if (y > 60*/
/* || !(world.getBlockState(pos).getBlock() instanceof BlockLimestone)*//*
) {
                this.isValid = false;
            } else {
                TombPieces.TombTemplate tomb = new TombPieces.TombTemplate(world.getSaveHandler().getStructureTemplateManager(), pos, random, rotation);
                this.components.add(tomb);
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
                        for (StructureComponent component : this.components) {
                            if (component.getBoundingBox().isVecInside(pos)) {
                                component.addComponentParts(world, rand, box);
                                break;
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
}*/
