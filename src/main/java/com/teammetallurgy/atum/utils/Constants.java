package com.teammetallurgy.atum.utils;

import net.minecraftforge.fml.ModList;

public class Constants {
    public static final String MOD_ID = "atum";
    public static final String MOD_NAME = "Atum2";
    public static final String CLIENT = "com.teammetallurgy.atum.proxy.ClientProxy";
    public static final String SERVER = "com.teammetallurgy.atum.proxy.CommonProxy";

    public static final boolean IS_JEI_LOADED = ModList.get().isLoaded("jei");
    public static final boolean IS_QUARK_LOADED = ModList.get().isLoaded("quark");
}