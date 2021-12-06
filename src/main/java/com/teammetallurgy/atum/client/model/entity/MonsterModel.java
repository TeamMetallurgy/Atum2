package com.teammetallurgy.atum.client.model.entity;

import net.minecraft.client.model.AbstractZombieModel;
import net.minecraft.world.entity.monster.Monster;

import javax.annotation.Nonnull;

public class MonsterModel<T extends Monster> extends AbstractZombieModel<T> {

    public MonsterModel(float boxScale, boolean b) {
        this(boxScale, 0.0F, 64, b ? 32 : 64);
    }

    public MonsterModel(float boxScale, float yRotation, int textureWidth, int textureHeight) {
        super(boxScale, yRotation, textureWidth, textureHeight);
    }

    @Override
    public boolean isAggressive(@Nonnull T entity) {
        return entity.isAggressive();
    }
}