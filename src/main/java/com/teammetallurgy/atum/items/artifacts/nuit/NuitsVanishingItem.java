package com.teammetallurgy.atum.items.artifacts.nuit;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.api.God;
import com.teammetallurgy.atum.api.IArtifact;
import com.teammetallurgy.atum.items.artifacts.AmuletItem;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effects;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID)
public class NuitsVanishingItem extends AmuletItem implements IArtifact {
    protected static final Object2BooleanMap<LivingEntity> INVISIBLE = new Object2BooleanOpenHashMap<>();
    public static final Object2IntMap<LivingEntity> TIMER = new Object2IntOpenHashMap<>();

    public NuitsVanishingItem() {
        super(new Item.Properties().maxStackSize(1));
    }

    @Override
    public God getGod() {
        return God.NUIT;
    }

    @SubscribeEvent
    public static void onTarget(LivingSetAttackTargetEvent event) {
        if (TIMER.getInt(event.getTarget()) <= 0 && INVISIBLE.getBoolean(event.getTarget()) && event.getTarget() instanceof PlayerEntity && event.getEntityLiving() instanceof MobEntity) {
            ((MobEntity) event.getEntityLiving()).setAttackTarget(null);
        }
    }

    @SubscribeEvent
    public static void onAttack(LivingAttackEvent event) {
        Entity source = event.getSource().getTrueSource();
        if (source instanceof LivingEntity) {
            LivingEntity attacker = (LivingEntity) source;
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
        World world = livingEntity.getEntityWorld();
        if (!TIMER.containsKey(livingEntity)) {
            INVISIBLE.putIfAbsent(livingEntity, true);

            if (!isLivingEntityMoving(livingEntity)) {
                INVISIBLE.replace(livingEntity, true);
                if (!world.isRemote) {
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
        if (!livingEntity.getEntityWorld().isRemote && !livingEntity.isPotionActive(Effects.INVISIBILITY) && livingEntity.isInvisible()) {
            livingEntity.setInvisible(false);
        }
    }

    public static boolean isLivingEntityMoving(LivingEntity livingEntity) {
        return livingEntity.distanceWalkedModified != livingEntity.prevDistanceWalkedModified || livingEntity.isCrouching();
    }
}