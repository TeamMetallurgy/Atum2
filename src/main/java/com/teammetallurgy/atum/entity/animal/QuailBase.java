package com.teammetallurgy.atum.entity.animal;

import com.teammetallurgy.atum.entity.ai.goal.FollowFlockLeaderGoal;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Stream;

public abstract class QuailBase extends Animal { //Needed to get Follow Goal working properly
    private QuailBase flockLeader;
    private int groupSize = 1;

    protected QuailBase(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.6D));
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, Player.class, 8.0F, 1.4D, 1.6D));
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, Wolf.class, 10.0F, 1.3D, 1.6D));
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, DesertWolfEntity.class, 10.0F, 1.3D, 1.6D));
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, Monster.class, 4.0F, 1.0D, 1.4D));
        this.goalSelector.addGoal(5, new FollowFlockLeaderGoal(this));
        this.goalSelector.addGoal(5, new FollowParentGoal(this, 1.1D));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
    }

    @Override
    public void tick() {
        super.tick();
        if (this.isFlockLeader() && this.level().random.nextInt(200) == 1) {
            List<? extends QuailBase> list = this.level().getEntitiesOfClass(QuailBase.class, this.getBoundingBox().inflate(8.0D, 8.0D, 8.0D));
            if (list.size() <= 1) {
                this.groupSize = 1;
            }
        }
    }

    public int getMaxFlockSize() {
        return 6;
    }

    public boolean hasFlockLeader() {
        return this.flockLeader != null && this.flockLeader.isAlive();
    }

    public QuailBase addToFlock(QuailBase quail) { //startFollowing
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

    public void addFollowers(Stream<? extends QuailBase> quails) {
        quails.limit(this.getMaxFlockSize() - this.groupSize).filter((q) -> q != this).forEach((quail) -> {
            quail.addToFlock(this);
        });
    }

    @Override
    @Nullable
    public SpawnGroupData finalizeSpawn(@Nonnull ServerLevelAccessor level, @Nonnull DifficultyInstance difficulty, @Nonnull MobSpawnType reason, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag dataTag) {
        if (spawnData == null) {
            spawnData = new QuailBase.GroupData(this);
        } else {
            this.addToFlock(((QuailBase.GroupData) spawnData).flockLeader);
        }
        return super.finalizeSpawn(level, difficulty, reason, spawnData, dataTag);
    }

    public static class GroupData extends AgeableMobGroupData {
        public final QuailBase flockLeader;

        public GroupData(QuailBase flockLeader) {
            super(true);
            this.flockLeader = flockLeader;
        }
    }
}