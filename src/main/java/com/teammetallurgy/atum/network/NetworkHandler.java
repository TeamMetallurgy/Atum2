package com.teammetallurgy.atum.network;

import com.teammetallurgy.atum.network.packet.OpenWolfGuiPacket;
import com.teammetallurgy.atum.network.packet.StormStrengthPacket;
import com.teammetallurgy.atum.network.packet.WeatherPacket;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class NetworkHandler {
    public static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(Constants.MOD_ID, "atum_channel"))
            .clientAcceptedVersions(v -> true)
            .serverAcceptedVersions(v -> true)
            .networkProtocolVersion(() -> "ATUM1")
            .simpleChannel();

    public static void initialize() {
        CHANNEL.registerMessage(0, OpenWolfGuiPacket.class, OpenWolfGuiPacket::encode, OpenWolfGuiPacket::decode, OpenWolfGuiPacket.Handler::handle);
        CHANNEL.registerMessage(1, WeatherPacket.class, WeatherPacket::encode, WeatherPacket::decode, WeatherPacket.Handler::handle);
        CHANNEL.registerMessage(2, StormStrengthPacket.class, StormStrengthPacket::encode, StormStrengthPacket::decode, StormStrengthPacket.Handler::handle);
    }
}