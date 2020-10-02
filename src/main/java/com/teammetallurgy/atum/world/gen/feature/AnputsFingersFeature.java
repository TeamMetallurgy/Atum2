package com.teammetallurgy.atum.world.gen.feature;

import com.mojang.serialization.Codec;
import com.teammetallurgy.atum.init.AtumBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
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
    public boolean place(@Nonnull IWorld world, @Nonnull ChunkGenerator<? extends GenerationSettings> generator, @Nonnull Random rand, @Nonnull BlockPos pos, BlockClusterFeatureConfig config) {
        BlockState state = config.stateProvider.getBlockState(rand, pos);
        BlockPos placePos = world.getHeight(Heightmap.Type.WORLD_SURFACE_WG, pos);

        boolean isNextToDeadwood = false;
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            if (world.getBlockState(placePos.offset(direction)).getBlock() == AtumBlocks.DEADWOOD_LOG) {
                isNextToDeadwood = true;
            }
        }

        if (isNextToDeadwood) {
            BlockPos.Mutable mutablePos = new BlockPos.Mutable();
            mutablePos.setPos(placePos);
            if (world.isAirBlock(mutablePos) && world.getBlockState(mutablePos.down()).getBlock() == AtumBlocks.SAND) {
                config.blockPlacer.func_225567_a_(world, mutablePos, state, rand);
                return true;
            }
        }
        return false;
    }
}