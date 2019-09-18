package com.teammetallurgy.atum.items.artifacts.nuit;

import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.items.AmuletItem;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effects;
import net.minecraft.util.Hand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID)
public class NuitsVanishingItem extends AmuletItem {
    private static boolean isInvisible;

    public NuitsVanishingItem() {
        super();
        this.setMaxDamage(3600);
    }

    @SubscribeEvent
    public static void onTarget(LivingSetAttackTargetEvent event) {
        if (isInvisible && event.getTarget() != null && event.getEntityLiving() != null) {
            if (event.getTarget() instanceof EntityPlayer) {
                ((LivingEntity) event.getEntityLiving()).setAttackTarget(null);
            }
        }
    }

    @SubscribeEvent
    public static void onTick(TickEvent.PlayerTickEvent event) {
        PlayerEntity player = event.player;
        World world = player.world;
        Hand hand = player.getHeldItem(Hand.OFF_HAND).getItem() == AtumItems.NUITS_VANISHING ? Hand.OFF_HAND : Hand.MAIN_HAND;
        ItemStack heldStack = player.getHeldItem(hand);
        if (IS_BAUBLES_INSTALLED && getAmulet(player).getItem() == AtumItems.NUITS_VANISHING) {
            heldStack = getAmulet(player);
        }
        if (event.phase == TickEvent.Phase.START) {
            if (heldStack.getItem() == AtumItems.NUITS_VANISHING) {
                if (!isPlayerMoving(player)) {
                    isInvisible = true;
                    if (!world.isRemote) {
                        heldStack.damageItem(1, player, (entity) -> {
                            entity.sendBreakAnimation(hand);
                        });
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
                if (!world.isRemote && !player.isPotionActive(Effects.INVISIBILITY) && player.isInvisible()) {
                    player.setInvisible(false);
                }
            }
        }
    }

    public static boolean isPlayerMoving(PlayerEntity player) {
        return player.distanceWalkedModified != player.prevDistanceWalkedModified || player.isSneaking();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(@Nonnull ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag tooltipType) {
        if (Keyboard.isKeyDown(42)) {
            tooltip.add(TextFormatting.DARK_PURPLE + I18n.format(this.getTranslationKey() + ".line1"));
            tooltip.add(TextFormatting.DARK_PURPLE + I18n.format(this.getTranslationKey() + ".line2"));
        } else {
            tooltip.add(I18n.format(this.getTranslationKey() + ".line3") + " " + TextFormatting.DARK_GRAY + "[SHIFT]");
        }
    }
}