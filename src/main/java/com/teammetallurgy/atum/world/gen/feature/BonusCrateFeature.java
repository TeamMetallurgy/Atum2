package com.teammetallurgy.atum.world.gen.feature;

import com.mojang.serialization.Codec;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumLootTables;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import javax.annotation.Nonnull;
import java.util.stream.IntStream;

public class BonusCrateFeature extends Feature<NoneFeatureConfiguration> { //TODO Water seems to generate on two of the sides of it?? Apparently vanilla bug - look into how to fix

    public BonusCrateFeature(Codec<NoneFeatureConfiguration> config) {
        super(config);
    }

    @Override
    public boolean place(@Nonnull FeaturePlaceContext<NoneFeatureConfiguration> placeContext) {
        WorldGenLevel genLevel = placeContext.level();
        RandomSource random = placeContext.random();
        ChunkPos chunkpos = new ChunkPos(placeContext.origin());
        IntArrayList xPositions = Util.toShuffledList(IntStream.rangeClosed(chunkpos.getMinBlockX(), chunkpos.getMaxBlockX()), random);
        IntArrayList zPositions = Util.toShuffledList(IntStream.rangeClosed(chunkpos.getMinBlockZ(), chunkpos.getMaxBlockZ()), random);
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

        for (Integer x : xPositions) {
            for (Integer z : zPositions) {
                mutablePos.set(x, 0, z);
                BlockPos posHeight = genLevel.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, mutablePos);
                if (genLevel.isEmptyBlock(posHeight) || genLevel.getBlockState(posHeight).getCollisionShape(genLevel, posHeight).isEmpty()) {
                    genLevel.setBlock(posHeight, AtumBlocks.DEADWOOD_CRATE.get().defaultBlockState(), 2);
                    RandomizableContainerBlockEntity.setBlockEntityLootTable(genLevel, random, posHeight, AtumLootTables.CRATE_BONUS);
                    BlockState torch = AtumBlocks.DEADWOOD_TORCH.get().defaultBlockState();

                    for (Direction horizontal : Direction.Plane.HORIZONTAL) {
                        BlockPos neighborPos = posHeight.relative(horizontal);
                        if (torch.canSurvive(genLevel, neighborPos)) {
                            genLevel.setBlock(neighborPos, torch, 2);
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }
}