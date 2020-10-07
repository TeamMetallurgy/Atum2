package com.teammetallurgy.atum.api;

import com.teammetallurgy.atum.Atum;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags.IOptionalNamedTag;

public class AtumAPI {

    public static class Tags {
        //Flowers
        public static final IOptionalNamedTag<Item> FLOWERS_WHITE = tag("forge", "flowers/white");
        public static final IOptionalNamedTag<Item> FLOWERS_ORANGE = tag("forge", "flowers/orange");
        public static final IOptionalNamedTag<Item> FLOWERS_MAGENTA = tag("forge", "flowers/magenta");
        public static final IOptionalNamedTag<Item> FLOWERS_LIGHT_BLUE = tag("forge", "flowers/light_blue");
        public static final IOptionalNamedTag<Item> FLOWERS_YELLOW = tag("forge", "flowers/yellow");
        public static final IOptionalNamedTag<Item> FLOWERS_LIME = tag("forge", "flowers/lime");
        public static final IOptionalNamedTag<Item> FLOWERS_PINK = tag("forge", "flowers/pink");
        public static final IOptionalNamedTag<Item> FLOWERS_GRAY = tag("forge", "flowers/gray");
        public static final IOptionalNamedTag<Item> FLOWERS_LIGHT_GRAY = tag("forge", "flowers/light_gray");
        public static final IOptionalNamedTag<Item> FLOWERS_CYAN = tag("forge", "flowers/cyan");
        public static final IOptionalNamedTag<Item> FLOWERS_PURPLE = tag("forge", "flowers/purple");
        public static final IOptionalNamedTag<Item> FLOWERS_BLUE = tag("forge", "flowers/blue");
        public static final IOptionalNamedTag<Item> FLOWERS_BROWN = tag("forge", "flowers/brown");
        public static final IOptionalNamedTag<Item> FLOWERS_GREEN = tag("forge", "flowers/green");
        public static final IOptionalNamedTag<Item> FLOWERS_RED = tag("forge", "flowers/red");
        public static final IOptionalNamedTag<Item> FLOWERS_BLACK = tag("forge", "flowers/black");

        public static final IOptionalNamedTag<Item> CROPS_EMMER = tag("forge", "crops/emmer");
        public static final IOptionalNamedTag<Item> CROPS_FLAX = tag("forge", "crops/flax");
        public static final IOptionalNamedTag<Item> DUSTS_BLAZE = tag("forge", "dusts/blaze");
        public static final IOptionalNamedTag<Item> SUGAR_CANE = tag("forge", "sugar_cane");

        public static final IOptionalNamedTag<Item> RELIC = tag(Atum.MOD_ID, "relic");
        public static final IOptionalNamedTag<Item> TOOLTIP = tag(Atum.MOD_ID, "tooltip");
        public static final IOptionalNamedTag<Block> BASE_STONE_ATUM = blockTag(Atum.MOD_ID, "base_stone_atum");
        public static final IOptionalNamedTag<Block> INFINIBURN = blockTag(Atum.MOD_ID, "infiniburn");
        public static final IOptionalNamedTag<Block> LIMESTONE_BRICKS = blockTag(Atum.MOD_ID, "limestone_bricks");
        public static final IOptionalNamedTag<Block> LINEN_BLOCKS = blockTag(Atum.MOD_ID, "linen_blocks");
        public static final IOptionalNamedTag<Block> SHRUB = blockTag(Atum.MOD_ID, "shrub");
        public static final IOptionalNamedTag<Block> THREADED_BLOCKS = blockTag(Atum.MOD_ID, "threaded_blocks");

        public static IOptionalNamedTag<Item> tag(String modID, String name) {
            return ItemTags.createOptional(new ResourceLocation(modID, name));
        }

        public static IOptionalNamedTag<Block> blockTag(String modID, String name) {
            return BlockTags.createOptional(new ResourceLocation(modID, name));
        }
    }
}