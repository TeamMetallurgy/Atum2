package com.teammetallurgy.atum.entity.ai;

import com.teammetallurgy.atum.entity.animal.EntityDesertWolf;
import net.minecraft.entity.ai.EntityAIFollowOwner;

public class EntityAIFollowOwnerWithoutSaddle extends EntityAIFollowOwner {
    private final EntityDesertWolf wolf;

    public EntityAIFollowOwnerWithoutSaddle(EntityDesertWolf wolf, double followSpeed, float minDist, float maxDist) {
        super(wolf, followSpeed, minDist, maxDist);
        this.wolf = wolf;
    }

    @Override
    public boolean shouldExecute() {
        if (wolf.isSaddled()) return false;
        return super.shouldExecute();
    }
}