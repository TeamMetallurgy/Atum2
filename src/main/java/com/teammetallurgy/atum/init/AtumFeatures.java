package com.teammetallurgy.atum.init;

import com.google.common.collect.ImmutableSet;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.blocks.stone.limestone.LimestoneBlock;
import com.teammetallurgy.atum.blocks.wood.DeadwoodLogBlock;
import com.teammetallurgy.atum.misc.AtumConfig;
import com.teammetallurgy.atum.world.gen.carver.AtumCarvers;
import com.teammetallurgy.atum.world.gen.feature.*;
import com.teammetallurgy.atum.world.gen.feature.config.DoubleBlockStateFeatureConfig;
import com.teammetallurgy.atum.world.gen.feature.config.PalmConfig;
import com.teammetallurgy.atum.world.gen.structure.girafitomb.GirafiTombStructure;
import com.teammetallurgy.atum.world.gen.structure.lighthouse.LighthouseStructure;
import com.teammetallurgy.atum.world.gen.structure.pyramid.PyramidStructure;
import com.teammetallurgy.atum.world.gen.structure.ruins.RuinStructure;
import com.teammetallurgy.atum.world.gen.structure.tomb.TombStructure;
import net.minecraft.block.Blocks;
import net.minecraft.block.pattern.BlockMatcher;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.blockplacer.ColumnBlockPlacer;
import net.minecraft.world.gen.blockplacer.SimpleBlockPlacer;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.foliageplacer.AcaciaFoliagePlacer;
import net.minecraft.world.gen.placement.*;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

