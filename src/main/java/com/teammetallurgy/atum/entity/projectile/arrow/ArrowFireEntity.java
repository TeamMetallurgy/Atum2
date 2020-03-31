package com.teammetallurgy.atum.entity.projectile.arrow;

import com.teammetallurgy.atum.init.AtumParticles;
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
import net.minecraft.world.server.ServerWorld;

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
    public void tick() {
        super.tick();

        if (this.getIsCritical()) {
            if (world instanceof ServerWorld) {
                ServerWorld serverWorld = (ServerWorld) world;
                serverWorld.spawnParticle(AtumParticles.RA_FIRE, this.getPosX() + (world.rand.nextDouble() - 0.5D) * (double) this.getWidth(), this.getPosY() + world.rand.nextDouble() * (double) this.getHeight(), this.getPosZ() + (world.rand.nextDouble() - 0.5D) * (double) this.getWidth(), 2, 0.0D, 0.0D, 0.0D, 0.01D);
            }
        }
    }

    @Override
    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/arrow/arrow_fire.png");
    }
}