package com.teammetallurgy.atum;

import com.mojang.serialization.Codec;
import com.teammetallurgy.atum.api.AtumAPI;
import com.teammetallurgy.atum.blocks.stone.khnumite.KhnumiteFaceBlock;
import com.teammetallurgy.atum.client.ClientHandler;
import com.teammetallurgy.atum.commands.AtumWeather;
import com.teammetallurgy.atum.entity.ai.brain.sensor.AtumSensorTypes;
import com.teammetallurgy.atum.init.AtumVillagerProfession;
import com.teammetallurgy.atum.init.*;
import com.teammetallurgy.atum.integration.IntegrationHandler;
import com.teammetallurgy.atum.misc.AtumConfig;
import com.teammetallurgy.atum.misc.AtumItemGroup;
import com.teammetallurgy.atum.misc.datagenerator.BlockStatesGenerator;
import com.teammetallurgy.atum.misc.datagenerator.RecipeGenerator;
import com.teammetallurgy.atum.network.NetworkHandler;
import com.teammetallurgy.atum.world.SandstormHandler;
import com.teammetallurgy.atum.world.biome.AtumBiomeSource;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.theillusivec4.curios.api.SlotTypeMessage;
import top.theillusivec4.curios.api.SlotTypePreset;

@Mod(value = Atum.MOD_ID)
public class Atum {
    public static final String MOD_ID = "atum";
    public static final Logger LOG = LogManager.getLogger(StringUtils.capitalize(MOD_ID));
    public static final CreativeModeTab GROUP = new AtumItemGroup();
    public static final ResourceKey<Level> ATUM = ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(MOD_ID, "atum"));
    public static Codec<AtumBiomeSource> ATUM_MULTI_NOISE;
    public static final WoodType PALM = WoodType.create("atum_palm");
    public static final WoodType DEADWOOD = WoodType.create("atum_deadwood");

    public Atum() {
        final IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(this::setupCommon);
        modBus.addListener(this::setupClient);
        modBus.addListener(this::interModComms);
        modBus.addListener(this::gatherDataEvent);
        registerDeferredRegistries(modBus);
        MinecraftForge.EVENT_BUS.addListener(this::onCommandRegistering);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, AtumConfig.spec);
        IntegrationHandler.INSTANCE.addSupport();
        AtumAPI.Tags.init();
    }

    private void setupCommon(FMLCommonSetupEvent event) {
        ATUM_MULTI_NOISE = Registry.register(Registry.BIOME_SOURCE, new ResourceLocation(MOD_ID, "atum_multi_noise"), AtumBiomeSource.CODEC);
        AtumRecipes.addBrewingRecipes();
        IntegrationHandler.INSTANCE.init();
        event.enqueueWork(AtumBlocks::setBlockInfo);
        event.enqueueWork(AtumItems::setItemInfo);
        event.enqueueWork(() -> WoodType.register(PALM));
        event.enqueueWork(() -> WoodType.register(DEADWOOD));
        if (AtumConfig.SANDSTORM.sandstormEnabled.get()) {
            MinecraftForge.EVENT_BUS.register(SandstormHandler.INSTANCE);
        }
        //MinecraftForge.EVENT_BUS.register(AtumStructures.PYRAMID_STRUCTURE); //TODO Require structures functioning
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

    private void interModComms(InterModEnqueueEvent event) {
        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.CHARM.getMessageBuilder().build());
        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.NECKLACE.getMessageBuilder().build());
        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.RING.getMessageBuilder().build());
        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.BRACELET.getMessageBuilder().build());
    }

    public void gatherDataEvent(GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();

        if (event.includeClient()) {
            gen.addProvider(new BlockStatesGenerator(gen, event.getExistingFileHelper()));
        }

        if (event.includeServer()) {
            gen.addProvider(new RecipeGenerator(gen));
        }
    }

    public static void registerDeferredRegistries(IEventBus modBus) {
        AtumBlocks.BLOCK_DEFERRED.register(modBus);
        AtumItems.ITEM_DEFERRED.register(modBus);
        AtumEntities.ENTITY_DEFERRED.register(modBus);
        AtumTileEntities.BLOCK_ENTITY_DEFERRED.register(modBus);
        AtumBiomes.BIOME_DEFERRED.register(modBus);
        AtumMenuType.MENU_TYPE_DEFERRED.register(modBus);
        AtumPointsOfInterest.POI_DEFERRED.register(modBus);
        AtumSounds.SOUND_DEFERRED.register(modBus);
        AtumParticles.PARTICLE_DEFERRED.register(modBus);
        AtumVillagerProfession.ATUM_PROFESSION_DEFERRED.register(modBus);
        AtumSensorTypes.SENSOR_TYPE_DEFERRED.register(modBus);
    }
}