package com.teammetallurgy.atum.world.gen;

import com.teammetallurgy.atum.api.AtumAPI;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumFeatures;
import com.teammetallurgy.atum.init.AtumStructures;
import com.teammetallurgy.atum.misc.AtumConfig;
import com.teammetallurgy.atum.world.gen.carver.AtumCarvers;
import net.minecraft.world.biome.BiomeGenerationSettings;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.template.BlockMatchRuleTest;
import net.minecraft.world.gen.feature.template.RuleTest;
import net.minecraft.world.gen.feature.template.TagMatchRuleTest;

import static net.minecraft.world.gen.GenerationStage.Decoration.*;

public class AtumDefaultFeatures {
    public static final RuleTest NATURAL_STONE = new TagMatchRuleTest(AtumAPI.Tags.BASE_STONE_ATUM);
    public static final RuleTest LIMESTONE_CRACKED = new BlockMatchRuleTest(AtumBlocks.LIMESTONE_CRACKED);

    public static void addCarvers(BiomeGenerationSettings.Builder builder) {
        builder.withCarver(GenerationStage.Carving.AIR, AtumCarvers.CAVE_CONFIGURED);
        builder.withCarver(GenerationStage.Carving.AIR, AtumCarvers.CANYON_CONFIGURED);
    }

    public static void addSandLayer(BiomeGenerationSettings.Builder builder) {
        builder.withFeature(GenerationStage.Decoration.TOP_LAYER_MODIFICATION, AtumFeatures.SAND_LAYER_CONFIGURED);
    }

    public static void addSurfaceLavaLake(BiomeGenerationSettings.Builder builder) {
        builder.withFeature(GenerationStage.Decoration.LAKES, AtumFeatures.SURFACE_LAVA_LAKE_CONFIGURED);
    }

    public static void addSprings(BiomeGenerationSettings.Builder builder) {
        builder.withFeature(VEGETAL_DECORATION, AtumFeatures.WATER_SPRING);
        builder.withFeature(VEGETAL_DECORATION, AtumFeatures.LAVA_SPRING);
    }

    public static void addMaterialPockets(BiomeGenerationSettings.Builder builder) {
        int sandCount = AtumConfig.Helper.get(AtumConfig.WORLD_GEN.sandCount);
        if (sandCount > 0) {
            builder.withFeature(UNDERGROUND_ORES, AtumFeatures.SAND);
        }
        int limestoneGravelCount = AtumConfig.Helper.get(AtumConfig.WORLD_GEN.limestoneGravelCount);
        if (limestoneGravelCount > 0) {
            builder.withFeature(UNDERGROUND_ORES, AtumFeatures.LIMESTONE_GRAVEL);
        }
        int marlCount = AtumConfig.Helper.get(AtumConfig.WORLD_GEN.marlCount);
        if (marlCount > 0) {
            builder.withFeature(UNDERGROUND_ORES, AtumFeatures.MARL);
        }
    }

    public static void addStoneVariants(BiomeGenerationSettings.Builder builder) {
        int alabasterCount = AtumConfig.Helper.get(AtumConfig.WORLD_GEN.alabasterCount);
        if (alabasterCount > 0) {
            builder.withFeature(UNDERGROUND_ORES, AtumFeatures.ALABASTER);
        }
        int porphyryCount = AtumConfig.Helper.get(AtumConfig.WORLD_GEN.porphyryCount);
        if (porphyryCount > 0) {
            builder.withFeature(UNDERGROUND_ORES, AtumFeatures.PORPHYRY);
        }
    }

