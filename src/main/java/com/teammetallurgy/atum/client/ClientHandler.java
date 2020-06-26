package com.teammetallurgy.atum.client;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.blocks.wood.AtumTorchUnlitBlock;
import com.teammetallurgy.atum.client.gui.block.KilnScreen;
import com.teammetallurgy.atum.client.gui.block.TrapScreen;
import com.teammetallurgy.atum.client.gui.entity.AlphaDesertWolfScreen;
import com.teammetallurgy.atum.client.gui.entity.CamelScreen;
import com.teammetallurgy.atum.client.model.SeparatePerspectiveModel;
import com.teammetallurgy.atum.client.model.entity.BonestormModel;
import com.teammetallurgy.atum.client.model.entity.ForsakenModel;
import com.teammetallurgy.atum.client.model.entity.MonsterModel;
import com.teammetallurgy.atum.client.model.entity.NomadModel;
import com.teammetallurgy.atum.client.render.entity.HeartOfRaRender;
import com.teammetallurgy.atum.client.render.entity.TefnutsCallRender;
import com.teammetallurgy.atum.client.render.entity.mobs.*;
import com.teammetallurgy.atum.client.render.tileentity.*;
import com.teammetallurgy.atum.entity.projectile.arrow.CustomArrow;
import com.teammetallurgy.atum.init.*;
import com.teammetallurgy.atum.items.DyeableTexturedArmor;
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
import net.minecraft.entity.EntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.FoliageColors;
import net.minecraft.world.biome.BiomeColors;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
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
        ModelLoaderRegistry.registerLoader(new ResourceLocation(Atum.MOD_ID, "separate_perspective"), SeparatePerspectiveModel.Loader.INSTANCE);
        //Screens
        ScreenManager.registerFactory(AtumGuis.ALPHA_DESERT_WOLF, AlphaDesertWolfScreen::new);
        ScreenManager.registerFactory(AtumGuis.CAMEL, CamelScreen::new);
        ScreenManager.registerFactory(AtumGuis.KILN, KilnScreen::new);
        ScreenManager.registerFactory(AtumGuis.TRAP, TrapScreen::new);
        //Colors
        BlockColors blockColors = Minecraft.getInstance().getBlockColors();
        ItemColors itemColor = Minecraft.getInstance().getItemColors();
        //Palm Leave color
        itemColor.register((stack, tintIndex) -> {
            BlockState state = ((BlockItem) stack.getItem()).getBlock().getDefaultState();
            return Minecraft.getInstance().getBlockColors().getColor(state, null, null, tintIndex);
        }, AtumBlocks.PALM_LEAVES, AtumBlocks.DEADWOOD_LEAVES);
        blockColors.register((state, world, pos, tintIndex) -> world != null && pos != null ? BiomeColors.getFoliageColor(world, pos) : FoliageColors.getDefault(), AtumBlocks.PALM_LEAVES, AtumBlocks.DEADWOOD_LEAVES);
        //Dyeable armor
        itemColor.register((stack, tintIndex) -> tintIndex > 0 ? -1 : ((DyeableTexturedArmor) stack.getItem()).getColor(stack), AtumItems.WANDERER_HELMET, AtumItems.WANDERER_CHEST, AtumItems.WANDERER_LEGS, AtumItems.WANDERER_BOOTS, AtumItems.DESERT_HELMET_IRON, AtumItems.DESERT_CHEST_IRON, AtumItems.DESERT_LEGS_IRON, AtumItems.DESERT_BOOTS_IRON, AtumItems.DESERT_HELMET_GOLD, AtumItems.DESERT_CHEST_GOLD, AtumItems.DESERT_LEGS_GOLD, AtumItems.DESERT_BOOTS_GOLD, AtumItems.DESERT_HELMET_DIAMOND, AtumItems.DESERT_CHEST_DIAMOND, AtumItems.DESERT_LEGS_DIAMOND, AtumItems.DESERT_BOOTS_DIAMOND);
        //Dead Grass
        itemColor.register((stack, tintIndex) -> {
            BlockState blockState = ((BlockItem) stack.getItem()).getBlock().getDefaultState();
            return blockColors.getColor(blockState, null, null, tintIndex);
        }, AtumBlocks.DEAD_GRASS);
        blockColors.register((state, world, pos, tintIndex) -> {
            if (world != null && pos != null) {
                return BiomeColors.getGrassColor(world, pos);
            } else {
                return 12889745;
            }
        }, AtumBlocks.DEAD_GRASS);
    }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        RenderType cutoutMipped = RenderType.getCutoutMipped();
        RenderType translucent = RenderType.getTranslucent();
        RenderTypeLookup.setRenderLayer(AtumBlocks.OASIS_GRASS, cutoutMipped);
        RenderTypeLookup.setRenderLayer(AtumBlocks.DEAD_GRASS, cutoutMipped);
        RenderTypeLookup.setRenderLayer(AtumBlocks.SHRUB, cutoutMipped);
        RenderTypeLookup.setRenderLayer(AtumBlocks.WEED, cutoutMipped);
        RenderTypeLookup.setRenderLayer(AtumBlocks.OPHIDIAN_TONGUE, cutoutMipped);
        RenderTypeLookup.setRenderLayer(AtumBlocks.BONE_LADDER, cutoutMipped);
        RenderTypeLookup.setRenderLayer(AtumBlocks.RADIANT_BEACON, cutoutMipped);
        RenderTypeLookup.setRenderLayer(AtumBlocks.RADIANT_BEACON_FRAMED, cutoutMipped);
        RenderTypeLookup.setRenderLayer(AtumBlocks.CRYSTAL_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.CRYSTAL_WHITE_STAINED_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.CRYSTAL_ORANGE_STAINED_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.CRYSTAL_MAGENTA_STAINED_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.CRYSTAL_LIGHT_BLUE_STAINED_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.CRYSTAL_YELLOW_STAINED_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.CRYSTAL_LIME_STAINED_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.CRYSTAL_PINK_STAINED_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.CRYSTAL_GRAY_STAINED_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.CRYSTAL_LIGHT_GRAY_STAINED_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.CRYSTAL_CYAN_STAINED_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.CRYSTAL_PURPLE_STAINED_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.CRYSTAL_BLUE_STAINED_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.CRYSTAL_BROWN_STAINED_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.CRYSTAL_GREEN_STAINED_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.CRYSTAL_RED_STAINED_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.CRYSTAL_BLACK_STAINED_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.THIN_CRYSTAL_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.THIN_CRYSTAL_WHITE_STAINED_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.THIN_CRYSTAL_ORANGE_STAINED_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.THIN_CRYSTAL_MAGENTA_STAINED_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.THIN_CRYSTAL_LIGHT_BLUE_STAINED_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.THIN_CRYSTAL_YELLOW_STAINED_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.THIN_CRYSTAL_LIME_STAINED_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.THIN_CRYSTAL_PINK_STAINED_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.THIN_CRYSTAL_GRAY_STAINED_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.THIN_CRYSTAL_LIGHT_GRAY_STAINED_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.THIN_CRYSTAL_CYAN_STAINED_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.THIN_CRYSTAL_PURPLE_STAINED_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.THIN_CRYSTAL_BLUE_STAINED_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.THIN_CRYSTAL_BROWN_STAINED_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.THIN_CRYSTAL_GREEN_STAINED_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.THIN_CRYSTAL_RED_STAINED_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.THIN_CRYSTAL_BLACK_STAINED_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.FRAMED_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.FRAMED_WHITE_STAINED_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.FRAMED_ORANGE_STAINED_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.FRAMED_MAGENTA_STAINED_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.FRAMED_LIGHT_BLUE_STAINED_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.FRAMED_YELLOW_STAINED_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.FRAMED_LIME_STAINED_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.FRAMED_PINK_STAINED_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.FRAMED_GRAY_STAINED_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.FRAMED_LIGHT_GRAY_STAINED_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.FRAMED_CYAN_STAINED_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.FRAMED_PURPLE_STAINED_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.FRAMED_BLUE_STAINED_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.FRAMED_BROWN_STAINED_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.FRAMED_GREEN_STAINED_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.FRAMED_RED_STAINED_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.FRAMED_BLACK_STAINED_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.THIN_FRAMED_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.THIN_FRAMED_WHITE_STAINED_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.THIN_FRAMED_ORANGE_STAINED_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.THIN_FRAMED_MAGENTA_STAINED_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.THIN_FRAMED_LIGHT_BLUE_STAINED_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.THIN_FRAMED_YELLOW_STAINED_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.THIN_FRAMED_LIME_STAINED_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.THIN_FRAMED_PINK_STAINED_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.THIN_FRAMED_GRAY_STAINED_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.THIN_FRAMED_LIGHT_GRAY_STAINED_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.THIN_FRAMED_CYAN_STAINED_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.THIN_FRAMED_PURPLE_STAINED_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.THIN_FRAMED_BLUE_STAINED_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.THIN_FRAMED_BROWN_STAINED_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.THIN_FRAMED_GREEN_STAINED_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.THIN_FRAMED_RED_STAINED_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.THIN_FRAMED_BLACK_STAINED_GLASS, translucent);
        RenderTypeLookup.setRenderLayer(AtumBlocks.PALM_SAPLING, cutoutMipped);
        RenderTypeLookup.setRenderLayer(AtumBlocks.PALM_LEAVES, cutoutMipped);
        RenderTypeLookup.setRenderLayer(AtumBlocks.DEADWOOD_LEAVES, cutoutMipped);
        RenderTypeLookup.setRenderLayer(AtumBlocks.PALM_LADDER, cutoutMipped);
        RenderTypeLookup.setRenderLayer(AtumBlocks.DEADWOOD_LADDER, cutoutMipped);
        RenderTypeLookup.setRenderLayer(AtumBlocks.DEADWOOD_HATCH, cutoutMipped);
        RenderTypeLookup.setRenderLayer(AtumBlocks.DEADWOOD_DOOR, cutoutMipped);
        RenderTypeLookup.setRenderLayer(AtumBlocks.PAPYRUS, cutoutMipped);
        RenderTypeLookup.setRenderLayer(AtumBlocks.FLAX, cutoutMipped);
        RenderTypeLookup.setRenderLayer(AtumBlocks.EMMER_WHEAT, cutoutMipped);
        for (Block torch : AtumTorchUnlitBlock.ALL_TORCHES) {
            RenderTypeLookup.setRenderLayer(torch, cutoutMipped);
        }

        ClientRegistry.bindTileEntityRenderer(AtumTileEntities.CHEST_SPAWNER, TileChestRender::new);
        ClientRegistry.bindTileEntityRenderer(AtumTileEntities.LIMESTONE_CHEST, TileChestRender::new);
        ClientRegistry.bindTileEntityRenderer(AtumTileEntities.SARCOPHAGUS, SarcophagusRender::new);
        ClientRegistry.bindTileEntityRenderer(AtumTileEntities.CRATE, CrateRender::new);
        ClientRegistry.bindTileEntityRenderer(AtumTileEntities.HEART_OF_RA, HeartOfRaBaseRender::new);
        ClientRegistry.bindTileEntityRenderer(AtumTileEntities.RADIANT_BEACON, RadiantBeaconRender::new);
        ClientRegistry.bindTileEntityRenderer(AtumTileEntities.QUERN, QuernRender::new);
        RenderingRegistry.registerEntityRenderingHandler(AtumEntities.TARANTULA, TarantulaRender::new);
        RenderingRegistry.registerEntityRenderingHandler(AtumEntities.ASSASSIN, AtumBipedRender::new);
        RenderingRegistry.registerEntityRenderingHandler(AtumEntities.SERGEANT, AtumBipedRender::new);
        RenderingRegistry.registerEntityRenderingHandler(AtumEntities.BRIGAND, AtumBipedRender::new);
        RenderingRegistry.registerEntityRenderingHandler(AtumEntities.BARBARIAN, AtumBipedRender::new);
        RenderingRegistry.registerEntityRenderingHandler(AtumEntities.NOMAD, manager -> new AtumBipedRender<>(manager, new NomadModel<>(), new NomadModel<>(0.5F), new NomadModel<>(1.0F)));
        RenderingRegistry.registerEntityRenderingHandler(AtumEntities.BANDIT_WARLORD, AtumBipedRender::new);
        RenderingRegistry.registerEntityRenderingHandler(AtumEntities.PHARAOH, AtumBipedRender::new);
        RenderingRegistry.registerEntityRenderingHandler(AtumEntities.MUMMY, manager -> new AtumBipedRender<>(manager, new MonsterModel<>(0.0F, false), new MonsterModel<>(0.5F, false), new MonsterModel<>(1.0F, false)));
        RenderingRegistry.registerEntityRenderingHandler(AtumEntities.FORSAKEN, manager -> new AtumBipedRender<>(manager, new ForsakenModel(), new ForsakenModel(0.5F), new ForsakenModel(1.0F)));
        RenderingRegistry.registerEntityRenderingHandler(AtumEntities.WRAITH, manager -> new AtumBipedRender<>(manager, new MonsterModel<>(0.0F, false), new MonsterModel<>(0.5F, false), new MonsterModel<>(1.0F, false)));
        RenderingRegistry.registerEntityRenderingHandler(AtumEntities.SUNSPEAKER, AtumBipedRender::new);
        RenderingRegistry.registerEntityRenderingHandler(AtumEntities.BONESTORM, manager -> new AtumMobRender<>(manager, new BonestormModel<>()));
        RenderingRegistry.registerEntityRenderingHandler(AtumEntities.STONEGUARD, AtumBipedRender::new);
        RenderingRegistry.registerEntityRenderingHandler(AtumEntities.STONEWARDEN, StonewardenRender::new);
        RenderingRegistry.registerEntityRenderingHandler(AtumEntities.STONEGUARD_FRIENDLY, AtumBipedRender::new);
        RenderingRegistry.registerEntityRenderingHandler(AtumEntities.STONEWARDEN_FRIENDLY, StonewardenRender::new);
        RenderingRegistry.registerEntityRenderingHandler(AtumEntities.DESERT_WOLF, DesertWolfRender::new);
        RenderingRegistry.registerEntityRenderingHandler(AtumEntities.CAMEL, CamelRender::new);
        RenderingRegistry.registerEntityRenderingHandler(AtumEntities.SCARAB, ScarabRender::new);
        RenderingRegistry.registerEntityRenderingHandler(AtumEntities.DESERT_RABBIT, DesertRabbitRender::new);
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
        RenderingRegistry.registerEntityRenderingHandler(AtumEntities.HEART_OF_RA, HeartOfRaRender::new);
        RenderingRegistry.registerEntityRenderingHandler(AtumEntities.CAMEL_SPIT, LlamaSpitRenderer::new);
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
    }
}