package com.teammetallurgy.atum.items;

import com.google.common.base.Preconditions;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.utils.AtumUtils;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.block.BlockState;
import net.minecraft.block.CauldronBlock;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;

public class LootItem extends Item {
    public static final NonNullList<LootEntry> LOOT_ENTRIES = NonNullList.create();

    public LootItem(Item.Properties properties) {
        super(properties.group(Atum.GROUP));
    }

    public Item getLootItem(Type type, Quality quality) {
        return ForgeRegistries.ITEMS.getValue(new ResourceLocation(Constants.MOD_ID, AtumUtils.toRegistryName("loot." + quality.getName() + "." + type.getName())));
    }

    public static Type getType(Item item) {
        if (!(item instanceof LootItem)) {
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
        if (!(item instanceof LootItem)) {
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
    public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entityItem) {
        World world = entityItem.world;
        BlockState state = world.getBlockState(new BlockPos(MathHelper.floor(entityItem.posX), MathHelper.floor(entityItem.posY), MathHelper.floor(entityItem.posZ)));
        if (state.getFluidState().isTagged(FluidTags.WATER) || state.getBlock() instanceof CauldronBlock && state.get(CauldronBlock.LEVEL) > 0) {
            if (stack.getItem() instanceof LootItem && String.valueOf(stack.getItem().getRegistryName()).contains("dirty") && !world.isRemote) {
                while (stack.getCount() > 0) {
                    Item item = getLootItem(getType(stack.getItem()), WeightedRandom.getRandomItem(random, LOOT_ENTRIES).quality);
                    if (random.nextFloat() <= 0.10F) {
                        stack.shrink(1);
                        world.playSound(null, entityItem.posX, entityItem.posY, entityItem.posZ, SoundEvents.ENTITY_ITEM_BREAK, entityItem.getSoundCategory(), 0.8F, 0.8F + entityItem.world.rand.nextFloat() * 0.4F);
                    } else {
                        world.addEntity(new ItemEntity(world, entityItem.posX, entityItem.posY, entityItem.posZ, new ItemStack(item)));
                        world.playSound(null, entityItem.posX, entityItem.posY, entityItem.posZ, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, entityItem.getSoundCategory(), 0.8F, 0.8F + entityItem.world.rand.nextFloat() * 0.4F);
                        stack.shrink(1);
                    }
                }
            }
        }
        return super.onEntityItemUpdate(stack, entityItem);
    }

    public enum Type implements IStringSerializable {
        IDOL("idol"),
        NECKLACE("necklace"),
        RING("ring"),
        BROOCH("broach"),
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

        public LootEntry(Quality quality, int weight) {
            super(weight);
            this.quality = quality;
        }
    }
}