package com.teammetallurgy.atum.items;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.init.AtumItems;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.CauldronBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.TooltipFlag;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.tags.FluidTags;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;
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
        Level world = entityItem.level;
        BlockState state = world.getBlockState(new BlockPos(Mth.floor(entityItem.getX()), Mth.floor(entityItem.getY()), Mth.floor(entityItem.getZ())));
        if ((state.getFluidState().is(FluidTags.WATER) || state.getBlock() instanceof CauldronBlock && state.getValue(CauldronBlock.LEVEL) > 0) && entityItem.getItem().getItem() == AtumItems.DIRTY_COIN) {
            if (!world.isClientSide) {
                while (stack.getCount() > 0) {
                    if (random.nextFloat() <= 0.10F) {
                        stack.shrink(1);
                        world.playSound(null, entityItem.getX(), entityItem.getY(), entityItem.getZ(), SoundEvents.ITEM_BREAK, entityItem.getSoundSource(), 0.8F, 0.8F + entityItem.level.random.nextFloat() * 0.4F);
                    } else {
                        world.addFreshEntity(new ItemEntity(world, entityItem.getX(), entityItem.getY(), entityItem.getZ(), new ItemStack(AtumItems.GOLD_COIN)));
                        world.playSound(null, entityItem.getX(), entityItem.getY(), entityItem.getZ(), SoundEvents.EXPERIENCE_ORB_PICKUP, entityItem.getSoundSource(), 0.8F, 0.8F + entityItem.level.random.nextFloat() * 0.4F);
                        stack.shrink(1);
                    }
                }
            }
        }
        return super.onEntityItemUpdate(stack, entityItem);
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack stack, @Nullable Level world, @Nonnull List<Component> tooltip, @Nonnull TooltipFlag flag) {
        super.appendHoverText(stack, world, tooltip, flag);
        if (stack.getItem() == AtumItems.DIRTY_COIN) {
            if (InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_KEY_LEFT_SHIFT)) {
                tooltip.add(new TranslatableComponent(Atum.MOD_ID + ".tooltip.dirty").append(": ").withStyle(ChatFormatting.GRAY)
                        .append(new TranslatableComponent(Atum.MOD_ID + ".tooltip.dirty.description").withStyle(ChatFormatting.DARK_GRAY)));
            } else {
                tooltip.add(new TranslatableComponent(Atum.MOD_ID + ".tooltip.dirty").withStyle(ChatFormatting.GRAY)
                        .append(" ").append(new TranslatableComponent(Atum.MOD_ID + ".tooltip.shift").withStyle(ChatFormatting.DARK_GRAY)));
            }
        }
    }
}