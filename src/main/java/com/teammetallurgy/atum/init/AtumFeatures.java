package com.teammetallurgy.atum.init;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.world.gen.feature.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class AtumFeatures {
    public static final DeferredRegister<Feature<?>> FEATURES_DEFERRED = DeferredRegister.create(Registries.FEATURE, Atum.MOD_ID);

    //Features
    public static final DeferredHolder<Feature<?>, BonusCrateFeature> BONUS_CRATE = register("bonus_crate", new BonusCrateFeature(NoneFeatureConfiguration.CODEC));
    public static final DeferredHolder<Feature<?>, StartStructureFeature> START_STRUCTURE = register("start_structure", new StartStructureFeature(NoneFeatureConfiguration.CODEC));
    public static final DeferredHolder<Feature<?>, LimestoneDungeonsFeature> LIMESTONE_DUNGEONS = register("limestone_dungeon", new LimestoneDungeonsFeature(NoneFeatureConfiguration.CODEC));
    public static final DeferredHolder<Feature<?>, DeadwoodFeature> DEADWOOD_TREE = register("deadwood_tree", new DeadwoodFeature(NoneFeatureConfiguration.CODEC));
    public static final DeferredHolder<Feature<?>, LimestoneSpikeFeature> LIMESTONE_SPIKE = register("limestone_spike", new LimestoneSpikeFeature(NoneFeatureConfiguration.CODEC));
    public static final DeferredHolder<Feature<?>, AnputsFingersFeature> ANPUTS_FINGERS = register("anputs_fingers", new AnputsFingersFeature(NoneFeatureConfiguration.CODEC));
    public static final DeferredHolder<Feature<?>, Feature<NoneFeatureConfiguration>> SAND_LAYER = register("sand_layer", new SandLayerFeature(NoneFeatureConfiguration.CODEC));

    //Configured Feature Resource Keys (Only use when in-code references is needed)
    public static final ResourceKey<ConfiguredFeature<?, ?>> PALM_TREE = registerConfiguredKey("palm_tree");

    //Feature Configs //TODO Kept for reference, needs to be moved to json
//    public static final RandomPatchConfiguration OASIS_GRASS_CONFIG = (new RandomPatchConfiguration.GrassConfigurationBuilder(new SimpleStateProvider(AtumBlocks.OASIS_GRASS.defaultBlockState()), new SimpleBlockPlacer())).tries(64).build();
//    public static final RandomPatchConfiguration DENSE_DRY_GRASS_CONFIG = (new RandomPatchConfiguration.GrassConfigurationBuilder(new SimpleStateProvider(AtumBlocks.DRY_GRASS.defaultBlockState()), new SimpleBlockPlacer())).tries(30).build();
//    public static final RandomPatchConfiguration SPARSE_DRY_GRASS_CONFIG = (new RandomPatchConfiguration.GrassConfigurationBuilder(new SimpleStateProvider(AtumBlocks.DRY_GRASS.defaultBlockState()), new SimpleBlockPlacer())).tries(16).build();
//    public static final RandomPatchConfiguration DRY_TALL_GRASS_CONFIG = (new RandomPatchConfiguration.GrassConfigurationBuilder(new SimpleStateProvider(AtumBlocks.TALL_DRY_GRASS.defaultBlockState()), new DoublePlantPlacer())).tries(20).build();
//    public static final RandomPatchConfiguration SPARSE_TALL_GRASS_CONFIG = (new RandomPatchConfiguration.GrassConfigurationBuilder(new SimpleStateProvider(AtumBlocks.TALL_DRY_GRASS.defaultBlockState()), new DoublePlantPlacer())).tries(2).build();
//    public static final RandomPatchConfiguration PAPYRUS_CONFIG = (new RandomPatchConfiguration.GrassConfigurationBuilder(new SimpleStateProvider(AtumBlocks.PAPYRUS.defaultBlockState()), new ColumnPlacer(1, 2))).tries(8).xspread(2).yspread(0).zspread(2).needWater().build();
//    public static final TreeConfiguration DEAD_PALM_TREE_CONFIG = (new TreeConfiguration.TreeConfigurationBuilder(new SimpleStateProvider(AtumBlocks.DEADWOOD_LOG.defaultBlockState().setValue(DeadwoodLogBlock.HAS_SCARAB, true)), new SimpleStateProvider(AtumBlocks.DRY_LEAVES.defaultBlockState()), new PalmFoliagePlacer(0.0F), new PalmTrunkPlacer(4, 1, 5, 0.0F), new TwoLayersFeatureSize(0, 0, 0))).ignoreVines().build();
//    public static final SpringConfiguration WATER_SPRING_CONFIG = new SpringConfiguration(Fluids.WATER.defaultFluidState(), true, 4, 1, ImmutableSet.of(AtumBlocks.LIMESTONE));
//    public static final SpringConfiguration LAVA_SPRING_CONFIG = new SpringConfiguration(Fluids.LAVA.defaultFluidState(), true, 4, 1, ImmutableSet.of(AtumBlocks.LIMESTONE));

    //ConfiguredFeature //TODO Kept for reference, needs to be moved to json
//    public static final ConfiguredFeature<?, ?> DEAD_PALM_TREE = register("dead_palm_tree", ATUM_TREE.configured(DEAD_PALM_TREE_CONFIG).decorated(Features.Decorators.HEIGHTMAP_SQUARE).decorated(FeatureDecorator.COUNT_EXTRA.configured(new FrequencyWithExtraChanceDecoratorConfiguration(1, 0.1F, 1))));
//    public static final ConfiguredFeature<?, ?> ACACIA_TREE = register("acacia_tree", ATUM_TREE.configured(new TreeConfiguration.TreeConfigurationBuilder(new SimpleStateProvider(Blocks.ACACIA_LOG.defaultBlockState()), new SimpleStateProvider(Blocks.ACACIA_LEAVES.defaultBlockState()), new AcaciaFoliagePlacer(UniformInt.fixed(2), UniformInt.fixed(0)), new ForkingTrunkPlacer(5, 2, 2), new TwoLayersFeatureSize(1, 0, 2)).ignoreVines().build()).decorated(Features.Decorators.HEIGHTMAP_SQUARE).decorated(FeatureDecorator.COUNT_EXTRA.configured(new FrequencyWithExtraChanceDecoratorConfiguration(1, 0.1F, 1))));
//    public static final ConfiguredFeature<?, ?> DEADWOOD_3_01_1 = register("deadwood_tree_3_01_1", DEADWOOD_FEATURE.configured(NoneFeatureConfiguration.INSTANCE).decorated(Features.Decorators.HEIGHTMAP_SQUARE).decorated(FeatureDecorator.COUNT_EXTRA.configured(new FrequencyWithExtraChanceDecoratorConfiguration(3, 0.1F, 1))));
//    public static final ConfiguredFeature<?, ?> DEADWOOD_20_025_3 = register("deadwood_tree_20_025_3", DEADWOOD_FEATURE.configured(NoneFeatureConfiguration.INSTANCE).decorated(Features.Decorators.HEIGHTMAP_SQUARE).decorated(FeatureDecorator.COUNT_EXTRA.configured(new FrequencyWithExtraChanceDecoratorConfiguration(20, 0.25F, 3))));
//    public static final ConfiguredFeature<?, ?> DEADWOOD_0_02_1 = register("deadwood_tree_0_02_1", DEADWOOD_FEATURE.configured(NoneFeatureConfiguration.INSTANCE).decorated(Features.Decorators.HEIGHTMAP_SQUARE).decorated(FeatureDecorator.COUNT_EXTRA.configured(new FrequencyWithExtraChanceDecoratorConfiguration(0, 0.2F, 1))));
//    public static final ConfiguredFeature<?, ?> DEADWOOD_0_01_1 = register("deadwood_tree_0_01_1", DEADWOOD_FEATURE.configured(NoneFeatureConfiguration.INSTANCE).decorated(Features.Decorators.HEIGHTMAP_SQUARE).decorated(FeatureDecorator.COUNT_EXTRA.configured(new FrequencyWithExtraChanceDecoratorConfiguration(0, 0.1F, 1))));
//    public static final ConfiguredFeature<?, ?> DEADWOOD_0_08_1 = register("deadwood_tree_0_08_1", DEADWOOD_FEATURE.configured(NoneFeatureConfiguration.INSTANCE).decorated(Features.Decorators.HEIGHTMAP_SQUARE).decorated(FeatureDecorator.COUNT_EXTRA.configured(new FrequencyWithExtraChanceDecoratorConfiguration(0, 0.8F, 1))));
//    public static final ConfiguredFeature<?, ?> DEADWOOD_0_025_1 = register("deadwood_tree_0_025_1", DEADWOOD_FEATURE.configured(NoneFeatureConfiguration.INSTANCE).decorated(Features.Decorators.HEIGHTMAP_SQUARE).decorated(FeatureDecorator.COUNT_EXTRA.configured(new FrequencyWithExtraChanceDecoratorConfiguration(0, 0.25F, 1))));
//    public static final ConfiguredFeature<?, ?> SAND = register("sand", Feature.ORE.configured(new OreConfiguration(AtumDefaultFeatures.NATURAL_STONE, AtumBlocks.SAND.defaultBlockState(), AtumConfig.WORLD_GEN.sandVeinSize.get())).range(AtumConfig.WORLD_GEN.sandMaxHeight.get()).squared().count(AtumConfig.WORLD_GEN.sandCount.get()));
//    public static final ConfiguredFeature<?, ?> LIMESTONE_GRAVEL = register("limestone_gravel", Feature.ORE.configured(new OreConfiguration(AtumDefaultFeatures.NATURAL_STONE, AtumBlocks.LIMESTONE_GRAVEL.defaultBlockState(), AtumConfig.WORLD_GEN.limestoneGravelVeinSize.get())).range(AtumConfig.WORLD_GEN.limestoneGravelMaxHeight.get()).squared().count(AtumConfig.WORLD_GEN.limestoneGravelCount.get()));
//    public static final ConfiguredFeature<?, ?> MARL = register("marl", Feature.ORE.configured(new OreConfiguration(AtumDefaultFeatures.NATURAL_STONE, AtumBlocks.MARL.defaultBlockState(), AtumConfig.WORLD_GEN.marlVeinSize.get())).range(AtumConfig.WORLD_GEN.marlMaxHeight.get()).squared().count(AtumConfig.WORLD_GEN.marlCount.get()));
//    public static final ConfiguredFeature<?, ?> MARL_DRIED_RIVER = register("marl_dried_river", Feature.ORE.configured(new OreConfiguration(AtumDefaultFeatures.LIMESTONE_CRACKED, AtumBlocks.MARL.defaultBlockState(), 32)).decorated(FeatureDecorator.RANGE.configured(new RangeDecoratorConfiguration(55, 0, 67))).count(90));
//    public static final ConfiguredFeature<?, ?> ALABASTER = register("alabaster", Feature.ORE.configured(new OreConfiguration(AtumDefaultFeatures.NATURAL_STONE, AtumBlocks.ALABASTER.defaultBlockState(), AtumConfig.WORLD_GEN.alabasterVeinSize.get())).range(AtumConfig.WORLD_GEN.alabasterMaxHeight.get()).squared().count(AtumConfig.WORLD_GEN.alabasterCount.get()));
//    public static final ConfiguredFeature<?, ?> PORPHYRY = register("porphyry", Feature.ORE.configured(new OreConfiguration(AtumDefaultFeatures.NATURAL_STONE, AtumBlocks.PORPHYRY.defaultBlockState(), AtumConfig.WORLD_GEN.porphyryVeinSize.get())).range(AtumConfig.WORLD_GEN.porphyryMaxHeight.get()).squared().count(AtumConfig.WORLD_GEN.porphyryCount.get()));
//    public static final ConfiguredFeature<?, ?> LIMESTONE_INFESTED = register("limestone_infested", Feature.ORE.configured(new OreConfiguration(AtumDefaultFeatures.NATURAL_STONE, AtumBlocks.LIMESTONE.defaultBlockState().setValue(LimestoneBlock.HAS_SCARAB, true), 10)).decorated(FeatureDecorator.RANGE.configured(new RangeDecoratorConfiguration(0, 11, 80)).squared().count(8)));
//    public static final ConfiguredFeature<?, ?> PAPYRUS = register("papyrus", Feature.RANDOM_PATCH.configured(AtumFeatures.PAPYRUS_CONFIG).decorated(Features.Decorators.HEIGHTMAP_DOUBLE_SQUARE).count(3));
//    public static final ConfiguredFeature<?, ?> OASIS_GRASS = register("oasis_grass", Feature.RANDOM_PATCH.configured(AtumFeatures.OASIS_GRASS_CONFIG).decorated(Features.Decorators.HEIGHTMAP_DOUBLE_SQUARE).count(16));
//    public static final ConfiguredFeature<?, ?> LILY_PAD = register("lily_pad", Feature.RANDOM_PATCH.configured((new RandomPatchConfiguration.GrassConfigurationBuilder(new SimpleStateProvider(Blocks.LILY_PAD.defaultBlockState()), SimpleBlockPlacer.INSTANCE)).tries(7).build()).decorated(Features.Decorators.HEIGHTMAP_DOUBLE_SQUARE).count(2));
//    public static final ConfiguredFeature<?, ?> DRY_TALL_GRASS = register("dry_tall_grass", Feature.RANDOM_PATCH.configured(AtumFeatures.DRY_TALL_GRASS_CONFIG).decorated(Features.Decorators.ADD_32).decorated(Features.Decorators.HEIGHTMAP).squared().decorated(FeatureDecorator.COUNT_NOISE.configured(new NoiseDependantDecoratorConfiguration(-0.8D, 0, 7))));
//    public static final ConfiguredFeature<?, ?> SPARSE_TALL_GRASS = register("sparse_tall_grass", Feature.RANDOM_PATCH.configured(AtumFeatures.SPARSE_TALL_GRASS_CONFIG).decorated(Features.Decorators.ADD_32).decorated(Features.Decorators.HEIGHTMAP).squared().decorated(FeatureDecorator.COUNT_NOISE.configured(new NoiseDependantDecoratorConfiguration(-0.8D, 0, 7))));
//    public static final ConfiguredFeature<?, ?> SPARSE_DRY_GRASS_SPREAD_5 = register("dry_grass_spread_5", Feature.RANDOM_PATCH.configured(AtumFeatures.SPARSE_DRY_GRASS_CONFIG).decorated(Features.Decorators.HEIGHTMAP_DOUBLE_SQUARE).count(5));
//    public static final ConfiguredFeature<?, ?> SPARSE_DRY_GRASS_NOISE_08_2_3 = register("dry_grass_spread_08_2_3", Feature.RANDOM_PATCH.configured(AtumFeatures.SPARSE_DRY_GRASS_CONFIG).decorated(Features.Decorators.HEIGHTMAP_DOUBLE_SQUARE).decorated(FeatureDecorator.COUNT_NOISE.configured(new NoiseDependantDecoratorConfiguration(-0.8D, 2, 3))));
//    public static final ConfiguredFeature<?, ?> DENSE_DRY_GRASS_NOISE_08_5_10 = register("dry_grass_spread_08_5_10", Feature.RANDOM_PATCH.configured(AtumFeatures.DENSE_DRY_GRASS_CONFIG).decorated(Features.Decorators.HEIGHTMAP_DOUBLE_SQUARE).decorated(Features.Decorators.HEIGHTMAP_DOUBLE_SQUARE).decorated(FeatureDecorator.COUNT_NOISE.configured(new NoiseDependantDecoratorConfiguration(-0.8D, 5, 10))));
//    public static final ConfiguredFeature<?, ?> WATER_SPRING = register("water_spring", Feature.SPRING.configured(AtumFeatures.WATER_SPRING_CONFIG).decorated(FeatureDecorator.RANGE_BIASED.configured(new RangeDecoratorConfiguration(8, 8, 50)).squared().count(14)));
//    public static final ConfiguredFeature<?, ?> LAVA_SPRING = register("lava_spring", Feature.SPRING.configured(AtumFeatures.LAVA_SPRING_CONFIG).decorated(FeatureDecorator.RANGE_VERY_BIASED.configured(new RangeDecoratorConfiguration(8, 16, 256)).squared().count(8)));

    private static <C extends FeatureConfiguration, F extends Feature<C>> DeferredHolder<Feature<?>, F> register(String name, F feature) {
        return FEATURES_DEFERRED.register(name, () -> feature);
    }

    public static ResourceKey<ConfiguredFeature<?, ?>> registerConfiguredKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(Atum.MOD_ID, name));
    }
}