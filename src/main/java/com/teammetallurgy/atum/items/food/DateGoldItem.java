package com.teammetallurgy.atum.items.food;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class DateGoldItem extends ItemFood { //TODO Remove in 1.13, and use ItemAppleGold instead

    public DateGoldItem(int amount, float saturation, boolean isWolfFood) {
        super(amount, saturation, isWolfFood);
    }

    @Override
    @Nonnull
    public EnumRarity getRarity(@Nonnull ItemStack stack) {
        return EnumRarity.RARE;
    }

    @Override
    protected void onFoodEaten(@Nonnull ItemStack stack, World world, @Nonnull PlayerEntity player) {
        if (!world.isRemote) {
            player.addPotionEffect(new EffectInstance(Effects.REGENERATION, 100, 1));
            player.addPotionEffect(new EffectInstance(Effects.ABSORPTION, 2400, 0));
        }
    }
}