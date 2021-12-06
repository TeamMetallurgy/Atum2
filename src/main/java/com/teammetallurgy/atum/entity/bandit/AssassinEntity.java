package com.teammetallurgy.atum.entity.bandit;

import com.teammetallurgy.atum.entity.ai.goal.OpenAnyDoorGoal;
import com.teammetallurgy.atum.entity.ai.pathfinding.ClimberGroundPathNavigator;
import com.teammetallurgy.atum.init.AtumItems;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

public class AssassinEntity extends BanditBaseEntity {
    private final DamageSource ASSASSINATED = new EntityDamageSource("assassinated", this);
    private static final EntityDataAccessor<Byte> CLIMBING = SynchedEntityData.defineId(AssassinEntity.class, EntityDataSerializers.BYTE);
    private LivingEntity markedTarget;

    public AssassinEntity(EntityType<? extends AssassinEntity> entityType, Level world) {
        super(entityType, world);
        this.xpReward = 12;
        (new ClimberGroundPathNavigator(this, world)).setCanOpenDoors(true);
        this.setCanPatrol(false);
    }

    @Override
    protected boolean hasSkinVariants() {
        return false;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new MarkedForDeathGoal(this, this.markedTarget));
        this.goalSelector.addGoal(3, new OpenAnyDoorGoal(this, false, true));
        this.goalSelector.addGoal(4, new AssassinMeleeAttackGoal(this, 1.2D, true));
    }

    @Override
    protected void applyEntityAI() {
        this.targetSelector.addGoal(0, new HurtByTargetGoal(this, BanditBaseEntity.class));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, false));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(CLIMBING, (byte) 0);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return getBaseAttributes().add(Attributes.MAX_HEALTH, 40.0D).add(Attributes.ATTACK_DAMAGE, 5.0D).add(Attributes.ARMOR, 4.0F);
    }

    @Override
    @Nonnull
    protected PathNavigation createNavigation(@Nonnull Level world) {
        return new ClimberGroundPathNavigator(this, world);
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.level.isClientSide) {
            this.setBesideClimbableBlock(this.horizontalCollision);
        }
    }

    @Override
    public boolean onClimbable() {
        return this.isBesideClimbableBlock();
    }

    private boolean isBesideClimbableBlock() {
        return (this.entityData.get(CLIMBING) & 1) != 0;
    }

    private void setBesideClimbableBlock(boolean isClimbing) {
        byte climbing = this.entityData.get(CLIMBING);

        if (isClimbing) {
            climbing = (byte) (climbing | 1);
        } else {
            climbing = (byte) (climbing & -2);
        }
        this.entityData.set(CLIMBING, climbing);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return null;
    }

    @Override
    protected void playStepSound(@Nonnull BlockPos pos, @Nonnull BlockState state) {
    }

    @Override
    public int getMaxSpawnClusterSize() {
        return 1;
    }

    @Override
    protected void populateDefaultEquipmentSlots(@Nonnull DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(AtumItems.POISON_DAGGER));
    }

    @Override
    public boolean doHurtTarget(@Nonnull Entity entity) {
        if (!super.doHurtTarget(entity)) {
            return false;
        } else {
            if (this.getItemBySlot(EquipmentSlot.MAINHAND).getItem() == AtumItems.POISON_DAGGER && entity instanceof LivingEntity) {
                entity.hurt(ASSASSINATED, (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE));
                (((LivingEntity) entity)).addEffect(new MobEffectInstance(MobEffects.POISON, 100, 1));
            }
            return true;
        }
    }

    @Override
    public void addAdditionalSaveData(@Nonnull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        if (this.markedTarget instanceof Player) {
            compound.putUUID("MarkedForDeathTarget", this.markedTarget.getUUID());
        }
    }

    @Override
    public void readAdditionalSaveData(@Nonnull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("MarkedForDeathTarget")) {
            Player playerEntity = this.level.getPlayerByUUID(compound.getUUID("MarkedForDeathTarget"));
            if (playerEntity != null) {
                this.markedTarget = playerEntity;
            }
        }
    }

    public static boolean canSpawn(EntityType<? extends BanditBaseEntity> banditBase, LevelAccessor world, MobSpawnType spawnReason, BlockPos pos, Random random) {
        return spawnReason == MobSpawnType.EVENT ? world.canSeeSkyFromBelowWater(pos) && world.getBrightness(LightLayer.BLOCK, pos) <= 8 : BanditBaseEntity.canSpawn(banditBase, world, spawnReason, pos, random);
    }

    public void setMarkedTarget(LivingEntity livingEntity) {
        this.markedTarget = livingEntity;
        this.goalSelector.addGoal(1, new MarkedForDeathGoal(this, livingEntity)); //Set goal initially
    }

    public static class MarkedForDeathGoal extends TargetGoal {
        protected LivingEntity markedTarget;

        public MarkedForDeathGoal(Mob mob, @Nullable LivingEntity markedTarget) {
            super(mob, false);
            this.markedTarget = markedTarget;
        }

        @Override
        protected double getFollowDistance() {
            return 128.0D;
        }

        @Override
        public boolean canContinueToUse() {
            LivingEntity target;
            LivingEntity revenge = this.mob.getLastHurtByMob();
            if (revenge != null) {
                target = revenge;
            } else {
                target = this.markedTarget;
                if (target == null) {
                    target = this.mob.getTarget();
                }
            }

            if (target == null) {
                return false;
            } else if (!target.isAlive()) {
                return false;
            } else {
                double distance = this.getFollowDistance();
                if (this.mob.distanceToSqr(target) > distance * distance) {
                    return false;
                } else {
                    if (target instanceof Player && ((Player) target).getAbilities().invulnerable) {
                        return false;
                    } else {
                        this.mob.setTarget(target);
                        return true;
                    }
                }
            }
        }

        @Override
        public boolean canUse() {
            return this.markedTarget != null;
        }

        @Override
        public void stop() {
            super.stop();
            this.markedTarget = null;
            ((AssassinEntity) this.mob).markedTarget = null;
        }
    }

    public static class AssassinMeleeAttackGoal extends MeleeAttackGoal {

        public AssassinMeleeAttackGoal(PathfinderMob creature, double speedIn, boolean useLongMemory) {
            super(creature, speedIn, useLongMemory);
        }

        @Override
        public void tick() {
            if (this.mob != null && this.mob.getTarget() != null) {
                super.tick();
            }
        }
    }
}