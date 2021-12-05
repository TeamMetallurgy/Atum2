package com.teammetallurgy.atum.misc.potion;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.entity.bandit.AssassinEntity;
import com.teammetallurgy.atum.init.AtumEffects;
import com.teammetallurgy.atum.init.AtumEntities;
import com.teammetallurgy.atum.misc.AtumConfig;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MarkedForDeathEffect extends MobEffect { //When on Easy difficulty & level 1 Marked For Death, make sure to have it at least at 51 seconds
    private static final Object2IntMap<LivingEntity> NEXT_SPAWN = new Object2IntOpenHashMap<>();

    public MarkedForDeathEffect() {
        super(MobEffectCategory.NEUTRAL, 12624973);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public List<ItemStack> getCurativeItems() {
        return new ArrayList<>();
    }

    @Override
    public void applyEffectTick(@Nonnull LivingEntity livingEntity, int amplifier) {
        Level world = livingEntity.level;
        if (!world.isClientSide()) {
            if (world instanceof ServerLevel) {
                ServerLevel serverWorld = (ServerLevel) world;
                if (serverWorld.dimension() == Atum.ATUM && !livingEntity.isSpectator()) {
                    Random random = serverWorld.random;
                    int x = (24 + random.nextInt(24)) * (random.nextBoolean() ? -1 : 1);
                    int z = (24 + random.nextInt(24)) * (random.nextBoolean() ? -1 : 1);
                    BlockPos.MutableBlockPos mutablePos = (new BlockPos.MutableBlockPos(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ())).move(x, 0, z);
                    DifficultyInstance difficulty = serverWorld.getCurrentDifficultyAt(mutablePos);
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
                        if (serverWorld.isAreaLoaded(mutablePos, 10)) {
                            if (NEXT_SPAWN.getInt(livingEntity) <= 0) {
                                mutablePos.setY(serverWorld.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, mutablePos).getY());
                                this.spawnAssassin(serverWorld, mutablePos, random, livingEntity);
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
    public void onEffectExpired(PotionEvent.PotionExpiryEvent event) {
        MobEffectInstance effectInstance = event.getPotionEffect();
        if (effectInstance != null && effectInstance.getEffect() == AtumEffects.MARKED_FOR_DEATH) {
            NEXT_SPAWN.removeInt(event.getEntityLiving());
        }
    }

    @SubscribeEvent
    public void onEffectRemoval(PotionEvent.PotionRemoveEvent event) {
        MobEffectInstance effectInstance = event.getPotionEffect();
        if (effectInstance != null && effectInstance.getEffect() == AtumEffects.MARKED_FOR_DEATH) {
            NEXT_SPAWN.removeInt(event.getEntityLiving());
        }
    }

    private void spawnAssassin(ServerLevel world, BlockPos pos, Random rand, LivingEntity markedTarget) {
        EntityType<? extends AssassinEntity> entityType = AtumEntities.ASSASSIN;
        BlockState state = world.getBlockState(pos);
        if (NaturalSpawner.isValidEmptySpawnBlock(world, pos, state, state.getFluidState(), entityType) && AssassinEntity.canSpawn(entityType, world, MobSpawnType.EVENT, pos, rand)) {
            AssassinEntity assassin = entityType.create(world);
            if (assassin != null) {
                assassin.setPos(pos.getX() + rand.nextInt(5) - rand.nextInt(5), pos.getY(), pos.getZ() + rand.nextInt(5) - rand.nextInt(5));
                assassin.finalizeSpawn(world, world.getCurrentDifficultyAt(pos), MobSpawnType.EVENT, null, null);
                assassin.setMarkedTarget(markedTarget);
                world.addFreshEntity(assassin);
            }
        }
    }
}