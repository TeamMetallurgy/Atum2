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

public class ArrowSlownessEntity extends CustomArrow {
    private float velocity;

    public ArrowSlownessEntity(FMLPlayMessages.SpawnEntity spawnEntity, World world) {
        this(AtumEntities.SLOWNESS_ARROW, world);
    }

    public ArrowSlownessEntity(EntityType<? extends ArrowSlownessEntity> entityType, World world) {
        super(entityType, world);
    }

    public ArrowSlownessEntity(World world, LivingEntity shooter, float velocity) {
        super(AtumEntities.SLOWNESS_ARROW, world, shooter);
        this.velocity = velocity;
    }

    @Override
    protected void onEntityHit(@Nonnull EntityRayTraceResult rayTraceResult) {
        Entity entity = rayTraceResult.getEntity();
        if (!world.isRemote && entity instanceof LivingEntity) {
            LivingEntity livingBase = (LivingEntity) entity;
            float chance = 0.25F;
            if (velocity == 1.0F) {
                chance = 1.0F;
            }
            if (rand.nextFloat() <= chance) {
                livingBase.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 60, 1, false, true));
                if (world instanceof ServerWorld) {
                    ServerWorld serverWorld = (ServerWorld) world;
                    serverWorld.spawnParticle(AtumParticles.GEB, entity.getPosX(), this.getPosY(), entity.getPosZ(), 15, 0.0D, -0.06D, 0.0D, 0.025D);
                }
            }
        }
        super.onEntityHit(rayTraceResult);
    }

    @Override
    public ResourceLocation getTexture() {
        return new ResourceLocation(Atum.MOD_ID, "textures/arrow/arrow_slowness.png");
    }
}