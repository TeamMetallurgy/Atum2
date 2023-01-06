package com.teammetallurgy.atum.entity.ai.brain.task;

import com.google.common.collect.ImmutableMap;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.entity.villager.AtumVillagerEntity;
import com.teammetallurgy.atum.init.AtumVillagerProfession;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.npc.Villager;

import javax.annotation.Nonnull;
import java.util.Optional;

public class AtumAssignProfessionTask extends Behavior<Villager> {

    public AtumAssignProfessionTask() {
        super(ImmutableMap.of(MemoryModuleType.POTENTIAL_JOB_SITE, MemoryStatus.VALUE_PRESENT));
    }

    @Override
    protected boolean checkExtraStartConditions(@Nonnull ServerLevel world, Villager owner) {
        BlockPos pos = owner.getBrain().getMemory(MemoryModuleType.POTENTIAL_JOB_SITE).get().pos();
        return pos.closerThan(owner.blockPosition(), 2.0D) || owner.assignProfessionWhenSpawned();
    }

    @Override
    protected void start(ServerLevel world, Villager entity, long gameTimeIn) {
        GlobalPos globalpos = entity.getBrain().getMemory(MemoryModuleType.POTENTIAL_JOB_SITE).get();
        entity.getBrain().eraseMemory(MemoryModuleType.POTENTIAL_JOB_SITE);
        entity.getBrain().setMemory(MemoryModuleType.JOB_SITE, globalpos);
        world.broadcastEntityEvent(entity, (byte) 14);
        if (entity instanceof AtumVillagerEntity && ((AtumVillagerEntity) entity).getAtumVillagerData().getAtumProfession() == AtumVillagerProfession.NONE.get()) {
            MinecraftServer minecraftserver = world.getServer();
            Optional.ofNullable(minecraftserver.getLevel(globalpos.dimension())).flatMap((w) -> {
                return w.getPoiManager().getType(globalpos.pos());
            }).flatMap((poiType) -> {
                return Atum.villagerProfession.get().getValues().stream().filter((profession) -> {
                    return profession.heldJobSite().test(poiType);
                }).findFirst();
            }).ifPresent((profession) -> {
                ((AtumVillagerEntity) entity).setAtumVillagerData(((AtumVillagerEntity) entity).getAtumVillagerData().withProfession(profession));
                entity.refreshBrain(world);
            });
        }
    }
}