package com.teammetallurgy.atum.entity.ai.brain.task;

import com.google.common.collect.ImmutableMap;
import com.teammetallurgy.atum.entity.villager.AtumVillagerEntity;
import com.teammetallurgy.atum.entity.villager.AtumVillagerProfession;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.Villager;

public class SwitchAtumVillagerJobTask extends Behavior<Villager> {
    final AtumVillagerProfession profession;

    public SwitchAtumVillagerJobTask(AtumVillagerProfession profession) {
        super(ImmutableMap.of(MemoryModuleType.JOB_SITE, MemoryStatus.VALUE_PRESENT, MemoryModuleType.LIVING_ENTITIES, MemoryStatus.VALUE_PRESENT));
        this.profession = profession;
    }

    @Override
    protected void start(ServerLevel worldIn, Villager entity, long gameTimeIn) {
        GlobalPos globalpos = entity.getBrain().getMemory(MemoryModuleType.JOB_SITE).get();
        worldIn.getPoiManager().getType(globalpos.pos()).ifPresent((p_233933_3_) -> {
            BehaviorUtils.getNearbyVillagersWithCondition(entity, (p_233935_3_) -> {
                return this.competesForSameJobsite(globalpos, p_233933_3_, p_233935_3_);
            }).reduce(entity, SwitchAtumVillagerJobTask::selectWinner);
        });
    }

    private static Villager selectWinner(Villager p_233932_0_, Villager p_233932_1_) {
        Villager villagerentity;
        Villager villagerentity1;
        if (p_233932_0_.getVillagerXp() > p_233932_1_.getVillagerXp()) {
            villagerentity = p_233932_0_;
            villagerentity1 = p_233932_1_;
        } else {
            villagerentity = p_233932_1_;
            villagerentity1 = p_233932_0_;
        }
        villagerentity1.getBrain().eraseMemory(MemoryModuleType.JOB_SITE);
        return villagerentity;
    }

    private boolean competesForSameJobsite(GlobalPos globalPos, PoiType poiType, Villager entity) {
        return entity instanceof AtumVillagerEntity && this.hasJobSite(entity) && globalPos.equals(entity.getBrain().getMemory(MemoryModuleType.JOB_SITE).get()) && this.hasMatchingProfession(poiType, ((AtumVillagerEntity) entity).getAtumVillagerData().getAtumProfession());
    }

    private boolean hasMatchingProfession(PoiType poiType, AtumVillagerProfession profession) {
        return profession.getPointOfInterest().getPredicate().test(poiType);
    }

    private boolean hasJobSite(Villager p_233931_1_) {
        return p_233931_1_.getBrain().getMemory(MemoryModuleType.JOB_SITE).isPresent();
    }
}