package com.teammetallurgy.atum.blocks.wood;

import net.minecraft.block.WoodType;

public class AtumWoodTypes extends WoodType {
    public static final WoodType PALM = register(new AtumWoodTypes("palm"));
    public static final WoodType DEADWOOD = register(new AtumWoodTypes("deadwood"));

    protected AtumWoodTypes(String name) {
        super("atum_" + name);
    }
}