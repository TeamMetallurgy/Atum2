package com.teammetallurgy.atum.world.gen.structure;

import com.teammetallurgy.atum.init.AtumBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;

import javax.annotation.Nonnull;

public abstract class AtumStructure extends Structure {

    protected AtumStructure(StructureSettings settings) {
        super(settings);
    }

    @Override
    public void afterPlace(WorldGenLevel genLevel, @Nonnull StructureManager manager, @Nonnull ChunkGenerator chunkGenerator, @Nonnull RandomSource random, BoundingBox boundingBox, @Nonnull ChunkPos chunkPos, PiecesContainer piecesContainer) {
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
        int minBuild = genLevel.getMinBuildHeight();
        BoundingBox calculateBoundingBox = piecesContainer.calculateBoundingBox();
        int calculateBoundingBoxminY = calculateBoundingBox.minY();

        for (int x = boundingBox.minX(); x <= boundingBox.maxX(); ++x) {
            for (int z = boundingBox.minZ(); z <= boundingBox.maxZ(); ++z) {
                mutablePos.set(x, calculateBoundingBoxminY, z);
                if (!genLevel.isEmptyBlock(mutablePos) && calculateBoundingBox.isInside(mutablePos) && piecesContainer.isInsidePiece(mutablePos)) {
                    for (int i1 = calculateBoundingBoxminY - 1; i1 > minBuild; --i1) {
                        mutablePos.setY(i1);
                        if (!genLevel.isEmptyBlock(mutablePos) && !genLevel.getBlockState(mutablePos).getMaterial().isLiquid()) {
                            break;
                        }
                        setBelowStructureBlock(genLevel, mutablePos, random); //Same as vanilla, but use Limestone instead
                    }
                }
            }
        }
    }

    public void setBelowStructureBlock(WorldGenLevel genLevel, BlockPos.MutableBlockPos mutablePos, RandomSource random) {
        genLevel.setBlock(mutablePos, AtumBlocks.LIMESTONE.get().defaultBlockState(), 2); //Same as vanilla, but use Limestone instead
    }
}