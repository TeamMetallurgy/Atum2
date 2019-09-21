package com.teammetallurgy.atum.utils;

import net.minecraftforge.common.ForgeConfigSpec;

public class AtumConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final General GENERAL = new General(BUILDER);
    public static final AtumStart ATUM_START = new AtumStart(BUILDER);
    public static final Sandstorm SANDSTORM = new Sandstorm(BUILDER);
    public static final WorldGen WORLD_GEN = new WorldGen(BUILDER);
    public static final Biome BIOME = new Biome(BUILDER);

    //Categories
    //public static final String OREGEN = AtumConfig.WORLDGEN + Configuration.CATEGORY_SPLITTER + "ore gen";
    public static final String MOBS = "mobs";
    public static final String MOD_INTEGRATION = "mod integration";

    public static class General {
        public static final String GENERAL = "general";
        public ForgeConfigSpec.BooleanValue allowCreation;
        public ForgeConfigSpec.BooleanValue fogEnabled;
        //public static boolean RECIPE_OVERRIDING; //TODO Figure out if there is another way in 1.14

        public General(ForgeConfigSpec.Builder builder) {
            builder.push(GENERAL);
            allowCreation = builder.comment("Can a non-creative user create a portal using the scarab?")
                    .translation("atum.config.portal_creation")
                    .define("Atum Portal", true);
            fogEnabled = builder.comment("Should clientside fog be rendered?")
                    .translation("atum.config.fog")
                    .worldRestart()
                    .define("Atum Fog", true);
            builder.pop();
            //RECIPE_OVERRIDING = config.getBoolean("Enable Atum recipe overriding?", CATEGORY_GENERAL, true, "Atum is removing and adding some vanilla recipes, in order for certain recipes to get priority. It should not break anything. This is a fail safe if it does.");
        }
    }

    public static class AtumStart {
        public static final String ATUM_START = "atum start";
        public ForgeConfigSpec.BooleanValue startInAtum;
        public ForgeConfigSpec.ConfigValue<String> atumStartStructure;
        public ForgeConfigSpec.BooleanValue startInAtumPortal;

        public AtumStart(ForgeConfigSpec.Builder builder) {
            builder.push(ATUM_START);
            startInAtum = builder.comment("New players should start in Atum?")
                    .translation("atum.config.atum_start")
                    .define("Start in Atum", false);
            atumStartStructure = builder.comment("Structure that will generate next to the player when starting in Atum (Requires 'Start in Atum' to be enabled). Leave empty for no structure.")
                    .translation("atum.config.atum_start_structure")
                    .define("Atum starting structure", "atum:tent_small");
            startInAtum = builder.comment("Should a portal back to the Overworld generate, when starting in Atum?")
                    .translation("atum.config.atum_start_portal")
                    .define("Create Atum Portal", false);
            builder.pop();
        }
    }

    public static class Sandstorm {
        public static final String SANDSTORM = "sandstorm";
        public ForgeConfigSpec.IntValue sandstormFog;
        public ForgeConfigSpec.IntValue sandDarkness;
        public ForgeConfigSpec.IntValue sandAlpha;
        public ForgeConfigSpec.IntValue sandEyesAlpha;
        public ForgeConfigSpec.IntValue sandstormTransitionTime;

        public Sandstorm(ForgeConfigSpec.Builder builder) {
            builder.push(SANDSTORM);
            sandstormFog = builder.comment("Multiplier to fog during sandstorms")
                    .translation("atum.config.sandstormfog")
                    .defineInRange("Sandstorm Fog", 2, 0, 100);
            sandDarkness = builder.comment("How light the sand particles are")
                    .translation("atum.config.sandbrightness")
                    .defineInRange("Sandstorm Brightness", 75, 0, 100);
            sandAlpha = builder.comment("Base transparency for sand particles")
                    .translation("atum.config.sandalpha")
                    .defineInRange("Sandstorm Base Transparency", 10, 0, 100);
            sandEyesAlpha = builder.comment("Sand particle transparency while wearing Sandstorm reducing helmets")
                    .translation("atum.config.sandalpha")
                    .defineInRange("Sandstorm Helmet Transparency", 40, 0, 100);
            sandstormTransitionTime = builder.comment("Seconds it takes to transition from clear to sandstorm")
                    .translation("atum.config.sandstormtransition")
                    .defineInRange("Sandstorm Transition Time", 25, 0, 100);
            builder.pop();
        }
    }

    public static class WorldGen {
        public static final String WORLDGEN = "world gen";
        public ForgeConfigSpec.BooleanValue pyramidEnabled;
        public ForgeConfigSpec.IntValue waterLevel;

        public WorldGen(ForgeConfigSpec.Builder builder) {
            builder.push(WORLDGEN);
            pyramidEnabled = builder.comment("Should Pyramids generate in Atum?")
                    .translation("atum.config.pyramid_enabled")
                    .define("Enable Pyramids", true);
            waterLevel = builder.comment("Sets above what Y-level water will vaporize at (Except Oasis). Set to 0 to disable")
                    .translation("atum.config.water_level")
                    .defineInRange("Water Level", 50, 0, 255);
            builder.pop();
        }
    }

    public static class Biome {
        public static final String BIOME = "biome";
        public ForgeConfigSpec.IntValue subBiomeChance;
        public ForgeConfigSpec.IntValue oasisChance;

        public Biome(ForgeConfigSpec.Builder builder) {
            builder.push(BIOME);
            subBiomeChance = builder.comment("By default 1 in 30 Sand Plains or Sand Dunes biomes can contain either an Oasis or Dead Oasis. Set to 0 to disable both oases biomes.")
                    .translation("atum.config.oaseschances")
                    .defineInRange("Oases chance", 30, 0, 10000);
            oasisChance = builder.comment("Sets the percentage chance for oases to generate as an Oasis. The remaining oases will generate as an Dead Oasis. Set to 0 to only get Dead Oasis or to 100 to only get Oasis")
                    .translation("atum.config.oasispercentage")
                    .defineInRange("Oasis percentage", 50, 0, 10000);
            builder.pop();
        }
    }

    public static ForgeConfigSpec spec = BUILDER.build();
}