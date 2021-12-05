package com.teammetallurgy.atum.world.spawner;

import com.teammetallurgy.atum.entity.animal.ServalEntity;
import com.teammetallurgy.atum.init.AtumEntities;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.GameRules;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.CustomSpawner;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraftforge.common.ForgeHooks;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Random;

public class ServalSpawner implements CustomSpawner {
    private int timer;

    @Override
    public int tick(@Nonnull ServerLevel serverWorld, boolean spawnHostileMobs, boolean spawnPassiveMobs) {
        if (spawnPassiveMobs && serverWorld.getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING)) {
            --this.timer;
            if (this.timer > 0) {
                return 0;
            } else {
                this.timer = 1200;
                Player randomPlayer = serverWorld.getRandomPlayer();
                if (randomPlayer == null) {
                    return 0;
                } else {
                    Random random = serverWorld.random;
                    int i = (8 + random.nextInt(24)) * (random.nextBoolean() ? -1 : 1);
                    int j = (8 + random.nextInt(24)) * (random.nextBoolean() ? -1 : 1);
                    BlockPos blockpos = randomPlayer.blockPosition().offset(i, 0, j);
                    if (!serverWorld.hasChunksAt(blockpos.getX() - 10, blockpos.getY() - 10, blockpos.getZ() - 10, blockpos.getX() + 10, blockpos.getY() + 10, blockpos.getZ() + 10)) {
                        return 0;
                    } else {
                        if (NaturalSpawner.isSpawnPositionOk(SpawnPlacements.Type.ON_GROUND, serverWorld, blockpos, AtumEntities.SERVAL)) {
                            if (serverWorld.isCloseToVillage(blockpos, 2)) {
                                return this.attemptSpawn(serverWorld, blockpos);
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

    private int attemptSpawn(ServerLevel serverWorld, BlockPos pos) {
        if (serverWorld.getPoiManager().getCountInRange(PoiType.HOME.getPredicate(), pos, 48, PoiManager.Occupancy.IS_OCCUPIED) > 4L) {
            List<ServalEntity> list = serverWorld.getEntitiesOfClass(ServalEntity.class, (new AABB(pos)).inflate(48.0D, 8.0D, 48.0D));
            if (list.size() < 5) {
                return this.spawnServal(pos, serverWorld);
            }
        }

        return 0;
    }

    private int spawnServal(BlockPos pos, ServerLevel serverWorld) {
        ServalEntity serval = AtumEntities.SERVAL.create(serverWorld);
        if (serval == null) {
            return 0;
        } else {
            if (ForgeHooks.canEntitySpawn(serval, serverWorld, pos.getX(), pos.getY(), pos.getZ(), null, MobSpawnType.NATURAL) == -1) {
                return 0;
            }
            serval.finalizeSpawn(serverWorld, serverWorld.getCurrentDifficultyAt(pos), MobSpawnType.NATURAL, null, null);
            serval.moveTo(pos, 0.0F, 0.0F);
            serverWorld.addFreshEntityWithPassengers(serval);
            return 1;
        }
    }
}