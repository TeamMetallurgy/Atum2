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
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Map;

public class ItemLoot extends Item {
    private static final NonNullList<LootEntry> LOOT_ENTRIES = NonNullList.create();

    private ItemLoot() {
        this.setMaxDamage(0);
    }

    public static void createLootItems() {
        for (Type type : Type.values()) {
            for (Quality quality : Quality.values()) {
                Item item = new ItemLoot();
                if (quality == Quality.DIRTY) {
                    item.setMaxStackSize(64);
                } else {
                    item.setMaxStackSize(1);
                }
                LOOT_ENTRIES.add(new LootEntry(quality, quality.getWeight()));
                AtumRegistry.registerItem(item, "loot." + quality.getName() + "." + type.getName());
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
                if (type != null) {
                    return type;
                }
            }
        }
        return Type.IDOL;
    }

    @Override
    public boolean onEntityItemUpdate(EntityItem entityItem) {
        World world = entityItem.world;
        Block block = world.getBlockState(new BlockPos(MathHelper.floor(entityItem.posX), MathHelper.floor(entityItem.posY), MathHelper.floor(entityItem.posZ))).getBlock();
        if (block == Blocks.WATER || block == Blocks.FLOWING_WATER) {
            ItemStack stack = entityItem.getItem();

            if (stack.getItem() instanceof ItemLoot && String.valueOf(stack.getItem().getRegistryName()).contains("dirty") && !world.isRemote) {
                while (stack.getCount() > 0) {
                    Item item = getLootItem(getType(stack.getItem()), WeightedRandom.getRandomItem(itemRand, LOOT_ENTRIES).quality);
                    if (itemRand.nextFloat() <= 0.10F) {
                        stack.shrink(1);
                        world.playSound(null, entityItem.posX, entityItem.posY, entityItem.posZ, SoundEvents.ENTITY_ITEM_BREAK, entityItem.getSoundCategory(), 0.8F, 0.8F + entityItem.world.rand.nextFloat() * 0.4F);
                    } else {
                        world.spawnEntity(new EntityItem(world, entityItem.posX, entityItem.posY, entityItem.posZ, new ItemStack(item)));
                        world.playSound(null, entityItem.posX, entityItem.posY, entityItem.posZ, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, entityItem.getSoundCategory(), 0.8F, 0.8F + entityItem.world.rand.nextFloat() * 0.4F);
                        stack.shrink(1);
                    }
                }
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
        DIRTY(1, "dirty", 0),
        SILVER(2, "silver", 48),
        GOLD(3, "gold", 25),
        SAPPHIRE(4, "sapphire", 20),
        RUBY(5, "ruby", 15),
        EMERALD(6, "emerald", 10),
        DIAMOND(7, "diamond", 5);

        private static final Map<Integer, Quality> INDEX_LOOKUP = Maps.newHashMap();
        private final int index;
        private final String unlocalizedName;
        private final int weight;

        Quality(int index, String name, int lootWeight) {
            this.index = index;
            this.unlocalizedName = name;
            this.weight = lootWeight;
        }

        public int getIndexNumber() {
            return this.index;
        }

        public int getWeight() {
            return weight;
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

    public static class LootEntry extends WeightedRandom.Item {
        public final Quality quality;

        public LootEntry(Quality quality, int weight) {
            super(weight);
            this.quality = quality;
        }
    }
}