    public static void addOres(BiomeGenerationSettings.Builder builder) {
        //Vanilla based ores
        int coalCount = AtumConfig.Helper.get(AtumConfig.WORLD_GEN.coalCount);
        if (coalCount > 0) {
            builder.withFeature(UNDERGROUND_ORES, AtumFeatures.COAL_ORE);
        }
        int ironCount = AtumConfig.Helper.get(AtumConfig.WORLD_GEN.ironCount);
        if (ironCount > 0) {
            builder.withFeature(UNDERGROUND_ORES, AtumFeatures.IRON_ORE);
        }
        int goldCount = AtumConfig.Helper.get(AtumConfig.WORLD_GEN.goldCount);
        if (goldCount > 0) {
            builder.withFeature(UNDERGROUND_ORES, AtumFeatures.GOLD_ORE);
        }
        int redstoneCount = AtumConfig.Helper.get(AtumConfig.WORLD_GEN.redstoneCount);
        if (redstoneCount > 0) {
            builder.withFeature(UNDERGROUND_ORES, AtumFeatures.REDSTONE_ORE);
        }
        int diamondCount = AtumConfig.Helper.get(AtumConfig.WORLD_GEN.diamondCount);
        if (diamondCount > 0) {
            builder.withFeature(UNDERGROUND_ORES, AtumFeatures.DIAMOND_ORE);
        }
        int lapisBaseline = AtumConfig.Helper.get(AtumConfig.WORLD_GEN.lapisBaseline);
        if (lapisBaseline > 0) {
            builder.withFeature(UNDERGROUND_ORES, AtumFeatures.LAPIS_ORE);
        }

        //Atum ores
        int khnumiteCount = AtumConfig.Helper.get(AtumConfig.WORLD_GEN.khnumiteCount);
        if (khnumiteCount > 0) {
            builder.withFeature(UNDERGROUND_ORES, AtumFeatures.KHNUMITE_RAW);
        }
        int boneOreCount = AtumConfig.Helper.get(AtumConfig.WORLD_GEN.boneOreCount);
        if (boneOreCount > 0) {
            builder.withFeature(UNDERGROUND_ORES, AtumFeatures.BONE_ORE);
        }
        int relicOreCount = AtumConfig.Helper.get(AtumConfig.WORLD_GEN.relicOreCount);
        if (relicOreCount > 0) {
            builder.withFeature(UNDERGROUND_ORES, AtumFeatures.RELIC_ORE);
        }
        int nebuCount = AtumConfig.Helper.get(AtumConfig.WORLD_GEN.nebuCount);
        if (nebuCount > 0) {
            builder.withFeature(UNDERGROUND_ORES, AtumFeatures.NEBU_ORE);
        }
    }

    public static void addEmeraldOre(BiomeGenerationSettings.Builder builder) {
        boolean emeraldEnabled = AtumConfig.Helper.get(AtumConfig.WORLD_GEN.emeraldEnabled);
        if (emeraldEnabled) {
            builder.withFeature(UNDERGROUND_ORES, AtumFeatures.EMERALD_ORE);
        }
    }

    public static void addInfestedLimestone(BiomeGenerationSettings.Builder builder) {
        builder.withFeature(UNDERGROUND_DECORATION, AtumFeatures.LIMESTONE_INFESTED);
    }

    public static void addShrubs(BiomeGenerationSettings.Builder builder) {
        int shrubFrequency = AtumConfig.Helper.get(AtumConfig.WORLD_GEN.shrubFrequency);
        if (shrubFrequency > 0) {
            builder.withFeature(VEGETAL_DECORATION, AtumFeatures.SHRUB);
            builder.withFeature(VEGETAL_DECORATION, AtumFeatures.WEED);
        }
    }

    public static void addFossils(BiomeGenerationSettings.Builder builder) {
        int fossilsChance = AtumConfig.Helper.get(AtumConfig.WORLD_GEN.fossilsChance);
        if (fossilsChance > 0) {
            builder.withFeature(UNDERGROUND_DECORATION, AtumFeatures.DIRTY_BONE_FOSSILS_CONFIGURED);
        }
    }

    public static void addDungeon(BiomeGenerationSettings.Builder builder) {
        int dungeonChance = AtumConfig.Helper.get(AtumConfig.WORLD_GEN.dungeonChance);
        if (dungeonChance > 0) {
            builder.withFeature(UNDERGROUND_STRUCTURES, AtumFeatures.LIMESTONE_DUNGEONS_CONFIGURED);
        }
    }

    public static void addTomb(BiomeGenerationSettings.Builder builder) {
        builder.withStructure(AtumStructures.TOMB_FEATURE);
    }

    public static void addPyramid(BiomeGenerationSettings.Builder builder) {
        builder.withStructure(AtumStructures.PYRAMID_FEATURE);
    }

    public static void addRuins(BiomeGenerationSettings.Builder builder) {
        builder.withStructure(AtumStructures.RUIN_FEATURE);
    }

    public static void addGatehouse(BiomeGenerationSettings.Builder builder) {
        builder.withStructure(AtumStructures.GATEHOUSE_FEATURE);
    }

    public static void addGenericVillage(BiomeGenerationSettings.Builder builder) {
        builder.withStructure(AtumStructures.GENERIC_VILLAGE);
    }

    public static void addMineshaft(BiomeGenerationSettings.Builder builder, boolean isSurface) {
        double mineshaftProbability = AtumConfig.Helper.get(AtumConfig.WORLD_GEN.mineshaftProbability);
        if (mineshaftProbability > 0.0D) {
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