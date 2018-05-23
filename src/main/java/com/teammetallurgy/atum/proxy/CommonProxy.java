package com.teammetallurgy.atum.proxy;

import com.teammetallurgy.atum.init.AtumParticles;
import com.teammetallurgy.atum.network.NetworkHandler;
import com.teammetallurgy.atum.network.packet.PacketParticle;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class CommonProxy {

    public void spawnParticle(AtumParticles.Types particleType, EntityPlayer player, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        PacketParticle packetParticle = new PacketParticle(particleType, (float) x, (float) y, (float) z, (float) xSpeed, (float) ySpeed, (float) zSpeed);
        NetworkHandler.WRAPPER.sendToAllAround(packetParticle, new NetworkRegistry.TargetPoint(player.dimension, player.posX, player.posY, player.posZ, 1024));
    }
}