package com.teammetallurgy.atum.entity.projectile.arrow;

import com.teammetallurgy.atum.init.AtumEntities;
import com.teammetallurgy.atum.init.AtumItems;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.monster.EndermanEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.server.SChangeGameStatePacket;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TefnutsCallEntity extends AbstractArrowEntity {
    protected ItemStack thrownStack = new ItemStack(AtumItems.TEFNUTS_CALL);
    private boolean dealtDamage;
    public int returningTicks;

    public TefnutsCallEntity(FMLPlayMessages.SpawnEntity spawnPacket, World world) {
        this(AtumEntities.TEFNUTS_CALL, world);
    }

    public TefnutsCallEntity(EntityType<? extends TefnutsCallEntity> entityType, World world) {
        super(entityType, world);
    }

    public TefnutsCallEntity(World world, LivingEntity shooter, @Nonnull ItemStack stack) {
        super(AtumEntities.TEFNUTS_CALL, shooter, world);
        this.thrownStack = stack.copy();
    }

    @OnlyIn(Dist.CLIENT)
    public TefnutsCallEntity(World world, double x, double y, double z) {
        super(AtumEntities.TEFNUTS_CALL, x, y, z, world);
    }

    @Override
    @Nonnull
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public boolean isImmuneToExplosions() {
        return true;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean canRenderOnFire() {
        return false;
    }

    @Override
    @Nonnull
    protected ItemStack getArrowStack() {
        return this.thrownStack.copy();
    }

    @Override
    @Nullable
    protected EntityRayTraceResult rayTraceEntities(@Nonnull Vector3d startVec, @Nonnull Vector3d endVec) {
        return this.dealtDamage ? null : super.rayTraceEntities(startVec, endVec);
    }

    private boolean shouldReturnToThrower() {
        Entity entity = this.getShooter();
        if (entity != null && entity.isAlive()) {
            return !(entity instanceof ServerPlayerEntity) || !entity.isSpectator();
        } else {
            return false;
        }
    }

    @Override
    public void tick() {
        if (this.timeInGround > 4) {
            this.dealtDamage = true;
        }

        Entity entity = this.getShooter();
        if ((this.dealtDamage || this.getNoClip()) && entity != null) {
            if (this.shouldReturnToThrower()) {//Always return to valid thrower
                this.setNoClip(true);
                Vector3d vec3d = new Vector3d(entity.getPosX() - this.getPosX(), entity.getPosYEye() - this.getPosY(), entity.getPosZ() - this.getPosZ());
                this.setRawPosition(this.getPosX(), this.getPosY() + vec3d.y * 0.015D, this.getPosZ());
                if (this.world.isRemote) {
                    this.lastTickPosY = this.getPosY();
                }

                double d0 = 0.05D;
                this.setMotion(this.getMotion().scale(0.95D).add(vec3d.normalize().scale(d0)));
                if (this.returningTicks == 0) {
                    this.playSound(SoundEvents.ITEM_TRIDENT_RETURN, 10.0F, 1.0F);
                }
                ++this.returningTicks;
            }
        }
        super.tick();
    }

    @Override
    protected void onEntityHit(@Nonnull EntityRayTraceResult rayTraceResult) {
        Entity entity = rayTraceResult.getEntity();
        Entity shooter = this.getShooter();

        if (shooter != entity) {
            Vector3d motion = this.getMotion();
            float f = MathHelper.sqrt(motion.x * motion.x + motion.y * motion.y + motion.z * motion.z);
            int i = MathHelper.ceil((double) f * this.getDamage());
            if (this.getIsCritical()) {
                i += this.rand.nextInt(i / 2 + 2);
            }

            DamageSource damagesource;

            if (shooter == null) {
                damagesource = DamageSource.causeArrowDamage(this, this);
            } else {
                damagesource = DamageSource.causeArrowDamage(this, shooter);
            }

            if (this.isBurning() && !(entity instanceof EndermanEntity)) {
                entity.setFire(5);
            }

            if (entity.attackEntityFrom(damagesource, (float) i)) {
                if (entity instanceof LivingEntity) {
                    LivingEntity livingEntity = (LivingEntity) entity;

                    if (shooter instanceof LivingEntity) {
                        EnchantmentHelper.applyThornEnchantments(livingEntity, shooter);
                        EnchantmentHelper.applyArthropodEnchantments((LivingEntity) shooter, livingEntity);
                    }

                    this.arrowHit(livingEntity);

                    if (livingEntity != shooter && livingEntity instanceof PlayerEntity && shooter instanceof ServerPlayerEntity) {
                        ((ServerPlayerEntity) shooter).connection.sendPacket(new SChangeGameStatePacket(6, 0.0F));
                    }
                }
                if (this.world instanceof ServerWorld) {
                    ServerWorld serverWorld = (ServerWorld) this.world;
                    BlockPos entityPos = this.getPosition();
                    if (this.world.canSeeSky(entityPos)) {
                        LightningBoltEntity lightningBolt = new LightningBoltEntity(this.world, (double) entityPos.getX() + 0.5D, entityPos.getY(), (double) entityPos.getZ() + 0.5D, false);
                        lightningBolt.setCaster(shooter instanceof ServerPlayerEntity ? (ServerPlayerEntity) shooter : null);
                        serverWorld.addLightningBolt(lightningBolt);
                    }
                }
                this.playSound(SoundEvents.ITEM_TRIDENT_THUNDER, 4.0F, 1.0F);
            }
        }
    }

    @Override
    @Nonnull
    protected SoundEvent getHitEntitySound() {
        return SoundEvents.ITEM_TRIDENT_HIT_GROUND;
    }

    @Override
    public void onCollideWithPlayer(@Nonnull PlayerEntity player) {
        Entity entity = this.getShooter();
        if (entity == null || entity.getUniqueID() == player.getUniqueID()) {
            super.onCollideWithPlayer(player);
        }
    }

    @Override
    public void readAdditional(@Nonnull CompoundNBT compound) {
        super.readAdditional(compound);
        if (compound.contains("TefnutsCall", 10)) {
            this.thrownStack = ItemStack.read(compound.getCompound("TefnutsCall"));
        }
        this.dealtDamage = compound.getBoolean("DealtDamage");
    }

    @Override
    public void writeAdditional(@Nonnull CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.put("TefnutsCall", this.thrownStack.write(new CompoundNBT()));
        compound.putBoolean("DealtDamage", this.dealtDamage);
    }

    @Override
    public void func_225516_i_() {
        if (this.pickupStatus != AbstractArrowEntity.PickupStatus.ALLOWED) {
            super.func_225516_i_();
        }
    }

    @Override
    public boolean isInRangeToRender3d(double x, double y, double z) {
        return true;
    }
}