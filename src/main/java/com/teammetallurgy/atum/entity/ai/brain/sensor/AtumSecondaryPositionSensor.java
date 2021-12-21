package com.teammetallurgy.atum.entity.ai.brain.sensor;

import com.google.common.collect.Lists;
import com.teammetallurgy.atum.entity.villager.AtumVillagerEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.SecondaryPoiSensor;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.Level;

import java.util.List;

public class AtumSecondaryPositionSensor extends SecondaryPoiSensor {

    public AtumSecondaryPositionSensor() {
        super();
    }

    @Override
    protected void doTick(ServerLevel serverLevel, Villager entity) {
        ResourceKey<Level> worldKey = serverLevel.dimension();
        BlockPos pos = entity.blockPosition();
        List<GlobalPos> list = Lists.newArrayList();
        int i = 4;

        for (int j = -4; j <= 4; ++j) {
            for (int k = -2; k <= 2; ++k) {
                for (int l = -4; l <= 4; ++l) {
                    BlockPos statePos = pos.offset(j, k, l);
                    if (entity instanceof AtumVillagerEntity && ((AtumVillagerEntity) entity).getAtumVillagerData().getAtumProfession().getRelatedWorldBlocks().contains(serverLevel.getBlockState(statePos).getBlock())) {
                        list.add(GlobalPos.of(worldKey, statePos));
                    }
                }
            }
        }

        Brain<?> brain = entity.getBrain();
        if (!list.isEmpty()) {
            brain.setMemory(MemoryModuleType.SECONDARY_JOB_SITE, list);
        } else {
            brain.eraseMemory(MemoryModuleType.SECONDARY_JOB_SITE);
        }
    }
}