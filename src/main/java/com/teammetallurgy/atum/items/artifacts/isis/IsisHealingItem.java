package com.teammetallurgy.atum.items.artifacts.isis;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.init.AtumParticles;
import com.teammetallurgy.atum.items.AmuletItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class IsisHealingItem extends AmuletItem {
    private int duration = 40;

    public IsisHealingItem() {
        super(new Item.Properties().maxDamage(96));
    }

    /*@Override
    @Optional.Method(modid = "baubles")
    public void onWornTick(ItemStack stack, LivingEntity livingBase) {
        if (stack.getItem() == this && livingBase instanceof PlayerEntity) {
            if (duration >= 1) {
                duration--;
            }
            PlayerEntity player = (PlayerEntity) livingBase;
            this.doEffect(player.world, player, getAmulet(player));
        }
    }*/

    @Override
    public void inventoryTick(@Nonnull ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
        if (duration >= 1) {
            duration--;
        }
        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            if (player.getHeldItem(Hand.OFF_HAND).getItem() == this) {
                this.doEffect(world, player, stack, Hand.OFF_HAND);
            } else if (player.getHeldItem(Hand.MAIN_HAND).getItem() == this) {
                this.doEffect(world, player, stack, Hand.MAIN_HAND);
            }
        }
    }

    private void doEffect(World world, PlayerEntity player, @Nonnull ItemStack stack, Hand hand) {
        if (player.getHealth() < player.getMaxHealth() && duration == 0) {
            double x = MathHelper.nextDouble(world.rand, 0.0001D, 0.05D);
            double z = MathHelper.nextDouble(world.rand, 0.0001D, 0.05D);
            for (int l = 0; l < 24; ++l) {
                Atum.proxy.spawnParticle(AtumParticles.Types.ISIS, player, player.posX + (world.rand.nextDouble() - 0.25D) * (double) player.getWidth(), player.posY + world.rand.nextDouble() * (double) player.height, player.posZ + (world.rand.nextDouble() - 0.25D) * (double) player.getWidth(), x, 0.0D, -z);
            }
            if (!world.isRemote) {
                player.heal(1.0F);
                duration = 40;
                if (!player.abilities.isCreativeMode) {
                    stack.damageItem(1, player, (e) -> e.sendBreakAnimation(hand));
                }
            }
        }
    }
}