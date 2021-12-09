package com.teammetallurgy.atum.client.model.entity;

import net.minecraft.client.model.AbstractZombieModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.monster.Monster;

import javax.annotation.Nonnull;

public class MonsterModel<T extends Monster> extends AbstractZombieModel<T> {

    public MonsterModel(ModelPart part) {
        super(part);
    }

    @Override
    public boolean isAggressive(@Nonnull T entity) {
        return entity.isAggressive();
    }
}