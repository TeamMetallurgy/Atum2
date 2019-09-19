package com.teammetallurgy.atum.entity.projectile.arrow;

import com.teammetallurgy.atum.init.AtumParticles;
import com.teammetallurgy.atum.utils.Constants;
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

public class EntityArrowPoison extends CustomArrow {

    public EntityArrowPoison(EntityType<? extends EntityArrowPoison> entityType, World world) {
        super(entityType, world);
    }

    public EntityArrowPoison(World world, LivingEntity shooter) {
        super(world, shooter);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getShooter() instanceof PlayerEntity && world.getGameTime() % 4L == 0L) {
            world.addParticle(AtumParticles.SETH, posX, posY - 0.05D, posZ, 0.0D, 0.0D, 0.0D);
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
        return new ResourceLocation(Constants.MOD_ID, "textures/arrow/arrow_poison.png");
    }
}