package com.teammetallurgy.atum.entity.projectile.arrow;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.init.AtumParticles;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class EntityArrowRain extends CustomArrow {
    private float velocity;
    private boolean isSmallArrow = false;

    public EntityArrowRain(World world) {
        super(world);
    }

    public EntityArrowRain(World world, EntityLivingBase shooter, float velocity) {
        super(world, shooter);
        this.velocity = velocity;
    }

    public EntityArrowRain(World world, double x, double y, double z) {
        super(world, x, y, z);
        this.isSmallArrow = true;
        this.pickupStatus = EntityArrow.PickupStatus.DISALLOWED;
    }

    @Override
    public void onUpdate() { //TODO fix spread
        if (world.getTotalWorldTime() % (this.inGround ? 55L : 3L) == 0L) {
            Atum.proxy.spawnParticle(AtumParticles.Types.TEFNUT, this, posX, posY - 0.05D, posZ, 0.0D, 0.0D, 0.0D);
        }
        if (velocity == 1.0F && shootingEntity instanceof EntityLivingBase) {
            if (this.ticksInAir == 20) {
                this.setDead();
                if (!isSmallArrow) {
                    EntityArrowRain arrow1 = new EntityArrowRain(world, this.posX, this.posY, this.posZ);
                    arrow1.shoot(this, this.rotationPitch, this.rotationYaw, 0.0F, velocity, 1.0F);
                    arrow1.motionX -= 0.8D;
                    arrow1.motionZ -= 0.8D;

                    EntityArrowRain arrow2 = new EntityArrowRain(world, this.posX, this.posY, this.posZ);
                    arrow2.shoot(this, this.rotationPitch, this.rotationYaw, 0.0F, velocity, 1.0F);
                    arrow2.motionX += 0.4D;
                    arrow2.motionZ += 0.4D;

                    EntityArrowRain arrow3 = new EntityArrowRain(world, this.posX, this.posY, this.posZ);
                    arrow3.shoot(this, this.rotationPitch, this.rotationYaw, 0.0F, velocity, 1.0F);
                    arrow3.motionX += 0.1D;
                    arrow3.motionZ += 0.1D;

                    EntityArrowRain arrow4 = new EntityArrowRain(world, this.posX, this.posY, this.posZ);
                    arrow4.shoot(this, this.rotationPitch, this.rotationYaw, 0.0F, velocity, 1.0F);
                    arrow4.motionX += 0.4D;
                    arrow4.motionZ += 0.4D;

                    EntityArrowRain arrow5 = new EntityArrowRain(world, this.posX, this.posY, this.posZ);
                    arrow5.shoot(this, this.rotationPitch, this.rotationYaw, 0.0F, velocity, 1.0F);
                    arrow5.motionX += 0.8D;
                    arrow5.motionZ += 0.8D;

                    world.spawnEntity(arrow1);
                    world.spawnEntity(arrow2);
                    world.spawnEntity(arrow3);
                    world.spawnEntity(arrow4);
                    world.spawnEntity(arrow5);
                }
            }
        }
        super.onUpdate();
    }

    @Override
    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/arrow/arrow_rain.png");
    }
}