package com.teammetallurgy.atum.misc;

import com.electronwill.nightconfig.core.file.FileConfig;
import com.google.common.collect.HashMultimap;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.File;
import java.util.HashMap;

public class AtumConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final General GENERAL = new General(BUILDER);
    public static final AtumStart ATUM_START = new AtumStart(BUILDER);
    public static final Sandstorm SANDSTORM = new Sandstorm(BUILDER);
    public static final WorldGen WORLD_GEN = new WorldGen(BUILDER);
    public static final Biome BIOME = new Biome(BUILDER);
    public static final Mobs MOBS = new Mobs(BUILDER);

    public static class General {
        public static final String GENERAL = "general";
        public final ForgeConfigSpec.BooleanValue allowCreation;
        public final ForgeConfigSpec.BooleanValue fogEnabled;

        public General(ForgeConfigSpec.Builder builder) {
            builder.push(GENERAL);
            this.allowCreation = builder.comment("Can a non-creative user create a portal using the scarab?")
                    .translation("atum.config.portal_creation")
                    .define("Atum Portal", true);
            this.fogEnabled = builder.comment("Should clientside fog be rendered?")
                    .translation("atum.config.fog")
                    .worldRestart()
                    .define("Atum Fog", true);
            builder.pop();
        }
    }

    public static class AtumStart {
        public static final String ATUM_START = "atum start";
        public ForgeConfigSpec.BooleanValue startInAtum;
        public ForgeConfigSpec.ConfigValue<String> atumStartStructure;
        public ForgeConfigSpec.BooleanValue startInAtumPortal;

        AtumStart(ForgeConfigSpec.Builder builder) {
            builder.push(ATUM_START);
            this.startInAtum = builder.comment("New players should start in Atum?")
                    .translation("atum.config.atum_start")
                    .define("Start in Atum", false);
            this.atumStartStructure = builder.comment("Structure that will generate next to the player when starting in Atum (Requires 'Start in Atum' to be enabled). Leave empty for no structure.")
                    .translation("atum.config.atum_start_structure")
                    .define("Atum starting structure", "atum:tent_small");
            this.startInAtumPortal = builder.comment("Should a portal back to the Overworld generate, when starting in Atum?")
                    .translation("atum.config.atum_start_portal")
                    .define("Create Atum Portal", false);
            builder.pop();
        }
    }

    public static class Sandstorm {
        public static final String SANDSTORM = "sandstorm";
        public final ForgeConfigSpec.BooleanValue sandstormEnabled;
        public final ForgeConfigSpec.IntValue sandstormSandLayerChance;
        public final ForgeConfigSpec.IntValue sandstormFog;
        public final ForgeConfigSpec.IntValue sandDarkness;
        public final ForgeConfigSpec.IntValue sandAlpha;
        public final ForgeConfigSpec.IntValue sandEyesAlpha;
        public final ForgeConfigSpec.IntValue sandstormTransitionTime;

        Sandstorm(ForgeConfigSpec.Builder builder) {
            builder.push(SANDSTORM);
            this.sandstormEnabled = builder.comment("Enable/disables all functionality of sandstorms")
                    .translation("atum.configGui.sandstormenabled")
                    .define("Sandstorm Enabled", true);
            this.sandstormSandLayerChance = builder.comment("Chance for sandstorms to generate sand layers. The higher the value, the more rare it is. Set to 0 to disable.")
                    .translation("atum.configGui.sandstormsandrarity")
                    .defineInRange("Sandstorm Sand Layer", 60, 0, 10000);
            this.sandstormFog = builder.comment("Multiplier to fog during sandstorms")
                    .translation("atum.config.sandstormfog")
                    .defineInRange("Sandstorm Fog", 2, 0, 100);
            this.sandDarkness = builder.comment("How light the sand particles are")
                    .translation("atum.config.sandbrightness")
                    .defineInRange("Sandstorm Brightness", 75, 0, 100);
            this.sandAlpha = builder.comment("Base transparency for sand particles")
                    .translation("atum.config.sandalpha")
                    .defineInRange("Sandstorm Base Transparency", 10, 0, 100);
            this.sandEyesAlpha = builder.comment("Sand particle transparency while wearing Sandstorm reducing helmets")
                    .translation("atum.config.sandalpha")
                    .defineInRange("Sandstorm Helmet Transparency", 40, 0, 100);
            this.sandstormTransitionTime = builder.comment("Seconds it takes to transition from clear to sandstorm")
                    .translation("atum.config.sandstormtransition")
                    .defineInRange("Sandstorm Transition Time", 25, 0, 100);
            builder.pop();
        }
    }

    public static class WorldGen {
        public static final String WORLDGEN = "world gen";
        public static final String OREGEN = "ore gen";
        public final ForgeConfigSpec.DoubleValue mineshaftProbability;
        public final ForgeConfigSpec.BooleanValue pyramidEnabled;
        public final ForgeConfigSpec.IntValue pyramidSpacing;
        public final ForgeConfigSpec.IntValue pyramidSeparation;
        public final ForgeConfigSpec.BooleanValue ruinsEnabled;
        public final ForgeConfigSpec.IntValue ruinsSpacing;
        public final ForgeConfigSpec.IntValue ruinsSeparation;
        public final ForgeConfigSpec.IntValue ruinsAmount;
        public final ForgeConfigSpec.BooleanValue lighthouseEnabled;
        public final ForgeConfigSpec.IntValue lighthouseSpacing;
        public final ForgeConfigSpec.IntValue lighthouseSeparation;
        public final ForgeConfigSpec.IntValue shrubFrequency;
        public final ForgeConfigSpec.IntValue fossilsChance;
        public final ForgeConfigSpec.IntValue dungeonChance;
        public final ForgeConfigSpec.IntValue waterLevel;
        public final ForgeConfigSpec.BooleanValue sandLayerEdge;

        //Oregen options
        public final ForgeConfigSpec.IntValue coalVeinSize;
        public final ForgeConfigSpec.IntValue coalCount;
        public final ForgeConfigSpec.IntValue coalMaxHeight;
        public final ForgeConfigSpec.IntValue ironVeinSize;
        public final ForgeConfigSpec.IntValue ironCount;
        public final ForgeConfigSpec.IntValue ironMaxHeight;
        public final ForgeConfigSpec.IntValue goldVeinSize;
        public final ForgeConfigSpec.IntValue goldCount;
        public final ForgeConfigSpec.IntValue goldMaxHeight;
        public final ForgeConfigSpec.IntValue redstoneVeinSize;
        public final ForgeConfigSpec.IntValue redstoneCount;
        public final ForgeConfigSpec.IntValue redstoneMaxHeight;
        public final ForgeConfigSpec.IntValue diamondVeinSize;
        public final ForgeConfigSpec.IntValue diamondCount;
        public final ForgeConfigSpec.IntValue diamondMaxHeight;
        public final ForgeConfigSpec.IntValue lapisVeinSize;
        public final ForgeConfigSpec.IntValue lapisBaseline;
        public final ForgeConfigSpec.IntValue lapisSpread;
        public final ForgeConfigSpec.IntValue khnumiteVeinSize;
        public final ForgeConfigSpec.IntValue khnumiteCount;
        public final ForgeConfigSpec.IntValue khnumiteMaxHeight;
        public final ForgeConfigSpec.IntValue boneOreVeinSize;
        public final ForgeConfigSpec.IntValue boneOreCount;
        public final ForgeConfigSpec.IntValue boneOreMaxHeight;
        public final ForgeConfigSpec.IntValue relicOreVeinSize;
        public final ForgeConfigSpec.IntValue relicOreCount;
        public final ForgeConfigSpec.IntValue relicOreMaxHeight;
        public final ForgeConfigSpec.BooleanValue emeraldEnabled;
        public final ForgeConfigSpec.IntValue alabasterVeinSize;
        public final ForgeConfigSpec.IntValue alabasterCount;
        public final ForgeConfigSpec.IntValue alabasterMaxHeight;
        public final ForgeConfigSpec.IntValue porphyryVeinSize;
        public final ForgeConfigSpec.IntValue porphyryCount;
        public final ForgeConfigSpec.IntValue porphyryMaxHeight;
        public final ForgeConfigSpec.IntValue sandVeinSize;
        public final ForgeConfigSpec.IntValue sandCount;
        public final ForgeConfigSpec.IntValue sandMaxHeight;
        public final ForgeConfigSpec.IntValue limestoneGravelVeinSize;
        public final ForgeConfigSpec.IntValue limestoneGravelCount;
        public final ForgeConfigSpec.IntValue limestoneGravelMaxHeight;
        public final ForgeConfigSpec.IntValue marlVeinSize;
        public final ForgeConfigSpec.IntValue marlCount;
        public final ForgeConfigSpec.IntValue marlMaxHeight;

        WorldGen(ForgeConfigSpec.Builder builder) {
            builder.push(WORLDGEN);
            this.mineshaftProbability = builder.comment("Probability of mineshafts generating. Set to 0 to disable. Default value same as vanilla overworld")
                    .translation("atum.config.mineshaft_probability")
                    .defineInRange("Minecraft probability", 0.008D, 0.0D, 1.0D);
            this.pyramidEnabled = builder.comment("Should Pyramids generate in Atum?")
                    .translation("atum.config.pyramid_enabled")
                    .define("Enable Pyramids", true);
            this.pyramidSpacing = builder
                    .translation("atum.config.pyramid_spacing")
                    .defineInRange("Pyramid spacing", 18, 1, 256);
            this.pyramidSeparation = builder
                    .translation("atum.config.pyramid_separation")
                    .defineInRange("Pyramid separation", 8, 1, 256);
            this.ruinsEnabled = builder.comment("Should Ruins generate in Atum?")
                    .translation("atum.config.ruins_enabled")
                    .define("Enable Ruins", true);
            this.ruinsSpacing = builder
                    .translation("atum.config.ruins_spacing")
                    .defineInRange("Ruins spacing", 5, 1, 256);
            this.ruinsSeparation = builder
                    .translation("atum.config.ruins_separation")
                    .defineInRange("Ruins separation", 3, 1, 256);
            this.ruinsAmount = builder.comment("Specify the amount of ruin variants structures/ruins. Allows for additional ruin structures with a resourcepack")
                    .translation("atum.config.ruins_amount")
                    .defineInRange("Ruins Amount", 19, 1, 999);
            this.lighthouseEnabled = builder.comment("Should Lighthouses generate in Atum?")
                    .translation("atum.config.lighthouse_enabled")
                    .define("Enable Lighthouse", true);
            this.lighthouseSpacing = builder
                    .translation("atum.config.lighthouse_spacing")
                    .defineInRange("Lighthouse spacing", 10, 1, 256);
            this.lighthouseSeparation = builder
                    .translation("atum.config.lighthouse_separation")
                    .defineInRange("Lighthouse separation", 4, 1, 256);
            this.shrubFrequency = builder.defineInRange("Shrub frequency, set to 0 to disable", 1, 0, 64);
            this.fossilsChance = builder.defineInRange("Fossils chance, set to 0 to disable", 64, 0, 255);
            this.dungeonChance = builder.defineInRange("Dungeon chance, set to 0 to disable", 8, 0, 255);
            this.waterLevel = builder.comment("Sets above what Y-level water will vaporize at (Except Oasis). Set to 0 to disable")
                    .translation("atum.config.water_level")
                    .defineInRange("Water Level", 50, 0, 255);
            this.sandLayerEdge = builder.comment("Should Sand Layers generate along all edges?")
                    .translation("atum.config.sand_layer_enabled")
                    .define("Enable Sand Layer along edges", true);
            builder.pop();
            builder.push(OREGEN).comment("All vanilla based ores, uses the vanilla values by default.");
            this.coalVeinSize = builder.defineInRange("Coal vein size", 17, 1, 64);
            this.coalCount = builder.defineInRange("Coal count, set to 0 to disable", 20, 0, 64);
            this.coalMaxHeight = builder.defineInRange("Coal max height", 128, 1, 255);
            this.ironVeinSize = builder.defineInRange("Iron vein size", 9, 1, 64);
            this.ironCount = builder.defineInRange("Iron count, set to 0 to disable", 20, 0, 64);
            this.ironMaxHeight = builder.defineInRange("Iron max height", 64, 1, 255);
            this.goldVeinSize = builder.defineInRange("Gold vein size", 9, 1, 64);
            this.goldCount = builder.defineInRange("Gold count, set to 0 to disable", 2, 0, 64);
            this.goldMaxHeight = builder.defineInRange("Gold max height", 32, 1, 255);
            this.redstoneVeinSize = builder.defineInRange("Redstone vein size", 8, 1, 64);
            this.redstoneCount = builder.defineInRange("Redstone count, set to 0 to disable", 8, 0, 64);
            this.redstoneMaxHeight = builder.defineInRange("Redstone max height", 16, 1, 255);
            this.diamondVeinSize = builder.defineInRange("Diamond vein size", 8, 1, 64);
            this.diamondCount = builder.defineInRange("Diamond count, set to 0 to disable", 1, 0, 64);
            this.diamondMaxHeight = builder.defineInRange("Diamond max height", 16, 1, 255);
            this.lapisVeinSize = builder.defineInRange("Lapis vein size", 7, 1, 64);
            this.lapisBaseline = builder.defineInRange("Lapis baseline, set to 0 to disable", 16, 0, 64);
            this.lapisSpread = builder.defineInRange("Lapis spread", 16, 1, 64);
            this.khnumiteVeinSize = builder.defineInRange("Khnumite vein size", 6, 1, 64);
            this.khnumiteCount = builder.defineInRange("Khnumite count, set to 0 to disable", 4, 0, 64);
            this.khnumiteMaxHeight = builder.defineInRange("Khnumite max height", 20, 1, 255);
            this.boneOreVeinSize = builder.defineInRange("Bone Ore vein size", 9, 1, 64);
            this.boneOreCount = builder.defineInRange("Bone Ore count, set to 0 to disable", 12, 0, 64);
            this.boneOreMaxHeight = builder.defineInRange("Bone Ore max height", 128, 1, 255);
            this.relicOreVeinSize = builder.defineInRange("Relic Ore vein size", 5, 1, 64);
            this.relicOreCount = builder.defineInRange("Relic Ore count, set to 0 to disable", 4, 0, 64);
            this.relicOreMaxHeight = builder.defineInRange("Relic Ore max height", 64, 1, 255);
            this.emeraldEnabled = builder.comment("Should Emeralds generate in Atum?")
                    .translation("atum.config.emeralds_enabled")
                    .define("Enable Emeralds", true);
            this.alabasterVeinSize = builder.defineInRange("Alabaster vein size", 30, 1, 64);
            this.alabasterCount = builder.defineInRange("Alabaster count, set to 0 to disable", 10, 0, 64);
            this.alabasterMaxHeight = builder.defineInRange("Alabaster max height", 60, 1, 255);
            this.porphyryVeinSize = builder.defineInRange("Porphyry vein size", 30, 1, 64);
            this.porphyryCount = builder.defineInRange("Porphyry count, set to 0 to disable", 10, 0, 64);
            this.porphyryMaxHeight = builder.defineInRange("Porphyry max height", 60, 1, 255);
            this.sandVeinSize = builder.defineInRange("Sand pocket vein size", 28, 1, 64);
            this.sandCount = builder.defineInRange("Sand pocket count, set to 0 to disable", 14, 0, 64);
            this.sandMaxHeight = builder.defineInRange("Sand pocket max height", 255, 1, 255);
            this.limestoneGravelVeinSize = builder.defineInRange("Limestone Gravel pocket vein size", 32, 1, 64);
            this.limestoneGravelCount = builder.defineInRange("Limestone Gravel pocket count, set to 0 to disable", 10, 0, 64);
            this.limestoneGravelMaxHeight = builder.defineInRange("Limestone Gravel pocket max height", 255, 1, 255);
            this.marlVeinSize = builder.defineInRange("Marl vein size", 14, 1, 64);
            this.marlCount = builder.defineInRange("Marl count, set to 0 to disable", 8, 0, 64);
            this.marlMaxHeight = builder.defineInRange("Marl max height", 50, 1, 255);
            builder.pop();
        }
    }

    public static class Biome {
        public static final String BIOME = "biome";
        public ForgeConfigSpec.IntValue subBiomeChance;
        public ForgeConfigSpec.IntValue oasisChance;
        public ForgeConfigSpec.IntValue weight;

        Biome(ForgeConfigSpec.Builder builder) {
            builder.push(BIOME);
            this.subBiomeChance = builder.comment("By default 1 in 30 Sand Plains or Sand Dunes biomes can contain either an Oasis or Dead Oasis. Set to 0 to disable both oases biomes.")
                    .translation("atum.config.oaseschances")
                    .defineInRange("Oases chance", 30, 0, 10000);
            this.oasisChance = builder.comment("Sets the percentage chance for oases to generate as an Oasis. The remaining oases will generate as an Dead Oasis. Set to 0 to only get Dead Oasis or to 100 to only get Oasis")
                    .translation("atum.config.oasispercentage")
                    .defineInRange("Oasis percentage", 50, 0, 10000);
            builder.pop();
        }

        public Biome(ForgeConfigSpec.Builder builder, String biomeName, int weight) {
            builder.push(BIOME);
            builder.push(biomeName);
            this.weight = builder.defineInRange("weight", weight, -1, 1000);
            builder.pop(2);
        }
    }

    public static class Mobs {
        public static final String MOBS = "mobs";
        public static HashMap<EntityType<?>, EntityClassification> ENTITY_CLASSIFICATION = new HashMap<>();
        public static HashMultimap<ResourceLocation, EntityType<?>> ENTITY_TYPE = HashMultimap.create();
        public ForgeConfigSpec.IntValue min;
        public ForgeConfigSpec.IntValue max;
        public ForgeConfigSpec.IntValue weight;

        public ForgeConfigSpec.IntValue banditPatrolFrequency;
        public ForgeConfigSpec.IntValue markedForDeathTimeBaseValue;

        public Mobs(ForgeConfigSpec.Builder builder) {
            builder.push(MOBS);
            this.banditPatrolFrequency = builder.comment("How frequent Bandit patrols are. The higher the number, the less patrols will spawn")
                    .defineInRange("banditPatrolFrequency", 330, -1, 10000);
            this.markedForDeathTimeBaseValue = builder.comment("How long time is required for an Assassin to spawn. The higher the number, the less frequent Assassin will spawn")
                    .defineInRange("markedForDeathFrequency", 1000, 1, 10000);
            builder.pop();
        }

        public Mobs(ForgeConfigSpec.Builder builder, String mobName, int min, int max, int weight, EntityType<?> entityType, EntityClassification classification, ResourceLocation biomeName) {
            ENTITY_CLASSIFICATION.put(entityType, classification);
            ENTITY_TYPE.put(biomeName, entityType);
            builder.push(MOBS);
            builder.push(mobName);
            this.min = builder.defineInRange("min", min, -1, 63);
            this.max = builder.defineInRange("max", max, 1, 64);
            this.weight = builder.defineInRange("weight", weight, -1, 1000);
            builder.pop(2);
        }
    }

    public static class ModIntegration {
        public static final String MOD_INTEGRATION = "mod integration";

        public ModIntegration(ForgeConfigSpec.Builder builder, String modName, boolean defaultValue) {
            builder.push(MOD_INTEGRATION);
            builder.define(modName, defaultValue);
            builder.pop();
        }
    }

    public static ForgeConfigSpec spec = BUILDER.build();

    public static class Helper {
        private static final FileConfig CONFIG_FILE = FileConfig.of(new File(FMLPaths.CONFIGDIR.get().toFile(), "atum-common.toml"));

        public static <T> T get(String category, String subCategory, String value) {
            return get(category + "." + subCategory, value);
        }

        public static <T> T get(String category, String value) {
            return get(category + "." + value);
        }

        public static <T> T get(String category) {
            CONFIG_FILE.load();
            return CONFIG_FILE.get(category);
        }

        public static String getSubConfig(String category, String subCategory) {
            return category + "." + subCategory;
        }
    }
}