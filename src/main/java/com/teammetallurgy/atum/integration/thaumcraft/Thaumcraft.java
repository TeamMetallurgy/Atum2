package com.teammetallurgy.atum.integration.thaumcraft;

import com.teammetallurgy.atum.integration.IModIntegration;
import net.minecraftforge.common.MinecraftForge;

public class Thaumcraft implements IModIntegration {
    public static final String THAUMCRAFT_ID = "thaumcraft";

    @Override
    public void init() {
        MinecraftForge.EVENT_BUS.register(new AtumAspects());
    }
}