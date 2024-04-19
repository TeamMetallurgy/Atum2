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

public class ArrowPoisonEntity extends CustomArrow {

    public ArrowPoisonEntity(PlayMessages.SpawnEntity spawnEntity, Level level) {
        this(AtumEntities.POISON_ARROW.get(), level);
    }

    public ArrowPoisonEntity(EntityType<? extends ArrowPoisonEntity> entityType, Level level) {
        super(entityType, level);
    }

    public ArrowPoisonEntity(Level level, LivingEntity shooter) {
        super(AtumEntities.POISON_ARROW.get(), level, shooter);
    }

    @Override
    public void tick() {
        super.tick();

        //Particle while arrow is in air
        if (this.isCritArrow()) {
            if (level instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(AtumParticles.SETH.get(), this.getX() + (level.random.nextDouble() - 0.5D) * (double) this.getBbWidth(), this.getY() + level.random.nextDouble() * (double) this.getBbHeight(), this.getZ() + (level.random.nextDouble() - 0.5D) * (double) this.getBbWidth(), 1, 0.0D, 0.0D, 0.0D, 0.0D);
            }
        }

        //Particle after hit
        if (level.getGameTime() % 8L == 0L) {
            if (level instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(AtumParticles.SETH.get(), getX(), getY() - 0.05D, getZ(), 1, 0.0D, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    @Override
    protected void onHitEntity(@Nonnull EntityHitResult rayTraceResult) {
        super.onHitEntity(rayTraceResult);

        Entity hitEntity = rayTraceResult.getEntity();
        if (!level.isClientSide && hitEntity instanceof LivingEntity livingBase) {
            MobEffectInstance poison = new MobEffectInstance(MobEffects.POISON, 110, 0, false, true);

            if (livingBase.getEffect(MobEffects.POISON) != null) {  //Extra damage, if target is already poisoned
                this.setBaseDamage(this.getBaseDamage() * 1.5D);
            }

            livingBase.addEffect(poison);
        }
    }

    @Override
    public ResourceLocation getTexture() {
        return new ResourceLocation(Atum.MOD_ID, "textures/arrow/arrow_poison.png");
    }
}