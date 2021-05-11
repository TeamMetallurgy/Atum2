package com.teammetallurgy.atum.items;

import com.google.common.base.Preconditions;
import com.teammetallurgy.atum.Atum;
import net.minecraft.block.BlockState;
import net.minecraft.block.CauldronBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nonnull;
import java.util.List;

public class RelicItem extends Item {
    public static final NonNullList<RelicEntry> RELIC_ENTRIES = NonNullList.create();

    public RelicItem(Item.Properties properties) {
        super(properties.group(Atum.GROUP));
    }

    public Item getRelic(Type type, Quality quality) {
        return ForgeRegistries.ITEMS.getValue(new ResourceLocation(Atum.MOD_ID, "relic_" + quality.getString() + "_" + type.getString()));
    }

    public static Type getType(Item item) {
        if (!(item instanceof RelicItem)) {
            Atum.LOG.error("Item is not a relic");
        } else {
            for (Quality quality : Quality.values()) {
                Preconditions.checkNotNull(item.getRegistryName(), "registryName");
                Type type = Type.byString(item.getRegistryName().getPath().replace("relic_", "").replace(quality.getString(), "").replace("_", ""));
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
                Quality quality = Quality.byString(item.getRegistryName().getPath().replace("relic_", "").replace(type.getString(), "").replace("_", ""));
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
        BlockState state = world.getBlockState(new BlockPos(MathHelper.floor(entityItem.getPosX()), MathHelper.floor(entityItem.getPosY()), MathHelper.floor(entityItem.getPosZ())));
        if (state.getFluidState().isTagged(FluidTags.WATER) || state.getBlock() instanceof CauldronBlock && state.get(CauldronBlock.LEVEL) > 0) {
            if (stack.getItem() instanceof RelicItem && String.valueOf(stack.getItem().getRegistryName()).contains("dirty") && !world.isRemote) {
                while (stack.getCount() > 0) {
                    Item item = getRelic(getType(stack.getItem()), WeightedRandom.getRandomItem(random, RELIC_ENTRIES).quality);
                    if (random.nextFloat() <= 0.10F) {
                        stack.shrink(1);
                        world.playSound(null, entityItem.getPosX(), entityItem.getPosY(), entityItem.getPosZ(), SoundEvents.ENTITY_ITEM_BREAK, entityItem.getSoundCategory(), 0.8F, 0.8F + entityItem.world.rand.nextFloat() * 0.4F);
                    } else {
                        world.addEntity(new ItemEntity(world, entityItem.getPosX(), entityItem.getPosY(), entityItem.getPosZ(), new ItemStack(item)));
                        world.playSound(null, entityItem.getPosX(), entityItem.getPosY(), entityItem.getPosZ(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, entityItem.getSoundCategory(), 0.8F, 0.8F + entityItem.world.rand.nextFloat() * 0.4F);
                        stack.shrink(1);
                    }
                }
            }
        }
        return super.onEntityItemUpdate(stack, entityItem);
    }

    @Override
    public void addInformation(@Nonnull ItemStack stack, World world, @Nonnull List<ITextComponent> tooltip, @Nonnull ITooltipFlag flag) {
        if (getQuality(stack.getItem()) == Quality.DIRTY) {
            if (InputMappings.isKeyDown(Minecraft.getInstance().getMainWindow().getHandle(), GLFW.GLFW_KEY_LEFT_SHIFT)) {
                tooltip.add(new TranslationTextComponent(Atum.MOD_ID + ".tooltip.dirty").appendString(": ").mergeStyle(TextFormatting.GRAY)
                        .append(new TranslationTextComponent(Atum.MOD_ID + ".tooltip.dirty.description").mergeStyle(TextFormatting.DARK_GRAY)));
            } else {
                tooltip.add(new TranslationTextComponent(Atum.MOD_ID + ".tooltip.dirty").mergeStyle(TextFormatting.GRAY)
                        .appendString(" ").append(new TranslationTextComponent(Atum.MOD_ID + ".tooltip.shift").mergeStyle(TextFormatting.DARK_GRAY)));
            }
        } else {
            tooltip.add(new TranslationTextComponent(Atum.MOD_ID + ".tooltip.vanity").mergeStyle(TextFormatting.YELLOW));
        }
    }

    public enum Type implements IStringSerializable {
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
                if (type.getString().equals(name)) {
                    return type;
                }
            }
            return null;
        }

        @Override
        @Nonnull
        public String getString() {
            return this.unlocalizedName;
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

        Quality(String name, int weight) {
            this.unlocalizedName = name;
            this.weight = weight;
        }

        public static Quality byString(String name) {
            for (Quality quality : Quality.values()) {
                if (quality.getString().equals(name)) {
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
        public String getString() {
            return this.unlocalizedName;
        }
    }

    public static class RelicEntry extends WeightedRandom.Item {
        final Quality quality;

        public RelicEntry(Quality quality, int weight) {
            super(weight);
            this.quality = quality;
        }
    }
}