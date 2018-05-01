package com.teammetallurgy.atum.entity;

import com.teammetallurgy.atum.entity.projectile.EntitySmallBone;
import com.teammetallurgy.atum.init.AtumItems;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityBonestorm extends EntityUndeadBase {
    private float heightOffset = 0.2F;
    private int heightOffsetUpdateTime;

    public EntityBonestorm(World world) {
        super(world);
        this.isImmuneToFire = true;
        this.experienceValue = 8;
    }

    @Override
    protected void initEntityAI() {
        this.tasks.addTask(4, new EntityBonestorm.AIBoneAttack(this));
        this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0D));
        this.tasks.addTask(7, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<EntityPlayer>(this, EntityPlayer.class, true));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(2.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.23000000417232513D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(48.0D);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_SKELETON_HORSE_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.ENTITY_SKELETON_HORSE_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_SKELETON_HORSE_DEATH;
    }

    @Override
    public void onLivingUpdate() {
        if (!this.onGround && this.motionY < 0.0D) {
            this.motionY *= 0.6D;
        }

        super.onLivingUpdate();
    }

    @Override
    protected void updateAITasks() {
        --this.heightOffsetUpdateTime;

        if (this.heightOffsetUpdateTime <= 0) {
            this.heightOffsetUpdateTime = 100;
            this.heightOffset = 0.5F + (float) this.rand.nextGaussian() * 3.0F;
        }

        EntityLivingBase entitylivingbase = this.getAttackTarget();

        if (entitylivingbase != null && entitylivingbase.posY + (double) entitylivingbase.getEyeHeight() > this.posY + (double) this.getEyeHeight() + (double) this.heightOffset) {
            this.motionY += (0.30000001192092896D - this.motionY) * 0.30000001192092896D;
            this.isAirBorne = true;
        }

        super.updateAITasks();
    }

    @Override
    public void fall(float distance, float damageMultiplier) {
    }

    @Override
    protected void dropFewItems(boolean recentlyHit, int looting) {
        if (recentlyHit) {
            int j = rand.nextInt(2) + 1 + rand.nextInt(1 + looting);
            for (int k = 0; k < j; ++k) {
                this.dropItem(AtumItems.DUSTY_BONE, 1);
            }
        }
    }

    private static class AIBoneAttack extends EntityAIBase {
        private EntityBonestorm bonestorm;
        private int timer;
        private int attackTime;

        private AIBoneAttack(EntityBonestorm entityBonestorm) {
            this.bonestorm = entityBonestorm;
            this.setMutexBits(3);
        }

        @Override
        public boolean shouldExecute() {
            EntityLivingBase entitylivingbase = this.bonestorm.getAttackTarget();
            return entitylivingbase != null && entitylivingbase.isEntityAlive();
        }

        @Override
        public void startExecuting() {
            this.timer = 0;
        }

        @Override
        public void updateTask() {
            --this.attackTime;
            EntityLivingBase entitylivingbase = this.bonestorm.getAttackTarget();
            double d0 = this.bonestorm.getDistanceSq(entitylivingbase);

            if (d0 < 4.0D) {
                if (this.attackTime <= 0) {
                    this.attackTime = 20;
                    this.bonestorm.attackEntityAsMob(entitylivingbase);
                }

                this.bonestorm.getMoveHelper().setMoveTo(entitylivingbase.posX, entitylivingbase.posY, entitylivingbase.posZ, 1.0D);
            } else if (d0 < 256.0D) {
                double d1 = entitylivingbase.posX - this.bonestorm.posX;
                double d2 = entitylivingbase.getEntityBoundingBox().minY + (double) (entitylivingbase.height / 2.0F) - (this.bonestorm.posY + (double) (this.bonestorm.height / 2.0F));
                double d3 = entitylivingbase.posZ - this.bonestorm.posZ;

                if (this.attackTime <= 0) {
                    ++this.timer;

                    if (this.timer == 1) {
                        this.attackTime = 60;
                    } else if (this.timer <= 4) {
                        this.attackTime = 6;
                    } else {
                        this.attackTime = 100;
                        this.timer = 0;
                    }

                    if (this.timer > 1) {
                        float f = MathHelper.sqrt(MathHelper.sqrt(d0)) * 0.5F;
                        this.bonestorm.world.playSound(null, entitylivingbase.getPosition(), SoundEvents.ENTITY_SKELETON_HURT, SoundCategory.HOSTILE, 0.7F, (this.bonestorm.rand.nextFloat() - this.bonestorm.rand.nextFloat()) * 0.2F + 1.0F);

                        for (int i = 0; i < 1; ++i) {
                            EntitySmallBone entitySmallBone = new EntitySmallBone(this.bonestorm.world, this.bonestorm, d1 + this.bonestorm.getRNG().nextGaussian() * (double) f, d2, d3 + this.bonestorm.getRNG().nextGaussian() * (double) f);
                            entitySmallBone.posY = this.bonestorm.posY + (double) (this.bonestorm.height / 2.0F) + 0.5D;
                            this.bonestorm.world.spawnEntity(entitySmallBone);
                        }
                    }
                }
                this.bonestorm.getLookHelper().setLookPositionWithEntity(entitylivingbase, 10.0F, 10.0F);
            } else {
                this.bonestorm.getNavigator().clearPath();
                this.bonestorm.getMoveHelper().setMoveTo(entitylivingbase.posX, entitylivingbase.posY, entitylivingbase.posZ, 1.0D);
            }
            super.updateTask();
        }
    }
}