package com.teammetallurgy.atum.entity.ai.brain.task;

import com.teammetallurgy.atum.entity.villager.AtumVillagerEntity;
import com.teammetallurgy.atum.init.AtumEntities;
import net.minecraft.world.entity.AgableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.behavior.VillagerMakeLove;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.server.level.ServerLevel;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.function.Predicate;

public class CreateBabyVillagerWithGenderTask extends VillagerMakeLove {

    @Override
    protected boolean checkExtraStartConditions(@Nonnull ServerLevel world, @Nonnull Villager owner) {
        return this.canBreed((AtumVillagerEntity) owner);
    }

    @Override
    protected boolean canStillUse(@Nonnull ServerLevel world, @Nonnull Villager entity, long gameTime) {
        return gameTime <= this.birthTimestamp && this.canBreed((AtumVillagerEntity) entity);
    }

    private boolean canBreed(AtumVillagerEntity villager) {
        Brain<Villager> brain = villager.getBrain();
        Optional<AgableMob> optional = brain.getMemory(MemoryModuleType.BREED_TARGET).filter((breedTarget) -> {
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
            return BehaviorUtils.entityIsVisible(brain, livingEntity);
        }).isPresent();
    }
}