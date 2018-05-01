package com.teammetallurgy.atum.items;

import com.google.common.collect.Maps;
import com.teammetallurgy.atum.utils.AtumRegistry;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Random;

public class ItemLoot extends Item {

    public ItemLoot() {
        this.setMaxDamage(0);
        this.setMaxStackSize(1);
    }

    @Nonnull
    public static ItemStack getRandomLoot(Random random, boolean includeDirty) { //TODO Test
        int type = random.ints(includeDirty ? 1 : 2, Type.values().length).findAny().getAsInt();
        int quality = random.ints(includeDirty ? 1 : 2, Quality.values().length).findAny().getAsInt();
        return new ItemStack(getLootItem(Type.byIndex(type), Quality.byIndex(quality)));
    }

    public static Item getLootItem(Type type, Quality quality) {
        return Item.REGISTRY.getObject(new ResourceLocation("loot." + quality.getName() + "." + type.getName()));
    }

    public static void createLootItems() {
        for (Type type : Type.values()) {
            for (Quality quality : Quality.values()) {
                AtumRegistry.registerItem(new ItemLoot(), "loot." + quality.getName() + "." + type.getName());
            }
        }
    }

    @Override
    public boolean onEntityItemUpdate(EntityItem entityItem) {
        Block block = entityItem.world.getBlockState(new BlockPos(MathHelper.floor(entityItem.posX), MathHelper.floor(entityItem.posY), MathHelper.floor(entityItem.posZ))).getBlock();
        if (block == Blocks.WATER || block == Blocks.FLOWING_WATER) {
            ItemStack item = entityItem.getItem();
            int damage = item.getItemDamage() >> 1;
            int quality = damage & 15;
            if (quality == 0) {
                damage |= (int) (Math.random() * 6.0D) + 1;
            }

            item.setItemDamage(damage << 1);
            entityItem.setItem(item);
        }

        return super.onEntityItemUpdate(entityItem);
    }

    public enum Type implements IStringSerializable {
        IDOL(1, "idol"),
        NECKLACE(2, "necklace"),
        RING(3, "ring"),
        BROACH(4, "broach"),
        SCEPTER(5, "scepter");

        private static final Map<Integer, Type> INDEX_LOOKUP = Maps.newHashMap();
        private final int index;
        private final String unlocalizedName;

        Type(int index, String name) {
            this.index = index;
            this.unlocalizedName = name;
        }

        public int getIndexNumber() {
            return this.index;
        }

        public static Type byIndex(int index) {
            Type type = INDEX_LOOKUP.get(index);
            return type == null ? IDOL : type;
        }

        @Override
        @Nonnull
        public String getName() {
            return unlocalizedName;
        }

        static {
            for (Type type : values()) {
                INDEX_LOOKUP.put(type.getIndexNumber(), type);
            }
        }
    }

    public enum Quality implements IStringSerializable {
        DIRTY(1, "dirty"),
        SILVER(2, "silver"),
        GOLD(3, "gold"),
        SAPPHIRE(4, "sapphire"),
        RUBY(5, "ruby"),
        EMERALD(6, "emerald"),
        DIAMOND(7, "diamond");

        private static final Map<Integer, Quality> INDEX_LOOKUP = Maps.newHashMap();
        private final int index;
        private final String unlocalizedName;

        Quality(int index, String name) {
            this.index = index;
            this.unlocalizedName = name;
        }

        public int getIndexNumber() {
            return this.index;
        }

        public static Quality byIndex(int index) {
            Quality quality = INDEX_LOOKUP.get(index);
            return quality == null ? DIRTY : quality;
        }


        @Override
        @Nonnull
        public String getName() {
            return unlocalizedName;
        }

        static {
            for (Quality quality : values()) {
                INDEX_LOOKUP.put(quality.getIndexNumber(), quality);
            }
        }
    }
}