package com.teammetallurgy.atum.network.packet;

import com.teammetallurgy.atum.Atum;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

import javax.annotation.Nonnull;

public class SyncArrowFlightPathPacket implements CustomPacketPayload {
    public static final ResourceLocation ID = new ResourceLocation(Atum.MOD_ID, "sync_arrow_flight_path");
    private final int arrowID;

    public SyncArrowFlightPathPacket(int arrowID) {
        this.arrowID = arrowID;
    }

    public SyncArrowFlightPathPacket(FriendlyByteBuf buf) {
        this.arrowID = buf.readInt();
    }

    @Override
    @Nonnull
    public ResourceLocation id() {
        return ID;
    }

    @Override
    public void write(@Nonnull FriendlyByteBuf buf) {
        buf.writeInt(this.arrowID);
    }

    public void handle(PlayPayloadContext context) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            Entity entity = player.level().getEntity(this.arrowID);
            if (entity != null) {
                entity.setDeltaMovement(entity.getDeltaMovement().add(0.0D, 0.05D, 0.0D));
            }
        }
    }
}