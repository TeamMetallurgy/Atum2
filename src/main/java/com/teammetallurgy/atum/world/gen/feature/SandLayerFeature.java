package com.teammetallurgy.atum.world.gen.feature;

import com.mojang.serialization.Codec;
import com.teammetallurgy.atum.blocks.SandLayersBlock;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.world.DimensionHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import javax.annotation.Nonnull;

public class SandLayerFeature extends Feature<NoneFeatureConfiguration> {

    public SandLayerFeature(Codec<NoneFeatureConfiguration> config) {
        super(config);
    }

    @Override
    public boolean place(@Nonnull FeaturePlaceContext<NoneFeatureConfiguration> placeContext) {
        WorldGenLevel genLevel = placeContext.level();
        BlockPos pos = placeContext.origin();
        RandomSource random = placeContext.random();
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

        for (int i = 0; i < 16; ++i) {
            for (int j = 0; j < 16; ++j) {
                int x = pos.getX() + i;
                int z = pos.getZ() + j;
                int y = genLevel.getHeight(Heightmap.Types.MOTION_BLOCKING, x, z);
                mutablePos.set(x, y, z);

                for (Direction facing : Direction.Plane.HORIZONTAL) {
                    BlockPos posOffset = mutablePos.relative(facing);
                    if (genLevel.getBlockState(posOffset).isFaceSturdy(genLevel, posOffset, Direction.UP) && DimensionHelper.canPlaceSandLayer(genLevel, mutablePos)) {
                        int layers = Mth.nextInt(random, 1, 3);
                        genLevel.setBlock(mutablePos, AtumBlocks.STRANGE_SAND_LAYERED.get().defaultBlockState().setValue(SandLayersBlock.LAYERS, layers), 2);
                    }
                }
            }
        }
        return false;
    }
}