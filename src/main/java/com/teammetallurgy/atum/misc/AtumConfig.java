package com.teammetallurgy.atum.misc;

import com.electronwill.nightconfig.core.file.FileConfig;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.io.File;

public class AtumConfig {
    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final General GENERAL = new General(BUILDER);
    public static final AtumStart ATUM_START = new AtumStart(BUILDER);
    public static final Sandstorm SANDSTORM = new Sandstorm(BUILDER);
    public static final Mobs MOBS = new Mobs(BUILDER);
    public static final WorldGen WORLD_GEN = new WorldGen(BUILDER);

    public static class General {
        public static final String GENERAL = "general";
        public final ModConfigSpec.BooleanValue allowCreation;
        public final ModConfigSpec.IntValue fogDensity;

        public General(ModConfigSpec.Builder builder) {
            builder.push(GENERAL);
            this.allowCreation = builder.comment("Can a non-creative user create a portal using the scarab?")
                    .translation("atum.config.portal_creation")
                    .define("Atum Portal", true);
            this.fogDensity = builder.comment("Value that determines how dense the fog is in Atum. Set to 0 to disable fog completely.")
                    .translation("atum.config.fog")
                    .defineInRange("Atum Fog Density", 200, 0, 10000);
            builder.pop();
        }
    }

    public static class AtumStart {
        public static final String ATUM_START = "atum start";
        public ModConfigSpec.BooleanValue startInAtum;
        public ModConfigSpec.ConfigValue<String> atumStartStructure;
        public ModConfigSpec.BooleanValue startInAtumPortal;

        AtumStart(ModConfigSpec.Builder builder) {
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
        public final ModConfigSpec.BooleanValue sandstormEnabled;
        public final ModConfigSpec.IntValue sandstormSandLayerChance;
        public final ModConfigSpec.IntValue sandstormFog;
        public final ModConfigSpec.IntValue sandDarkness;
        public final ModConfigSpec.IntValue sandAlpha;
        public final ModConfigSpec.IntValue sandEyesAlpha;
        public final ModConfigSpec.IntValue sandstormTransitionTime;

        Sandstorm(ModConfigSpec.Builder builder) {
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
        public final ModConfigSpec.IntValue ruinsAmount;

        WorldGen(ModConfigSpec.Builder builder) {
            builder.push(WORLDGEN);
            this.ruinsAmount = builder.comment("Specify the amount of ruin variants structures/ruins. Allows for additional ruin structures with a resourcepack")
                    .translation("atum.config.ruins_amount")
                    .defineInRange("Ruins Amount", 17, 1, 999);
            builder.pop();
        }
    }

    public static class Mobs {
        public static final String MOBS = "mobs";
        public ModConfigSpec.IntValue banditPatrolFrequency;
        public ModConfigSpec.IntValue markedForDeathTimeBaseValue;
        public ModConfigSpec.BooleanValue displayPharaohSlainMessage;

        public Mobs(ModConfigSpec.Builder builder) {
            builder.push(MOBS);
            this.banditPatrolFrequency = builder.comment("How frequent Bandit patrols are. The higher the number, the less patrols will spawn")
                    .defineInRange("banditPatrolFrequency", 20000, -1, 100000);
            this.markedForDeathTimeBaseValue = builder.comment("How long time is required for an Assassin to spawn. The higher the number, the less frequent Assassin will spawn")
                    .defineInRange("markedForDeathFrequency", 1000, 1, 10000);
            this.displayPharaohSlainMessage = builder.comment("Whether a message should be broadcast to everybody, when a Pharaoh have been killed")
                    .define("displayPharaohSlainMessage", true);
            builder.pop();
        }
    }

    public static class ModIntegration {
        public static final String MOD_INTEGRATION = "mod integration";

        public ModIntegration(ModConfigSpec.Builder builder, String modName, boolean defaultValue) {
            builder.push(MOD_INTEGRATION);
            builder.define(modName, defaultValue);
            builder.pop();
        }
    }

    public static ModConfigSpec spec = BUILDER.build();

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