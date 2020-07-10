package com.teammetallurgy.atum.misc;

import com.electronwill.nightconfig.core.file.FileConfig;
import com.google.common.collect.HashMultimap;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
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
        public static final String OREGEN = WORLDGEN + "." + "ore gen";
        public final ForgeConfigSpec.BooleanValue mineshaftEnabled;
        public final ForgeConfigSpec.BooleanValue pyramidEnabled;
        public final ForgeConfigSpec.BooleanValue ruinsEnabled;
        public final ForgeConfigSpec.IntValue ruinsAmount;
        public final ForgeConfigSpec.IntValue waterLevel;
        public final ForgeConfigSpec.BooleanValue sandLayerEdge;

        WorldGen(ForgeConfigSpec.Builder builder) {
            builder.push(WORLDGEN);
            this.mineshaftEnabled = builder.comment("Should Mineshafts generate in Atum?")
                    .translation("atum.config.mineshaft_enabled")
                    .define("Enable Mineshafts", true);
            this.pyramidEnabled = builder.comment("Should Pyramids generate in Atum?")
                    .translation("atum.config.pyramid_enabled")
                    .define("Enable Pyramids", true);
            this.ruinsEnabled = builder.comment("Should Ruins generate in Atum?")
                    .translation("atum.config.ruins_enabled")
                    .define("Enable Ruins", true);
            this.ruinsAmount = builder.comment("Specify the amount of ruin variants structures/ruins. Allows for additional ruin structures with a resourcepack")
                    .translation("atum.config.ruins_amount")
                    .defineInRange("Ruins Amount", 19, 1, 999);
            this.waterLevel = builder.comment("Sets above what Y-level water will vaporize at (Except Oasis). Set to 0 to disable")
                    .translation("atum.config.water_level")
                    .defineInRange("Water Level", 50, 0, 255);
            this.sandLayerEdge = builder.comment("Should Sand Layers generate along all edges?")
                    .translation("atum.config.sand_layer_enabled")
                    .define("Enable Sand Layer along edges", true);
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
        public static HashMultimap<net.minecraft.world.biome.Biome, EntityType<?>> ENTITY_TYPE = HashMultimap.create();
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

        public Mobs(ForgeConfigSpec.Builder builder, String mobName, int min, int max, int weight, EntityType<?> entityType, EntityClassification classification, net.minecraft.world.biome.Biome biome) {
            ENTITY_CLASSIFICATION.put(entityType, classification);
            ENTITY_TYPE.put(biome, entityType);
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
            CONFIG_FILE.load();
            return CONFIG_FILE.get(category + "." + value);
        }

        public static String getSubConfig(String category, String subCategory) {
            return category + "." + subCategory;
        }
    }
}