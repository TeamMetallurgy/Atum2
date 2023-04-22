package com.teammetallurgy.atum.entity.ai.brain.task;

import com.teammetallurgy.atum.entity.villager.AtumVillagerData;
import com.teammetallurgy.atum.entity.villager.AtumVillagerEntity;
import com.teammetallurgy.atum.init.AtumVillagerProfession;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.npc.Villager;

public class AtumResetProfession {
    public static BehaviorControl<Villager> create() {
        return BehaviorBuilder.create((p_259684_) -> {
            return p_259684_.group(p_259684_.absent(MemoryModuleType.JOB_SITE)).apply(p_259684_, (p_260035_) -> {
                return (p_260244_, villager, p_259597_) -> {
                    if (villager instanceof AtumVillagerEntity atumVillagerEntity) {
                        AtumVillagerData villagerdata = atumVillagerEntity.getAtumVillagerData();
                        if (villagerdata.getAtumProfession() != AtumVillagerProfession.NONE.get() && villagerdata.getAtumProfession() != AtumVillagerProfession.NITWIT.get() && atumVillagerEntity.getVillagerXp() == 0 && villagerdata.getLevel() <= 1) {
                            atumVillagerEntity.setVillagerData(atumVillagerEntity.getAtumVillagerData().withProfession(AtumVillagerProfession.NONE.get()));
                            atumVillagerEntity.refreshBrain(p_260244_);
                            return true;
                        } else {
                            return false;
                        }
                    } else {
                        return false;
                    }
                };
            });
        });
    }
}