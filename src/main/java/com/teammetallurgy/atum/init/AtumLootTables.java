package com.teammetallurgy.atum.init;

import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;

public class AtumLootTables {
    public static final ResourceLocation FISH = register("gameplay/fishing/fish");
    public static final ResourceLocation RUINS = register("chests/ruins");
    public static final ResourceLocation PHARAOH = register("chests/pharaoh");
    public static final ResourceLocation TARANTULA = register("entities/tarantula");

    private static ResourceLocation register(String path) {
        return LootTableList.register(new ResourceLocation(Constants.MOD_ID, path));
    }
}
