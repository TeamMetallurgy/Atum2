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
        if (AtumConfig.WORLD_GEN.sandCount.get() > 0) {
            builder.withFeature(UNDERGROUND_ORES, AtumFeatures.SAND);
        }
        if (AtumConfig.WORLD_GEN.limestoneGravelCount.get() > 0) {
            builder.withFeature(UNDERGROUND_ORES, AtumFeatures.LIMESTONE_GRAVEL);
        }
        if (AtumConfig.WORLD_GEN.marlCount.get() > 0) {
            builder.withFeature(UNDERGROUND_ORES, AtumFeatures.MARL);
        }
    }

    public static void addStoneVariants(BiomeGenerationSettings.Builder builder) {
        if (AtumConfig.WORLD_GEN.alabasterCount.get() > 0) {
            builder.withFeature(UNDERGROUND_ORES, AtumFeatures.ALABASTER);
        }
        if (AtumConfig.WORLD_GEN.porphyryCount.get() > 0) {
            builder.withFeature(UNDERGROUND_ORES, AtumFeatures.PORPHYRY);
        }
    }

    public static void addOres(BiomeGenerationSettings.Builder builder) {
        //Vanilla based ores
        if (AtumConfig.WORLD_GEN.coalCount.get() > 0) {
            builder.withFeature(UNDERGROUND_ORES, AtumFeatures.COAL_ORE);
        }
        if (AtumConfig.WORLD_GEN.ironCount.get() > 0) {
            builder.withFeature(UNDERGROUND_ORES, AtumFeatures.IRON_ORE);
        }
        if (AtumConfig.WORLD_GEN.goldCount.get() > 0) {
            builder.withFeature(UNDERGROUND_ORES, AtumFeatures.GOLD_ORE);
        }
        if (AtumConfig.WORLD_GEN.redstoneCount.get() > 0) {
            builder.withFeature(UNDERGROUND_ORES, AtumFeatures.REDSTONE_ORE);
        }
        if (AtumConfig.WORLD_GEN.diamondCount.get() > 0) {
            builder.withFeature(UNDERGROUND_ORES, AtumFeatures.DIAMOND_ORE);
        }
        if (AtumConfig.WORLD_GEN.lapisBaseline.get() > 0) {
            builder.withFeature(UNDERGROUND_ORES, AtumFeatures.LAPIS_ORE);
        }

        //Atum ores
        if (AtumConfig.WORLD_GEN.khnumiteCount.get() > 0) {
            builder.withFeature(UNDERGROUND_ORES, AtumFeatures.KHNUMITE_RAW);
        }
        if (AtumConfig.WORLD_GEN.boneOreCount.get() > 0) {
            builder.withFeature(UNDERGROUND_ORES, AtumFeatures.BONE_ORE);
        }
        if (AtumConfig.WORLD_GEN.relicOreCount.get() > 0) {
            builder.withFeature(UNDERGROUND_ORES, AtumFeatures.RELIC_ORE);
        }
        if (AtumConfig.WORLD_GEN.nebuCount.get() > 0) {
            builder.withFeature(UNDERGROUND_ORES, AtumFeatures.NEBU_ORE);
        }
    }

    public static void addEmeraldOre(BiomeGenerationSettings.Builder builder) {
        if (AtumConfig.WORLD_GEN.emeraldEnabled.get()) {
            builder.withFeature(UNDERGROUND_ORES, AtumFeatures.EMERALD_ORE);
        }
    }

    public static void addInfestedLimestone(BiomeGenerationSettings.Builder builder) {
        builder.withFeature(UNDERGROUND_DECORATION, AtumFeatures.LIMESTONE_INFESTED);
    }

    public static void addShrubs(BiomeGenerationSettings.Builder builder) {
        if (AtumConfig.WORLD_GEN.shrubFrequency.get() > 0) {
            builder.withFeature(VEGETAL_DECORATION, AtumFeatures.SHRUB);
            builder.withFeature(VEGETAL_DECORATION, AtumFeatures.WEED);
        }
    }

    public static void addFossils(BiomeGenerationSettings.Builder builder) {
        if (AtumConfig.WORLD_GEN.fossilsChance.get() > 0) {
            builder.withFeature(UNDERGROUND_DECORATION, AtumFeatures.DIRTY_BONE_FOSSILS_CONFIGURED);
        }
    }

    public static void addDungeon(BiomeGenerationSettings.Builder builder) {
        if (AtumConfig.WORLD_GEN.dungeonChance.get() > 0) {
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