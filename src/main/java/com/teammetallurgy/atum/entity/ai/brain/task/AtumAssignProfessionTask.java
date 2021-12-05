package com.teammetallurgy.atum.entity.ai.brain.task;

import com.google.common.collect.ImmutableMap;
import com.teammetallurgy.atum.entity.villager.AtumVillagerEntity;
import com.teammetallurgy.atum.entity.villager.AtumVillagerProfession;
import com.teammetallurgy.atum.misc.AtumRegistry;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;
import java.util.Optional;

public class AtumAssignProfessionTask extends Task<VillagerEntity> {

    public AtumAssignProfessionTask() {
        super(ImmutableMap.of(MemoryModuleType.POTENTIAL_JOB_SITE, MemoryModuleStatus.VALUE_PRESENT));
    }

    @Override
    protected boolean shouldExecute(@Nonnull ServerWorld world, VillagerEntity owner) {
        BlockPos pos = owner.getBrain().getMemory(MemoryModuleType.POTENTIAL_JOB_SITE).get().getPos();
        return pos.withinDistance(owner.getPositionVec(), 2.0D) || owner.shouldAssignProfessionOnSpawn();
    }

    @Override
    protected void startExecuting(ServerWorld world, VillagerEntity entity, long gameTimeIn) {
        GlobalPos globalpos = entity.getBrain().getMemory(MemoryModuleType.POTENTIAL_JOB_SITE).get();
        entity.getBrain().removeMemory(MemoryModuleType.POTENTIAL_JOB_SITE);
        entity.getBrain().setMemory(MemoryModuleType.JOB_SITE, globalpos);
        world.setEntityState(entity, (byte) 14);
        if (entity instanceof AtumVillagerEntity && ((AtumVillagerEntity) entity).getAtumVillagerData().getAtumProfession() == AtumVillagerProfession.NONE.get()) {
            MinecraftServer minecraftserver = world.getServer();
            Optional.ofNullable(minecraftserver.getWorld(globalpos.getDimension())).flatMap((w) -> {
                return w.getPointOfInterestManager().getType(globalpos.getPos());
            }).flatMap((poiType) -> {
                return AtumRegistry.VILLAGER_PROFESSION.get().getValues().stream().filter((profession) -> {
                    return profession.getPointOfInterest() == poiType;
                }).findFirst();
            }).ifPresent((profession) -> {
                ((AtumVillagerEntity) entity).setAtumVillagerData(((AtumVillagerEntity) entity).getAtumVillagerData().withProfession(profession));
                entity.resetBrain(world);
            });
        }
    }
}