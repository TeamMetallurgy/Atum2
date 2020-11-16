package com.teammetallurgy.atum.world.teleporter;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.init.AtumFeatures;
import com.teammetallurgy.atum.misc.AtumConfig;
import com.teammetallurgy.atum.world.DimensionHelper;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.ITeleporter;

import java.util.function.Function;

public class TeleporterAtumStart implements ITeleporter {

    @Override
    public Entity placeEntity(Entity entity, ServerWorld currentWorld, ServerWorld destWorld, float yaw, Function<Boolean, Entity> repositionEntity) {
        BlockPos spawnPos = DimensionHelper.getSurfacePos(destWorld, destWorld.getSpawnPoint());
        if (!DimensionHelper.getData(destWorld).hasStartStructureSpawned()) {
            this.onInitialAtumJoining(destWorld, spawnPos);
            DimensionHelper.getData(destWorld).setHasStartStructureSpawned(true);
        }
        return this.onAtumJoining(destWorld, repositionEntity.apply(false), spawnPos, yaw);
    }

    private Entity onAtumJoining(ServerWorld world, Entity entity, BlockPos spawnPos, float yaw) {
        if (world.getDimensionKey() == Atum.ATUM) {
            if (AtumConfig.ATUM_START.startInAtumPortal.get()) {
                TeleporterAtum teleporterAtum = TeleporterAtum.INSTANCE;
                teleporterAtum.makePortal(world, entity);
                teleporterAtum.placeInPortal(world, entity, yaw);
            } else {
                entity.rotationYaw = yaw;
                entity.moveForced(spawnPos.getX(), spawnPos.getY() + 1, spawnPos.getZ());
            }
        }
        return entity;
    }

    private void onInitialAtumJoining(ServerWorld world, BlockPos spawnPos) {
        if (world.getDimensionKey() == Atum.ATUM) {
            if (AtumConfig.ATUM_START.startInAtumPortal.get()) {
                spawnPos = spawnPos.add(4, 0, 4);
            }
            if (!AtumConfig.ATUM_START.atumStartStructure.get().isEmpty()) {
                ConfiguredFeature<?, ?> startStructure = AtumFeatures.START_STRUCTURE.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG);
                startStructure.generate(world, world.getChunkProvider().getChunkGenerator(), world.rand, spawnPos);
            }
            if (world.getServer().getServerConfiguration().getDimensionGeneratorSettings().hasBonusChest()) {
                ConfiguredFeature<?, ?> bonusCrate = AtumFeatures.BONUS_CRATE.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG);
                bonusCrate.generate(world, world.getChunkProvider().getChunkGenerator(), world.rand, spawnPos);
            }
        }
    }
}