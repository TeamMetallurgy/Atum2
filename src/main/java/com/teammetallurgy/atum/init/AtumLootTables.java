package com.teammetallurgy.atum.init;

import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;

public class AtumLootTables {
    public static final ResourceLocation FISH = register("gameplay/fishing/fish");

    private static ResourceLocation register(String path) {
        return LootTableList.register(new ResourceLocation(Constants.MOD_ID, path));
    }
}
