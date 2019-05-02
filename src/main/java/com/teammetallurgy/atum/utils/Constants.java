package com.teammetallurgy.atum.utils;

import net.minecraftforge.fml.common.Loader;

public class Constants {

    public static final String MOD_ID = "atum";
    public static final String MOD_NAME = "Atum2";
    public static final String VERSION = "@VERSION@";
    public static final String CLIENT = "com.teammetallurgy.atum.proxy.ClientProxy";
    public static final String SERVER = "com.teammetallurgy.atum.proxy.CommonProxy";
    public static final String FACTORY = "com.teammetallurgy.atum.client.gui.AtumGuiFactory";
    public static final String DEPENDENCIES = "required-after:forge@[14.23.5.2768,);after:baubles";

    public static final boolean IS_JEI_LOADED = Loader.isModLoaded("jei");
    public static final boolean IS_QUARK_LOADED = Loader.isModLoaded("quark");
}