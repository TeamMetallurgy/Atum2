package com.teammetallurgy.atum.entity.ai.goal;

import net.minecraft.entity.ai.goal.SitGoal;
import net.minecraft.entity.passive.TameableEntity;

public class SitWithCheckGoal extends SitGoal {
    private final boolean canSit;

    public SitWithCheckGoal(TameableEntity entity, boolean canSit) {
        super(entity);
        this.canSit = canSit;
    }

    @Override
    public boolean shouldExecute() {
        return this.canSit && super.shouldExecute();
    }
}