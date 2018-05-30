package com.teammetallurgy.atum.entity.projectile.arrow;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.init.AtumParticles;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class EntityArrowStraight extends CustomArrow {
    private float velocity;

    public EntityArrowStraight(World world) {
        super(world);
    }

    public EntityArrowStraight(World world, EntityLivingBase shooter, float velocity) {
        super(world, shooter);
        this.velocity = velocity;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.velocity == 1.0F) {
            this.motionY += 0.05;
            if (!this.inGround && ticksExisted > 300) {
                this.setDead();
            }
        }
        if (shootingEntity instanceof EntityLivingBase && !inGround && velocity == 1.0F && this.isEntityAlive()) {
            Atum.proxy.spawnParticle(AtumParticles.Types.HORUS, this, posX, posY - 0.05D, posZ, 0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/arrow/arrow_straight.png");
    }
}