/*package com.teammetallurgy.atum.world.gen.feature.tree;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teammetallurgy.atum.init.AtumBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelSimulatedRW;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.LevelWriter;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;

public class PalmTrunkPlacer extends TrunkPlacer { //Based on StraightTrunkPlacer
    public static final Codec<PalmTrunkPlacer> CODEC = RecordCodecBuilder.create((instance) -> {
        return instance.group(Codec.intRange(0, 32).fieldOf("base_height").forGetter((tp) -> {
            return tp.baseHeight;
        }), Codec.intRange(0, 24).fieldOf("height_rand_a").forGetter((tp) -> {
            return tp.heightRandA;
        }), Codec.intRange(0, 24).fieldOf("height_rand_b").forGetter((tp) -> {
            return tp.heightRandB;
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
    protected TrunkPlacerType<?> type() {
        return TreePlacerTypes.PALM_Trunk;
    }

    @Override
    @Nonnull
    public List<FoliagePlacer.FoliageAttachment> placeTrunk(@Nonnull LevelSimulatedReader genReader, BiConsumer<BlockPos, @Nonnull BlockState> biConsumer, @Nonnull RandomSource rand, int amount, @Nonnull BlockPos pos, @Nonnull TreeConfiguration config) {
        for (int i = 0; i < amount; ++i) {
            BlockPos placePos = pos.above(i);
            placeLogWithOphidian(genReader, rand, placePos, logs, box, config);
        }

        return ImmutableList.of(new FoliagePlacer.FoliageAttachment(pos.above(amount), 0, false));
    }

    protected boolean placeLogWithOphidian(LevelSimulatedReader genReader, RandomSource rand, BlockPos pos, Set<BlockPos> logs, BoundingBox box, TreeConfiguration config) {
        if (TreeFeature.validTreePos(genReader, pos)) {
            setBlock(genReader, pos, config.trunkProvider.getState(rand, pos), box);
            logs.add(pos.immutable());

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

    private void addOphidianTongueToTree(LevelSimulatedRW seedReader, BlockPos pos, BooleanProperty booleanProperty, Set<BlockPos> positions, BoundingBox mutableBox) {
        this.generateOphidianTongue(seedReader, pos, booleanProperty, positions, mutableBox);
        int i = 4;
        for (pos = pos.below(); isAirAt(seedReader, pos) && i > 0; --i) {
            this.generateOphidianTongue(seedReader, pos, booleanProperty, positions, mutableBox);
            pos = pos.below();
        }
    }

    protected void generateOphidianTongue(LevelWriter level, BlockPos pos, BooleanProperty booleanProperty, Set<BlockPos> positions, BoundingBox mutableBox) {
        this.setOphidianTongue(level, pos, AtumBlocks.OPHIDIAN_TONGUE.get().defaultBlockState().setValue(booleanProperty, true), positions, mutableBox);
    }

    protected void setOphidianTongue(LevelWriter worldWriter, BlockPos pos, BlockState state, Set<BlockPos> positions, BoundingBox mutableBox) {
        worldWriter.setBlock(pos, state, 19);
        positions.add(pos);
        mutableBox.expand(new BoundingBox(pos, pos));
    }

    public static boolean isAirAt(LevelSimulatedReader reader, BlockPos pos) {
        return reader.isStateAtPosition(pos, (ab) -> ab.isAir());
    }
}*/