package com.teammetallurgy.atum.entity.ai.brain.task;

import com.teammetallurgy.atum.entity.villager.AtumVillagerEntity;
import com.teammetallurgy.atum.init.AtumEntities;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.BrainUtil;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.CreateBabyVillagerTask;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.function.Predicate;

public class CreateBabyVillagerWithGenderTask extends CreateBabyVillagerTask {

    @Override
    protected boolean shouldExecute(@Nonnull ServerWorld world, @Nonnull VillagerEntity owner) {
        return this.canBreed((AtumVillagerEntity) owner);
    }

    @Override
    protected boolean shouldContinueExecuting(@Nonnull ServerWorld world, @Nonnull VillagerEntity entity, long gameTime) {
        return gameTime <= this.duration && this.canBreed((AtumVillagerEntity) entity);
    }

    private boolean canBreed(AtumVillagerEntity villager) {
        Brain<VillagerEntity> brain = villager.getBrain();
        Optional<AgeableEntity> optional = brain.getMemory(MemoryModuleType.BREED_TARGET).filter((breedTarget) -> {
            return (villager.getType() == AtumEntities.VILLAGER_MALE && breedTarget.getType() == AtumEntities.VILLAGER_FEMALE) || villager.getType() == AtumEntities.VILLAGER_FEMALE && breedTarget.getType() == AtumEntities.VILLAGER_MALE;
        });
        if (!optional.isPresent()) {
            return false;
        } else {
            return isCorrectVisibleType(brain, MemoryModuleType.BREED_TARGET, villager.getType()) && villager.canBreed() && optional.get().canBreed();
        }
    }

    public static boolean isCorrectVisibleType(Brain<?> brains, MemoryModuleType<? extends LivingEntity> memorymodule, EntityType<?> type) {
        return canSeeEntity(brains, memorymodule, (livingEntity) -> {
            return (livingEntity.getType() == AtumEntities.VILLAGER_MALE && type == AtumEntities.VILLAGER_FEMALE) || livingEntity.getType() == AtumEntities.VILLAGER_FEMALE && type == AtumEntities.VILLAGER_MALE;
        });
    }

    private static boolean canSeeEntity(Brain<?> brain, MemoryModuleType<? extends LivingEntity> memoryType, Predicate<LivingEntity> livingPredicate) {
        return brain.getMemory(memoryType).filter(livingPredicate).filter(LivingEntity::isAlive).filter((livingEntity) -> {
            return BrainUtil.canSee(brain, livingEntity);
        }).isPresent();
    }
}