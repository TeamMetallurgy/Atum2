package com.teammetallurgy.atum.misc;

import com.electronwill.nightconfig.core.file.FileConfig;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.File;

public class AtumConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final General GENERAL = new General(BUILDER);
    public static final AtumStart ATUM_START = new AtumStart(BUILDER);
    public static final Sandstorm SANDSTORM = new Sandstorm(BUILDER);
    public static final WorldGen WORLD_GEN = new WorldGen(BUILDER);
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

    public static class WorldGen { //TODO. Probably only kept for reference to values for now
        public static final String WORLDGEN = "world gen";
        public final ForgeConfigSpec.DoubleValue mineshaftProbability;
        public final ForgeConfigSpec.IntValue ruinsAmount;
        public final ForgeConfigSpec.IntValue fossilsChance;
        public final ForgeConfigSpec.BooleanValue sandLayerEdge;

        WorldGen(ForgeConfigSpec.Builder builder) {
            builder.push(WORLDGEN);
            this.mineshaftProbability = builder.comment("Probability of mineshafts generating. Set to 0 to disable. Default value same as vanilla overworld")
                    .translation("atum.config.mineshaft_probability")
                    .defineInRange("Minecraft probability", 0.007D, 0.0D, 1.0D);
            this.ruinsAmount = builder.comment("Specify the amount of ruin variants structures/ruins. Allows for additional ruin structures with a resourcepack")
                    .translation("atum.config.ruins_amount")
                    .defineInRange("Ruins Amount", 17, 1, 999);
            this.fossilsChance = builder.defineInRange("Fossils chance, set to 0 to disable", 64, 0, 255);
            this.sandLayerEdge = builder.comment("Should Sand Layers generate along all edges?")
                    .translation("atum.config.sand_layer_enabled")
                    .define("Enable Sand Layer along edges", true);
            builder.pop();
        }
    }

    public static class Mobs {
        public static final String MOBS = "mobs";
        public ForgeConfigSpec.IntValue banditPatrolFrequency;
        public ForgeConfigSpec.IntValue markedForDeathTimeBaseValue;
        public ForgeConfigSpec.BooleanValue displayPharaohSlainMessage;

        public Mobs(ForgeConfigSpec.Builder builder) {
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