package com.teammetallurgy.atum.entity.ai.brain.task;

import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.npc.Villager;
import net.minecraftforge.common.ToolActions;

import javax.annotation.Nonnull;

public class CuratorStartAdmiringItemTask <E extends Villager> extends Behavior<E> {

    public CuratorStartAdmiringItemTask() {
        super(ImmutableMap.of(MemoryModuleType.ADMIRING_ITEM, MemoryStatus.VALUE_ABSENT));
    }

    @Override
    protected boolean checkExtraStartConditions(@Nonnull ServerLevel world, @Nonnull E owner) {
        return !owner.getOffhandItem().isEmpty() && !owner.getOffhandItem().canPerformAction(ToolActions.SHIELD_BLOCK);
    }

    @Override
    protected void start(@Nonnull ServerLevel world, @Nonnull E entity, long gameTimeIn) {
        AtumVillagerGoalPackages.trade(entity, true);
    }
}