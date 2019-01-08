package com.teammetallurgy.atum.items.artifacts.isis;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.init.AtumParticles;
import com.teammetallurgy.atum.items.ItemAmulet;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemIsisHealing extends ItemAmulet {
    private int duration = 40;

    public ItemIsisHealing() {
        super();
        this.setMaxDamage(96);
    }

    @Override
    @Optional.Method(modid = "baubles")
    public void onWornTick(ItemStack stack, EntityLivingBase livingBase) {
        if (stack.getItem() == this && livingBase instanceof EntityPlayer) {
            if (duration >= 1) {
                duration--;
            }
            EntityPlayer player = (EntityPlayer) livingBase;
            this.doEffect(player.world, player, getAmulet(player));
        }
    }

    @Override
    public void onUpdate(@Nonnull ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
        if (duration >= 1) {
            duration--;
        }
        if (entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entity;
            if (player.getHeldItem(EnumHand.OFF_HAND).getItem() == this) {
                this.doEffect(world, player, stack);
            } else if (player.getHeldItem(EnumHand.MAIN_HAND).getItem() == this) {
                this.doEffect(world, player, stack);
            }
        }
    }

    private void doEffect(World world, EntityPlayer player, @Nonnull ItemStack stack) {
        if (player.getHealth() < player.getMaxHealth() && duration == 0) {
            double x = MathHelper.nextDouble(world.rand, 0.0001D, 0.05D);
            double z = MathHelper.nextDouble(world.rand, 0.0001D, 0.05D);
            for (int l = 0; l < 24; ++l) {
                Atum.proxy.spawnParticle(AtumParticles.Types.ISIS, player, player.posX + (world.rand.nextDouble() - 0.25D) * (double) player.width, player.posY + world.rand.nextDouble() * (double) player.height, player.posZ + (world.rand.nextDouble() - 0.25D) * (double) player.width, x, 0.0D, -z);
            }
            if (!world.isRemote) {
                player.heal(1.0F);
                duration = 40;
                if (!player.capabilities.isCreativeMode) {
                    stack.damageItem(1, player);
                }
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(@Nonnull ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag tooltipType) {
        if (Keyboard.isKeyDown(42)) {
            tooltip.add(TextFormatting.DARK_PURPLE + I18n.format(this.getTranslationKey() + ".line1"));
            tooltip.add(TextFormatting.DARK_PURPLE + I18n.format(this.getTranslationKey() + ".line2"));
        } else {
            tooltip.add(I18n.format(this.getTranslationKey() + ".line3") + " " + TextFormatting.DARK_GRAY + "[SHIFT]");
        }
    }
}