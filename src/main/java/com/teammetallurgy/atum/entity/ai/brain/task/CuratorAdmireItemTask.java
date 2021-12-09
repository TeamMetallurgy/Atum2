package com.teammetallurgy.atum.entity.ai.brain.task;

import com.google.common.collect.ImmutableMap;
import com.teammetallurgy.atum.api.AtumAPI;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.npc.Villager;

import javax.annotation.Nonnull;

public class CuratorAdmireItemTask<E extends Villager> extends Behavior<E> {
    private final int admireTime;

    public CuratorAdmireItemTask(int admireTime) {
        super(ImmutableMap.of(MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM, MemoryStatus.VALUE_PRESENT, MemoryModuleType.ADMIRING_ITEM, MemoryStatus.VALUE_ABSENT, MemoryModuleType.ADMIRING_DISABLED, MemoryStatus.VALUE_ABSENT, MemoryModuleType.DISABLE_WALK_TO_ADMIRE_ITEM, MemoryStatus.VALUE_ABSENT));
        this.admireTime = admireTime;
    }

    @Override
    protected boolean checkExtraStartConditions(@Nonnull ServerLevel world, E owner) {
        ItemEntity itemEntity = owner.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM).get();
        return AtumAPI.Tags.RELIC_NON_DIRTY.contains(itemEntity.getItem().getItem());
    }

    @Override
    protected void start(@Nonnull ServerLevel world, @Nonnull E entity, long gameTime) {
        entity.getBrain().setMemoryWithExpiry(MemoryModuleType.ADMIRING_ITEM, true, this.admireTime);
    }
}