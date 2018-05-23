package com.teammetallurgy.atum.entity.arrow;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.init.AtumParticles;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityArrowPoison extends CustomArrow {

    public EntityArrowPoison(World world) {
        super(world);
    }

    public EntityArrowPoison(World world, EntityLivingBase shooter) {
        super(world, shooter);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (shootingEntity instanceof EntityPlayer && world.getTotalWorldTime() % 60L == 0L) {
            EntityPlayer player = (EntityPlayer) shootingEntity;
            Atum.proxy.spawnParticle(AtumParticles.Types.SETH, player, posX, posY - 0.05D, posZ, 0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    protected void onHit(RayTraceResult raytraceResult) {
        Entity entity = raytraceResult.entityHit;
        if (raytraceResult != null && entity instanceof EntityLivingBase && !world.isRemote && raytraceResult.typeOfHit == RayTraceResult.Type.ENTITY) {
            EntityLivingBase livingBase = (EntityLivingBase) entity;
            livingBase.addPotionEffect(new PotionEffect(MobEffects.POISON, 80, 0, false, true));
        }
        super.onHit(raytraceResult);
    }

    @Override
    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/arrow/arrow_poison.png");
    }
}