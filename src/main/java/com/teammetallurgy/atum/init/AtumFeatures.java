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
import net.minecraft.block.Blocks;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.blockplacer.ColumnBlockPlacer;
import net.minecraft.world.gen.blockplacer.DoublePlantBlockPlacer;
import net.minecraft.world.gen.blockplacer.SimpleBlockPlacer;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.foliageplacer.AcaciaFoliagePlacer;
import net.minecraft.world.gen.placement.*;
import net.minecraft.world.gen.trunkplacer.ForkyTrunkPlacer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AtumFeatures {
    private static final List<Feature<?>> FEATURES = new ArrayList<>();
    //Features
    public static final Feature<BlockStateFeatureConfig> SURFACE_LAVA_LAKE = register("surface_lava_lake", new LakeFeature(BlockStateFeatureConfig.field_236455_a_));
    public static final Feature<BaseTreeFeatureConfig> ATUM_TREE = register("atum_tree", new AtumTreeFeature(BaseTreeFeatureConfig.CODEC));
    public static final BonusCrateFeature BONUS_CRATE = register("bonus_crate", new BonusCrateFeature(NoFeatureConfig.field_236558_a_));
    public static final StartStructureFeature START_STRUCTURE = register("start_structure", new StartStructureFeature(NoFeatureConfig.field_236558_a_));
    public static final DirtyBoneFossilsFeature DIRTY_BONE_FOSSILS = register("dirty_bone_fossil", new DirtyBoneFossilsFeature(NoFeatureConfig.field_236558_a_));
    public static final LimestoneDungeonsFeature LIMESTONE_DUNGEONS = register("limestone_dungeon", new LimestoneDungeonsFeature(NoFeatureConfig.field_236558_a_));
    public static final DeadwoodFeature DEADWOOD_FEATURE = register("deadwood_tree", new DeadwoodFeature(NoFeatureConfig.field_236558_a_));
    public static final Feature<NoFeatureConfig> LIMESTONE_SPIKE = register("limestone_spike", new LimestoneSpikeFeature(NoFeatureConfig.field_236558_a_));
    public static final Feature<BlockClusterFeatureConfig> ANPUTS_FINGERS = register("anputs_fingers", new AnputsFingersFeature(BlockClusterFeatureConfig.field_236587_a_));
    public static final Feature<NoFeatureConfig> SAND_LAYER = register("sand_layer", new SandLayerFeature(NoFeatureConfig.field_236558_a_));

    //Feature Configs
    public static final BlockClusterFeatureConfig OASIS_GRASS_CONFIG = (new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(AtumBlocks.OASIS_GRASS.getDefaultState()), new SimpleBlockPlacer())).tries(64).build();
    public static final BlockClusterFeatureConfig DENSE_DRY_GRASS_CONFIG = (new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(AtumBlocks.DRY_GRASS.getDefaultState()), new SimpleBlockPlacer())).tries(30).build();
    public static final BlockClusterFeatureConfig SPARSE_DRY_GRASS_CONFIG = (new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(AtumBlocks.DRY_GRASS.getDefaultState()), new SimpleBlockPlacer())).tries(16).build();
    public static final BlockClusterFeatureConfig DRY_TALL_GRASS_CONFIG = (new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(AtumBlocks.TALL_DRY_GRASS.getDefaultState()), new DoublePlantBlockPlacer())).tries(20).build();
    public static final BlockClusterFeatureConfig SPARSE_TALL_GRASS_CONFIG = (new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(AtumBlocks.TALL_DRY_GRASS.getDefaultState()), new DoublePlantBlockPlacer())).tries(2).build();
    public static final BlockClusterFeatureConfig ANPUTS_FINGERS_CONFIG = (new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(AtumBlocks.ANPUTS_FINGERS.getDefaultState()), new SimpleBlockPlacer())).tries(10).build();
    public static final BlockClusterFeatureConfig PAPYRUS_CONFIG = (new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(AtumBlocks.PAPYRUS.getDefaultState()), new ColumnBlockPlacer(1, 2))).tries(8).xSpread(2).ySpread(0).zSpread(2).requiresWater().build();
    public static final BaseTreeFeatureConfig PALM_TREE_CONFIG = (new BaseTreeFeatureConfig.Builder(new SimpleBlockStateProvider(AtumBlocks.PALM_LOG.getDefaultState()), new SimpleBlockStateProvider(AtumBlocks.PALM_LEAVES.getDefaultState()), new PalmFoliagePlacer(0.1F), new PalmTrunkPlacer(4, 1, 5, 0.25F), new TwoLayerFeature(0, 0, 0))).setIgnoreVines().build();
    public static final BaseTreeFeatureConfig PALM_TREE_CONFIG_SAPLING = (new BaseTreeFeatureConfig.Builder(new SimpleBlockStateProvider(AtumBlocks.PALM_LOG.getDefaultState()), new SimpleBlockStateProvider(AtumBlocks.PALM_LEAVES.getDefaultState()), new PalmFoliagePlacer(0.1F), new PalmTrunkPlacer(4, 1, 5, 0.0F), new TwoLayerFeature(0, 0, 0))).setIgnoreVines().build();
    public static final BaseTreeFeatureConfig DEAD_PALM_TREE_CONFIG = (new BaseTreeFeatureConfig.Builder(new SimpleBlockStateProvider(AtumBlocks.DEADWOOD_LOG.getDefaultState().with(DeadwoodLogBlock.HAS_SCARAB, true)), new SimpleBlockStateProvider(AtumBlocks.DRY_LEAVES.getDefaultState()), new PalmFoliagePlacer(0.0F), new PalmTrunkPlacer(4, 1, 5, 0.0F), new TwoLayerFeature(0, 0, 0))).setIgnoreVines().build();
    public static final LiquidsConfig WATER_SPRING_CONFIG = new LiquidsConfig(Fluids.WATER.getDefaultState(), true, 4, 1, ImmutableSet.of(AtumBlocks.LIMESTONE));
    public static final LiquidsConfig LAVA_SPRING_CONFIG = new LiquidsConfig(Fluids.LAVA.getDefaultState(), true, 4, 1, ImmutableSet.of(AtumBlocks.LIMESTONE));
    public static final BlockClusterFeatureConfig SHRUB_CONFIG = (new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(AtumBlocks.SHRUB.getDefaultState()), new SimpleBlockPlacer())).tries(3).build();
    public static final BlockClusterFeatureConfig WEED_CONFIG = (new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(AtumBlocks.WEED.getDefaultState()), new SimpleBlockPlacer())).tries(3).build();

    //ConfiguredFeature
    public static final ConfiguredFeature<?, ?> SURFACE_LAVA_LAKE_CONFIGURED = register("surface_lava_lake", SURFACE_LAVA_LAKE.withConfiguration(new BlockStateFeatureConfig(Blocks.LAVA.getDefaultState())).withPlacement(Placement.LAVA_LAKE.configure(new ChanceConfig(80))));
    public static final ConfiguredFeature<?, ?> PALM_TREE = register("palm_tree", ATUM_TREE.withConfiguration(PALM_TREE_CONFIG).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).withPlacement(Placement.COUNT_EXTRA.configure(new AtSurfaceWithExtraConfig(9, 0.25F, 2))));
    public static final ConfiguredFeature<?, ?> DEAD_PALM_TREE = register("dead_palm_tree", ATUM_TREE.withConfiguration(DEAD_PALM_TREE_CONFIG).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).withPlacement(Placement.COUNT_EXTRA.configure(new AtSurfaceWithExtraConfig(1, 0.1F, 1))));
    public static final ConfiguredFeature<?, ?> ACACIA_TREE = register("acacia_tree", ATUM_TREE.withConfiguration(new BaseTreeFeatureConfig.Builder(new SimpleBlockStateProvider(Blocks.ACACIA_LOG.getDefaultState()), new SimpleBlockStateProvider(Blocks.ACACIA_LEAVES.getDefaultState()), new AcaciaFoliagePlacer(FeatureSpread.func_242252_a(2), FeatureSpread.func_242252_a(0)), new ForkyTrunkPlacer(5, 2, 2), new TwoLayerFeature(1, 0, 2)).setIgnoreVines().build()).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).withPlacement(Placement.COUNT_EXTRA.configure(new AtSurfaceWithExtraConfig(1, 0.1F, 1))));
    public static final ConfiguredFeature<?, ?> BONUS_CRATE_CONFIGURED = register("bonus_crate", BONUS_CRATE.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
    public static final ConfiguredFeature<?, ?> START_STRUCTURE_CONFIGURED = register("start_structure", START_STRUCTURE.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
    public static final ConfiguredFeature<?, ?> DIRTY_BONE_FOSSILS_CONFIGURED = register("dirty_bone_fossil", DIRTY_BONE_FOSSILS.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).chance(AtumConfig.WORLD_GEN.fossilsChance.get()));
    public static final ConfiguredFeature<?, ?> LIMESTONE_DUNGEONS_CONFIGURED = register("limestone_dungeon", LIMESTONE_DUNGEONS.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).range(256).square().func_242731_b(AtumConfig.WORLD_GEN.dungeonChance.get()));
    public static final ConfiguredFeature<?, ?> DEADWOOD_3_01_1 = register("deadwood_tree_3_01_1", DEADWOOD_FEATURE.withConfiguration(NoFeatureConfig.field_236559_b_).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).withPlacement(Placement.COUNT_EXTRA.configure(new AtSurfaceWithExtraConfig(3, 0.1F, 1))));
    public static final ConfiguredFeature<?, ?> DEADWOOD_20_025_3 = register("deadwood_tree_20_025_3", DEADWOOD_FEATURE.withConfiguration(NoFeatureConfig.field_236559_b_).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).withPlacement(Placement.COUNT_EXTRA.configure(new AtSurfaceWithExtraConfig(20, 0.25F, 3))));
    public static final ConfiguredFeature<?, ?> DEADWOOD_0_02_1 = register("deadwood_tree_0_02_1", DEADWOOD_FEATURE.withConfiguration(NoFeatureConfig.field_236559_b_).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).withPlacement(Placement.COUNT_EXTRA.configure(new AtSurfaceWithExtraConfig(0, 0.2F, 1))));
    public static final ConfiguredFeature<?, ?> DEADWOOD_0_01_1 = register("deadwood_tree_0_01_1", DEADWOOD_FEATURE.withConfiguration(NoFeatureConfig.field_236559_b_).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).withPlacement(Placement.COUNT_EXTRA.configure(new AtSurfaceWithExtraConfig(0, 0.1F, 1))));
    public static final ConfiguredFeature<?, ?> DEADWOOD_0_08_1 = register("deadwood_tree_0_08_1", DEADWOOD_FEATURE.withConfiguration(NoFeatureConfig.field_236559_b_).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).withPlacement(Placement.COUNT_EXTRA.configure(new AtSurfaceWithExtraConfig(0, 0.8F, 1))));
    public static final ConfiguredFeature<?, ?> DEADWOOD_0_025_1 = register("deadwood_tree_0_025_1", DEADWOOD_FEATURE.withConfiguration(NoFeatureConfig.field_236559_b_).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).withPlacement(Placement.COUNT_EXTRA.configure(new AtSurfaceWithExtraConfig(0, 0.25F, 1))));
    public static final ConfiguredFeature<?, ?> LIMESTONE_SPIKE_CONFIGURED = register("limestone_spike", LIMESTONE_SPIKE.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).func_242731_b(3));
    public static final ConfiguredFeature<?, ?> ANPUTS_FINGERS_CONFIGURED = register("anputs_fingers", ANPUTS_FINGERS.withConfiguration(ANPUTS_FINGERS_CONFIG).withPlacement(Features.Placements.PATCH_PLACEMENT).withPlacement(Placement.HEIGHTMAP_WORLD_SURFACE.configure(IPlacementConfig.NO_PLACEMENT_CONFIG)).func_242731_b(16));
    public static final ConfiguredFeature<?, ?> SAND_LAYER_CONFIGURED = register("sand_layer", SAND_LAYER.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
    public static final ConfiguredFeature<?, ?> COAL_ORE = register("coal_ore", Feature.ORE.withConfiguration(new OreFeatureConfig(AtumDefaultFeatures.NATURAL_STONE, AtumBlocks.COAL_ORE.getDefaultState(), AtumConfig.WORLD_GEN.coalVeinSize.get())).range(AtumConfig.WORLD_GEN.coalMaxHeight.get()).square().func_242731_b(AtumConfig.WORLD_GEN.coalCount.get()));
    public static final ConfiguredFeature<?, ?> IRON_ORE = register("iron_ore", Feature.ORE.withConfiguration(new OreFeatureConfig(AtumDefaultFeatures.NATURAL_STONE, AtumBlocks.IRON_ORE.getDefaultState(), AtumConfig.WORLD_GEN.ironVeinSize.get())).range(AtumConfig.WORLD_GEN.ironMaxHeight.get()).square().func_242731_b(AtumConfig.WORLD_GEN.ironCount.get()));
    public static final ConfiguredFeature<?, ?> GOLD_ORE = register("gold_ore", Feature.ORE.withConfiguration(new OreFeatureConfig(AtumDefaultFeatures.NATURAL_STONE, AtumBlocks.GOLD_ORE.getDefaultState(), AtumConfig.WORLD_GEN.goldVeinSize.get())).range(AtumConfig.WORLD_GEN.goldMaxHeight.get()).square().func_242731_b(AtumConfig.WORLD_GEN.goldCount.get()));
    public static final ConfiguredFeature<?, ?> REDSTONE_ORE = register("redstone_ore", Feature.ORE.withConfiguration(new OreFeatureConfig(AtumDefaultFeatures.NATURAL_STONE, AtumBlocks.REDSTONE_ORE.getDefaultState(), AtumConfig.WORLD_GEN.redstoneVeinSize.get())).range(AtumConfig.WORLD_GEN.redstoneMaxHeight.get()).square().func_242731_b(AtumConfig.WORLD_GEN.redstoneCount.get()));
    public static final ConfiguredFeature<?, ?> DIAMOND_ORE = register("diamond_ore", Feature.ORE.withConfiguration(new OreFeatureConfig(AtumDefaultFeatures.NATURAL_STONE, AtumBlocks.DIAMOND_ORE.getDefaultState(), AtumConfig.WORLD_GEN.diamondVeinSize.get())).range(AtumConfig.WORLD_GEN.diamondMaxHeight.get()).square().func_242731_b(AtumConfig.WORLD_GEN.diamondCount.get()));
    public static final ConfiguredFeature<?, ?> LAPIS_ORE = register("lapis_ore", Feature.ORE.withConfiguration(new OreFeatureConfig(AtumDefaultFeatures.NATURAL_STONE, AtumBlocks.LAPIS_ORE.getDefaultState(), AtumConfig.WORLD_GEN.lapisVeinSize.get())).withPlacement(Placement.DEPTH_AVERAGE.configure(new DepthAverageConfig(AtumConfig.WORLD_GEN.lapisBaseline.get(), AtumConfig.WORLD_GEN.lapisSpread.get()))));
    public static final ConfiguredFeature<?, ?> EMERALD_ORE = register("emerald_ore", Feature.EMERALD_ORE.withConfiguration(new ReplaceBlockConfig(AtumBlocks.LIMESTONE.getDefaultState(), AtumBlocks.EMERALD_ORE.getDefaultState())).withPlacement(Placement.EMERALD_ORE.configure(IPlacementConfig.NO_PLACEMENT_CONFIG)));
    public static final ConfiguredFeature<?, ?> KHNUMITE_RAW = register("khnumite_raw", Feature.ORE.withConfiguration(new OreFeatureConfig(AtumDefaultFeatures.NATURAL_STONE, AtumBlocks.KHNUMITE_RAW.getDefaultState(), AtumConfig.WORLD_GEN.khnumiteVeinSize.get())).range(AtumConfig.WORLD_GEN.khnumiteMaxHeight.get()).square().func_242731_b(AtumConfig.WORLD_GEN.khnumiteCount.get()));
    public static final ConfiguredFeature<?, ?> BONE_ORE = register("bone_ore", Feature.ORE.withConfiguration(new OreFeatureConfig(AtumDefaultFeatures.NATURAL_STONE, AtumBlocks.BONE_ORE.getDefaultState(), AtumConfig.WORLD_GEN.boneOreVeinSize.get())).range(AtumConfig.WORLD_GEN.boneOreMaxHeight.get()).square().func_242731_b(AtumConfig.WORLD_GEN.boneOreCount.get()));
    public static final ConfiguredFeature<?, ?> RELIC_ORE = register("relic_ore", Feature.ORE.withConfiguration(new OreFeatureConfig(AtumDefaultFeatures.NATURAL_STONE, AtumBlocks.RELIC_ORE.getDefaultState(), AtumConfig.WORLD_GEN.relicOreVeinSize.get())).range(AtumConfig.WORLD_GEN.relicOreMaxHeight.get()).square().func_242731_b(AtumConfig.WORLD_GEN.relicOreCount.get()));
    public static final ConfiguredFeature<?, ?> NEBU_ORE = register("nebu_ore", Feature.ORE.withConfiguration(new OreFeatureConfig(AtumDefaultFeatures.NATURAL_STONE, AtumBlocks.NEBU_ORE.getDefaultState(), AtumConfig.WORLD_GEN.nebuVeinSize.get())).range(AtumConfig.WORLD_GEN.nebuMaxHeight.get()).square().func_242731_b(AtumConfig.WORLD_GEN.nebuCount.get()));
    public static final ConfiguredFeature<?, ?> SAND = register("sand", Feature.ORE.withConfiguration(new OreFeatureConfig(AtumDefaultFeatures.NATURAL_STONE, AtumBlocks.SAND.getDefaultState(), AtumConfig.WORLD_GEN.sandVeinSize.get())).range(AtumConfig.WORLD_GEN.sandMaxHeight.get()).square().func_242731_b(AtumConfig.WORLD_GEN.sandCount.get()));
    public static final ConfiguredFeature<?, ?> LIMESTONE_GRAVEL = register("limestone_gravel", Feature.ORE.withConfiguration(new OreFeatureConfig(AtumDefaultFeatures.NATURAL_STONE, AtumBlocks.LIMESTONE_GRAVEL.getDefaultState(), AtumConfig.WORLD_GEN.limestoneGravelVeinSize.get())).range(AtumConfig.WORLD_GEN.limestoneGravelMaxHeight.get()).square().func_242731_b(AtumConfig.WORLD_GEN.limestoneGravelCount.get()));
    public static final ConfiguredFeature<?, ?> MARL = register("marl", Feature.ORE.withConfiguration(new OreFeatureConfig(AtumDefaultFeatures.NATURAL_STONE, AtumBlocks.MARL.getDefaultState(), AtumConfig.WORLD_GEN.marlVeinSize.get())).range(AtumConfig.WORLD_GEN.marlMaxHeight.get()).square().func_242731_b(AtumConfig.WORLD_GEN.marlCount.get()));
    public static final ConfiguredFeature<?, ?> MARL_DRIED_RIVER = register("marl_dried_river", Feature.ORE.withConfiguration(new OreFeatureConfig(AtumDefaultFeatures.LIMESTONE_CRACKED, AtumBlocks.MARL.getDefaultState(), 32)).withPlacement(Placement.RANGE.configure(new TopSolidRangeConfig(55, 0, 67))).func_242731_b(90));
    public static final ConfiguredFeature<?, ?> ALABASTER = register("alabaster", Feature.ORE.withConfiguration(new OreFeatureConfig(AtumDefaultFeatures.NATURAL_STONE, AtumBlocks.ALABASTER.getDefaultState(), AtumConfig.WORLD_GEN.alabasterVeinSize.get())).range(AtumConfig.WORLD_GEN.alabasterMaxHeight.get()).square().func_242731_b(AtumConfig.WORLD_GEN.alabasterCount.get()));
    public static final ConfiguredFeature<?, ?> PORPHYRY = register("porphyry", Feature.ORE.withConfiguration(new OreFeatureConfig(AtumDefaultFeatures.NATURAL_STONE, AtumBlocks.PORPHYRY.getDefaultState(), AtumConfig.WORLD_GEN.porphyryVeinSize.get())).range(AtumConfig.WORLD_GEN.porphyryMaxHeight.get()).square().func_242731_b(AtumConfig.WORLD_GEN.porphyryCount.get()));
    public static final ConfiguredFeature<?, ?> LIMESTONE_INFESTED = register("limestone_infested", Feature.ORE.withConfiguration(new OreFeatureConfig(AtumDefaultFeatures.NATURAL_STONE, AtumBlocks.LIMESTONE.getDefaultState().with(LimestoneBlock.HAS_SCARAB, true), 10)).withPlacement(Placement.RANGE.configure(new TopSolidRangeConfig(0, 11, 80)).square().func_242731_b(8)));
    public static final ConfiguredFeature<?, ?> SHRUB = register("shrub", Feature.RANDOM_PATCH.withConfiguration(AtumFeatures.SHRUB_CONFIG).withPlacement(Features.Placements.PATCH_PLACEMENT).func_242731_b(AtumConfig.WORLD_GEN.shrubFrequency.get()));
    public static final ConfiguredFeature<?, ?> WEED = register("weed", Feature.RANDOM_PATCH.withConfiguration(AtumFeatures.WEED_CONFIG).withPlacement(Features.Placements.PATCH_PLACEMENT).func_242731_b(AtumConfig.WORLD_GEN.shrubFrequency.get()));
    public static final ConfiguredFeature<?, ?> PAPYRUS = register("papyrus", Feature.RANDOM_PATCH.withConfiguration(AtumFeatures.PAPYRUS_CONFIG).withPlacement(Features.Placements.PATCH_PLACEMENT).func_242731_b(3));
    public static final ConfiguredFeature<?, ?> OASIS_GRASS = register("oasis_grass", Feature.RANDOM_PATCH.withConfiguration(AtumFeatures.OASIS_GRASS_CONFIG).withPlacement(Features.Placements.PATCH_PLACEMENT).func_242731_b(16));
    public static final ConfiguredFeature<?, ?> LILY_PAD = register("lily_pad", Feature.RANDOM_PATCH.withConfiguration((new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(Blocks.LILY_PAD.getDefaultState()), SimpleBlockPlacer.PLACER)).tries(7).build()).withPlacement(Features.Placements.PATCH_PLACEMENT).func_242731_b(2));
    public static final ConfiguredFeature<?, ?> DRY_TALL_GRASS = register("dry_tall_grass", Feature.RANDOM_PATCH.withConfiguration(AtumFeatures.DRY_TALL_GRASS_CONFIG).withPlacement(Features.Placements.VEGETATION_PLACEMENT).withPlacement(Features.Placements.FLOWER_TALL_GRASS_PLACEMENT).square().withPlacement(Placement.COUNT_NOISE.configure(new NoiseDependant(-0.8D, 0, 7))));
    public static final ConfiguredFeature<?, ?> SPARSE_TALL_GRASS = register("sparse_tall_grass", Feature.RANDOM_PATCH.withConfiguration(AtumFeatures.SPARSE_TALL_GRASS_CONFIG).withPlacement(Features.Placements.VEGETATION_PLACEMENT).withPlacement(Features.Placements.FLOWER_TALL_GRASS_PLACEMENT).square().withPlacement(Placement.COUNT_NOISE.configure(new NoiseDependant(-0.8D, 0, 7))));
    public static final ConfiguredFeature<?, ?> SPARSE_DRY_GRASS_SPREAD_5 = register("dry_grass_spread_5", Feature.RANDOM_PATCH.withConfiguration(AtumFeatures.SPARSE_DRY_GRASS_CONFIG).withPlacement(Features.Placements.PATCH_PLACEMENT).func_242731_b(5));
    public static final ConfiguredFeature<?, ?> SPARSE_DRY_GRASS_NOISE_08_2_3 = register("dry_grass_spread_08_2_3", Feature.RANDOM_PATCH.withConfiguration(AtumFeatures.SPARSE_DRY_GRASS_CONFIG).withPlacement(Features.Placements.PATCH_PLACEMENT).withPlacement(Placement.COUNT_NOISE.configure(new NoiseDependant(-0.8D, 2, 3))));
    public static final ConfiguredFeature<?, ?> DENSE_DRY_GRASS_NOISE_08_5_10 = register("dry_grass_spread_08_5_10", Feature.RANDOM_PATCH.withConfiguration(AtumFeatures.DENSE_DRY_GRASS_CONFIG).withPlacement(Features.Placements.PATCH_PLACEMENT).withPlacement(Features.Placements.PATCH_PLACEMENT).withPlacement(Placement.COUNT_NOISE.configure(new NoiseDependant(-0.8D, 5, 10))));
    public static final ConfiguredFeature<?, ?> WATER_SPRING = register("water_spring", Feature.SPRING_FEATURE.withConfiguration(AtumFeatures.WATER_SPRING_CONFIG).withPlacement(Placement.RANGE_BIASED.configure(new TopSolidRangeConfig(8, 8, 50)).square().func_242731_b(14)));
    public static final ConfiguredFeature<?, ?> LAVA_SPRING = register("lava_spring", Feature.SPRING_FEATURE.withConfiguration(AtumFeatures.LAVA_SPRING_CONFIG).withPlacement(Placement.RANGE_VERY_BIASED.configure(new TopSolidRangeConfig(8, 16, 256)).square().func_242731_b(8)));

    private static <C extends IFeatureConfig, F extends Feature<C>> F register(String name, F feature) {
        feature.setRegistryName(new ResourceLocation(Atum.MOD_ID, name));
        FEATURES.add(feature);
        return feature;
    }

    private static <FC extends IFeatureConfig> ConfiguredFeature<FC, ?> register(String name, ConfiguredFeature<FC, ?> configuredFeature) {
        ResourceLocation location = new ResourceLocation(Atum.MOD_ID, name);
        return Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, location, configuredFeature);
    }

    @SubscribeEvent
    public static void registerFeature(RegistryEvent.Register<Feature<?>> event) {
        for (Feature<?> feature : FEATURES) {
            event.getRegistry().register(feature);
        }
    }
}