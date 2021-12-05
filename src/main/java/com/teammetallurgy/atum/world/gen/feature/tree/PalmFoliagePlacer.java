package com.teammetallurgy.atum.world.gen.feature.tree;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teammetallurgy.atum.blocks.vegetation.DateBlock;
import com.teammetallurgy.atum.init.AtumBlocks;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.LevelSimulatedRW;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.util.UniformInt;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;

import javax.annotation.Nonnull;
import java.util.Random;
import java.util.Set;

import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer.FoliageAttachment;

public class PalmFoliagePlacer extends FoliagePlacer {
    public static final Codec<PalmFoliagePlacer> CODEC = RecordCodecBuilder.create((instance) -> instance.group(Codec.FLOAT.fieldOf("date_chance").orElse(0.0F).forGetter((fp) -> fp.dateChance))
            .apply(instance, PalmFoliagePlacer::new));
    private final float dateChance;

    public PalmFoliagePlacer(float dateChance) {
        super(UniformInt.fixed(0), UniformInt.fixed(0));
        this.dateChance = dateChance;
    }

    @Override
    @Nonnull
    protected FoliagePlacerType<?> type() {
        return TreePlacerTypes.PALM_FOLIAGE;
    }

    @Override
    protected void createFoliage(@Nonnull LevelSimulatedRW genReader, @Nonnull Random rand, @Nonnull TreeConfiguration config, int i, @Nonnull FoliageAttachment foliage, int i1, int i2, @Nonnull Set<BlockPos> positions, int i4, @Nonnull BoundingBox box) {
        BlockPos leafPos = foliage.foliagePos();
        this.generateLeaf(genReader, leafPos.above(), rand, config);
        for (BlockPos baseLeafPos : BlockPos.MutableBlockPos.betweenClosed(leafPos.offset(-1, 0, -1), leafPos.offset(1, 0, 1))) {
            this.generateLeaf(genReader, baseLeafPos, rand, config);
        }
        this.generateLeaf(genReader, leafPos.offset(2, 0, 0), rand, config);
        this.generateLeaf(genReader, leafPos.offset(-2, 0, 0), rand, config);
        this.generateLeaf(genReader, leafPos.offset(0, 0, 2), rand, config);
        this.generateLeaf(genReader, leafPos.offset(0, 0, -2), rand, config);
        this.generateLeaf(genReader, leafPos.offset(0, -1, -2), rand, config);
        this.generateLeaf(genReader, leafPos.offset(0, -1, 2), rand, config);
        this.generateLeaf(genReader, leafPos.offset(2, -1, 0), rand, config);
        this.generateLeaf(genReader, leafPos.offset(-2, -1, 0), rand, config);
        this.generateLeaf(genReader, leafPos.offset(0, -1, -3), rand, config);
        this.generateLeaf(genReader, leafPos.offset(0, -1, 3), rand, config);
        this.generateLeaf(genReader, leafPos.offset(3, -1, 0), rand, config);
        this.generateLeaf(genReader, leafPos.offset(-3, -1, 0), rand, config);

        if (this.dateChance > 0.0F) {
            BlockPos datePos = leafPos.below().relative(Direction.Plane.HORIZONTAL.getRandomDirection(rand));
            if (rand.nextDouble() <= this.dateChance) {
                genReader.setBlock(datePos, AtumBlocks.DATE_BLOCK.defaultBlockState().setValue(DateBlock.AGE, Mth.nextInt(rand, 0, 7)), 2);
                if (rand.nextDouble() <= 0.25F) { //Chance for 2nd date
                    datePos = leafPos.below().relative(Direction.Plane.HORIZONTAL.getRandomDirection(rand));
                    genReader.setBlock(datePos, AtumBlocks.DATE_BLOCK.defaultBlockState().setValue(DateBlock.AGE, Mth.nextInt(rand, 0, 7)), 2);
                }
            }
        }
    }

    @Override
    public int foliageHeight(@Nonnull Random rand, int i, @Nonnull TreeConfiguration config) {
        return 0;
    }

    @Override
    protected boolean shouldSkipLocation(@Nonnull Random rand, int i1, int i2, int i3, int i4, boolean b) {
        return true;
    }

    private void generateLeaf(LevelSimulatedRW seedReader, BlockPos pos, Random rand, TreeConfiguration config) {
        if (TreeFeature.isAirOrLeaves(seedReader, pos)) {
            seedReader.setBlock(pos, config.leavesProvider.getState(rand, pos), 19);
        }
    }
}