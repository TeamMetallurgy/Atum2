package com.teammetallurgy.atum.items.artifacts.nepthys;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.api.God;
import com.teammetallurgy.atum.api.IArtifact;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.init.AtumParticles;
import com.teammetallurgy.atum.items.tools.AtumShieldItem;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.entity.living.LivingHurtEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID)
public class NepthysConsecrationItem extends AtumShieldItem implements IArtifact {
    private static final Object2BooleanMap<LivingEntity> IS_BLOCKING = new Object2BooleanOpenHashMap<>();

    public NepthysConsecrationItem() {
        super(500, new Item.Properties().rarity(Rarity.RARE));
        this.setRepairItem(AtumItems.NEBU_INGOT.get());
    }

    @Override
    public God getGod() {
        return God.NEPTHYS;
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
    public static void onHurt(LivingHurtEvent event) {
        Entity source = event.getSource().getDirectEntity();
        LivingEntity livingEntity = event.getEntity();
        if (source instanceof LivingEntity && IS_BLOCKING.containsKey(livingEntity) && ((LivingEntity) source).getMobType() == MobType.UNDEAD /*&& random.nextFloat() <= 0.50F*/) {
            source.setSecondsOnFire(8);
            source.hurt(livingEntity.damageSources().generic(), 2.0F);
            if (livingEntity.level instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(AtumParticles.LIGHT_SPARKLE.get(), livingEntity.getX(), livingEntity.getY() + 1.0D, livingEntity.getZ(), 40, 0.1D, 0.0D, 0.1D, 0.01D);
            }
            IS_BLOCKING.removeBoolean(livingEntity);
        }
    }
}