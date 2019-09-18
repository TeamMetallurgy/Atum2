package com.teammetallurgy.atum.items;

import com.google.common.collect.Lists;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.List;

public class LinenBandageItem extends Item {
    private List<EffectInstance> badEffects = Lists.newArrayList();

    @Override
    @Nonnull
    public ItemStack onItemUseFinish(@Nonnull ItemStack stack, World world, LivingEntity livingBase) {
        if (livingBase instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) livingBase;
            for (EffectInstance potionEffect : player.getActivePotionEffects()) {
                Potion potion = potionEffect.getPotion();
                if (potion.isBadEffect()) {
                    badEffects.add(potionEffect);
                }
            }
            for (EffectInstance badEffect : badEffects) {
                player.removeActivePotionEffect(badEffect.getPotion());
            }
            player.heal(10);
        }
        return super.onItemUseFinish(stack, world, livingBase);
    }

    @Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, @Nonnull Hand hand) {
        player.setActiveHand(hand);
        return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
    }

    @Override
    @Nonnull
    public EnumAction getItemUseAction(@Nonnull ItemStack stack) {
        return EnumAction.BOW;
    }

    @Override
    public int getMaxItemUseDuration(@Nonnull ItemStack stack) {
        return 75;
    }
}