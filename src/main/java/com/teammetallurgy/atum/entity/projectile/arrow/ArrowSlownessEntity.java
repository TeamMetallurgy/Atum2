package com.teammetallurgy.atum.entity.projectile.arrow;

import com.teammetallurgy.atum.init.AtumParticles;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class ArrowSlownessEntity extends CustomArrow {
    private float velocity;

    public ArrowSlownessEntity(EntityType<? extends ArrowSlownessEntity> entityType, World world) {
        super(entityType, world);
    }

    public ArrowSlownessEntity(World world, LivingEntity shooter, float velocity) {
        super(world, shooter);
        this.velocity = velocity;
    }

    @Override
    protected void onHit(RayTraceResult raytraceResult) {
        if (raytraceResult.getType() == RayTraceResult.Type.ENTITY) {
            Entity entity = ((EntityRayTraceResult) raytraceResult).getEntity();
            if (!world.isRemote && entity instanceof LivingEntity) {
                LivingEntity livingBase = (LivingEntity) entity;
                float chance = 0.25F;
                if (velocity == 1.0F) {
                    chance = 1.0F;
                }
                if (rand.nextFloat() <= chance) {
                    livingBase.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 60, 1, false, true));
                    for (int amount = 0; amount < 25; ++amount) {
                        world.addParticle(AtumParticles.GEB, entity.getPosX() + (world.rand.nextDouble() - 0.5D) * (double) entity.getWidth(), this.getPosY(), entity.getPosZ() + (world.rand.nextDouble() - 0.5D) * (double) entity.getWidth(), 0.0D, -0.06D, 0.0D);
                    }
                }
            }
        }
        super.onHit(raytraceResult);
    }

    @Override
    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/arrow/arrow_slowness.png");
    }
}