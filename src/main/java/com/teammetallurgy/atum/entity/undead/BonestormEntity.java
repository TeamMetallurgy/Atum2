package com.teammetallurgy.atum.entity.undead;

import com.teammetallurgy.atum.entity.projectile.SmallBoneEntity;
import com.teammetallurgy.atum.integration.champion.ChampionsHelper;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.*;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.EnumSet;

public class BonestormEntity extends UndeadBaseEntity {
    private static final ResourceLocation BONESTORM_TEXTURE = new ResourceLocation(Constants.MOD_ID, "textures/entity/bonestorm.png");
    private String texturePath;

    private float heightOffset = 0.2F;
    private int heightOffsetUpdateTime;

    public BonestormEntity(EntityType<? extends BonestormEntity> entityType, World world) {
        super(entityType, world);
        this.experienceValue = 8;
        this.setCanPickUpLoot(false);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new BonestormEntity.AIBoneAttack(this));
    }

    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(15.0D);
        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(3.0D);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.24D);
        this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(30.0D);
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
    public void livingTick() {
        if (!this.onGround && this.getMotion().y < 0.0D) {
            this.setMotion(this.getMotion().mul(1.0D, 0.6D, 1.0D));
        }

        super.livingTick();
    }

    @Override
    protected void updateAITasks() {
        --this.heightOffsetUpdateTime;

        if (this.heightOffsetUpdateTime <= 0) {
            this.heightOffsetUpdateTime = 100;
            this.heightOffset = 0.5F + (float) this.rand.nextGaussian() * 3.0F;
        }

        LivingEntity livingBase = this.getAttackTarget();

        if (livingBase != null && livingBase.posY + (double) livingBase.getEyeHeight() > this.posY + (double) this.getEyeHeight() + (double) this.heightOffset) {
            this.setMotion(this.getMotion().add(0.0D, (0.30000001192092896D - this.getMotion().y) * 0.30000001192092896D, 0.0D));
            this.isAirBorne = true;
        }

        super.updateAITasks();
    }

    @OnlyIn(Dist.CLIENT)
    public String getTexture() {
        if (this.texturePath == null) {
            if (ChampionsHelper.isChampion(this)) {
                ResourceLocation texture = ChampionsHelper.getTexture(this, "bonestorm");
                if (texture != null) {
                    this.texturePath = texture.toString();
                    return this.texturePath;
                }
            }
            this.texturePath = BONESTORM_TEXTURE.toString();
        }
        return this.texturePath;
    }

    @Override
    public void fall(float distance, float damageMultiplier) {
    }

    private static class AIBoneAttack extends Goal {
        private BonestormEntity bonestorm;
        private int attackStep;
        private int attackTime;

        private AIBoneAttack(BonestormEntity bonestorm) {
            this.bonestorm = bonestorm;
            this.setMutexFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean shouldExecute() {
            LivingEntity livingBase = this.bonestorm.getAttackTarget();
            return livingBase != null && livingBase.isAlive();
        }

        @Override
        public void startExecuting() {
            this.attackStep = 0;
        }

        @Override
        public void tick() {
            --this.attackTime;
            LivingEntity livingBase = this.bonestorm.getAttackTarget();
            if (livingBase != null) {
                double distance = this.bonestorm.getDistanceSq(livingBase);

                if (distance < 4.0D) {
                    if (this.attackTime <= 0) {
                        this.attackTime = 20;
                        this.bonestorm.attackEntityAsMob(livingBase);
                    }
                    this.bonestorm.getMoveHelper().setMoveTo(livingBase.posX, livingBase.posY, livingBase.posZ, 1.0D);
                } else if (distance < this.getFollowDistance() * this.getFollowDistance()) {
                    double boneX = livingBase.posX - this.bonestorm.posX;
                    double boneY = livingBase.getBoundingBox().minY + (double) (livingBase.getHeight() / 2.0F) - (this.bonestorm.posY + (double) (this.bonestorm.getHeight() / 2.0F));
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
                                SmallBoneEntity entitySmallBone = new SmallBoneEntity(this.bonestorm.world, this.bonestorm, boneX + this.bonestorm.getRNG().nextGaussian() * (double) f, boneY, boneZ + this.bonestorm.getRNG().nextGaussian() * (double) f);
                                entitySmallBone.posY = this.bonestorm.posY + (this.bonestorm.getHeight() / 2.0F) + 0.5D;
                                this.bonestorm.world.addEntity(entitySmallBone);
                            }
                        }
                    }
                    this.bonestorm.getLookController().setLookPositionWithEntity(livingBase, 10.0F, 10.0F);
                } else {
                    this.bonestorm.getNavigator().clearPath();
                    this.bonestorm.getMoveHelper().setMoveTo(livingBase.posX, livingBase.posY, livingBase.posZ, 1.0D);
                }
            }
            super.tick();
        }

        private double getFollowDistance() {
            return this.bonestorm.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).getValue();
        }
    }
}
