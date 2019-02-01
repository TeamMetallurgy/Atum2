package com.teammetallurgy.atum.network.packet;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.entity.animal.EntityDesertWolf;
import com.teammetallurgy.atum.world.WorldProviderAtum;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
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
	protected void handleClientSide(EntityPlayer player) {
		//System.out.println(isStorming + " " + player.world.isRemote);
		WorldProvider provider = Minecraft.getMinecraft().player.world.provider;
		if(provider instanceof WorldProviderAtum) {
			WorldProviderAtum providerAtum = (WorldProviderAtum)provider;
			providerAtum.stormStrength = stormStrength;
		}
	}

	@Override
	protected void handleServerSide(EntityPlayer player) {
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