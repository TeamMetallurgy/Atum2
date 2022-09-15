package com.teammetallurgy.atum.misc;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.world.DimensionHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.AbstractMap;
import java.util.Optional;

public class SpawnHelper {

    public static final String TAG_ATUM_RESPAWN = "atum_respawn";
    private static final StringTextComponent BED_MISSING_MSG = new StringTextComponent("You have no home bed or respawn anchor, or it was obstructed");

    /**
     * Gets a player's spawnpoint, resetting if the bed is invalid
     * Always returns a valid world and position which can be teleported to.
     *
     * @param serverWorld any world
     * @param serverPlayer the player
     * @param msgMode how to tell the player if their bed is missing; 0 is no message, 1 is after respawn, 2 is immediately
     * @return surface pos of the spawnpoint
     */
    public static AbstractMap.SimpleEntry<ServerWorld, BlockPos> validateAndGetSpawnPoint(ServerWorld serverWorld, ServerPlayerEntity serverPlayer, int msgMode) {
        boolean atumSpawn = AtumConfig.ATUM_START.startInAtum.get();
        boolean forcedSpawn = serverPlayer.func_241142_M_(); // if true -> don't check for valid bed
        ServerWorld spawnWorld = serverWorld.getServer().getWorld(serverPlayer.func_241141_L_());
        BlockPos spawnPos = serverPlayer.func_241140_K_();

        Optional<Vector3d> bedPos = Optional.empty();
        if (spawnPos != null) {
            bedPos = PlayerEntity.func_242374_a(spawnWorld, spawnPos, serverPlayer.func_242109_L(), forcedSpawn, false);
        }

        boolean reset = false;
        if (atumSpawn) {
            ServerWorld atumWorld = serverWorld.getServer().getWorld(Atum.ATUM);
            BlockPos defaultSpawnPos = DimensionHelper.getSurfacePos(atumWorld, atumWorld.getSpawnPoint()).up();
            // best attempt at checking whether this is the default atum spawnpoint
            boolean isSpawnPosDefault = forcedSpawn &&
                    spawnWorld.getDimensionKey() == Atum.ATUM &&
                    spawnPos != null &&
                    spawnPos.getX() == defaultSpawnPos.getX() &&
                    spawnPos.getZ() == defaultSpawnPos.getZ();

            if (!bedPos.isPresent() || isSpawnPosDefault) {
                if (!forcedSpawn && spawnPos != null) {
                    sendBedMissingMsg(serverPlayer, msgMode);
                }
                serverPlayer.func_242111_a(Atum.ATUM, defaultSpawnPos, serverPlayer.getRotationYawHead(), true, false);
                spawnWorld = atumWorld;
                spawnPos = defaultSpawnPos;
                reset = true;
            }
        } else {
            if (!bedPos.isPresent()) {
                if (!forcedSpawn && spawnPos != null) {
                    sendBedMissingMsg(serverPlayer, msgMode);
                }
                serverPlayer.func_242111_a(World.OVERWORLD, null, serverPlayer.getRotationYawHead(), false, false);
                spawnWorld = serverWorld.getServer().getWorld(World.OVERWORLD);
                spawnPos = DimensionHelper.getSurfacePos(spawnWorld, spawnWorld.getSpawnPoint()).up();
                reset = true;
            }
        }

        if (!reset) {
            spawnPos = spawnPos.up();
        }

        return new AbstractMap.SimpleEntry<>(spawnWorld, spawnPos);
    }

    /**
     * Sends a message to a player saying that their bed is missing
     *
     * @param serverPlayer the player
     * @param msgMode how to tell the player if their bed is missing; 0 is no message, 1 is after respawn, 2 is immediately
     */
    public static void sendBedMissingMsg(ServerPlayerEntity serverPlayer, int msgMode) {
        if (msgMode == 1) {
            CompoundNBT tag = serverPlayer.getPersistentData();
            CompoundNBT persistedTag = tag.getCompound(PlayerEntity.PERSISTED_NBT_TAG);
            persistedTag.putBoolean(TAG_ATUM_RESPAWN, true);
            tag.put(PlayerEntity.PERSISTED_NBT_TAG, persistedTag);
        } else if (msgMode == 2) {
            serverPlayer.sendMessage(BED_MISSING_MSG, Util.DUMMY_UUID);
        }
    }
}
