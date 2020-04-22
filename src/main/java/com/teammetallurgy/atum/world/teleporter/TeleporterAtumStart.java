package com.teammetallurgy.atum.world.teleporter;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.misc.AtumConfig;
import com.teammetallurgy.atum.world.dimension.AtumDimension;
import com.teammetallurgy.atum.world.dimension.AtumDimensionType;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.ITeleporter;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.function.Function;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID)
public class TeleporterAtumStart implements ITeleporter {
    private static WorldSettings worldSettings;

    @Override
    public Entity placeEntity(Entity entity, ServerWorld currentWorld, ServerWorld destWorld, float yaw, Function<Boolean, Entity> repositionEntity) {
        AtumDimension atum = (AtumDimension) destWorld.dimension;
        if (!atum.hasStartStructureSpawned) {
            this.onAtumJoining(destWorld, entity, yaw);
            atum.hasStartStructureSpawned = true;
            return repositionEntity.apply(false);
        }
        return entity;
    }

    private void onAtumJoining(ServerWorld world, Entity entity, float yaw) {
        if (world.dimension.getType() == AtumDimensionType.ATUM) {
            if (AtumConfig.ATUM_START.startInAtumPortal.get()) {
                TeleporterAtum.makePortal(world, entity);
                TeleporterAtum.placeInPortal(world, entity, yaw);
            }
            if (!AtumConfig.ATUM_START.atumStartStructure.get().isEmpty()) {
                //new WorldGenStartStructure().generate(world, world.rand, pos.down()); //TODO
            }
            if (worldSettings != null && worldSettings.isBonusChestEnabled()) {
                ConfiguredFeature<?, ?> bonusChest = Feature.BONUS_CHEST.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG); //TODO test
                bonusChest.place(world, world.getChunkProvider().getChunkGenerator(), world.rand, new BlockPos(world.getWorldInfo().getSpawnX(), world.getWorldInfo().getSpawnY(), world.getWorldInfo().getSpawnZ()));
            }
        }
    }

    @SubscribeEvent
    public static void onCreateSpawnPos(WorldEvent.CreateSpawnPosition event) {
        worldSettings = event.getSettings();
    }
}