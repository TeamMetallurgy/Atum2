package com.teammetallurgy.atum.world.spawner;

import com.google.common.collect.Lists;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.misc.AtumConfig;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.Difficulty;
import net.minecraft.world.GameRules;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.spawner.ISpecialSpawner;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID)
public class AtumSpawnHandling {
    public static List<ISpecialSpawner> specialSpawners = Lists.newArrayList(new BanditPatrolSpawner());

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onBiomeLoad(BiomeLoadingEvent event) {
        ResourceLocation name = event.getName();
        if (AtumConfig.Mobs.ENTITY_TYPE.containsKey(name)) {
            System.out.println("ATUM BIOME: " + name.getPath());
            for (EntityType<?> entityType : AtumConfig.Mobs.ENTITY_TYPE.get(name)) {
                String baseCategory = AtumConfig.Mobs.MOBS;
                EntityClassification classification = AtumConfig.Mobs.ENTITY_CLASSIFICATION.get(entityType);
                if (entityType != null && entityType.getRegistryName() != null) {
                    String mobName = entityType.getRegistryName().getPath();
                    int weight = AtumConfig.Helper.get(baseCategory, mobName, "weight");
                    int min = AtumConfig.Helper.get(baseCategory, mobName, "min");
                    int max = AtumConfig.Helper.get(baseCategory, mobName, "max");
                    event.getSpawns().getSpawner(classification).add(new MobSpawnInfo.Spawners(entityType, weight, min, max)); //TODO Test
                }
            }
        }
    }

    @SubscribeEvent
    public static void onWorldTick(TickEvent.WorldTickEvent event) {
        if (event.world.getDimensionKey() == Atum.ATUM && event.world instanceof ServerWorld) {
            ServerWorld serverWorld = (ServerWorld) event.world;
            boolean doMobSpawning = serverWorld.getGameRules().getBoolean(GameRules.DO_MOB_SPAWNING);
            if (doMobSpawning) {
                for (ISpecialSpawner specialSpawner : specialSpawners) {
                    specialSpawner.func_230253_a_(serverWorld, serverWorld.getDifficulty() != Difficulty.PEACEFUL, true);
                }
            }
        }
    }
}