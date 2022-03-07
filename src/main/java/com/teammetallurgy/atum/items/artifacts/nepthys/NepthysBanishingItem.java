package com.teammetallurgy.atum.items.artifacts.nepthys;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.api.AtumMats;
import com.teammetallurgy.atum.api.God;
import com.teammetallurgy.atum.api.IArtifact;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.init.AtumParticles;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SwordItem;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID)
public class NepthysBanishingItem extends SwordItem implements IArtifact {
    private static final Object2FloatMap<Player> COOLDOWN = new Object2FloatOpenHashMap<>();

    public NepthysBanishingItem() {
        super(AtumMats.NEBU, 3, -2.4F, new Item.Properties().rarity(Rarity.RARE).tab(Atum.GROUP));
    }

    @Override
    public God getGod() {
        return God.NEPTHYS;
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onAttack(AttackEntityEvent event) {
        Player player = event.getPlayer();
        if (player.level.isClientSide) return;
        if (event.getTarget() instanceof LivingEntity && ((LivingEntity) event.getTarget()).getMobType() == MobType.UNDEAD) {
            if (player.getMainHandItem().getItem() == AtumItems.NEPTHYS_BANISHING.get()) {
                COOLDOWN.put(player, player.getAttackStrengthScale(0.5F));
            }
        }
    }

    @SubscribeEvent
    public static void onHurt(LivingHurtEvent event) {
        Entity trueSource = event.getSource().getEntity();
        if (trueSource instanceof Player && COOLDOWN.containsKey(trueSource)) {
            if (COOLDOWN.getFloat(trueSource) == 1.0F) {
                LivingEntity target = event.getEntityLiving();
                event.setAmount(event.getAmount() * 2);
                if (target.level instanceof ServerLevel serverLevel) {
                    Random random = serverLevel.random;
                    serverLevel.sendParticles(AtumParticles.LIGHT_SPARKLE.get(), target.getX() + (random.nextDouble() - 0.5D) * (double) target.getBbWidth(), target.getY() + (target.getBbHeight() / 2), target.getZ() + (random.nextDouble() - 0.5D) * (double) target.getBbWidth(), 16, 0.25D, 0.05D, 0.25D, 0.01D);
                }
            }
            COOLDOWN.removeFloat(trueSource);
        }
    }
}