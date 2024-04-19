package com.teammetallurgy.atum.items;

import com.google.common.base.Preconditions;
import com.mojang.blaze3d.platform.InputConstants;
import com.teammetallurgy.atum.Atum;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.StringRepresentable;
import net.minecraft.util.random.Weight;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.util.random.WeightedRandom;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RelicItem extends Item {
    public static final List<RelicEntry> RELIC_ENTRIES = new ArrayList<>();

    public RelicItem(Item.Properties properties) {
        super(properties);
    }

    public Item getRelic(Type type, Quality quality) {
        return BuiltInRegistries.ITEM.get(new ResourceLocation(Atum.MOD_ID, "relic_" + quality.getSerializedName() + "_" + type.getSerializedName()));
    }

    public static Type getType(Item item) {
        if (!(item instanceof RelicItem)) {
            Atum.LOG.error("Item is not a relic");
        } else {
            for (Quality quality : Quality.values()) {
                Preconditions.checkNotNull(BuiltInRegistries.ITEM.getKey(item), "registryName");
                Type type = Type.byString(BuiltInRegistries.ITEM.getKey(item).getPath().replace("relic_", "").replace(quality.getSerializedName(), "").replace("_", ""));
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
                Preconditions.checkNotNull(BuiltInRegistries.ITEM.getKey(item), "registryName");
                Quality quality = Quality.byString(BuiltInRegistries.ITEM.getKey(item).getPath().replace("relic_", "").replace(type.getSerializedName(), "").replace("_", ""));
                if (quality != null) {
                    return quality;
                }
            }
        }
        return Quality.DIRTY;
    }

    @Override
    public boolean onEntityItemUpdate(@Nonnull ItemStack stack, ItemEntity entityItem) {
        Level level = entityItem.level();
        BlockState state = level.getBlockState(new BlockPos(Mth.floor(entityItem.getX()), Mth.floor(entityItem.getY()), Mth.floor(entityItem.getZ())));
        if (state.getFluidState().is(FluidTags.WATER) || state.getBlock() instanceof LayeredCauldronBlock && state.getValue(LayeredCauldronBlock.LEVEL) > 0) {
            if (stack.getItem() instanceof RelicItem && String.valueOf(BuiltInRegistries.ITEM.getKey(stack.getItem())).contains("dirty") && !level.isClientSide) {
                while (stack.getCount() > 0) {
                    Optional<RelicEntry> optional = WeightedRandom.getRandomItem(level.random, RELIC_ENTRIES);
                    if (optional.isPresent()) {
                        Item item = getRelic(getType(stack.getItem()), optional.get().getQuality());
                        if (level.random.nextFloat() <= 0.10F) {
                            stack.shrink(1);
                            level.playSound(null, entityItem.getX(), entityItem.getY(), entityItem.getZ(), SoundEvents.ITEM_BREAK, entityItem.getSoundSource(), 0.8F, 0.8F + entityItem.level().random.nextFloat() * 0.4F);
                        } else {
                            level.addFreshEntity(new ItemEntity(level, entityItem.getX(), entityItem.getY(), entityItem.getZ(), new ItemStack(item)));
                            level.playSound(null, entityItem.getX(), entityItem.getY(), entityItem.getZ(), SoundEvents.EXPERIENCE_ORB_PICKUP, entityItem.getSoundSource(), 0.8F, 0.8F + entityItem.level().random.nextFloat() * 0.4F);
                            stack.shrink(1);
                        }
                    }
                }
            }
        }
        return super.onEntityItemUpdate(stack, entityItem);
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack stack, Level level, @Nonnull List<Component> tooltip, @Nonnull TooltipFlag flag) {
        if (getQuality(stack.getItem()) == Quality.DIRTY) {
            if (InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_KEY_LEFT_SHIFT)) {
                tooltip.add(Component.translatable(Atum.MOD_ID + ".tooltip.dirty").append(": ").withStyle(ChatFormatting.GRAY).append(Component.translatable(Atum.MOD_ID + ".tooltip.dirty.description").withStyle(ChatFormatting.DARK_GRAY)));
            } else {
                tooltip.add(Component.translatable(Atum.MOD_ID + ".tooltip.dirty").withStyle(ChatFormatting.GRAY).append(" ").append(Component.translatable(Atum.MOD_ID + ".tooltip.shift").withStyle(ChatFormatting.DARK_GRAY)));
            }
        } else {
            tooltip.add(Component.translatable(Atum.MOD_ID + ".tooltip.vanity").withStyle(ChatFormatting.YELLOW));
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

    public static class RelicEntry implements WeightedEntry {
        private final Quality quality;
        private final int weight;

        public RelicEntry(Quality quality, int weight) {
            this.quality = quality;
            this.weight = weight;
        }

        public Quality getQuality() {
            return this.quality;
        }

        @Override
        @Nonnull
        public Weight getWeight() {
            return Weight.of(this.weight);
        }

        public int getWeightValue() {
            return this.weight;
        }
    }
}