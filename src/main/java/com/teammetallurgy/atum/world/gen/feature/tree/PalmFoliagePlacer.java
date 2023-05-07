package com.teammetallurgy.atum.world.gen.feature.tree;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teammetallurgy.atum.blocks.vegetation.DateBlock;
import com.teammetallurgy.atum.init.AtumBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;

import javax.annotation.Nonnull;

public class PalmFoliagePlacer extends FoliagePlacer {
    public static final Codec<PalmFoliagePlacer> CODEC = RecordCodecBuilder.create((instance) -> instance.group(Codec.FLOAT.fieldOf("date_chance").orElse(0.0F).forGetter((fp) -> fp.dateChance))
            .apply(instance, PalmFoliagePlacer::new));
    private final float dateChance;

    public PalmFoliagePlacer(float dateChance) {
        super(ConstantInt.of(0), ConstantInt.of(0));
        this.dateChance = dateChance;
    }

    @Override
    @Nonnull
    protected FoliagePlacerType<?> type() {
        return TreePlacerTypes.PALM_FOLIAGE.get();
    }

    @Override
    protected void createFoliage(@Nonnull LevelSimulatedReader genReader, @Nonnull FoliageSetter foliageSetter, @Nonnull RandomSource rand, @Nonnull TreeConfiguration config, int i, @Nonnull FoliageAttachment foliage, int i1, int i2, int i3) {
        BlockPos leafPos = foliage.pos();
        this.generateLeaf(genReader, foliageSetter, leafPos.above(), rand, config);
        for (BlockPos baseLeafPos : BlockPos.MutableBlockPos.betweenClosed(leafPos.offset(-1, 0, -1), leafPos.offset(1, 0, 1))) {
            this.generateLeaf(genReader, foliageSetter, baseLeafPos, rand, config);
        }
        this.generateLeaf(genReader, foliageSetter, leafPos.offset(2, 0, 0), rand, config);
        this.generateLeaf(genReader, foliageSetter, leafPos.offset(-2, 0, 0), rand, config);
        this.generateLeaf(genReader, foliageSetter, leafPos.offset(0, 0, 2), rand, config);
        this.generateLeaf(genReader, foliageSetter, leafPos.offset(0, 0, -2), rand, config);
        this.generateLeaf(genReader, foliageSetter, leafPos.offset(0, -1, -2), rand, config);
        this.generateLeaf(genReader, foliageSetter, leafPos.offset(0, -1, 2), rand, config);
        this.generateLeaf(genReader, foliageSetter, leafPos.offset(2, -1, 0), rand, config);
        this.generateLeaf(genReader, foliageSetter, leafPos.offset(-2, -1, 0), rand, config);
        this.generateLeaf(genReader, foliageSetter, leafPos.offset(0, -1, -3), rand, config);
        this.generateLeaf(genReader, foliageSetter, leafPos.offset(0, -1, 3), rand, config);
        this.generateLeaf(genReader, foliageSetter, leafPos.offset(3, -1, 0), rand, config);
        this.generateLeaf(genReader, foliageSetter, leafPos.offset(-3, -1, 0), rand, config);

        if (this.dateChance > 0.0F) {
            BlockPos datePos = leafPos.below().relative(Direction.Plane.HORIZONTAL.getRandomDirection(rand));
            if (rand.nextDouble() <= this.dateChance) {
                foliageSetter.set(datePos, AtumBlocks.DATE_BLOCK.get().defaultBlockState().setValue(DateBlock.AGE, Mth.nextInt(rand, 0, 7)));
                if (rand.nextDouble() <= 0.25F) { //Chance for 2nd date
                    datePos = leafPos.below().relative(Direction.Plane.HORIZONTAL.getRandomDirection(rand));
                    foliageSetter.set(datePos, AtumBlocks.DATE_BLOCK.get().defaultBlockState().setValue(DateBlock.AGE, Mth.nextInt(rand, 0, 7)));
                }
            }
        }
    }

    @Override
    public int foliageHeight(@Nonnull RandomSource random, int i, @Nonnull TreeConfiguration treeConfiguration) {
        return 0;
    }

    @Override
    protected boolean shouldSkipLocation(@Nonnull RandomSource random, int i1, int i2, int i3, int i4, boolean b) {
        return false;
    }

    private void generateLeaf(LevelSimulatedReader seedReader, FoliageSetter foliageSetter, BlockPos pos, RandomSource rand, TreeConfiguration config) {
        if (TreeFeature.isAirOrLeaves(seedReader, pos)) {
            foliageSetter.set(pos, config.foliageProvider.getState(rand, pos));
        }
    }
}