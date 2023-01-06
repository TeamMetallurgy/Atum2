package com.teammetallurgy.atum.world.spawner;

import com.teammetallurgy.atum.entity.animal.ServalEntity;
import com.teammetallurgy.atum.init.AtumEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.CustomSpawner;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.ForgeHooks;

import javax.annotation.Nonnull;
import java.util.List;

public class ServalSpawner implements CustomSpawner {
    private int timer;

    @Override
    public int tick(@Nonnull ServerLevel serverLevel, boolean spawnHostileMobs, boolean spawnPassiveMobs) {
        if (spawnPassiveMobs && serverLevel.getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING)) {
            --this.timer;
            if (this.timer > 0) {
                return 0;
            } else {
                this.timer = 1200;
                Player randomPlayer = serverLevel.getRandomPlayer();
                if (randomPlayer == null) {
                    return 0;
                } else {
                    RandomSource random = serverLevel.random;
                    int i = (8 + random.nextInt(24)) * (random.nextBoolean() ? -1 : 1);
                    int j = (8 + random.nextInt(24)) * (random.nextBoolean() ? -1 : 1);
                    BlockPos blockpos = randomPlayer.blockPosition().offset(i, 0, j);
                    if (!serverLevel.hasChunksAt(blockpos.getX() - 10, blockpos.getY() - 10, blockpos.getZ() - 10, blockpos.getX() + 10, blockpos.getY() + 10, blockpos.getZ() + 10)) {
                        return 0;
                    } else {
                        if (NaturalSpawner.isSpawnPositionOk(SpawnPlacements.Type.ON_GROUND, serverLevel, blockpos, AtumEntities.SERVAL.get())) {
                            if (serverLevel.isCloseToVillage(blockpos, 2)) {
                                return this.attemptSpawn(serverLevel, blockpos);
                            }
                        }

                        return 0;
                    }
                }
            }
        } else {
            return 0;
        }
    }

    private int attemptSpawn(ServerLevel serverLevel, BlockPos pos) {
        if (serverLevel.getPoiManager().getCountInRange((poiTypeHolder -> poiTypeHolder.is(PoiTypes.HOME)), pos, 48, PoiManager.Occupancy.IS_OCCUPIED) > 4L) {
            List<ServalEntity> list = serverLevel.getEntitiesOfClass(ServalEntity.class, (new AABB(pos)).inflate(48.0D, 8.0D, 48.0D));
            if (list.size() < 5) {
                return this.spawnServal(pos, serverLevel);
            }
        }

        return 0;
    }

    private int spawnServal(BlockPos pos, ServerLevel serverLevel) {
        ServalEntity serval = AtumEntities.SERVAL.get().create(serverLevel);
        if (serval == null) {
            return 0;
        } else {
            if (ForgeHooks.canEntitySpawn(serval, serverLevel, pos.getX(), pos.getY(), pos.getZ(), null, MobSpawnType.NATURAL) == -1) {
                return 0;
            }
            serval.finalizeSpawn(serverLevel, serverLevel.getCurrentDifficultyAt(pos), MobSpawnType.NATURAL, null, null);
            serval.moveTo(pos, 0.0F, 0.0F);
            serverLevel.addFreshEntityWithPassengers(serval);
            return 1;
        }
    }
}