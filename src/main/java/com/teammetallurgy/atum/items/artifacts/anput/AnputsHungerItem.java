package com.teammetallurgy.atum.items.artifacts.anput;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.api.God;
import com.teammetallurgy.atum.api.IArtifact;
import com.teammetallurgy.atum.api.material.AtumMaterialTiers;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.items.tools.DaggerItem;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID)
public class AnputsHungerItem extends DaggerItem implements IArtifact {
    protected static final Object2IntMap<Player> HUNGER_TIMER = new Object2IntOpenHashMap<>();

    public AnputsHungerItem() {
        super(AtumMaterialTiers.NEBU, new Item.Properties().rarity(Rarity.RARE));
    }

    @Override
    public God getGod() {
        return God.ANPUT;
    }

    @Override
    public void inventoryTick(@Nonnull ItemStack stack, @Nonnull Level level, @Nonnull Entity entity, int itemSlot, boolean isSelected) {
        if (entity instanceof Player player) {
            if (player.getItemInHand(InteractionHand.MAIN_HAND).getItem() == AtumItems.ANPUTS_HUNGER.get()) {
                HUNGER_TIMER.putIfAbsent(player, 80);
                int hungerTimer = HUNGER_TIMER.getInt(player);
                if (hungerTimer > 0 && player.getFoodData().getFoodLevel() > 0) {
                    HUNGER_TIMER.replace(player, hungerTimer - 1);
                }
                if (hungerTimer == 0) {
                    player.getFoodData().eat(-1, 0.0F);
                    HUNGER_TIMER.replace(player, 80);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onAttack(AttackEntityEvent event) {
        Player player = event.getEntity();
        if (event.getTarget() instanceof LivingEntity && player.getItemInHand(InteractionHand.MAIN_HAND).getItem() == AtumItems.ANPUTS_HUNGER.get()) {
            player.getFoodData().eat(1, 0.0F);
            ((LivingEntity) event.getTarget()).addEffect(new MobEffectInstance(MobEffects.CONFUSION, 40));
            HUNGER_TIMER.replace(player, 200);
        }
    }
}