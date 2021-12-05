package com.teammetallurgy.atum.items.tools;

import com.teammetallurgy.atum.Atum;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID)
public class HammerItem extends SwordItem {
    private static final AttributeModifier STUN_MODIFIER = new AttributeModifier(UUID.fromString("b4ebf092-fe62-4250-b945-7dc45b2f1036"), "Hammer stun", -1000.0D, AttributeModifier.Operation.ADDITION);
    private static final Object2FloatMap<Player> COOLDOWN = new Object2FloatOpenHashMap<>();
    protected static final Object2IntMap<LivingEntity> STUN = new Object2IntOpenHashMap<>();

    protected HammerItem(Tier tier, Item.Properties properties) {
        super(tier, 17, -3.55F, properties.tab(Atum.GROUP));
    }

    @SubscribeEvent
    public static void onHurt(LivingHurtEvent event) {
        Entity trueSource = event.getSource().getEntity();
        if (trueSource instanceof Player && COOLDOWN.containsKey(trueSource)) {
            if (COOLDOWN.getFloat(trueSource) == 1.0F) {
                Item heldItem = ((Player)trueSource).getMainHandItem().getItem();
                if (heldItem instanceof HammerItem) {
                    HammerItem hammerItem = (HammerItem) heldItem;
                    LivingEntity target = event.getEntityLiving();
                    AttributeInstance attribute = target.getAttribute(Attributes.MOVEMENT_SPEED);
                    if (attribute != null && !attribute.hasModifier(STUN_MODIFIER)) {
                        attribute.addTransientModifier(STUN_MODIFIER);
                        hammerItem.onStun(target);
                    }
                }
            }
            COOLDOWN.removeFloat(trueSource);
        }
    }

    protected void onStun(LivingEntity target) {
        STUN.put(target, 40);
    }

    @SubscribeEvent
    public static void livingTick(LivingEvent.LivingUpdateEvent event) {
        LivingEntity entity = event.getEntityLiving();
        if (STUN.isEmpty()) return;
        AttributeInstance attribute = entity.getAttribute(Attributes.MOVEMENT_SPEED);
        if (attribute != null && attribute.hasModifier(STUN_MODIFIER)) {
            int stunTime = STUN.getInt(entity);
            if (stunTime <= 1) {
                attribute.removeModifier(STUN_MODIFIER);
                STUN.remove(entity, stunTime);
            } else {
                STUN.replace(entity, stunTime - 1);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onAttack(AttackEntityEvent event) {
        Player player = event.getPlayer();
        if (player.level.isClientSide) return;
        if (event.getTarget() instanceof LivingEntity && player.getMainHandItem().getItem() instanceof HammerItem) {
            COOLDOWN.put(player, player.getAttackStrengthScale(0.5F));
        }
    }
}