package com.teammetallurgy.atum.proxy;

import com.teammetallurgy.atum.init.AtumParticles;
import com.teammetallurgy.atum.network.NetworkHandler;
import com.teammetallurgy.atum.network.packet.PacketParticle;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class CommonProxy {

    public void init() {
    }

    public void spawnParticle(AtumParticles.Types particleType, Entity entity, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        PacketParticle packetParticle = new PacketParticle(particleType, (float) x, (float) y, (float) z, (float) xSpeed, (float) ySpeed, (float) zSpeed);
        NetworkHandler.WRAPPER.sendToAllTracking(packetParticle, new NetworkRegistry.TargetPoint(entity.dimension, entity.posX, entity.posY, entity.posZ, 248));
    }
}