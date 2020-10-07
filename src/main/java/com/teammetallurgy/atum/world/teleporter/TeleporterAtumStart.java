package com.teammetallurgy.atum.world.teleporter;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.init.AtumFeatures;
import com.teammetallurgy.atum.misc.AtumConfig;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.ITeleporter;
import net.minecraftforge.fml.common.Mod;

import java.util.function.Function;

public class TeleporterAtumStart implements ITeleporter {

    @Override
    public Entity placeEntity(Entity entity, ServerWorld currentWorld, ServerWorld destWorld, float yaw, Function<Boolean, Entity> repositionEntity) {
        /*if (!AtumDimension.DATA.hasStartStructureSpawned()) { //TODO
            this.onAtumJoining(destWorld, entity, yaw);
            AtumDimension.DATA.setHasStartStructureSpawned(true);
            return repositionEntity.apply(false);
        }*/
        this.onAtumJoining(destWorld, entity, yaw);
        return repositionEntity.apply(false);
    }

    private void onAtumJoining(ServerWorld world, Entity entity, float yaw) {
        if (world.getDimensionKey() == Atum.ATUM) {
            BlockPos spawnPos = new BlockPos(world.getWorldInfo().getSpawnX(), world.getWorldInfo().getSpawnY(), world.getWorldInfo().getSpawnZ());
            if (AtumConfig.ATUM_START.startInAtumPortal.get()) {
                TeleporterAtum.makePortal(world, entity);
                TeleporterAtum.placeInPortal(world, entity, yaw);
            } else {
                entity.rotationYaw = yaw;
                entity.moveForced(spawnPos.getX(), spawnPos.getY() + 1, spawnPos.getZ());
            }
            if (AtumConfig.ATUM_START.startInAtumPortal.get()) {
                spawnPos = spawnPos.add(4, 0, 4);
            }
            if (!AtumConfig.ATUM_START.atumStartStructure.get().isEmpty()) {
                ConfiguredFeature<?, ?> startStructure = AtumFeatures.START_STRUCTURE.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG);
                startStructure.func_242765_a(world, world.getChunkProvider().getChunkGenerator(), world.rand, spawnPos);

            }
            if (world.getServer().func_240793_aU_().getDimensionGeneratorSettings().hasBonusChest()) {
                ConfiguredFeature<?, ?> bonusCrate = AtumFeatures.BONUS_CRATE.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG);
                bonusCrate.func_242765_a(world, world.getChunkProvider().getChunkGenerator(), world.rand, spawnPos);
            }
        }
    }
}