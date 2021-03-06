package com.teammetallurgy.atum.entity.ai.brain.task;

import com.google.common.collect.ImmutableMap;
import com.teammetallurgy.atum.entity.villager.AtumVillagerData;
import com.teammetallurgy.atum.entity.villager.AtumVillagerEntity;
import com.teammetallurgy.atum.entity.villager.AtumVillagerProfession;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.merchant.villager.VillagerData;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;

public class AtumChangeJobTask extends Task<VillagerEntity> {
    public AtumChangeJobTask() {
        super(ImmutableMap.of(MemoryModuleType.JOB_SITE, MemoryModuleStatus.VALUE_ABSENT));
    }

    @Override
    protected boolean shouldExecute(@Nonnull ServerWorld world, @Nonnull VillagerEntity owner) {
        if (owner instanceof AtumVillagerEntity) {
            AtumVillagerData villagerData = ((AtumVillagerEntity) owner).getAtumVillagerData();
            return villagerData.getAtumProfession() != AtumVillagerProfession.NONE.get() && villagerData.getAtumProfession() != AtumVillagerProfession.NITWIT.get() && owner.getXp() == 0 && villagerData.getLevel() <= 1;
        } else {
            return false;
        }
    }

    @Override
    protected void startExecuting(@Nonnull ServerWorld world, @Nonnull VillagerEntity entity, long gameTime) {
        if (entity instanceof AtumVillagerEntity) {
            entity.setVillagerData(((AtumVillagerEntity) entity).getAtumVillagerData().withProfession(AtumVillagerProfession.NONE.get()));
            entity.resetBrain(world);
        }
    }
}