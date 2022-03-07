package com.teammetallurgy.atum.api;

import com.teammetallurgy.atum.Atum;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class AtumAPI {

    public static class Tags {
        //Flowers
        public static final TagKey<Item> FLOWERS_WHITE = tag("forge", "flowers/white");
        public static final TagKey<Item> FLOWERS_ORANGE = tag("forge", "flowers/orange");
        public static final TagKey<Item> FLOWERS_MAGENTA = tag("forge", "flowers/magenta");
        public static final TagKey<Item> FLOWERS_LIGHT_BLUE = tag("forge", "flowers/light_blue");
        public static final TagKey<Item> FLOWERS_YELLOW = tag("forge", "flowers/yellow");
        public static final TagKey<Item> FLOWERS_LIME = tag("forge", "flowers/lime");
        public static final TagKey<Item> FLOWERS_PINK = tag("forge", "flowers/pink");
        public static final TagKey<Item> FLOWERS_GRAY = tag("forge", "flowers/gray");
        public static final TagKey<Item> FLOWERS_LIGHT_GRAY = tag("forge", "flowers/light_gray");
        public static final TagKey<Item> FLOWERS_CYAN = tag("forge", "flowers/cyan");
        public static final TagKey<Item> FLOWERS_PURPLE = tag("forge", "flowers/purple");
        public static final TagKey<Item> FLOWERS_BLUE = tag("forge", "flowers/blue");
        public static final TagKey<Item> FLOWERS_BROWN = tag("forge", "flowers/brown");
        public static final TagKey<Item> FLOWERS_GREEN = tag("forge", "flowers/green");
        public static final TagKey<Item> FLOWERS_RED = tag("forge", "flowers/red");
        public static final TagKey<Item> FLOWERS_BLACK = tag("forge", "flowers/black");

        public static final TagKey<Item> CROPS_EMMER = tag("forge", "crops/emmer");
        public static final TagKey<Item> CROPS_FLAX = tag("forge", "crops/flax");
        public static final TagKey<Item> DUSTS_BLAZE = tag("forge", "dusts/blaze");
        public static final TagKey<Item> SUGAR_CANE = tag("forge", "sugar_cane");

        public static final TagKey<Item> RELIC = tag(Atum.MOD_ID, "relic");
        public static final TagKey<Item> RELIC_NON_DIRTY = tag(Atum.MOD_ID, "relic_non_dirty");
        public static final TagKey<Item> TOOLTIP = tag(Atum.MOD_ID, "tooltip");
        public static final TagKey<Block> BASE_STONE_ATUM = blockTag(Atum.MOD_ID, "base_stone_atum");
        public static final TagKey<Block> INFINIBURN = blockTag(Atum.MOD_ID, "infiniburn");
        public static final TagKey<Block> LIMESTONE_BRICKS = blockTag(Atum.MOD_ID, "limestone_bricks");
        public static final TagKey<Block> LINEN_BLOCKS = blockTag(Atum.MOD_ID, "linen_blocks");
        public static final TagKey<Block> SHRUB = blockTag(Atum.MOD_ID, "shrub");
        public static final TagKey<Block> THREADED_BLOCKS = blockTag(Atum.MOD_ID, "threaded_blocks");
        public static final TagKey<Block> SCAFFOLDING = blockTag("forge", "scaffolding");

        public static TagKey<Item> tag(String modID, String name) {
            return ItemTags.create(new ResourceLocation(modID, name));
        }

        public static TagKey<Block> blockTag(String modID, String name) {
            return BlockTags.create(new ResourceLocation(modID, name));
        }

        public static void init() {
        }
    }
}