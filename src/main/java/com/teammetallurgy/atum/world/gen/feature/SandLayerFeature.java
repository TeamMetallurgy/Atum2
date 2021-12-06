package com.teammetallurgy.atum.world.gen.feature;

import com.mojang.serialization.Codec;
import com.teammetallurgy.atum.blocks.SandLayersBlock;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.world.DimensionHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import javax.annotation.Nonnull;
import java.util.Random;

public class SandLayerFeature extends Feature<NoneFeatureConfiguration> {

    public SandLayerFeature(Codec<NoneFeatureConfiguration> config) {
        super(config);
    }

    @Override
    public boolean place(@Nonnull WorldGenLevel seedReader, @Nonnull ChunkGenerator chunkGenerator, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull NoneFeatureConfiguration config) {
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

        for (int i = 0; i < 16; ++i) {
            for (int j = 0; j < 16; ++j) {
                int x = pos.getX() + i;
                int z = pos.getZ() + j;
                int y = seedReader.getHeight(Heightmap.Types.MOTION_BLOCKING, x, z);
                mutablePos.set(x, y, z);

                for (Direction facing : Direction.Plane.HORIZONTAL) {
                    BlockPos posOffset = mutablePos.relative(facing);
                    if (seedReader.getBlockState(posOffset).isFaceSturdy(seedReader, posOffset, Direction.UP) && DimensionHelper.canPlaceSandLayer(seedReader, mutablePos)) {
                        int layers = Mth.nextInt(rand, 1, 3);
                        seedReader.setBlock(mutablePos, AtumBlocks.SAND_LAYERED.defaultBlockState().setValue(SandLayersBlock.LAYERS, layers), 2);
                    }
                }
            }
        }
        return true;
    }
}