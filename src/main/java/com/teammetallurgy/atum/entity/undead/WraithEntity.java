package com.teammetallurgy.atum.entity.undead;

import com.teammetallurgy.atum.entity.ai.pathfinding.ClimberGroundPathNavigator;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;

public class WraithEntity extends UndeadBaseEntity {
    private static final EntityDataAccessor<Byte> CLIMBING = SynchedEntityData.defineId(WraithEntity.class, EntityDataSerializers.BYTE);
    private int cycleHeight;
    private final int cycleTime;

    public WraithEntity(EntityType<? extends WraithEntity> entityType, Level world) {
        super(entityType, world);
        this.xpReward = 6;
        this.setCanPickUpLoot(false);

        this.cycleTime = (int) ((Math.random() * 40) + 80);
        this. cycleHeight = (int) (Math.random() * this.cycleTime);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new WraithEntity.AIWraithAttack(this));
    }

    protected void applyEntityAI() {
        super.applyEntityAI();
        this.targetSelector.addGoal(1, new WraithEntity.AIWraithTarget<>(this, Player.class));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(CLIMBING, (byte) 0);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 8.0F).add(Attributes.ATTACK_DAMAGE, 2.0D).add(Attributes.MOVEMENT_SPEED, 0.35D).add(Attributes.FOLLOW_RANGE, 30.0D);
    }

    @Override
    @Nonnull
    protected PathNavigation createNavigation(@Nonnull Level world) {
        return new ClimberGroundPathNavigator(this, world);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.HUSK_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(@Nonnull DamageSource damageSource) {
        return SoundEvents.HUSK_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.HUSK_DEATH;
    }

    @Override
    protected void playStepSound(@Nonnull BlockPos pos, @Nonnull BlockState state) {
    }

    @Override
    protected int calculateFallDamage(float distance, float damageMultiplier) {
        return 0;
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level.isClientSide) {
            this.setBesideClimbableBlock(this.horizontalCollision);
        }
    }

    @Override
    public void aiStep() {
        this.cycleHeight = (this.cycleHeight + 1) % this.cycleTime;

        super.aiStep();
    }

    @Override
    public boolean causeFallDamage(float distance, float damageMultiplier) {
        return false;
    }

    @Override
    public boolean onClimbable() {
        return this.isBesideClimbableBlock();
    }

    private boolean isBesideClimbableBlock() {
        return (this.entityData.get(CLIMBING) & 1) != 0;
    }

    private void setBesideClimbableBlock(boolean climbing) {
        byte b0 = this.entityData.get(CLIMBING);

        if (climbing) {
            b0 = (byte) (b0 | 1);
        } else {
            b0 = (byte) (b0 & -2);
        }

        this.entityData.set(CLIMBING, b0);
    }

    @Override
    public boolean doHurtTarget(@Nonnull Entity entity) {
        if (!super.doHurtTarget(entity)) {
            return false;
        } else {
            if (this.random.nextDouble() <= 0.175D) {
                if (entity instanceof LivingEntity) {
                    LivingEntity livingBase = (LivingEntity) entity;
                    if (!livingBase.hasEffect(MobEffects.MOVEMENT_SLOWDOWN)) {
                        livingBase.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 80, 1));
                    }
                }
            }
            return true;
        }
    }

    private static class AIWraithAttack extends MeleeAttackGoal {
        AIWraithAttack(WraithEntity wraith) {
            super(wraith, 1.0D, true);
        }

        @Override
        public boolean canContinueToUse() {
            float brightness = this.mob.getBrightness();
            if (brightness >= 0.5F && this.mob.getRandom().nextInt(100) == 0) {
                this.mob.setTarget(null);
                return false;
            } else {
                return super.canContinueToUse();
            }
        }

        @Override
        protected double getAttackReachSqr(LivingEntity attackTarget) {
            return 4.0F + attackTarget.getBbWidth();
        }
    }

    private static class AIWraithTarget<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
        AIWraithTarget(WraithEntity wraith, Class<T> classTarget) {
            super(wraith, classTarget, true);
        }

        @Override
        public boolean canUse() {
            float f = this.mob.getBrightness();
            return f < 0.5F && super.canUse();
        }
    }
}