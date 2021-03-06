package com.teammetallurgy.atum.entity.ai.brain.task;

import com.google.common.collect.ImmutableMap;
import com.teammetallurgy.atum.entity.villager.AtumVillagerEntity;
import com.teammetallurgy.atum.entity.villager.AtumVillagerProfession;
import net.minecraft.entity.ai.brain.BrainUtil;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.village.PointOfInterestType;
import net.minecraft.world.server.ServerWorld;

public class SwitchAtumVillagerJobTask extends Task<VillagerEntity> {
    final AtumVillagerProfession profession;

    public SwitchAtumVillagerJobTask(AtumVillagerProfession profession) {
        super(ImmutableMap.of(MemoryModuleType.JOB_SITE, MemoryModuleStatus.VALUE_PRESENT, MemoryModuleType.MOBS, MemoryModuleStatus.VALUE_PRESENT));
        this.profession = profession;
    }

    @Override
    protected void startExecuting(ServerWorld worldIn, VillagerEntity entity, long gameTimeIn) {
        GlobalPos globalpos = entity.getBrain().getMemory(MemoryModuleType.JOB_SITE).get();
        worldIn.getPointOfInterestManager().getType(globalpos.getPos()).ifPresent((p_233933_3_) -> {
            BrainUtil.getNearbyVillagers(entity, (p_233935_3_) -> {
                return this.func_233934_a_(globalpos, p_233933_3_, p_233935_3_);
            }).reduce(entity, SwitchAtumVillagerJobTask::func_233932_a_);
        });
    }

    private static VillagerEntity func_233932_a_(VillagerEntity p_233932_0_, VillagerEntity p_233932_1_) {
        VillagerEntity villagerentity;
        VillagerEntity villagerentity1;
        if (p_233932_0_.getXp() > p_233932_1_.getXp()) {
            villagerentity = p_233932_0_;
            villagerentity1 = p_233932_1_;
        } else {
            villagerentity = p_233932_1_;
            villagerentity1 = p_233932_0_;
        }
        villagerentity1.getBrain().removeMemory(MemoryModuleType.JOB_SITE);
        return villagerentity;
    }

    private boolean func_233934_a_(GlobalPos globalPos, PointOfInterestType poiType, VillagerEntity entity) {
        return entity instanceof AtumVillagerEntity && this.func_233931_a_(entity) && globalPos.equals(entity.getBrain().getMemory(MemoryModuleType.JOB_SITE).get()) && this.func_233930_a_(poiType, ((AtumVillagerEntity) entity).getAtumVillagerData().getAtumProfession());
    }

    private boolean func_233930_a_(PointOfInterestType poiType, AtumVillagerProfession profession) {
        return profession.getPointOfInterest().getPredicate().test(poiType);
    }

    private boolean func_233931_a_(VillagerEntity p_233931_1_) {
        return p_233931_1_.getBrain().getMemory(MemoryModuleType.JOB_SITE).isPresent();
    }
}