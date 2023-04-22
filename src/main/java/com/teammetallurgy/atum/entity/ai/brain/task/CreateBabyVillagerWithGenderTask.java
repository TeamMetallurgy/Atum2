package com.teammetallurgy.atum.entity.ai.brain.task;

import com.teammetallurgy.atum.entity.villager.AtumVillagerEntity;
import com.teammetallurgy.atum.init.AtumEntities;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.behavior.VillagerMakeLove;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.npc.Villager;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.function.Predicate;

public class CreateBabyVillagerWithGenderTask extends VillagerMakeLove {

    @Override
    protected boolean checkExtraStartConditions(@Nonnull ServerLevel level, @Nonnull Villager owner) {
        return this.canBreed((AtumVillagerEntity) owner);
    }

    @Override
    protected boolean canStillUse(@Nonnull ServerLevel level, @Nonnull Villager entity, long gameTime) {
        return gameTime <= this.birthTimestamp && this.canBreed((AtumVillagerEntity) entity);
    }

    private boolean canBreed(AtumVillagerEntity villager) {
        Brain<Villager> brain = villager.getBrain();
        Optional<AgeableMob> optional = brain.getMemory(MemoryModuleType.BREED_TARGET).filter((breedTarget) -> (villager.getType() == AtumEntities.VILLAGER_MALE.get() && breedTarget.getType() == AtumEntities.VILLAGER_FEMALE.get()) || villager.getType() == AtumEntities.VILLAGER_FEMALE.get() && breedTarget.getType() == AtumEntities.VILLAGER_MALE.get());
        return optional.filter(ageableMob -> isCorrectVisibleType(brain, MemoryModuleType.BREED_TARGET, villager.getType()) && villager.canBreed() && ageableMob.canBreed()).isPresent();
    }

    public static boolean isCorrectVisibleType(Brain<?> brains, MemoryModuleType<? extends LivingEntity> memorymodule, EntityType<?> type) {
        return canSeeEntity(brains, memorymodule, (livingEntity) -> (livingEntity.getType() == AtumEntities.VILLAGER_MALE.get() && type == AtumEntities.VILLAGER_FEMALE.get()) || livingEntity.getType() == AtumEntities.VILLAGER_FEMALE.get() && type == AtumEntities.VILLAGER_MALE.get());
    }

    private static boolean canSeeEntity(Brain<?> brain, MemoryModuleType<? extends LivingEntity> memoryType, Predicate<LivingEntity> livingPredicate) {
        return brain.getMemory(memoryType).filter(livingPredicate).filter(LivingEntity::isAlive).filter((livingEntity) -> BehaviorUtils.entityIsVisible(brain, livingEntity)).isPresent();
    }
}