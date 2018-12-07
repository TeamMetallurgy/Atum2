package com.teammetallurgy.atum.integration.thaumcraft;

import com.teammetallurgy.atum.utils.Constants;
import net.minecraftforge.common.MinecraftForge;

public class Thaumcraft {

    public static void init() {
        if (Constants.IS_THAUMCRAFT_LOADED) {
            System.out.println("IS_THAUMCRAFT_LOADED");
            MinecraftForge.EVENT_BUS.register(new AtumAspects());
        }
        System.out.println("Init");
    }
}