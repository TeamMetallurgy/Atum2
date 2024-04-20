package com.teammetallurgy.atum;

import com.teammetallurgy.atum.api.AtumAPI;
import com.teammetallurgy.atum.api.recipe.AtumRecipeTypes;
import com.teammetallurgy.atum.blocks.stone.khnumite.KhnumiteFaceBlock;
import com.teammetallurgy.atum.client.ClientHandler;
import com.teammetallurgy.atum.client.render.ItemStackRenderer;
import com.teammetallurgy.atum.commands.AtumWeather;
import com.teammetallurgy.atum.entity.ai.brain.sensor.AtumSensorTypes;
import com.teammetallurgy.atum.init.*;
import com.teammetallurgy.atum.integration.IntegrationHandler;
import com.teammetallurgy.atum.misc.AtumConfig;
import com.teammetallurgy.atum.world.SandstormHandler;
import com.teammetallurgy.atum.world.gen.feature.tree.TreePlacerTypes;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.InterModComms;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.event.lifecycle.InterModEnqueueEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.theillusivec4.curios.api.SlotTypeMessage;
import top.theillusivec4.curios.api.SlotTypePreset;

@Mod(value = Atum.MOD_ID)
public class Atum {
    public static final String MOD_ID = "atum";
    public static final ResourceLocation LOCATION = new ResourceLocation(MOD_ID, "atum");
    public static final Logger LOG = LogManager.getLogger(StringUtils.capitalize(MOD_ID));
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID);
    public static DeferredHolder<CreativeModeTab, CreativeModeTab> GROUP = CREATIVE_TABS.register("tab", () -> new CreativeModeTab.Builder(CreativeModeTab.Row.TOP, 0)
            .icon(() -> new ItemStack(AtumItems.SCARAB.get()))
            .title(Component.translatable("tabs." + MOD_ID + ".tab"))
            .displayItems((featureFlagSet, tabOutput) -> {
                AtumItems.ITEMS_FOR_TAB_LIST
                        .forEach(registryObject -> tabOutput.accept(new ItemStack(registryObject.get())));
            }).build());
    public static final ResourceKey<Level> ATUM = ResourceKey.create(Registries.DIMENSION, LOCATION);
    public static final WoodType PALM = WoodType.register(new WoodType("atum_palm", AtumBlockSetType.PALM));
    public static final WoodType DEADWOOD = WoodType.register(new WoodType("atum_deadwood", AtumBlockSetType.DEADWOOD));

    public Atum(IEventBus modBus) {
        modBus.addListener(this::setupCommon);
        modBus.addListener(this::setupClient);
        modBus.addListener(this::interModComms);
        this.registerDeferredRegistries(modBus);
        NeoForge.EVENT_BUS.addListener(this::onCommandRegistering);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, AtumConfig.spec);
        IntegrationHandler.INSTANCE.addSupport();
        AtumAPI.Tags.init();
    }

    private void setupCommon(FMLCommonSetupEvent event) {
        IntegrationHandler.INSTANCE.init();
        event.enqueueWork(AtumRecipes::addBrewingRecipes);
        event.enqueueWork(AtumBlocks::setBlockInfo);
        event.enqueueWork(AtumItems::setItemInfo);
        event.enqueueWork(() -> WoodType.register(PALM));
        event.enqueueWork(() -> WoodType.register(DEADWOOD));
        if (AtumConfig.SANDSTORM.sandstormEnabled.get()) {
            NeoForge.EVENT_BUS.register(SandstormHandler.INSTANCE);
        }
        event.enqueueWork(KhnumiteFaceBlock::addDispenserSupport);
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
        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE,
                () -> SlotTypePreset.CHARM.getMessageBuilder().build());
        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE,
                () -> SlotTypePreset.NECKLACE.getMessageBuilder().build());
        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE,
                () -> SlotTypePreset.RING.getMessageBuilder().build());
        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE,
                () -> SlotTypePreset.BRACELET.getMessageBuilder().build());
    }

    public void registerDeferredRegistries(IEventBus modBus) {
        AtumBlocks.BLOCK_DEFERRED.register(modBus);
        AtumItems.ITEM_DEFERRED.register(modBus);
        CREATIVE_TABS.register(modBus);
        AtumEntities.ENTITY_DEFERRED.register(modBus);
        AtumTileEntities.BLOCK_ENTITY_DEFERRED.register(modBus);
        AtumFeatures.FEATURES_DEFERRED.register(modBus);
        AtumStructures.STRUCTURE_TYPE_DEFERRED.register(modBus);
        AtumStructurePieces.STRUCTURE_PIECES_DEFERRED.register(modBus);
        AtumEffects.MOB_EFFECT_DEFERRED.register(modBus);
        TreePlacerTypes.FOLIAGE_PLACER_DEFERRED.register(modBus);
        AtumMenuType.MENU_TYPE_DEFERRED.register(modBus);
        AtumPoiTypes.POI_DEFERRED.register(modBus);
        AtumSounds.SOUND_DEFERRED.register(modBus);
        AtumParticles.PARTICLE_DEFERRED.register(modBus);
        AtumSensorTypes.SENSOR_TYPE_DEFERRED.register(modBus);
        AtumDataSerializer.DATA_SERIALIZER_DEFERRED.register(modBus);
        AtumRecipeTypes.RECIPE_TYPE_DEFERRED.register(modBus);
        AtumRecipeSerializers.RECIPE_SERIALIZER_DEFERRED.register(modBus);
    }

    public void newRegistryEvent(NewRegistryEvent event) {
        event.register(AtumVillagerProfession.VILLAGER_PROFESSION);
    }
}