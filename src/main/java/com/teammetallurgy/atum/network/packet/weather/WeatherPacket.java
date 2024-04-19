package com.teammetallurgy.atum.network.packet.weather;

import com.teammetallurgy.atum.world.SandstormHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.neoforged.neoforge.network.NetworkEvent;

import java.util.function.Supplier;

public class WeatherPacket {
	private final int stormTime;

	public WeatherPacket(int stormTime) {
		this.stormTime = stormTime;
	}

	public static void encode(WeatherPacket packet, FriendlyByteBuf buf) {
		buf.writeInt(packet.stormTime);
	}

	public static WeatherPacket decode(FriendlyByteBuf buf) {
		return new WeatherPacket(buf.readInt());
	}

	public static class Handler {
		public static void handle(WeatherPacket message, Supplier<NetworkEvent.Context> ctx) {
			SandstormHandler.INSTANCE.stormTime = message.stormTime;
			ctx.get().setPacketHandled(true);
		}
	}
}