package com.teammetallurgy.atum.entity.arrow;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.init.AtumParticles;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityArrowSlowness extends CustomArrow {
    float velocity;

    public EntityArrowSlowness(World world) {
        super(world);
    }

    public EntityArrowSlowness(World world, EntityLivingBase shooter, float velocity) {
        super(world, shooter);
        this.velocity = velocity;
    }

    @Override
    protected void onHit(RayTraceResult raytraceResult) {
        Entity entity = raytraceResult.entityHit;
        if (raytraceResult != null && entity instanceof EntityLivingBase && !world.isRemote && raytraceResult.typeOfHit == RayTraceResult.Type.ENTITY) {
            EntityLivingBase livingBase = (EntityLivingBase) entity;
            float chance = 0.25F;
            if (velocity == 1.0F) {
                chance = 1.0F;
            }
            if (rand.nextFloat() <= chance) {
                livingBase.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 60, 1, false, true));
                double xy = MathHelper.nextDouble(rand, 0.0001D, 0.05D);
                for (int amount = 0; amount < 25; ++amount) {
                    Atum.proxy.spawnParticle(AtumParticles.Types.GEB, entity, entity.posX, this.posY, entity.posZ, xy, 0.005D, xy);
                }
            }
        }
        super.onHit(raytraceResult);
    }

    @Override
    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/arrow/arrow_slowness.png");
    }
}