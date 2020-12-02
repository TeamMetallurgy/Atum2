package com.teammetallurgy.atum.items.artifacts.atem;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.api.AtumMats;
import com.teammetallurgy.atum.api.God;
import com.teammetallurgy.atum.api.IArtifact;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.init.AtumParticles;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.item.SwordItem;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID)
public class AtemsWillItem extends SwordItem implements IArtifact {
    private static final Object2FloatMap<PlayerEntity> COOLDOWN = new Object2FloatOpenHashMap<>();

    public AtemsWillItem() {
        super(AtumMats.NEBU, 3, -2.4F, new Item.Properties().group(Atum.GROUP));
    }

    @Override
    public God getGod() {
        return God.ATEM;
    }

    @Override
    @Nonnull
    public Rarity getRarity(@Nonnull ItemStack stack) {
        return Rarity.RARE;
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onAttack(AttackEntityEvent event) {
        PlayerEntity player = event.getPlayer();
        if (player.world.isRemote) return;
        if (event.getTarget() instanceof LivingEntity && ((LivingEntity) event.getTarget()).getCreatureAttribute() == CreatureAttribute.UNDEAD) {
            if (player.getHeldItemMainhand().getItem() == AtumItems.ATEMS_WILL) {
                COOLDOWN.put(player, player.getCooledAttackStrength(0.5F));
            }
        }
    }

    @SubscribeEvent
    public static void onHurt(LivingHurtEvent event) {
        Entity trueSource = event.getSource().getTrueSource();
        if (trueSource instanceof PlayerEntity && COOLDOWN.containsKey(trueSource)) {
            if (COOLDOWN.getFloat(trueSource) == 1.0F) {
                LivingEntity target = event.getEntityLiving();
                event.setAmount(event.getAmount() * 2);
                if (target.world instanceof ServerWorld) {
                    ServerWorld serverWorld = (ServerWorld) target.world;
                    serverWorld.spawnParticle(AtumParticles.LIGHT_SPARKLE, target.getPosX() + (random.nextDouble() - 0.5D) * (double) target.getWidth(), target.getPosY() + (target.getHeight() / 2), target.getPosZ() + (random.nextDouble() - 0.5D) * (double) target.getWidth(), 16, 0.25D, 0.05D, 0.25D, 0.01D);
                }
            }
            COOLDOWN.removeFloat(trueSource);
        }
    }
}