package com.teammetallurgy.atum.entity.projectile.arrow;

import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class ArrowFireEntity extends CustomArrow {

    public ArrowFireEntity(EntityType<? extends ArrowFireEntity> entityType, World world) {
        super(entityType, world);
    }

    public ArrowFireEntity(World world, LivingEntity shooter) {
        super(world, shooter);
    }

    @Override
    protected void onHit(RayTraceResult rayTrace) {
        super.onHit(rayTrace);
        if (!world.isRemote) {
            if (rayTrace.getType() == RayTraceResult.Type.ENTITY) {
                EntityRayTraceResult rayTraceEntity = (EntityRayTraceResult) rayTrace;
                Entity hitEnity = rayTraceEntity.getEntity();
                if (hitEnity instanceof LivingEntity) {
                    hitEnity.setFire(5);
                }
            } else if (rayTrace.getType() == RayTraceResult.Type.BLOCK && this.getShooter() instanceof PlayerEntity) {
                BlockRayTraceResult rayTraceBlock = (BlockRayTraceResult) rayTrace;
                BlockPos pos = rayTraceBlock.getPos().offset(rayTraceBlock.getFace());
                PlayerEntity player = (PlayerEntity) this.getShooter();
                if (player.canPlayerEdit(pos, rayTraceBlock.getFace(), player.getHeldItem(player.getActiveHand())) && world.getBlockState(pos).getMaterial() == Material.AIR) {
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