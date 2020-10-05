package com.teammetallurgy.atum.world.spawner;

import com.teammetallurgy.atum.entity.bandit.BanditBaseEntity;
import com.teammetallurgy.atum.entity.bandit.SergeantEntity;
import com.teammetallurgy.atum.init.AtumBiomes;
import com.teammetallurgy.atum.init.AtumEntities;
import com.teammetallurgy.atum.init.AtumFeatures;
import com.teammetallurgy.atum.misc.AtumConfig;
import com.teammetallurgy.atum.world.gen.structure.StructureHelper;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.spawner.WorldEntitySpawner;

import javax.annotation.Nullable;
import java.util.Optional;
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
                            BlockPos.Mutable mutablePos = (new BlockPos.Mutable(player.getPosX(), player.getPosY(), player.getPosZ())).move(x, 0, z);
                            if (!serverWorld.isAreaLoaded(mutablePos, 8) || StructureHelper.doesChunkHaveStructure(serverWorld, mutablePos.getX(), mutablePos.getZ(), AtumFeatures.PYRAMID_STRUCTURE)) {
                                return 0;
                            } else {
                                Biome biome = serverWorld.getBiome(mutablePos);
                                Optional<RegistryKey<Biome>> biomeKey = serverWorld.func_241828_r().getRegistry(Registry.BIOME_KEY).getOptionalKey(biome);
                                if (biomeKey.isPresent() && (biomeKey.get() == AtumBiomes.DRIED_RIVER || biomeKey.get() == AtumBiomes.OASIS)) { //TODO test
                                    return 0;
                                } else {
                                    int amount = 0;
                                    int difficulty = 1 + (int) Math.ceil(serverWorld.getDifficultyForLocation(mutablePos).getAdditionalDifficulty());
                                    BanditBaseEntity leadingEntity = null;
                                    for (int size = 0; size < difficulty; ++size) {
                                        EntityType<? extends BanditBaseEntity> entityType = this.getEntityType(rand);
                                        ++amount;
                                        mutablePos.setY(serverWorld.getHeight(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, mutablePos).getY());
                                        if (size == 0) {
                                            SergeantEntity leader = AtumEntities.SERGEANT.create(serverWorld);
                                            if (leader != null) {
                                                if (this.spawnLeader(leader, serverWorld, mutablePos, rand)) {
                                                    leadingEntity = leader;
                                                } else {
                                                    break;
                                                }
                                            } else {
                                                break;
                                            }
                                        } else {
                                            this.spawnPatroller(entityType, serverWorld, mutablePos, rand, leadingEntity);
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

    private boolean spawnPatroller(EntityType<? extends BanditBaseEntity> entityType, ServerWorld world, BlockPos pos, Random rand, @Nullable BanditBaseEntity leadingEntity) {
        BlockState state = world.getBlockState(pos);
        if (!WorldEntitySpawner.func_234968_a_(world, pos, state, state.getFluidState(), entityType)) {
            return false;
        } else if (!BanditBaseEntity.canSpawn(entityType, world, SpawnReason.PATROL, pos, rand)) {
            return false;
        } else {
            BanditBaseEntity bandit = entityType.create(world);
            if (bandit != null) {
                bandit.setCanPatrol(true);
                if (leadingEntity != null) {
                    bandit.setLeadingEntity(leadingEntity);
                }
                bandit.setPosition(pos.getX(), pos.getY(), pos.getZ());
                bandit.onInitialSpawn(world, world.getDifficultyForLocation(pos), SpawnReason.PATROL, null, null);
                world.addEntity(bandit);
                return true;
            } else {
                return false;
            }
        }
    }

    private boolean spawnLeader(BanditBaseEntity leader, ServerWorld world, BlockPos pos, Random rand) {
        BlockState state = world.getBlockState(pos);
        EntityType<? extends BanditBaseEntity> type = (EntityType<? extends BanditBaseEntity>) leader.getType();
        if (!WorldEntitySpawner.func_234968_a_(world, pos, state, state.getFluidState(), type)) {
            return false;
        } else if (!BanditBaseEntity.canSpawn(type, world, SpawnReason.PATROL, pos, rand)) {
            return false;
        } else {
            leader.setLeader(true);
            leader.resetPatrolTarget();
            leader.setCanPatrol(true);
            leader.setPosition(pos.getX(), pos.getY(), pos.getZ());
            leader.onInitialSpawn(world, world.getDifficultyForLocation(pos), SpawnReason.PATROL, null, null);
            world.addEntity(leader);
            return true;
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