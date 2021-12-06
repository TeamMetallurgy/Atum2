package com.teammetallurgy.atum.entity.ai.goal;

import com.teammetallurgy.atum.entity.animal.QuailEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.List;
import java.util.function.Predicate;

public class FollowFlockLeaderGoal extends Goal {
    private final QuailEntity taskOwner;
    private int navigateTimer;
    private int cooldown;

    public FollowFlockLeaderGoal(QuailEntity taskOwner) {
        this.taskOwner = taskOwner;
        this.cooldown = this.getNewCooldown(taskOwner);
    }

    protected int getNewCooldown(QuailEntity taskOwner) {
        return 200 + taskOwner.getRandom().nextInt(200) % 20;
    }

    @Override
    public boolean canUse() {
        if (this.taskOwner.isFlockLeader() || this.taskOwner.isBaby()) { //Added child check, since that's handled by FollowParentGoal
            return false;
        } else if (this.taskOwner.hasFlockLeader()) {
            return true;
        } else if (this.cooldown > 0) {
            --this.cooldown;
            return false;
        } else {
            this.cooldown = this.getNewCooldown(this.taskOwner);
            Predicate<QuailEntity> predicate = (quail) -> {
                return quail.canGroupGrow() || !quail.hasFlockLeader();
            };
            List<QuailEntity> list = this.taskOwner.level.getEntitiesOfClass(this.taskOwner.getClass(), this.taskOwner.getBoundingBox().inflate(8.0D, 8.0D, 8.0D), predicate);
            QuailEntity quail = list.stream().filter(QuailEntity::canGroupGrow).findAny().orElse(this.taskOwner);
            quail.addFollowers(list.stream().filter((quailEntity) -> {
                return !quailEntity.hasFlockLeader();
            }));
            return this.taskOwner.hasFlockLeader();
        }
    }

    @Override
    public boolean canContinueToUse() {
        return this.taskOwner.hasFlockLeader() && this.taskOwner.inRangeOfFlockLeader();
    }

    @Override
    public void start() {
        this.navigateTimer = 0;
    }

    @Override
    public void stop() {
        this.taskOwner.leaveGroup();
    }

    @Override
    public void tick() {
        if (--this.navigateTimer <= 0) {
            this.navigateTimer = 10;
            this.taskOwner.moveToFlockLeader();
        }
    }
}