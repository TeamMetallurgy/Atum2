package com.teammetallurgy.atum.entity.projectile.arrow;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.init.AtumParticles;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;

public class ArrowFireEntity extends CustomArrow {

    public ArrowFireEntity(EntityType<? extends ArrowFireEntity> entityType, World world) {
        super(entityType, world);
    }

    public ArrowFireEntity(World world, LivingEntity shooter) {
        super(world, shooter);
    }

    @Override
    protected void onEntityHit(@Nonnull EntityRayTraceResult rayTraceResult) {
        super.onEntityHit(rayTraceResult);
        Entity hitEnity = rayTraceResult.getEntity();
        if (hitEnity instanceof LivingEntity) {
            if (!hitEnity.isImmuneToFire()) {
                if (hitEnity.getFireTimer() > 0) {
                    this.setDamage(this.getDamage() * 1.5D);
                    this.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 1.0F, 1.0F);
                }
                hitEnity.setFire(5);
            }
        }
    }

    @Override
    protected void func_230299_a_(@Nonnull BlockRayTraceResult rayTraceResult) {
        super.func_230299_a_(rayTraceResult);
        Entity shooter = this.func_234616_v_();
        if (shooter instanceof PlayerEntity) {
            BlockPos pos = rayTraceResult.getPos().offset(rayTraceResult.getFace());
            PlayerEntity player = (PlayerEntity) shooter;
            if (player.canPlayerEdit(pos, rayTraceResult.getFace(), player.getHeldItem(player.getActiveHand())) && this.world.getBlockState(pos).getMaterial() == Material.AIR) {
                this.world.setBlockState(pos, Blocks.FIRE.getDefaultState());
            }
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (this.getIsCritical()) {
            if (this.world instanceof ServerWorld) {
                ServerWorld serverWorld = (ServerWorld) this.world;
                serverWorld.spawnParticle(AtumParticles.RA_FIRE, this.getPosX() + (this.world.rand.nextDouble() - 0.5D) * (double) this.getWidth(), this.getPosY() + this.world.rand.nextDouble() * (double) this.getHeight(), this.getPosZ() + (this.world.rand.nextDouble() - 0.5D) * (double) this.getWidth(), 2, 0.0D, 0.0D, 0.0D, 0.01D);
            }
        }
    }

    @Override
    public ResourceLocation getTexture() {
        return new ResourceLocation(Atum.MOD_ID, "textures/arrow/arrow_fire.png");
    }
}