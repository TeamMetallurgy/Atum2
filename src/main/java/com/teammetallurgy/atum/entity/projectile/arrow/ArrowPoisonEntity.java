package com.teammetallurgy.atum.entity.projectile.arrow;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.init.AtumParticles;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class ArrowPoisonEntity extends CustomArrow {

    public ArrowPoisonEntity(EntityType<? extends ArrowPoisonEntity> entityType, World world) {
        super(entityType, world);
    }

    public ArrowPoisonEntity(World world, LivingEntity shooter) {
        super(world, shooter);
    }

    @Override
    public void tick() {
        super.tick();

        //Particle while arrow is in air
        if (this.getIsCritical()) {
            if (world instanceof ServerWorld) {
                ServerWorld serverWorld = (ServerWorld) world;
                serverWorld.spawnParticle(AtumParticles.SETH, this.getPosX() + (world.rand.nextDouble() - 0.5D) * (double) this.getWidth(), this.getPosY() + world.rand.nextDouble() * (double) this.getHeight(), this.getPosZ() + (world.rand.nextDouble() - 0.5D) * (double) this.getWidth(), 1, 0.0D, 0.0D, 0.0D, 0.0D);
            }
        }

        //Particle after hit
        if (this.getShooter() instanceof PlayerEntity && world.getGameTime() % 8L == 0L) {
            if (world instanceof ServerWorld) {
                ServerWorld serverWorld = (ServerWorld) world;
                serverWorld.spawnParticle(AtumParticles.SETH, getPosX(), getPosY() - 0.05D, getPosZ(), 1, 0.0D, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    @Override
    protected void onHit(RayTraceResult raytraceResult) {
        if (raytraceResult.getType() == RayTraceResult.Type.ENTITY) {
            Entity entity = ((EntityRayTraceResult) raytraceResult).getEntity();
            if (!world.isRemote && entity instanceof LivingEntity) {
                LivingEntity livingBase = (LivingEntity) entity;
                livingBase.addPotionEffect(new EffectInstance(Effects.POISON, 80, 0, false, true));
            }
        }
        super.onHit(raytraceResult);
    }

    @Override
    public ResourceLocation getTexture() {
        return new ResourceLocation(Atum.MOD_ID, "textures/arrow/arrow_poison.png");
    }
}