package com.teammetallurgy.atum.items.tools;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;

import javax.annotation.Nonnull;

public class ItemDaggerPoison extends ItemDagger {

    @Override
    public boolean hitEntity(@Nonnull ItemStack stack, EntityLivingBase target, @Nonnull EntityLivingBase attacker) {
        target.addPotionEffect(new PotionEffect(MobEffects.POISON, 100, 1));
        return super.hitEntity(stack, target, attacker);
    }
}