import static net.minecraft.world.gen.GenerationStage.Decoration.*;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AtumFeatures {
    private static final List<Feature<?>> FEATURES = new ArrayList<>();
    //Features
    public static final Feature<DoubleBlockStateFeatureConfig> OASIS_POND = register("oasis_pond", new OasisPondFeature(DoubleBlockStateFeatureConfig::deserializeDouble));
    public static final Feature<BlockStateFeatureConfig> SURFACE_LAVA_LAKE = register("surface_lava_lake", new LakeFeature(BlockStateFeatureConfig::deserialize));
    public static final Feature<PalmConfig> PALM_TREE = register("palm_tree", new PalmFeature(PalmConfig::deserializePalm));
    public static final BonusCrateFeature BONUS_CRATE = register("bonus_crate", new BonusCrateFeature(NoFeatureConfig::deserialize));
    public static final StartStructureFeature START_STRUCTURE = register("start_structure", new StartStructureFeature(NoFeatureConfig::deserialize));
    public static final DirtyBoneFossilsFeature DIRTY_BONE_FOSSILS = register("dirty_bone_fossil", new DirtyBoneFossilsFeature(NoFeatureConfig::deserialize));
    public static final LimestoneDungeonsFeature LIMESTONE_DUNGEONS = register("limestone_dungeon", new LimestoneDungeonsFeature(NoFeatureConfig::deserialize));
    public static final DeadwoodFeature DEADWOOD_FEATURE = register("deadwood_tree", new DeadwoodFeature(TreeFeatureConfig::func_227338_a_));
    public static final Feature<NoFeatureConfig> LIMESTONE_SPIKE = register("limestone_spike", new LimestoneSpikeFeature(NoFeatureConfig::deserialize));

    //Structures
    public static final Structure<NoFeatureConfig> GIRAFI_TOMB = register("girafi_tomb", new GirafiTombStructure(NoFeatureConfig::deserialize));
    public static final Structure<NoFeatureConfig> LIGHTHOUSE = register("lighthouse", new LighthouseStructure(NoFeatureConfig::deserialize));
    public static final Structure<NoFeatureConfig> TOMB = register("tomb", new TombStructure(NoFeatureConfig::deserialize));
    public static final Structure<NoFeatureConfig> RUIN = register("ruin", new RuinStructure(NoFeatureConfig::deserialize));
    public static final Structure<NoFeatureConfig> PYRAMID = register("pyramid", new PyramidStructure(NoFeatureConfig::deserialize));

    //Feature Configs
    public static final BlockClusterFeatureConfig OASIS_GRASS_CONFIG = (new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(AtumBlocks.OASIS_GRASS.getDefaultState()), new SimpleBlockPlacer())).tries(30).build();
    public static final BlockClusterFeatureConfig DEAD_GRASS_CONFIG = (new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(AtumBlocks.DEAD_GRASS.getDefaultState()), new SimpleBlockPlacer())).tries(2).build();
    public static final BlockClusterFeatureConfig ANPUTS_FINGERS_CONFIG = (new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(AtumBlocks.ANPUTS_FINGERS.getDefaultState()), new SimpleBlockPlacer())).tries(64).func_227317_b_().build();
    public static final BlockClusterFeatureConfig PAPYRUS_CONFIG = (new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(AtumBlocks.PAPYRUS.getDefaultState()), new ColumnBlockPlacer(1, 2))).tries(124).xSpread(1).ySpread(0).zSpread(1).func_227317_b_().requiresWater().build();
    public static final PalmConfig PALM_TREE_CONFIG = (new PalmConfig.Builder(new SimpleBlockStateProvider(AtumBlocks.PALM_LOG.getDefaultState()), new SimpleBlockStateProvider(AtumBlocks.PALM_LEAVES.getDefaultState()), new AcaciaFoliagePlacer(0, 0))).baseHeight(5).heightRandA(2).heightRandB(1).trunkHeight(1).ignoreVines().date(0.1D).ophidianTongue(0.6D).setSapling((IPlantable) AtumBlocks.PALM_SAPLING).build();
    public static final PalmConfig PALM_TREE_CONFIG_SAPLING = (new PalmConfig.Builder(new SimpleBlockStateProvider(AtumBlocks.PALM_LOG.getDefaultState()), new SimpleBlockStateProvider(AtumBlocks.PALM_LEAVES.getDefaultState()), new AcaciaFoliagePlacer(0, 0))).baseHeight(5).heightRandA(2).heightRandB(1).trunkHeight(1).ignoreVines().date(0.1D).ophidianTongue(0.0D).setSapling((IPlantable) AtumBlocks.PALM_SAPLING).build();
    public static final PalmConfig DEAD_PALM_TREE_CONFIG = (new PalmConfig.Builder(new SimpleBlockStateProvider(AtumBlocks.DEADWOOD_LOG.getDefaultState().with(DeadwoodLogBlock.HAS_SCARAB, true)), new SimpleBlockStateProvider(AtumBlocks.DEADWOOD_LEAVES.getDefaultState()), new AcaciaFoliagePlacer(0, 0))).baseHeight(5).heightRandA(2).heightRandB(1).trunkHeight(1).ignoreVines().date(0.0D).ophidianTongue(0.0D).setSapling(null).build();
    public static final LiquidsConfig WATER_SPRING_CONFIG = new LiquidsConfig(Fluids.WATER.getDefaultState(), true, 4, 1, ImmutableSet.of(AtumBlocks.LIMESTONE));
    public static final LiquidsConfig LAVA_SPRING_CONFIG = new LiquidsConfig(Fluids.LAVA.getDefaultState(), true, 4, 1, ImmutableSet.of(AtumBlocks.LIMESTONE));
    public static final BlockClusterFeatureConfig SHRUB_CONFIG = (new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(AtumBlocks.SHRUB.getDefaultState()), new SimpleBlockPlacer())).tries(3).build();
    public static final BlockClusterFeatureConfig WEED_CONFIG = (new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(AtumBlocks.WEED.getDefaultState()), new SimpleBlockPlacer())).tries(3).build();

    private static <C extends IFeatureConfig, F extends Feature<C>> F register(String name, F feature) {
        feature.setRegistryName(new ResourceLocation(Atum.MOD_ID, name));
        FEATURES.add(feature);
        return feature;
    }

    @SubscribeEvent
    public static void registerFeature(RegistryEvent.Register<Feature<?>> event) {
        for (Feature<?> feature : FEATURES) {
            event.getRegistry().register(feature);
        }

    }

    public static class Default {
        public static final OreFeatureConfig.FillerBlockType NATURAL_STONE = OreFeatureConfig.FillerBlockType.create("ATUM_NATURAL_STONE", "atum_natural_stone", new BlockMatcher(AtumBlocks.LIMESTONE));

        public static void addCarvers(Biome biome) {
            biome.addCarver(GenerationStage.Carving.AIR, Biome.createCarver(AtumCarvers.CAVE, new ProbabilityConfig(0.14285715F)));
            biome.addCarver(GenerationStage.Carving.AIR, Biome.createCarver(AtumCarvers.CANYON, new ProbabilityConfig(0.02F)));
        }

        public static void addSprings(Biome biome) {
            biome.addFeature(VEGETAL_DECORATION, Feature.SPRING_FEATURE.withConfiguration(WATER_SPRING_CONFIG).withPlacement(Placement.COUNT_BIASED_RANGE.configure(new CountRangeConfig(14, 8, 8, 50))));
            biome.addFeature(VEGETAL_DECORATION, Feature.SPRING_FEATURE.withConfiguration(LAVA_SPRING_CONFIG).withPlacement(Placement.COUNT_VERY_BIASED_RANGE.configure(new CountRangeConfig(8, 8, 16, 256))));
        }

        public static void addLavaLakes(Biome biome) { //TODO. Not working?
            biome.addFeature(LOCAL_MODIFICATIONS, SURFACE_LAVA_LAKE.withConfiguration(new BlockStateFeatureConfig(Blocks.LAVA.getDefaultState())).withPlacement(Placement.LAVA_LAKE.configure(new ChanceConfig(80000))));
        }

        public static void addMaterialPockets(Biome biome) {
            biome.addFeature(UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(NATURAL_STONE, AtumBlocks.SAND.getDefaultState(), 28)).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(14, 0, 0, 256))));
            biome.addFeature(UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(NATURAL_STONE, AtumBlocks.LIMESTONE_GRAVEL.getDefaultState(), 32)).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(10, 0, 0, 256))));
            biome.addFeature(UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(NATURAL_STONE, AtumBlocks.MARL.getDefaultState(), 14)).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(8, 0, 0, 50))));
        }

        public static void addStoneVariants(Biome biome) {
            biome.addFeature(UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(NATURAL_STONE, AtumBlocks.ALABASTER.getDefaultState(), 30)).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(10, 0, 0, 60))));
            biome.addFeature(UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(NATURAL_STONE, AtumBlocks.PORPHYRY.getDefaultState(), 30)).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(10, 0, 0, 60))));
        }

        public static void addOres(Biome biome) {
            //Vanilla based ores
            biome.addFeature(UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(NATURAL_STONE, AtumBlocks.COAL_ORE.getDefaultState(), 17)).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(20, 0, 0, 128))));
            biome.addFeature(UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(NATURAL_STONE, AtumBlocks.IRON_ORE.getDefaultState(), 9)).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(20, 0, 0, 64))));
            biome.addFeature(UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(NATURAL_STONE, AtumBlocks.GOLD_ORE.getDefaultState(), 9)).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(2, 0, 0, 32))));
            biome.addFeature(UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(NATURAL_STONE, AtumBlocks.REDSTONE_ORE.getDefaultState(), 8)).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(8, 0, 0, 16))));
            biome.addFeature(UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(NATURAL_STONE, AtumBlocks.DIAMOND_ORE.getDefaultState(), 8)).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(1, 0, 0, 16))));
            biome.addFeature(UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(NATURAL_STONE, AtumBlocks.LAPIS_ORE.getDefaultState(), 7)).withPlacement(Placement.COUNT_DEPTH_AVERAGE.configure(new DepthAverageConfig(1, 16, 16))));

            //Atum ores
            biome.addFeature(UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(NATURAL_STONE, AtumBlocks.KHNUMITE_RAW.getDefaultState(), 6)).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(4, 0, 0, 20))));
            biome.addFeature(UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(NATURAL_STONE, AtumBlocks.BONE_ORE.getDefaultState(), 9)).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(12, 0, 0, 128))));
            biome.addFeature(UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(NATURAL_STONE, AtumBlocks.RELIC_ORE.getDefaultState(), 5)).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(4, 0, 0, 64))));
        }

        public static void addEmeraldOre(Biome biome) {
            biome.addFeature(UNDERGROUND_ORES, Feature.EMERALD_ORE.withConfiguration(new ReplaceBlockConfig(AtumBlocks.LIMESTONE.getDefaultState(), AtumBlocks.EMERALD_ORE.getDefaultState())).withPlacement(Placement.EMERALD_ORE.configure(IPlacementConfig.NO_PLACEMENT_CONFIG)));
        }

        public static void addInfestedLimestone(Biome biome) {
            biome.addFeature(UNDERGROUND_DECORATION, Feature.ORE.withConfiguration(new OreFeatureConfig(NATURAL_STONE, AtumBlocks.LIMESTONE.getDefaultState().with(LimestoneBlock.HAS_SCARAB, true), 10)).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(8, 0, 11, 80))));
        }

        public static void addShrubs(Biome biome) {
            biome.addFeature(VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration(SHRUB_CONFIG).withPlacement(Placement.COUNT_HEIGHTMAP_DOUBLE.configure(new FrequencyConfig(1))));
            biome.addFeature(VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration(WEED_CONFIG).withPlacement(Placement.COUNT_HEIGHTMAP_DOUBLE.configure(new FrequencyConfig(1))));
        }

        public static void addFossils(Biome biome) {
            biome.addFeature(UNDERGROUND_DECORATION, DIRTY_BONE_FOSSILS.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.CHANCE_PASSTHROUGH.configure(new ChanceConfig(64))));
        }

        public static void addDungeon(Biome biome) {
            biome.addFeature(UNDERGROUND_STRUCTURES, LIMESTONE_DUNGEONS.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.DUNGEONS.configure(new ChanceConfig(8))));
        }

        public static void addDeadwoodTrees(Biome biome, int count, float extraChance, int extraCount) {
            biome.addFeature(VEGETAL_DECORATION, DEADWOOD_FEATURE.withConfiguration((new BaseTreeFeatureConfig.Builder(new SimpleBlockStateProvider(AtumBlocks.DEADWOOD_LOG.getDefaultState().with(DeadwoodLogBlock.HAS_SCARAB, true)), new SimpleBlockStateProvider(AtumBlocks.DEADWOOD_BRANCH.getDefaultState()))).build()).withPlacement(Placement.COUNT_EXTRA_HEIGHTMAP.configure(new AtSurfaceWithExtraConfig(count, extraChance, extraCount))));
        }

        public static void addTomb(Biome biome) {
            biome.addStructure(AtumFeatures.TOMB.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
            biome.addFeature(UNDERGROUND_STRUCTURES, AtumFeatures.TOMB.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
        }

        public static void addPyramid(Biome biome) {
            if (AtumConfig.WORLD_GEN.pyramidEnabled.get()) {
                biome.addStructure(AtumFeatures.PYRAMID.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
                biome.addFeature(SURFACE_STRUCTURES, AtumFeatures.PYRAMID.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
            }
        }
    }
}