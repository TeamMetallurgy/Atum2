package com.teammetallurgy.atum.network.packet;

import com.teammetallurgy.atum.init.AtumParticles;
import com.teammetallurgy.atum.network.NetworkHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class PacketParticle extends Packet<PacketParticle> {
    private AtumParticles.Types particle;
    private double xCoord;
    private double yCoord;
    private double zCoord;
    private double xSpeed;
    private double ySpeed;
    private double zSpeed;

    public PacketParticle() {
    }

    public PacketParticle(AtumParticles.Types particle, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        this.particle = particle;
        this.xCoord = x;
        this.yCoord = y;
        this.zCoord = z;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
        this.zSpeed = zSpeed;
    }

    @Override
    protected void handleClientSide(EntityPlayer player) {
    }

    @Override
    protected void handleServerSide(EntityPlayer player) {
        PacketParticle packetParticle = new PacketParticle(this.particle, this.xCoord, this.yCoord, this.zCoord, this.xSpeed, this.ySpeed, this.zSpeed);
        NetworkHandler.WRAPPER.sendToAllAround(packetParticle, new NetworkRegistry.TargetPoint(player.dimension, player.posX, player.posY, player.posZ, 1024));
    }

    @Override
    protected void toBytes(PacketBuffer buffer) {
        buffer.writeString(this.particle.getParticleName());
        buffer.writeDouble(this.xCoord);
        buffer.writeDouble(this.yCoord);
        buffer.writeDouble(this.zCoord);
        buffer.writeDouble(this.xSpeed);
        buffer.writeDouble(this.ySpeed);
        buffer.writeDouble(this.zSpeed);
    }

    @Override
    protected void fromBytes(PacketBuffer buffer) {
        this.particle = AtumParticles.Types.getParticleFromName(buffer.readString(256));

        if (this.particle == null) {
            this.particle = AtumParticles.Types.LIGHT_SPARKLE;
        }

        this.xCoord = buffer.readDouble();
        this.yCoord = buffer.readDouble();
        this.zCoord = buffer.readDouble();
        this.xSpeed = buffer.readDouble();
        this.ySpeed = buffer.readDouble();
        this.zSpeed = buffer.readDouble();
    }
}