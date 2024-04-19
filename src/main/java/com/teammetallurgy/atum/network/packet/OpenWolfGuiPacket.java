package com.teammetallurgy.atum.network.packet;

import com.teammetallurgy.atum.entity.animal.DesertWolfEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.network.NetworkEvent;
import net.neoforged.neoforge.network.NetworkHooks;

import java.util.function.Supplier;

public class OpenWolfGuiPacket {
	private final int wolfID;

	public OpenWolfGuiPacket(int wolfID) {
		this.wolfID = wolfID;
	}

	public static void encode(OpenWolfGuiPacket packet, FriendlyByteBuf buf) {
		buf.writeInt(packet.wolfID);
	}

	public static OpenWolfGuiPacket decode(FriendlyByteBuf buf) {
		return new OpenWolfGuiPacket(buf.readInt());
	}

	public static class Handler {
		public static void handle(OpenWolfGuiPacket message, Supplier<NetworkEvent.Context> ctx) {
			ServerPlayer playerMP = ctx.get().getSender();
			if (playerMP != null && !(playerMP instanceof FakePlayer)) {
				Entity entity = playerMP.level.getEntity(message.wolfID);
				if (entity instanceof DesertWolfEntity wolf) {
					NetworkHooks.openScreen(playerMP, wolf, buf -> buf.writeInt(wolf.getId()));
				}
				ctx.get().setPacketHandled(true);
			}
		}
	}
}