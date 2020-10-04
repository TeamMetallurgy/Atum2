package com.teammetallurgy.atum.network.packet;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class WeatherPacket {
	private final boolean isStorming;
	private final int stormTime;

	public WeatherPacket(boolean isStorming, int stormTime) {
		this.isStorming = isStorming;
		this.stormTime = stormTime;
	}

	public static void encode(WeatherPacket packet, PacketBuffer buf) {
		buf.writeBoolean(packet.isStorming);
		buf.writeInt(packet.stormTime);
	}

	public static WeatherPacket decode(PacketBuffer buf) {
		return new WeatherPacket(buf.readBoolean(), buf.readInt());
	}

	public static class Handler {
		public static void handle(WeatherPacket message, Supplier<NetworkEvent.Context> ctx) {
			/*Dimension dimension = Minecraft.getInstance().player.world.dimension; //TODO
			if (dimension instanceof AtumDimension) {
				AtumDimension providerAtum = (AtumDimension) dimension;
				AtumDimension.DATA.setStorming(message.isStorming);
				providerAtum.stormTime = message.stormTime;
			}*/
			ctx.get().setPacketHandled(true);
		}
	}
}