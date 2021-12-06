package com.teammetallurgy.atum.entity.projectile.arrow;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.init.AtumEntities;
import com.teammetallurgy.atum.init.AtumParticles;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.PlayMessages;

public class ArrowRainEntity extends CustomArrow {
    private float velocity;
    private boolean isSmallArrow = false;

    public ArrowRainEntity(PlayMessages.SpawnEntity spawnEntity, Level world) {
        this(AtumEntities.RAIN_ARROW, world);
    }

    public ArrowRainEntity(EntityType<? extends ArrowRainEntity> entityType, Level world) {
        super(entityType, world);
    }

    public ArrowRainEntity(Level world, LivingEntity shooter, float velocity) {
        super(AtumEntities.RAIN_ARROW, world, shooter);
        this.velocity = velocity;
    }

    public ArrowRainEntity(Level world, double x, double y, double z, boolean canPickup) { //Small arrow constructor
        super(AtumEntities.RAIN_ARROW, world, x, y, z);
        this.isSmallArrow = true;
        this.pickup = canPickup ? Pickup.ALLOWED : Arrow.Pickup.DISALLOWED;
    }

    @Override
    public void tick() {
        if (this.level.getGameTime() % (this.inGround ? 55L : 3L) == 0L) {
            if (this.level instanceof ServerLevel) {
                ServerLevel serverWorld = (ServerLevel) this.level;
                serverWorld.sendParticles(AtumParticles.TEFNUT_DROP, getX(), getY() - 0.05D, getZ(), 1, 0.0D, 0.0D, 0.0D, 0.0D);
            }
        }
        if (this.velocity == 1.0F && this.getOwner() instanceof LivingEntity) {
            if (this.tickCount == 12) {
                this.remove();
                if (!this.isSmallArrow) {
                    ArrowRainEntity arrow1 = new ArrowRainEntity(this.level, this.getX() + 0.5D, this.getY(), this.getZ(), false);

                    ArrowRainEntity arrow2 = new ArrowRainEntity(this.level, this.getX(), this.getY(), this.getZ() + 0.5D, false);

                    ArrowRainEntity arrow3 = new ArrowRainEntity(this.level, this.getX() - 0.5D, this.getY(), this.getZ(), false);

                    ArrowRainEntity arrow4 = new ArrowRainEntity(this.level, this.getX(), this.getY(), this.getZ() - 0.5D, false);

                    ArrowRainEntity arrow5 = new ArrowRainEntity(this.level, this.getX(), this.getY(), this.getZ(), true);

                    this.level.addFreshEntity(arrow1);
                    this.level.addFreshEntity(arrow2);
                    this.level.addFreshEntity(arrow3);
                    this.level.addFreshEntity(arrow4);
                    this.level.addFreshEntity(arrow5);
                }
            }
        }
        super.tick();
    }

    @Override
    protected void tickDespawn() {
        if (this.isSmallArrow && this.pickup == Pickup.DISALLOWED && this.inGroundTime >= 150) { //Remove small arrows quicker, to prevent lag
            this.remove();
        }
        super.tickDespawn();
    }

    @Override
    public ResourceLocation getTexture() {
        return new ResourceLocation(Atum.MOD_ID, "textures/arrow/arrow_rain.png");
    }
}