package com.teammetallurgy.atum.entity.ai;

import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.ai.EntityAIAttackRangedBow;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.item.ItemBow;

public class AIBowAttack<T extends EntityMob & IRangedAttackMob> extends EntityAIAttackRangedBow {
    private final T entity;

    public AIBowAttack(T mob, double moveSpeedAmp, int attackCooldown, float maxAttackDistanceIn) {
        super(mob, moveSpeedAmp, attackCooldown, maxAttackDistanceIn);
        this.entity = mob;
    }

    protected boolean isBowInMainhand() {
        return !this.entity.getHeldItemMainhand().isEmpty() && this.entity.getHeldItemMainhand().getItem() instanceof ItemBow;
    }
}