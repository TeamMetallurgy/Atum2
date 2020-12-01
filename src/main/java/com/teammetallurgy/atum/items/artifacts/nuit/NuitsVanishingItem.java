package com.teammetallurgy.atum.items.artifacts.nuit;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.items.tools.AmuletItem;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effects;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID)
public class NuitsVanishingItem extends AmuletItem {
    protected static final Object2BooleanMap<LivingEntity> INVISIBLE = new Object2BooleanOpenHashMap<>();

    public NuitsVanishingItem() {
        super(new Item.Properties().maxDamage(3600));
    }

    @SubscribeEvent
    public static void onTarget(LivingSetAttackTargetEvent event) {
        if (INVISIBLE.getBoolean(event.getTarget()) && event.getTarget() instanceof PlayerEntity && event.getEntityLiving() instanceof MobEntity) {
            ((MobEntity) event.getEntityLiving()).setAttackTarget(null);
        }
    }

    @Override
    public void onUnequip(String identifier, int index, LivingEntity livingEntity, @Nonnull ItemStack stack) {
        INVISIBLE.replace(livingEntity, false);
        if (!livingEntity.getEntityWorld().isRemote && !livingEntity.isPotionActive(Effects.INVISIBILITY) && livingEntity.isInvisible()) {
            livingEntity.setInvisible(false);
        }
    }

    @Override
    public void curioTick(String identifier, int index, LivingEntity livingEntity, @Nonnull ItemStack stack) {
        World world = livingEntity.getEntityWorld();
        INVISIBLE.putIfAbsent(livingEntity, true);

        if (!isLivingEntityMoving(livingEntity)) {
            INVISIBLE.replace(livingEntity, true);
            if (!world.isRemote) {
                stack.damageItem(1, livingEntity, (entity) -> {
                    entity.sendBreakAnimation(entity.getActiveHand());
                });
                livingEntity.setInvisible(true);
            }
        } else {
            INVISIBLE.replace(livingEntity, false);
            if (!world.isRemote && livingEntity.isInvisible()) {
                livingEntity.setInvisible(false);
            }
        }
    }

    public static boolean isLivingEntityMoving(LivingEntity livingEntity) {
        return livingEntity.distanceWalkedModified != livingEntity.prevDistanceWalkedModified || livingEntity.isCrouching();
    }
}