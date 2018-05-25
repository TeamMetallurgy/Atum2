package com.teammetallurgy.atum.entity.arrow;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.init.AtumParticles;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityArrowStraight extends CustomArrow {
    private float velocity;

    public EntityArrowStraight(World world) {
        super(world);
    }

    public EntityArrowStraight(World world, EntityLivingBase shooter, float velocity) {
        super(world, shooter);
        this.velocity = velocity;
        if (velocity == 1.0F) {
            this.setNoGravity(true);
        }
    }

    @Override
    public void onUpdate() {
        if (!this.inGround && motionY <= 0.05F && velocity == 1.0F) {
            this.setDead();
        }
        if (shootingEntity instanceof EntityPlayer && !inGround && velocity == 1.0F && this.isEntityAlive()) {
            Atum.proxy.spawnParticle(AtumParticles.Types.HORUS, this, posX, posY - 0.05D, posZ, 0.0D, 0.0D, 0.0D);
        }
        super.onUpdate();
    }

    @Override
    protected void onHit(RayTraceResult raytraceResult) {
        super.onHit(raytraceResult);
    }

    @Override
    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/arrow/arrow_straight.png");
    }
}