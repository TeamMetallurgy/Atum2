package com.teammetallurgy.atum.entity.ai;

import com.teammetallurgy.atum.entity.animal.DesertWolfEntity;
import net.minecraft.entity.ai.goal.FollowOwnerGoal;

public class GoalFollowOwnerWithoutSaddle extends FollowOwnerGoal {
    private final DesertWolfEntity wolf;

    public GoalFollowOwnerWithoutSaddle(DesertWolfEntity wolf, double followSpeed, float minDist, float maxDist) {
        super(wolf, followSpeed, minDist, maxDist);
        this.wolf = wolf;
    }

    @Override
    public boolean shouldExecute() {
        if (wolf.isTamed() && wolf.isAlpha()) {
            return false;
        }
        return super.shouldExecute();
    }
}