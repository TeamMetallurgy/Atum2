package com.teammetallurgy.atum.world.gen;

import com.teammetallurgy.atum.api.AtumAPI;
import com.teammetallurgy.atum.blocks.stone.limestone.LimestoneBlock;
import com.teammetallurgy.atum.blocks.wood.DeadwoodLogBlock;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumFeatures;
import com.teammetallurgy.atum.init.AtumStructures;
import com.teammetallurgy.atum.misc.AtumConfig;
import com.teammetallurgy.atum.world.gen.carver.AtumCarvers;
import com.teammetallurgy.atum.world.gen.structure.mineshaft.AtumMineshaftConfig;
import com.teammetallurgy.atum.world.gen.structure.mineshaft.AtumMineshaftStructure;
import net.minecraft.world.biome.BiomeGenerationSettings;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.template.RuleTest;
import net.minecraft.world.gen.feature.template.TagMatchRuleTest;
import net.minecraft.world.gen.foliageplacer.BlobFoliagePlacer;
import net.minecraft.world.gen.placement.*;
import net.minecraft.world.gen.trunkplacer.StraightTrunkPlacer;

import java.util.Random;

import static net.minecraft.world.gen.GenerationStage.Decoration.*;

public class AtumDefaultFeatures {
    public static final RuleTest NATURAL_STONE = new TagMatchRuleTest(AtumAPI.Tags.BASE_STONE_ATUM);

    public static void addCarvers(BiomeGenerationSettings.Builder builder) {
        builder.withCarver(GenerationStage.Carving.AIR, AtumCarvers.CAVE_CONFIGURED);
        builder.withCarver(GenerationStage.Carving.AIR, AtumCarvers.CANYON_CONFIGURED);
    }

    public static void addSprings(BiomeGenerationSettings.Builder builder) {
        builder.withFeature(VEGETAL_DECORATION, Feature.SPRING_FEATURE.withConfiguration(AtumFeatures.WATER_SPRING_CONFIG).withPlacement(Placement.field_242908_m.configure(new TopSolidRangeConfig(8, 8, 50)).func_242728_a().func_242731_b(14)));
        builder.withFeature(VEGETAL_DECORATION, Feature.SPRING_FEATURE.withConfiguration(AtumFeatures.LAVA_SPRING_CONFIG).withPlacement(Placement.field_242909_n.configure(new TopSolidRangeConfig(8, 16, 256)).func_242728_a().func_242731_b(8)));
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
            builder.withFeature(VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration(AtumFeatures.SHRUB_CONFIG).withPlacement(Features.Placements.PATCH_PLACEMENT).func_242731_b(AtumConfig.WORLD_GEN.shrubFrequency.get()));
            builder.withFeature(VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration(AtumFeatures.WEED_CONFIG).withPlacement(Features.Placements.PATCH_PLACEMENT).func_242731_b(AtumConfig.WORLD_GEN.shrubFrequency.get()));
        }
    }

    public static void addFossils(BiomeGenerationSettings.Builder builder) {
        if (AtumConfig.WORLD_GEN.fossilsChance.get() > 0) {

            builder.withFeature(UNDERGROUND_DECORATION, AtumFeatures.DIRTY_BONE_FOSSILS.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).func_242729_a(AtumConfig.WORLD_GEN.fossilsChance.get()));
        }
    }

    public static void addDungeon(BiomeGenerationSettings.Builder builder) {
        if (AtumConfig.WORLD_GEN.dungeonChance.get() > 0) {
            builder.withFeature(UNDERGROUND_STRUCTURES, AtumFeatures.LIMESTONE_DUNGEONS.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).func_242733_d(256).func_242728_a().func_242731_b(AtumConfig.WORLD_GEN.dungeonChance.get()));
        }
    }

    public static void addDeadwoodTrees(BiomeGenerationSettings.Builder builder, int count, float extraChance, int extraCount) {
        builder.withFeature(VEGETAL_DECORATION, AtumFeatures.DEADWOOD_FEATURE.withConfiguration((new BaseTreeFeatureConfig.Builder(new SimpleBlockStateProvider(AtumBlocks.DEADWOOD_LOG.getDefaultState().with(DeadwoodLogBlock.HAS_SCARAB, true)), new SimpleBlockStateProvider(AtumBlocks.DEADWOOD_BRANCH.getDefaultState()), new BlobFoliagePlacer(FeatureSpread.func_242252_a(0), FeatureSpread.func_242252_a(0), 0), new StraightTrunkPlacer(0, 0, 0), new TwoLayerFeature(0, 0, 0)).build())).withPlacement(Placement.field_242902_f.configure(new AtSurfaceWithExtraConfig(count, extraChance, extraCount))));
    }

    public static void addTomb(BiomeGenerationSettings.Builder builder) {
        builder.withStructure(AtumStructures.TOMB_FEATURE);
    }

    public static void addPyramid(BiomeGenerationSettings.Builder builder) {
        if (AtumConfig.WORLD_GEN.pyramidEnabled.get()) {
            builder.withStructure(AtumStructures.PYRAMID_FEATURE);
        }
    }

    public static void addRuins(BiomeGenerationSettings.Builder builder) {
        if (AtumConfig.WORLD_GEN.ruinsEnabled.get()) {
            builder.withStructure(AtumStructures.RUIN_FEATURE);
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
            builder.withStructure(AtumStructures.MINESHAFT_FEATURE.field_236268_b_.func_236391_a_(config));  //TODO Test
        }
    }
}