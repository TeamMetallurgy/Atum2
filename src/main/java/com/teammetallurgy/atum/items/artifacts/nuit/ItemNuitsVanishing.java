package com.teammetallurgy.atum.items.artifacts.nuit;

import com.teammetallurgy.atum.init.AtumItems;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

@Mod.EventBusSubscriber
public class ItemNuitsVanishing extends Item {
    private static boolean isInvisible = false;

    public ItemNuitsVanishing() {
        this.setMaxStackSize(1);
        this.setMaxDamage(3600);
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
        if (entity instanceof EntityLivingBase) {
            EntityLivingBase livingBase = (EntityLivingBase) entity;
            if (isInvisible) {
                if (!world.isRemote) {
                    livingBase.setInvisible(true);
                    stack.damageItem(1, livingBase);
                }
            } else {
                livingBase.setInvisible(false);
            }
        }
        super.onUpdate(stack, world, entity, itemSlot, isSelected);
    }

    @SubscribeEvent
    public static void onTarget(LivingSetAttackTargetEvent event) {
        if (isInvisible && event.getTarget() != null && event.getEntityLiving() != null) {
            if (event.getTarget() instanceof EntityPlayer) {
                ((EntityLiving) event.getEntityLiving()).setAttackTarget(null);
            }
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void onRender(RenderPlayerEvent.Pre event) {
        EntityPlayer player = event.getEntityPlayer();
        EnumHand hand = player.getHeldItem(EnumHand.OFF_HAND).getItem() == AtumItems.NUITS_VANISHING ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;
        ItemStack heldStack = player.getHeldItem(hand);
        if (heldStack.getItem() == AtumItems.NUITS_VANISHING) {
            if (player.motionX == 0.0F) {
                isInvisible = true;
                event.setCanceled(true);
            } else {
                player.setInvisible(false);
                isInvisible = false;
            }
        } else {
            player.setInvisible(false);
        }
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        return toRepair.getItem() == Items.DIAMOND;
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
}