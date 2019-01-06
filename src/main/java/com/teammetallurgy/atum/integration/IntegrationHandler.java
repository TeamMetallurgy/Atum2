package com.teammetallurgy.atum.integration;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.integration.thaumcraft.Thaumcraft;
import com.teammetallurgy.atum.utils.AtumConfig;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.Loader;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class IntegrationHandler {
    public static final IntegrationHandler INSTANCE = new IntegrationHandler();
    private final NonNullList<IModIntegration> integratedMods = NonNullList.create();
    private HashMap<String, Class<? extends IModIntegration>> mods = new HashMap<>();

    public void initModIntegration() {
        mods.put(Thaumcraft.THAUMCRAFT_ID, Thaumcraft.class);

        List<String> enabledModSupport = mods.keySet().stream().filter(modid -> AtumConfig.config.get(AtumConfig.MOD_INTEGRATION, modid, true).getBoolean()).collect(Collectors.toList());
        AtumConfig.config.save();

        mods.entrySet().stream().filter(entry -> enabledModSupport.contains(entry.getKey()) && Loader.isModLoaded(entry.getKey())).forEach(entry -> {
            try {
                integratedMods.add(entry.getValue().newInstance());
            } catch (Exception e) {
                Atum.LOG.error("Failed to load mod integration handler");
                e.printStackTrace();
            }
        });
    }

    public void preInit() {
        for (IModIntegration modSupport : integratedMods) {
            try {
                modSupport.preInit();
            } catch (Exception e) {
                Atum.LOG.error("Failed to load mod integration from " + modSupport.getClass() + " in PreInit");
                e.printStackTrace();
            }
        }
    }

    public void init() {
        for (IModIntegration modSupport : integratedMods) {
            try {
                modSupport.init();
            } catch (Exception e) {
                Atum.LOG.error("Failed to load mod integration from " + modSupport.getClass() + " in Init");
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