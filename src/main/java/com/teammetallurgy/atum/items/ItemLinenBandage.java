package com.teammetallurgy.atum.items;

import com.google.common.collect.Lists;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.List;

public class ItemLinenBandage extends Item {
    private List<PotionEffect> badEffects = Lists.newArrayList();

    @Override
    @Nonnull
    public ItemStack onItemUseFinish(@Nonnull ItemStack stack, World world, EntityLivingBase livingBase) {
        if (livingBase instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) livingBase;
            for (PotionEffect potionEffect : player.getActivePotionEffects()) {
                Potion potion = potionEffect.getPotion();
                if (potion.isBadEffect()) {
                    badEffects.add(potionEffect);
                }
            }
            for (PotionEffect badEffect : badEffects) {
                player.removeActivePotionEffect(badEffect.getPotion());
            }
            player.heal(10);
        }
        return super.onItemUseFinish(stack, world, livingBase);
    }

    @Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {
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