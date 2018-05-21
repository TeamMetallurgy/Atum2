package com.teammetallurgy.atum.entity.arrow;

import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityArrowFire extends CustomArrow {

    public EntityArrowFire(World world) {
        super(world);
    }

    public EntityArrowFire(World world, EntityLivingBase shooter) {
        super(world, shooter);
    }

    @Override
    protected void onHit(RayTraceResult rayTraceResult) {
        super.onHit(rayTraceResult);
        if (rayTraceResult != null && !world.isRemote) {
            Entity hitEnity = rayTraceResult.entityHit;
            if (rayTraceResult.typeOfHit == RayTraceResult.Type.ENTITY && hitEnity instanceof EntityLivingBase) {
                hitEnity.setFire(5);
            } else if (rayTraceResult.typeOfHit == RayTraceResult.Type.BLOCK && shootingEntity instanceof EntityPlayer) {
                BlockPos pos = rayTraceResult.getBlockPos().offset(rayTraceResult.sideHit);
                EntityPlayer player = (EntityPlayer) shootingEntity;
                if (player.canPlayerEdit(pos, rayTraceResult.sideHit, player.getHeldItem(player.getActiveHand())) && world.getBlockState(pos).getMaterial() == Material.AIR) {
                    world.setBlockState(pos, Blocks.FIRE.getDefaultState());
                }
            }
        }
    }

    @Override
    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/arrow/arrow_fire.png");
    }
}