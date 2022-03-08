package com.teammetallurgy.atum.items;

import com.google.common.collect.Lists;
import com.teammetallurgy.atum.Atum;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;
import java.util.List;

public class LinenBandageItem extends Item {
    private List<MobEffectInstance> badEffects = Lists.newArrayList();

    public LinenBandageItem() {
        super(new Item.Properties().tab(Atum.GROUP));
    }

    @Override
    @Nonnull
    public ItemStack finishUsingItem(@Nonnull ItemStack stack, @Nonnull Level level, @Nonnull LivingEntity livingBase) {
        if (livingBase instanceof Player player) {
            for (MobEffectInstance potionEffect : player.getActiveEffects()) {
                MobEffect potion = potionEffect.getEffect();
                if (potion.getCategory() == MobEffectCategory.HARMFUL) {
                    badEffects.add(potionEffect);
                }
            }
            for (MobEffectInstance badEffect : badEffects) {
                player.removeEffectNoUpdate(badEffect.getEffect());
            }
            player.heal(10);
        }
        return super.finishUsingItem(stack, level, livingBase);
    }

    @Override
    @Nonnull
    public InteractionResultHolder<ItemStack> use(@Nonnull Level level, Player player, @Nonnull InteractionHand hand) {
        player.startUsingItem(hand);
        return new InteractionResultHolder<>(InteractionResult.SUCCESS, player.getItemInHand(hand));
    }

    @Override
    @Nonnull
    public UseAnim getUseAnimation(@Nonnull ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public int getUseDuration(@Nonnull ItemStack stack) {
        return 75;
    }
}