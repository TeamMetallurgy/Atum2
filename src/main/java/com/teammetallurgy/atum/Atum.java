package com.teammetallurgy.atum;

import com.mojang.serialization.Codec;
import com.teammetallurgy.atum.api.AtumAPI;
import com.teammetallurgy.atum.blocks.stone.khnumite.KhnumiteFaceBlock;
import com.teammetallurgy.atum.client.ClientHandler;
import com.teammetallurgy.atum.commands.AtumWeather;
import com.teammetallurgy.atum.init.AtumStructures;
import com.teammetallurgy.atum.integration.IntegrationHandler;
import com.teammetallurgy.atum.misc.AtumConfig;
import com.teammetallurgy.atum.misc.AtumItemGroup;
import com.teammetallurgy.atum.network.NetworkHandler;
import com.teammetallurgy.atum.world.SandstormHandler;
import com.teammetallurgy.atum.world.biome.AtumBiomeProvider;
import com.teammetallurgy.atum.world.gen.AtumChunkGenerator;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.gen.NoiseChunkGenerator;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(value = Atum.MOD_ID)
public class Atum {
    public static final String MOD_ID = "atum";
    public static final Logger LOG = LogManager.getLogger(StringUtils.capitalize(MOD_ID));
    public static final ItemGroup GROUP = new AtumItemGroup();
    public static final RegistryKey<World> ATUM = RegistryKey.getOrCreateKey(Registry.WORLD_KEY, new ResourceLocation(Atum.MOD_ID, "atum"));
    public static final Codec<AtumBiomeProvider> ATUM_LAYERD = Registry.register(Registry.BIOME_PROVIDER_CODEC, new ResourceLocation(MOD_ID, "atum_layered"), AtumBiomeProvider.CODEC);
    public static final Codec<AtumChunkGenerator> ATUM_NOISE = Registry.register(Registry.CHUNK_GENERATOR_CODEC, new ResourceLocation(Atum.MOD_ID, "noise"), AtumChunkGenerator.CODEC);

    public Atum() {
        final IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(this::setupCommon);
        modBus.addListener(this::setupClient);
        MinecraftForge.EVENT_BUS.addListener(this::onCommandRegistering);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, AtumConfig.spec);
        IntegrationHandler.INSTANCE.addSupport();
        AtumAPI.Tags.init();
    }

    private void setupCommon(FMLCommonSetupEvent event) {
        IntegrationHandler.INSTANCE.init();
        if (AtumConfig.SANDSTORM.sandstormEnabled.get()) {
            MinecraftForge.EVENT_BUS.register(SandstormHandler.INSTANCE);
        }
        MinecraftForge.EVENT_BUS.register(AtumStructures.PYRAMID_STRUCTURE);
        KhnumiteFaceBlock.addDispenserSupport();
        NetworkHandler.initialize();
        IntegrationHandler.INSTANCE.setup();
    }

    private void setupClient(FMLClientSetupEvent event) {
        ClientHandler.init();
        IntegrationHandler.INSTANCE.clientSide();
    }

    @SubscribeEvent
    public void onCommandRegistering(RegisterCommandsEvent event) {
        AtumWeather.register(event.getDispatcher());
    }
}