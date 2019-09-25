package com.teammetallurgy.atum.entity.ai;

import net.minecraft.entity.ai.goal.SitGoal;
import net.minecraft.entity.passive.TameableEntity;

public class GoalSitWithCheck extends SitGoal {
    private final boolean canSit;

    public GoalSitWithCheck(TameableEntity entity, boolean canSit) {
        super(entity);
        this.canSit = canSit;
    }

    @Override
    public boolean shouldExecute() {
        return this.canSit && super.shouldExecute();
    }
}