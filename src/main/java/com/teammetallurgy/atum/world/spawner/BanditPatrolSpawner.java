package com.teammetallurgy.atum.world.spawner;

import com.teammetallurgy.atum.entity.bandit.BanditBaseEntity;
import com.teammetallurgy.atum.init.AtumBiomes;
import com.teammetallurgy.atum.init.AtumEntities;
import com.teammetallurgy.atum.init.AtumFeatures;
import com.teammetallurgy.atum.misc.AtumConfig;
import com.teammetallurgy.atum.world.gen.structure.StructureHelper;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.spawner.WorldEntitySpawner;

import java.util.Random;

public class BanditPatrolSpawner {
    private int timer;

    public int tick(ServerWorld serverWorld, boolean spawnHostileMobs) {
        if (!spawnHostileMobs) {
            return 0;
        } else if (AtumConfig.MOBS.banditPatrolFrequency.get() < 1) {
            return 0;
        } else {
            Random rand = serverWorld.rand;
            --this.timer;
            if (this.timer > 0) {
                return 0;
            } else {
                this.timer += rand.nextInt(AtumConfig.MOBS.banditPatrolFrequency.get());
                if (serverWorld.isDaytime()) {
                    int playerAmount = serverWorld.getPlayers().size();
                    if (playerAmount < 1) {
                        return 0;
                    } else {
                        PlayerEntity player = serverWorld.getPlayers().get(rand.nextInt(playerAmount));
                        if (player.isSpectator()) {
                            return 0;
                        } else {
                            int x = (20 + rand.nextInt(20)) * (rand.nextBoolean() ? -1 : 1);
                            int z = (20 + rand.nextInt(20)) * (rand.nextBoolean() ? -1 : 1);
                            BlockPos.Mutable mutablePos = (new BlockPos.Mutable(player)).move(x, 0, z);
                            if (!serverWorld.isAreaLoaded(mutablePos, 8) || StructureHelper.doesChunkHaveStructure(serverWorld, mutablePos.getX(), mutablePos.getZ(), AtumFeatures.PYRAMID)) {
                                return 0;
                            } else {
                                Biome biome = serverWorld.getBiome(mutablePos);
                                if (biome == AtumBiomes.DRIED_RIVER || biome == AtumBiomes.OASIS) {
                                    return 0;
                                } else {
                                    int amount = 0;
                                    int difficulty = 1 + (int) Math.ceil(serverWorld.getDifficultyForLocation(mutablePos).getAdditionalDifficulty());
                                    for (int size = 0; size < difficulty; ++size) {
                                        EntityType<? extends BanditBaseEntity> entityType = this.getEntityType(rand);
                                        ++amount;
                                        mutablePos.setY(serverWorld.getHeight(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, mutablePos).getY());
                                        if (size == 0) {
                                            if (!this.spawnPatroller(AtumEntities.SERGEANT, serverWorld, mutablePos, rand, true)) {
                                                break;
                                            }
                                        } else {
                                            this.spawnPatroller(entityType, serverWorld, mutablePos, rand, false);
                                        }
                                        mutablePos.setX(mutablePos.getX() + rand.nextInt(5) - rand.nextInt(5));
                                        mutablePos.setZ(mutablePos.getZ() + rand.nextInt(5) - rand.nextInt(5));
                                    }
                                    return amount;
                                }
                            }
                        }
                    }
                } else {
                    return 0;
                }
            }
        }
    }

    private boolean spawnPatroller(EntityType<? extends BanditBaseEntity> entityType, World world, BlockPos pos, Random rand, boolean isLeader) {
        BlockState state = world.getBlockState(pos);
        if (!WorldEntitySpawner.isSpawnableSpace(world, pos, state, state.getFluidState())) {
            return false;
        } else if (!BanditBaseEntity.canSpawn(entityType, world, SpawnReason.PATROL, pos, rand)) {
            return false;
        } else {
            BanditBaseEntity bandit = entityType.create(world);
            if (bandit != null) {
                if (isLeader) {
                    bandit.setLeader(true);
                    bandit.resetPatrolTarget();
                }
                bandit.setCanPatrol(true);
                bandit.setPosition(pos.getX(), pos.getY(), pos.getZ());
                bandit.onInitialSpawn(world, world.getDifficultyForLocation(pos), SpawnReason.PATROL, null, null);
                world.addEntity(bandit);
                return true;
            } else {
                return false;
            }
        }
    }

    private EntityType<? extends BanditBaseEntity> getEntityType(Random rand) {
        double chance = rand.nextDouble();
        if (chance <= 0.5D) {
            return AtumEntities.BRIGAND;
        } else if (chance > 0.5D && chance < 0.87D) {
            return AtumEntities.NOMAD;
        } else {
            return AtumEntities.BARBARIAN;
        }
    }
}