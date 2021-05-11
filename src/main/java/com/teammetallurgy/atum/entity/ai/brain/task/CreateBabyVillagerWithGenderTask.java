package com.teammetallurgy.atum.entity.ai.brain.task;

import com.teammetallurgy.atum.entity.villager.AtumVillagerEntity;
import com.teammetallurgy.atum.init.AtumEntities;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.BrainUtil;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.CreateBabyVillagerTask;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;
import java.util.Optional;

public class CreateBabyVillagerWithGenderTask extends CreateBabyVillagerTask {

    @Override
    protected boolean shouldExecute(@Nonnull ServerWorld world, @Nonnull VillagerEntity owner) {
        return this.canBreed((AtumVillagerEntity) owner);
    }

    private boolean canBreed(AtumVillagerEntity villager) {
        Brain<VillagerEntity> brain = villager.getBrain();
        Optional<AgeableEntity> optional = brain.getMemory(MemoryModuleType.BREED_TARGET).filter((breedTarget) -> {
            return (villager.getType() == AtumEntities.VILLAGER_MALE && breedTarget.getType() == AtumEntities.VILLAGER_FEMALE) || villager.getType() == AtumEntities.VILLAGER_FEMALE && breedTarget.getType() == AtumEntities.VILLAGER_MALE;
        });
        if (!optional.isPresent()) {
            return false;
        } else {
            return BrainUtil.isCorrectVisibleType(brain, MemoryModuleType.BREED_TARGET, villager.getType()) && villager.canBreed() && optional.get().canBreed();
        }
    }
}