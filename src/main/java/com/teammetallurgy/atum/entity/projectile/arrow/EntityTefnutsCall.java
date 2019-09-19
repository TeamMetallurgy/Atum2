package com.teammetallurgy.atum.entity.projectile.arrow;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.play.server.SPacketChangeGameState;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

public class EntityTefnutsCall extends CustomArrow {
    private ItemStack stack = ItemStack.EMPTY;

    public EntityTefnutsCall(World world) {
        super(world);
    }

    public EntityTefnutsCall(World world, LivingEntity shooter) {
        super(world, shooter);
    }

    public void setStack(@Nonnull ItemStack stack) {
        this.stack = stack;
    }

    @Override
    @Nonnull
    protected ItemStack getArrowStack() {
        return stack;
    }

    @Override
    protected void onHit(RayTraceResult raytraceResult) {
        Entity entity = raytraceResult.entityHit;
        if (raytraceResult != null && raytraceResult.entityHit instanceof PlayerEntity) {
            PlayerEntity entityplayer = (PlayerEntity) raytraceResult.entityHit;

            if (this.shootingEntity instanceof PlayerEntity && !((PlayerEntity) this.shootingEntity).canAttackPlayer(entityplayer)) {
                raytraceResult = null;
            }
        }

        if (raytraceResult != null && entity != null && raytraceResult.getType() == RayTraceResult.Type.ENTITY) {
            float f = MathHelper.sqrt(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
            int i = MathHelper.ceil((double) f * this.getDamage());
            if (this.getIsCritical()) {
                i += this.rand.nextInt(i / 2 + 2);
            }

            DamageSource damagesource;

            if (this.shootingEntity == null) {
                damagesource = DamageSource.causeArrowDamage(this, this);
            } else {
                damagesource = DamageSource.causeArrowDamage(this, this.shootingEntity);
            }

            if (this.isBurning() && !(entity instanceof EntityEnderman)) {
                entity.setFire(5);
            }

            if (entity.attackEntityFrom(damagesource, (float) i)) {
                if (entity instanceof LivingEntity) {
                    LivingEntity entitylivingbase = (LivingEntity) entity;

                    if (!this.world.isRemote) {
                        entitylivingbase.setArrowCountInEntity(entitylivingbase.getArrowCountInEntity() + 1);
                    }

                    if (this.shootingEntity instanceof LivingEntity) {
                        EnchantmentHelper.applyThornEnchantments(entitylivingbase, this.shootingEntity);
                        EnchantmentHelper.applyArthropodEnchantments((LivingEntity) this.shootingEntity, entitylivingbase);
                    }

                    this.arrowHit(entitylivingbase);

                    if (this.shootingEntity != null && entitylivingbase != this.shootingEntity && entitylivingbase instanceof PlayerEntity && this.shootingEntity instanceof ServerPlayerEntity) {
                        ((ServerPlayerEntity) this.shootingEntity).connection.sendPacket(new SPacketChangeGameState(6, 0.0F));
                    }
                }
                world.addWeatherEffect(new EntityLightningBolt(world, posX, posY, posZ, false));
            }
        }
        super.onHit(raytraceResult);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean canRenderOnFire() {
        return false;
    }

    @Override
    public void writeEntityToNBT(@Nonnull CompoundNBT compound) {
        CompoundNBT stackTag = new CompoundNBT();
        stack.writeToNBT(stackTag);
        compound.setTag("stack", stackTag);
    }

    @Override
    public void readEntityFromNBT(@Nonnull CompoundNBT compound) {
        CompoundNBT stackTag = compound.getCompoundTag("stack");
        stack = new ItemStack(stackTag);
    }
}