package com.teammetallurgy.atum.entity.undead;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.ClimberPathNavigator;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class WraithEntity extends UndeadBaseEntity {
    private static final DataParameter<Byte> CLIMBING = EntityDataManager.createKey(WraithEntity.class, DataSerializers.BYTE);
    private int cycleHeight;
    private int cycleTime;

    public WraithEntity(EntityType<? extends WraithEntity> entityType, World world) {
        super(entityType, world);
        this.experienceValue = 6;
        this.setCanPickUpLoot(false);

        cycleTime = (int) ((Math.random() * 40) + 80);
        cycleHeight = (int) (Math.random() * cycleTime);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new WraithEntity.AIWraithAttack(this));
    }

    protected void applyEntityAI() {
        super.applyEntityAI();
        this.targetSelector.addGoal(1, new WraithEntity.AIWraithTarget<>(this, PlayerEntity.class));
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(CLIMBING, (byte) 0);
    }

    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(8.0D);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.35D);
        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(2.0D);
        this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(30.0D);
    }

    @Override
    @Nonnull
    protected PathNavigator createNavigator(@Nonnull World world) {
        return new ClimberPathNavigator(this, world);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_HUSK_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.ENTITY_HUSK_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_HUSK_DEATH;
    }

    @Override
    protected void playStepSound(@Nonnull BlockPos pos, BlockState state) {
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.world.isRemote) {
            this.setBesideClimbableBlock(this.collidedHorizontally);
        }
    }

    @Override
    public void livingTick() {
        cycleHeight = (cycleHeight + 1) % cycleTime;

        super.livingTick();
    }

    @Override
    public void fall(float distance, float damageMultiplier) {
    }

    @Override
    public boolean isOnLadder() {
        return this.isBesideClimbableBlock();
    }

    private boolean isBesideClimbableBlock() {
        return (this.dataManager.get(CLIMBING) & 1) != 0;
    }

    private void setBesideClimbableBlock(boolean climbing) {
        byte b0 = this.dataManager.get(CLIMBING);

        if (climbing) {
            b0 = (byte) (b0 | 1);
        } else {
            b0 = (byte) (b0 & -2);
        }

        this.dataManager.set(CLIMBING, b0);
    }

    @Override
    public boolean attackEntityAsMob(Entity entity) {
        if (!super.attackEntityAsMob(entity)) {
            return false;
        } else {
            if (rand.nextDouble() <= 0.175D) {
                if (entity instanceof LivingEntity) {
                    LivingEntity livingBase = (LivingEntity) entity;
                    if (!livingBase.isPotionActive(Effects.SLOWNESS)) {
                        livingBase.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 80, 1));
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
        public boolean shouldContinueExecuting() {
            float brightness = this.attacker.getBrightness();
            if (brightness >= 0.5F && this.attacker.getRNG().nextInt(100) == 0) {
                this.attacker.setAttackTarget(null);
                return false;
            } else {
                return super.shouldContinueExecuting();
            }
        }

        @Override
        protected double getAttackReachSqr(LivingEntity attackTarget) {
            return 4.0F + attackTarget.getWidth();
        }
    }

    private static class AIWraithTarget<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
        AIWraithTarget(WraithEntity wraith, Class<T> classTarget) {
            super(wraith, classTarget, true);
        }

        @Override
        public boolean shouldExecute() {
            float f = this.goalOwner.getBrightness();
            return f < 0.5F && super.shouldExecute();
        }
    }
}