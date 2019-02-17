package com.teammetallurgy.atum.entity.undead;

import com.teammetallurgy.atum.entity.projectile.EntitySmallBone;
import com.teammetallurgy.atum.init.AtumLootTables;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Objects;

public class EntityBonestorm extends EntityUndeadBase {
    private float heightOffset = 0.2F;
    private int heightOffsetUpdateTime;

    public EntityBonestorm(World world) {
        super(world);
        this.experienceValue = 8;
        this.setCanPickUpLoot(false);
    }

    @Override
    protected void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(1, new EntityBonestorm.AIBoneAttack(this));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(15.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(3.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.24D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(36.0D);
    }

    @Override
    protected float getBurnDamage() {
        return 0.5F;
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

        EntityLivingBase livingBase = this.getAttackTarget();

        if (livingBase != null && livingBase.posY + (double) livingBase.getEyeHeight() > this.posY + (double) this.getEyeHeight() + (double) this.heightOffset) {
            this.motionY += (0.30000001192092896D - this.motionY) * 0.30000001192092896D;
            this.isAirBorne = true;
        }

        super.updateAITasks();
    }

    @Override
    public void fall(float distance, float damageMultiplier) {
    }

    @Override
    @Nullable
    protected ResourceLocation getLootTable() {
        return AtumLootTables.BONESTORM;
    }

    private static class AIBoneAttack extends EntityAIBase {
        private EntityBonestorm bonestorm;
        private int attackStep;
        private int attackTime;

        private AIBoneAttack(EntityBonestorm bonestorm) {
            this.bonestorm = bonestorm;
            this.setMutexBits(3);
        }

        @Override
        public boolean shouldExecute() {
            EntityLivingBase livingBase = this.bonestorm.getAttackTarget();
            return livingBase != null && livingBase.isEntityAlive();
        }

        @Override
        public void startExecuting() {
            this.attackStep = 0;
        }

        @Override
        public void updateTask() {
            --this.attackTime;
            EntityLivingBase livingBase = this.bonestorm.getAttackTarget();
            double distance = this.bonestorm.getDistanceSq(Objects.requireNonNull(livingBase));

            if (distance < 4.0D) {
                if (this.attackTime <= 0) {
                    this.attackTime = 20;
                    this.bonestorm.attackEntityAsMob(livingBase);
                }
                this.bonestorm.getMoveHelper().setMoveTo(livingBase.posX, livingBase.posY, livingBase.posZ, 1.0D);
            } else if (distance < this.getFollowDistance() * this.getFollowDistance()) {
                double boneX = livingBase.posX - this.bonestorm.posX;
                double boneY = livingBase.getEntityBoundingBox().minY + (double) (livingBase.height / 2.0F) - (this.bonestorm.posY + (double) (this.bonestorm.height / 2.0F));
                double boneZ = livingBase.posZ - this.bonestorm.posZ;

                if (this.attackTime <= 0) {
                    ++this.attackStep;

                    if (this.attackStep == 1) {
                        this.attackTime = 60;
                    } else if (this.attackStep <= 4) {
                        this.attackTime = 6;
                    } else {
                        this.attackTime = 100;
                        this.attackStep = 0;
                    }

                    if (this.attackStep > 1) {
                        float f = MathHelper.sqrt(MathHelper.sqrt(distance)) * 0.5F;
                        this.bonestorm.world.playSound(null, livingBase.getPosition(), SoundEvents.ENTITY_SKELETON_HURT, SoundCategory.HOSTILE, 0.7F, (this.bonestorm.rand.nextFloat() - this.bonestorm.rand.nextFloat()) * 0.2F + 1.0F);

                        for (int i = 0; i < 1; ++i) {
                            EntitySmallBone entitySmallBone = new EntitySmallBone(this.bonestorm.world, this.bonestorm, boneX + this.bonestorm.getRNG().nextGaussian() * (double) f, boneY, boneZ + this.bonestorm.getRNG().nextGaussian() * (double) f);
                            entitySmallBone.posY = this.bonestorm.posY + (double) (this.bonestorm.height / 2.0F) + 0.5D;
                            this.bonestorm.world.spawnEntity(entitySmallBone);
                        }
                    }
                }
                this.bonestorm.getLookHelper().setLookPositionWithEntity(livingBase, 10.0F, 10.0F);
            } else {
                this.bonestorm.getNavigator().clearPath();
                this.bonestorm.getMoveHelper().setMoveTo(livingBase.posX, livingBase.posY, livingBase.posZ, 1.0D);
            }
            super.updateTask();
        }

        private double getFollowDistance() {
            return this.bonestorm.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).getAttributeValue();
        }
    }
}
