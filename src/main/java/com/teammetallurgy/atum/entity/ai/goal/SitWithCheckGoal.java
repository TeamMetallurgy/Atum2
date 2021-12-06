package com.teammetallurgy.atum.entity.ai.goal;

import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;

public class SitWithCheckGoal extends SitWhenOrderedToGoal {
    private final boolean canSit;

    public SitWithCheckGoal(TamableAnimal entity, boolean canSit) {
        super(entity);
        this.canSit = canSit;
    }

    @Override
    public boolean canUse() {
        return this.canSit && super.canUse();
    }
}