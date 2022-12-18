package com.teammetallurgy.atum.items;

import com.mojang.blaze3d.platform.InputConstants;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.init.AtumItems;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class CoinItem extends Item {

    public CoinItem() {
        super(new Item.Properties().tab(Atum.GROUP));
    }

    @Override
    public boolean onEntityItemUpdate(@Nonnull ItemStack stack, ItemEntity entityItem) {
        Level level = entityItem.level;
        BlockState state = level.getBlockState(new BlockPos(Mth.floor(entityItem.getX()), Mth.floor(entityItem.getY()), Mth.floor(entityItem.getZ())));
        if ((state.getFluidState().is(FluidTags.WATER) || state.getBlock() instanceof LayeredCauldronBlock && state.getValue(LayeredCauldronBlock.LEVEL) > 0) && entityItem.getItem().getItem() == AtumItems.DIRTY_COIN.get()) {
            if (!level.isClientSide) {
                while (stack.getCount() > 0) {
                    if (level.random.nextFloat() <= 0.10F) {
                        stack.shrink(1);
                        level.playSound(null, entityItem.getX(), entityItem.getY(), entityItem.getZ(), SoundEvents.ITEM_BREAK, entityItem.getSoundSource(), 0.8F, 0.8F + entityItem.level.random.nextFloat() * 0.4F);
                    } else {
                        level.addFreshEntity(new ItemEntity(level, entityItem.getX(), entityItem.getY(), entityItem.getZ(), new ItemStack(AtumItems.GOLD_COIN.get())));
                        level.playSound(null, entityItem.getX(), entityItem.getY(), entityItem.getZ(), SoundEvents.EXPERIENCE_ORB_PICKUP, entityItem.getSoundSource(), 0.8F, 0.8F + entityItem.level.random.nextFloat() * 0.4F);
                        stack.shrink(1);
                    }
                }
            }
        }
        return super.onEntityItemUpdate(stack, entityItem);
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack stack, @Nullable Level level, @Nonnull List<Component> tooltip, @Nonnull TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        if (stack.getItem() == AtumItems.DIRTY_COIN.get()) {
            if (InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_KEY_LEFT_SHIFT)) {
                tooltip.add(Component.translatable(Atum.MOD_ID + ".tooltip.dirty").append(": ").withStyle(ChatFormatting.GRAY)
                        .append(Component.translatable(Atum.MOD_ID + ".tooltip.dirty.description").withStyle(ChatFormatting.DARK_GRAY)));
            } else {
                tooltip.add(Component.translatable(Atum.MOD_ID + ".tooltip.dirty").withStyle(ChatFormatting.GRAY)
                        .append(" ").append(Component.translatable(Atum.MOD_ID + ".tooltip.shift").withStyle(ChatFormatting.DARK_GRAY)));
            }
        }
    }
}