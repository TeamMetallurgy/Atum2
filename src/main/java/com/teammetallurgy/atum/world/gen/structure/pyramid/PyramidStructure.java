package com.teammetallurgy.atum.world.gen.structure.pyramid;

import com.mojang.datafixers.Dynamic;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.world.gen.structure.StructureHelper;
import net.minecraft.util.Rotation;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeManager;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class PyramidStructure extends Structure<NoFeatureConfig> {

    public PyramidStructure(Function<Dynamic<?>, ? extends NoFeatureConfig> config) {
        super(config);
    }

    @Override
    @Nonnull
    protected ChunkPos getStartPositionForPosition(ChunkGenerator<?> chunkGenerator, Random random, int x, int z, int spacingOffsetsX, int spacingOffsetsZ) {
        int spacing = 18;
        int separation = 8;
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
    }

    @Override
    public boolean func_225558_a_(@Nonnull BiomeManager manager, @Nonnull ChunkGenerator<?> generator, @Nonnull Random rand, int chunkX, int chunkZ, @Nonnull Biome biome) {
        ChunkPos chunkpos = this.getStartPositionForPosition(generator, rand, chunkX, chunkZ, 0, 0);
        if (chunkX == chunkpos.x && chunkZ == chunkpos.z) {
            if (!generator.hasStructure(biome, this)) {
                return false;
            } else {
                return StructureHelper.getYPosForStructure(chunkX, chunkZ, generator, null) > 55;
            }
        } else {
            return false;
        }
    }

    @Override
    @Nonnull
    public String getStructureName() {
        return String.valueOf(PyramidPieces.PYRAMID);
    }

    @Override
    @Nonnull
    public IStartFactory getStartFactory() {
        return Start::new;
    }

    @Override
    public int getSize() {
        return 4;
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

        public Start(Structure<?> structure, int chunkPosX, int chunkPosZ, MutableBoundingBox box, int references, long seed) {
            super(structure, chunkPosX, chunkPosZ, box, references, seed);
        }

        @Override
        public void init(@Nonnull ChunkGenerator<?> generator, @Nonnull TemplateManager manager, int chunkX, int chunkZ, @Nonnull Biome biome) {
            Rotation rotation = Rotation.values()[this.rand.nextInt(Rotation.values().length)];
            int y = StructureHelper.getYPosForStructure(chunkX, chunkZ, generator, rotation);

            if (y > 55) {
                int yChance = MathHelper.nextInt(this.rand, 7, 14);
                BlockPos pos = new BlockPos(chunkX * 16 + 8, y - yChance, chunkZ * 16 + 8);
                List<StructurePiece> components = PyramidPieces.getComponents(manager, pos, rotation);
                this.components.addAll(components);
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

                        for (StructurePiece component : this.components) {
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
    }
}