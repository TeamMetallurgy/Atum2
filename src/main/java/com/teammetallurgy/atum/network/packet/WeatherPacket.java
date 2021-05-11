package com.teammetallurgy.atum.network.packet;

import com.teammetallurgy.atum.world.SandstormHandler;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class WeatherPacket {
	private final int stormTime;

	public WeatherPacket(int stormTime) {
		this.stormTime = stormTime;
	}

	public static void encode(WeatherPacket packet, PacketBuffer buf) {
		buf.writeInt(packet.stormTime);
	}

	public static WeatherPacket decode(PacketBuffer buf) {
		return new WeatherPacket(buf.readInt());
	}

	public static class Handler {
		public static void handle(WeatherPacket message, Supplier<NetworkEvent.Context> ctx) {
			SandstormHandler.INSTANCE.stormTime = message.stormTime;
			ctx.get().setPacketHandled(true);
		}
	}
}