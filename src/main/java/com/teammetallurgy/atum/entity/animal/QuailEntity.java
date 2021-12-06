package com.teammetallurgy.atum.entity.animal;

import com.teammetallurgy.atum.entity.ai.goal.FollowFlockLeaderGoal;
import com.teammetallurgy.atum.init.AtumEntities;
import com.teammetallurgy.atum.init.AtumItems;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Stream;

public class QuailEntity extends Animal {
    private static final Ingredient TEMPTATION_ITEMS = Ingredient.of(AtumItems.EMMER_SEEDS, Items.WHEAT_SEEDS, Items.MELON_SEEDS, Items.PUMPKIN_SEEDS, Items.BEETROOT_SEEDS);
    public float wingRotation;
    public float destPos;
    public float oFlapSpeed;
    public float oFlap;
    public float wingRotDelta = 1.0F;
    public int timeUntilNextEgg = this.random.nextInt(6000) + 6000;
    private QuailEntity flockLeader;
    private int groupSize = 1;

    public QuailEntity(EntityType<? extends QuailEntity> type, Level world) {
        super(type, world);
        this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.6D));
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, Player.class, 8.0F, 1.4D, 1.6D));
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, Wolf.class, 10.0F, 1.3D, 1.6D));
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, DesertWolfEntity.class, 10.0F, 1.3D, 1.6D));
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, Monster.class, 4.0F, 1.0D, 1.4D));
        this.goalSelector.addGoal(3, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(4, new TemptGoal(this, 1.0D, TEMPTATION_ITEMS, false));
        this.goalSelector.addGoal(5, new FollowFlockLeaderGoal(this));
        this.goalSelector.addGoal(5, new FollowParentGoal(this, 1.1D));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 4.0D).add(Attributes.MOVEMENT_SPEED, 0.32D);
    }

    @Override
    protected float getStandingEyeHeight(@Nonnull Pose pose, @Nonnull EntityDimensions size) {
        return this.isBaby() ? size.height * 0.5F : size.height;
    }

    @Override
    public QuailEntity getBreedOffspring(@Nonnull ServerLevel world, @Nonnull AgeableMob ageableEntity) {
        return AtumEntities.QUAIL.create(world);
    }

    @Override
    public boolean isFood(@Nonnull ItemStack stack) {
        return TEMPTATION_ITEMS.test(stack);
    }

    public int getMaxFlockSize() {
        return 6;
    }

    public boolean hasFlockLeader() {
        return this.flockLeader != null && this.flockLeader.isAlive();
    }

    public QuailEntity addToFlock(QuailEntity quail) { //startFollowing
        this.flockLeader = quail;
        quail.increaseGroupSize();
        return quail;
    }

    public void leaveGroup() {
        this.flockLeader.decreaseGroupSize();
        this.flockLeader = null;
    }

    private void increaseGroupSize() {
        ++this.groupSize;
    }

    private void decreaseGroupSize() {
        --this.groupSize;
    }

    public boolean canGroupGrow() {
        return this.isFlockLeader() && this.groupSize < this.getMaxFlockSize();
    }

    @Override
    public void tick() {
        super.tick();
        if (this.isFlockLeader() && this.level.random.nextInt(200) == 1) {
            List<QuailEntity> list = this.level.getEntitiesOfClass(QuailEntity.class, this.getBoundingBox().inflate(8.0D, 8.0D, 8.0D));
            if (list.size() <= 1) {
                this.groupSize = 1;
            }
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();
        this.oFlap = this.wingRotation;
        this.oFlapSpeed = this.destPos;
        this.destPos = (float) ((double) this.destPos + (double) (this.onGround ? -1 : 4) * 0.3D);
        this.destPos = Mth.clamp(this.destPos, 0.0F, 1.0F);
        if (!this.onGround && this.wingRotDelta < 1.0F) {
            this.wingRotDelta = 1.0F;
        }

        this.wingRotDelta = (float) ((double) this.wingRotDelta * 0.9D);
        Vec3 vector3d = this.getDeltaMovement();
        if (!this.onGround && vector3d.y < 0.0D) {
            this.setDeltaMovement(vector3d.multiply(1.0D, 0.6D, 1.0D));
        }

        this.wingRotation += this.wingRotDelta * 2.0F;
        if (!this.level.isClientSide && this.isAlive() && !this.isBaby() && --this.timeUntilNextEgg <= 0) {
            this.playSound(SoundEvents.CHICKEN_EGG, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
            this.spawnAtLocation(AtumItems.QUAIL_EGG);
            this.timeUntilNextEgg = this.random.nextInt(6000) + 6000;
        }
    }

    @Override
    public boolean causeFallDamage(float distance, float damageMultiplier, @Nonnull DamageSource source) {
        return false;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.CHICKEN_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(@Nonnull DamageSource damageSource) {
        return SoundEvents.CHICKEN_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.CHICKEN_DEATH;
    }

    @Override
    protected void playStepSound(@Nonnull BlockPos pos, @Nonnull BlockState block) {
        this.playSound(SoundEvents.CHICKEN_STEP, 0.15F, 1.0F);
    }

    @Override
    public void readAdditionalSaveData(@Nonnull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("EggLayTime")) {
            this.timeUntilNextEgg = compound.getInt("EggLayTime");
        }
    }

    @Override
    public void addAdditionalSaveData(@Nonnull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("EggLayTime", this.timeUntilNextEgg);
    }

    @Override
    public void positionRider(@Nonnull Entity passenger) {
        super.positionRider(passenger);
        float f = Mth.sin(this.yBodyRot * ((float)Math.PI / 180F));
        float f1 = Mth.cos(this.yBodyRot * ((float)Math.PI / 180F));
        passenger.setPos(this.getX() + (double)(0.1F * f), this.getY(0.5D) + passenger.getMyRidingOffset() + 0.0D, this.getZ() - (double)(0.1F * f1));
        if (passenger instanceof LivingEntity) {
            ((LivingEntity)passenger).yBodyRot = this.yBodyRot;
        }
    }

    public boolean isFlockLeader() {
        return this.groupSize > 1;
    }

    public boolean inRangeOfFlockLeader() {
        return this.distanceToSqr(this.flockLeader) <= 121.0D;
    }

    public void moveToFlockLeader() {
        if (this.hasFlockLeader()) {
            this.getNavigation().moveTo(this.flockLeader, 1.0D);
        }
    }

    public void addFollowers(Stream<QuailEntity> quails) {
        quails.limit(this.getMaxFlockSize() - this.groupSize).filter((q) -> {
            return q != this;
        }).forEach((quail) -> {
            quail.addToFlock(this);
        });
    }

    @Nullable
    public SpawnGroupData finalizeSpawn(@Nonnull ServerLevelAccessor world, @Nonnull DifficultyInstance difficulty, @Nonnull MobSpawnType reason, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag dataTag) {
        if (spawnData == null) {
            spawnData = new GroupData(this);
        } else {
            this.addToFlock(((GroupData) spawnData).flockLeader);
        }
        return super.finalizeSpawn(world, difficulty, reason, spawnData, dataTag);
    }

    public static class GroupData implements SpawnGroupData {
        public final QuailEntity flockLeader;

        public GroupData(QuailEntity flockLeader) {
            this.flockLeader = flockLeader;
        }
    }
}