package com.teammetallurgy.atum.entity.projectile.arrow;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.init.AtumParticles;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class EntityArrowRain extends CustomArrow {
    private float velocity;
    private boolean isSmallArrow = false;

    public EntityArrowRain(World world) {
        super(world);
    }

    public EntityArrowRain(World world, LivingEntity shooter, float velocity) {
        super(world, shooter);
        this.velocity = velocity;
    }

    public EntityArrowRain(World world, double x, double y, double z) {
        super(world, x, y, z);
        this.isSmallArrow = true;
        this.pickupStatus = EntityArrow.PickupStatus.DISALLOWED;
    }

    @Override
    public void onUpdate() {
        if (world.getTotalWorldTime() % (this.inGround ? 55L : 3L) == 0L) {
            Atum.proxy.spawnParticle(AtumParticles.Types.TEFNUT_DROP, this, posX, posY - 0.05D, posZ, 0.0D, 0.0D, 0.0D);
        }
        if (velocity == 1.0F && shootingEntity instanceof LivingEntity) {
            if (this.ticksInAir == 12) {
                this.setDead();
                if (!isSmallArrow) {
                    EntityArrowRain arrow1 = new EntityArrowRain(world, this.posX + 0.5D, this.posY, this.posZ);

                    EntityArrowRain arrow2 = new EntityArrowRain(world, this.posX, this.posY, this.posZ + 0.5D);

                    EntityArrowRain arrow3 = new EntityArrowRain(world, this.posX - 0.5D, this.posY, this.posZ);

                    EntityArrowRain arrow4 = new EntityArrowRain(world, this.posX, this.posY, this.posZ - 0.5D);

                    EntityArrowRain arrow5 = new EntityArrowRain(world, this.posX, this.posY, this.posZ);

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