package com.teammetallurgy.atum.api;

import com.teammetallurgy.atum.Atum;
import net.minecraft.item.Item;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;

public class AtumAPI {

    public static class Tags {
        //Flowers
        public static final Tag<Item> FLOWERS_WHITE = tag("forge", "flowers/white");
        public static final Tag<Item> FLOWERS_ORANGE = tag("forge", "flowers/orange");
        public static final Tag<Item> FLOWERS_MAGENTA = tag("forge", "flowers/magenta");
        public static final Tag<Item> FLOWERS_LIGHT_BLUE = tag("forge", "flowers/light_blue");
        public static final Tag<Item> FLOWERS_YELLOW = tag("forge", "flowers/yellow");
        public static final Tag<Item> FLOWERS_LIME = tag("forge", "flowers/lime");
        public static final Tag<Item> FLOWERS_PINK = tag("forge", "flowers/pink");
        public static final Tag<Item> FLOWERS_GRAY = tag("forge", "flowers/gray");
        public static final Tag<Item> FLOWERS_LIGHT_GRAY = tag("forge", "flowers/light_gray");
        public static final Tag<Item> FLOWERS_CYAN = tag("forge", "flowers/cyan");
        public static final Tag<Item> FLOWERS_PURPLE = tag("forge", "flowers/purple");
        public static final Tag<Item> FLOWERS_BLUE = tag("forge", "flowers/blue");
        public static final Tag<Item> FLOWERS_BROWN = tag("forge", "flowers/brown");
        public static final Tag<Item> FLOWERS_GREEN = tag("forge", "flowers/green");
        public static final Tag<Item> FLOWERS_RED = tag("forge", "flowers/red");
        public static final Tag<Item> FLOWERS_BLACK = tag("forge", "flowers/black");

        public static final Tag<Item> CROPS_EMMER = tag("forge", "crops/emmer");
        public static final Tag<Item> CROPS_FLAX = tag("forge", "crops/flax");
        public static final Tag<Item> DUSTS_BLAZE = tag("forge", "dusts/blaze");
        public static final Tag<Item> SUGAR_CANE = tag("forge", "sugar_cane");

        public static final Tag<Item> RELIC = tag(Atum.MOD_ID, "relic");
        public static final Tag<Item> TOOLTIP = tag(Atum.MOD_ID, "tooltip");

        public static Tag<Item> tag(String modID, String name) {
            return new ItemTags.Wrapper(new ResourceLocation(modID, name));
        }
    }
}