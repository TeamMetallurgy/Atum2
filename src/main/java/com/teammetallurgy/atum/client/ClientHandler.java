package com.teammetallurgy.atum.client;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.blocks.lighting.AtumTorchUnlitBlock;
import com.teammetallurgy.atum.client.gui.block.GodforgeScreen;
import com.teammetallurgy.atum.client.gui.block.KilnScreen;
import com.teammetallurgy.atum.client.gui.block.TrapScreen;
import com.teammetallurgy.atum.client.gui.entity.AlphaDesertWolfScreen;
import com.teammetallurgy.atum.client.gui.entity.CamelScreen;
import com.teammetallurgy.atum.client.model.entity.*;
import com.teammetallurgy.atum.client.render.entity.PharaohOrbRender;
import com.teammetallurgy.atum.client.render.entity.TefnutsCallRender;
import com.teammetallurgy.atum.client.render.entity.mobs.*;
import com.teammetallurgy.atum.client.render.tileentity.*;
import com.teammetallurgy.atum.entity.projectile.arrow.CustomArrow;
import com.teammetallurgy.atum.init.*;
import com.teammetallurgy.atum.items.WandererDyeableArmor;
import com.teammetallurgy.atum.items.artifacts.anubis.AnubisWrathItem;
import com.teammetallurgy.atum.items.tools.BaseBowItem;
import com.teammetallurgy.atum.misc.AtumRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.LlamaSpitRenderer;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraft.client.renderer.entity.VillagerRenderer;
import net.minecraft.client.renderer.tileentity.SignTileEntityRenderer;
import net.minecraft.entity.EntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.FoliageColors;
import net.minecraft.world.biome.BiomeColors;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientHandler {
    private static final List<ResourceLocation> CHEST_ATLAS_TEXTURES = new ArrayList<>();
    private static final List<ResourceLocation> SHIELD_ATLAS_TEXTURES = new ArrayList<>();

    public static void init() {
        //Screens
        ScreenManager.registerFactory(AtumGuis.ALPHA_DESERT_WOLF, AlphaDesertWolfScreen::new);
        ScreenManager.registerFactory(AtumGuis.CAMEL, CamelScreen::new);
        ScreenManager.registerFactory(AtumGuis.KILN, KilnScreen::new);
        ScreenManager.registerFactory(AtumGuis.TRAP, TrapScreen::new);
        ScreenManager.registerFactory(AtumGuis.GODFORGE, GodforgeScreen::new);
        //Colors
        BlockColors blockColors = Minecraft.getInstance().getBlockColors();
        ItemColors itemColor = Minecraft.getInstance().getItemColors();
        //Palm Leave color
        itemColor.register((stack, tintIndex) -> {
            BlockState state = ((BlockItem) stack.getItem()).getBlock().getDefaultState();
            return Minecraft.getInstance().getBlockColors().getColor(state, null, null, tintIndex);
        }, AtumBlocks.PALM_LEAVES, AtumBlocks.DRY_LEAVES);
        blockColors.register((state, world, pos, tintIndex) -> world != null && pos != null ? BiomeColors.getFoliageColor(world, pos) : FoliageColors.getDefault(), AtumBlocks.PALM_LEAVES, AtumBlocks.DRY_LEAVES);
        //Dyeable armor
        itemColor.register((stack, tintIndex) -> tintIndex > 0 ? -1 : ((WandererDyeableArmor) stack.getItem()).getColor(stack), AtumItems.WANDERER_HELMET, AtumItems.WANDERER_CHEST, AtumItems.WANDERER_LEGS, AtumItems.WANDERER_BOOTS, AtumItems.DESERT_HELMET_IRON, AtumItems.DESERT_CHEST_IRON, AtumItems.DESERT_LEGS_IRON, AtumItems.DESERT_BOOTS_IRON, AtumItems.DESERT_HELMET_GOLD, AtumItems.DESERT_CHEST_GOLD, AtumItems.DESERT_LEGS_GOLD, AtumItems.DESERT_BOOTS_GOLD, AtumItems.DESERT_HELMET_DIAMOND, AtumItems.DESERT_CHEST_DIAMOND, AtumItems.DESERT_LEGS_DIAMOND, AtumItems.DESERT_BOOTS_DIAMOND);
        //Dead Grass
        itemColor.register((stack, tintIndex) -> {
            return 12889745;
        }, AtumBlocks.DRY_GRASS, AtumBlocks.TALL_DRY_GRASS);
        blockColors.register((state, world, pos, tintIndex) -> {
            if (world != null && pos != null) {
                return BiomeColors.getGrassColor(world, pos);
            } else {
                return 12889745;
            }
        }, AtumBlocks.DRY_GRASS, AtumBlocks.TALL_DRY_GRASS);
        ItemModelsProperties.registerProperty(AtumItems.ANUBIS_WRATH, new ResourceLocation("tier"), (stack, world, entity) -> AnubisWrathItem.getTier(stack));
        ItemModelsProperties.registerProperty(AtumItems.TEFNUTS_CALL, new ResourceLocation("throwing"), (stack, world, entity) -> entity != null && entity.isHandActive() && entity.getActiveItemStack() == stack ? 1.0F : 0.0F);
        registerBowModelProperties(AtumItems.SHORT_BOW);
        registerBowModelProperties(AtumItems.ANPUTS_GROUNDING);
        registerBowModelProperties(AtumItems.HORUS_SOARING);
        registerBowModelProperties(AtumItems.MONTUS_BLAST);
        registerBowModelProperties(AtumItems.ISIS_DIVISION);
        registerBowModelProperties(AtumItems.RAS_FURY);
        registerBowModelProperties(AtumItems.SETHS_VENOM);
        registerBowModelProperties(AtumItems.SHUS_BREATH);
        registerBowModelProperties(AtumItems.TEFNUTS_RAIN);
        registerShieldModelProperties(AtumItems.NUITS_IRE);
        registerShieldModelProperties(AtumItems.NUITS_QUARTER);
        registerShieldModelProperties(AtumItems.BRIGAND_SHIELD);
        registerShieldModelProperties(AtumItems.STONEGUARD_SHIELD);
        registerShieldModelProperties(AtumItems.ATEMS_PROTECTION);
    }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        RenderType cutout = RenderType.getCutout();
        RenderType translucent = RenderType.getTranslucent();
        RenderTypeLookup.setRenderLayer(AtumBlocks.ANPUTS_FINGERS, cutout);
        RenderTypeLookup.setRenderLayer(AtumBlocks.OASIS_GRASS, cutout);
        RenderTypeLookup.setRenderLayer(AtumBlocks.DRY_GRASS, cutout);
        RenderTypeLookup.setRenderLayer(AtumBlocks.TALL_DRY_GRASS, cutout);
        RenderTypeLookup.setRenderLayer(AtumBlocks.SHRUB, cutout);
        RenderTypeLookup.setRenderLayer(AtumBlocks.WEED, cutout);
        RenderTypeLookup.setRenderLayer(AtumBlocks.OPHIDIAN_TONGUE, cutout);
        RenderTypeLookup.setRenderLayer(AtumBlocks.BONE_LADDER, cutout);
        RenderTypeLookup.setRenderLayer(AtumBlocks.CRYSTAL_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.WHITE_STAINED_CRYSTAL_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.ORANGE_STAINED_CRYSTAL_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.MAGENTA_STAINED_CRYSTAL_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.LIGHT_BLUE_STAINED_CRYSTAL_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.YELLOW_STAINED_CRYSTAL_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.LIME_STAINED_CRYSTAL_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.PINK_STAINED_CRYSTAL_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.GRAY_STAINED_CRYSTAL_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.LIGHT_GRAY_STAINED_CRYSTAL_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.CYAN_STAINED_CRYSTAL_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.PURPLE_STAINED_CRYSTAL_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.BLUE_STAINED_CRYSTAL_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.BROWN_STAINED_CRYSTAL_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.GREEN_STAINED_CRYSTAL_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.RED_STAINED_CRYSTAL_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.BLACK_STAINED_CRYSTAL_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.CRYSTAL_GLASS_PANE, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.WHITE_STAINED_CRYSTAL_GLASS_PANE, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.ORANGE_STAINED_CRYSTAL_GLASS_PANE, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.MAGENTA_STAINED_CRYSTAL_GLASS_PANE, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.LIGHT_BLUE_STAINED_CRYSTAL_GLASS_PANE, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.YELLOW_STAINED_CRYSTAL_GLASS_PANE, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.LIME_STAINED_CRYSTAL_GLASS_PANE, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.PINK_STAINED_CRYSTAL_GLASS_PANE, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.GRAY_STAINED_CRYSTAL_GLASS_PANE, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.LIGHT_GRAY_STAINED_CRYSTAL_GLASS_PANE, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.CYAN_STAINED_CRYSTAL_GLASS_PANE, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.PURPLE_STAINED_CRYSTAL_GLASS_PANE, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.BLUE_STAINED_CRYSTAL_GLASS_PANE, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.BROWN_STAINED_CRYSTAL_GLASS_PANE, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.GREEN_STAINED_CRYSTAL_GLASS_PANE, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.RED_STAINED_CRYSTAL_GLASS_PANE, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.BLACK_STAINED_CRYSTAL_GLASS_PANE, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.PALM_FRAMED_CRYSTAL_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.WHITE_STAINED_PALM_FRAMED_CRYSTAL_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.ORANGE_STAINED_PALM_FRAMED_CRYSTAL_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.ORANGE_STAINED_PALM_FRAMED_CRYSTAL_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.LIGHT_BLUE_STAINED_PALM_FRAMED_CRYSTAL_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.YELLOW_STAINED_PALM_FRAMED_CRYSTAL_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.LIME_STAINED_PALM_FRAMED_CRYSTAL_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.PINK_STAINED_PALM_FRAMED_CRYSTAL_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.GRAY_STAINED_PALM_FRAMED_CRYSTAL_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.LIGHT_GRAY_STAINED_PALM_FRAMED_CRYSTAL_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.CYAN_STAINED_PALM_FRAMED_CRYSTAL_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.PURPLE_STAINED_PALM_FRAMED_CRYSTAL_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.BLUE_STAINED_PALM_FRAMED_CRYSTAL_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.BROWN_STAINED_PALM_FRAMED_CRYSTAL_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.GREEN_STAINED_PALM_FRAMED_CRYSTAL_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.RED_STAINED_PALM_FRAMED_CRYSTAL_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.BLACK_STAINED_PALM_FRAMED_CRYSTAL_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.PALM_FRAMED_CRYSTAL_GLASS_PANE, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.WHITE_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.ORANGE_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.MAGENTA_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.LIGHT_BLUE_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.YELLOW_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.LIME_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.PINK_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.GRAY_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.LIGHT_GRAY_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.CYAN_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.PURPLE_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.BLUE_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.BROWN_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.GREEN_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.RED_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.BLACK_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.DEADWOOD_FRAMED_CRYSTAL_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.WHITE_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.ORANGE_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.ORANGE_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.LIGHT_BLUE_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.YELLOW_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.LIME_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.PINK_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.GRAY_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.LIGHT_GRAY_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.CYAN_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.PURPLE_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.BLUE_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.BROWN_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.GREEN_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.RED_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.BLACK_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.WHITE_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.ORANGE_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.MAGENTA_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.LIGHT_BLUE_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.YELLOW_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.LIME_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.PINK_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.GRAY_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.LIGHT_GRAY_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.CYAN_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.PURPLE_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.BLUE_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.BROWN_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.GREEN_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.RED_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.BLACK_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.PALM_SAPLING, cutout);
        RenderTypeLookup.setRenderLayer(AtumBlocks.POTTED_PALM_SAPLING, cutout);
        RenderTypeLookup.setRenderLayer(AtumBlocks.PALM_LEAVES, cutout);
        RenderTypeLookup.setRenderLayer(AtumBlocks.DRY_LEAVES, cutout);
        RenderTypeLookup.setRenderLayer(AtumBlocks.PALM_LADDER, cutout);
        RenderTypeLookup.setRenderLayer(AtumBlocks.DEADWOOD_LADDER, cutout);
        RenderTypeLookup.setRenderLayer(AtumBlocks.DEADWOOD_HATCH, cutout);
        RenderTypeLookup.setRenderLayer(AtumBlocks.DEADWOOD_DOOR, cutout);
        RenderTypeLookup.setRenderLayer(AtumBlocks.PAPYRUS, cutout);
        RenderTypeLookup.setRenderLayer(AtumBlocks.FLAX, cutout);
        RenderTypeLookup.setRenderLayer(AtumBlocks.EMMER_WHEAT, cutout);
        RenderTypeLookup.setRenderLayer(AtumBlocks.PALM_SCAFFOLDING, cutout);
        RenderTypeLookup.setRenderLayer(AtumBlocks.DEADWOOD_SCAFFOLDING, cutout);
        for (Block torch : AtumTorchUnlitBlock.ALL_TORCHES) {
            RenderTypeLookup.setRenderLayer(torch, cutout);
        }
        RenderTypeLookup.setRenderLayer(AtumBlocks.NEBU_LANTERN, cutout);
        RenderTypeLookup.setRenderLayer(AtumBlocks.LANTERN_OF_ANPUT, cutout);
        RenderTypeLookup.setRenderLayer(AtumBlocks.LANTERN_OF_ANUBIS, cutout);
        RenderTypeLookup.setRenderLayer(AtumBlocks.LANTERN_OF_ATEM, cutout);
        RenderTypeLookup.setRenderLayer(AtumBlocks.LANTERN_OF_GEB, cutout);
        RenderTypeLookup.setRenderLayer(AtumBlocks.LANTERN_OF_HORUS, cutout);
        RenderTypeLookup.setRenderLayer(AtumBlocks.LANTERN_OF_ISIS, cutout);
        RenderTypeLookup.setRenderLayer(AtumBlocks.LANTERN_OF_MONTU, cutout);
        RenderTypeLookup.setRenderLayer(AtumBlocks.LANTERN_OF_NEPTHYS, cutout);
        RenderTypeLookup.setRenderLayer(AtumBlocks.LANTERN_OF_NUIT, cutout);
        RenderTypeLookup.setRenderLayer(AtumBlocks.LANTERN_OF_OSIRIS, cutout);
        RenderTypeLookup.setRenderLayer(AtumBlocks.LANTERN_OF_PTAH, cutout);
        RenderTypeLookup.setRenderLayer(AtumBlocks.LANTERN_OF_RA, cutout);
        RenderTypeLookup.setRenderLayer(AtumBlocks.LANTERN_OF_SETH, cutout);
        RenderTypeLookup.setRenderLayer(AtumBlocks.LANTERN_OF_SHU, cutout);
        RenderTypeLookup.setRenderLayer(AtumBlocks.LANTERN_OF_TEFNUT, cutout);

        ClientRegistry.bindTileEntityRenderer(AtumTileEntities.LIMESTONE_CHEST, TileChestRender::new);
        ClientRegistry.bindTileEntityRenderer(AtumTileEntities.SARCOPHAGUS, SarcophagusRender::new);
        ClientRegistry.bindTileEntityRenderer(AtumTileEntities.CRATE, CrateRender::new);
        ClientRegistry.bindTileEntityRenderer(AtumTileEntities.HEART_OF_RA, HeartOfRaBaseRender::new);
        //ClientRegistry.bindTileEntityRenderer(AtumTileEntities.RADIANT_BEACON, RadiantBeaconRender::new);
        ClientRegistry.bindTileEntityRenderer(AtumTileEntities.QUERN, QuernRender::new);
        ClientRegistry.bindTileEntityRenderer(AtumTileEntities.SIGN, SignTileEntityRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(AtumEntities.TARANTULA, TarantulaRender::new);
        RenderingRegistry.registerEntityRenderingHandler(AtumEntities.ASSASSIN, AtumBipedRender::new);
        RenderingRegistry.registerEntityRenderingHandler(AtumEntities.SERGEANT, AtumBipedRender::new);
        RenderingRegistry.registerEntityRenderingHandler(AtumEntities.BRIGAND, AtumBipedRender::new);
        RenderingRegistry.registerEntityRenderingHandler(AtumEntities.BARBARIAN, AtumBipedRender::new);
        RenderingRegistry.registerEntityRenderingHandler(AtumEntities.NOMAD, manager -> new AtumBipedRender<>(manager, new NomadModel<>(), new NomadModel<>(0.5F), new NomadModel<>(1.0F)));
        RenderingRegistry.registerEntityRenderingHandler(AtumEntities.BANDIT_WARLORD, AtumBipedRender::new);
        RenderingRegistry.registerEntityRenderingHandler(AtumEntities.PHARAOH, manager -> new AtumBipedRender<>(manager, new PharaohModel<>(0.0F), new PharaohModel<>(0.5F), new PharaohModel<>(1.0F)));
        RenderingRegistry.registerEntityRenderingHandler(AtumEntities.MUMMY, manager -> new AtumBipedRender<>(manager, new MonsterModel<>(0.0F, false), new MonsterModel<>(0.5F, false), new MonsterModel<>(1.0F, false)));
        RenderingRegistry.registerEntityRenderingHandler(AtumEntities.FORSAKEN, manager -> new AtumBipedRender<>(manager, new ForsakenModel(), new ForsakenModel(0.5F), new ForsakenModel(1.0F)));
        RenderingRegistry.registerEntityRenderingHandler(AtumEntities.WRAITH, manager -> new AtumBipedRender<>(manager, new MonsterModel<>(0.0F, false), new MonsterModel<>(0.5F, false), new MonsterModel<>(1.0F, false)));
        RenderingRegistry.registerEntityRenderingHandler(AtumEntities.VILLAGER, manager -> new AtumVillagerRenderer(manager, (IReloadableResourceManager) Minecraft.getInstance().getResourceManager()));
        RenderingRegistry.registerEntityRenderingHandler(AtumEntities.BONESTORM, manager -> new AtumMobRender<>(manager, new BonestormModel<>()));
        RenderingRegistry.registerEntityRenderingHandler(AtumEntities.STONEGUARD, AtumBipedRender::new);
        RenderingRegistry.registerEntityRenderingHandler(AtumEntities.STONEWARDEN, StonewardenRender::new);
        RenderingRegistry.registerEntityRenderingHandler(AtumEntities.STONEGUARD_FRIENDLY, AtumBipedRender::new);
        RenderingRegistry.registerEntityRenderingHandler(AtumEntities.STONEWARDEN_FRIENDLY, StonewardenRender::new);
        RenderingRegistry.registerEntityRenderingHandler(AtumEntities.DESERT_WOLF, DesertWolfRender::new);
        RenderingRegistry.registerEntityRenderingHandler(AtumEntities.CAMEL, CamelRender::new);
        RenderingRegistry.registerEntityRenderingHandler(AtumEntities.SCARAB, ScarabRender::new);
        RenderingRegistry.registerEntityRenderingHandler(AtumEntities.DESERT_RABBIT, DesertRabbitRender::new);
        RenderingRegistry.registerEntityRenderingHandler(AtumEntities.QUAIL, QuailRender::new);
        RenderingRegistry.registerEntityRenderingHandler(AtumEntities.PHARAOH_ORB, PharaohOrbRender::new);
        for (EntityType<? extends CustomArrow> arrow : AtumRegistry.ARROWS) {
            RenderingRegistry.registerEntityRenderingHandler(arrow, manager -> new ArrowRenderer<CustomArrow>(manager) {
                @Override
                @Nonnull
                public ResourceLocation getEntityTexture(@Nonnull CustomArrow entity) {
                    return entity.getTexture();
                }
            });
        }
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        RenderingRegistry.registerEntityRenderingHandler(AtumEntities.SMALL_BONE, manager -> new SpriteRenderer<>(manager, itemRenderer, 0.35F, true));
        RenderingRegistry.registerEntityRenderingHandler(AtumEntities.TEFNUTS_CALL, TefnutsCallRender::new);
        RenderingRegistry.registerEntityRenderingHandler(AtumEntities.CAMEL_SPIT, LlamaSpitRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(AtumEntities.QUAIL_EGG, manager -> new SpriteRenderer<>(manager, itemRenderer));
    }

    public static void registerBowModelProperties(BaseBowItem bow) {
        ItemModelsProperties.registerProperty(bow, new ResourceLocation("pull"), (stack, world, entity) -> {
            if (entity == null) {
                return 0.0F;
            } else {
                return entity.getActiveItemStack() != stack ? 0.0F : bow.getDrawbackSpeed(stack, entity);
            }
        });
        ItemModelsProperties.registerProperty(bow, new ResourceLocation("pulling"), (stack, world, entity) -> entity != null && entity.isHandActive() && entity.getActiveItemStack() == stack ? 1.0F : 0.0F);
    }

    public static void registerShieldModelProperties(Item shield) {
        ItemModelsProperties.registerProperty(shield, new ResourceLocation("blocking"), (stack, world, entity) -> entity != null && entity.isHandActive() && entity.getActiveItemStack() == stack ? 1.0F : 0.0F);
    }

    public static void addToChestAtlas(ResourceLocation location) {
        if (!CHEST_ATLAS_TEXTURES.contains(location)) {
            CHEST_ATLAS_TEXTURES.add(location);
        }
    }

    public static void addToShieldAtlas(ResourceLocation location) {
        if (!SHIELD_ATLAS_TEXTURES.contains(location)) {
            SHIELD_ATLAS_TEXTURES.add(location);
        }
    }

    @SubscribeEvent
    public static void onTextureStitch(TextureStitchEvent.Pre event) {
        if (event.getMap().getTextureLocation().equals(Atlases.CHEST_ATLAS)) {
            for (ResourceLocation location : CHEST_ATLAS_TEXTURES) {
                event.addSprite(location);
            }
        }
        if (event.getMap().getTextureLocation().equals(Atlases.SHIELD_ATLAS)) {
            for (ResourceLocation location : SHIELD_ATLAS_TEXTURES) {
                event.addSprite(location);
            }
        }
        if (event.getMap().getTextureLocation().equals(Atlases.SIGN_ATLAS)) {
            event.addSprite(new ResourceLocation("entity/signs/atum_palm"));
            event.addSprite(new ResourceLocation("entity/signs/atum_deadwood"));
        }
    }
}