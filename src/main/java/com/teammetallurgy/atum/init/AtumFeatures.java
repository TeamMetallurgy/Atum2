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
import com.teammetallurgy.atum.world.gen.structure.mineshaft.AtumMineshaftConfig;
import com.teammetallurgy.atum.world.gen.structure.mineshaft.AtumMineshaftStructure;
import com.teammetallurgy.atum.world.gen.structure.pyramid.PyramidStructure;
import com.teammetallurgy.atum.world.gen.structure.ruins.RuinStructure;
import com.teammetallurgy.atum.world.gen.structure.tomb.TombStructure;
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
import java.util.Random;

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
    public static final DeadwoodFeature DEADWOOD_FEATURE = register("deadwood_tree", new DeadwoodFeature(TreeFeatureConfig::deserializeFoliage));
    public static final Feature<NoFeatureConfig> LIMESTONE_SPIKE = register("limestone_spike", new LimestoneSpikeFeature(NoFeatureConfig::deserialize));
    public static final Feature<BlockClusterFeatureConfig> ANPUTS_FINGERS = register("anputs_fingers", new AnputsFingersFeature(BlockClusterFeatureConfig::deserialize));

    //Structures
    public static final Structure<NoFeatureConfig> GIRAFI_TOMB = register("girafi_tomb", new GirafiTombStructure(NoFeatureConfig::deserialize));
    public static final Structure<NoFeatureConfig> LIGHTHOUSE = register("lighthouse", new LighthouseStructure(NoFeatureConfig::deserialize));
    public static final Structure<NoFeatureConfig> TOMB = register("tomb", new TombStructure(NoFeatureConfig::deserialize));
    public static final Structure<NoFeatureConfig> RUIN = register("ruin", new RuinStructure(NoFeatureConfig::deserialize));
    public static final Structure<NoFeatureConfig> PYRAMID = register("pyramid", new PyramidStructure(NoFeatureConfig::deserialize));
    public static final Structure<AtumMineshaftConfig> MINESHAFT = register("mineshaft", new AtumMineshaftStructure(AtumMineshaftConfig::deserialize));

    //Feature Configs
    public static final BlockClusterFeatureConfig OASIS_GRASS_CONFIG = (new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(AtumBlocks.OASIS_GRASS.getDefaultState()), new SimpleBlockPlacer())).tries(30).build();
    public static final BlockClusterFeatureConfig DEAD_GRASS_CONFIG = (new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(AtumBlocks.DEAD_GRASS.getDefaultState()), new SimpleBlockPlacer())).tries(2).build();
    public static final BlockClusterFeatureConfig ANPUTS_FINGERS_CONFIG = (new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(AtumBlocks.ANPUTS_FINGERS.getDefaultState()), new SimpleBlockPlacer())).build();
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

        public static void addMaterialPockets(Biome biome) {
            if (AtumConfig.WORLD_GEN.sandCount.get() > 0) {
                biome.addFeature(UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(NATURAL_STONE, AtumBlocks.SAND.getDefaultState(), AtumConfig.WORLD_GEN.sandVeinSize.get())).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(AtumConfig.WORLD_GEN.sandCount.get(), AtumConfig.WORLD_GEN.sandBottomOffset.get(), AtumConfig.WORLD_GEN.sandTopOffset.get(), AtumConfig.WORLD_GEN.sandMaxHeight.get()))));
            }
            if (AtumConfig.WORLD_GEN.limestoneGravelCount.get() > 0) {
                biome.addFeature(UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(NATURAL_STONE, AtumBlocks.LIMESTONE_GRAVEL.getDefaultState(), AtumConfig.WORLD_GEN.limestoneGravelVeinSize.get())).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(AtumConfig.WORLD_GEN.limestoneGravelCount.get(), AtumConfig.WORLD_GEN.limestoneGravelBottomOffset.get(), AtumConfig.WORLD_GEN.limestoneGravelTopOffset.get(), AtumConfig.WORLD_GEN.limestoneGravelMaxHeight.get()))));
            }
            if (AtumConfig.WORLD_GEN.marlCount.get() > 0) {
                biome.addFeature(UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(NATURAL_STONE, AtumBlocks.MARL.getDefaultState(), AtumConfig.WORLD_GEN.marlVeinSize.get())).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(AtumConfig.WORLD_GEN.marlCount.get(), AtumConfig.WORLD_GEN.marlBottomOffset.get(), AtumConfig.WORLD_GEN.marlTopOffset.get(), AtumConfig.WORLD_GEN.marlMaxHeight.get()))));
            }
        }

        public static void addStoneVariants(Biome biome) {
            if (AtumConfig.WORLD_GEN.alabasterCount.get() > 0) {
                biome.addFeature(UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(NATURAL_STONE, AtumBlocks.ALABASTER.getDefaultState(), AtumConfig.WORLD_GEN.alabasterVeinSize.get())).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(AtumConfig.WORLD_GEN.alabasterCount.get(), AtumConfig.WORLD_GEN.alabasterBottomOffset.get(), AtumConfig.WORLD_GEN.alabasterTopOffset.get(), AtumConfig.WORLD_GEN.alabasterMaxHeight.get()))));
            }
            if (AtumConfig.WORLD_GEN.porphyryCount.get() > 0) {
                biome.addFeature(UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(NATURAL_STONE, AtumBlocks.PORPHYRY.getDefaultState(), AtumConfig.WORLD_GEN.porphyryVeinSize.get())).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(AtumConfig.WORLD_GEN.porphyryCount.get(), AtumConfig.WORLD_GEN.porphyryBottomOffset.get(), AtumConfig.WORLD_GEN.porphyryTopOffset.get(), AtumConfig.WORLD_GEN.porphyryMaxHeight.get()))));
            }
        }

        public static void addOres(Biome biome) {
            //Vanilla based ores
            if (AtumConfig.WORLD_GEN.coalCount.get() > 0) {
                biome.addFeature(UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(NATURAL_STONE, AtumBlocks.COAL_ORE.getDefaultState(), AtumConfig.WORLD_GEN.coalVeinSize.get())).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(AtumConfig.WORLD_GEN.coalCount.get(), AtumConfig.WORLD_GEN.coalBottomOffset.get(), AtumConfig.WORLD_GEN.coalTopOffset.get(), AtumConfig.WORLD_GEN.coalMaxHeight.get()))));
            }
            if (AtumConfig.WORLD_GEN.ironCount.get() > 0) {
                biome.addFeature(UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(NATURAL_STONE, AtumBlocks.IRON_ORE.getDefaultState(), AtumConfig.WORLD_GEN.ironVeinSize.get())).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(AtumConfig.WORLD_GEN.ironCount.get(), AtumConfig.WORLD_GEN.ironBottomOffset.get(), AtumConfig.WORLD_GEN.ironTopOffset.get(), AtumConfig.WORLD_GEN.ironMaxHeight.get()))));
            }
            if (AtumConfig.WORLD_GEN.goldCount.get() > 0) {
                biome.addFeature(UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(NATURAL_STONE, AtumBlocks.GOLD_ORE.getDefaultState(), AtumConfig.WORLD_GEN.goldVeinSize.get())).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(AtumConfig.WORLD_GEN.goldCount.get(), AtumConfig.WORLD_GEN.goldBottomOffset.get(), AtumConfig.WORLD_GEN.goldTopOffset.get(), AtumConfig.WORLD_GEN.goldMaxHeight.get()))));
            }
            if (AtumConfig.WORLD_GEN.redstoneCount.get() > 0) {
                biome.addFeature(UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(NATURAL_STONE, AtumBlocks.REDSTONE_ORE.getDefaultState(), AtumConfig.WORLD_GEN.redstoneVeinSize.get())).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(AtumConfig.WORLD_GEN.redstoneCount.get(), AtumConfig.WORLD_GEN.redstoneBottomOffset.get(), AtumConfig.WORLD_GEN.redstoneTopOffset.get(), AtumConfig.WORLD_GEN.redstoneMaxHeight.get()))));
            }
            if (AtumConfig.WORLD_GEN.diamondCount.get() > 0) {
                biome.addFeature(UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(NATURAL_STONE, AtumBlocks.DIAMOND_ORE.getDefaultState(), AtumConfig.WORLD_GEN.diamondVeinSize.get())).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(AtumConfig.WORLD_GEN.diamondCount.get(), AtumConfig.WORLD_GEN.diamondBottomOffset.get(), AtumConfig.WORLD_GEN.diamondTopOffset.get(), AtumConfig.WORLD_GEN.diamondMaxHeight.get()))));
            }
            if (AtumConfig.WORLD_GEN.lapisCount.get() > 0) {
                biome.addFeature(UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(NATURAL_STONE, AtumBlocks.LAPIS_ORE.getDefaultState(), AtumConfig.WORLD_GEN.lapisVeinSize.get())).withPlacement(Placement.COUNT_DEPTH_AVERAGE.configure(new DepthAverageConfig(AtumConfig.WORLD_GEN.lapisCount.get(), AtumConfig.WORLD_GEN.lapisBaseline.get(), AtumConfig.WORLD_GEN.lapisSpread.get()))));
            }

            //Atum ores
            if (AtumConfig.WORLD_GEN.khnumiteCount.get() > 0) {
                biome.addFeature(UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(NATURAL_STONE, AtumBlocks.KHNUMITE_RAW.getDefaultState(), AtumConfig.WORLD_GEN.khnumiteVeinSize.get())).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(AtumConfig.WORLD_GEN.khnumiteCount.get(), AtumConfig.WORLD_GEN.khnumiteBottomOffset.get(), AtumConfig.WORLD_GEN.khnumiteTopOffset.get(), AtumConfig.WORLD_GEN.khnumiteMaxHeight.get()))));
            }
            if (AtumConfig.WORLD_GEN.boneOreCount.get() > 0) {
                biome.addFeature(UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(NATURAL_STONE, AtumBlocks.BONE_ORE.getDefaultState(), AtumConfig.WORLD_GEN.boneOreVeinSize.get())).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(AtumConfig.WORLD_GEN.boneOreCount.get(), AtumConfig.WORLD_GEN.boneOreBottomOffset.get(), AtumConfig.WORLD_GEN.boneOreTopOffset.get(), AtumConfig.WORLD_GEN.boneOreMaxHeight.get()))));
            }
            if (AtumConfig.WORLD_GEN.relicOreCount.get() > 0) {
                biome.addFeature(UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(NATURAL_STONE, AtumBlocks.RELIC_ORE.getDefaultState(), AtumConfig.WORLD_GEN.relicOreVeinSize.get())).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(AtumConfig.WORLD_GEN.relicOreCount.get(), AtumConfig.WORLD_GEN.relicOreBottomOffset.get(), AtumConfig.WORLD_GEN.relicOreTopOffset.get(), AtumConfig.WORLD_GEN.relicOreMaxHeight.get()))));
            }
        }

        public static void addEmeraldOre(Biome biome) {
            if (AtumConfig.WORLD_GEN.emeraldEnabled.get()) {
                biome.addFeature(UNDERGROUND_ORES, Feature.EMERALD_ORE.withConfiguration(new ReplaceBlockConfig(AtumBlocks.LIMESTONE.getDefaultState(), AtumBlocks.EMERALD_ORE.getDefaultState())).withPlacement(Placement.EMERALD_ORE.configure(IPlacementConfig.NO_PLACEMENT_CONFIG)));
            }
        }

        public static void addInfestedLimestone(Biome biome) {
            biome.addFeature(UNDERGROUND_DECORATION, Feature.ORE.withConfiguration(new OreFeatureConfig(NATURAL_STONE, AtumBlocks.LIMESTONE.getDefaultState().with(LimestoneBlock.HAS_SCARAB, true), 10)).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(8, 0, 11, 80))));
        }

        public static void addShrubs(Biome biome) {
            if (AtumConfig.WORLD_GEN.shrubFrequency.get() > 0) {
                biome.addFeature(VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration(SHRUB_CONFIG).withPlacement(Placement.COUNT_HEIGHTMAP_DOUBLE.configure(new FrequencyConfig(AtumConfig.WORLD_GEN.shrubFrequency.get()))));
                biome.addFeature(VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration(WEED_CONFIG).withPlacement(Placement.COUNT_HEIGHTMAP_DOUBLE.configure(new FrequencyConfig(AtumConfig.WORLD_GEN.shrubFrequency.get()))));
            }
        }

        public static void addFossils(Biome biome) {
            if (AtumConfig.WORLD_GEN.fossilsChance.get() > 0) {
                biome.addFeature(UNDERGROUND_DECORATION, DIRTY_BONE_FOSSILS.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.CHANCE_PASSTHROUGH.configure(new ChanceConfig(AtumConfig.WORLD_GEN.fossilsChance.get()))));
            }
        }

        public static void addDungeon(Biome biome) {
            if (AtumConfig.WORLD_GEN.dungeonChance.get() > 0) {
                biome.addFeature(UNDERGROUND_STRUCTURES, LIMESTONE_DUNGEONS.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.DUNGEONS.configure(new ChanceConfig(AtumConfig.WORLD_GEN.dungeonChance.get()))));
            }
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

        public static void addRuins(Biome biome) {
            if (AtumConfig.WORLD_GEN.ruinsEnabled.get()) {
                biome.addStructure(AtumFeatures.RUIN.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
                biome.addFeature(SURFACE_STRUCTURES, AtumFeatures.RUIN.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
            }
        }

        public static void addMineshaft(Biome biome, boolean isSurface) {
            if (AtumConfig.WORLD_GEN.mineshaftProbability.get() > 0.0D) {
                int chance = new Random().nextInt(100);
                AtumMineshaftStructure.Type type;
                if (chance > 50) {
                    type = isSurface ? AtumMineshaftStructure.Type.LIMESTONE_SURFACE : AtumMineshaftStructure.Type.LIMESTONE;
                } else {
                    type = isSurface ? AtumMineshaftStructure.Type.DEADWOOD_SURFACE : AtumMineshaftStructure.Type.DEADWOOD;
                }
                AtumMineshaftConfig config = new AtumMineshaftConfig(AtumConfig.WORLD_GEN.mineshaftProbability.get(), type);
                biome.addStructure(AtumFeatures.MINESHAFT.withConfiguration(config));
                biome.addFeature(SURFACE_STRUCTURES, AtumFeatures.MINESHAFT.withConfiguration(config));
            }
        }
    }
}