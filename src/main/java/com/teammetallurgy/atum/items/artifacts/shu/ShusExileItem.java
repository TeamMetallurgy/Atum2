package com.teammetallurgy.atum.items.artifacts.shu;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.api.AtumMats;
import com.teammetallurgy.atum.api.God;
import com.teammetallurgy.atum.api.IArtifact;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.init.AtumParticles;
import com.teammetallurgy.atum.items.tools.BattleAxeItem;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID)
public class ShusExileItem extends BattleAxeItem implements IArtifact {
    private static final Object2FloatMap<Player> COOLDOWN = new Object2FloatOpenHashMap<>();

    public ShusExileItem() {
        super(AtumMats.NEBU, 4.5F, -2.9F, new Item.Properties().rarity(Rarity.RARE));
    }

    @Override
    public God getGod() {
        return God.SHU;
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onAttack(AttackEntityEvent event) {
        Player attacker = event.getEntity();
        if (attacker.level.isClientSide) return;
        if (event.getTarget() instanceof LivingEntity && attacker.getMainHandItem().getItem() == AtumItems.SHUS_EXILE.get()) {
            COOLDOWN.put(attacker, attacker.getAttackStrengthScale(0.5F));

            if (COOLDOWN.getFloat(attacker) == 1.0F) {
                LivingEntity target = (LivingEntity) event.getTarget();
                float defaultKnockback = 0.5F;
                target.knockback(defaultKnockback * 3, Mth.sin(attacker.getYRot() * ((float) Math.PI / 180F)), -Mth.cos(attacker.getYRot() * ((float) Math.PI / 180F)));
                if (target.level instanceof ServerLevel serverLevel) {
                    RandomSource random = target.level.random;
                    double x = Mth.nextDouble(random, 0.001D, 0.02D);
                    double z = Mth.nextDouble(random, 0.001D, 0.02D);
                    serverLevel.sendParticles(AtumParticles.SHU.get(), target.getX() + (random.nextDouble() - 0.5D) * (double) target.getBbWidth(), target.getY() + target.getEyeHeight(), target.getZ() + (random.nextDouble() - 0.5D) * (double) target.getBbWidth(), 12, x, 0.04D, -z, 0.015D);
                }
            }
        }
    }
}