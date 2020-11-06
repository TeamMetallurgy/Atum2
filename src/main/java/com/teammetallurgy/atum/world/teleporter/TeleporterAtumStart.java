package com.teammetallurgy.atum.world.teleporter;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.init.AtumFeatures;
import com.teammetallurgy.atum.misc.AtumConfig;
import com.teammetallurgy.atum.world.DimensionHelper;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.ITeleporter;

import java.util.function.Function;

public class TeleporterAtumStart implements ITeleporter {

    @Override
    public Entity placeEntity(Entity entity, ServerWorld currentWorld, ServerWorld destWorld, float yaw, Function<Boolean, Entity> repositionEntity) {
        System.out.println("HAS START STRUCTURE SPAWNED: " + DimensionHelper.getData(destWorld).hasStartStructureSpawned());
        if (!DimensionHelper.getData(destWorld).hasStartStructureSpawned()) { //TODO Test in multiplayer
            this.onAtumJoining(destWorld, entity, yaw);
            DimensionHelper.getData(destWorld).setHasStartStructureSpawned(true);
            return repositionEntity.apply(false);
        }
        return repositionEntity.apply(false);
    }

    private void onAtumJoining(ServerWorld world, Entity entity, float yaw) {
        if (world.getDimensionKey() == Atum.ATUM) {
            BlockPos spawnPos = new BlockPos(world.getWorldInfo().getSpawnX(), world.getWorldInfo().getSpawnY(), world.getWorldInfo().getSpawnZ());
            spawnPos = world.getHeight(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, spawnPos);
            if (AtumConfig.ATUM_START.startInAtumPortal.get()) {
                TeleporterAtum teleporterAtum = TeleporterAtum.INSTANCE;
                teleporterAtum.makePortal(world, entity);
                teleporterAtum.placeInPortal(world, entity, yaw);
            } else {
                entity.rotationYaw = yaw;
                entity.moveForced(spawnPos.getX(), spawnPos.getY() + 1, spawnPos.getZ());
            }
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