/*
package com.teammetallurgy.atum.world.gen.feature;

import com.mojang.serialization.Codec;
import com.teammetallurgy.atum.init.AtumBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.RandomPatchFeature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;

import javax.annotation.Nonnull;
import java.util.Random;

public class AnputsFingersFeature extends RandomPatchFeature { //TODO

    public AnputsFingersFeature(Codec<RandomPatchConfiguration> config) {
        super(config);
    }

    @Override
    public boolean place(@Nonnull WorldGenLevel seedReader, @Nonnull ChunkGenerator generator, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull RandomPatchConfiguration config) {
        BlockState state = config.stateProvider.getState(rand, pos);
        BlockPos placePos = seedReader.getHeightmapPos(Heightmap.Types.WORLD_SURFACE_WG, pos);

        boolean isNextToDeadwood = false;
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            if (seedReader.getBlockState(placePos.relative(direction)).getBlock() == AtumBlocks.DEADWOOD_LOG) {
                isNextToDeadwood = true;
            }
        }

        if (isNextToDeadwood) {
            BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
            mutablePos.set(placePos);
            if (seedReader.isEmptyBlock(mutablePos) && seedReader.getBlockState(mutablePos.below()).getBlock() == AtumBlocks.SAND) {
                config.blockPlacer.place(seedReader, mutablePos, state, rand);
                return true;
            }
        }
        return false;
    }
}*/
