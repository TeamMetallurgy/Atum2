package com.teammetallurgy.atum.network.packet;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.entity.animal.DesertWolfEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

import javax.annotation.Nonnull;
import java.util.Optional;

public class OpenWolfGuiPacket implements CustomPacketPayload {
	public static final ResourceLocation ID = new ResourceLocation(Atum.MOD_ID, "open_wolf_gui");
	private final int wolfID;

	public OpenWolfGuiPacket(int wolfID) {
		this.wolfID = wolfID;
	}

	public OpenWolfGuiPacket(FriendlyByteBuf buf) {
		this.wolfID = buf.readInt();
	}

	@Override
	@Nonnull
	public ResourceLocation id() {
		return ID;
	}

	@Override
	public void write(@Nonnull FriendlyByteBuf buf) {
		buf.writeInt(this.wolfID);
	}

	public void handle(PlayPayloadContext context) {
		Optional<Player> optionalPlayer = context.player();
		if (optionalPlayer.isPresent() && optionalPlayer.get() instanceof ServerPlayer serverPlayer) {
			if (!(serverPlayer instanceof FakePlayer)) {
				Entity entity = serverPlayer.level().getEntity(this.wolfID);
				if (entity instanceof DesertWolfEntity wolf) {
					serverPlayer.openMenu(wolf, buf -> buf.writeInt(wolf.getId()));
				}
			}
		}
	}
}