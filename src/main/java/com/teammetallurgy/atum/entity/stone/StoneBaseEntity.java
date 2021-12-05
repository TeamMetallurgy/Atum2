package com.teammetallurgy.atum.entity.stone;

import com.teammetallurgy.atum.entity.bandit.BanditBaseEntity;
import com.teammetallurgy.atum.entity.undead.UndeadBaseEntity;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.init.AtumStructures;
import com.teammetallurgy.atum.world.gen.structure.StructureHelper;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.util.*;
import net.minecraft.core.BlockPos;
import net.minecraft.world.*;
import net.minecraft.server.level.ServerLevel;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.MoveTowardsRestrictionGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.ServerLevelAccessor;

public class StoneBaseEntity extends Monster {
    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(StoneBaseEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Byte> PLAYER_CREATED = SynchedEntityData.defineId(StoneBaseEntity.class, EntityDataSerializers.BYTE);
    private int homeCheckTimer;

    public StoneBaseEntity(EntityType<? extends StoneBaseEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(4, new MoveTowardsRestrictionGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.applyEntityAI();
    }

    private void applyEntityAI() {
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, input -> !isPlayerCreated()));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, StoneBaseEntity.class, 10, true, false, input -> input instanceof StoneBaseEntity && (!((StoneBaseEntity) input).isPlayerCreated() && isPlayerCreated() || ((StoneBaseEntity) input).isPlayerCreated() && !isPlayerCreated())));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, UndeadBaseEntity.class, true));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, BanditBaseEntity.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Monster.class, 10, true, false, input -> input != null && this.isPlayerCreated() && !(input instanceof StoneBaseEntity) && input.getMobType() == MobType.UNDEAD));
    }

    public static AttributeSupplier.Builder getBaseAttributes() {
        return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED, 0.15D).add(Attributes.FOLLOW_RANGE, 16.0D);
    }

    void setFriendlyAttributes() {
        this.getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(24.0D);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(VARIANT, 0);
        this.entityData.define(PLAYER_CREATED, (byte) 0);
    }

    @Override
    @Nullable
    public SpawnGroupData finalizeSpawn(@Nonnull ServerLevelAccessor world, @Nonnull DifficultyInstance difficulty, @Nonnull MobSpawnType spawnReason, @Nullable SpawnGroupData livingdata, @Nullable CompoundTag nbt) {
        if (this.isPlayerCreated()) {
            this.setFriendlyAttributes();
        }
        return super.finalizeSpawn(world, difficulty, spawnReason, livingdata, nbt);
    }

    void setVariant(int variant) {
        this.entityData.set(VARIANT, variant);
    }

    public int getVariant() {
        return this.entityData.get(VARIANT);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level.isClientSide && this.entityData.isDirty()) {
            this.entityData.clearDirty();
        }
    }

    @Override
    public void remove() {
        if (!level.isClientSide && this.isPlayerCreated() && this.level.getDifficulty() == Difficulty.PEACEFUL) {
            //Don't set player created stone mobs as dead on peaceful
        } else {
            super.remove();
        }
    }

    @Override
    protected void customServerAiStep() {
        if (--this.homeCheckTimer <= 0) {
            this.homeCheckTimer = 70 + this.random.nextInt(50);

            if (!this.hasRestriction()) {
                this.restrictTo(this.blockPosition(), 16);
            }
        }
        super.customServerAiStep();
    }

    @Override
    @Nonnull
    protected InteractionResult mobInteract(Player player, @Nonnull InteractionHand hand) {
        ItemStack heldStack = player.getItemInHand(hand);

        if (heldStack.getItem() == AtumItems.KHNUMITE) {
            if (!player.abilities.instabuild) {
                heldStack.shrink(1);
            }

            if (!this.level.isClientSide) {
                this.heal(5.0F);
            }
            return InteractionResult.SUCCESS;
        } else {
            return super.mobInteract(player, hand);
        }
    }

    @Override
    public boolean canBeAffected(@Nonnull MobEffectInstance potionEffect) {
        return potionEffect.getEffect() != MobEffects.POISON && super.canBeAffected(potionEffect);
    }

    @Override
    public void knockback(float strength, double xRatio, double zRatio) {
        //Immune to knockback
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return !this.isPlayerCreated();
    }

    @Override
    protected int decreaseAirSupply(int air) {
        return air;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.STONE_STEP;
    }

    @Override
    protected SoundEvent getHurtSound(@Nonnull DamageSource damageSource) {
        return SoundEvents.STONE_HIT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.STONE_BREAK;
    }

    @Override
    @Nonnull
    protected SoundEvent getFallDamageSound(int height) {
        return height > 4 ? SoundEvents.HOSTILE_BIG_FALL : SoundEvents.STONE_FALL;
    }

    @Override
    public int getAmbientSoundInterval() {
        return 120;
    }

    @Override
    public boolean causeFallDamage(float distance, float damageMultiplier) {
        return false;
    }

    @Override
    protected void playStepSound(@Nonnull BlockPos pos, @Nonnull BlockState state) {
        this.playSound(SoundEvents.STONE_STEP, 0.15F, 1.0F);
    }

    public static boolean isValidLightLevel(LevelAccessor world, @Nonnull BlockPos pos, Random random) {
        return world.getBrightness(LightLayer.SKY, pos) == 0 && world.getMaxLocalRawBrightness(pos) <= random.nextInt(10);
    }

    public static boolean canSpawn(EntityType<? extends StoneBaseEntity> stoneBase, LevelAccessor world, MobSpawnType spawnReason, BlockPos pos, Random random) {
        return isValidLightLevel(world, pos, random) && checkAnyLightMonsterSpawnRules(stoneBase, world, spawnReason, pos, random) && world instanceof ServerLevel &&
                !StructureHelper.doesChunkHaveStructure((ServerLevel) world, pos, AtumStructures.PYRAMID_STRUCTURE);
    }

    boolean isPlayerCreated() {
        return (this.entityData.get(PLAYER_CREATED) & 1) != 0;
    }

    public void setPlayerCreated(boolean playerCreated) {
        byte b = this.entityData.get(PLAYER_CREATED);

        if (playerCreated) {
            this.entityData.set(PLAYER_CREATED, (byte) (b | 1));
        } else {
            this.entityData.set(PLAYER_CREATED, (byte) (b & -2));
        }
    }

    @Override
    public void addAdditionalSaveData(@Nonnull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Variant", this.getVariant());
        compound.putBoolean("PlayerCreated", this.isPlayerCreated());
    }

    @Override
    public void readAdditionalSaveData(@Nonnull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setVariant(compound.getInt("Variant"));
        this.setPlayerCreated(compound.getBoolean("PlayerCreated"));
    }
}