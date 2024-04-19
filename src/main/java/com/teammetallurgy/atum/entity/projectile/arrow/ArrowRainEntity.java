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

public class ArrowRainEntity extends CustomArrow {
    private float velocity;
    private boolean isSmallArrow = false;

    public ArrowRainEntity(EntityType<? extends ArrowRainEntity> entityType, Level level) {
        super(entityType, level);
    }

    public ArrowRainEntity(Level level, LivingEntity shooter, float velocity) {
        super(AtumEntities.RAIN_ARROW.get(), level, shooter);
        this.velocity = velocity;
    }

    public ArrowRainEntity(Level level, double x, double y, double z, boolean canPickup) { //Small arrow constructor
        super(AtumEntities.RAIN_ARROW.get(), level, x, y, z);
        this.isSmallArrow = true;
        this.pickup = canPickup ? Pickup.ALLOWED : Arrow.Pickup.DISALLOWED;
    }

    @Override
    public void tick() {
        if (this.level().getGameTime() % (this.inGround ? 55L : 3L) == 0L) {
            if (this.level() instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(AtumParticles.TEFNUT_DROP.get(), getX(), getY() - 0.05D, getZ(), 1, 0.0D, 0.0D, 0.0D, 0.0D);
            }
        }
        if (this.velocity == 1.0F && this.getOwner() instanceof LivingEntity) {
            if (this.tickCount == 12) {
                this.discard();
                if (!this.isSmallArrow) {
                    ArrowRainEntity arrow1 = new ArrowRainEntity(this.level(), this.getX() + 0.5D, this.getY(), this.getZ(), false);

                    ArrowRainEntity arrow2 = new ArrowRainEntity(this.level(), this.getX(), this.getY(), this.getZ() + 0.5D, false);

                    ArrowRainEntity arrow3 = new ArrowRainEntity(this.level(), this.getX() - 0.5D, this.getY(), this.getZ(), false);

                    ArrowRainEntity arrow4 = new ArrowRainEntity(this.level(), this.getX(), this.getY(), this.getZ() - 0.5D, false);

                    ArrowRainEntity arrow5 = new ArrowRainEntity(this.level(), this.getX(), this.getY(), this.getZ(), true);

                    this.level().addFreshEntity(arrow1);
                    this.level().addFreshEntity(arrow2);
                    this.level().addFreshEntity(arrow3);
                    this.level().addFreshEntity(arrow4);
                    this.level().addFreshEntity(arrow5);
                }
            }
        }
        super.tick();
    }

    @Override
    protected void tickDespawn() {
        if (this.isSmallArrow && this.pickup == Pickup.DISALLOWED && this.inGroundTime >= 150) { //Remove small arrows quicker, to prevent lag
            this.discard();
        }
        super.tickDespawn();
    }

    @Override
    public ResourceLocation getTexture() {
        return new ResourceLocation(Atum.MOD_ID, "textures/arrow/arrow_rain.png");
    }
}