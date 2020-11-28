package com.teammetallurgy.atum.entity.animal;

import com.teammetallurgy.atum.entity.ai.goal.FollowFlockLeaderGoal;
import com.teammetallurgy.atum.init.AtumEntities;
import com.teammetallurgy.atum.init.AtumItems;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Stream;

public class QuailEntity extends AnimalEntity { //TODO Look at quail meat
    private static final Ingredient TEMPTATION_ITEMS = Ingredient.fromItems(AtumItems.EMMER_SEEDS, Items.WHEAT_SEEDS, Items.MELON_SEEDS, Items.PUMPKIN_SEEDS, Items.BEETROOT_SEEDS);
    public float wingRotation;
    public float destPos;
    public float oFlapSpeed;
    public float oFlap;
    public float wingRotDelta = 1.0F;
    public int timeUntilNextEgg = this.rand.nextInt(6000) + 6000;
    private QuailEntity flockLeader;
    private int groupSize = 1;

    public QuailEntity(EntityType<? extends QuailEntity> type, World world) {
        super(type, world);
        this.setPathPriority(PathNodeType.WATER, 0.0F);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.6D));
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, PlayerEntity.class, 8.0F, 1.4D, 1.6D));
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, WolfEntity.class, 10.0F, 1.3D, 1.6D));
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, DesertWolfEntity.class, 10.0F, 1.3D, 1.6D));
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, MonsterEntity.class, 4.0F, 1.0D, 1.4D));
        this.goalSelector.addGoal(3, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(4, new TemptGoal(this, 1.0D, false, TEMPTATION_ITEMS));
        this.goalSelector.addGoal(5, new FollowFlockLeaderGoal(this));
        this.goalSelector.addGoal(5, new FollowParentGoal(this, 1.1D));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(7, new LookAtGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
    }

    public static AttributeModifierMap.MutableAttribute getAttributes() {
        return MobEntity.func_233666_p_().createMutableAttribute(Attributes.MAX_HEALTH, 4.0D).createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.32D);
    }

    @Override
    protected float getStandingEyeHeight(@Nonnull Pose pose, @Nonnull EntitySize size) {
        return this.isChild() ? size.height * 0.5F : size.height;
    }

    @Override
    public QuailEntity func_241840_a(@Nonnull ServerWorld world, @Nonnull AgeableEntity ageableEntity) {
        return AtumEntities.QUAIL.create(world);
    }

    @Override
    public boolean isBreedingItem(@Nonnull ItemStack stack) {
        return TEMPTATION_ITEMS.test(stack);
    }

    public int getMaxFlockSize() {
        return 6;
    }

    public boolean hasFlockLeader() {
        return this.flockLeader != null && this.flockLeader.isAlive();
    }

    public QuailEntity addToFlock(QuailEntity quail) { //func_212803_a
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
        if (this.isFlockLeader() && this.world.rand.nextInt(200) == 1) {
            List<QuailEntity> list = this.world.getEntitiesWithinAABB(this.getClass(), this.getBoundingBox().grow(8.0D, 8.0D, 8.0D));
            if (list.size() <= 1) {
                this.groupSize = 1;
            }
        }
    }

    @Override
    public void livingTick() {
        super.livingTick();
        this.oFlap = this.wingRotation;
        this.oFlapSpeed = this.destPos;
        this.destPos = (float) ((double) this.destPos + (double) (this.onGround ? -1 : 4) * 0.3D);
        this.destPos = MathHelper.clamp(this.destPos, 0.0F, 1.0F);
        if (!this.onGround && this.wingRotDelta < 1.0F) {
            this.wingRotDelta = 1.0F;
        }

        this.wingRotDelta = (float) ((double) this.wingRotDelta * 0.9D);
        Vector3d vector3d = this.getMotion();
        if (!this.onGround && vector3d.y < 0.0D) {
            this.setMotion(vector3d.mul(1.0D, 0.6D, 1.0D));
        }

        this.wingRotation += this.wingRotDelta * 2.0F;
        if (!this.world.isRemote && this.isAlive() && !this.isChild() && --this.timeUntilNextEgg <= 0) {
            this.playSound(SoundEvents.ENTITY_CHICKEN_EGG, 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
            this.entityDropItem(AtumItems.QUAIL_EGG);
            this.timeUntilNextEgg = this.rand.nextInt(6000) + 6000;
        }
    }

    @Override
    public boolean onLivingFall(float distance, float damageMultiplier) {
        return false;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_CHICKEN_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(@Nonnull DamageSource damageSource) {
        return SoundEvents.ENTITY_CHICKEN_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_CHICKEN_DEATH;
    }

    @Override
    protected void playStepSound(@Nonnull BlockPos pos, @Nonnull BlockState block) {
        this.playSound(SoundEvents.ENTITY_CHICKEN_STEP, 0.15F, 1.0F);
    }

    @Override
    public void readAdditional(@Nonnull CompoundNBT compound) {
        super.readAdditional(compound);
        if (compound.contains("EggLayTime")) {
            this.timeUntilNextEgg = compound.getInt("EggLayTime");
        }
    }

    @Override
    public void writeAdditional(@Nonnull CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putInt("EggLayTime", this.timeUntilNextEgg);
    }

    @Override
    public void updatePassenger(@Nonnull Entity passenger) {
        super.updatePassenger(passenger);
        float f = MathHelper.sin(this.renderYawOffset * ((float)Math.PI / 180F));
        float f1 = MathHelper.cos(this.renderYawOffset * ((float)Math.PI / 180F));
        passenger.setPosition(this.getPosX() + (double)(0.1F * f), this.getPosYHeight(0.5D) + passenger.getYOffset() + 0.0D, this.getPosZ() - (double)(0.1F * f1));
        if (passenger instanceof LivingEntity) {
            ((LivingEntity)passenger).renderYawOffset = this.renderYawOffset;
        }
    }

    public boolean isFlockLeader() {
        return this.groupSize > 1;
    }

    public boolean inRangeOfFlockLeader() {
        return this.getDistanceSq(this.flockLeader) <= 121.0D;
    }

    public void moveToFlockLeader() {
        if (this.hasFlockLeader()) {
            this.getNavigator().tryMoveToEntityLiving(this.flockLeader, 1.0D);
        }
    }

    public void func_212810_a(Stream<QuailEntity> quails) {
        quails.limit(this.getMaxFlockSize() - this.groupSize).filter((q) -> {
            return q != this;
        }).forEach((quail) -> {
            quail.addToFlock(this);
        });
    }

    @Nullable
    public ILivingEntityData onInitialSpawn(@Nonnull IServerWorld world, @Nonnull DifficultyInstance difficulty, @Nonnull SpawnReason reason, @Nullable ILivingEntityData spawnData, @Nullable CompoundNBT dataTag) {
        if (spawnData == null) {
            spawnData = new GroupData(this);
        } else {
            this.addToFlock(((GroupData) spawnData).flockLeader);
        }
        return super.onInitialSpawn(world, difficulty, reason, spawnData, dataTag);
    }

    public static class GroupData extends AgeableData {
        public final QuailEntity flockLeader;

        public GroupData(QuailEntity flockLeader) {
            super(true);
            this.flockLeader = flockLeader;
        }
    }
}