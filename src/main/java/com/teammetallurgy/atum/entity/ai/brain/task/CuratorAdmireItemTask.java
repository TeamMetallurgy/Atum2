package com.teammetallurgy.atum.entity.ai.brain.task;

import com.google.common.collect.ImmutableMap;
import com.teammetallurgy.atum.api.AtumAPI;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;

public class CuratorAdmireItemTask<E extends VillagerEntity> extends Task<E> {
    private final int admireTime;

    public CuratorAdmireItemTask(int admireTime) {
        super(ImmutableMap.of(MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM, MemoryModuleStatus.VALUE_PRESENT, MemoryModuleType.ADMIRING_ITEM, MemoryModuleStatus.VALUE_ABSENT, MemoryModuleType.ADMIRING_DISABLED, MemoryModuleStatus.VALUE_ABSENT, MemoryModuleType.DISABLE_WALK_TO_ADMIRE_ITEM, MemoryModuleStatus.VALUE_ABSENT));
        this.admireTime = admireTime;
    }

    @Override
    protected boolean shouldExecute(@Nonnull ServerWorld world, E owner) {
        ItemEntity itemEntity = owner.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM).get();
        return itemEntity.getItem().getItem().isIn(AtumAPI.Tags.RELIC_NON_DIRTY);
    }

    @Override
    protected void startExecuting(@Nonnull ServerWorld world, @Nonnull E entity, long gameTime) {
        entity.getBrain().replaceMemory(MemoryModuleType.ADMIRING_ITEM, true, this.admireTime);
    }
}