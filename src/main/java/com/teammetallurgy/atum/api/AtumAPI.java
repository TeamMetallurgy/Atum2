package com.teammetallurgy.atum.api;

import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.item.Item;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;

public class AtumAPI {
    /**
     * Reference to Atum's materials
     **/
    public static AtumMats MATS = new AtumMats();

    public static class Tags {
        public static final Tag<Item> DUSTS_BLAZE = tag("forge", "dusts/blaze");
        public static final Tag<Item> RELIC = tag(Constants.MOD_ID, "relic");
        public static final Tag<Item> TOOLTIP = tag(Constants.MOD_ID, "tooltip");

        public static Tag<Item> tag(String modID, String name) {
            return new ItemTags.Wrapper(new ResourceLocation(modID, name));
        }
    }
}