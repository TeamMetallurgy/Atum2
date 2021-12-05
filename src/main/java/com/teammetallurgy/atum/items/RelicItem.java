package com.teammetallurgy.atum.items;

import com.google.common.base.Preconditions;
import com.teammetallurgy.atum.Atum;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.CauldronBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.TooltipFlag;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.*;
import net.minecraft.core.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nonnull;
import java.util.List;

import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;

public class RelicItem extends Item {
    public static final NonNullList<RelicEntry> RELIC_ENTRIES = NonNullList.create();

    public RelicItem(Item.Properties properties) {
        super(properties.tab(Atum.GROUP));
    }

    public Item getRelic(Type type, Quality quality) {
        return ForgeRegistries.ITEMS.getValue(new ResourceLocation(Atum.MOD_ID, "relic_" + quality.getSerializedName() + "_" + type.getSerializedName()));
    }

    public static Type getType(Item item) {
        if (!(item instanceof RelicItem)) {
            Atum.LOG.error("Item is not a relic");
        } else {
            for (Quality quality : Quality.values()) {
                Preconditions.checkNotNull(item.getRegistryName(), "registryName");
                Type type = Type.byString(item.getRegistryName().getPath().replace("relic_", "").replace(quality.getSerializedName(), "").replace("_", ""));
                if (type != null) {
                    return type;
                }
            }
        }
        return Type.IDOL;
    }

    public static Quality getQuality(Item item) {
        if (!(item instanceof RelicItem)) {
            Atum.LOG.error("Item is not a relic");
        } else {
            for (Type type : Type.values()) {
                Preconditions.checkNotNull(item.getRegistryName(), "registryName");
                Quality quality = Quality.byString(item.getRegistryName().getPath().replace("relic_", "").replace(type.getSerializedName(), "").replace("_", ""));
                if (quality != null) {
                    return quality;
                }
            }
        }
        return Quality.DIRTY;
    }

    @Override
    public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entityItem) {
        Level world = entityItem.level;
        BlockState state = world.getBlockState(new BlockPos(Mth.floor(entityItem.getX()), Mth.floor(entityItem.getY()), Mth.floor(entityItem.getZ())));
        if (state.getFluidState().is(FluidTags.WATER) || state.getBlock() instanceof CauldronBlock && state.getValue(CauldronBlock.LEVEL) > 0) {
            if (stack.getItem() instanceof RelicItem && String.valueOf(stack.getItem().getRegistryName()).contains("dirty") && !world.isClientSide) {
                while (stack.getCount() > 0) {
                    Item item = getRelic(getType(stack.getItem()), WeighedRandom.getRandomItem(random, RELIC_ENTRIES).quality);
                    if (random.nextFloat() <= 0.10F) {
                        stack.shrink(1);
                        world.playSound(null, entityItem.getX(), entityItem.getY(), entityItem.getZ(), SoundEvents.ITEM_BREAK, entityItem.getSoundSource(), 0.8F, 0.8F + entityItem.level.random.nextFloat() * 0.4F);
                    } else {
                        world.addFreshEntity(new ItemEntity(world, entityItem.getX(), entityItem.getY(), entityItem.getZ(), new ItemStack(item)));
                        world.playSound(null, entityItem.getX(), entityItem.getY(), entityItem.getZ(), SoundEvents.EXPERIENCE_ORB_PICKUP, entityItem.getSoundSource(), 0.8F, 0.8F + entityItem.level.random.nextFloat() * 0.4F);
                        stack.shrink(1);
                    }
                }
            }
        }
        return super.onEntityItemUpdate(stack, entityItem);
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack stack, Level world, @Nonnull List<Component> tooltip, @Nonnull TooltipFlag flag) {
        if (getQuality(stack.getItem()) == Quality.DIRTY) {
            if (InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_KEY_LEFT_SHIFT)) {
                tooltip.add(new TranslatableComponent(Atum.MOD_ID + ".tooltip.dirty").append(": ").withStyle(ChatFormatting.GRAY)
                        .append(new TranslatableComponent(Atum.MOD_ID + ".tooltip.dirty.description").withStyle(ChatFormatting.DARK_GRAY)));
            } else {
                tooltip.add(new TranslatableComponent(Atum.MOD_ID + ".tooltip.dirty").withStyle(ChatFormatting.GRAY)
                        .append(" ").append(new TranslatableComponent(Atum.MOD_ID + ".tooltip.shift").withStyle(ChatFormatting.DARK_GRAY)));
            }
        } else {
            tooltip.add(new TranslatableComponent(Atum.MOD_ID + ".tooltip.vanity").withStyle(ChatFormatting.YELLOW));
        }
    }

    public enum Type implements StringRepresentable {
        IDOL("idol"),
        NECKLACE("necklace"),
        RING("ring"),
        BROOCH("brooch"),
        BRACELET("bracelet");

        private final String unlocalizedName;

        Type(String name) {
            this.unlocalizedName = name;
        }

        public static Type byString(String name) {
            for (Type type : Type.values()) {
                if (type.getSerializedName().equals(name)) {
                    return type;
                }
            }
            return null;
        }

        @Override
        @Nonnull
        public String getSerializedName() {
            return this.unlocalizedName;
        }
    }

    public enum Quality implements StringRepresentable {
        DIRTY("dirty", 0),
        SILVER("silver", 48),
        GOLD("gold", 25),
        SAPPHIRE("sapphire", 20),
        RUBY("ruby", 15),
        EMERALD("emerald", 10),
        DIAMOND("diamond", 5);

        private final String unlocalizedName;
        private final int weight;

        Quality(String name, int weight) {
            this.unlocalizedName = name;
            this.weight = weight;
        }

        public static Quality byString(String name) {
            for (Quality quality : Quality.values()) {
                if (quality.getSerializedName().equals(name)) {
                    return quality;
                }
            }
            return null;
        }

        public int getWeight() {
            return this.weight;
        }

        @Override
        @Nonnull
        public String getSerializedName() {
            return this.unlocalizedName;
        }
    }

    public static class RelicEntry extends WeighedRandom.WeighedRandomItem {
        final Quality quality;

        public RelicEntry(Quality quality, int weight) {
            super(weight);
            this.quality = quality;
        }
    }
}