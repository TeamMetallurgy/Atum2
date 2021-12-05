package com.teammetallurgy.atum.integration;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.integration.theoneprobe.TOPSupport;
import com.teammetallurgy.atum.misc.AtumConfig;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.ModList;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class IntegrationHandler {
    public static final IntegrationHandler INSTANCE = new IntegrationHandler();
    private final NonNullList<IModIntegration> integratedMods = NonNullList.create();
    private HashMap<String, Class<? extends IModIntegration>> mods = new HashMap<>();

    public void addSupport() {
        //addSupport(Thaumcraft.THAUMCRAFT_ID, Thaumcraft.class, true);
        addSupport(TOPSupport.THE_ONE_PROBE, TOPSupport.class, true);
    }

    public void init() {
        List<String> enabledModSupport = mods.keySet().stream().filter(IntegrationHandler::getConfigValue).collect(Collectors.toList());
        this.mods.entrySet().stream().filter(entry -> enabledModSupport.contains(entry.getKey()) && ModList.get().isLoaded(entry.getKey())).forEach(entry -> {
            try {
                this.integratedMods.add(entry.getValue().newInstance());
            } catch (Exception e) {
                Atum.LOG.error("Failed to load mod integration handler");
                e.printStackTrace();
            }
        });
    }

    private void addSupport(String modID, Class<? extends IModIntegration> modClass, boolean defaultValue) {
        this.mods.put(modID, modClass);
        new AtumConfig.ModIntegration(AtumConfig.BUILDER, modID, defaultValue); //Write config
    }

    public static boolean getConfigValue(String modID) {
        return AtumConfig.Helper.get(AtumConfig.ModIntegration.MOD_INTEGRATION, modID);
    }

    public void setup() {
        for (IModIntegration modSupport : this.integratedMods) {
            try {
                modSupport.setup();
            } catch (Exception e) {
                Atum.LOG.error("Failed to load mod integration from " + modSupport.getClass() + " during setup");
                e.printStackTrace();
            }
        }
    }

    public void clientSide() {
        for (IModIntegration modSupport : this.integratedMods) {
            try {
                modSupport.clientSide();
            } catch (Exception e) {
                Atum.LOG.error("Failed to load mod integration from " + modSupport.getClass() + " on client-side.");
                e.printStackTrace();
            }
        }
    }
}