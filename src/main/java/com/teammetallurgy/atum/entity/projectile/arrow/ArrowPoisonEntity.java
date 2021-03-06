package com.teammetallurgy.atum.entity.projectile.arrow;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.init.AtumEntities;
import com.teammetallurgy.atum.init.AtumParticles;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.FMLPlayMessages;

import javax.annotation.Nonnull;

public class ArrowPoisonEntity extends CustomArrow {

    public ArrowPoisonEntity(FMLPlayMessages.SpawnEntity spawnEntity, World world) {
        this(AtumEntities.POISON_ARROW, world);
    }

    public ArrowPoisonEntity(EntityType<? extends ArrowPoisonEntity> entityType, World world) {
        super(entityType, world);
    }

    public ArrowPoisonEntity(World world, LivingEntity shooter) {
        super(AtumEntities.POISON_ARROW, world, shooter);
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
        if (world.getGameTime() % 8L == 0L) {
            if (world instanceof ServerWorld) {
                ServerWorld serverWorld = (ServerWorld) world;
                serverWorld.spawnParticle(AtumParticles.SETH, getPosX(), getPosY() - 0.05D, getPosZ(), 1, 0.0D, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    @Override
    protected void onEntityHit(@Nonnull EntityRayTraceResult rayTraceResult) {
        Entity entity = rayTraceResult.getEntity();
        if (!world.isRemote && entity instanceof LivingEntity) {
            LivingEntity livingBase = (LivingEntity) entity;
            livingBase.addPotionEffect(new EffectInstance(Effects.POISON, 80, 0, false, true));
        }
        super.onEntityHit(rayTraceResult);
    }

    @Override
    public ResourceLocation getTexture() {
        return new ResourceLocation(Atum.MOD_ID, "textures/arrow/arrow_poison.png");
    }
}