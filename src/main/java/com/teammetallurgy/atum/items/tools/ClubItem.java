package com.teammetallurgy.atum.items.tools;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.entity.stone.StoneBaseEntity;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.neoforged.neoforge.event.entity.living.LivingHurtEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID)
public class ClubItem extends SwordItem {
    private static final Object2FloatMap<LivingEntity> cooldown = new Object2FloatOpenHashMap<>();

    public ClubItem(Tier itemTier) {
        super(itemTier, 13, -3.4F, new Item.Properties());
    }

    @SubscribeEvent
    public static void onHurt(LivingHurtEvent event) {
        LivingEntity target = event.getEntity();
        Entity source = event.getSource().getEntity();
        if (!(target instanceof StoneBaseEntity) && source instanceof LivingEntity attacker) {
            if (attacker.getMainHandItem().getItem() instanceof ClubItem) {
                float knockback = 0.0F;
                if (cooldown.getFloat(attacker) == 1.0F) {
                    knockback = 1.8F;
                }
                target.push(-Mth.sin(attacker.getYRot() * 3.1415927F / 180.0F) * knockback * 0.5F, 0.1D, Mth.cos(attacker.getYRot() * 3.1415927F / 180.0F) * knockback * 0.5F);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onAttack(AttackEntityEvent event) {
        Player player = event.getEntity();
        if (player.level().isClientSide) return;
        if (event.getTarget() instanceof LivingEntity && !(event.getTarget() instanceof StoneBaseEntity)) {
            if (player.getMainHandItem().getItem() instanceof ClubItem) {
                cooldown.put(player, player.getAttackStrengthScale(0.5F));
            }
        }
    }
}