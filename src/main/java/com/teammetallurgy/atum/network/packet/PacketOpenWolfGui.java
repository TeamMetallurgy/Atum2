package com.teammetallurgy.atum.network.packet;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.entity.animal.EntityDesertWolf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;

import java.io.IOException;

public class PacketOpenWolfGui extends Packet<PacketOpenWolfGui> {
	private int wolfID;

	public PacketOpenWolfGui() {
	}

	public PacketOpenWolfGui(int wolfID) {
		this.wolfID = wolfID;
	}

	@Override
	protected void handleClientSide(EntityPlayer player) {
	}

	@Override
	protected void handleServerSide(EntityPlayer player) {
		Entity wolf = player.world.getEntityByID(wolfID);
		if (wolf instanceof EntityDesertWolf) {
			((EntityDesertWolf) wolf).getInventory().setCustomName(wolf.getName());
			player.openGui(Atum.instance, 4, player.world, wolfID, 0, 0);
		}
	}

	@Override
	protected void toBytes(PacketBuffer buffer) {
		buffer.writeInt(wolfID);
	}

	@Override
	protected void fromBytes(PacketBuffer buffer) {
		wolfID = buffer.readInt();
	}
}