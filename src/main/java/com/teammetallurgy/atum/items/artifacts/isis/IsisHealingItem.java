package com.teammetallurgy.atum.items.artifacts.isis;

import com.teammetallurgy.atum.api.God;
import com.teammetallurgy.atum.api.IArtifact;
import com.teammetallurgy.atum.init.AtumParticles;
import com.teammetallurgy.atum.items.tools.AmuletItem;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;

public class IsisHealingItem extends AmuletItem implements IArtifact {
    protected static final Object2IntMap<LivingEntity> DURATION = new Object2IntOpenHashMap<>();

    public IsisHealingItem() {
        super(new Item.Properties().maxDamage(96));
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
            World world = livingEntity.getEntityWorld();
            if (world instanceof ServerWorld) {
                ServerWorld serverWorld = (ServerWorld) world;
                double x = MathHelper.nextDouble(world.rand, 0.0001D, 0.05D);
                double z = MathHelper.nextDouble(world.rand, 0.0001D, 0.05D);
                serverWorld.spawnParticle(AtumParticles.ISIS, livingEntity.getPosX(), livingEntity.getPosY() + 1.2D, livingEntity.getPosZ(), 24, x, 0.0D, -z, 0.02D);
            }
            if (!world.isRemote) {
                livingEntity.heal(1.0F);
                DURATION.replace(livingEntity, 40);
                if (livingEntity instanceof  PlayerEntity) {
                    PlayerEntity player = (PlayerEntity) livingEntity;
                    if (!player.abilities.isCreativeMode) {
                        stack.damageItem(1, player, (e) -> e.sendBreakAnimation(e.getActiveHand()));
                    }
                }
            }
        }
    }
}