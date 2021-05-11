package com.teammetallurgy.atum.world.spawner;

import com.teammetallurgy.atum.entity.animal.ServalEntity;
import com.teammetallurgy.atum.init.AtumEntities;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.village.PointOfInterestManager;
import net.minecraft.village.PointOfInterestType;
import net.minecraft.world.GameRules;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.spawner.ISpecialSpawner;
import net.minecraft.world.spawner.WorldEntitySpawner;
import net.minecraftforge.common.ForgeHooks;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Random;

public class ServalSpawner implements ISpecialSpawner {
    private int timer;

    @Override
    public int func_230253_a_(@Nonnull ServerWorld serverWorld, boolean spawnHostileMobs, boolean spawnPassiveMobs) {
        if (spawnPassiveMobs && serverWorld.getGameRules().getBoolean(GameRules.DO_MOB_SPAWNING)) {
            --this.timer;
            if (this.timer > 0) {
                return 0;
            } else {
                this.timer = 1200;
                PlayerEntity randomPlayer = serverWorld.getRandomPlayer();
                if (randomPlayer == null) {
                    return 0;
                } else {
                    Random random = serverWorld.rand;
                    int i = (8 + random.nextInt(24)) * (random.nextBoolean() ? -1 : 1);
                    int j = (8 + random.nextInt(24)) * (random.nextBoolean() ? -1 : 1);
                    BlockPos blockpos = randomPlayer.getPosition().add(i, 0, j);
                    if (!serverWorld.isAreaLoaded(blockpos.getX() - 10, blockpos.getY() - 10, blockpos.getZ() - 10, blockpos.getX() + 10, blockpos.getY() + 10, blockpos.getZ() + 10)) {
                        return 0;
                    } else {
                        if (WorldEntitySpawner.canCreatureTypeSpawnAtLocation(EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, serverWorld, blockpos, AtumEntities.SERVAL)) {
                            if (serverWorld.func_241119_a_(blockpos, 2)) {
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

    private int attemptSpawn(ServerWorld serverWorld, BlockPos pos) {
        if (serverWorld.getPointOfInterestManager().getCountInRange(PointOfInterestType.HOME.getPredicate(), pos, 48, PointOfInterestManager.Status.IS_OCCUPIED) > 4L) {
            List<ServalEntity> list = serverWorld.getEntitiesWithinAABB(ServalEntity.class, (new AxisAlignedBB(pos)).grow(48.0D, 8.0D, 48.0D));
            if (list.size() < 5) {
                return this.spawnServal(pos, serverWorld);
            }
        }

        return 0;
    }

    private int spawnServal(BlockPos pos, ServerWorld serverWorld) {
        ServalEntity serval = AtumEntities.SERVAL.create(serverWorld);
        if (serval == null) {
            return 0;
        } else {
            if (ForgeHooks.canEntitySpawn(serval, serverWorld, pos.getX(), pos.getY(), pos.getZ(), null, SpawnReason.NATURAL) == -1) {
                return 0;
            }
            serval.onInitialSpawn(serverWorld, serverWorld.getDifficultyForLocation(pos), SpawnReason.NATURAL, null, null);
            serval.moveToBlockPosAndAngles(pos, 0.0F, 0.0F);
            serverWorld.func_242417_l(serval);
            return 1;
        }
    }
}