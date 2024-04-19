package com.teammetallurgy.atum.network.packet.weather;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.world.SandstormHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class WeatherPacket implements CustomPacketPayload {
	public static final ResourceLocation ID = new ResourceLocation(Atum.MOD_ID, "weather");
	private final int stormTime;

	public WeatherPacket(int stormTime) {
		this.stormTime = stormTime;
	}

	public WeatherPacket(FriendlyByteBuf buf) {
		this.stormTime = buf.readInt();
	}

	@Override
	@Nonnull
	public ResourceLocation id() {
		return ID;
	}

	@Override
	public void write(@Nonnull FriendlyByteBuf buf) {
		buf.writeInt(this.stormTime);
	}

	public void handle(PlayPayloadContext context) {
		SandstormHandler.INSTANCE.stormTime = this.stormTime;
	}
}