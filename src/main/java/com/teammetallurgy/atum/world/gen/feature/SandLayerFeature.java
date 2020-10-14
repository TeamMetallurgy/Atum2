package com.teammetallurgy.atum.world.gen.feature;

import com.mojang.serialization.Codec;
import com.teammetallurgy.atum.blocks.SandLayersBlock;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.world.DimensionHelper;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

import javax.annotation.Nonnull;
import java.util.Random;

public class SandLayerFeature extends Feature<NoFeatureConfig> {

    public SandLayerFeature(Codec<NoFeatureConfig> config) {
        super(config);
    }

    @Override
    public boolean func_241855_a(@Nonnull ISeedReader seedReader, @Nonnull ChunkGenerator chunkGenerator, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull NoFeatureConfig config) {
        BlockPos.Mutable mutablePos = new BlockPos.Mutable();

        for (int i = 0; i < 16; ++i) {
            for (int j = 0; j < 16; ++j) {
                int x = pos.getX() + i;
                int z = pos.getZ() + j;
                int y = seedReader.getHeight(Heightmap.Type.MOTION_BLOCKING, x, z);
                mutablePos.setPos(x, y, z);

                if (DimensionHelper.canPlaceSandLayer(seedReader, pos)) {
                    for (Direction facing : Direction.Plane.HORIZONTAL) {
                        BlockPos posOffset = mutablePos.offset(facing);
                        if (seedReader.getBlockState(posOffset).isSolidSide(seedReader, posOffset, Direction.UP)) {
                            int layers = MathHelper.nextInt(rand, 1, 3);
                            seedReader.setBlockState(pos, AtumBlocks.SAND_LAYERED.getDefaultState().with(SandLayersBlock.LAYERS, layers), 2);
                        }
                    }
                }
            }
        }
        return true;
    }
}