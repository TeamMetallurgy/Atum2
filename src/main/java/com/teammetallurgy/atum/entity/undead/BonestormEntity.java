package com.teammetallurgy.atum.entity.undead;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.entity.ITexture;
import com.teammetallurgy.atum.entity.projectile.SmallBoneEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import java.util.EnumSet;

public class BonestormEntity extends UndeadBaseEntity implements ITexture {
    private static final ResourceLocation BONESTORM_TEXTURE = new ResourceLocation(Atum.MOD_ID, "textures/entity/bonestorm.png");
    private String texturePath;

    private float heightOffset = 0.2F;
    private int heightOffsetUpdateTime;

    public BonestormEntity(EntityType<? extends BonestormEntity> entityType, Level level) {
        super(entityType, level);
        this.xpReward = 8;
        this.setCanPickUpLoot(false);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new BonestormEntity.AIBoneAttack(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 15.0F).add(Attributes.ATTACK_DAMAGE, 3.0D).add(Attributes.MOVEMENT_SPEED, 0.24D).add(Attributes.FOLLOW_RANGE, 30.0D);
    }

    @Override
    protected float getBurnDamage() {
        return 0.5F;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.SKELETON_HORSE_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(@Nonnull DamageSource damageSource) {
        return SoundEvents.SKELETON_HORSE_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.SKELETON_HORSE_DEATH;
    }

    @Override
    public void aiStep() {
        if (!this.onGround && this.getDeltaMovement().y < 0.0D) {
            this.setDeltaMovement(this.getDeltaMovement().multiply(1.0D, 0.6D, 1.0D));
        }

        super.aiStep();
    }

    @Override
    protected void customServerAiStep() {
        --this.heightOffsetUpdateTime;

        if (this.heightOffsetUpdateTime <= 0) {
            this.heightOffsetUpdateTime = 100;
            this.heightOffset = 0.5F + (float) this.random.nextGaussian() * 3.0F;
        }

        LivingEntity livingBase = this.getTarget();

        if (livingBase != null && livingBase.getY() + (double) livingBase.getEyeHeight() > this.getY() + (double) this.getEyeHeight() + (double) this.heightOffset) {
            this.setDeltaMovement(this.getDeltaMovement().add(0.0D, (0.30000001192092896D - this.getDeltaMovement().y) * 0.30000001192092896D, 0.0D));
            this.hasImpulse = true;
        }

        super.customServerAiStep();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public String getTexture() {
        if (this.texturePath == null) {
            this.texturePath = BONESTORM_TEXTURE.toString();
        }
        return this.texturePath;
    }

    @Override
    public boolean causeFallDamage(float distance, float damageMultiplier, DamageSource source) {
        return false;
    }

    private static class AIBoneAttack extends Goal {
        private final BonestormEntity bonestorm;
        private int attackStep;
        private int attackTime;

        private AIBoneAttack(BonestormEntity bonestorm) {
            this.bonestorm = bonestorm;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            LivingEntity livingBase = this.bonestorm.getTarget();
            return livingBase != null && livingBase.isAlive();
        }

        @Override
        public void start() {
            this.attackStep = 0;
        }

        @Override
        public void tick() {
            --this.attackTime;
            LivingEntity livingBase = this.bonestorm.getTarget();
            if (livingBase != null) {
                double distance = this.bonestorm.distanceToSqr(livingBase);

                if (distance < 4.0D) {
                    if (this.attackTime <= 0) {
                        this.attackTime = 20;
                        this.bonestorm.doHurtTarget(livingBase);
                    }
                    this.bonestorm.getMoveControl().setWantedPosition(livingBase.getX(), livingBase.getY(), livingBase.getZ(), 1.0D);
                } else if (distance < this.getFollowDistance() * this.getFollowDistance()) {
                    double boneX = livingBase.getX() - this.bonestorm.getX();
                    double boneY = livingBase.getBoundingBox().minY + (double) (livingBase.getBbHeight() / 2.0F) - (this.bonestorm.getY() + (double) (this.bonestorm.getBbHeight() / 2.0F));
                    double boneZ = livingBase.getZ() - this.bonestorm.getZ();

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
                            float f = Mth.sqrt(Mth.sqrt((float) distance)) * 0.5F;
                            this.bonestorm.level.playSound(null, livingBase.blockPosition(), SoundEvents.SKELETON_HURT, SoundSource.HOSTILE, 0.7F, (this.bonestorm.random.nextFloat() - this.bonestorm.random.nextFloat()) * 0.2F + 1.0F);

                            for (int i = 0; i < 1; ++i) {
                                SmallBoneEntity entitySmallBone = new SmallBoneEntity(this.bonestorm.level, this.bonestorm, boneX + this.bonestorm.getRandom().nextGaussian() * (double) f, boneY, boneZ + this.bonestorm.getRandom().nextGaussian() * (double) f);
                                entitySmallBone.setPos(entitySmallBone.getX(), this.bonestorm.getY() + (this.bonestorm.getBbHeight() / 2.0F) + 0.5D, entitySmallBone.getZ());
                                this.bonestorm.level.addFreshEntity(entitySmallBone);
                            }
                        }
                    }
                    this.bonestorm.getLookControl().setLookAt(livingBase, 10.0F, 10.0F);
                } else {
                    this.bonestorm.getNavigation().stop();
                    this.bonestorm.getMoveControl().setWantedPosition(livingBase.getX(), livingBase.getY(), livingBase.getZ(), 1.0D);
                }
            }
            super.tick();
        }

        private double getFollowDistance() {
            return this.bonestorm.getAttributeValue(Attributes.FOLLOW_RANGE);
        }
    }
}