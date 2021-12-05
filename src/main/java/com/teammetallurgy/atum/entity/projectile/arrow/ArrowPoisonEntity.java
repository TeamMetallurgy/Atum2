package com.teammetallurgy.atum.entity.projectile.arrow;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.init.AtumEntities;
import com.teammetallurgy.atum.init.AtumParticles;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.fml.network.FMLPlayMessages;

import javax.annotation.Nonnull;

public class ArrowPoisonEntity extends CustomArrow {

    public ArrowPoisonEntity(FMLPlayMessages.SpawnEntity spawnEntity, Level world) {
        this(AtumEntities.POISON_ARROW, world);
    }

    public ArrowPoisonEntity(EntityType<? extends ArrowPoisonEntity> entityType, Level world) {
        super(entityType, world);
    }

    public ArrowPoisonEntity(Level world, LivingEntity shooter) {
        super(AtumEntities.POISON_ARROW, world, shooter);
    }

    @Override
    public void tick() {
        super.tick();

        //Particle while arrow is in air
        if (this.isCritArrow()) {
            if (level instanceof ServerLevel) {
                ServerLevel serverWorld = (ServerLevel) level;
                serverWorld.sendParticles(AtumParticles.SETH, this.getX() + (level.random.nextDouble() - 0.5D) * (double) this.getBbWidth(), this.getY() + level.random.nextDouble() * (double) this.getBbHeight(), this.getZ() + (level.random.nextDouble() - 0.5D) * (double) this.getBbWidth(), 1, 0.0D, 0.0D, 0.0D, 0.0D);
            }
        }

        //Particle after hit
        if (level.getGameTime() % 8L == 0L) {
            if (level instanceof ServerLevel) {
                ServerLevel serverWorld = (ServerLevel) level;
                serverWorld.sendParticles(AtumParticles.SETH, getX(), getY() - 0.05D, getZ(), 1, 0.0D, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    @Override
    protected void onHitEntity(@Nonnull EntityHitResult rayTraceResult) {
        Entity entity = rayTraceResult.getEntity();
        if (!level.isClientSide && entity instanceof LivingEntity) {
            LivingEntity livingBase = (LivingEntity) entity;
            livingBase.addEffect(new MobEffectInstance(MobEffects.POISON, 80, 0, false, true));
        }
        super.onHitEntity(rayTraceResult);
    }

    @Override
    public ResourceLocation getTexture() {
        return new ResourceLocation(Atum.MOD_ID, "textures/arrow/arrow_poison.png");
    }
}