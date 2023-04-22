package com.teammetallurgy.atum.network;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.network.packet.OpenWolfGuiPacket;
import com.teammetallurgy.atum.network.packet.StormStrengthPacket;
import com.teammetallurgy.atum.network.packet.SyncHandStackSizePacket;
import com.teammetallurgy.atum.network.packet.WeatherPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

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

    public static void sendTo(ServerPlayer playerMP, Object toSend) {
        CHANNEL.sendTo(toSend, playerMP.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }

    public static void sendToServer(Object msg) {
        CHANNEL.sendToServer(msg);
    }

    public static void sendToDimension(Object packet, ServerLevel serverLevel, ResourceKey<Level> dimension) {
        PlayerList playerList = serverLevel.getServer().getPlayerList();
        for (int i = 0; i < playerList.getPlayerCount(); ++i) {
            ServerPlayer serverPlayer = playerList.getPlayers().get(i);
            if (serverPlayer.level.dimension() == dimension) {
                sendTo(serverPlayer, packet);
            }
        }
    }

    /*
     * Used to update TESRs
     */
    public static void sendToTracking(ServerLevel level, BlockPos blockPos, Packet<?> packet, boolean boundaryOnly) {
        level.getChunkSource().chunkMap.getPlayers(new ChunkPos(blockPos), boundaryOnly).forEach(p -> p.connection.send(packet));
    }
}