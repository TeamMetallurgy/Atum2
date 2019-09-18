package com.teammetallurgy.atum.items.food;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

public class DateGoldEnchantedItem extends ItemFood { //TODO Remove in 1.13, and use ItemAppleGoldEnchanted instead

    public DateGoldEnchantedItem(int amount, float saturation, boolean isWolfFood) {
        super(amount, saturation, isWolfFood);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean hasEffect(@Nonnull ItemStack stack) {
        return true;
    }

    @Override
    @Nonnull
    public EnumRarity getRarity(@Nonnull ItemStack stack) {
        return EnumRarity.EPIC;
    }

    @Override
    protected void onFoodEaten(@Nonnull ItemStack stack, World world, @Nonnull PlayerEntity player) {
        if (!world.isRemote) {
            player.addPotionEffect(new EffectInstance(Effects.REGENERATION, 400, 1));
            player.addPotionEffect(new EffectInstance(Effects.RESISTANCE, 6000, 0));
            player.addPotionEffect(new EffectInstance(Effects.FIRE_RESISTANCE, 6000, 0));
            player.addPotionEffect(new EffectInstance(Effects.ABSORPTION, 2400, 3));
        }
    }
}