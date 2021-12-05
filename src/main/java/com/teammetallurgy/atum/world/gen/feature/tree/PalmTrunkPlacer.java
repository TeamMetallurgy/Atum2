package com.teammetallurgy.atum.world.gen.feature.tree;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teammetallurgy.atum.init.AtumBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.VineBlock;
import net.minecraft.state.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorldWriter;
import net.minecraft.world.gen.IWorldGenerationBaseReader;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.TreeFeature;
import net.minecraft.world.gen.foliageplacer.FoliagePlacer;
import net.minecraft.world.gen.trunkplacer.AbstractTrunkPlacer;
import net.minecraft.world.gen.trunkplacer.TrunkPlacerType;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class PalmTrunkPlacer extends AbstractTrunkPlacer { //Based on StraightTrunkPlacer
    public static final Codec<PalmTrunkPlacer> CODEC = RecordCodecBuilder.create((instance) -> {
        return instance.group(Codec.intRange(0, 32).fieldOf("base_height").forGetter((tp) -> {
            return tp.field_236906_d_;
        }), Codec.intRange(0, 24).fieldOf("height_rand_a").forGetter((tp) -> {
            return tp.field_236907_e_;
        }), Codec.intRange(0, 24).fieldOf("height_rand_b").forGetter((tp) -> {
            return tp.field_236908_f_;
        }), Codec.FLOAT.fieldOf("ophidian_tongue_chance").forGetter((tp) -> {
            return tp.ophidianTongueChance;
        })).apply(instance, PalmTrunkPlacer::new);
    });
    private final float ophidianTongueChance;

    public PalmTrunkPlacer(int baseHeight, int heightRandA, int heightRandB, float ophidianTongueChance) {
        super(baseHeight, heightRandA, heightRandB);
        this.ophidianTongueChance = ophidianTongueChance;
    }

    @Override
    @Nonnull
    protected TrunkPlacerType<?> func_230381_a_() {
        return TreePlacerTypes.PALM_Trunk;
    }

    @Override
    @Nonnull
    public List<FoliagePlacer.Foliage> func_230382_a_(@Nonnull IWorldGenerationReader genReader, @Nonnull Random rand, int amount, @Nonnull BlockPos pos, @Nonnull Set<BlockPos> logs, @Nonnull MutableBoundingBox box, @Nonnull BaseTreeFeatureConfig config) {
        for (int i = 0; i < amount; ++i) {
            BlockPos placePos = pos.up(i);
            placeLogWithOphidian(genReader, rand, placePos, logs, box, config);
        }

        return ImmutableList.of(new FoliagePlacer.Foliage(pos.up(amount), 0, false));
    }

    protected boolean placeLogWithOphidian(IWorldGenerationReader genReader, Random rand, BlockPos pos, Set<BlockPos> logs, MutableBoundingBox box, BaseTreeFeatureConfig config) {
        if (TreeFeature.isReplaceableAt(genReader, pos)) {
            func_236913_a_(genReader, pos, config.trunkProvider.getBlockState(rand, pos), box);
            logs.add(pos.toImmutable());

            if (this.ophidianTongueChance > 0.0F && rand.nextDouble() <= this.ophidianTongueChance) { //It's working, but a little wonky. Works out okay-ish in-game
                Set<BlockPos> set = Sets.newHashSet();
                BlockPos genPos;
                if (rand.nextInt(8) == 0) {
                    genPos = pos.west();
                    if (isAirAt(genReader, genPos)) {
                        this.addOphidianTongueToTree(genReader, genPos, VineBlock.EAST, set, box);
                    }
                }
                if (rand.nextInt(8) == 0) {
                    genPos = pos.east();
                    if (isAirAt(genReader, genPos)) {
                        this.addOphidianTongueToTree(genReader, genPos, VineBlock.WEST, set, box);
                    }
                }
                if (rand.nextInt(8) == 0) {
                    genPos = pos.north();
                    if (isAirAt(genReader, genPos)) {
                        this.addOphidianTongueToTree(genReader, genPos, VineBlock.SOUTH, set, box);
                    }
                }
                if (rand.nextInt(8) == 0) {
                    genPos = pos.south();
                    if (isAirAt(genReader, genPos)) {
                        this.addOphidianTongueToTree(genReader, genPos, VineBlock.NORTH, set, box);
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }

    private void addOphidianTongueToTree(IWorldGenerationReader seedReader, BlockPos pos, BooleanProperty booleanProperty, Set<BlockPos> positions, MutableBoundingBox mutableBox) {
        this.generateOphidianTongue(seedReader, pos, booleanProperty, positions, mutableBox);
        int i = 4;
        for (pos = pos.down(); isAirAt(seedReader, pos) && i > 0; --i) {
            this.generateOphidianTongue(seedReader, pos, booleanProperty, positions, mutableBox);
            pos = pos.down();
        }
    }

    protected void generateOphidianTongue(IWorldWriter world, BlockPos pos, BooleanProperty booleanProperty, Set<BlockPos> positions, MutableBoundingBox mutableBox) {
        this.setOphidianTongue(world, pos, AtumBlocks.OPHIDIAN_TONGUE.getDefaultState().with(booleanProperty, true), positions, mutableBox);
    }

    protected void setOphidianTongue(IWorldWriter worldWriter, BlockPos pos, BlockState state, Set<BlockPos> positions, MutableBoundingBox mutableBox) {
        worldWriter.setBlockState(pos, state, 19);
        positions.add(pos);
        mutableBox.expandTo(new MutableBoundingBox(pos, pos));
    }

    public static boolean isAirAt(IWorldGenerationBaseReader reader, BlockPos pos) {
        return reader.hasBlockState(pos, (ab) -> ab.isAir());
    }
}