package com.teammetallurgy.atum.entity.projectile.arrow;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.init.AtumParticles;
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

    public ArrowRainEntity(World world, double x, double y, double z, boolean canPickup) { //Small arrow constructor
        super(world, x, y, z);
        this.isSmallArrow = true;
        this.pickupStatus = canPickup ? PickupStatus.ALLOWED : ArrowEntity.PickupStatus.DISALLOWED;
    }

    @Override
    public void tick() {
        if (this.world.getGameTime() % (this.inGround ? 55L : 3L) == 0L) {
            if (this.world instanceof ServerWorld) {
                ServerWorld serverWorld = (ServerWorld) this.world;
                serverWorld.spawnParticle(AtumParticles.TEFNUT_DROP, getPosX(), getPosY() - 0.05D, getPosZ(), 1, 0.0D, 0.0D, 0.0D, 0.0D);
            }
        }
        if (this.velocity == 1.0F && this.func_234616_v_() instanceof LivingEntity) {
            if (this.ticksExisted == 12) {
                this.remove();
                if (!this.isSmallArrow) {
                    ArrowRainEntity arrow1 = new ArrowRainEntity(this.world, this.getPosX() + 0.5D, this.getPosY(), this.getPosZ(), false);

                    ArrowRainEntity arrow2 = new ArrowRainEntity(this.world, this.getPosX(), this.getPosY(), this.getPosZ() + 0.5D, false);

                    ArrowRainEntity arrow3 = new ArrowRainEntity(this.world, this.getPosX() - 0.5D, this.getPosY(), this.getPosZ(), false);

                    ArrowRainEntity arrow4 = new ArrowRainEntity(this.world, this.getPosX(), this.getPosY(), this.getPosZ() - 0.5D, false);

                    ArrowRainEntity arrow5 = new ArrowRainEntity(this.world, this.getPosX(), this.getPosY(), this.getPosZ(), true);

                    this.world.addEntity(arrow1);
                    this.world.addEntity(arrow2);
                    this.world.addEntity(arrow3);
                    this.world.addEntity(arrow4);
                    this.world.addEntity(arrow5);
                }
            }
        }
        super.tick();
    }

    @Override
    protected void func_225516_i_() {
        if (this.isSmallArrow && this.pickupStatus == PickupStatus.DISALLOWED && this.timeInGround >= 150) { //Remove small arrows quicker, to prevent lag
            this.remove();
        }
        super.func_225516_i_();
    }

    @Override
    public ResourceLocation getTexture() {
        return new ResourceLocation(Atum.MOD_ID, "textures/arrow/arrow_rain.png");
    }
}