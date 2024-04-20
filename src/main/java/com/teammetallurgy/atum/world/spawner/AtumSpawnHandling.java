package com.teammetallurgy.atum.world.spawner;

import com.google.common.collect.Lists;
import com.teammetallurgy.atum.Atum;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.CustomSpawner;
import net.minecraft.world.level.GameRules;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.TickEvent;

import java.util.List;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID)
public class AtumSpawnHandling {
    public static List<CustomSpawner> specialSpawners = Lists.newArrayList(new BanditPatrolSpawner(), new ServalSpawner());

    @SubscribeEvent
    public static void onWorldTick(TickEvent.LevelTickEvent event) { //Bandit spawn handling
        if (event.level.dimension() == Atum.ATUM && event.level instanceof ServerLevel serverLevel) {
            boolean doMobSpawning = serverLevel.getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING);
            if (doMobSpawning) {
                for (CustomSpawner specialSpawner : specialSpawners) {
                    specialSpawner.tick(serverLevel, serverLevel.getDifficulty() != Difficulty.PEACEFUL, true);
                }
            }
        }
    }
}