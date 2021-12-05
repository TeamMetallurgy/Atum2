package com.teammetallurgy.atum.items.artifacts.isis;

import com.teammetallurgy.atum.api.God;
import com.teammetallurgy.atum.api.IArtifact;
import com.teammetallurgy.atum.init.AtumParticles;
import com.teammetallurgy.atum.items.artifacts.AmuletItem;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;

import javax.annotation.Nonnull;

public class IsisHealingItem extends AmuletItem implements IArtifact {
    protected static final Object2IntMap<LivingEntity> DURATION = new Object2IntOpenHashMap<>();

    public IsisHealingItem() {
        super(new Item.Properties().stacksTo(1));
    }

    @Override
    public God getGod() {
        return God.ISIS;
    }

    @Override
    public void curioTick(String identifier, int index, LivingEntity livingEntity, @Nonnull ItemStack stack) {
        DURATION.putIfAbsent(livingEntity, 40);

        int duration = DURATION.getInt(livingEntity);
        if (duration > 0) {
            DURATION.replace(livingEntity, duration - 1);
        }
        if (duration == 0) {
            this.doEffect(livingEntity, stack);
        }
    }

    private void doEffect(LivingEntity livingEntity, @Nonnull ItemStack stack) {
        if (livingEntity.getHealth() < livingEntity.getMaxHealth()) {
            Level world = livingEntity.getCommandSenderWorld();
            if (world instanceof ServerLevel) {
                ServerLevel serverWorld = (ServerLevel) world;
                double x = Mth.nextDouble(world.random, 0.0001D, 0.05D);
                double z = Mth.nextDouble(world.random, 0.0001D, 0.05D);
                serverWorld.sendParticles(AtumParticles.ISIS, livingEntity.getX(), livingEntity.getY() + 1.2D, livingEntity.getZ(), 24, x, 0.0D, -z, 0.02D);
            }
            if (!world.isClientSide) {
                livingEntity.heal(1.0F);
                DURATION.replace(livingEntity, 40);
            }
        }
    }
}