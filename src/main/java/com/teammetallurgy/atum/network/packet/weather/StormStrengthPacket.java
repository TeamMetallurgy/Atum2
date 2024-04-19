package com.teammetallurgy.atum.network.packet.weather;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.world.SandstormHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

import javax.annotation.Nonnull;

public class StormStrengthPacket implements CustomPacketPayload {
	public static final ResourceLocation ID = new ResourceLocation(Atum.MOD_ID, "storm_strength");
	private final float stormStrength;

	public StormStrengthPacket(float stormStrength) {
		this.stormStrength = stormStrength;
	}

	public StormStrengthPacket(FriendlyByteBuf buf) {
		this.stormStrength = buf.readFloat();
	}

	@Override
	@Nonnull
	public ResourceLocation id() {
		return ID;
	}


	@Override
	public void write(@Nonnull FriendlyByteBuf buf) {
		buf.writeFloat(this.stormStrength);
	}

	public void handle(PlayPayloadContext context) {
		SandstormHandler.INSTANCE.stormStrength = this.stormStrength;
	}
}