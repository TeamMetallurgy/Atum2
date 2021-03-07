package com.teammetallurgy.atum.entity.ai.brain.task;

import com.google.common.collect.ImmutableMap;
import com.teammetallurgy.atum.entity.villager.AtumVillagerProfession;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;

public class CuratorStartAdmiringItemTask <E extends VillagerEntity> extends Task<E> {

    public CuratorStartAdmiringItemTask() {
        super(ImmutableMap.of(MemoryModuleType.ADMIRING_ITEM, MemoryModuleStatus.VALUE_ABSENT));
    }

    @Override
    protected boolean shouldExecute(@Nonnull ServerWorld world, @Nonnull E owner) {
        return !owner.getHeldItemOffhand().isEmpty() && !owner.getHeldItemOffhand().isShield(owner);
    }

    @Override
    protected void startExecuting(@Nonnull ServerWorld world, @Nonnull E entity, long gameTimeIn) {
        AtumVillagerTasks.trade(entity, true);
    }
}