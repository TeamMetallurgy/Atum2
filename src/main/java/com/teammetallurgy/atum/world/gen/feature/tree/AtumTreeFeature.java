/*
package com.teammetallurgy.atum.world.gen.feature.tree;

import com.mojang.serialization.Codec;
import com.teammetallurgy.atum.init.AtumBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelSimulatedRW;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.OptionalInt;
import java.util.Random;
import java.util.Set;

public class AtumTreeFeature extends TreeFeature { //TODO

    public AtumTreeFeature(Codec<TreeConfiguration> config) {
        super(config);
    }

    @Override
    protected boolean doPlace(@Nonnull LevelSimulatedRW genReader, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull Set<BlockPos> logs, @Nonnull Set<BlockPos> leaves, @Nonnull BoundingBox box, TreeConfiguration config) { //Coped from TreeFeature, to add more soil support
        int trunk = config.trunkPlacer.getTreeHeight(rand);
        int foliage = config.foliagePlacer.foliageHeight(rand, trunk, config);
        int k = trunk - foliage;
        int l = config.foliagePlacer.foliageRadius(rand, k);
        BlockPos blockpos;
        if (!config.fromSapling) {
            int i = genReader.getHeightmapPos(Heightmap.Types.OCEAN_FLOOR, pos).getY();
            int j = genReader.getHeightmapPos(Heightmap.Types.WORLD_SURFACE, pos).getY();
            if (j - i > config.maxWaterDepth) {
                return false;
            }

            int k1;
            if (config.heightmap == Heightmap.Types.OCEAN_FLOOR) {
                k1 = i;
            } else if (config.heightmap == Heightmap.Types.WORLD_SURFACE) {
                k1 = j;
            } else {
                k1 = genReader.getHeightmapPos(config.heightmap, pos).getY();
            }

            blockpos = new BlockPos(pos.getX(), k1, pos.getZ());
        } else {
            blockpos = pos;
        }

        if (blockpos.getY() >= 1 && blockpos.getY() + trunk + 1 <= 256) {
            if (!isSoilOrFarm(genReader, blockpos.below())) {
                return false;
            } else {
                OptionalInt optionalInt = config.minimumSize.minClippedHeight();
                int l1 = this.getMaxFreeTreeHeight(genReader, trunk, blockpos, config);
                if (l1 >= trunk || optionalInt.isPresent() && l1 >= optionalInt.getAsInt()) {
                    List<FoliagePlacer.FoliageAttachment> list = config.trunkPlacer.placeTrunk(genReader, rand, l1, blockpos, logs, box, config);
                    list.forEach((p_236407_8_) -> {
                        config.foliagePlacer.createFoliage(genReader, rand, config, l1, p_236407_8_, foliage, l, leaves, box);
                    });
                    return true;
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }
    }

    protected static boolean isSoilOrFarm(LevelSimulatedReader reader, @Nonnull BlockPos pos) {
        return isDirtOrFarmlandAt(reader, pos) || reader.isStateAtPosition(pos, (state -> state.getBlock() == AtumBlocks.LIMESTONE_GRAVEL)) || reader.isStateAtPosition(pos, (state -> state.getBlock() == AtumBlocks.FERTILE_SOIL));
    }

    private static boolean isDirtOrFarmlandAt(LevelSimulatedReader reader, BlockPos pos) {
        return reader.isStateAtPosition(pos, (state) -> {
            Block block = state.getBlock();
            return isDirt(block) || block instanceof FarmBlock;
        });
    }
}*/
