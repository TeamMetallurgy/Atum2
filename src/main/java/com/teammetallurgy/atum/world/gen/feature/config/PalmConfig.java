package com.teammetallurgy.atum.world.gen.feature.config;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.blockstateprovider.BlockStateProvider;
import net.minecraft.world.gen.feature.AbstractFeatureSizeType;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.foliageplacer.FoliagePlacer;
import net.minecraft.world.gen.treedecorator.TreeDecorator;
import net.minecraft.world.gen.trunkplacer.AbstractTrunkPlacer;

import javax.annotation.Nonnull;
import java.util.List;

public class PalmConfig extends BaseTreeFeatureConfig {
    public static final Codec<PalmConfig> PALM_CODEC = RecordCodecBuilder.create((palmConfigInstance) -> {
        return palmConfigInstance.group(BlockStateProvider.CODEC.fieldOf("trunk_provider").forGetter((c) -> {
            return c.trunkProvider;
        }), BlockStateProvider.CODEC.fieldOf("leaves_provider").forGetter((c) -> {
            return c.leavesProvider;
        }), FoliagePlacer.field_236749_d_.fieldOf("foliage_placer").forGetter((c) -> {
            return c.field_236677_f_;
        }), AbstractTrunkPlacer.field_236905_c_.fieldOf("trunk_placer").forGetter((c) -> {
            return c.field_236678_g_;
        }), AbstractFeatureSizeType.field_236704_a_.fieldOf("minimum_size").forGetter((c) -> {
            return c.field_236679_h_;
        }), TreeDecorator.field_236874_c_.listOf().fieldOf("decorators").forGetter((c) -> {
            return c.decorators;
        }), Codec.INT.fieldOf("max_water_depth").orElse(0).forGetter((c) -> {
            return c.field_236680_i_;
        }), Codec.BOOL.fieldOf("ignore_vines").orElse(false).forGetter((c) -> {
            return c.field_236681_j_;
        }), Heightmap.Type.field_236078_g_.fieldOf("heightmap").forGetter((c) -> {
            return c.field_236682_l_;
        }), Codec.DOUBLE.fieldOf("date_chance").orElse(0.0D).forGetter((c) -> {
            return c.dateChance;
        }), Codec.DOUBLE.fieldOf("ophidian_tongue_chance").orElse(0.0D).forGetter((c) -> {
            return c.ophidianTongueChance;
        })).apply(palmConfigInstance, PalmConfig::new);
    });
    public final double dateChance;
    public final double ophidianTongueChance;

    public PalmConfig(BlockStateProvider trunkProvider, BlockStateProvider leavesProvider, FoliagePlacer foliagePlacer, AbstractTrunkPlacer trunkPlacer, AbstractFeatureSizeType featureSizeType, List<TreeDecorator> decorators, int maxWaterDepth, boolean ignoreVines, Heightmap.Type heightmap, double dateChance, double ophidianTongueChance) {
        super(trunkProvider, leavesProvider, foliagePlacer, trunkPlacer, featureSizeType, decorators, maxWaterDepth, ignoreVines, heightmap);
        this.dateChance = dateChance;
        this.ophidianTongueChance = ophidianTongueChance;
    }

    public static class Builder extends BaseTreeFeatureConfig.Builder {
        private double dateChance;
        private double ophidianTongueChance;
        private final FoliagePlacer foliagePlacer;
        private final AbstractTrunkPlacer trunkPlacer;
        private final AbstractFeatureSizeType featureSizeType;
        private List<TreeDecorator> decorators = ImmutableList.of();
        private int maxWaterDepth;
        private boolean ignoreVines;
        private Heightmap.Type heightmap = Heightmap.Type.OCEAN_FLOOR;

        public Builder(BlockStateProvider trunkProvider, BlockStateProvider leavesProvider, FoliagePlacer foliagePlacer, AbstractTrunkPlacer trunkPlacer, AbstractFeatureSizeType featureSizeType) {
            super(trunkProvider, leavesProvider, foliagePlacer, trunkPlacer, featureSizeType);
            this.foliagePlacer = foliagePlacer;
            this.trunkPlacer = trunkPlacer;
            this.featureSizeType = featureSizeType;
        }

        public Builder date(double dateChance) {
            this.dateChance = dateChance;
            return this;
        }

        public Builder ophidianTongue(double ophidianTongueChance) {
            this.ophidianTongueChance = ophidianTongueChance;
            return this;
        }

        @Override
        @Nonnull
        public Builder func_236703_a_(@Nonnull List<TreeDecorator> decorators) {
            this.decorators = decorators;
            return this;
        }

        @Override
        @Nonnull
        public Builder func_236701_a_(int maxWaterDepth) {
            this.maxWaterDepth = maxWaterDepth;
            return this;
        }

        @Override
        @Nonnull
        public Builder setIgnoreVines() {
            this.ignoreVines = true;
            return this;
        }

        @Override
        @Nonnull
        public Builder func_236702_a_(@Nonnull Heightmap.Type heightmap) {
            this.heightmap = heightmap;
            return this;
        }

        @Override
        @Nonnull
        public PalmConfig build() {
            return new PalmConfig(this.trunkProvider, this.leavesProvider, this.foliagePlacer, this.trunkPlacer, this.featureSizeType, this.decorators, this.maxWaterDepth, this.ignoreVines, this.heightmap, this.dateChance, this.ophidianTongueChance);
        }
    }
}