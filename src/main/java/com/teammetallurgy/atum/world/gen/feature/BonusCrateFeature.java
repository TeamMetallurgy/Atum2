package com.teammetallurgy.atum.world.gen.feature;

import com.mojang.serialization.Codec;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumLootTables;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BonusCrateFeature extends Feature<NoFeatureConfig> {

    public BonusCrateFeature(Codec<NoFeatureConfig> config) {
        super(config);
    }

    @Override
    public boolean place(@Nonnull IWorld world, @Nonnull ChunkGenerator<? extends GenerationSettings> generator, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull NoFeatureConfig config) {
        ChunkPos chunkpos = new ChunkPos(pos);
        List<Integer> xPositions = IntStream.rangeClosed(chunkpos.getXStart(), chunkpos.getXEnd()).boxed().collect(Collectors.toList());
        Collections.shuffle(xPositions, rand);
        List<Integer> zPositions = IntStream.rangeClosed(chunkpos.getZStart(), chunkpos.getZEnd()).boxed().collect(Collectors.toList());
        Collections.shuffle(zPositions, rand);
        BlockPos.Mutable mutablePos = new BlockPos.Mutable();

        for (Integer x : xPositions) {
            for (Integer z : zPositions) {
                mutablePos.setPos(x, 0, z);
                BlockPos posHeight = world.getHeight(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, mutablePos);
                if (world.isAirBlock(posHeight) || world.getBlockState(posHeight).getCollisionShape(world, posHeight).isEmpty()) {
                    world.setBlockState(posHeight, AtumBlocks.DEADWOOD_CRATE.getDefaultState(), 2);
                    LockableLootTileEntity.setLootTable(world, rand, posHeight, AtumLootTables.CRATE_BONUS);
                    BlockState torch = AtumBlocks.DEADWOOD_TORCH.getDefaultState();

                    for (Direction horizontal : Direction.Plane.HORIZONTAL) {
                        BlockPos neighborPos = posHeight.offset(horizontal);
                        if (torch.isValidPosition(world, neighborPos)) {
                            world.setBlockState(neighborPos, torch, 2);
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }
}