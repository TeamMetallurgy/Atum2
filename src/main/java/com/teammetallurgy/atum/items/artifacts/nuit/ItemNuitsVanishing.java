package com.teammetallurgy.atum.items.artifacts.nuit;

import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.items.ItemAmulet;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID)
public class ItemNuitsVanishing extends ItemAmulet {
    private static boolean isInvisible;

    public ItemNuitsVanishing() {
        super();
        this.setMaxDamage(3600);
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
    public static void onTick(TickEvent.PlayerTickEvent event) {
        EntityPlayer player = event.player;
        World world = player.world;
        EnumHand hand = player.getHeldItem(EnumHand.OFF_HAND).getItem() == AtumItems.NUITS_VANISHING ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;
        ItemStack heldStack = player.getHeldItem(hand);
        if (IS_BAUBLES_INSTALLED && getAmulet(player).getItem() == AtumItems.NUITS_VANISHING) {
            heldStack = getAmulet(player);
        }
        if (event.phase == TickEvent.Phase.START) {
            if (heldStack.getItem() == AtumItems.NUITS_VANISHING) {
                if (!isPlayerMoving(player)) {
                    isInvisible = true;
                    if (!world.isRemote) {
                        heldStack.damageItem(1, player);
                        player.setInvisible(true);
                    }
                } else {
                    isInvisible = false;
                    if (!world.isRemote && player.isInvisible()) {
                        player.setInvisible(false);
                    }
                }
            } else {
                isInvisible = false;
                if (!world.isRemote && !player.isPotionActive(MobEffects.INVISIBILITY) && player.isInvisible()) {
                    player.setInvisible(false);
                }
            }
        }
    }

    public static boolean isPlayerMoving(EntityPlayer player) {
        return player.distanceWalkedModified != player.prevDistanceWalkedModified;
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