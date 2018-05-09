package com.teammetallurgy.atum.entity.ai;

import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.ai.EntityAIAttackRangedBow;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.item.ItemBow;

public class AtumEntityAIAttackRangedBow<T extends EntityMob & IRangedAttackMob> extends EntityAIAttackRangedBow {
    private T entity;

    public AtumEntityAIAttackRangedBow(T mob, double moveSpeedAmp, int attackCooldown, float maxAttackDistance) {
        super(mob, moveSpeedAmp, attackCooldown, maxAttackDistance);
        this.entity = mob;
    }

    @Override
    protected boolean isBowInMainhand() {
        return !this.entity.getHeldItemMainhand().isEmpty() && this.entity.getHeldItemMainhand().getItem() instanceof ItemBow;
    }
}