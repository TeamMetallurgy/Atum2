package com.teammetallurgy.atum.entity.projectile.arrow;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.init.AtumEntities;
import com.teammetallurgy.atum.init.AtumParticles;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.neoforged.neoforge.network.PlayMessages;

import javax.annotation.Nonnull;

public class ArrowSlownessEntity extends CustomArrow {
    private float velocity;

    public ArrowSlownessEntity(PlayMessages.SpawnEntity spawnEntity, Level level) {
        this(AtumEntities.SLOWNESS_ARROW.get(), level);
    }

    public ArrowSlownessEntity(EntityType<? extends ArrowSlownessEntity> entityType, Level level) {
        super(entityType, level);
    }

    public ArrowSlownessEntity(Level level, LivingEntity shooter, float velocity) {
        super(AtumEntities.SLOWNESS_ARROW.get(), level, shooter);
        this.velocity = velocity;
    }

    @Override
    protected void onHitEntity(@Nonnull EntityHitResult rayTraceResult) {
        Entity entity = rayTraceResult.getEntity();
        if (!level.isClientSide && entity instanceof LivingEntity livingBase) {
            float chance = 0.25F;
            if (velocity == 1.0F) {
                chance = 1.0F;
            }
            if (random.nextFloat() <= chance) {
                livingBase.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 1, false, true));
                if (level instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(AtumParticles.GEB.get(), entity.getX(), this.getY(), entity.getZ(), 15, 0.0D, -0.06D, 0.0D, 0.025D);
                }
            }
        }
        super.onHitEntity(rayTraceResult);
    }

    @Override
    public ResourceLocation getTexture() {
        return new ResourceLocation(Atum.MOD_ID, "textures/arrow/arrow_slowness.png");
    }
}