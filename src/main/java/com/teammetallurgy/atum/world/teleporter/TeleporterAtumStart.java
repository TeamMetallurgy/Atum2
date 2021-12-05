package com.teammetallurgy.atum.world.teleporter;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.init.AtumFeatures;
import com.teammetallurgy.atum.misc.AtumConfig;
import com.teammetallurgy.atum.world.DimensionHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.common.util.ITeleporter;

import java.util.function.Function;

public class TeleporterAtumStart implements ITeleporter {

    @Override
    public Entity placeEntity(Entity entity, ServerLevel currentWorld, ServerLevel destWorld, float yaw, Function<Boolean, Entity> repositionEntity) {
        BlockPos spawnPos = DimensionHelper.getSurfacePos(destWorld, destWorld.getSharedSpawnPos());
        if (!DimensionHelper.getData(destWorld).hasStartStructureSpawned()) {
            this.onInitialAtumJoining(destWorld, spawnPos);
            DimensionHelper.getData(destWorld).setHasStartStructureSpawned(true);
        }
        return this.onAtumJoining(destWorld, repositionEntity.apply(false), spawnPos, yaw);
    }

    private Entity onAtumJoining(ServerLevel world, Entity entity, BlockPos spawnPos, float yaw) {
        if (world.dimension() == Atum.ATUM) {
            if (AtumConfig.ATUM_START.startInAtumPortal.get()) {
                TeleporterAtum teleporterAtum = TeleporterAtum.INSTANCE;
                teleporterAtum.makePortal(world, entity);
                teleporterAtum.placeInPortal(world, entity, yaw);
            } else {
                entity.yRot = yaw;
                entity.moveTo(spawnPos.getX(), spawnPos.getY() + 1, spawnPos.getZ());
            }
        }
        return entity;
    }

    private void onInitialAtumJoining(ServerLevel world, BlockPos spawnPos) {
        if (world.dimension() == Atum.ATUM) {
            if (AtumConfig.ATUM_START.startInAtumPortal.get()) {
                spawnPos = spawnPos.offset(4, 0, 4);
            }
            if (!AtumConfig.ATUM_START.atumStartStructure.get().isEmpty()) {
                ConfiguredFeature<?, ?> startStructure = AtumFeatures.START_STRUCTURE_CONFIGURED;
                startStructure.place(world, world.getChunkSource().getGenerator(), world.random, spawnPos);
            }
            if (world.getServer().getWorldData().worldGenSettings().generateBonusChest()) {
                ConfiguredFeature<?, ?> bonusCrate = AtumFeatures.BONUS_CRATE_CONFIGURED;
                bonusCrate.place(world, world.getChunkSource().getGenerator(), world.random, spawnPos);
            }
        }
    }
}