package com.teammetallurgy.atum.world.gen.structure.pyramid;

import com.teammetallurgy.atum.init.AtumBiomes;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.world.ChunkGeneratorAtum;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureStart;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MapGenPyramid extends MapGenStructure {
    private static final List<Biome> ALLOWED_BIOMES = Arrays.asList(AtumBiomes.SAND_PLAINS, AtumBiomes.SAND_DUNES, AtumBiomes.LIMESTONE_CRAGS, AtumBiomes.DEADWOOD_FOREST);
    private final ChunkGeneratorAtum chunkGenerator;
    private int seed = 10387404;
    private int spacing = 16;
    private int separation = 4;

    public MapGenPyramid(ChunkGeneratorAtum chunkGenerator) {
        this.chunkGenerator = chunkGenerator;
    }
    
    public boolean isPyramidInChunk(int chunkX, int chunkZ) {
        for (int dx = -2; dx <= 2; dx++) {
            for (int dz = -2; dz <= 2; dz++) {
                if (this.structureMap.containsKey(ChunkPos.asLong(chunkX + dx, chunkZ + dz))) {
                    StructureStart pyramid = structureMap.get(ChunkPos.asLong(chunkX + dx, chunkZ + dz));
                    ChunkPos chunkPos = new ChunkPos(chunkX, chunkZ);
                    if (pyramid.getBoundingBox() != null && pyramid.getBoundingBox().intersectsWith(
                            chunkPos.getXStart(), chunkPos.getZStart(), chunkPos.getXEnd(), chunkPos.getZEnd())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    @Nonnull
    public String getStructureName() {
        return String.valueOf(PyramidPieces.PYRAMID);
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
            return this.world.getBiomeProvider().areBiomesViable(x * 16 + 8, z * 16 + 8, 16, ALLOWED_BIOMES);
        }
        return false;
    }

    @Override
    @Nonnull
    protected StructureStart getStructureStart(int chunkX, int chunkZ) {
        return new Start(this.world, this.chunkGenerator, this.rand, chunkX, chunkZ);
    }

    /*@SubscribeEvent
    public static void onBlockPlaced(PlayerInteractEvent.RightClickBlock event) {
        if (!event.getWorld().isRemote) {
            WorldServer world = (WorldServer) event.getWorld();
            if (world.getChunkProvider().chunkGenerator.isInsideStructure(world, String.valueOf(PyramidPieces.PYRAMID), new BlockPos(event.getHitVec()))) {
                if (!event.getEntityPlayer().isCreative() && !(Block.getBlockFromItem(event.getItemStack().getItem()) instanceof BlockTorch)) {
                    event.setCanceled(true);
                }
            }
        }
    }*/

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

            if (y < 57) {
                this.isValid = false;
            } else {
                int yChance = MathHelper.getInt(random, 10, 16);
                BlockPos pos = new BlockPos(chunkX * 16 + 8, y - yChance, chunkZ * 16 + 8);
                List<StructureComponent> components = PyramidPieces.getComponents(world.getSaveHandler().getStructureTemplateManager(), pos, rotation, random);
                this.components.addAll(components);
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