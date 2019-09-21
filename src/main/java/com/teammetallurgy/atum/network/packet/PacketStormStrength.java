package com.teammetallurgy.atum.network.packet;

import com.teammetallurgy.atum.world.WorldProviderAtum;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.WorldProvider;

public class PacketStormStrength extends Packet<PacketStormStrength> {
	private float stormStrength;

	public PacketStormStrength() {
	}

	public PacketStormStrength(float stormStrength) {
		this.stormStrength = stormStrength;
	}

	@Override
	protected void handleClientSide(PlayerEntity player) {
		WorldProvider dimension = Minecraft.getInstance().player.world.dimension;
		if (dimension instanceof WorldProviderAtum) {
			WorldProviderAtum providerAtum = (WorldProviderAtum) dimension;
			providerAtum.stormStrength = stormStrength;
		}
	}

	@Override
	protected void handleServerSide(PlayerEntity player) {
	}

	@Override
	protected void toBytes(PacketBuffer buffer) {
		buffer.writeFloat(stormStrength);
	}

	@Override
	protected void fromBytes(PacketBuffer buffer) {
		stormStrength = buffer.readFloat();
	}
}