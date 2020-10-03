package com.teammetallurgy.atum.world.gen.feature.config;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;
import com.teammetallurgy.atum.init.AtumBlocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.blockstateprovider.BlockStateProvider;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.foliageplacer.FoliagePlacer;
import net.minecraft.world.gen.foliageplacer.FoliagePlacerType;
import net.minecraft.world.gen.treedecorator.TreeDecorator;
import net.minecraftforge.common.IPlantable;

import javax.annotation.Nonnull;
import java.util.List;

public class PalmConfig extends BaseTreeFeatureConfig {
    public final double dateChance;
    public final double ophidianTongueChance;

    public PalmConfig(BlockStateProvider trunkProvider, BlockStateProvider leavesProvider, FoliagePlacer foliagePlacer, List<TreeDecorator> decorators, int baseHeight, int heightRandA, int heightRandB, int trunkHeight, int trunkHeightRandom, int trunkTopOffset, int trunkTopOffsetRandom, int foliageHeight, int foliageHeightRandom, int maxWaterDepth, boolean ignoreVines, double dateChance, double ophidianTongueChance) {
        super(trunkProvider, leavesProvider, foliagePlacer, decorators, baseHeight, heightRandA, heightRandB, trunkHeight, trunkHeightRandom, trunkTopOffset, trunkTopOffsetRandom, foliageHeight, foliageHeightRandom, maxWaterDepth, ignoreVines);
        this.dateChance = dateChance;
        this.ophidianTongueChance = ophidianTongueChance;
    }

    @Override
    @Nonnull
    protected PalmConfig setSapling(@Nonnull IPlantable plantable) {
        super.setSapling(plantable);
        return this;
    }

    @Override
    @Nonnull
    public <T> Dynamic<T> serialize(DynamicOps<T> ops) {
        ImmutableMap.Builder<T, T> builder = ImmutableMap.builder();
        builder.put(ops.createString("date_chance"), ops.createDouble(this.dateChance)).put(ops.createString("ophidian_tongue_chance"), ops.createDouble(this.ophidianTongueChance));
        Dynamic<T> dynamic = new Dynamic<>(ops, ops.createMap(builder.build()));
        return dynamic.merge(super.serialize(ops));
    }

    public static <T> PalmConfig deserialize(Dynamic<T> dynamic) {
        BaseTreeFeatureConfig treeConfig = BaseTreeFeatureConfig.deserialize(dynamic);
        FoliagePlacerType<?> foliagePlacerType = Registry.FOLIAGE_PLACER_TYPE.getOrDefault(new ResourceLocation(dynamic.get("foliage_placer").get("type").asString().orElseThrow(RuntimeException::new)));
        return new PalmConfig(treeConfig.trunkProvider, treeConfig.leavesProvider, foliagePlacerType.func_227391_a_(dynamic.get("foliage_placer").orElseEmptyMap()), treeConfig.decorators, treeConfig.baseHeight, dynamic.get("height_rand_a").asInt(0), dynamic.get("height_rand_b").asInt(0), dynamic.get("trunk_height").asInt(-1), dynamic.get("trunk_height_random").asInt(0), dynamic.get("trunk_top_offset").asInt(0), dynamic.get("trunk_top_offset_random").asInt(0), dynamic.get("foliage_height").asInt(-1), dynamic.get("foliage_height_random").asInt(0), dynamic.get("max_water_depth").asInt(0), dynamic.get("ignore_vines").asBoolean(false), dynamic.get("date_chance").asDouble(0.0D), dynamic.get("ophidian_tongue_chance").asDouble(0.0D));
    }

    public static <T> PalmConfig deserializePalm(Dynamic<T> data) {
        return deserialize(data).setSapling((IPlantable) AtumBlocks.PALM_SAPLING);
    }

    public static class Builder extends BaseTreeFeatureConfig.Builder {
        private double dateChance;
        private double ophidianTongueChance;
        private final FoliagePlacer foliagePlacer;
        private List<TreeDecorator> decorators = ImmutableList.of();
        private int baseHeight;
        private int heightRandA;
        private int heightRandB;
        private int trunkHeight = -1;
        private int trunkHeightRandom;
        private int trunkTopOffset;
        private int trunkTopOffsetRandom;
        private int foliageHeight = -1;
        private int foliageHeightRandom;
        private int maxWaterDepth;
        private boolean ignoreVines;

        public Builder(BlockStateProvider trunkProvider, BlockStateProvider leavesProvider, FoliagePlacer foliagePlacer) {
            super(trunkProvider, leavesProvider, foliagePlacer);
            this.foliagePlacer = foliagePlacer;
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
        public Builder decorators(@Nonnull List<TreeDecorator> decorators) {
            this.decorators = decorators;
            return this;
        }

        @Override
        @Nonnull
        public Builder baseHeight(int baseHeight) {
            this.baseHeight = baseHeight;
            return this;
        }

        @Override
        @Nonnull
        public Builder heightRandA(int heightRandA) {
            this.heightRandA = heightRandA;
            return this;
        }

        @Override
        @Nonnull
        public Builder heightRandB(int heightRandB) {
            this.heightRandB = heightRandB;
            return this;
        }

        @Override
        @Nonnull
        public Builder trunkHeight(int trunkHeight) {
            this.trunkHeight = trunkHeight;
            return this;
        }

        @Override
        @Nonnull
        public Builder trunkHeightRandom(int trunkHeightRandom) {
            this.trunkHeightRandom = trunkHeightRandom;
            return this;
        }

        @Override
        @Nonnull
        public Builder trunkTopOffset(int trunkTopOffset) {
            this.trunkTopOffset = trunkTopOffset;
            return this;
        }

        @Override
        @Nonnull
        public Builder trunkTopOffsetRandom(int trunkTopOffsetRandom) {
            this.trunkTopOffsetRandom = trunkTopOffsetRandom;
            return this;
        }

        @Override
        @Nonnull
        public Builder foliageHeight(int foliageHeight) {
            this.foliageHeight = foliageHeight;
            return this;
        }

        @Override
        @Nonnull
        public Builder foliageHeightRandom(int foliageHeightRandom) {
            this.foliageHeightRandom = foliageHeightRandom;
            return this;
        }

        @Override
        @Nonnull
        public Builder maxWaterDepth(int maxWaterDepth) {
            this.maxWaterDepth = maxWaterDepth;
            return this;
        }

        @Override
        @Nonnull
        public Builder ignoreVines() {
            this.ignoreVines = true;
            return this;
        }

        @Override
        @Nonnull
        public Builder setSapling(@Nonnull IPlantable plantable) {
            return (Builder) super.setSapling(plantable);
        }

        @Override
        @Nonnull
        public PalmConfig build() {
            return new PalmConfig(this.trunkProvider, this.leavesProvider, this.foliagePlacer, this.decorators, this.baseHeight, this.heightRandA, this.heightRandB, this.trunkHeight, this.trunkHeightRandom, this.trunkTopOffset, this.trunkTopOffsetRandom, this.foliageHeight, this.foliageHeightRandom, this.maxWaterDepth, this.ignoreVines, this.dateChance, this.ophidianTongueChance).setSapling(this.sapling);
        }
    }
}