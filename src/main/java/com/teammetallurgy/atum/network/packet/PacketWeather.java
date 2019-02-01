package com.teammetallurgy.atum.network.packet;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.entity.animal.EntityDesertWolf;
import com.teammetallurgy.atum.world.WorldProviderAtum;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.WorldProvider;

public class PacketWeather extends Packet<PacketWeather> {
	private boolean isStorming;
	private int stormTime;

	public PacketWeather() {
	}

	public PacketWeather(boolean isStorming, int stormTime) {
		this.isStorming = isStorming;
		this.stormTime = stormTime;
	}

	@Override
	protected void handleClientSide(EntityPlayer player) {
		//System.out.println(isStorming + " " + player.world.isRemote);
		WorldProvider provider = Minecraft.getMinecraft().player.world.provider;
		if(provider instanceof WorldProviderAtum) {
			WorldProviderAtum providerAtum = (WorldProviderAtum)provider;
			providerAtum.isStorming = isStorming;
			providerAtum.stormTime = stormTime;
		}
	}

	@Override
	protected void handleServerSide(EntityPlayer player) {
	}

	@Override
	protected void toBytes(PacketBuffer buffer) {
		buffer.writeBoolean(isStorming);
		buffer.writeInt(stormTime);
	}

	@Override
	protected void fromBytes(PacketBuffer buffer) {
		isStorming = buffer.readBoolean();
		stormTime = buffer.readInt();
	}
}