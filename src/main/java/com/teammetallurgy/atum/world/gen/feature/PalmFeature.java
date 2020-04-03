package com.teammetallurgy.atum.world.gen.feature;

import com.mojang.datafixers.Dynamic;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.world.gen.feature.config.PalmConfig;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.DefaultBiomeFeatures;
import net.minecraft.world.gen.IWorldGenerationBaseReader;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.feature.AbstractSmallTreeFeature;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;

public class PalmFeature extends AbstractSmallTreeFeature<PalmConfig> { //Based on Acacia tree

    public PalmFeature(Function<Dynamic<?>, ? extends PalmConfig> dynamicFunction) {
        super(dynamicFunction);
    }

    @Override
    protected boolean func_225557_a_(@Nonnull IWorldGenerationReader genReader, Random rand, @Nonnull BlockPos pos, @Nonnull Set<BlockPos> logs, @Nonnull Set<BlockPos> leaves, @Nonnull MutableBoundingBox mutableBox, PalmConfig config) {
        int baseHeight = config.baseHeight + rand.nextInt(config.heightRandA + 1) + rand.nextInt(config.heightRandB + 1);
        int trunkHeight = config.trunkHeight >= 0 ? config.trunkHeight + rand.nextInt(config.trunkHeightRandom + 1) : baseHeight - (config.foliageHeight + rand.nextInt(config.foliageHeightRandom + 1));
        int foliagePlacer = config.foliagePlacer.func_225573_a_(rand, trunkHeight, baseHeight, config);
        Optional<BlockPos> optional = this.func_227212_a_(genReader, baseHeight, trunkHeight, foliagePlacer, pos, config);
        if (!optional.isPresent()) {
            return false;
        } else {
            BlockPos posOptional = optional.get();
            this.setFertileSoilAt(genReader, posOptional.down(), posOptional);
            int height = baseHeight - rand.nextInt(4) - 1;
            int i1 = 3 - rand.nextInt(3);
            BlockPos.Mutable mutablePos = new BlockPos.Mutable();
            int x = posOptional.getX();
            int z = posOptional.getZ();
            int y = 0;

            for (int h = 0; h < baseHeight; ++h) {
                int treeHeight = posOptional.getY() + h;
                if (h >= height && i1 > 0) {
                    --i1;
                }
                if (this.func_227216_a_(genReader, rand, mutablePos.setPos(x, treeHeight, z), logs, mutableBox, config)) {
                    y = treeHeight;
                }
            }

            BlockPos foliagePos = new BlockPos(x, y, z);
            config.foliagePlacer.func_225571_a_(genReader, rand, config, baseHeight, trunkHeight, foliagePlacer + 1, foliagePos, leaves);
            return true;
        }
    }

    protected void setFertileSoilAt(IWorldGenerationReader reader, BlockPos pos, BlockPos origin) {
        if (!(reader instanceof IWorld)) {
            this.setFertileSoil(reader, pos);
            return;
        }
        ((IWorld) reader).getBlockState(pos).onPlantGrow((IWorld) reader, pos, origin);
    }

    protected void setFertileSoil(IWorldGenerationReader genReader, BlockPos pos) {
        if (!(isFertileSoil(genReader, pos))) {
            this.setBlockState(genReader, pos, AtumBlocks.FERTILE_SOIL.getDefaultState());
        }
    }

    protected static boolean isFertileSoil(IWorldGenerationBaseReader genReader, BlockPos pos) {
        return genReader.hasBlockState(pos, (p) -> p.getBlock() != AtumBlocks.FERTILE_SOIL);
    }
}