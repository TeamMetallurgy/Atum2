package com.teammetallurgy.atum.world.gen.structure;

import com.teammetallurgy.atum.init.AtumFeatures;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.palette.UpgradeData;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.structure.Structure;

import javax.annotation.Nullable;
import java.util.Random;

public class StructureHelper {

    public static boolean doesChunkHaveStructure(IWorld world, int chunkX, int chunkZ, Structure<?> structure) { //TODO
        return !world.getChunk(chunkX, chunkZ, ChunkStatus.STRUCTURE_REFERENCES).getStructureReferences(structure.getStructureName()).isEmpty();
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