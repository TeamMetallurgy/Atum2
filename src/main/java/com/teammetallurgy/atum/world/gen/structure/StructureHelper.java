package com.teammetallurgy.atum.world.gen.structure;

public class StructureHelper {

    /*public static boolean doesChunkHaveStructure(ServerLevel serverLevel, BlockPos pos, ConfiguredStructureFeaturr<?, ?> structure) {
        return serverLevel.structureFeatureManager().startsForFeature(SectionPos.of(pos), structure).stream().findAny().isPresent();
    }*/

    /*public static int getYPosForStructure(PieceGeneratorSupplier.Context<NoneFeatureConfiguration> context, @Nullable Rotation rotation) {
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

        int k = context.chunkPos().getBlockX(7);
        int l = context.chunkPos().getBlockZ(7);
        int[] aint = context.getCornerHeights(k, x, l, z);
        return Math.min(Math.min(aint[0], aint[1]), Math.min(aint[2], aint[3]));
    }

    public static Direction getDirectionFromRotation(Rotation rotation) {
        return switch (rotation) {
            case CLOCKWISE_90 -> Direction.WEST;
            case CLOCKWISE_180 -> Direction.NORTH;
            case COUNTERCLOCKWISE_90 -> Direction.EAST;
            default -> Direction.SOUTH;
        };
    }*/
}