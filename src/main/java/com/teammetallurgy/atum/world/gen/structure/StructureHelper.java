package com.teammetallurgy.atum.world.gen.structure;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.StructureFeature;

import javax.annotation.Nullable;
import java.util.Random;

public class StructureHelper {

    public static boolean doesChunkHaveStructure(WorldGenLevel seedReader, BlockPos pos, StructureFeature<?> structure) {
        return seedReader.startsForFeature(SectionPos.of(pos), structure).findAny().isPresent();
    }

    public static int getYPosForStructure(int chunkX, int chunkZ, ChunkGenerator generator, @Nullable Rotation rotation) {
        if (rotation == null) {
            Random rand = new Random();
            rotation = Rotation.getRandom(rand);
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
        int i1 = generator.getFirstOccupiedHeight(k, l, Heightmap.Types.WORLD_SURFACE_WG);
        int j1 = generator.getFirstOccupiedHeight(k, l + z, Heightmap.Types.WORLD_SURFACE_WG);
        int k1 = generator.getFirstOccupiedHeight(k + x, l, Heightmap.Types.WORLD_SURFACE_WG);
        int l1 = generator.getFirstOccupiedHeight(k + x, l + z, Heightmap.Types.WORLD_SURFACE_WG);
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