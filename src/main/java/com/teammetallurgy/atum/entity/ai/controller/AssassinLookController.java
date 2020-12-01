package com.teammetallurgy.atum.entity.ai.controller;

import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.controller.LookController;

public class AssassinLookController extends LookController {

    public AssassinLookController(MobEntity mob) {
        super(mob);
    }

    @Override
    public void setLookPositionWithEntity(Entity entity, float deltaYaw, float deltaPitch) {
        if (entity != null) {
            super.setLookPositionWithEntity(entity, deltaYaw, deltaPitch);
        }
    }
}