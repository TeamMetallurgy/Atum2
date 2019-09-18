package com.teammetallurgy.atum.network.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.io.IOException;
import java.util.Objects;

public abstract class Packet<REQ extends Packet<REQ>> implements IMessage, IMessageHandler<REQ, REQ> {

    @Override
    public REQ onMessage(REQ message, MessageContext ctx) {
        if (ctx.side == Dist.SERVER) {
            runServer(message, ctx.getServerHandler().player);
        } else {
            runClient(message, getPlayerClient());
        }
        return null;
    }

    @OnlyIn(Dist.CLIENT)
    private void runClient(final REQ packet, final PlayerEntity player) {
        Minecraft.getMinecraft().addScheduledTask(() -> packet.handleClientSide(player));
    }

    private void runServer(final REQ packet, final PlayerEntity player) {
        Objects.requireNonNull(player.getServer()).addScheduledTask(() -> packet.handleServerSide(player));
    }

    @OnlyIn(Dist.CLIENT)
    public PlayerEntity getPlayerClient() {
        return Minecraft.getMinecraft().player;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        try {
            fromBytes(new PacketBuffer(buf));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        toBytes(new PacketBuffer(buf));
    }

    @OnlyIn(Dist.CLIENT)
    protected abstract void handleClientSide(PlayerEntity player);

    protected abstract void handleServerSide(PlayerEntity player);

    protected abstract void toBytes(PacketBuffer buffer);

    protected abstract void fromBytes(PacketBuffer buffer) throws IOException;
}
