package com.teammetallurgy.atum.world.gen.feature;

import com.mojang.serialization.Codec;
import com.teammetallurgy.atum.init.AtumBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import javax.annotation.Nonnull;

public class AnputsFingersFeature extends Feature<NoneFeatureConfiguration> {

    public AnputsFingersFeature(Codec<NoneFeatureConfiguration> config) {
        super(config);
    }

    @Override
    public boolean place(@Nonnull FeaturePlaceContext<NoneFeatureConfiguration> placeContext) {
        WorldGenLevel genLevel = placeContext.level();
        BlockPos pos = placeContext.origin();
        BlockState state = AtumBlocks.ANPUTS_FINGERS.get().defaultBlockState();
        BlockPos placePos = genLevel.getHeightmapPos(Heightmap.Types.WORLD_SURFACE_WG, pos);

        boolean isNextToDeadwood = false;
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            if (genLevel.getBlockState(placePos.relative(direction)).getBlock() == AtumBlocks.DEADWOOD_LOG.get()) {
                isNextToDeadwood = true;
            }
        }

        if (isNextToDeadwood) {
            BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
            mutablePos.set(placePos);
            if (genLevel.isEmptyBlock(mutablePos) && genLevel.getBlockState(mutablePos.below()).getBlock() == AtumBlocks.STRANGE_SAND.get()) {
                genLevel.setBlock(mutablePos, state, 2);
                return true;
            }
        }
        return false;
    }
}