package com.teammetallurgy.atum.world.spawner;

import com.google.common.collect.Lists;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.misc.AtumConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.CustomSpawner;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID)
public class AtumSpawnHandling {
    public static List<CustomSpawner> specialSpawners = Lists.newArrayList(new BanditPatrolSpawner(), new ServalSpawner());

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onBiomeLoad(BiomeLoadingEvent event) { //Passive spawns handling
        ResourceLocation name = event.getName();
        if (AtumConfig.Mobs.ENTITY_TYPE.containsKey(name)) {
            for (EntityType<?> entityType : AtumConfig.Mobs.ENTITY_TYPE.get(name)) {
                String baseCategory = AtumConfig.Mobs.MOBS;
                MobCategory classification = AtumConfig.Mobs.ENTITY_CLASSIFICATION.get(entityType);
                if (entityType != null && entityType.getRegistryName() != null) {
                    String mobName = entityType.getRegistryName().getPath();
                    int weight = AtumConfig.Helper.get(baseCategory, mobName, "weight");
                    int min = AtumConfig.Helper.get(baseCategory, mobName, "min");
                    int max = AtumConfig.Helper.get(baseCategory, mobName, "max");
                    event.getSpawns().getSpawner(classification).add(new MobSpawnSettings.SpawnerData(entityType, weight, min, max));
                }
            }
        }
    }

    @SubscribeEvent
    public static void onWorldTick(TickEvent.WorldTickEvent event) { //Bandit spawn handling
        if (event.world.dimension() == Atum.ATUM && event.world instanceof ServerLevel) {
            ServerLevel serverWorld = (ServerLevel) event.world;
            boolean doMobSpawning = serverWorld.getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING);
            if (doMobSpawning) {
                for (CustomSpawner specialSpawner : specialSpawners) {
                    specialSpawner.tick(serverWorld, serverWorld.getDifficulty() != Difficulty.PEACEFUL, true);
                }
            }
        }
    }
}