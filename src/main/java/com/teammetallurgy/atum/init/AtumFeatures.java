package com.teammetallurgy.atum.init;

import com.google.common.collect.ImmutableSet;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.blocks.stone.limestone.LimestoneBlock;
import com.teammetallurgy.atum.blocks.wood.DeadwoodLogBlock;
import com.teammetallurgy.atum.misc.AtumConfig;
import com.teammetallurgy.atum.world.gen.AtumDefaultFeatures;
import com.teammetallurgy.atum.world.gen.feature.*;
import com.teammetallurgy.atum.world.gen.feature.tree.AtumTreeFeature;
import com.teammetallurgy.atum.world.gen.feature.tree.PalmFoliagePlacer;
import com.teammetallurgy.atum.world.gen.feature.tree.PalmTrunkPlacer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.world.level.levelgen.feature.blockplacers.ColumnPlacer;
import net.minecraft.world.level.levelgen.feature.blockplacers.DoublePlantPlacer;
import net.minecraft.world.level.levelgen.feature.blockplacers.SimpleBlockPlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.level.levelgen.feature.foliageplacers.AcaciaFoliagePlacer;
import net.minecraft.world.gen.placement.*;
import net.minecraft.world.level.levelgen.feature.trunkplacers.ForkingTrunkPlacer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.data.worldgen.Features;
import net.minecraft.util.UniformInt;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.DecoratorConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoiseDependantDecoratorConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RangeDecoratorConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.ReplaceBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SpringConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.placement.ChanceDecoratorConfiguration;
import net.minecraft.world.level.levelgen.placement.DepthAverageConfigation;
import net.minecraft.world.level.levelgen.placement.FeatureDecorator;
import net.minecraft.world.level.levelgen.placement.FrequencyWithExtraChanceDecoratorConfiguration;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AtumFeatures {
    private static final List<Feature<?>> FEATURES = new ArrayList<>();
    //Features
    public static final Feature<BlockStateConfiguration> SURFACE_LAVA_LAKE = register("surface_lava_lake", new LakeFeature(BlockStateConfiguration.CODEC));
    public static final Feature<TreeConfiguration> ATUM_TREE = register("atum_tree", new AtumTreeFeature(TreeConfiguration.CODEC));
    public static final BonusCrateFeature BONUS_CRATE = register("bonus_crate", new BonusCrateFeature(NoneFeatureConfiguration.CODEC));
    public static final StartStructureFeature START_STRUCTURE = register("start_structure", new StartStructureFeature(NoneFeatureConfiguration.CODEC));
    public static final DirtyBoneFossilsFeature DIRTY_BONE_FOSSILS = register("dirty_bone_fossil", new DirtyBoneFossilsFeature(NoneFeatureConfiguration.CODEC));
    public static final LimestoneDungeonsFeature LIMESTONE_DUNGEONS = register("limestone_dungeon", new LimestoneDungeonsFeature(NoneFeatureConfiguration.CODEC));
    public static final DeadwoodFeature DEADWOOD_FEATURE = register("deadwood_tree", new DeadwoodFeature(NoneFeatureConfiguration.CODEC));
    public static final Feature<NoneFeatureConfiguration> LIMESTONE_SPIKE = register("limestone_spike", new LimestoneSpikeFeature(NoneFeatureConfiguration.CODEC));
    public static final Feature<RandomPatchConfiguration> ANPUTS_FINGERS = register("anputs_fingers", new AnputsFingersFeature(RandomPatchConfiguration.CODEC));
    public static final Feature<NoneFeatureConfiguration> SAND_LAYER = register("sand_layer", new SandLayerFeature(NoneFeatureConfiguration.CODEC));

    //Feature Configs
    public static final RandomPatchConfiguration OASIS_GRASS_CONFIG = (new RandomPatchConfiguration.GrassConfigurationBuilder(new SimpleStateProvider(AtumBlocks.OASIS_GRASS.defaultBlockState()), new SimpleBlockPlacer())).tries(64).build();
    public static final RandomPatchConfiguration DENSE_DRY_GRASS_CONFIG = (new RandomPatchConfiguration.GrassConfigurationBuilder(new SimpleStateProvider(AtumBlocks.DRY_GRASS.defaultBlockState()), new SimpleBlockPlacer())).tries(30).build();
    public static final RandomPatchConfiguration SPARSE_DRY_GRASS_CONFIG = (new RandomPatchConfiguration.GrassConfigurationBuilder(new SimpleStateProvider(AtumBlocks.DRY_GRASS.defaultBlockState()), new SimpleBlockPlacer())).tries(16).build();
    public static final RandomPatchConfiguration DRY_TALL_GRASS_CONFIG = (new RandomPatchConfiguration.GrassConfigurationBuilder(new SimpleStateProvider(AtumBlocks.TALL_DRY_GRASS.defaultBlockState()), new DoublePlantPlacer())).tries(20).build();
    public static final RandomPatchConfiguration SPARSE_TALL_GRASS_CONFIG = (new RandomPatchConfiguration.GrassConfigurationBuilder(new SimpleStateProvider(AtumBlocks.TALL_DRY_GRASS.defaultBlockState()), new DoublePlantPlacer())).tries(2).build();
    public static final RandomPatchConfiguration ANPUTS_FINGERS_CONFIG = (new RandomPatchConfiguration.GrassConfigurationBuilder(new SimpleStateProvider(AtumBlocks.ANPUTS_FINGERS.defaultBlockState()), new SimpleBlockPlacer())).tries(10).build();
    public static final RandomPatchConfiguration PAPYRUS_CONFIG = (new RandomPatchConfiguration.GrassConfigurationBuilder(new SimpleStateProvider(AtumBlocks.PAPYRUS.defaultBlockState()), new ColumnPlacer(1, 2))).tries(8).xspread(2).yspread(0).zspread(2).needWater().build();
    public static final TreeConfiguration PALM_TREE_CONFIG = (new TreeConfiguration.TreeConfigurationBuilder(new SimpleStateProvider(AtumBlocks.PALM_LOG.defaultBlockState()), new SimpleStateProvider(AtumBlocks.PALM_LEAVES.defaultBlockState()), new PalmFoliagePlacer(0.1F), new PalmTrunkPlacer(4, 1, 5, 0.25F), new TwoLayersFeatureSize(0, 0, 0))).ignoreVines().build();
    public static final TreeConfiguration PALM_TREE_CONFIG_SAPLING = (new TreeConfiguration.TreeConfigurationBuilder(new SimpleStateProvider(AtumBlocks.PALM_LOG.defaultBlockState()), new SimpleStateProvider(AtumBlocks.PALM_LEAVES.defaultBlockState()), new PalmFoliagePlacer(0.1F), new PalmTrunkPlacer(4, 1, 5, 0.0F), new TwoLayersFeatureSize(0, 0, 0))).ignoreVines().build();
    public static final TreeConfiguration DEAD_PALM_TREE_CONFIG = (new TreeConfiguration.TreeConfigurationBuilder(new SimpleStateProvider(AtumBlocks.DEADWOOD_LOG.defaultBlockState().setValue(DeadwoodLogBlock.HAS_SCARAB, true)), new SimpleStateProvider(AtumBlocks.DRY_LEAVES.defaultBlockState()), new PalmFoliagePlacer(0.0F), new PalmTrunkPlacer(4, 1, 5, 0.0F), new TwoLayersFeatureSize(0, 0, 0))).ignoreVines().build();
    public static final SpringConfiguration WATER_SPRING_CONFIG = new SpringConfiguration(Fluids.WATER.defaultFluidState(), true, 4, 1, ImmutableSet.of(AtumBlocks.LIMESTONE));
    public static final SpringConfiguration LAVA_SPRING_CONFIG = new SpringConfiguration(Fluids.LAVA.defaultFluidState(), true, 4, 1, ImmutableSet.of(AtumBlocks.LIMESTONE));
    public static final RandomPatchConfiguration SHRUB_CONFIG = (new RandomPatchConfiguration.GrassConfigurationBuilder(new SimpleStateProvider(AtumBlocks.SHRUB.defaultBlockState()), new SimpleBlockPlacer())).tries(3).build();
    public static final RandomPatchConfiguration WEED_CONFIG = (new RandomPatchConfiguration.GrassConfigurationBuilder(new SimpleStateProvider(AtumBlocks.WEED.defaultBlockState()), new SimpleBlockPlacer())).tries(3).build();

    //ConfiguredFeature
    public static final ConfiguredFeature<?, ?> SURFACE_LAVA_LAKE_CONFIGURED = register("surface_lava_lake", SURFACE_LAVA_LAKE.configured(new BlockStateConfiguration(Blocks.LAVA.defaultBlockState())).decorated(FeatureDecorator.LAVA_LAKE.configured(new ChanceDecoratorConfiguration(80))));
    public static final ConfiguredFeature<?, ?> PALM_TREE = register("palm_tree", ATUM_TREE.configured(PALM_TREE_CONFIG).decorated(Features.Decorators.HEIGHTMAP_SQUARE).decorated(FeatureDecorator.COUNT_EXTRA.configured(new FrequencyWithExtraChanceDecoratorConfiguration(9, 0.25F, 2))));
    public static final ConfiguredFeature<?, ?> DEAD_PALM_TREE = register("dead_palm_tree", ATUM_TREE.configured(DEAD_PALM_TREE_CONFIG).decorated(Features.Decorators.HEIGHTMAP_SQUARE).decorated(FeatureDecorator.COUNT_EXTRA.configured(new FrequencyWithExtraChanceDecoratorConfiguration(1, 0.1F, 1))));
    public static final ConfiguredFeature<?, ?> ACACIA_TREE = register("acacia_tree", ATUM_TREE.configured(new TreeConfiguration.TreeConfigurationBuilder(new SimpleStateProvider(Blocks.ACACIA_LOG.defaultBlockState()), new SimpleStateProvider(Blocks.ACACIA_LEAVES.defaultBlockState()), new AcaciaFoliagePlacer(UniformInt.fixed(2), UniformInt.fixed(0)), new ForkingTrunkPlacer(5, 2, 2), new TwoLayersFeatureSize(1, 0, 2)).ignoreVines().build()).decorated(Features.Decorators.HEIGHTMAP_SQUARE).decorated(FeatureDecorator.COUNT_EXTRA.configured(new FrequencyWithExtraChanceDecoratorConfiguration(1, 0.1F, 1))));
    public static final ConfiguredFeature<?, ?> BONUS_CRATE_CONFIGURED = register("bonus_crate", BONUS_CRATE.configured(FeatureConfiguration.NONE));
    public static final ConfiguredFeature<?, ?> START_STRUCTURE_CONFIGURED = register("start_structure", START_STRUCTURE.configured(FeatureConfiguration.NONE));
    public static final ConfiguredFeature<?, ?> DIRTY_BONE_FOSSILS_CONFIGURED = register("dirty_bone_fossil", DIRTY_BONE_FOSSILS.configured(FeatureConfiguration.NONE).chance(AtumConfig.WORLD_GEN.fossilsChance.get()));
    public static final ConfiguredFeature<?, ?> LIMESTONE_DUNGEONS_CONFIGURED = register("limestone_dungeon", LIMESTONE_DUNGEONS.configured(FeatureConfiguration.NONE).range(256).squared().count(AtumConfig.WORLD_GEN.dungeonChance.get()));
    public static final ConfiguredFeature<?, ?> DEADWOOD_3_01_1 = register("deadwood_tree_3_01_1", DEADWOOD_FEATURE.configured(NoneFeatureConfiguration.INSTANCE).decorated(Features.Decorators.HEIGHTMAP_SQUARE).decorated(FeatureDecorator.COUNT_EXTRA.configured(new FrequencyWithExtraChanceDecoratorConfiguration(3, 0.1F, 1))));
    public static final ConfiguredFeature<?, ?> DEADWOOD_20_025_3 = register("deadwood_tree_20_025_3", DEADWOOD_FEATURE.configured(NoneFeatureConfiguration.INSTANCE).decorated(Features.Decorators.HEIGHTMAP_SQUARE).decorated(FeatureDecorator.COUNT_EXTRA.configured(new FrequencyWithExtraChanceDecoratorConfiguration(20, 0.25F, 3))));
    public static final ConfiguredFeature<?, ?> DEADWOOD_0_02_1 = register("deadwood_tree_0_02_1", DEADWOOD_FEATURE.configured(NoneFeatureConfiguration.INSTANCE).decorated(Features.Decorators.HEIGHTMAP_SQUARE).decorated(FeatureDecorator.COUNT_EXTRA.configured(new FrequencyWithExtraChanceDecoratorConfiguration(0, 0.2F, 1))));
    public static final ConfiguredFeature<?, ?> DEADWOOD_0_01_1 = register("deadwood_tree_0_01_1", DEADWOOD_FEATURE.configured(NoneFeatureConfiguration.INSTANCE).decorated(Features.Decorators.HEIGHTMAP_SQUARE).decorated(FeatureDecorator.COUNT_EXTRA.configured(new FrequencyWithExtraChanceDecoratorConfiguration(0, 0.1F, 1))));
    public static final ConfiguredFeature<?, ?> DEADWOOD_0_08_1 = register("deadwood_tree_0_08_1", DEADWOOD_FEATURE.configured(NoneFeatureConfiguration.INSTANCE).decorated(Features.Decorators.HEIGHTMAP_SQUARE).decorated(FeatureDecorator.COUNT_EXTRA.configured(new FrequencyWithExtraChanceDecoratorConfiguration(0, 0.8F, 1))));
    public static final ConfiguredFeature<?, ?> DEADWOOD_0_025_1 = register("deadwood_tree_0_025_1", DEADWOOD_FEATURE.configured(NoneFeatureConfiguration.INSTANCE).decorated(Features.Decorators.HEIGHTMAP_SQUARE).decorated(FeatureDecorator.COUNT_EXTRA.configured(new FrequencyWithExtraChanceDecoratorConfiguration(0, 0.25F, 1))));
    public static final ConfiguredFeature<?, ?> LIMESTONE_SPIKE_CONFIGURED = register("limestone_spike", LIMESTONE_SPIKE.configured(FeatureConfiguration.NONE).decorated(Features.Decorators.HEIGHTMAP_SQUARE).count(3));
    public static final ConfiguredFeature<?, ?> ANPUTS_FINGERS_CONFIGURED = register("anputs_fingers", ANPUTS_FINGERS.configured(ANPUTS_FINGERS_CONFIG).decorated(Features.Decorators.HEIGHTMAP_DOUBLE_SQUARE).decorated(FeatureDecorator.HEIGHTMAP_WORLD_SURFACE.configured(DecoratorConfiguration.NONE)).count(16));
    public static final ConfiguredFeature<?, ?> SAND_LAYER_CONFIGURED = register("sand_layer", SAND_LAYER.configured(FeatureConfiguration.NONE));
    public static final ConfiguredFeature<?, ?> COAL_ORE = register("coal_ore", Feature.ORE.configured(new OreConfiguration(AtumDefaultFeatures.NATURAL_STONE, AtumBlocks.COAL_ORE.defaultBlockState(), AtumConfig.WORLD_GEN.coalVeinSize.get())).range(AtumConfig.WORLD_GEN.coalMaxHeight.get()).squared().count(AtumConfig.WORLD_GEN.coalCount.get()));
    public static final ConfiguredFeature<?, ?> IRON_ORE = register("iron_ore", Feature.ORE.configured(new OreConfiguration(AtumDefaultFeatures.NATURAL_STONE, AtumBlocks.IRON_ORE.defaultBlockState(), AtumConfig.WORLD_GEN.ironVeinSize.get())).range(AtumConfig.WORLD_GEN.ironMaxHeight.get()).squared().count(AtumConfig.WORLD_GEN.ironCount.get()));
    public static final ConfiguredFeature<?, ?> GOLD_ORE = register("gold_ore", Feature.ORE.configured(new OreConfiguration(AtumDefaultFeatures.NATURAL_STONE, AtumBlocks.GOLD_ORE.defaultBlockState(), AtumConfig.WORLD_GEN.goldVeinSize.get())).range(AtumConfig.WORLD_GEN.goldMaxHeight.get()).squared().count(AtumConfig.WORLD_GEN.goldCount.get()));
    public static final ConfiguredFeature<?, ?> REDSTONE_ORE = register("redstone_ore", Feature.ORE.configured(new OreConfiguration(AtumDefaultFeatures.NATURAL_STONE, AtumBlocks.REDSTONE_ORE.defaultBlockState(), AtumConfig.WORLD_GEN.redstoneVeinSize.get())).range(AtumConfig.WORLD_GEN.redstoneMaxHeight.get()).squared().count(AtumConfig.WORLD_GEN.redstoneCount.get()));
    public static final ConfiguredFeature<?, ?> DIAMOND_ORE = register("diamond_ore", Feature.ORE.configured(new OreConfiguration(AtumDefaultFeatures.NATURAL_STONE, AtumBlocks.DIAMOND_ORE.defaultBlockState(), AtumConfig.WORLD_GEN.diamondVeinSize.get())).range(AtumConfig.WORLD_GEN.diamondMaxHeight.get()).squared().count(AtumConfig.WORLD_GEN.diamondCount.get()));
    public static final ConfiguredFeature<?, ?> LAPIS_ORE = register("lapis_ore", Feature.ORE.configured(new OreConfiguration(AtumDefaultFeatures.NATURAL_STONE, AtumBlocks.LAPIS_ORE.defaultBlockState(), AtumConfig.WORLD_GEN.lapisVeinSize.get())).decorated(FeatureDecorator.DEPTH_AVERAGE.configured(new DepthAverageConfigation(AtumConfig.WORLD_GEN.lapisBaseline.get(), AtumConfig.WORLD_GEN.lapisSpread.get()))));
    public static final ConfiguredFeature<?, ?> EMERALD_ORE = register("emerald_ore", Feature.EMERALD_ORE.configured(new ReplaceBlockConfiguration(AtumBlocks.LIMESTONE.defaultBlockState(), AtumBlocks.EMERALD_ORE.defaultBlockState())).decorated(FeatureDecorator.EMERALD_ORE.configured(DecoratorConfiguration.NONE)));
    public static final ConfiguredFeature<?, ?> KHNUMITE_RAW = register("khnumite_raw", Feature.ORE.configured(new OreConfiguration(AtumDefaultFeatures.NATURAL_STONE, AtumBlocks.KHNUMITE_RAW.defaultBlockState(), AtumConfig.WORLD_GEN.khnumiteVeinSize.get())).range(AtumConfig.WORLD_GEN.khnumiteMaxHeight.get()).squared().count(AtumConfig.WORLD_GEN.khnumiteCount.get()));
    public static final ConfiguredFeature<?, ?> BONE_ORE = register("bone_ore", Feature.ORE.configured(new OreConfiguration(AtumDefaultFeatures.NATURAL_STONE, AtumBlocks.BONE_ORE.defaultBlockState(), AtumConfig.WORLD_GEN.boneOreVeinSize.get())).range(AtumConfig.WORLD_GEN.boneOreMaxHeight.get()).squared().count(AtumConfig.WORLD_GEN.boneOreCount.get()));
    public static final ConfiguredFeature<?, ?> RELIC_ORE = register("relic_ore", Feature.ORE.configured(new OreConfiguration(AtumDefaultFeatures.NATURAL_STONE, AtumBlocks.RELIC_ORE.defaultBlockState(), AtumConfig.WORLD_GEN.relicOreVeinSize.get())).range(AtumConfig.WORLD_GEN.relicOreMaxHeight.get()).squared().count(AtumConfig.WORLD_GEN.relicOreCount.get()));
    public static final ConfiguredFeature<?, ?> NEBU_ORE = register("nebu_ore", Feature.ORE.configured(new OreConfiguration(AtumDefaultFeatures.NATURAL_STONE, AtumBlocks.NEBU_ORE.defaultBlockState(), AtumConfig.WORLD_GEN.nebuVeinSize.get())).range(AtumConfig.WORLD_GEN.nebuMaxHeight.get()).squared().count(AtumConfig.WORLD_GEN.nebuCount.get()));
    public static final ConfiguredFeature<?, ?> SAND = register("sand", Feature.ORE.configured(new OreConfiguration(AtumDefaultFeatures.NATURAL_STONE, AtumBlocks.SAND.defaultBlockState(), AtumConfig.WORLD_GEN.sandVeinSize.get())).range(AtumConfig.WORLD_GEN.sandMaxHeight.get()).squared().count(AtumConfig.WORLD_GEN.sandCount.get()));
    public static final ConfiguredFeature<?, ?> LIMESTONE_GRAVEL = register("limestone_gravel", Feature.ORE.configured(new OreConfiguration(AtumDefaultFeatures.NATURAL_STONE, AtumBlocks.LIMESTONE_GRAVEL.defaultBlockState(), AtumConfig.WORLD_GEN.limestoneGravelVeinSize.get())).range(AtumConfig.WORLD_GEN.limestoneGravelMaxHeight.get()).squared().count(AtumConfig.WORLD_GEN.limestoneGravelCount.get()));
    public static final ConfiguredFeature<?, ?> MARL = register("marl", Feature.ORE.configured(new OreConfiguration(AtumDefaultFeatures.NATURAL_STONE, AtumBlocks.MARL.defaultBlockState(), AtumConfig.WORLD_GEN.marlVeinSize.get())).range(AtumConfig.WORLD_GEN.marlMaxHeight.get()).squared().count(AtumConfig.WORLD_GEN.marlCount.get()));
    public static final ConfiguredFeature<?, ?> MARL_DRIED_RIVER = register("marl_dried_river", Feature.ORE.configured(new OreConfiguration(AtumDefaultFeatures.LIMESTONE_CRACKED, AtumBlocks.MARL.defaultBlockState(), 32)).decorated(FeatureDecorator.RANGE.configured(new RangeDecoratorConfiguration(55, 0, 67))).count(90));
    public static final ConfiguredFeature<?, ?> ALABASTER = register("alabaster", Feature.ORE.configured(new OreConfiguration(AtumDefaultFeatures.NATURAL_STONE, AtumBlocks.ALABASTER.defaultBlockState(), AtumConfig.WORLD_GEN.alabasterVeinSize.get())).range(AtumConfig.WORLD_GEN.alabasterMaxHeight.get()).squared().count(AtumConfig.WORLD_GEN.alabasterCount.get()));
    public static final ConfiguredFeature<?, ?> PORPHYRY = register("porphyry", Feature.ORE.configured(new OreConfiguration(AtumDefaultFeatures.NATURAL_STONE, AtumBlocks.PORPHYRY.defaultBlockState(), AtumConfig.WORLD_GEN.porphyryVeinSize.get())).range(AtumConfig.WORLD_GEN.porphyryMaxHeight.get()).squared().count(AtumConfig.WORLD_GEN.porphyryCount.get()));
    public static final ConfiguredFeature<?, ?> LIMESTONE_INFESTED = register("limestone_infested", Feature.ORE.configured(new OreConfiguration(AtumDefaultFeatures.NATURAL_STONE, AtumBlocks.LIMESTONE.defaultBlockState().setValue(LimestoneBlock.HAS_SCARAB, true), 10)).decorated(FeatureDecorator.RANGE.configured(new RangeDecoratorConfiguration(0, 11, 80)).squared().count(8)));
    public static final ConfiguredFeature<?, ?> SHRUB = register("shrub", Feature.RANDOM_PATCH.configured(AtumFeatures.SHRUB_CONFIG).decorated(Features.Decorators.HEIGHTMAP_DOUBLE_SQUARE).count(AtumConfig.WORLD_GEN.shrubFrequency.get()));
    public static final ConfiguredFeature<?, ?> WEED = register("weed", Feature.RANDOM_PATCH.configured(AtumFeatures.WEED_CONFIG).decorated(Features.Decorators.HEIGHTMAP_DOUBLE_SQUARE).count(AtumConfig.WORLD_GEN.shrubFrequency.get()));
    public static final ConfiguredFeature<?, ?> PAPYRUS = register("papyrus", Feature.RANDOM_PATCH.configured(AtumFeatures.PAPYRUS_CONFIG).decorated(Features.Decorators.HEIGHTMAP_DOUBLE_SQUARE).count(3));
    public static final ConfiguredFeature<?, ?> OASIS_GRASS = register("oasis_grass", Feature.RANDOM_PATCH.configured(AtumFeatures.OASIS_GRASS_CONFIG).decorated(Features.Decorators.HEIGHTMAP_DOUBLE_SQUARE).count(16));
    public static final ConfiguredFeature<?, ?> LILY_PAD = register("lily_pad", Feature.RANDOM_PATCH.configured((new RandomPatchConfiguration.GrassConfigurationBuilder(new SimpleStateProvider(Blocks.LILY_PAD.defaultBlockState()), SimpleBlockPlacer.INSTANCE)).tries(7).build()).decorated(Features.Decorators.HEIGHTMAP_DOUBLE_SQUARE).count(2));
    public static final ConfiguredFeature<?, ?> DRY_TALL_GRASS = register("dry_tall_grass", Feature.RANDOM_PATCH.configured(AtumFeatures.DRY_TALL_GRASS_CONFIG).decorated(Features.Decorators.ADD_32).decorated(Features.Decorators.HEIGHTMAP).squared().decorated(FeatureDecorator.COUNT_NOISE.configured(new NoiseDependantDecoratorConfiguration(-0.8D, 0, 7))));
    public static final ConfiguredFeature<?, ?> SPARSE_TALL_GRASS = register("sparse_tall_grass", Feature.RANDOM_PATCH.configured(AtumFeatures.SPARSE_TALL_GRASS_CONFIG).decorated(Features.Decorators.ADD_32).decorated(Features.Decorators.HEIGHTMAP).squared().decorated(FeatureDecorator.COUNT_NOISE.configured(new NoiseDependantDecoratorConfiguration(-0.8D, 0, 7))));
    public static final ConfiguredFeature<?, ?> SPARSE_DRY_GRASS_SPREAD_5 = register("dry_grass_spread_5", Feature.RANDOM_PATCH.configured(AtumFeatures.SPARSE_DRY_GRASS_CONFIG).decorated(Features.Decorators.HEIGHTMAP_DOUBLE_SQUARE).count(5));
    public static final ConfiguredFeature<?, ?> SPARSE_DRY_GRASS_NOISE_08_2_3 = register("dry_grass_spread_08_2_3", Feature.RANDOM_PATCH.configured(AtumFeatures.SPARSE_DRY_GRASS_CONFIG).decorated(Features.Decorators.HEIGHTMAP_DOUBLE_SQUARE).decorated(FeatureDecorator.COUNT_NOISE.configured(new NoiseDependantDecoratorConfiguration(-0.8D, 2, 3))));
    public static final ConfiguredFeature<?, ?> DENSE_DRY_GRASS_NOISE_08_5_10 = register("dry_grass_spread_08_5_10", Feature.RANDOM_PATCH.configured(AtumFeatures.DENSE_DRY_GRASS_CONFIG).decorated(Features.Decorators.HEIGHTMAP_DOUBLE_SQUARE).decorated(Features.Decorators.HEIGHTMAP_DOUBLE_SQUARE).decorated(FeatureDecorator.COUNT_NOISE.configured(new NoiseDependantDecoratorConfiguration(-0.8D, 5, 10))));
    public static final ConfiguredFeature<?, ?> WATER_SPRING = register("water_spring", Feature.SPRING.configured(AtumFeatures.WATER_SPRING_CONFIG).decorated(FeatureDecorator.RANGE_BIASED.configured(new RangeDecoratorConfiguration(8, 8, 50)).squared().count(14)));
    public static final ConfiguredFeature<?, ?> LAVA_SPRING = register("lava_spring", Feature.SPRING.configured(AtumFeatures.LAVA_SPRING_CONFIG).decorated(FeatureDecorator.RANGE_VERY_BIASED.configured(new RangeDecoratorConfiguration(8, 16, 256)).squared().count(8)));

    private static <C extends FeatureConfiguration, F extends Feature<C>> F register(String name, F feature) {
        feature.setRegistryName(new ResourceLocation(Atum.MOD_ID, name));
        FEATURES.add(feature);
        return feature;
    }

    private static <FC extends FeatureConfiguration> ConfiguredFeature<FC, ?> register(String name, ConfiguredFeature<FC, ?> configuredFeature) {
        ResourceLocation location = new ResourceLocation(Atum.MOD_ID, name);
        return Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, location, configuredFeature);
    }

    @SubscribeEvent
    public static void registerFeature(RegistryEvent.Register<Feature<?>> event) {
        for (Feature<?> feature : FEATURES) {
            event.getRegistry().register(feature);
        }
    }
}