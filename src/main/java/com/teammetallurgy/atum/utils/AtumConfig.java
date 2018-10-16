package com.teammetallurgy.atum.utils;

import com.teammetallurgy.atum.world.biome.base.AtumBiome;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static net.minecraftforge.common.config.Configuration.CATEGORY_GENERAL;
import static net.minecraftforge.common.config.Configuration.CATEGORY_SPLITTER;

public class AtumConfig {
    public static Configuration config;

    public static final String WORLDGEN = "worldgen";
    public static boolean ALLOW_CREATION;
    public static boolean FOG_ENABLED;
    public static boolean PYRAMID_ENABLED;
    public static boolean COAL_ENABLED;
    public static boolean IRON_ENABLED;
    public static boolean GOLD_ENABLED;
    public static boolean REDSTONE_ENABLED;
    public static boolean DIAMOND_ENABLED;
    public static boolean EMERALD_ENABLED;
    public static boolean LAPIS_ENABLED;

    public static int DIMENSION_ID;
    public static int COAL_VEIN;
    public static int IRON_VEIN;
    public static int GOLD_VEIN;
    public static int REDSTONE_VEIN;
    public static int DIAMOND_VEIN;
    public static int EMERALD_VEIN;
    public static int LAPIS_VEIN;


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

        prop = config.get(CATEGORY_GENERAL, "Atum Fog", true);
        prop.setComment("Should clientside fog be rendered?");
        prop.setLanguageKey("atum.configGui.fog");
        FOG_ENABLED = prop.getBoolean(true);
        propOrder.add(prop.getName());

        prop = config.get(CATEGORY_GENERAL, "Atum Dimension ID", 17);
        prop.setComment("The ID of the Atum Dimension");
        prop.setLanguageKey("atum.configGui.dimensionID").setRequiresMcRestart(true);
        DIMENSION_ID = prop.getInt();
        propOrder.add(prop.getName());

        ////////// biomes
        for (AtumBiome biome : AtumRegistry.BIOMES) {
            prop = config.get(CATEGORY_GENERAL, "Atum " + biome.getBiomeName() + " Biome Weight", biome.getWeight());
            prop.setComment("The weight of the Atum biome " + biome.toString());
            prop.setLanguageKey("atum.configGui.biomeWeight." + biome.getBiomeName()).setRequiresMcRestart(true);
            biome.setWeight(prop.getInt());
            propOrder.add(prop.getName());
        }

        PYRAMID_ENABLED = config.getBoolean("Should Pyramids generate in Atum?", WORLDGEN, true, "Set to true to enable Pyramids");

        ////////// features
        COAL_ENABLED = config.getBoolean("Generate Coal", WORLDGEN + CATEGORY_SPLITTER + "Coal ore", true, "Should coal ore generate in Atum?");
        COAL_VEIN = config.getInt("Size", WORLDGEN + CATEGORY_SPLITTER + "Coal ore", 16, 0, 64, "Coal vein size");

        IRON_ENABLED = config.getBoolean("Generate Iron", WORLDGEN + CATEGORY_SPLITTER + "Iron ore", true, "Should iron ore generate in Atum?");
        IRON_VEIN = config.getInt("Size", WORLDGEN + CATEGORY_SPLITTER + "Iron ore", 8, 0, 64, "Iron vein size");

        GOLD_ENABLED = config.getBoolean("Generate Gold", WORLDGEN + CATEGORY_SPLITTER + "Gold ore", true, "Should gold ore generate in Atum?");
        GOLD_VEIN = config.getInt("Size", WORLDGEN + CATEGORY_SPLITTER + "Gold ore", 8, 0, 64, "Gold vein size");

        REDSTONE_ENABLED = config.getBoolean("Generate Redstone", WORLDGEN + CATEGORY_SPLITTER + "Redstone ore", true, "Should redstone ore generate in Atum?");
        REDSTONE_VEIN = config.getInt("Size", WORLDGEN + CATEGORY_SPLITTER + "Redstone ore", 7, 0, 64, "Redstone vein size");

        DIAMOND_ENABLED = config.getBoolean("Generate Diamond", WORLDGEN + CATEGORY_SPLITTER + "Diamond ore", true, "Should diamond ore generate in Atum?");
        DIAMOND_VEIN = config.getInt("Size", WORLDGEN + CATEGORY_SPLITTER + "Diamond ore", 7, 0, 64, "Diamond vein size");

        EMERALD_ENABLED = config.getBoolean("Generate Emerald", WORLDGEN + CATEGORY_SPLITTER + "Emerald ore", true, "Should emerald ore generate in Atum?");
        EMERALD_VEIN = config.getInt("Size", WORLDGEN + CATEGORY_SPLITTER + "Emerald ore", 5, 0, 64, "Emerald vein size");

        LAPIS_ENABLED = config.getBoolean("Generate Lapis", WORLDGEN + CATEGORY_SPLITTER + "Lapis ore", true, "Should lapis ore generate in Atum?");
        LAPIS_VEIN = config.getInt("Size", WORLDGEN + CATEGORY_SPLITTER + "Lapis ore", 6, 0, 64, "Lapis vein size");

        config.setCategoryPropertyOrder(CATEGORY_GENERAL, propOrder);

        if (config.hasChanged()) {
            config.save();
        }
    }
}