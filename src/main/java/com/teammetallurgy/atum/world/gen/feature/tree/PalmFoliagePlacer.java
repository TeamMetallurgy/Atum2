package com.teammetallurgy.atum.world.gen.feature.tree;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teammetallurgy.atum.blocks.vegetation.DateBlock;
import com.teammetallurgy.atum.init.AtumBlocks;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.FeatureSpread;
import net.minecraft.world.gen.feature.TreeFeature;
import net.minecraft.world.gen.foliageplacer.FoliagePlacer;
import net.minecraft.world.gen.foliageplacer.FoliagePlacerType;

import javax.annotation.Nonnull;
import java.util.Random;
import java.util.Set;

public class PalmFoliagePlacer extends FoliagePlacer {
    public static final Codec<PalmFoliagePlacer> CODEC = RecordCodecBuilder.create((instance) -> instance.group(Codec.FLOAT.fieldOf("date_chance").orElse(0.0F).forGetter((fp) -> fp.dateChance))
            .apply(instance, PalmFoliagePlacer::new));
    private final float dateChance;

    public PalmFoliagePlacer(float dateChance) {
        super(FeatureSpread.func_242252_a(0), FeatureSpread.func_242252_a(0));
        this.dateChance = dateChance;
    }

    @Override
    @Nonnull
    protected FoliagePlacerType<?> func_230371_a_() {
        return TreePlacerTypes.PALM_FOLIAGE;
    }

    @Override
    protected void func_230372_a_(@Nonnull IWorldGenerationReader genReader, @Nonnull Random rand, @Nonnull BaseTreeFeatureConfig config, int i, @Nonnull Foliage foliage, int i1, int i2, @Nonnull Set<BlockPos> positions, int i4, @Nonnull MutableBoundingBox box) {
        BlockPos leafPos = foliage.func_236763_a_();
        this.generateLeaf(genReader, leafPos.up(), rand, config);
        for (BlockPos baseLeafPos : BlockPos.Mutable.getAllInBoxMutable(leafPos.add(-1, 0, -1), leafPos.add(1, 0, 1))) {
            this.generateLeaf(genReader, baseLeafPos, rand, config);
        }
        this.generateLeaf(genReader, leafPos.add(2, 0, 0), rand, config);
        this.generateLeaf(genReader, leafPos.add(-2, 0, 0), rand, config);
        this.generateLeaf(genReader, leafPos.add(0, 0, 2), rand, config);
        this.generateLeaf(genReader, leafPos.add(0, 0, -2), rand, config);
        this.generateLeaf(genReader, leafPos.add(0, -1, -2), rand, config);
        this.generateLeaf(genReader, leafPos.add(0, -1, 2), rand, config);
        this.generateLeaf(genReader, leafPos.add(2, -1, 0), rand, config);
        this.generateLeaf(genReader, leafPos.add(-2, -1, 0), rand, config);
        this.generateLeaf(genReader, leafPos.add(0, -1, -3), rand, config);
        this.generateLeaf(genReader, leafPos.add(0, -1, 3), rand, config);
        this.generateLeaf(genReader, leafPos.add(3, -1, 0), rand, config);
        this.generateLeaf(genReader, leafPos.add(-3, -1, 0), rand, config);

        if (this.dateChance > 0.0D) {
            BlockPos datePos = leafPos.down().offset(Direction.Plane.HORIZONTAL.random(rand));
            if (rand.nextDouble() <= this.dateChance) {
                genReader.setBlockState(datePos, AtumBlocks.DATE_BLOCK.getDefaultState().with(DateBlock.AGE, MathHelper.nextInt(rand, 0, 7)), 2);
                if (rand.nextDouble() <= 0.25F) { //Chance for 2nd date
                    datePos = leafPos.down().offset(Direction.Plane.HORIZONTAL.random(rand));
                    genReader.setBlockState(datePos, AtumBlocks.DATE_BLOCK.getDefaultState().with(DateBlock.AGE, MathHelper.nextInt(rand, 0, 7)), 2);
                }
            }
        }
    }

    @Override
    public int func_230374_a_(@Nonnull Random rand, int i, @Nonnull BaseTreeFeatureConfig config) {
        return 0;
    }

    @Override
    protected boolean func_230373_a_(@Nonnull Random rand, int i1, int i2, int i3, int i4, boolean b) {
        return true;
    }

    private void generateLeaf(IWorldGenerationReader seedReader, BlockPos pos, Random rand, BaseTreeFeatureConfig config) {
        if (TreeFeature.isAirOrLeavesAt(seedReader, pos)) {
            seedReader.setBlockState(pos, config.leavesProvider.getBlockState(rand, pos), 19);
        }
    }
}