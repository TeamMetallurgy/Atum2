package com.teammetallurgy.atum.init;

import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;

public class AtumLootTables {
    public static final ResourceLocation ATUMS_BOUNTY = register("gameplay/fishing/atums_bounty_fish");
    public static final ResourceLocation FISHING = register("gameplay/fishing");
    public static final ResourceLocation PHARAOH = register("chests/pharaoh");
    public static final ResourceLocation RELIC = register("blocks/relic_ore");
    public static final ResourceLocation RUINS = register("chests/ruins");
    public static final ResourceLocation TARANTULA = register("entities/tarantula");

    private static ResourceLocation register(String path) {
        return LootTableList.register(new ResourceLocation(Constants.MOD_ID, path));
    }
}
