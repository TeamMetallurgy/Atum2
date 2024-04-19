package com.teammetallurgy.atum.network;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.network.packet.OpenWolfGuiPacket;
import com.teammetallurgy.atum.network.packet.SyncArrowFlightPathPacket;
import com.teammetallurgy.atum.network.packet.SyncHandStackSizePacket;
import com.teammetallurgy.atum.network.packet.weather.StormStrengthPacket;
import com.teammetallurgy.atum.network.packet.weather.WeatherPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
import net.neoforged.neoforge.network.registration.IPayloadRegistrar;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class NetworkHandler {

    @SubscribeEvent
    public static void registerPackets(RegisterPayloadHandlerEvent event) {
        final IPayloadRegistrar registrar = event.registrar(Atum.MOD_ID).versioned("1.0");
        registrar.play(OpenWolfGuiPacket.ID, OpenWolfGuiPacket::new, play -> play.server(OpenWolfGuiPacket::handle));
        registrar.play(WeatherPacket.ID, WeatherPacket::new, play -> play.server(WeatherPacket::handle));
        registrar.play(StormStrengthPacket.ID, StormStrengthPacket::new, play -> play.server(StormStrengthPacket::handle));
        registrar.play(SyncHandStackSizePacket.ID, SyncHandStackSizePacket::new, play -> play.client(SyncHandStackSizePacket::handle));
        registrar.play(SyncArrowFlightPathPacket.ID, SyncArrowFlightPathPacket::new, play -> play.client(SyncArrowFlightPathPacket::handle));
    }

    public static void sendTo(ServerPlayer serverPlayer, CustomPacketPayload... payloads) {
        PacketDistributor.PLAYER.with(serverPlayer).send(payloads);
    }

    public static void sendToServer(CustomPacketPayload... payloads) {
        PacketDistributor.SERVER.noArg().send(payloads);
    }

    public static void sendToDimension(ResourceKey<Level> dimension, CustomPacketPayload... payloads) {
        PacketDistributor.DIMENSION.with(dimension).send(payloads);
    }

    /*
     * Used to update TESRs
     */
    public static void sendToTracking(ServerLevel level, BlockPos blockPos, Packet<?> packet, boolean boundaryOnly) {
        level.getChunkSource().chunkMap.getPlayers(new ChunkPos(blockPos), boundaryOnly).forEach(p -> p.connection.send(packet));
    }
}