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
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.neoforged.neoforge.event.entity.living.LivingHurtEvent;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID)
public class AtemsProtectionItem extends AtumShieldItem implements IArtifact {
    private static final Object2BooleanMap<LivingEntity> IS_BLOCKING = new Object2BooleanOpenHashMap<>();
    protected static final Object2IntMap<LivingEntity> TIMER = new Object2IntOpenHashMap<>();

    public AtemsProtectionItem() {
        super(600, new Item.Properties().rarity(Rarity.RARE));
        this.setRepairItem(AtumItems.NEBU_INGOT.get());
    }

    @Override
    public God getGod() {
        return God.ATEM;
    }

    @Override
    public void onUseTick(@Nonnull Level level, @Nonnull LivingEntity livingEntity, @Nonnull ItemStack stack, int count) {
        super.onUseTick(level, livingEntity, stack, count);
        IS_BLOCKING.putIfAbsent(livingEntity, true);
    }

    @Override
    public void releaseUsing(@Nonnull ItemStack stack, @Nonnull Level level, @Nonnull LivingEntity livingEntity, int timeLeft) {
        super.releaseUsing(stack, level, livingEntity, timeLeft);
        IS_BLOCKING.removeBoolean(livingEntity);
    }

    @SubscribeEvent
    public static void onLivingTick(LivingEvent.LivingTickEvent event) {
        if (TIMER.containsKey(event.getEntity())) {
            LivingEntity livingEntity = event.getEntity();
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
        Entity source = event.getSource().getDirectEntity();
        LivingEntity livingEntity = event.getEntity();
        if (source instanceof LivingEntity && IS_BLOCKING.containsKey(livingEntity) && IS_BLOCKING.getBoolean(livingEntity)) {
            RandomSource random = ((LivingEntity) source).getRandom();
            if (random.nextDouble() <= 0.20D) {
                TIMER.put(livingEntity, 200);
                if (livingEntity.level() instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(AtumParticles.LIGHT_SPARKLE.get(), livingEntity.getX(), livingEntity.getY() + 1.0D, livingEntity.getZ(), 40, 0.1D, 0.0D, 0.1D, 0.01D);
                }
            }
            IS_BLOCKING.removeBoolean(livingEntity);
        }

        if (TIMER.containsKey(livingEntity)) {
            event.setAmount(0.0F);
        }
    }
}