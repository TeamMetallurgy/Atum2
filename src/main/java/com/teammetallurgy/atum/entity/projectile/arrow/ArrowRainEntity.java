package com.teammetallurgy.atum.entity.projectile.arrow;

import com.teammetallurgy.atum.init.AtumParticles;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class ArrowRainEntity extends CustomArrow {
    private float velocity;
    private boolean isSmallArrow = false;

    public ArrowRainEntity(EntityType<? extends ArrowRainEntity> entityType, World world) {
        super(entityType, world);
    }

    public ArrowRainEntity(World world, LivingEntity shooter, float velocity) {
        super(world, shooter);
        this.velocity = velocity;
    }

    public ArrowRainEntity(World world, double x, double y, double z) {
        super(world, x, y, z);
        this.isSmallArrow = true;
        this.pickupStatus = ArrowEntity.PickupStatus.DISALLOWED;
    }

    @Override
    public void tick() {
        if (world.getGameTime() % (this.inGround ? 55L : 3L) == 0L) {
            if (world instanceof ServerWorld) {
                ServerWorld serverWorld = (ServerWorld) world;
                serverWorld.spawnParticle(AtumParticles.TEFNUT_DROP, getPosX(), getPosY() - 0.05D, getPosZ(), 1, 0.0D, 0.0D, 0.0D, 0.0D);
            }
        }
        if (velocity == 1.0F && this.getShooter() instanceof LivingEntity) {
            if (this.ticksInAir == 12) {
                this.remove();
                if (!isSmallArrow) {
                    ArrowRainEntity arrow1 = new ArrowRainEntity(world, this.getPosX() + 0.5D, this.getPosY(), this.getPosZ());

                    ArrowRainEntity arrow2 = new ArrowRainEntity(world, this.getPosX(), this.getPosY(), this.getPosZ() + 0.5D);

                    ArrowRainEntity arrow3 = new ArrowRainEntity(world, this.getPosX() - 0.5D, this.getPosY(), this.getPosZ());

                    ArrowRainEntity arrow4 = new ArrowRainEntity(world, this.getPosX(), this.getPosY(), this.getPosZ() - 0.5D);

                    ArrowRainEntity arrow5 = new ArrowRainEntity(world, this.getPosX(), this.getPosY(), this.getPosZ());

                    world.addEntity(arrow1);
                    world.addEntity(arrow2);
                    world.addEntity(arrow3);
                    world.addEntity(arrow4);
                    world.addEntity(arrow5);
                }
            }
        }
        super.tick();
    }

    @Override
    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/arrow/arrow_rain.png");
    }
}