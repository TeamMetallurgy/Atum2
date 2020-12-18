package com.teammetallurgy.atum.world.gen.structure;

import net.minecraft.util.Direction;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.SectionPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.structure.Structure;

import javax.annotation.Nullable;
import java.util.Random;

public class StructureHelper {

    /*
     * Check if chunk have structure. Do not use during Structure generation (Works for normal worldgen)
     */
    public static boolean doesChunkHaveStructure(ISeedReader seedReader, BlockPos pos, Structure<?> structure) {
        return seedReader.func_241827_a(SectionPos.from(pos), structure).findAny().isPresent();
    }

    public static int getYPosForStructure(int chunkX, int chunkZ, ChunkGenerator generator, @Nullable Rotation rotation) {
        if (rotation == null) {
            Random rand = new Random();
            rotation = Rotation.randomRotation(rand);
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
        int i1 = generator.getNoiseHeightMinusOne(k, l, Heightmap.Type.WORLD_SURFACE_WG);
        int j1 = generator.getNoiseHeightMinusOne(k, l + z, Heightmap.Type.WORLD_SURFACE_WG);
        int k1 = generator.getNoiseHeightMinusOne(k + x, l, Heightmap.Type.WORLD_SURFACE_WG);
        int l1 = generator.getNoiseHeightMinusOne(k + x, l + z, Heightmap.Type.WORLD_SURFACE_WG);
        return Math.min(Math.min(i1, j1), Math.min(k1, l1));
    }

    public static Direction getDirectionFromRotation(Rotation rotation) {
        switch (rotation) {
            case CLOCKWISE_90:
                return Direction.WEST;
            case CLOCKWISE_180:
                return Direction.NORTH;
            case COUNTERCLOCKWISE_90:
                return Direction.EAST;
            default:
                return Direction.SOUTH;
        }
    }
}