package com.teammetallurgy.atum.items;

import com.google.common.base.Preconditions;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.utils.*;
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

public class ItemLoot extends Item implements IOreDictEntry {
    private static final NonNullList<LootEntry> LOOT_ENTRIES = NonNullList.create();

    public static void createLootItems() {
        for (Type type : Type.values()) {
            for (Quality quality : Quality.values()) {
                Item item = new ItemLoot();
                if (quality == Quality.DIRTY) {
                    item.setMaxStackSize(64);
                } else {
                    item.setMaxStackSize(16);
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
                Type type = Type.byString(item.getRegistryName().getPath().replace("loot_", "").replace(quality.getName(), "").replace("_", ""));
                if (type != null) {
                    return type;
                }
            }
        }
        return Type.IDOL;
    }

    public static Quality getQuality(Item item) {
        if (!(item instanceof ItemLoot)) {
            Atum.LOG.error("Item is not a loot artifact");
        } else {
            for (Type type : Type.values()) {
                Preconditions.checkNotNull(item.getRegistryName(), "registryName");
                Quality quality = Quality.byString(item.getRegistryName().getPath().replace("loot_", "").replace(type.getName(), "").replace("_", ""));
                if (quality != null) {
                    return quality;
                }
            }
        }
        return Quality.DIRTY;
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

    @Override
    public void getOreDictEntries() {
        OreDictHelper.add(this, "relic");
    }

    public enum Type implements IStringSerializable {
        IDOL("idol"),
        NECKLACE("necklace"),
        RING("ring"),
        BROACH("broach"),
        SCEPTER("scepter");

        private final String unlocalizedName;

        Type(String name) {
            this.unlocalizedName = name;
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
    }

    public enum Quality implements IStringSerializable {
        DIRTY("dirty", 0),
        SILVER("silver", 48),
        GOLD("gold", 25),
        SAPPHIRE("sapphire", 20),
        RUBY("ruby", 15),
        EMERALD("emerald", 10),
        DIAMOND("diamond", 5);

        private final String unlocalizedName;
        private final int weight;

        Quality(String name, int lootWeight) {
            this.unlocalizedName = name;
            this.weight = lootWeight;
        }

        public static Quality byString(String name) {
            for (Quality quality : Quality.values()) {
                if (quality.getName().equals(name)) {
                    return quality;
                }
            }
            return null;
        }

        public int getWeight() {
            return weight;
        }

        @Override
        @Nonnull
        public String getName() {
            return unlocalizedName;
        }
    }

    public static class LootEntry extends WeightedRandom.Item {
        final Quality quality;

        LootEntry(Quality quality, int weight) {
            super(weight);
            this.quality = quality;
        }
    }
}