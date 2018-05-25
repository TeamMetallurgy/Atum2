package com.teammetallurgy.atum.items.artifacts.isis;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.init.AtumParticles;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemIsisHealing extends Item {
    private int duration = 40;

    public ItemIsisHealing() {
        super();
        this.setMaxDamage(96);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(@Nonnull ItemStack stack) {
        return true;
    }

    @Override
    @Nonnull
    public EnumRarity getRarity(@Nonnull ItemStack stack) {
        return EnumRarity.RARE;
    }

    @Override
    public void onUpdate(@Nonnull ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
        if (duration >= 1) {
            duration--;
        }
        if (entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entity;
            if (player.onGround) {
                if (player.getHeldItem(EnumHand.OFF_HAND).getItem() == this) {
                    this.doEffect(world, player, stack);
                } else if (player.getHeldItem(EnumHand.MAIN_HAND).getItem() == this) {
                    this.doEffect(world, player, stack);
                }
            }
        }
    }

    private void doEffect(World world, EntityPlayer player, @Nonnull ItemStack stack) {
        if (player.getHealth() < player.getMaxHealth() && duration == 0) {
            for (int l = 0; l < 24; ++l) {
                Atum.proxy.spawnParticle(AtumParticles.Types.ISIS, player, player.posX + (world.rand.nextDouble() - 0.5D) * (double) player.width, player.posY + world.rand.nextDouble() * (double) player.height, player.posZ + (world.rand.nextDouble() - 0.5D) * (double) player.width, 0.0D, 0.0D, 0.0D);
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
            tooltip.add(TextFormatting.DARK_PURPLE + I18n.format(this.getUnlocalizedName() + ".line1"));
            tooltip.add(TextFormatting.DARK_PURPLE + I18n.format(this.getUnlocalizedName() + ".line2"));
        } else {
            tooltip.add(I18n.format(this.getUnlocalizedName() + ".line3") + " " + TextFormatting.DARK_GRAY + "[SHIFT]");
        }
    }

    @Override
    public boolean getIsRepairable(@Nonnull ItemStack toRepair, @Nonnull ItemStack repair) {
        return repair.getItem() == Items.DIAMOND;
    }
}