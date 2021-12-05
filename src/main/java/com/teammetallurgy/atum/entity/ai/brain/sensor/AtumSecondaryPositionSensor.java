package com.teammetallurgy.atum.entity.ai.brain.sensor;

import com.google.common.collect.Lists;
import com.teammetallurgy.atum.entity.villager.AtumVillagerEntity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.SecondaryPositionSensor;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.List;

public class AtumSecondaryPositionSensor extends SecondaryPositionSensor {

    public AtumSecondaryPositionSensor() {
        super();
    }

    @Override
    protected void update(ServerWorld serverWorld, VillagerEntity entity) {
        RegistryKey<World> worldKey = serverWorld.getDimensionKey();
        BlockPos pos = entity.getPosition();
        List<GlobalPos> list = Lists.newArrayList();
        int i = 4;

        for (int j = -4; j <= 4; ++j) {
            for (int k = -2; k <= 2; ++k) {
                for (int l = -4; l <= 4; ++l) {
                    BlockPos statePos = pos.add(j, k, l);
                    if (entity instanceof AtumVillagerEntity && ((AtumVillagerEntity) entity).getAtumVillagerData().getAtumProfession().getRelatedWorldBlocks().contains(serverWorld.getBlockState(statePos).getBlock())) {
                        list.add(GlobalPos.getPosition(worldKey, statePos));
                    }
                }
            }
        }

        Brain<?> brain = entity.getBrain();
        if (!list.isEmpty()) {
            brain.setMemory(MemoryModuleType.SECONDARY_JOB_SITE, list);
        } else {
            brain.removeMemory(MemoryModuleType.SECONDARY_JOB_SITE);
        }
    }
}