package com.teammetallurgy.atum.items.tools;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

import javax.annotation.Nonnull;

public class PoisonDaggerItem extends DaggerItem {

    public PoisonDaggerItem() {
        super(Tiers.IRON);
    }

    @Override
    public boolean hurtEnemy(@Nonnull ItemStack stack, LivingEntity target, @Nonnull LivingEntity attacker) {
        target.addEffect(new MobEffectInstance(MobEffects.POISON, 100, 1));
        return super.hurtEnemy(stack, target, attacker);
    }
}