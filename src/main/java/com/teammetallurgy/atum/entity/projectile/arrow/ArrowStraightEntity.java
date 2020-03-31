package com.teammetallurgy.atum.entity.projectile.arrow;

import com.teammetallurgy.atum.init.AtumParticles;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class ArrowStraightEntity extends CustomArrow {
    private float velocity;

    public ArrowStraightEntity(EntityType<? extends CustomArrow> entityType, World world) {
        super(entityType, world);
    }

    public ArrowStraightEntity(World world, LivingEntity shooter, float velocity) {
        super(world, shooter);
        this.velocity = velocity;
    }

    @Override
    public void tick() {
        if (!world.isRemote) {
            super.tick();
        }

        if (this.velocity == 1.0F) {
            this.setMotion(this.getMotion().add(0.0D, 0.05D, 0.0D));
            if (!this.inGround && ticksExisted > 300) {
                this.remove();
            }

            if (this.getShooter() instanceof LivingEntity && !inGround && velocity == 1.0F && this.isAlive()) {
                if (world instanceof ServerWorld) {
                    ServerWorld serverWorld = (ServerWorld) world;
                    serverWorld.spawnParticle(AtumParticles.HORUS, getPosX(), getPosY() - 0.05D, getPosZ(), 1, 0.0D, 0.0D, 0.0D, 0.0D);
                }
            }
        }
    }

    @Override
    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/arrow/arrow_straight.png");
    }
}