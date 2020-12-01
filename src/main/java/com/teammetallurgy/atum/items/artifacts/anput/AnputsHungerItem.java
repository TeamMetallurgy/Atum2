package com.teammetallurgy.atum.items.artifacts.anput;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.api.AtumMats;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.items.tools.DaggerItem;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID)
public class AnputsHungerItem extends DaggerItem {
    protected static final Object2IntMap<PlayerEntity> HUNGER_TIMER = new Object2IntOpenHashMap<>();

    public AnputsHungerItem() {
        super(AtumMats.NEBU, new Item.Properties().rarity(Rarity.RARE));
    }

    @Override
    public void inventoryTick(@Nonnull ItemStack stack, @Nonnull World world, @Nonnull Entity entity, int itemSlot, boolean isSelected) {
        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            if (player.getHeldItem(Hand.MAIN_HAND).getItem() == AtumItems.ANPUTS_HUNGER) {
                HUNGER_TIMER.putIfAbsent(player, 80);
                int hungerTimer = HUNGER_TIMER.getInt(player);
                if (hungerTimer > 0 && player.getFoodStats().getFoodLevel() > 0) {
                    HUNGER_TIMER.replace(player, hungerTimer - 1);
                }
                if (hungerTimer == 0) {
                    player.getFoodStats().addStats(-1, 0.0F);
                    HUNGER_TIMER.replace(player, 80);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onAttack(AttackEntityEvent event) {
        PlayerEntity player = event.getPlayer();
        if (event.getTarget() instanceof LivingEntity && player.getHeldItem(Hand.MAIN_HAND).getItem() == AtumItems.ANPUTS_HUNGER) {
            player.getFoodStats().addStats(1, 0.0F);
            ((LivingEntity) event.getTarget()).addPotionEffect(new EffectInstance(Effects.NAUSEA, 40));
            HUNGER_TIMER.replace(player, 200);
        }
    }
}