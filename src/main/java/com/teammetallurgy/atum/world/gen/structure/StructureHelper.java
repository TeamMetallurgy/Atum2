package com.teammetallurgy.atum.world.gen.structure;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.longs.LongIterator;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.IStructureReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraftforge.registries.GameData;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class StructureHelper {

    public static boolean isStructureInChunk(IWorld world, int chunkX, int chunkZ, Feature<?> structure) { //TODO Test
        for (int dx = -2; dx <= 2; dx++) {
            for (int dz = -2; dz <= 2; dz++) {
                Registry<Structure<?>> structureMap = GameData.getStructureFeatures();
                ResourceLocation id = structure.getRegistryName();
                if (id != null && structureMap.containsKey(id)) {
                    Optional<Structure<?>> structureEntry = structureMap.getValue(id);
                    if (structureEntry.isPresent()) {
                        Structure<?> checkStructure = structureEntry.get();
                        ChunkPos chunkPos = new ChunkPos(chunkX, chunkZ);
                        StructureStart structureStart = getStart(world, chunkPos.asBlockPos(), true, checkStructure);
                        if (structureStart.getBoundingBox() != null && structureStart.getBoundingBox().intersectsWith(chunkPos.getXStart(), chunkPos.getZStart(), chunkPos.getXEnd(), chunkPos.getZEnd())) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public static StructureStart getStart(IWorld world, BlockPos pos, boolean b, Structure<?> structure) { //Copied from Structure
        label35:
        for (StructureStart start : getStarts(world, pos.getX() >> 4, pos.getZ() >> 4, structure)) {
            if (start.isValid() && start.getBoundingBox().isVecInside(pos)) {
                if (!b) {
                    return start;
                }
                Iterator<StructurePiece> pieceIterator = start.getComponents().iterator();

                while (true) {
                    if (!pieceIterator.hasNext()) {
                        continue label35;
                    }
                    StructurePiece piece = pieceIterator.next();
                    if (piece.getBoundingBox().isVecInside(pos)) {
                        break;
                    }
                }
                return start;
            }
        }
        return StructureStart.DUMMY;
    }

    public static List<StructureStart> getStarts(IWorld world, int x, int z, Structure<?> structure) { //Copied from Structure
        List<StructureStart> list = Lists.newArrayList();
        IChunk ichunk = world.getChunk(x, z, ChunkStatus.STRUCTURE_REFERENCES);
        LongIterator longiterator = ichunk.getStructureReferences(structure.getStructureName()).iterator();

        while(longiterator.hasNext()) {
            long i = longiterator.nextLong();
            IStructureReader istructurereader = world.getChunk(ChunkPos.getX(i), ChunkPos.getZ(i), ChunkStatus.STRUCTURE_STARTS);
            StructureStart structurestart = istructurereader.getStructureStart(structure.getStructureName());
            if (structurestart != null) {
                list.add(structurestart);
            }
        }
        return list;
    }

    public static int getYPosForStructure(int chunkX, int chunkZ, ChunkGenerator<?> generator, @Nullable Rotation rotation) {
        if (rotation == null) {
            Random rand = new Random();
            rotation = Rotation.values()[rand.nextInt(Rotation.values().length)];
        }
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
        return Math.min(Math.min(i1, j1), Math.min(k1, l1));
    }
}