package com.teammetallurgy.atum.integration;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.integration.champion.ChampionsHelper;
import com.teammetallurgy.atum.integration.theoneprobe.TOPSupport;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.ModList;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class IntegrationHandler {
    public static final IntegrationHandler INSTANCE = new IntegrationHandler();
    private final NonNullList<IModIntegration> integratedMods = NonNullList.create();
    private HashMap<String, Class<? extends IModIntegration>> mods = new HashMap<>();
    private static HashMap<String, Boolean> defaultConfig = new HashMap<>();

    public void initModIntegration() {
        //addSupport(Thaumcraft.THAUMCRAFT_ID, Thaumcraft.class, true);
        addSupport(TOPSupport.THE_ONE_PROBE, TOPSupport.class, true);
        addSupport(ChampionsHelper.CHAMPION_ID, ChampionsHelper.class, false);

        List<String> enabledModSupport = mods.keySet().stream().filter(IntegrationHandler::getConfigValue).collect(Collectors.toList());
        //AtumConfig.config.save(); //TODO

        mods.entrySet().stream().filter(entry -> enabledModSupport.contains(entry.getKey()) && ModList.get().isLoaded(entry.getKey())).forEach(entry -> {
            try {
                integratedMods.add(entry.getValue().newInstance());
            } catch (Exception e) {
                Atum.LOG.error("Failed to load mod integration handler");
                e.printStackTrace();
            }
        });
    }

    private void addSupport(String modID, Class<? extends IModIntegration> modClass, boolean configValue) {
        mods.put(modID, modClass);
        defaultConfig.put(modID, configValue);
    }

    public static boolean getConfigValue(String modID) {
        return true;
        //return AtumConfig.config.get(AtumConfig.MOD_INTEGRATION, modID, defaultConfig.get(modID)).getBoolean(); //TODO
    }

    public void setup() {
        for (IModIntegration modSupport : integratedMods) {
            try {
                modSupport.setup();
            } catch (Exception e) {
                Atum.LOG.error("Failed to load mod integration from " + modSupport.getClass() + " during setup");
                e.printStackTrace();
            }
        }
    }

    public void clientSide() {
        for (IModIntegration modSupport : integratedMods) {
            try {
                modSupport.clientSide();
            } catch (Exception e) {
                Atum.LOG.error("Failed to load mod integration from " + modSupport.getClass() + " on the client.");
                e.printStackTrace();
            }
        }
    }
}