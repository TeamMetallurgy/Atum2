package com.teammetallurgy.atum.network.packet;

import com.teammetallurgy.atum.entity.animal.DesertWolfEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.function.Supplier;

public class OpenWolfGuiPacket {
	private final int wolfID;

	public OpenWolfGuiPacket(int wolfID) {
		this.wolfID = wolfID;
	}

	public static void encode(OpenWolfGuiPacket packet, PacketBuffer buf) {
		buf.writeInt(packet.wolfID);
	}

	public static OpenWolfGuiPacket decode(PacketBuffer buf) {
		return new OpenWolfGuiPacket(buf.readInt());
	}

	public static class Handler {
		public static void handle(OpenWolfGuiPacket message, Supplier<NetworkEvent.Context> ctx) {
			ServerPlayerEntity playerMP = ctx.get().getSender();
			if (playerMP != null && !(playerMP instanceof FakePlayer)) {
				Entity entity = playerMP.world.getEntityByID(message.wolfID);
				if (entity instanceof DesertWolfEntity) {
					DesertWolfEntity wolf = (DesertWolfEntity) entity;
					//wolf.getInventory().setCustomName(wolf.getName()); //TODO?
					NetworkHooks.openGui(playerMP, wolf, buf -> buf.writeInt(wolf.getEntityId()));
				}
				ctx.get().setPacketHandled(true);
			}
		}
	}
}