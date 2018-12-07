package com.teammetallurgy.atum.entity.ai;

import net.minecraft.entity.ai.EntityAISit;
import net.minecraft.entity.passive.EntityTameable;

public class AISitWithCheck<T extends EntityTameable> extends EntityAISit {
    private final boolean canSit;

    public AISitWithCheck(EntityTameable entity, boolean canSit) {
        super(entity);
        this.canSit = canSit;
    }

    @Override
    public boolean shouldExecute() {
        return this.canSit && super.shouldExecute();
    }
}