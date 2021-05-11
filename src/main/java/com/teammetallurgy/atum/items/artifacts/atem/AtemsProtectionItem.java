package com.teammetallurgy.atum.items.artifacts.atem;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.api.God;
import com.teammetallurgy.atum.api.IArtifact;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.init.AtumParticles;
import com.teammetallurgy.atum.items.tools.AtumShieldItem;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID)
public class AtemsProtectionItem extends AtumShieldItem implements IArtifact {
    private static final Object2BooleanMap<LivingEntity> IS_BLOCKING = new Object2BooleanOpenHashMap<>();
    protected static final Object2IntMap<LivingEntity> TIMER = new Object2IntOpenHashMap<>();

    public AtemsProtectionItem() {
        super(600, new Item.Properties().rarity(Rarity.RARE));
        this.setRepairItem(AtumItems.NEBU_INGOT);
    }

    @Override
    public God getGod() {
        return God.ATEM;
    }

    @Override
    public void onUse(@Nonnull World world, @Nonnull LivingEntity livingEntity, @Nonnull ItemStack stack, int count) {
        super.onUse(world, livingEntity, stack, count);
        IS_BLOCKING.putIfAbsent(livingEntity, true);
    }

    @Override
    public void onPlayerStoppedUsing(@Nonnull ItemStack stack, @Nonnull World world, @Nonnull LivingEntity livingEntity, int timeLeft) {
        super.onPlayerStoppedUsing(stack, world, livingEntity, timeLeft);
        IS_BLOCKING.removeBoolean(livingEntity);
    }

    @SubscribeEvent
    public static void onLivingTick(LivingEvent.LivingUpdateEvent event) {
        if (TIMER.containsKey(event.getEntityLiving())) {
            LivingEntity livingEntity = event.getEntityLiving();
            int timer = TIMER.getInt(livingEntity);
            if (timer == 0) {
                TIMER.removeInt(livingEntity);
            }

            if (timer > 0) {
                TIMER.replace(livingEntity, timer - 1);
            }
        }
    }

    @SubscribeEvent
    public static void onLivingDamage(LivingHurtEvent event) {
        Entity source = event.getSource().getImmediateSource();
        LivingEntity livingEntity = event.getEntityLiving();
        if (source instanceof LivingEntity && IS_BLOCKING.containsKey(livingEntity) && IS_BLOCKING.getBoolean(livingEntity)) {
            if (random.nextDouble() <= 0.20D) {
                TIMER.put(livingEntity, 200);
                if (livingEntity.world instanceof ServerWorld) {
                    ServerWorld serverWorld = (ServerWorld) livingEntity.world;
                    serverWorld.spawnParticle(AtumParticles.LIGHT_SPARKLE, livingEntity.getPosX(), livingEntity.getPosY() + 1.0D, livingEntity.getPosZ(), 40, 0.1D, 0.0D, 0.1D, 0.01D);
                }
            }
            IS_BLOCKING.removeBoolean(livingEntity);
        }

        if (TIMER.containsKey(livingEntity)) {
            event.setAmount(0.0F);
        }
    }
}