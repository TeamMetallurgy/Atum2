package com.teammetallurgy.atum.network;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.network.packet.OpenWolfGuiPacket;
import com.teammetallurgy.atum.network.packet.StormStrengthPacket;
import com.teammetallurgy.atum.network.packet.SyncHandStackSizePacket;
import com.teammetallurgy.atum.network.packet.WeatherPacket;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.IPacket;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class NetworkHandler {
    private static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(Atum.MOD_ID, "atum_channel"))
            .clientAcceptedVersions(v -> true)
            .serverAcceptedVersions(v -> true)
            .networkProtocolVersion(() -> "ATUM1")
            .simpleChannel();

    public static void initialize() {
        CHANNEL.registerMessage(0, OpenWolfGuiPacket.class, OpenWolfGuiPacket::encode, OpenWolfGuiPacket::decode, OpenWolfGuiPacket.Handler::handle);
        CHANNEL.registerMessage(1, WeatherPacket.class, WeatherPacket::encode, WeatherPacket::decode, WeatherPacket.Handler::handle);
        CHANNEL.registerMessage(2, StormStrengthPacket.class, StormStrengthPacket::encode, StormStrengthPacket::decode, StormStrengthPacket.Handler::handle);
        CHANNEL.registerMessage(3, SyncHandStackSizePacket.class, SyncHandStackSizePacket::encode, SyncHandStackSizePacket::decode, SyncHandStackSizePacket.Handler::handle);
    }

    public static void sendTo(ServerPlayerEntity playerMP, Object toSend) {
        CHANNEL.sendTo(toSend, playerMP.connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
    }

    public static void sendToServer(Object msg) {
        CHANNEL.sendToServer(msg);
    }

    public static void sendToDimension(Object packet, ServerWorld serverWorld, DimensionType dimensionType) {
        PlayerList playerList = serverWorld.getServer().getPlayerList();
        for (int i = 0; i < playerList.getCurrentPlayerCount(); ++i) {
            ServerPlayerEntity serverPlayer = playerList.getPlayers().get(i);
            if (serverPlayer.dimension == dimensionType) {
                sendTo(serverPlayer, packet);
            }
        }
    }

    /*
     * Used to update TESRs
     */
    public static void sendToTracking(ServerWorld world, BlockPos blockPos, IPacket<?> packet, boolean boundaryOnly) {
        world.getChunkProvider().chunkManager.getTrackingPlayers(new ChunkPos(blockPos), boundaryOnly).forEach(p -> p.connection.sendPacket(packet));
    }
}