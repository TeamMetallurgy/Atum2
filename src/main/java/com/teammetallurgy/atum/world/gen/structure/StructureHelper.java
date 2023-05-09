package com.teammetallurgy.atum.world.gen.structure;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.Structure;

public class StructureHelper {

    public static boolean doesChunkHaveStructure(ServerLevel serverLevel, BlockPos pos, ResourceKey<Structure> structureResourceKey) {
        StructureManager structureManager = serverLevel.structureManager();
        Structure structure = structureManager.registryAccess().registryOrThrow(Registries.STRUCTURE).get(structureResourceKey);
        return structure != null && structureManager.getStructureAt(pos, structure).isValid();
    }

    public static Direction getDirectionFromRotation(Rotation rotation) {
        return switch (rotation) {
            case CLOCKWISE_90 -> Direction.WEST;
            case CLOCKWISE_180 -> Direction.NORTH;
            case COUNTERCLOCKWISE_90 -> Direction.EAST;
            default -> Direction.SOUTH;
        };
    }
}