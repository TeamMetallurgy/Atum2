package com.teammetallurgy.atum.items;

import com.google.common.collect.Lists;
import com.teammetallurgy.atum.Atum;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.List;

public class LinenBandageItem extends Item {
    private List<EffectInstance> badEffects = Lists.newArrayList();

    public LinenBandageItem() {
        super(new Item.Properties().group(Atum.GROUP));
    }

    @Override
    @Nonnull
    public ItemStack onItemUseFinish(@Nonnull ItemStack stack, @Nonnull World world, @Nonnull LivingEntity livingBase) {
        if (livingBase instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) livingBase;
            for (EffectInstance potionEffect : player.getActivePotionEffects()) {
                Effect potion = potionEffect.getPotion();
                if (potion.getEffectType() == EffectType.HARMFUL) {
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
        return new ActionResult<>(ActionResultType.SUCCESS, player.getHeldItem(hand));
    }

    @Override
    @Nonnull
    public UseAction getUseAction(@Nonnull ItemStack stack) {
        return UseAction.BOW;
    }

    @Override
    public int getUseDuration(@Nonnull ItemStack stack) {
        return 75;
    }
}