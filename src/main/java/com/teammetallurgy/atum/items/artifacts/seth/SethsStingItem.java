package com.teammetallurgy.atum.items.artifacts.seth;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.api.God;
import com.teammetallurgy.atum.api.IArtifact;
import com.teammetallurgy.atum.api.material.AtumMaterialTiers;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.init.AtumParticles;
import com.teammetallurgy.atum.items.tools.DaggerItem;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.living.LivingHurtEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID)
public class SethsStingItem extends DaggerItem implements IArtifact {
    private static final Object2FloatMap<Player> COOLDOWN = new Object2FloatOpenHashMap<>();

    public SethsStingItem() {
        super(AtumMaterialTiers.NEBU, new Item.Properties().rarity(Rarity.RARE));
    }

    @Override
    public God getGod() {
        return God.SETH;
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onAttack(AttackEntityEvent event) {
        Player player = event.getEntity();
        if (player.level().isClientSide) return;
        if (event.getTarget() instanceof LivingEntity) {
            if (player.getMainHandItem().getItem() == AtumItems.SETHS_STING.get()) {
                COOLDOWN.put(player, player.getAttackStrengthScale(0.5F));
            }
        }
    }

    @SubscribeEvent
    public static void onHurt(LivingHurtEvent event) {
        Entity trueSource = event.getSource().getEntity();
        if (trueSource instanceof Player && COOLDOWN.containsKey(trueSource)) {
            if (COOLDOWN.getFloat(trueSource) == 1.0F) {
                LivingEntity target = event.getEntity();
                target.addEffect(new MobEffectInstance(MobEffects.POISON, 80, 2));
                if (trueSource.level() instanceof ServerLevel serverLevel) {
                    RandomSource random = serverLevel.random;
                    serverLevel.sendParticles(AtumParticles.SETH.get(), target.getX() + (random.nextDouble() - 0.5D) * (double) target.getBbWidth(), target.getY(), target.getZ() + (random.nextDouble() - 0.5D) * (double) target.getBbWidth(), 10, 0.07D, 0.6D, 0.07D, 0.4D);
                }
            }
            COOLDOWN.removeFloat(trueSource);
        }
    }
}