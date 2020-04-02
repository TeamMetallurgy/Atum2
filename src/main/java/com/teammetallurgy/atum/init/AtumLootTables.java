package com.teammetallurgy.atum.init;

import com.teammetallurgy.atum.Atum;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTables;

public class AtumLootTables {
    //Entities
    public static final ResourceLocation DESERT_WOLF = register("entities/desert_wolf");
    public static final ResourceLocation DESERT_WOLF_ALPHA = register("entities/desert_wolf_alpha");
    public static final ResourceLocation SCARAB = register("entities/scarab");
    public static final ResourceLocation SCARAB_GOLDEN = register("entities/scarab_golden");

    //Fishing
    public static final ResourceLocation ATUMS_BOUNTY = register("gameplay/fishing/atums_bounty_fish");
    public static final ResourceLocation FISHING = register("gameplay/fishing");

    //Container Loot
    public static final ResourceLocation CRATE = register("chests/crate");
    public static final ResourceLocation CRATE_BONUS = register("chests/crate_bonus");
    public static final ResourceLocation GIRAFI_TOMB = register("chests/girafi_tomb");
    public static final ResourceLocation LIGHTHOUSE = register("chests/lighthouse");
    public static final ResourceLocation PHARAOH = register("chests/pharaoh");
    public static final ResourceLocation PYRAMID_CHEST = register("chests/pyramid_chest");
    public static final ResourceLocation SARCOPHAGUS_ARTIFACT = register("chests/sarcophagus_artifact");
    public static final ResourceLocation TOMB_CHEST = register("chests/tomb");

    private static ResourceLocation register(String path) {
        return LootTables.register(new ResourceLocation(Atum.MOD_ID, path));
    }
}