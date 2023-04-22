package com.teammetallurgy.atum.entity.ai.brain.task;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.entity.villager.AtumVillagerEntity;
import com.teammetallurgy.atum.init.AtumVillagerProfession;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.npc.Villager;

import java.util.Optional;

public class AtumAssignProfessionFromJobSite {

    public static BehaviorControl<Villager> create() {
        return BehaviorBuilder.create((p_258312_) -> {
            return p_258312_.group(p_258312_.present(MemoryModuleType.POTENTIAL_JOB_SITE), p_258312_.registered(MemoryModuleType.JOB_SITE)).apply(p_258312_, (p_258304_, p_258305_) -> {
                return (serverLevel, villager, p_258311_) -> {
                    if (villager instanceof  AtumVillagerEntity atumVillagerEntity) {
                        GlobalPos globalpos = p_258312_.get(p_258304_);
                        if (!globalpos.pos().closerToCenterThan(atumVillagerEntity.position(), 2.0D) && !atumVillagerEntity.assignProfessionWhenSpawned()) {
                            return false;
                        } else {
                            p_258304_.erase();
                            p_258305_.set(globalpos);
                            serverLevel.broadcastEntityEvent(atumVillagerEntity, (byte) 14);
                            if (atumVillagerEntity.getAtumVillagerData().getAtumProfession() != AtumVillagerProfession.NONE.get()) {
                                return true;
                            } else {
                                MinecraftServer minecraftserver = serverLevel.getServer();
                                Optional.ofNullable(minecraftserver.getLevel(globalpos.dimension())).flatMap((p_22467_) -> {
                                    return p_22467_.getPoiManager().getType(globalpos.pos());
                                }).flatMap((p_258313_) -> {
                                    return Atum.villagerProfession.get().getValues().stream().filter((p_217125_) -> {
                                        return p_217125_.heldJobSite().test(p_258313_);
                                    }).findFirst();
                                }).ifPresent((p_22464_) -> {
                                    atumVillagerEntity.setVillagerData(atumVillagerEntity.getAtumVillagerData().withProfession(p_22464_));
                                    atumVillagerEntity.refreshBrain(serverLevel);
                                });
                                return true;
                            }
                        }
                    } else {
                        return false;
                    }
                };
            });
        });
    }
}