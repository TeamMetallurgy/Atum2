package com.teammetallurgy.atum.world.spawner;

import com.teammetallurgy.atum.entity.bandit.BanditBaseEntity;
import com.teammetallurgy.atum.entity.bandit.SergeantEntity;
import com.teammetallurgy.atum.init.AtumBiomes;
import com.teammetallurgy.atum.init.AtumEntities;
import com.teammetallurgy.atum.init.AtumStructures;
import com.teammetallurgy.atum.misc.AtumConfig;
import com.teammetallurgy.atum.world.gen.structure.StructureHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.CustomSpawner;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public class BanditPatrolSpawner implements CustomSpawner {
    private int timer;

    @Override
    public int tick(@Nonnull ServerLevel serverLevel, boolean spawnHostileMobs, boolean spawnPassiveMobs) {
        if (!spawnHostileMobs) {
            return 0;
        } else if (AtumConfig.MOBS.banditPatrolFrequency.get() < 1) {
            return 0;
        } else {
            RandomSource rand = serverLevel.random;
            --this.timer;
            if (this.timer > 0) {
                return 0;
            } else {
                this.timer += rand.nextInt(AtumConfig.MOBS.banditPatrolFrequency.get());
                if (serverLevel.isDay()) {
                    int playerAmount = serverLevel.players().size();
                    if (playerAmount < 1) {
                        return 0;
                    } else {
                        Player player = serverLevel.players().get(rand.nextInt(playerAmount));
                        if (player.isSpectator()) {
                            return 0;
                        } else {
                            int x = (20 + rand.nextInt(20)) * (rand.nextBoolean() ? -1 : 1);
                            int z = (20 + rand.nextInt(20)) * (rand.nextBoolean() ? -1 : 1);
                            BlockPos.MutableBlockPos mutablePos = (new BlockPos.MutableBlockPos(player.getX(), player.getY(), player.getZ())).move(x, 0, z);
                            if (!serverLevel.isAreaLoaded(mutablePos, 8) || StructureHelper.doesChunkHaveStructure(serverLevel, mutablePos, AtumStructures.PYRAMID_KEY)) {
                                return 0;
                            } else {
                                Biome biome = serverLevel.getBiome(mutablePos).value();
                                Optional<ResourceKey<Biome>> biomeKey = serverLevel.registryAccess().registryOrThrow(Registries.BIOME).getResourceKey(biome);
                                if (biomeKey.isPresent() && (biomeKey.get() == AtumBiomes.DRIED_RIVER || biomeKey.get() == AtumBiomes.OASIS)) {
                                    return 0;
                                } else {
                                    int amount = 0;
                                    int difficulty = 1 + (int) Math.ceil(serverLevel.getCurrentDifficultyAt(mutablePos).getEffectiveDifficulty());
                                    BanditBaseEntity leadingEntity = null;
                                    for (int size = 0; size < difficulty; ++size) {
                                        EntityType<? extends BanditBaseEntity> entityType = this.getEntityType(rand);
                                        ++amount;
                                        mutablePos.setY(serverLevel.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, mutablePos).getY());
                                        if (size == 0) {
                                            SergeantEntity leader = AtumEntities.SERGEANT.get().create(serverLevel);
                                            if (leader != null) {
                                                if (this.spawnLeader(leader, serverLevel, mutablePos, rand)) {
                                                    leadingEntity = leader;
                                                } else {
                                                    break;
                                                }
                                            } else {
                                                break;
                                            }
                                        } else {
                                            this.spawnPatroller(entityType, serverLevel, mutablePos, rand, leadingEntity);
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

    private boolean spawnPatroller(EntityType<? extends BanditBaseEntity> entityType, ServerLevel level, BlockPos pos, RandomSource rand, @Nullable BanditBaseEntity leadingEntity) {
        BlockState state = level.getBlockState(pos);
        if (!NaturalSpawner.isValidEmptySpawnBlock(level, pos, state, state.getFluidState(), entityType)) {
            return false;
        } else if (!BanditBaseEntity.canSpawn(entityType, level, MobSpawnType.PATROL, pos, rand)) {
            return false;
        } else {
            BanditBaseEntity bandit = entityType.create(level);
            if (bandit != null) {
                bandit.setCanPatrol(true);
                if (leadingEntity != null) {
                    bandit.setLeadingEntity(leadingEntity);
                }
                bandit.setPos(pos.getX(), pos.getY(), pos.getZ());
                bandit.finalizeSpawn(level, level.getCurrentDifficultyAt(pos), MobSpawnType.PATROL, null, null);
                level.addFreshEntity(bandit);
                return true;
            } else {
                return false;
            }
        }
    }

    private boolean spawnLeader(BanditBaseEntity leader, ServerLevel level, BlockPos pos, RandomSource rand) {
        BlockState state = level.getBlockState(pos);
        EntityType<? extends BanditBaseEntity> type = (EntityType<? extends BanditBaseEntity>) leader.getType();
        if (!NaturalSpawner.isValidEmptySpawnBlock(level, pos, state, state.getFluidState(), type)) {
            return false;
        } else if (!BanditBaseEntity.canSpawn(type, level, MobSpawnType.PATROL, pos, rand)) {
            return false;
        } else {
            leader.setPatrolLeader(true);
            leader.findPatrolTarget();
            leader.setCanPatrol(true);
            leader.setPos(pos.getX(), pos.getY(), pos.getZ());
            leader.finalizeSpawn(level, level.getCurrentDifficultyAt(pos), MobSpawnType.PATROL, null, null);
            level.addFreshEntity(leader);
            return true;
        }
    }

    private EntityType<? extends BanditBaseEntity> getEntityType(RandomSource rand) {
        double chance = rand.nextDouble();
        if (chance <= 0.5D) {
            return AtumEntities.BRIGAND.get();
        } else if (chance > 0.5D && chance < 0.87D) {
            return AtumEntities.NOMAD.get();
        } else {
            return AtumEntities.BARBARIAN.get();
        }
    }
}