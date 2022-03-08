package com.teammetallurgy.atum.entity.animal;

import com.teammetallurgy.atum.blocks.stone.limestone.LimestoneBlock;
import com.teammetallurgy.atum.blocks.wood.DeadwoodLogBlock;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumLootTables;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.ForgeEventFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Random;

public class ScarabEntity extends Monster {
    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(ScarabEntity.class, EntityDataSerializers.INT);

    public ScarabEntity(EntityType<? extends ScarabEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(5, new AIHideInBlock(this));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this).setAlertOthers());
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 8.0D).add(Attributes.ATTACK_DAMAGE, 2.0D).add(Attributes.MOVEMENT_SPEED, 0.25D);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(VARIANT, 0);
    }

    @Override
    @Nullable
    public SpawnGroupData finalizeSpawn(@Nonnull ServerLevelAccessor world, @Nonnull DifficultyInstance difficulty, @Nonnull MobSpawnType spawnReason, @Nullable SpawnGroupData livingdata, @Nullable CompoundTag nbt) {
        livingdata = super.finalizeSpawn(world, difficulty, spawnReason, livingdata, nbt);
        if (random.nextDouble() <= 0.002D) {
            this.setVariant(1);
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(24.0D);
            this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(3.0D);
            this.heal(16); //Make sure Golden scarab have have full health on initial spawn
        } else {
            this.setVariant(0);
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(8.0D);
            this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(2.0D);
        }
        return livingdata;
    }

    private void setVariant(int variant) {
        this.entityData.set(VARIANT, variant);
    }

    public int getVariant() {
        return this.entityData.get(VARIANT);
    }

    @Override
    @Nonnull
    protected Entity.MovementEmission getMovementEmission() {
        return Entity.MovementEmission.EVENTS;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENDERMITE_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(@Nonnull DamageSource damageSource) {
        return SoundEvents.ENDERMITE_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENDERMITE_DEATH;
    }

    @Override
    protected void playStepSound(@Nonnull BlockPos pos, @Nonnull BlockState state) {
        this.playSound(SoundEvents.ENDERMITE_STEP, 0.15F, 1.0F);
    }

    @Override
    protected int getExperienceReward(@Nonnull Player player) {
        return this.getVariant() == 1 ? 30 : 3;
    }

    @Override
    @Nonnull
    protected ResourceLocation getDefaultLootTable() {
        return this.getVariant() == 1 ? AtumLootTables.SCARAB_GOLDEN : AtumLootTables.SCARAB;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level.isClientSide && this.entityData.isDirty()) {
            this.entityData.clearDirty();
        }
    }

    @Override
    public float getWalkTargetValue(@Nonnull BlockPos pos) {
        Block block = this.level.getBlockState(pos.below()).getBlock();
        return block == AtumBlocks.LIMESTONE.get() || block == AtumBlocks.DEADWOOD_LOG.get() ? 10.0F : super.getWalkTargetValue(pos);
    }

    @Override
    public float getEyeHeight(@Nonnull Pose pose) {
        return 0.1F;
    }

    @Override
    public double getMyRidingOffset() {
        return 0.1D;
    }

    public static boolean canSpawn(EntityType<ScarabEntity> scarab, LevelAccessor world, MobSpawnType spawnReason, BlockPos pos, Random random) {
        if (checkAnyLightMonsterSpawnRules(scarab, world, spawnReason, pos, random)) {
            Player player = world.getNearestPlayer((double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D, 5.0D, true);
            return player == null;
        } else {
            return false;
        }
    }

    @Override
    @Nonnull
    public MobType getMobType() {
        return MobType.ARTHROPOD;
    }

    @Override
    public void addAdditionalSaveData(@Nonnull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Variant", this.getVariant());
    }

    @Override
    public void readAdditionalSaveData(@Nonnull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setVariant(compound.getInt("Variant"));
    }

    static class AIHideInBlock extends RandomStrollGoal {
        private Direction facing;
        private boolean doMerge;

        AIHideInBlock(ScarabEntity scarab) {
            super(scarab, 1.0D, 10);
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            if (this.mob.getTarget() != null) {
                return false;
            } else if (!this.mob.getNavigation().isDone()) {
                return false;
            } else {
                Random random = this.mob.getRandom();

                if (ForgeEventFactory.getMobGriefingEvent(this.mob.level, this.mob) && random.nextInt(10) == 0) {
                    this.facing = Direction.getRandom(random);
                    BlockPos pos = (new BlockPos(this.mob.getX(), this.mob.getY() + 0.5D, this.mob.getZ())).relative(this.facing);
                    BlockState state = this.mob.level.getBlockState(pos);

                    if (state.getBlock() == AtumBlocks.LIMESTONE.get() || state.getBlock() == AtumBlocks.DEADWOOD_LOG.get()) {
                        this.doMerge = true;
                        return true;
                    }
                }
                this.doMerge = false;
                return super.canUse();
            }
        }

        @Override
        public boolean canContinueToUse() {
            return !this.doMerge && super.canContinueToUse();
        }

        @Override
        public void start() {
            if (!this.doMerge) {
                super.start();
            } else {
                Level world = this.mob.level;
                BlockPos pos = (new BlockPos(this.mob.getX(), this.mob.getY() + 0.5D, this.mob.getZ())).relative(this.facing);
                BlockState state = world.getBlockState(pos);

                if (state.getBlock() == AtumBlocks.LIMESTONE.get()) {
                    world.setBlock(pos, AtumBlocks.LIMESTONE.get().defaultBlockState().setValue(LimestoneBlock.HAS_SCARAB, true), 3);
                    this.mob.spawnAnim();
                    this.mob.discard();
                }
                if (state.getBlock() == AtumBlocks.DEADWOOD_LOG.get()) {
                    world.setBlock(pos, AtumBlocks.DEADWOOD_LOG.get().defaultBlockState().setValue(DeadwoodLogBlock.HAS_SCARAB, true), 3);
                    this.mob.spawnAnim();
                    this.mob.discard();
                }
            }
        }
    }
}