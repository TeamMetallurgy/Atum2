package com.teammetallurgy.atum.entity.projectile.arrow;

import com.teammetallurgy.atum.init.AtumEntities;
import com.teammetallurgy.atum.init.AtumItems;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TefnutsCallEntity extends AbstractArrow {
    protected ItemStack thrownStack = new ItemStack(AtumItems.TEFNUTS_CALL.get());
    private boolean dealtDamage;
    public int returningTicks;

    public TefnutsCallEntity(PlayMessages.SpawnEntity spawnPacket, Level level) {
        this(AtumEntities.TEFNUTS_CALL.get(), level);
    }

    public TefnutsCallEntity(EntityType<? extends TefnutsCallEntity> entityType, Level level) {
        super(entityType, level);
    }

    public TefnutsCallEntity(Level level, LivingEntity shooter, @Nonnull ItemStack stack) {
        super(AtumEntities.TEFNUTS_CALL.get(), shooter, level);
        this.thrownStack = stack.copy();
    }

    @OnlyIn(Dist.CLIENT)
    public TefnutsCallEntity(Level level, double x, double y, double z) {
        super(AtumEntities.TEFNUTS_CALL.get(), x, y, z, level);
    }

    @Override
    @Nonnull
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public boolean ignoreExplosion() {
        return true;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean displayFireAnimation() {
        return false;
    }

    @Override
    @Nonnull
    protected ItemStack getPickupItem() {
        return this.thrownStack.copy();
    }

    @Override
    @Nullable
    protected EntityHitResult findHitEntity(@Nonnull Vec3 startVec, @Nonnull Vec3 endVec) {
        return this.dealtDamage ? null : super.findHitEntity(startVec, endVec);
    }

    private boolean shouldReturnToThrower() {
        Entity entity = this.getOwner();
        if (entity != null && entity.isAlive()) {
            return !(entity instanceof ServerPlayer) || !entity.isSpectator();
        } else {
            return false;
        }
    }

    @Override
    public void tick() {
        if (this.inGroundTime > 4) {
            this.dealtDamage = true;
        }

        Entity entity = this.getOwner();
        if ((this.dealtDamage || this.isNoPhysics()) && entity != null) {
            if (this.shouldReturnToThrower()) {//Always return to valid thrower
                this.setNoPhysics(true);
                Vec3 vec3d = new Vec3(entity.getX() - this.getX(), entity.getEyeY() - this.getY(), entity.getZ() - this.getZ());
                this.setPosRaw(this.getX(), this.getY() + vec3d.y * 0.015D, this.getZ());
                if (this.level.isClientSide) {
                    this.yOld = this.getY();
                }

                double d0 = 0.05D;
                this.setDeltaMovement(this.getDeltaMovement().scale(0.95D).add(vec3d.normalize().scale(d0)));
                if (this.returningTicks == 0) {
                    this.playSound(SoundEvents.TRIDENT_RETURN, 10.0F, 1.0F);
                }
                ++this.returningTicks;
            }
        }
        super.tick();
    }

    @Override
    protected void onHitEntity(@Nonnull EntityHitResult rayTraceResult) {
        Entity entity = rayTraceResult.getEntity();
        Entity shooter = this.getOwner();

        if (shooter instanceof Player player) {
            DamageSource damagesource;

            damagesource = this.damageSources().arrow(this, shooter);

            if (this.isOnFire() && !(entity instanceof EnderMan)) {
                entity.setSecondsOnFire(5);
            }

            if (entity.hurt(damagesource, 13.0F)) {
                if (entity instanceof LivingEntity livingEntity) {
                    EnchantmentHelper.doPostHurtEffects(livingEntity, shooter);
                    EnchantmentHelper.doPostDamageEffects(player, livingEntity);

                    this.doPostHurtEffects(livingEntity);
                }
                if (this.level instanceof ServerLevel serverLevel) {
                    BlockPos entityPos = this.blockPosition();
                    if (this.level.canSeeSky(entityPos)) {
                        LightningBolt lightningBolt = EntityType.LIGHTNING_BOLT.create(this.level);
                        lightningBolt.moveTo(Vec3.atBottomCenterOf(entityPos));
                        lightningBolt.setCause(shooter instanceof ServerPlayer ? (ServerPlayer) shooter : null);
                        serverLevel.addFreshEntity(lightningBolt);
                    }
                }
                this.playSound(SoundEvents.TRIDENT_THUNDER, 4.0F, 1.0F);
            }
        }
    }

    @Override
    @Nonnull
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.TRIDENT_HIT_GROUND;
    }

    @Override
    public void playerTouch(@Nonnull Player player) {
        Entity entity = this.getOwner();
        if (entity == null || entity.getUUID() == player.getUUID()) {
            super.playerTouch(player);
        }
    }

    @Override
    public void readAdditionalSaveData(@Nonnull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("TefnutsCall", 10)) {
            this.thrownStack = ItemStack.of(compound.getCompound("TefnutsCall"));
        }
        this.dealtDamage = compound.getBoolean("DealtDamage");
    }

    @Override
    public void addAdditionalSaveData(@Nonnull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.put("TefnutsCall", this.thrownStack.save(new CompoundTag()));
        compound.putBoolean("DealtDamage", this.dealtDamage);
    }

    @Override
    public void tickDespawn() {
        if (this.pickup != AbstractArrow.Pickup.ALLOWED) {
            super.tickDespawn();
        }
    }

    @Override
    public boolean shouldRender(double x, double y, double z) {
        return true;
    }
}