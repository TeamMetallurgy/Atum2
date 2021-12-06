package com.teammetallurgy.atum.world.gen.feature;

import com.mojang.serialization.Codec;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumLootTables;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BonusCrateFeature extends Feature<NoneFeatureConfiguration> {

    public BonusCrateFeature(Codec<NoneFeatureConfiguration> config) {
        super(config);
    }

    @Override
    public boolean place(@Nonnull WorldGenLevel seedReader, @Nonnull ChunkGenerator generator, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull NoneFeatureConfiguration config) {
        ChunkPos chunkpos = new ChunkPos(pos);
        List<Integer> xPositions = IntStream.rangeClosed(chunkpos.getMinBlockX(), chunkpos.getMaxBlockX()).boxed().collect(Collectors.toList());
        Collections.shuffle(xPositions, rand);
        List<Integer> zPositions = IntStream.rangeClosed(chunkpos.getMinBlockZ(), chunkpos.getMaxBlockZ()).boxed().collect(Collectors.toList());
        Collections.shuffle(zPositions, rand);
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

        for (Integer x : xPositions) {
            for (Integer z : zPositions) {
                mutablePos.set(x, 0, z);
                BlockPos posHeight = seedReader.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, mutablePos);
                if (seedReader.isEmptyBlock(posHeight) || seedReader.getBlockState(posHeight).getCollisionShape(seedReader, posHeight).isEmpty()) {
                    seedReader.setBlock(posHeight, AtumBlocks.DEADWOOD_CRATE.defaultBlockState(), 2);
                    RandomizableContainerBlockEntity.setLootTable(seedReader, rand, posHeight, AtumLootTables.CRATE_BONUS);
                    BlockState torch = AtumBlocks.DEADWOOD_TORCH.defaultBlockState();

                    for (Direction horizontal : Direction.Plane.HORIZONTAL) {
                        BlockPos neighborPos = posHeight.relative(horizontal);
                        if (torch.canSurvive(seedReader, neighborPos)) {
                            seedReader.setBlock(neighborPos, torch, 2);
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }
}