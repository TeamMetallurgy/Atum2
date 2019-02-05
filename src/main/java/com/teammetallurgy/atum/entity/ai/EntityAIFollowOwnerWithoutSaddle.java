package com.teammetallurgy.atum.entity.ai;

import com.teammetallurgy.atum.entity.animal.EntityDesertWolf;
import net.minecraft.entity.ai.EntityAIFollowOwner;

public class EntityAIFollowOwnerWithoutSaddle extends EntityAIFollowOwner {
    private final EntityDesertWolf wolf;

    public EntityAIFollowOwnerWithoutSaddle(EntityDesertWolf tameableIn, double followSpeedIn, float minDistIn, float maxDistIn) {
        super(tameableIn, followSpeedIn, minDistIn, maxDistIn);
        this.wolf = tameableIn;
    }

    @Override
    public boolean shouldExecute() {
        if (wolf.isSaddled()) return false;
        return super.shouldExecute();
    }
}
