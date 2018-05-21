package com.teammetallurgy.atum.entity.arrow;

import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityArrowExplosive extends CustomArrow {

    public EntityArrowExplosive(World world) {
        super(world);
    }

    public EntityArrowExplosive(World world, EntityLivingBase shooter) {
        super(world, shooter);
    }

    @Override
    protected void onHit(RayTraceResult rayTraceResult) {
        super.onHit(rayTraceResult);

        if(!world.isRemote) {
            world.createExplosion(this, posX, posY, posZ, 2.0F, true);
        }
        this.setDead();
    }

    @Override
    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/arrow/arrow_exploding.png");
    }
}