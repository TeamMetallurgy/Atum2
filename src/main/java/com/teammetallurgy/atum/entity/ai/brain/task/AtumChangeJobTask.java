package com.teammetallurgy.atum.entity.ai.brain.task;

import com.google.common.collect.ImmutableMap;
import com.teammetallurgy.atum.entity.villager.AtumVillagerData;
import com.teammetallurgy.atum.entity.villager.AtumVillagerEntity;
import com.teammetallurgy.atum.init.AtumVillagerProfession;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.npc.Villager;

import javax.annotation.Nonnull;

public class AtumChangeJobTask extends Behavior<Villager> {
    public AtumChangeJobTask() {
        super(ImmutableMap.of(MemoryModuleType.JOB_SITE, MemoryStatus.VALUE_ABSENT));
    }

    @Override
    protected boolean checkExtraStartConditions(@Nonnull ServerLevel world, @Nonnull Villager owner) {
        if (owner instanceof AtumVillagerEntity) {
            AtumVillagerData villagerData = ((AtumVillagerEntity) owner).getAtumVillagerData();
            return villagerData.getAtumProfession() != AtumVillagerProfession.NONE.get() && villagerData.getAtumProfession() != AtumVillagerProfession.NITWIT.get() && owner.getVillagerXp() == 0 && villagerData.getLevel() <= 1;
        } else {
            return false;
        }
    }

    @Override
    protected void start(@Nonnull ServerLevel world, @Nonnull Villager entity, long gameTime) {
        if (entity instanceof AtumVillagerEntity) {
            ((AtumVillagerEntity) entity).setAtumVillagerData(((AtumVillagerEntity) entity).getAtumVillagerData().withProfession(AtumVillagerProfession.NONE.get()));
            entity.refreshBrain(world);
        }
    }
}