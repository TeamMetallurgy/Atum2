package com.teammetallurgy.atum.world.gen;

import com.teammetallurgy.atum.api.AtumAPI;
import com.teammetallurgy.atum.blocks.stone.limestone.LimestoneBlock;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumFeatures;
import com.teammetallurgy.atum.init.AtumStructures;
import com.teammetallurgy.atum.misc.AtumConfig;
import com.teammetallurgy.atum.world.gen.carver.AtumCarvers;
import net.minecraft.world.biome.BiomeGenerationSettings;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.template.RuleTest;
import net.minecraft.world.gen.feature.template.TagMatchRuleTest;
import net.minecraft.world.gen.placement.*;

import static net.minecraft.world.gen.GenerationStage.Decoration.*;

public class AtumDefaultFeatures {
    public static final RuleTest NATURAL_STONE = new TagMatchRuleTest(AtumAPI.Tags.BASE_STONE_ATUM);

    public static void addCarvers(BiomeGenerationSettings.Builder builder) {
        builder.withCarver(GenerationStage.Carving.AIR, AtumCarvers.CAVE_CONFIGURED);
        builder.withCarver(GenerationStage.Carving.AIR, AtumCarvers.CANYON_CONFIGURED);
    }

    public static void addSandLayer(BiomeGenerationSettings.Builder builder) {
        builder.withFeature(GenerationStage.Decoration.TOP_LAYER_MODIFICATION, AtumFeatures.SAND_LAYER.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
    }

    public static void addSprings(BiomeGenerationSettings.Builder builder) {
        builder.withFeature(VEGETAL_DECORATION, Feature.SPRING_FEATURE.withConfiguration(AtumFeatures.WATER_SPRING_CONFIG).withPlacement(Placement.RANGE_BIASED.configure(new TopSolidRangeConfig(8, 8, 50)).square().func_242731_b(14)));
        builder.withFeature(VEGETAL_DECORATION, Feature.SPRING_FEATURE.withConfiguration(AtumFeatures.LAVA_SPRING_CONFIG).withPlacement(Placement.RANGE_VERY_BIASED.configure(new TopSolidRangeConfig(8, 16, 256)).square().func_242731_b(8)));
    }

    public static void addMaterialPockets(BiomeGenerationSettings.Builder builder) {
        if (AtumConfig.WORLD_GEN.sandCount.get() > 0) {
            builder.withFeature(UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(NATURAL_STONE, AtumBlocks.SAND.getDefaultState(), AtumConfig.WORLD_GEN.sandVeinSize.get())).range(AtumConfig.WORLD_GEN.sandMaxHeight.get()).square().func_242731_b(AtumConfig.WORLD_GEN.sandCount.get()));
        }
        if (AtumConfig.WORLD_GEN.limestoneGravelCount.get() > 0) {
            builder.withFeature(UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(NATURAL_STONE, AtumBlocks.LIMESTONE_GRAVEL.getDefaultState(), AtumConfig.WORLD_GEN.limestoneGravelVeinSize.get())).range(AtumConfig.WORLD_GEN.limestoneGravelMaxHeight.get()).square().func_242731_b(AtumConfig.WORLD_GEN.limestoneGravelCount.get()));
        }
        if (AtumConfig.WORLD_GEN.marlCount.get() > 0) {
            builder.withFeature(UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(NATURAL_STONE, AtumBlocks.MARL.getDefaultState(), AtumConfig.WORLD_GEN.marlVeinSize.get())).range(AtumConfig.WORLD_GEN.marlMaxHeight.get()).square().func_242731_b(AtumConfig.WORLD_GEN.marlCount.get()));
        }
    }

    public static void addStoneVariants(BiomeGenerationSettings.Builder builder) {
        if (AtumConfig.WORLD_GEN.alabasterCount.get() > 0) {
            builder.withFeature(UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(NATURAL_STONE, AtumBlocks.ALABASTER.getDefaultState(), AtumConfig.WORLD_GEN.alabasterVeinSize.get())).range(AtumConfig.WORLD_GEN.alabasterMaxHeight.get()).square().func_242731_b(AtumConfig.WORLD_GEN.alabasterCount.get()));
        }
        if (AtumConfig.WORLD_GEN.porphyryCount.get() > 0) {
            builder.withFeature(UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(NATURAL_STONE, AtumBlocks.PORPHYRY.getDefaultState(), AtumConfig.WORLD_GEN.porphyryVeinSize.get())).range(AtumConfig.WORLD_GEN.porphyryMaxHeight.get()).square().func_242731_b(AtumConfig.WORLD_GEN.porphyryCount.get()));
        }
    }

    public static void addOres(BiomeGenerationSettings.Builder builder) {
        //Vanilla based ores
        if (AtumConfig.WORLD_GEN.coalCount.get() > 0) {
            builder.withFeature(UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(NATURAL_STONE, AtumBlocks.COAL_ORE.getDefaultState(), AtumConfig.WORLD_GEN.coalVeinSize.get())).range(AtumConfig.WORLD_GEN.coalMaxHeight.get()).square().func_242731_b(AtumConfig.WORLD_GEN.coalCount.get()));
        }
        if (AtumConfig.WORLD_GEN.ironCount.get() > 0) {
            builder.withFeature(UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(NATURAL_STONE, AtumBlocks.IRON_ORE.getDefaultState(), AtumConfig.WORLD_GEN.ironVeinSize.get())).range(AtumConfig.WORLD_GEN.ironMaxHeight.get()).square().func_242731_b(AtumConfig.WORLD_GEN.ironCount.get()));
        }
        if (AtumConfig.WORLD_GEN.goldCount.get() > 0) {
            builder.withFeature(UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(NATURAL_STONE, AtumBlocks.GOLD_ORE.getDefaultState(), AtumConfig.WORLD_GEN.goldVeinSize.get())).range(AtumConfig.WORLD_GEN.goldMaxHeight.get()).square().func_242731_b(AtumConfig.WORLD_GEN.goldCount.get()));
        }
        if (AtumConfig.WORLD_GEN.redstoneCount.get() > 0) {
            builder.withFeature(UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(NATURAL_STONE, AtumBlocks.REDSTONE_ORE.getDefaultState(), AtumConfig.WORLD_GEN.redstoneVeinSize.get())).range(AtumConfig.WORLD_GEN.redstoneMaxHeight.get()).square().func_242731_b(AtumConfig.WORLD_GEN.redstoneCount.get()));
        }
        if (AtumConfig.WORLD_GEN.diamondCount.get() > 0) {
            builder.withFeature(UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(NATURAL_STONE, AtumBlocks.DIAMOND_ORE.getDefaultState(), AtumConfig.WORLD_GEN.diamondVeinSize.get())).range(AtumConfig.WORLD_GEN.diamondMaxHeight.get()).square().func_242731_b(AtumConfig.WORLD_GEN.diamondCount.get()));
        }
        if (AtumConfig.WORLD_GEN.lapisBaseline.get() > 0) {
            builder.withFeature(UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(NATURAL_STONE, AtumBlocks.LAPIS_ORE.getDefaultState(), AtumConfig.WORLD_GEN.lapisVeinSize.get())).withPlacement(Placement.DEPTH_AVERAGE.configure(new DepthAverageConfig(AtumConfig.WORLD_GEN.lapisBaseline.get(), AtumConfig.WORLD_GEN.lapisSpread.get()))));
        }

        //Atum ores
        if (AtumConfig.WORLD_GEN.khnumiteCount.get() > 0) {
            builder.withFeature(UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(NATURAL_STONE, AtumBlocks.KHNUMITE_RAW.getDefaultState(), AtumConfig.WORLD_GEN.khnumiteVeinSize.get())).range(AtumConfig.WORLD_GEN.khnumiteMaxHeight.get()).square().func_242731_b(AtumConfig.WORLD_GEN.khnumiteCount.get()));
        }
        if (AtumConfig.WORLD_GEN.boneOreCount.get() > 0) {
            builder.withFeature(UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(NATURAL_STONE, AtumBlocks.BONE_ORE.getDefaultState(), AtumConfig.WORLD_GEN.boneOreVeinSize.get())).range(AtumConfig.WORLD_GEN.boneOreMaxHeight.get()).square().func_242731_b(AtumConfig.WORLD_GEN.boneOreCount.get()));
        }
        if (AtumConfig.WORLD_GEN.relicOreCount.get() > 0) {
            builder.withFeature(UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(NATURAL_STONE, AtumBlocks.RELIC_ORE.getDefaultState(), AtumConfig.WORLD_GEN.relicOreVeinSize.get())).range(AtumConfig.WORLD_GEN.relicOreMaxHeight.get()).square().func_242731_b(AtumConfig.WORLD_GEN.relicOreCount.get()));
        }
    }

    public static void addEmeraldOre(BiomeGenerationSettings.Builder builder) {
        if (AtumConfig.WORLD_GEN.emeraldEnabled.get()) {
            builder.withFeature(UNDERGROUND_ORES, Feature.EMERALD_ORE.withConfiguration(new ReplaceBlockConfig(AtumBlocks.LIMESTONE.getDefaultState(), AtumBlocks.EMERALD_ORE.getDefaultState())).withPlacement(Placement.EMERALD_ORE.configure(IPlacementConfig.NO_PLACEMENT_CONFIG)));
        }
    }

    public static void addInfestedLimestone(BiomeGenerationSettings.Builder builder) {
        builder.withFeature(UNDERGROUND_DECORATION, Feature.ORE.withConfiguration(new OreFeatureConfig(NATURAL_STONE, AtumBlocks.LIMESTONE.getDefaultState().with(LimestoneBlock.HAS_SCARAB, true), 10)).withPlacement(Placement.RANGE.configure(new TopSolidRangeConfig(0, 11, 80)).square().func_242731_b(8)));
    }

    public static void addShrubs(BiomeGenerationSettings.Builder builder) {
        if (AtumConfig.WORLD_GEN.shrubFrequency.get() > 0) {
            builder.withFeature(VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration(AtumFeatures.SHRUB_CONFIG).withPlacement(Features.Placements.PATCH_PLACEMENT).func_242731_b(AtumConfig.WORLD_GEN.shrubFrequency.get()));
            builder.withFeature(VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration(AtumFeatures.WEED_CONFIG).withPlacement(Features.Placements.PATCH_PLACEMENT).func_242731_b(AtumConfig.WORLD_GEN.shrubFrequency.get()));
        }
    }

    public static void addFossils(BiomeGenerationSettings.Builder builder) {
        if (AtumConfig.WORLD_GEN.fossilsChance.get() > 0) {
            builder.withFeature(UNDERGROUND_DECORATION, AtumFeatures.DIRTY_BONE_FOSSILS.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).chance(AtumConfig.WORLD_GEN.fossilsChance.get()));
        }
    }

    public static void addDungeon(BiomeGenerationSettings.Builder builder) {
        if (AtumConfig.WORLD_GEN.dungeonChance.get() > 0) {
            builder.withFeature(UNDERGROUND_STRUCTURES, AtumFeatures.LIMESTONE_DUNGEONS.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).range(256).square().func_242731_b(AtumConfig.WORLD_GEN.dungeonChance.get()));
        }
    }

    public static void addDeadwoodTrees(BiomeGenerationSettings.Builder builder, int count, float extraChance, int extraCount) {
        builder.withFeature(VEGETAL_DECORATION, AtumFeatures.DEADWOOD_FEATURE.withConfiguration(NoFeatureConfig.field_236559_b_).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).withPlacement(Placement.COUNT_EXTRA.configure(new AtSurfaceWithExtraConfig(count, extraChance, extraCount))));
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
            if (isSurface) {
                builder.withStructure(AtumStructures.MINESHAFT_DEADWOOD_SURFACE_FEATURE);
                builder.withStructure(AtumStructures.MINESHAFT_LIMESTONE_SURFACE_FEATURE);
            } else {
                builder.withStructure(AtumStructures.MINESHAFT_DEADWOOD_FEATURE);
                builder.withStructure(AtumStructures.MINESHAFT_LIMESTONE_FEATURE);
            }
        }
    }
}