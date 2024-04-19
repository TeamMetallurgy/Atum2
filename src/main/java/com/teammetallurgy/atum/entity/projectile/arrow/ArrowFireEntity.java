package com.teammetallurgy.atum.entity.projectile.arrow;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.init.AtumEntities;
import com.teammetallurgy.atum.init.AtumParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.neoforged.neoforge.network.PlayMessages;

import javax.annotation.Nonnull;

public class ArrowFireEntity extends CustomArrow {

    public ArrowFireEntity(PlayMessages.SpawnEntity spawnEntity, Level level) {
        this(AtumEntities.FIRE_ARROW.get(), level);
    }

    public ArrowFireEntity(EntityType<? extends ArrowFireEntity> entityType, Level level) {
        super(entityType, level);
    }

    public ArrowFireEntity(Level level, LivingEntity shooter) {
        super(AtumEntities.FIRE_ARROW.get(), level, shooter);
    }

    @Override
    protected void onHitEntity(@Nonnull EntityHitResult rayTraceResult) {
        super.onHitEntity(rayTraceResult);
        Entity hitEntity = rayTraceResult.getEntity();
        if (hitEntity instanceof LivingEntity) {
            if (!hitEntity.fireImmune()) {
                if (hitEntity.getRemainingFireTicks() > 0) {  //Extra damage, if target is already on fire
                    this.setBaseDamage(this.getBaseDamage() * 1.5D);
                    this.playSound(SoundEvents.FIRECHARGE_USE, 1.0F, 1.0F);
                }
                hitEntity.setSecondsOnFire(5);
            }
        }
    }

    @Override
    protected void onHitBlock(@Nonnull BlockHitResult rayTraceResult) {
        super.onHitBlock(rayTraceResult);
        Entity shooter = this.getOwner();
        if (shooter instanceof Player) {
            BlockPos pos = rayTraceResult.getBlockPos().relative(rayTraceResult.getDirection());
            Player player = (Player) shooter;
            if (player.mayUseItemAt(pos, rayTraceResult.getDirection(), player.getItemInHand(player.getUsedItemHand())) && this.level.getBlockState(pos).getMaterial() == Material.AIR) {
                this.level.setBlockAndUpdate(pos, Blocks.FIRE.defaultBlockState());
            }
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (this.isCritArrow()) {
            if (this.level instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(AtumParticles.RA_FIRE.get(), this.getX() + (this.level.random.nextDouble() - 0.5D) * (double) this.getBbWidth(), this.getY() + this.level.random.nextDouble() * (double) this.getBbHeight(), this.getZ() + (this.level.random.nextDouble() - 0.5D) * (double) this.getBbWidth(), 2, 0.0D, 0.0D, 0.0D, 0.01D);
            }
        }
    }

    @Override
    public ResourceLocation getTexture() {
        return new ResourceLocation(Atum.MOD_ID, "textures/arrow/arrow_fire.png");
    }
}