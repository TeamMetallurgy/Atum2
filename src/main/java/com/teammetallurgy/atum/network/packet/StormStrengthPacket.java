package com.teammetallurgy.atum.network.packet;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class StormStrengthPacket {
	private final float stormStrength;

	public StormStrengthPacket(float stormStrength) {
		this.stormStrength = stormStrength;
	}

	public static void encode(StormStrengthPacket packet, PacketBuffer buf) {
		buf.writeFloat(packet.stormStrength);
	}

	public static StormStrengthPacket decode(PacketBuffer buf) {
		return new StormStrengthPacket(buf.readFloat());
	}

	public static class Handler {
		public static void handle(StormStrengthPacket message, Supplier<NetworkEvent.Context> ctx) {
			/*Dimension dimension = Minecraft.getInstance().player.world.dimension; //TODO
			if (dimension instanceof AtumDimension) {
				AtumDimension providerAtum = (AtumDimension) dimension;
				providerAtum.stormStrength = message.stormStrength;
			}*/
			ctx.get().setPacketHandled(true);
		}
	}
}