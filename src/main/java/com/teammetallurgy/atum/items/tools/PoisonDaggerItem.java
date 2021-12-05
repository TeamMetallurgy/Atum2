package com.teammetallurgy.atum.items.tools;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTier;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;

import javax.annotation.Nonnull;

public class PoisonDaggerItem extends DaggerItem {

    public PoisonDaggerItem() {
        super(ItemTier.IRON);
    }

    @Override
    public boolean hitEntity(@Nonnull ItemStack stack, LivingEntity target, @Nonnull LivingEntity attacker) {
        target.addPotionEffect(new EffectInstance(Effects.POISON, 100, 1));
        return super.hitEntity(stack, target, attacker);
    }
}