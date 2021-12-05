package com.teammetallurgy.atum.entity.ai.goal;

import com.teammetallurgy.atum.entity.animal.DesertWolfEntity;
import net.minecraft.world.entity.ai.goal.FollowOwnerGoal;

public class FollowOwnerWithoutSaddleGoal extends FollowOwnerGoal {
    private final DesertWolfEntity wolf;

    public FollowOwnerWithoutSaddleGoal(DesertWolfEntity wolf, double followSpeed, float minDist, float maxDist) {
        super(wolf, followSpeed, minDist, maxDist, false);
        this.wolf = wolf;
    }

    @Override
    public boolean canUse() {
        if (this.wolf.isTame() && this.wolf.isAlpha()) {
            return false;
        }
        return super.canUse();
    }
}