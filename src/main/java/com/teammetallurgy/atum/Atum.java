package com.teammetallurgy.atum;

import com.teammetallurgy.atum.blocks.stone.khnumite.KhnumiteFaceBlock;
import com.teammetallurgy.atum.client.ClientHandler;
import com.teammetallurgy.atum.integration.IntegrationHandler;
import com.teammetallurgy.atum.misc.AtumConfig;
import com.teammetallurgy.atum.misc.AtumItemGroup;
import com.teammetallurgy.atum.network.NetworkHandler;
import com.teammetallurgy.atum.world.biome.AtumBiome;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(value = Atum.MOD_ID)
public class Atum {
    public static final String MOD_ID = "atum";
    public static final Logger LOG = LogManager.getLogger(StringUtils.capitalize(MOD_ID));
    public static final ItemGroup GROUP = new AtumItemGroup();

    public Atum() {
        final IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(this::setupCommon);
        modBus.addListener(this::setupClient);
        MinecraftForge.EVENT_BUS.addListener(this::onServerStarting);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, AtumConfig.spec);
        IntegrationHandler.INSTANCE.addSupport();
    }

    private void setupCommon(FMLCommonSetupEvent event) {
        IntegrationHandler.INSTANCE.init();
        KhnumiteFaceBlock.addDispenserSupport();
        NetworkHandler.initialize();
        AtumConfig.Mobs.ENTITY_TYPE.forEach(AtumBiome::initMobSpawns);
        IntegrationHandler.INSTANCE.setup();
    }

    private void setupClient(FMLClientSetupEvent event) {
        ClientHandler.init();
        IntegrationHandler.INSTANCE.clientSide();
    }

    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        //AtumWeather.register(event.getCommandDispatcher()); //TODO
    }
}