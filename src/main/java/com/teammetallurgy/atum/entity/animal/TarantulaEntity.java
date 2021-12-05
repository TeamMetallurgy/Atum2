package com.teammetallurgy.atum.entity.animal;

import com.teammetallurgy.atum.entity.undead.UndeadBaseEntity;
import com.teammetallurgy.atum.init.AtumStructures;
import com.teammetallurgy.atum.world.gen.structure.StructureHelper;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.ai.navigation.WallClimberNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;

import javax.annotation.Nonnull;
import java.util.Random;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;

public class TarantulaEntity extends Monster {
    private static final EntityDataAccessor<Byte> CLIMBING = SynchedEntityData.defineId(TarantulaEntity.class, EntityDataSerializers.BYTE);

    public TarantulaEntity(EntityType<? extends Monster> entityType, Level world) {
        super(entityType, world);
        this.xpReward = 5;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(3, new LeapAtTargetGoal(this, 0.4F));
        this.goalSelector.addGoal(4, new AITarantulaAttack(this));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, TarantulaEntity.class));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, UndeadBaseEntity.class, true));
    }

    public static AttributeSupplier.Builder getAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 15.0D).add(Attributes.MOVEMENT_SPEED, 0.4D)
                .add(Attributes.ATTACK_DAMAGE, 4.0D).add(Attributes.FOLLOW_RANGE, 36.0D);
    }

    @Override
    public boolean checkSpawnRules(@Nonnull LevelAccessor world, @Nonnull MobSpawnType spawnReason) {
        return spawnReason == MobSpawnType.SPAWNER || super.checkSpawnRules(world, spawnReason);
    }

    public static boolean canSpawn(EntityType<? extends TarantulaEntity> tarantula, ServerLevelAccessor world, MobSpawnType spawnReason, BlockPos pos, Random random) {
        return (spawnReason == MobSpawnType.SPAWNER || pos.getY() >= 40 && pos.getY() <= 62 && !world.canSeeSkyFromBelowWater(pos.below())) && checkMonsterSpawnRules(tarantula, world, spawnReason, pos, random) &&
                world instanceof ServerLevel && !StructureHelper.doesChunkHaveStructure((ServerLevel) world, pos, AtumStructures.PYRAMID_STRUCTURE);
    }

    @Override
    public double getPassengersRidingOffset() {
        return this.getBbHeight() * 0.5F;
    }

    @Override
    @Nonnull
    protected PathNavigation createNavigation(@Nonnull Level world) {
        return new WallClimberNavigation(this, world);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(CLIMBING, (byte) 0);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level.isClientSide) {
            this.setBesideClimbableBlock(this.horizontalCollision);
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.SPIDER_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(@Nonnull DamageSource damageSource) {
        return SoundEvents.SPIDER_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.SPIDER_DEATH;
    }

    @Override
    protected void playStepSound(@Nonnull BlockPos pos, @Nonnull BlockState state) {
        this.playSound(SoundEvents.SPIDER_STEP, 0.15F, 1.0F);
    }

    @Override
    public boolean onClimbable() {
        return this.isBesideClimbableBlock();
    }

    @Override
    @Nonnull
    public MobType getMobType() {
        return MobType.ARTHROPOD;
    }

    @Override
    public boolean doHurtTarget(@Nonnull Entity entity) {
        if (super.doHurtTarget(entity)) {
            if (entity instanceof LivingEntity) {
                int i = 0;
                if (this.level.getDifficulty() == Difficulty.NORMAL) {
                    i = 5;
                } else if (this.level.getDifficulty() == Difficulty.HARD) {
                    i = 8;
                }
                if (i > 0) {
                    ((LivingEntity) entity).addEffect(new MobEffectInstance(MobEffects.WEAKNESS, i * 20, 0));
                }
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean canBeAffected(@Nonnull MobEffectInstance effect) {
        return effect.getEffect() != MobEffects.POISON && super.canBeAffected(effect);
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
    public float getEyeHeight(@Nonnull Pose pose) {
        return 0.35F;
    }

    static class AITarantulaAttack extends MeleeAttackGoal {
        AITarantulaAttack(TarantulaEntity tarantula) {
            super(tarantula, 1.0D, true);
        }

        @Override
        protected double getAttackReachSqr(LivingEntity attackTarget) {
            return 4.0F + attackTarget.getBbWidth();
        }
    }
}