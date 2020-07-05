package com.teammetallurgy.atum.misc.potion;

import com.teammetallurgy.atum.entity.bandit.AssassinEntity;
import com.teammetallurgy.atum.init.AtumEffects;
import com.teammetallurgy.atum.init.AtumEntities;
import com.teammetallurgy.atum.misc.AtumConfig;
import com.teammetallurgy.atum.world.dimension.AtumDimensionType;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.spawner.WorldEntitySpawner;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MarkedForDeathEffect extends Effect { //When on Easy difficulty & level 1 Marked For Death, make sure to have it at least at 51 seconds
    private static final Object2IntMap<LivingEntity> NEXT_SPAWN = new Object2IntOpenHashMap<>();

    public MarkedForDeathEffect() {
        super(EffectType.NEUTRAL, 12624973);
    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        return true;
    }

    @Override
    public List<ItemStack> getCurativeItems() {
        return new ArrayList<>();
    }

    @Override
    public void performEffect(@Nonnull LivingEntity livingEntity, int amplifier) {
        World world = livingEntity.world;
        if (!world.isRemote()) {
            if (world instanceof ServerWorld) {
                ServerWorld serverWorld = (ServerWorld) world;
                if (serverWorld.getDimension().getType() == AtumDimensionType.ATUM && !livingEntity.isSpectator()) {
                    Random random = serverWorld.rand;
                    int x = (24 + random.nextInt(24)) * (random.nextBoolean() ? -1 : 1);
                    int z = (24 + random.nextInt(24)) * (random.nextBoolean() ? -1 : 1);
                    BlockPos.Mutable mutablePos = (new BlockPos.Mutable(livingEntity)).move(x, 0, z);
                    DifficultyInstance difficulty = serverWorld.getDifficultyForLocation(mutablePos);
                    if (difficulty.getDifficulty() != Difficulty.PEACEFUL) {
                        //Amplifier 0 to 9 = I to X
                        //Easy = 1, Normal = 2, Hard = 3
                        double multiplier = Math.max(1, (amplifier + Math.ceil(difficulty.getAdditionalDifficulty())) / 1.33D);
                        System.out.println("Multiplier: " + multiplier);
                        int value = (int) (AtumConfig.MOBS.markedForDeathTimeBaseValue.get() / multiplier);
                        if (!NEXT_SPAWN.containsKey(livingEntity)) {
                            NEXT_SPAWN.put(livingEntity, value);
                        } else {
                            int currentTime = NEXT_SPAWN.getInt(livingEntity);
                            System.out.println("NEXT SPAWN: " + (currentTime - 1));
                            NEXT_SPAWN.replace(livingEntity, currentTime - 1);
                        }
                        if (serverWorld.isAreaLoaded(mutablePos, 10)) {
                            if (NEXT_SPAWN.getInt(livingEntity) <= 0) {
                                System.out.println("TIMER DONE");
                                mutablePos.setY(serverWorld.getHeight(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, mutablePos).getY());
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
        EffectInstance effectInstance = event.getPotionEffect();
        if (effectInstance != null && effectInstance.getPotion() == AtumEffects.MARKED_FOR_DEATH) {
            NEXT_SPAWN.removeInt(event.getEntityLiving());
        }
    }

    @SubscribeEvent
    public void onEffectRemoval(PotionEvent.PotionRemoveEvent event) {
        EffectInstance effectInstance = event.getPotionEffect();
        if (effectInstance != null && effectInstance.getPotion() == AtumEffects.MARKED_FOR_DEATH) {
            NEXT_SPAWN.removeInt(event.getEntityLiving());
        }
    }

    private void spawnAssassin(World world, BlockPos pos, Random rand, LivingEntity markedTarget) {
        EntityType<? extends AssassinEntity> entityType = AtumEntities.ASSASSIN;
        BlockState state = world.getBlockState(pos);
        if (WorldEntitySpawner.isSpawnableSpace(world, pos, state, state.getFluidState()) && AssassinEntity.canSpawn(entityType, world, SpawnReason.EVENT, pos, rand)) {
            AssassinEntity assassin = entityType.create(world);
            if (assassin != null) {
                assassin.setPosition(pos.getX() + rand.nextInt(5) - rand.nextInt(5), pos.getY(), pos.getZ() + rand.nextInt(5) - rand.nextInt(5));
                System.out.println("ASSASSIN: " + assassin.getPosition());
                assassin.onInitialSpawn(world, world.getDifficultyForLocation(pos), SpawnReason.EVENT, null, null);
                assassin.setMarkedTarget(markedTarget);
                world.addEntity(assassin);
            }
        }
    }
}