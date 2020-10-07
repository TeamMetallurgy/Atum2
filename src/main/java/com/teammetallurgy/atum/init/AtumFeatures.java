package com.teammetallurgy.atum.init;

import com.google.common.collect.ImmutableSet;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.api.AtumAPI;
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
import net.minecraft.fluid.Fluids;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.biome.BiomeGenerationSettings;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.blockplacer.ColumnBlockPlacer;
import net.minecraft.world.gen.blockplacer.SimpleBlockPlacer;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.template.RuleTest;
import net.minecraft.world.gen.feature.template.TagMatchRuleTest;
import net.minecraft.world.gen.foliageplacer.AcaciaFoliagePlacer;
import net.minecraft.world.gen.foliageplacer.BlobFoliagePlacer;
import net.minecraft.world.gen.placement.*;
import net.minecraft.world.gen.trunkplacer.StraightTrunkPlacer;
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
    private static final List<Structure<?>> STRUCTURES = new ArrayList<>();
    //Features
    public static final Feature<DoubleBlockStateFeatureConfig> OASIS_POND = register("oasis_pond", new OasisPondFeature(DoubleBlockStateFeatureConfig.DOUBLE_STATE_CODEC));
    public static final Feature<BlockStateFeatureConfig> SURFACE_LAVA_LAKE = register("surface_lava_lake", new LakeFeature(BlockStateFeatureConfig.field_236455_a_));
    public static final Feature<PalmConfig> PALM_TREE = register("palm_tree", new PalmFeature(PalmConfig.PALM_CODEC));
    public static final BonusCrateFeature BONUS_CRATE = register("bonus_crate", new BonusCrateFeature(NoFeatureConfig.field_236558_a_));
    public static final StartStructureFeature START_STRUCTURE = register("start_structure", new StartStructureFeature(NoFeatureConfig.field_236558_a_));
    public static final DirtyBoneFossilsFeature DIRTY_BONE_FOSSILS = register("dirty_bone_fossil", new DirtyBoneFossilsFeature(NoFeatureConfig.field_236558_a_));
    public static final LimestoneDungeonsFeature LIMESTONE_DUNGEONS = register("limestone_dungeon", new LimestoneDungeonsFeature(NoFeatureConfig.field_236558_a_));
    public static final DeadwoodFeature DEADWOOD_FEATURE = register("deadwood_tree", new DeadwoodFeature(BaseTreeFeatureConfig.CODEC));
    public static final Feature<NoFeatureConfig> LIMESTONE_SPIKE = register("limestone_spike", new LimestoneSpikeFeature(NoFeatureConfig.field_236558_a_));
    public static final Feature<BlockClusterFeatureConfig> ANPUTS_FINGERS = register("anputs_fingers", new AnputsFingersFeature(BlockClusterFeatureConfig.field_236587_a_));

    //Structures
    //public static final Structure<NoFeatureConfig> GIRAFI_TOMB_STRUCTURE = register("girafi_tomb", new GirafiTombStructure(NoFeatureConfig.field_236558_a_));
    //public static final Structure<NoFeatureConfig> LIGHTHOUSE_STRUCTURE = register("lighthouse", new LighthouseStructure(NoFeatureConfig.field_236558_a_));
    //public static final Structure<NoFeatureConfig> TOMB_STRUCTURE = register("tomb", new TombStructure(NoFeatureConfig.field_236558_a_));
    //public static final Structure<NoFeatureConfig> RUIN_STRUCTURE = register("ruin", new RuinStructure(NoFeatureConfig.field_236558_a_));
    //public static final Structure<NoFeatureConfig> PYRAMID_STRUCTURE = register("pyramid", new PyramidStructure(NoFeatureConfig.field_236558_a_));
    //public static final Structure<AtumMineshaftConfig> MINESHAFT_STRUCTURE = register("mineshaft", new AtumMineshaftStructure(AtumMineshaftConfig.CODEC));

    //Structure Features
    //public static final StructureFeature<NoFeatureConfig, ? extends Structure<NoFeatureConfig>> GIRAFI_TOMB_FEATURE = register(GIRAFI_TOMB_STRUCTURE, NoFeatureConfig.field_236559_b_);
    //public static final StructureFeature<NoFeatureConfig, ? extends Structure<NoFeatureConfig>> LIGHTHOUSE_FEATURE = register(LIGHTHOUSE_STRUCTURE, NoFeatureConfig.field_236559_b_);
    //public static final StructureFeature<NoFeatureConfig, ? extends Structure<NoFeatureConfig>> TOMB_FEATURE = register(TOMB_STRUCTURE, NoFeatureConfig.field_236559_b_);
    //public static final StructureFeature<NoFeatureConfig, ? extends Structure<NoFeatureConfig>> RUIN_FEATURE = register(RUIN_STRUCTURE, NoFeatureConfig.field_236559_b_);
    //public static final StructureFeature<NoFeatureConfig, ? extends Structure<NoFeatureConfig>> PYRAMID_FEATURE = register(PYRAMID_STRUCTURE, NoFeatureConfig.field_236559_b_);
    //public static final StructureFeature<AtumMineshaftConfig, ? extends Structure<AtumMineshaftConfig>> MINESHAFT_FEATURE = register(MINESHAFT_STRUCTURE, new AtumMineshaftConfig(0.0F, AtumMineshaftStructure.Type.LIMESTONE)); //TODO Doing the config like this, is probably a problem

    //Feature Configs
    public static final BlockClusterFeatureConfig OASIS_GRASS_CONFIG = (new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(AtumBlocks.OASIS_GRASS.getDefaultState()), new SimpleBlockPlacer())).tries(30).build();
    public static final BlockClusterFeatureConfig DEAD_GRASS_CONFIG = (new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(AtumBlocks.DEAD_GRASS.getDefaultState()), new SimpleBlockPlacer())).tries(2).build();
    public static final BlockClusterFeatureConfig ANPUTS_FINGERS_CONFIG = (new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(AtumBlocks.ANPUTS_FINGERS.getDefaultState()), new SimpleBlockPlacer())).build();
    public static final BlockClusterFeatureConfig PAPYRUS_CONFIG = (new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(AtumBlocks.PAPYRUS.getDefaultState()), new ColumnBlockPlacer(1, 2))).tries(124).xSpread(1).ySpread(0).zSpread(1).func_227317_b_().requiresWater().build();
    public static final PalmConfig PALM_TREE_CONFIG = (new PalmConfig.Builder(new SimpleBlockStateProvider(AtumBlocks.PALM_LOG.getDefaultState()), new SimpleBlockStateProvider(AtumBlocks.PALM_LEAVES.getDefaultState()), new AcaciaFoliagePlacer(FeatureSpread.func_242252_a(0), FeatureSpread.func_242252_a(0)), new StraightTrunkPlacer(5, 2, 1), new TwoLayerFeature(1, 0, 1))).setIgnoreVines().date(0.1D).ophidianTongue(0.6D).build(); //TODO TwoLayerFeature n'stuff
    public static final PalmConfig PALM_TREE_CONFIG_SAPLING = (new PalmConfig.Builder(new SimpleBlockStateProvider(AtumBlocks.PALM_LOG.getDefaultState()), new SimpleBlockStateProvider(AtumBlocks.PALM_LEAVES.getDefaultState()), new AcaciaFoliagePlacer(FeatureSpread.func_242252_a(0), FeatureSpread.func_242252_a(0)), new StraightTrunkPlacer(5, 2, 1), new TwoLayerFeature(1, 0, 1))).setIgnoreVines().date(0.1D).ophidianTongue(0.0D).build(); //TODO TwoLayerFeature n'stuff
    public static final PalmConfig DEAD_PALM_TREE_CONFIG = (new PalmConfig.Builder(new SimpleBlockStateProvider(AtumBlocks.DEADWOOD_LOG.getDefaultState().with(DeadwoodLogBlock.HAS_SCARAB, true)), new SimpleBlockStateProvider(AtumBlocks.DEADWOOD_LEAVES.getDefaultState()), new AcaciaFoliagePlacer(FeatureSpread.func_242252_a(0), FeatureSpread.func_242252_a(0)), new StraightTrunkPlacer(5, 2, 1), new TwoLayerFeature(1, 0, 1))).setIgnoreVines().date(0.0D).ophidianTongue(0.0D).build(); //TODO TwoLayerFeature n'stuff
    public static final LiquidsConfig WATER_SPRING_CONFIG = new LiquidsConfig(Fluids.WATER.getDefaultState(), true, 4, 1, ImmutableSet.of(AtumBlocks.LIMESTONE));
    public static final LiquidsConfig LAVA_SPRING_CONFIG = new LiquidsConfig(Fluids.LAVA.getDefaultState(), true, 4, 1, ImmutableSet.of(AtumBlocks.LIMESTONE));
    public static final BlockClusterFeatureConfig SHRUB_CONFIG = (new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(AtumBlocks.SHRUB.getDefaultState()), new SimpleBlockPlacer())).tries(3).build();
    public static final BlockClusterFeatureConfig WEED_CONFIG = (new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(AtumBlocks.WEED.getDefaultState()), new SimpleBlockPlacer())).tries(3).build();

    private static <C extends IFeatureConfig, F extends Feature<C>> F register(String name, F feature) {
        feature.setRegistryName(new ResourceLocation(Atum.MOD_ID, name));
        FEATURES.add(feature);
        return feature;
    }

    private static <F extends Structure<?>> F register(String name, F structure) {
        structure.setRegistryName(new ResourceLocation(Atum.MOD_ID, name));
        STRUCTURES.add(structure);
        return structure;
    }

    private static <FC extends IFeatureConfig, F extends Structure<FC>> StructureFeature<FC, ?> register(F structure, FC fc) {
        return WorldGenRegistries.register(WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE, structure.getStructureName(), structure.func_236391_a_(fc));
    }

    @SubscribeEvent
    public static void registerFeature(RegistryEvent.Register<Feature<?>> event) {
        for (Feature<?> feature : FEATURES) {
            event.getRegistry().register(feature);
        }
    }

    @SubscribeEvent
    public static void registerStructure(RegistryEvent.Register<Structure<?>> event) {
        for (Structure<?> feature : STRUCTURES) {
            //event.getRegistry().register(feature); //TODO Fix Crash when pressing singleplayer
        }
    }

    public static class Default {
        public static final RuleTest NATURAL_STONE = new TagMatchRuleTest(AtumAPI.Tags.BASE_STONE_ATUM);

        public static void addCarvers(BiomeGenerationSettings.Builder builder) {
            builder.withCarver(GenerationStage.Carving.AIR, AtumCarvers.CAVE_CONFIGURED);
            builder.withCarver(GenerationStage.Carving.AIR, AtumCarvers.CANYON_CONFIGURED);
        }

        public static void addSprings(BiomeGenerationSettings.Builder builder) {
            builder.withFeature(VEGETAL_DECORATION, Feature.SPRING_FEATURE.withConfiguration(WATER_SPRING_CONFIG).withPlacement(Placement.field_242908_m.configure(new TopSolidRangeConfig(8, 8, 50)).func_242728_a().func_242731_b(14)));
            builder.withFeature(VEGETAL_DECORATION, Feature.SPRING_FEATURE.withConfiguration(LAVA_SPRING_CONFIG).withPlacement(Placement.field_242909_n.configure(new TopSolidRangeConfig(8, 16, 256)).func_242728_a().func_242731_b(8)));
        }

        public static void addMaterialPockets(BiomeGenerationSettings.Builder builder) {
            if (AtumConfig.WORLD_GEN.sandCount.get() > 0) {
                builder.withFeature(UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(NATURAL_STONE, AtumBlocks.SAND.getDefaultState(), AtumConfig.WORLD_GEN.sandVeinSize.get())).func_242733_d(AtumConfig.WORLD_GEN.sandMaxHeight.get()).func_242728_a().func_242731_b(AtumConfig.WORLD_GEN.sandCount.get()));
            }
            if (AtumConfig.WORLD_GEN.limestoneGravelCount.get() > 0) {
                builder.withFeature(UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(NATURAL_STONE, AtumBlocks.LIMESTONE_GRAVEL.getDefaultState(), AtumConfig.WORLD_GEN.limestoneGravelVeinSize.get())).func_242733_d(AtumConfig.WORLD_GEN.limestoneGravelMaxHeight.get()).func_242728_a().func_242731_b(AtumConfig.WORLD_GEN.limestoneGravelCount.get()));
            }
            if (AtumConfig.WORLD_GEN.marlCount.get() > 0) {
                builder.withFeature(UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(NATURAL_STONE, AtumBlocks.MARL.getDefaultState(), AtumConfig.WORLD_GEN.marlVeinSize.get())).func_242733_d(AtumConfig.WORLD_GEN.marlMaxHeight.get()).func_242728_a().func_242731_b(AtumConfig.WORLD_GEN.marlCount.get()));
            }
        }

        public static void addStoneVariants(BiomeGenerationSettings.Builder builder) {
            if (AtumConfig.WORLD_GEN.alabasterCount.get() > 0) {
                builder.withFeature(UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(NATURAL_STONE, AtumBlocks.ALABASTER.getDefaultState(), AtumConfig.WORLD_GEN.alabasterVeinSize.get())).func_242733_d(AtumConfig.WORLD_GEN.alabasterMaxHeight.get()).func_242728_a().func_242731_b(AtumConfig.WORLD_GEN.alabasterCount.get()));
            }
            if (AtumConfig.WORLD_GEN.porphyryCount.get() > 0) {
                builder.withFeature(UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(NATURAL_STONE, AtumBlocks.PORPHYRY.getDefaultState(), AtumConfig.WORLD_GEN.porphyryVeinSize.get())).func_242733_d(AtumConfig.WORLD_GEN.porphyryMaxHeight.get()).func_242728_a().func_242731_b(AtumConfig.WORLD_GEN.porphyryCount.get()));
            }
        }

        public static void addOres(BiomeGenerationSettings.Builder builder) {
            //Vanilla based ores
            if (AtumConfig.WORLD_GEN.coalCount.get() > 0) {
                builder.withFeature(UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(NATURAL_STONE, AtumBlocks.COAL_ORE.getDefaultState(), AtumConfig.WORLD_GEN.coalVeinSize.get())).func_242733_d(AtumConfig.WORLD_GEN.coalMaxHeight.get()).func_242728_a().func_242731_b(AtumConfig.WORLD_GEN.coalCount.get()));
            }
            if (AtumConfig.WORLD_GEN.ironCount.get() > 0) {
                builder.withFeature(UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(NATURAL_STONE, AtumBlocks.IRON_ORE.getDefaultState(), AtumConfig.WORLD_GEN.ironVeinSize.get())).func_242733_d(AtumConfig.WORLD_GEN.ironMaxHeight.get()).func_242728_a().func_242731_b(AtumConfig.WORLD_GEN.ironCount.get()));
            }
            if (AtumConfig.WORLD_GEN.goldCount.get() > 0) {
                builder.withFeature(UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(NATURAL_STONE, AtumBlocks.GOLD_ORE.getDefaultState(), AtumConfig.WORLD_GEN.goldVeinSize.get())).func_242733_d(AtumConfig.WORLD_GEN.goldMaxHeight.get()).func_242728_a().func_242731_b(AtumConfig.WORLD_GEN.goldCount.get()));
            }
            if (AtumConfig.WORLD_GEN.redstoneCount.get() > 0) {
                builder.withFeature(UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(NATURAL_STONE, AtumBlocks.REDSTONE_ORE.getDefaultState(), AtumConfig.WORLD_GEN.redstoneVeinSize.get())).func_242733_d(AtumConfig.WORLD_GEN.redstoneMaxHeight.get()).func_242728_a().func_242731_b(AtumConfig.WORLD_GEN.redstoneCount.get()));
            }
            if (AtumConfig.WORLD_GEN.diamondCount.get() > 0) {
                builder.withFeature(UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(NATURAL_STONE, AtumBlocks.DIAMOND_ORE.getDefaultState(), AtumConfig.WORLD_GEN.diamondVeinSize.get())).func_242733_d(AtumConfig.WORLD_GEN.diamondMaxHeight.get()).func_242728_a().func_242731_b(AtumConfig.WORLD_GEN.diamondCount.get()));
            }
            if (AtumConfig.WORLD_GEN.lapisBaseline.get() > 0) {
                builder.withFeature(UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(NATURAL_STONE, AtumBlocks.LAPIS_ORE.getDefaultState(), AtumConfig.WORLD_GEN.lapisVeinSize.get())).withPlacement(Placement.field_242910_o.configure(new DepthAverageConfig(AtumConfig.WORLD_GEN.lapisBaseline.get(), AtumConfig.WORLD_GEN.lapisSpread.get()))));
            }

            //Atum ores
            if (AtumConfig.WORLD_GEN.khnumiteCount.get() > 0) {
                builder.withFeature(UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(NATURAL_STONE, AtumBlocks.KHNUMITE_RAW.getDefaultState(), AtumConfig.WORLD_GEN.khnumiteVeinSize.get())).func_242733_d(AtumConfig.WORLD_GEN.khnumiteMaxHeight.get()).func_242728_a().func_242731_b(AtumConfig.WORLD_GEN.khnumiteCount.get()));
            }
            if (AtumConfig.WORLD_GEN.boneOreCount.get() > 0) {
                builder.withFeature(UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(NATURAL_STONE, AtumBlocks.BONE_ORE.getDefaultState(), AtumConfig.WORLD_GEN.boneOreVeinSize.get())).func_242733_d(AtumConfig.WORLD_GEN.boneOreMaxHeight.get()).func_242728_a().func_242731_b(AtumConfig.WORLD_GEN.boneOreCount.get()));
            }
            if (AtumConfig.WORLD_GEN.relicOreCount.get() > 0) {
                builder.withFeature(UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(NATURAL_STONE, AtumBlocks.RELIC_ORE.getDefaultState(), AtumConfig.WORLD_GEN.relicOreVeinSize.get())).func_242733_d(AtumConfig.WORLD_GEN.relicOreMaxHeight.get()).func_242728_a().func_242731_b(AtumConfig.WORLD_GEN.relicOreCount.get()));
            }
        }

        public static void addEmeraldOre(BiomeGenerationSettings.Builder builder) {
            if (AtumConfig.WORLD_GEN.emeraldEnabled.get()) {
                builder.withFeature(UNDERGROUND_ORES, Feature.EMERALD_ORE.withConfiguration(new ReplaceBlockConfig(AtumBlocks.LIMESTONE.getDefaultState(), AtumBlocks.EMERALD_ORE.getDefaultState())).withPlacement(Placement.EMERALD_ORE.configure(IPlacementConfig.NO_PLACEMENT_CONFIG)));
            }
        }

        public static void addInfestedLimestone(BiomeGenerationSettings.Builder builder) {

            builder.withFeature(UNDERGROUND_DECORATION, Feature.ORE.withConfiguration(new OreFeatureConfig(NATURAL_STONE, AtumBlocks.LIMESTONE.getDefaultState().with(LimestoneBlock.HAS_SCARAB, true), 10)).withPlacement(Placement.field_242907_l.configure(new TopSolidRangeConfig(0, 11, 80)).func_242728_a().func_242731_b(8)));
        }

        public static void addShrubs(BiomeGenerationSettings.Builder builder) {
            if (AtumConfig.WORLD_GEN.shrubFrequency.get() > 0) {
                builder.withFeature(VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration(SHRUB_CONFIG).withPlacement(Features.Placements.PATCH_PLACEMENT).func_242731_b(AtumConfig.WORLD_GEN.shrubFrequency.get()));
                builder.withFeature(VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration(WEED_CONFIG).withPlacement(Features.Placements.PATCH_PLACEMENT).func_242731_b(AtumConfig.WORLD_GEN.shrubFrequency.get()));
            }
        }

        public static void addFossils(BiomeGenerationSettings.Builder builder) {
            if (AtumConfig.WORLD_GEN.fossilsChance.get() > 0) {

                builder.withFeature(UNDERGROUND_DECORATION, DIRTY_BONE_FOSSILS.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).func_242729_a(AtumConfig.WORLD_GEN.fossilsChance.get()));
            }
        }

        public static void addDungeon(BiomeGenerationSettings.Builder builder) {
            if (AtumConfig.WORLD_GEN.dungeonChance.get() > 0) {
                builder.withFeature(UNDERGROUND_STRUCTURES, LIMESTONE_DUNGEONS.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).func_242733_d(256).func_242728_a().func_242731_b(AtumConfig.WORLD_GEN.dungeonChance.get()));
            }
        }

        public static void addDeadwoodTrees(BiomeGenerationSettings.Builder builder, int count, float extraChance, int extraCount) {
            builder.withFeature(VEGETAL_DECORATION, DEADWOOD_FEATURE.withConfiguration((new BaseTreeFeatureConfig.Builder(new SimpleBlockStateProvider(AtumBlocks.DEADWOOD_LOG.getDefaultState().with(DeadwoodLogBlock.HAS_SCARAB, true)), new SimpleBlockStateProvider(AtumBlocks.DEADWOOD_BRANCH.getDefaultState()), new BlobFoliagePlacer(FeatureSpread.func_242252_a(0), FeatureSpread.func_242252_a(0), 0), new StraightTrunkPlacer(0, 0, 0), new TwoLayerFeature(0, 0, 0)).build())).withPlacement(Placement.field_242902_f.configure(new AtSurfaceWithExtraConfig(count, extraChance, extraCount))));
        }

        public static void addTomb(BiomeGenerationSettings.Builder builder) {
            //builder.withStructure(AtumFeatures.TOMB_FEATURE); //TODO
        }

        public static void addPyramid(BiomeGenerationSettings.Builder builder) {
            if (AtumConfig.WORLD_GEN.pyramidEnabled.get()) {
                //builder.withStructure(AtumFeatures.PYRAMID_FEATURE); //TODO
            }
        }

        public static void addRuins(BiomeGenerationSettings.Builder builder) {
            if (AtumConfig.WORLD_GEN.ruinsEnabled.get()) {
                //builder.withStructure(AtumFeatures.RUIN_FEATURE); //TODO
            }
        }

        public static void addMineshaft(BiomeGenerationSettings.Builder builder, boolean isSurface) {
            if (AtumConfig.WORLD_GEN.mineshaftProbability.get() > 0.0D) {
                int chance = new Random().nextInt(100);
                AtumMineshaftStructure.Type type;
                if (chance > 50) {
                    type = isSurface ? AtumMineshaftStructure.Type.LIMESTONE_SURFACE : AtumMineshaftStructure.Type.LIMESTONE;
                } else {
                    type = isSurface ? AtumMineshaftStructure.Type.DEADWOOD_SURFACE : AtumMineshaftStructure.Type.DEADWOOD;
                }
                AtumMineshaftConfig config = new AtumMineshaftConfig(AtumConfig.WORLD_GEN.mineshaftProbability.get().floatValue(), type);
                //builder.withStructure(AtumFeatures.MINESHAFT_FEATURE.field_236268_b_.func_236391_a_(config));  //TODO Test
            }
        }
    }
}