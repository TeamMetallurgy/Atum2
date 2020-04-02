package com.teammetallurgy.atum.items.artifacts.nuit;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.items.tools.AmuletItem;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effects;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID)
public class NuitsVanishingItem extends AmuletItem {
    private static boolean isInvisible;

    public NuitsVanishingItem() {
        super(new Item.Properties().maxDamage(3600));
    }

    @SubscribeEvent
    public static void onTarget(LivingSetAttackTargetEvent event) {
        if (isInvisible && event.getTarget() instanceof PlayerEntity && event.getEntityLiving() instanceof MobEntity) {
            ((MobEntity) event.getEntityLiving()).setAttackTarget(null);
        }
    }

    @SubscribeEvent
    public static void onTick(TickEvent.PlayerTickEvent event) {
        PlayerEntity player = event.player;
        World world = player.world;
        Hand hand = player.getHeldItem(Hand.OFF_HAND).getItem() == AtumItems.NUITS_VANISHING ? Hand.OFF_HAND : Hand.MAIN_HAND;
        ItemStack heldStack = player.getHeldItem(hand);
        /*if (IS_BAUBLES_INSTALLED && getAmulet(player).getItem() == AtumItems.NUITS_VANISHING) {
            heldStack = getAmulet(player);
        }*/
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
        return player.distanceWalkedModified != player.prevDistanceWalkedModified || player.isCrouching();
    }
}