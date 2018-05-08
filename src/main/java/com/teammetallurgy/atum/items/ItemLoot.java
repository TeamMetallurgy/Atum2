package com.teammetallurgy.atum.items;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.utils.AtumRegistry;
import com.teammetallurgy.atum.utils.AtumUtils;
import com.teammetallurgy.atum.utils.Constants;
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

    public static void createLootItems() {
        for (Type type : Type.values()) {
            for (Quality quality : Quality.values()) {
                AtumRegistry.registerItem(new ItemLoot(), "loot." + quality.getName() + "." + type.getName());
            }
        }
    }

    public static Item getLootItem(Type type, Quality quality) {
        return Item.REGISTRY.getObject(new ResourceLocation(Constants.MOD_ID, AtumUtils.toRegistryName("loot." + quality.getName() + "." + type.getName())));
    }

    public static Type getType(Item item) {
        if (!(item instanceof ItemLoot)) {
            Atum.LOG.error("Item is not a loot artifact");
        } else {
            for (Quality quality : Quality.values()) {
                Preconditions.checkNotNull(item.getRegistryName(), "registryName");
                Type type = Type.byString(item.getRegistryName().getResourcePath().replace("loot_", "").replace(quality.getName(), "").replace("_", ""));
                return type != null ? type : Type.IDOL;
            }
        }
        return Type.IDOL;
    }

    @Override
    public boolean onEntityItemUpdate(EntityItem entityItem) {
        Block block = entityItem.world.getBlockState(new BlockPos(MathHelper.floor(entityItem.posX), MathHelper.floor(entityItem.posY), MathHelper.floor(entityItem.posZ))).getBlock();
        if (block == Blocks.WATER || block == Blocks.FLOWING_WATER) {
            ItemStack stack = entityItem.getItem();

            if (String.valueOf(stack.getItem().getRegistryName()).contains("dirty")) {
                int quality = new Random().ints(2, Quality.values().length).findAny().getAsInt();
                Item item = getLootItem(getType(stack.getItem()), Quality.byIndex(quality));
                entityItem.setItem(new ItemStack(item));
            }
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

        public static Type byString(String name) {
            for (Type type : Type.values()) {
                if (type.getName().equals(name)) {
                    return type;
                }
            }
            return null;
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