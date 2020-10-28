package com.teammetallurgy.atum.world.gen.feature.tree;

import com.mojang.serialization.Codec;
import com.teammetallurgy.atum.init.AtumBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.IWorldGenerationBaseReader;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.TreeFeature;
import net.minecraft.world.gen.foliageplacer.FoliagePlacer;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.OptionalInt;
import java.util.Random;
import java.util.Set;

public class AtumTreeFeature extends TreeFeature {

    public AtumTreeFeature(Codec<BaseTreeFeatureConfig> config) {
        super(config);
    }

    @Override
    protected boolean place(@Nonnull IWorldGenerationReader genReader, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull Set<BlockPos> logs, @Nonnull Set<BlockPos> leaves, @Nonnull MutableBoundingBox box, BaseTreeFeatureConfig config) { //Coped from TreeFeature, to add more soil support
        int trunk = config.trunkPlacer.func_236917_a_(rand);
        int foliage = config.foliagePlacer.func_230374_a_(rand, trunk, config);
        int k = trunk - foliage;
        int l = config.foliagePlacer.func_230376_a_(rand, k);
        BlockPos blockpos;
        if (!config.forcePlacement) {
            int i = genReader.getHeight(Heightmap.Type.OCEAN_FLOOR, pos).getY();
            int j = genReader.getHeight(Heightmap.Type.WORLD_SURFACE, pos).getY();
            if (j - i > config.maxWaterDepth) {
                return false;
            }

            int k1;
            if (config.field_236682_l_ == Heightmap.Type.OCEAN_FLOOR) {
                k1 = i;
            } else if (config.field_236682_l_ == Heightmap.Type.WORLD_SURFACE) {
                k1 = j;
            } else {
                k1 = genReader.getHeight(config.field_236682_l_, pos).getY();
            }

            blockpos = new BlockPos(pos.getX(), k1, pos.getZ());
        } else {
            blockpos = pos;
        }

        if (blockpos.getY() >= 1 && blockpos.getY() + trunk + 1 <= 256) {
            if (isSoilOrFarm(genReader, blockpos.down())) {
                return false;
            } else {
                OptionalInt optionalInt = config.minimumSize.func_236710_c_();
                int l1 = this.func_241521_a_(genReader, trunk, blockpos, config);
                if (l1 >= trunk || optionalInt.isPresent() && l1 >= optionalInt.getAsInt()) {
                    List<FoliagePlacer.Foliage> list = config.trunkPlacer.func_230382_a_(genReader, rand, l1, blockpos, logs, box, config);
                    list.forEach((p_236407_8_) -> {
                        config.foliagePlacer.func_236752_a_(genReader, rand, config, l1, p_236407_8_, foliage, l, leaves, box);
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

    protected static boolean isSoilOrFarm(IWorldGenerationBaseReader reader, @Nonnull BlockPos pos) {
        return isDirtOrFarmlandAt(reader, pos) || reader.hasBlockState(pos, (state -> state.getBlock() == AtumBlocks.LIMESTONE_GRAVEL));
    }

    private static boolean isDirtOrFarmlandAt(IWorldGenerationBaseReader reader, BlockPos pos) {
        return reader.hasBlockState(pos, (state) -> {
            Block block = state.getBlock();
            return isDirt(block) || block instanceof FarmlandBlock;
        });
    }
}