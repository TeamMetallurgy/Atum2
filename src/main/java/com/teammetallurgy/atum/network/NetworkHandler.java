package com.teammetallurgy.atum.network;

import com.teammetallurgy.atum.network.packet.PacketOpenWolfGui;
import com.teammetallurgy.atum.network.packet.PacketParticle;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class NetworkHandler {
    public static SimpleNetworkWrapper WRAPPER = NetworkRegistry.INSTANCE.newSimpleChannel(Constants.MOD_ID);
    private static int lastDiscriminator = 0;

    private NetworkHandler(String modId) {
        WRAPPER = NetworkRegistry.INSTANCE.newSimpleChannel(modId);
    }

    public static void register() {
        lastDiscriminator++;
        registerPacket(PacketParticle.class, Side.CLIENT);
        registerPacket(PacketOpenWolfGui.class, Side.SERVER);
    }

    @SuppressWarnings("all")
    private static void registerPacket(Class packetType, Side side) {
        WRAPPER.registerMessage(packetType, packetType, lastDiscriminator++, side);
    }
}