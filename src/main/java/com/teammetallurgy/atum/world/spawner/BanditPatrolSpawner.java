package com.teammetallurgy.atum.world.spawner;

import com.teammetallurgy.atum.entity.bandit.BanditBaseEntity;
import com.teammetallurgy.atum.init.AtumBiomes;
import com.teammetallurgy.atum.init.AtumEntities;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
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
        } else if (!serverWorld.getGameRules().getBoolean(GameRules.field_230127_D_)) {
            return 0;
        } else {
            Random rand = serverWorld.rand;
            --this.timer;
            if (this.timer > 0) {
                return 0;
            } else {
                System.out.println("Chance to spawn patrol");
                this.timer += rand.nextInt(250);
                if (serverWorld.isDaytime()) {
                    if (rand.nextInt(4) != 0) {
                        return 0;
                    } else { int playerAmount = serverWorld.getPlayers().size();
                        if (playerAmount < 1) {
                            return 0;
                        } else {
                            PlayerEntity player = serverWorld.getPlayers().get(rand.nextInt(playerAmount));
                            if (player.isSpectator()) {
                                return 0;
                            } else if (serverWorld.isVillage(player.getPosition())) {
                                return 0;
                            } else {
                                System.out.println("Successfully spawned patrol");
                                int x = (24 + rand.nextInt(24)) * (rand.nextBoolean() ? -1 : 1);
                                int z = (24 + rand.nextInt(24)) * (rand.nextBoolean() ? -1 : 1);
                                BlockPos.Mutable mutablePos = (new BlockPos.Mutable(player)).move(x, 0, z);
                                if (!serverWorld.isAreaLoaded(mutablePos.getX() - 10, mutablePos.getY() - 10, mutablePos.getZ() - 10, mutablePos.getX() + 10, mutablePos.getY() + 10, mutablePos.getZ() + 10)) {
                                    return 0;
                                } else {
                                    Biome biome = serverWorld.getBiome(mutablePos);
                                    if (biome == AtumBiomes.DRIED_RIVER || biome == AtumBiomes.OASIS) {
                                        return 0;
                                    } else {
                                        int amount = 0;
                                        int difficulty = 1 + (int) Math.ceil(serverWorld.getDifficultyForLocation(mutablePos).getAdditionalDifficulty());
                                        System.out.println("POS: " + mutablePos);
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
        //return chance > 0.86D ? AtumEntities.BARBARIAN: chance > 0.5D ? AtumEntities.NOMAD : AtumEntities.BRIGAND;
    }
}