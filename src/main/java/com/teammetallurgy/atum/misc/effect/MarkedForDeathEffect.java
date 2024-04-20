package com.teammetallurgy.atum.misc.effect;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.entity.bandit.AssassinEntity;
import com.teammetallurgy.atum.init.AtumEffects;
import com.teammetallurgy.atum.init.AtumEntities;
import com.teammetallurgy.atum.misc.AtumConfig;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;

import javax.annotation.Nonnull;

public class MarkedForDeathEffect extends MobEffect { //When on Easy difficulty & level 1 Marked For Death, make sure to have it at least at 51 seconds
    private static final Object2IntMap<LivingEntity> NEXT_SPAWN = new Object2IntOpenHashMap<>();

    public MarkedForDeathEffect() {
        super(MobEffectCategory.NEUTRAL, 12624973);
        NeoForge.EVENT_BUS.register(this);
    }

    @Override
    public void applyEffectTick(@Nonnull LivingEntity livingEntity, int amplifier) {
        Level level = livingEntity.level();
        if (!level.isClientSide()) {
            if (level instanceof ServerLevel serverLevel) {
                if (serverLevel.dimension() == Atum.ATUM && !livingEntity.isSpectator()) {
                    RandomSource random = serverLevel.random;
                    int x = (24 + random.nextInt(24)) * (random.nextBoolean() ? -1 : 1);
                    int z = (24 + random.nextInt(24)) * (random.nextBoolean() ? -1 : 1);
                    BlockPos.MutableBlockPos mutablePos = (new BlockPos.MutableBlockPos(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ())).move(x, 0, z);
                    DifficultyInstance difficulty = serverLevel.getCurrentDifficultyAt(mutablePos);
                    if (difficulty.getDifficulty() != Difficulty.PEACEFUL) {
                        //Amplifier 0 to 9 = I to X
                        //Easy = 1, Normal = 2, Hard = 3
                        double multiplier = Math.max(1, (amplifier + Math.ceil(difficulty.getEffectiveDifficulty())) / 1.33D);
                        int value = (int) (AtumConfig.MOBS.markedForDeathTimeBaseValue.get() / multiplier);
                        if (!NEXT_SPAWN.containsKey(livingEntity)) {
                            NEXT_SPAWN.put(livingEntity, value);
                        } else {
                            int currentTime = NEXT_SPAWN.getInt(livingEntity);
                            NEXT_SPAWN.replace(livingEntity, currentTime - 1);
                        }
                        if (serverLevel.isAreaLoaded(mutablePos, 10)) {
                            if (NEXT_SPAWN.getInt(livingEntity) <= 0) {
                                mutablePos.setY(serverLevel.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, mutablePos).getY());
                                this.spawnAssassin(serverLevel, mutablePos, random, livingEntity);
                                NEXT_SPAWN.removeInt(livingEntity);
                                NEXT_SPAWN.put(livingEntity, value);
                            }
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onEffectExpired(MobEffectEvent.Expired event) {
        MobEffectInstance effectInstance = event.getEffectInstance();
        if (effectInstance != null && effectInstance.getEffect() == AtumEffects.MARKED_FOR_DEATH.get()) {
            NEXT_SPAWN.removeInt(event.getEntity());
        }
    }

    @SubscribeEvent
    public void onEffectRemoval(MobEffectEvent.Remove event) {
        MobEffectInstance effectInstance = event.getEffectInstance();
        if (effectInstance != null && effectInstance.getEffect() == AtumEffects.MARKED_FOR_DEATH.get()) {
            NEXT_SPAWN.removeInt(event.getEntity());
        }
    }

    private void spawnAssassin(ServerLevel level, BlockPos pos, RandomSource rand, LivingEntity markedTarget) {
        EntityType<? extends AssassinEntity> entityType = AtumEntities.ASSASSIN.get();
        BlockState state = level.getBlockState(pos);
        if (NaturalSpawner.isValidEmptySpawnBlock(level, pos, state, state.getFluidState(), entityType) && AssassinEntity.canSpawn(entityType, level, MobSpawnType.EVENT, pos, rand)) {
            AssassinEntity assassin = entityType.create(level);
            if (assassin != null) {
                assassin.setPos(pos.getX() + rand.nextInt(5) - rand.nextInt(5), pos.getY(), pos.getZ() + rand.nextInt(5) - rand.nextInt(5));
                assassin.finalizeSpawn(level, level.getCurrentDifficultyAt(pos), MobSpawnType.EVENT, null, null);
                assassin.setMarkedTarget(markedTarget);
                level.addFreshEntity(assassin);
            }
        }
    }
}