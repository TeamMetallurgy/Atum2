package com.teammetallurgy.atum.items.artifacts.nuit;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.api.God;
import com.teammetallurgy.atum.api.IArtifact;
import com.teammetallurgy.atum.items.artifacts.AmuletItem;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.living.LivingAttackEvent;
import net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID)
public class NuitsVanishingItem extends AmuletItem implements IArtifact {
    protected static final Object2BooleanMap<LivingEntity> INVISIBLE = new Object2BooleanOpenHashMap<>();
    public static final Object2IntMap<LivingEntity> TIMER = new Object2IntOpenHashMap<>();

    public NuitsVanishingItem() {
        super(new Item.Properties().stacksTo(1));
    }

    @Override
    public God getGod() {
        return God.NUIT;
    }

    @SubscribeEvent
    public static void onTarget(LivingChangeTargetEvent event) {
        if (TIMER.getInt(event.getOriginalTarget()) <= 0 && INVISIBLE.getBoolean(event.getOriginalTarget()) && event.getOriginalTarget() instanceof Player && event.getEntity() instanceof Mob) {
            event.setNewTarget(null);
        }
    }

    @SubscribeEvent
    public static void onAttack(LivingAttackEvent event) {
        Entity source = event.getSource().getEntity();
        if (source instanceof LivingEntity attacker) {
            if (INVISIBLE.getBoolean(attacker)) {
                setNotInvisible(attacker);
                TIMER.putIfAbsent(attacker, 200);
            }
        }
    }

    @Override
    public void onUnequip(String identifier, int index, LivingEntity livingEntity, @Nonnull ItemStack stack) {
        setNotInvisible(livingEntity);
    }

    @Override
    public void curioBreak(@Nonnull ItemStack stack, LivingEntity livingEntity) {
        setNotInvisible(livingEntity);
    }

    @Override
    public void curioTick(String identifier, int index, LivingEntity livingEntity, @Nonnull ItemStack stack) {
        Level level = livingEntity.getCommandSenderWorld();
        if (!TIMER.containsKey(livingEntity)) {
            INVISIBLE.putIfAbsent(livingEntity, true);

            if (!isLivingEntityMoving(livingEntity)) {
                INVISIBLE.replace(livingEntity, true);
                if (!level.isClientSide) {
                    livingEntity.setInvisible(true);
                }
            } else {
                setNotInvisible(livingEntity);
            }
        }

        if (TIMER.containsKey(livingEntity)) {
            int timer = TIMER.getInt(livingEntity);
            if (timer == 0) {
                TIMER.removeInt(livingEntity);
            }

            if (timer > 0) {
                TIMER.replace(livingEntity, timer - 1);
            }
        }
    }

    public static void setNotInvisible(LivingEntity livingEntity) {
        INVISIBLE.replace(livingEntity, false);
        if (!livingEntity.getCommandSenderWorld().isClientSide && !livingEntity.hasEffect(MobEffects.INVISIBILITY) && livingEntity.isInvisible()) {
            livingEntity.setInvisible(false);
        }
    }

    public static boolean isLivingEntityMoving(LivingEntity livingEntity) {
        return livingEntity.walkDist != livingEntity.walkDistO || livingEntity.isCrouching();
    }
}