package com.teammetallurgy.atum.network.packet;

import com.teammetallurgy.atum.world.SandstormHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class StormStrengthPacket {
	private final float stormStrength;

	public StormStrengthPacket(float stormStrength) {
		this.stormStrength = stormStrength;
	}

	public static void encode(StormStrengthPacket packet, FriendlyByteBuf buf) {
		buf.writeFloat(packet.stormStrength);
	}

	public static StormStrengthPacket decode(FriendlyByteBuf buf) {
		return new StormStrengthPacket(buf.readFloat());
	}

	public static class Handler {
		public static void handle(StormStrengthPacket message, Supplier<NetworkEvent.Context> ctx) {
			SandstormHandler.INSTANCE.stormStrength = message.stormStrength;
			ctx.get().setPacketHandled(true);
		}
	}
}