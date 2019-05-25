package com.teammetallurgy.atum.utils;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static net.minecraftforge.common.config.Configuration.CATEGORY_GENERAL;

public class AtumConfig {
    public static Configuration config;
    //Categories
    public static final String WORLDGEN = "world gen";
    public static final String OREGEN = AtumConfig.WORLDGEN + Configuration.CATEGORY_SPLITTER + "ore gen";
    public static final String BIOME = "biome";
    public static final String MOBS = "mobs";
    public static final String ATUM_START = "atum start";
    public static final String MOD_INTEGRATION = "mod integration";
    public static final String SANDSTORM = "sandstorm";
    //Config entries
    public static boolean ALLOW_CREATION;
    public static boolean FOG_ENABLED;
    public static boolean PYRAMID_ENABLED;
    public static boolean START_IN_ATUM;
    public static String ATUM_START_STRUCTURE;
    public static boolean START_IN_ATUM_PORTAL;
    public static int DIMENSION_ID;
    public static boolean RECIPE_OVERRIDING;

    public static float SANDSTORM_FOG;
    public static float SAND_DARKNESS;
    public static float SAND_ALPHA;
    public static float SAND_EYES_ALPHA;
    public static int SANDSTORM_TRANSITION_TIME;

    public AtumConfig(File file) {
        AtumConfig.config = new Configuration(file);

        MinecraftForge.EVENT_BUS.register(this);
        syncConfigData();
    }

    @SubscribeEvent
    public void onConfigChange(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(Constants.MOD_ID)) {
            syncConfigData();
        }
    }

    private void syncConfigData() {
        List<String> propOrder = new ArrayList<>();
        Property prop;

        prop = config.get(CATEGORY_GENERAL, "Atum Portal", true);
        prop.setComment("Can a non-creative user create a portal using the scarab?");
        prop.setLanguageKey("atum.configGui.portalCreation");
        ALLOW_CREATION = prop.getBoolean(true);
        propOrder.add(prop.getName());

        prop = config.get(ATUM_START, "Start in Atum", false);
        prop.setComment("New players should start in Atum?");
        prop.setLanguageKey("atum.configGui.atumStart");
        START_IN_ATUM = prop.getBoolean();
        propOrder.add(prop.getName());

        prop = config.get(ATUM_START, "Atum starting structure", "atum:tent_small");
        prop.setComment("Structure that will generate next to the player when starting in Atum (Requires 'Start in Atum' to be enabled). Leave empty for no structure.");
        prop.setLanguageKey("atum.configGui.atumStartStructure");
        ATUM_START_STRUCTURE = prop.getString();
        propOrder.add(prop.getName());

        prop = config.get(ATUM_START, "Create Atum Portal", false);
        prop.setComment("Should a portal back to the Overworld generate, when starting in Atum?");
        prop.setLanguageKey("atum.configGui.atumStartPortal");
        START_IN_ATUM_PORTAL = prop.getBoolean();
        propOrder.add(prop.getName());

        prop = config.get(CATEGORY_GENERAL, "Atum Fog", true);
        prop.setComment("Should clientside fog be rendered?");
        prop.setLanguageKey("atum.configGui.fog").setRequiresMcRestart(true);
        FOG_ENABLED = prop.getBoolean(true);
        propOrder.add(prop.getName());

        prop = config.get(CATEGORY_GENERAL, "Atum Dimension ID", 17);
        prop.setComment("The ID of the Atum Dimension. Changing this will break worlds loaded up with the default ID");
        prop.setLanguageKey("atum.configGui.dimensionID").setRequiresMcRestart(true);
        DIMENSION_ID = prop.getInt();
        propOrder.add(prop.getName());

        RECIPE_OVERRIDING = config.getBoolean("Enable Atum recipe overriding?", CATEGORY_GENERAL, true, "Atum is removing and adding some vanilla recipes, in order for certain recipes to get priority. It should not break anything. This is a fail safe if it does.");

        prop = config.get(SANDSTORM, "Sandstorm Fog", 2);
        prop.setComment("Multiplier to fog during sandstorms");
        prop.setLanguageKey("atum.configGui.sandstormfog");
        SANDSTORM_FOG = prop.getLong();
        propOrder.add(prop.getName());

        prop = config.get(SANDSTORM, "Sandstorm Brightness", 75);
        prop.setComment("How light the sand particles are (0 - 100)");
        prop.setLanguageKey("atum.configGui.sandbrightness");
        SAND_DARKNESS = prop.getLong() / 100f;
        propOrder.add(prop.getName());

        prop = config.get(SANDSTORM, "Sandstorm Base Transparency", 10);
        prop.setComment("Base transparency for sand particles (0 - 100)");
        prop.setLanguageKey("atum.configGui.sandalpha");
        SAND_ALPHA = prop.getLong() / 100f;
        propOrder.add(prop.getName());

        prop = config.get(SANDSTORM, "Sandstorm Eyes Transparency", 40);
        prop.setComment("Sand particle transparency while wearing Eyes of Atum (0 - 100)");
        prop.setLanguageKey("atum.configGui.eyesalpha");
        SAND_EYES_ALPHA = prop.getLong() / 100f;
        propOrder.add(prop.getName());

        prop = config.get(SANDSTORM, "Sandstorm Transition Time", 25);
        prop.setComment("Seconds it takes to transition from clear to sandstorm");
        prop.setLanguageKey("atum.configGui.sandstormtransition");
        SANDSTORM_TRANSITION_TIME = prop.getInt();
        propOrder.add(prop.getName());

        PYRAMID_ENABLED = config.getBoolean("Should Pyramids generate in Atum?", WORLDGEN, true, "Set to true to enable Pyramids");

        config.setCategoryPropertyOrder(CATEGORY_GENERAL, propOrder);

        if (config.hasChanged()) {
            config.save();
        }
    }
}