package com.teammetallurgy.atum.entity.projectile.arrow;

import com.teammetallurgy.atum.init.AtumEntities;
import com.teammetallurgy.atum.init.AtumItems;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

public class TefnutsCallEntity extends TridentEntity { //TODO

    public TefnutsCallEntity(EntityType<? extends TefnutsCallEntity> entityType, World world) {
        super(entityType, world);
        this.thrownStack = new ItemStack(AtumItems.TEFNUTS_CALL);
    }

    public TefnutsCallEntity(World world, LivingEntity shooter, @Nonnull ItemStack stack) {
        this(AtumEntities.TEFNUTS_CALL, world);
        this.setPosition(shooter.getPosX(), shooter.getPosYEye() - (double) 0.1F, shooter.getPosZ());
        this.thrownStack = new ItemStack(AtumItems.TEFNUTS_CALL);
        this.thrownStack = stack.copy();
        this.setShooter(shooter);
        if (shooter instanceof PlayerEntity) {
            this.pickupStatus = AbstractArrowEntity.PickupStatus.ALLOWED;
        }
        this.dataManager.set(LOYALTY_LEVEL, (byte) EnchantmentHelper.getLoyaltyModifier(stack));
        this.dataManager.set(field_226571_aq_, stack.hasEffect());
    }

    @OnlyIn(Dist.CLIENT)
    public TefnutsCallEntity(World world, double x, double y, double z) {
        this(AtumEntities.TEFNUTS_CALL, world);
        this.setPosition(x, y, z);
        this.thrownStack = new ItemStack(AtumItems.TEFNUTS_CALL);
    }

    @Override
    public boolean isImmuneToExplosions() {
        return true;
    }

    /*@Override
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
                this.world.addWeatherEffect(new EntityLightningBolt(world, getPosX(), getPosY(), getPosZ(), false));
            }
        }
        super.onHit(raytraceResult);
    }*/

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean canRenderOnFire() {
        return false;
    }
}