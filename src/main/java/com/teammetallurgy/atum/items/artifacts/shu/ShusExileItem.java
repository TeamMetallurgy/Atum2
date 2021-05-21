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
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Rarity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID)
public class ShusExileItem extends BattleAxeItem implements IArtifact {
    private static final Object2FloatMap<PlayerEntity> COOLDOWN = new Object2FloatOpenHashMap<>();

    public ShusExileItem() {
        super(AtumMats.NEBU, 4.5F, -2.9F, new Item.Properties().rarity(Rarity.RARE));
    }

    @Override
    public God getGod() {
        return God.SHU;
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onAttack(AttackEntityEvent event) {
        PlayerEntity player = event.getPlayer();
        if (player.world.isRemote) return;
        if (event.getTarget() instanceof LivingEntity && player.getHeldItemMainhand().getItem() == AtumItems.SHUS_EXILE) {
            COOLDOWN.put(player, player.getCooledAttackStrength(0.5F));
        }
    }

    @SubscribeEvent
    public static void onKnockback(LivingKnockBackEvent event) {
        Entity attacker = event.getEntityLiving(); //TODO. No way to get attacker currently, both getEntityLiving and getEntity returns the target
        if (attacker instanceof PlayerEntity && COOLDOWN.containsKey(attacker)) {
            PlayerEntity player = (PlayerEntity) attacker;
            if (player.getHeldItemMainhand().getItem() == AtumItems.SHUS_EXILE && COOLDOWN.getFloat(attacker) == 1.0F) {
                LivingEntity target = event.getEntityLiving();
                event.setStrength(event.getStrength() * 3F);
                if (target.world instanceof ServerWorld) {
                    double x = MathHelper.nextDouble(random, 0.001D, 0.02D);
                    double z = MathHelper.nextDouble(random, 0.001D, 0.02D);
                    ServerWorld serverWorld = (ServerWorld) target.world;
                    serverWorld.spawnParticle(AtumParticles.SHU, target.getPosX() + (random.nextDouble() - 0.5D) * (double) target.getWidth(), target.getPosY() + target.getEyeHeight(), target.getPosZ() + (random.nextDouble() - 0.5D) * (double) target.getWidth(), 12, x, 0.04D, -z, 0.015D);
                }
            }
            COOLDOWN.removeFloat(attacker);
        }
    }
}