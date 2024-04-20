package com.teammetallurgy.atum.entity.projectile;

import com.teammetallurgy.atum.init.AtumEntities;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.init.AtumParticles;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

public class SmallBoneEntity extends Fireball {

    public SmallBoneEntity(EntityType<? extends SmallBoneEntity> entityType, Level level) {
        super(entityType, level);
    }

    public SmallBoneEntity(Level level, LivingEntity shooter, double accelX, double accelY, double accelZ) {
        super(AtumEntities.SMALL_BONE.get(), shooter, accelX, accelY, accelZ, level);
    }

    public SmallBoneEntity(Level level, double x, double y, double z, double accelX, double accelY, double accelZ) {
        super(AtumEntities.SMALL_BONE.get(), x, y, z, accelX, accelY, accelZ, level);
    }

    @Override
    protected boolean shouldBurn() {
        return false;
    }

    @Override
    @Nonnull
    protected ParticleOptions getTrailParticle() {
        return AtumParticles.EMPTY.get();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    @Nonnull
    public ItemStack getItem() {
        ItemStack stack = this.getItemRaw();
        return stack.isEmpty() ? new ItemStack(AtumItems.DUSTY_BONE.get()) : stack;
    }

    @Override
    protected void onHitEntity(@Nonnull EntityHitResult rayTraceResult) {
        super.onHitEntity(rayTraceResult);
        if (!this.level().isClientSide) {
            Entity entity = rayTraceResult.getEntity();
            Entity shootingEntity = this.getOwner();
            boolean flag = entity.hurt(this.damageSources().fireball(this, shootingEntity), 5.0F);
            if (shootingEntity instanceof LivingEntity && flag) {
                this.doEnchantDamageEffects((LivingEntity) shootingEntity, entity);
            }
        }
    }

    @Override
    protected void onHit(@Nonnull HitResult result) {
        super.onHit(result);
        if (!this.level().isClientSide) {
            this.discard();
        }
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    public boolean hurt(@Nonnull DamageSource source, float amount) {
        return false;
    }
}