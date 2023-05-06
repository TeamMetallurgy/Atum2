package com.teammetallurgy.atum.network.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncArrowFlightPathPacket {
    private final int arrowID;

    public SyncArrowFlightPathPacket(int arrowID) {
        this.arrowID = arrowID;
    }

    public static void encode(SyncArrowFlightPathPacket packet, FriendlyByteBuf buf) {
        buf.writeInt(packet.arrowID);
    }

    public static SyncArrowFlightPathPacket decode(FriendlyByteBuf buf) {
        return new SyncArrowFlightPathPacket(buf.readInt());
    }

    public static class Handler {
        public static void handle(SyncArrowFlightPathPacket message, Supplier<NetworkEvent.Context> ctx) {
            LocalPlayer player = Minecraft.getInstance().player;
            if (player != null) {
                Entity entity = player.level.getEntity(message.arrowID);
                if (entity != null) {
                    entity.setDeltaMovement(entity.getDeltaMovement().add(0.0D, 0.05D, 0.0D));
                }
            }
            ctx.get().setPacketHandled(true);
        }
    }
}