package com.teammetallurgy.atum.world.gen.feature;

import com.mojang.serialization.Codec;
import com.teammetallurgy.atum.init.AtumBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.BlockClusterFeatureConfig;
import net.minecraft.world.gen.feature.RandomPatchFeature;

import javax.annotation.Nonnull;
import java.util.Random;

public class AnputsFingersFeature extends RandomPatchFeature {

    public AnputsFingersFeature(Codec<BlockClusterFeatureConfig> config) {
        super(config);
    }

    @Override
    public boolean generate(@Nonnull ISeedReader seedReader, @Nonnull ChunkGenerator generator, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull BlockClusterFeatureConfig config) {
        BlockState state = config.stateProvider.getBlockState(rand, pos);
        BlockPos placePos = seedReader.getHeight(Heightmap.Type.WORLD_SURFACE_WG, pos);

        boolean isNextToDeadwood = false;
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            if (seedReader.getBlockState(placePos.offset(direction)).getBlock() == AtumBlocks.DEADWOOD_LOG) {
                isNextToDeadwood = true;
            }
        }

        if (isNextToDeadwood) {
            BlockPos.Mutable mutablePos = new BlockPos.Mutable();
            mutablePos.setPos(placePos);
            if (seedReader.isAirBlock(mutablePos) && seedReader.getBlockState(mutablePos.down()).getBlock() == AtumBlocks.SAND) {
                config.blockPlacer.place(seedReader, mutablePos, state, rand);
                return true;
            }
        }
        return false;
    }
}