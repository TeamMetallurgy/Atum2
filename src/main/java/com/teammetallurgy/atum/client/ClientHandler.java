package com.teammetallurgy.atum.client;

import com.teammetallurgy.atum.client.gui.block.KilnScreen;
import com.teammetallurgy.atum.client.gui.block.TrapScreen;
import com.teammetallurgy.atum.client.gui.entity.AlphaDesertWolfScreen;
import com.teammetallurgy.atum.client.gui.entity.CamelScreen;
import com.teammetallurgy.atum.client.model.entity.BonestormModel;
import com.teammetallurgy.atum.client.model.entity.DustySkeletonModel;
import com.teammetallurgy.atum.client.model.entity.NomadModel;
import com.teammetallurgy.atum.client.render.entity.HeartOfRaRender;
import com.teammetallurgy.atum.client.render.entity.mobs.*;
import com.teammetallurgy.atum.client.render.tileentity.*;
import com.teammetallurgy.atum.entity.projectile.arrow.CustomArrow;
import com.teammetallurgy.atum.init.*;
import com.teammetallurgy.atum.items.TexturedArmorItem;
import com.teammetallurgy.atum.utils.AtumRegistry;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.LlamaSpitRenderer;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraft.client.renderer.entity.TridentRenderer;
import net.minecraft.client.renderer.entity.model.ZombieModel;
import net.minecraft.entity.EntityType;
import net.minecraft.item.BlockItem;
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

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientHandler {
    private static final List<ResourceLocation> CHEST_ATLAS_TEXTURES = new ArrayList<>();

    public static void init() {
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
        itemColor.register((stack, tintIndex) -> tintIndex > 0 ? -1 : ((TexturedArmorItem) stack.getItem()).getColor(stack), AtumItems.WANDERER_HELMET, AtumItems.WANDERER_CHEST, AtumItems.WANDERER_LEGS, AtumItems.WANDERER_BOOTS, AtumItems.DESERT_HELMET_IRON, AtumItems.DESERT_CHEST_IRON, AtumItems.DESERT_LEGS_IRON, AtumItems.DESERT_BOOTS_IRON, AtumItems.DESERT_HELMET_GOLD, AtumItems.DESERT_CHEST_GOLD, AtumItems.DESERT_LEGS_GOLD, AtumItems.DESERT_BOOTS_GOLD, AtumItems.DESERT_HELMET_DIAMOND, AtumItems.DESERT_CHEST_DIAMOND, AtumItems.DESERT_LEGS_DIAMOND, AtumItems.DESERT_BOOTS_DIAMOND);
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
        ClientRegistry.bindTileEntityRenderer(AtumTileEntities.CHEST_SPAWNER, TileChestRender::new);
        ClientRegistry.bindTileEntityRenderer(AtumTileEntities.LIMESTONE_CHEST, TileChestRender::new);
        ClientRegistry.bindTileEntityRenderer(AtumTileEntities.SARCOPHAGUS, SarcophagusRender::new);
        ClientRegistry.bindTileEntityRenderer(AtumTileEntities.CRATE, CrateRender::new);
        ClientRegistry.bindTileEntityRenderer(AtumTileEntities.HEART_OF_RA, HeartOfRaBaseRender::new);
        ClientRegistry.bindTileEntityRenderer(AtumTileEntities.RADIANT_BEACON, RadiantBeaconRender::new);
        ClientRegistry.bindTileEntityRenderer(AtumTileEntities.QUERN, QuernRender::new);
        RenderingRegistry.registerEntityRenderingHandler(AtumEntities.TARANTULA, TarantulaRender::new);
        RenderingRegistry.registerEntityRenderingHandler(AtumEntities.ASSASSIN, AtumBipedRender::new);
        RenderingRegistry.registerEntityRenderingHandler(AtumEntities.BRIGAND, AtumBipedRender::new);
        RenderingRegistry.registerEntityRenderingHandler(AtumEntities.BARBARIAN, AtumBipedRender::new);
        RenderingRegistry.registerEntityRenderingHandler(AtumEntities.NOMAD, manager -> new AtumBipedRender<>(manager, new NomadModel<>(), new NomadModel<>(0.5F), new NomadModel<>(1.0F)));
        RenderingRegistry.registerEntityRenderingHandler(AtumEntities.BANDIT_WARLORD, AtumBipedRender::new);
        RenderingRegistry.registerEntityRenderingHandler(AtumEntities.PHARAOH, AtumBipedRender::new);
        RenderingRegistry.registerEntityRenderingHandler(AtumEntities.MUMMY, manager -> new AtumBipedRender<>(manager, new ZombieModel(0.0F, false), new ZombieModel(0.5F, false), new ZombieModel(1.0F, false)));
        RenderingRegistry.registerEntityRenderingHandler(AtumEntities.FORSAKEN, manager -> new AtumBipedRender(manager, new DustySkeletonModel(), new DustySkeletonModel(0.5F), new DustySkeletonModel(1.0F)));
        RenderingRegistry.registerEntityRenderingHandler(AtumEntities.WRAITH, manager -> new AtumBipedRender<>(manager, new ZombieModel(0.0F, false), new ZombieModel(0.5F, false), new ZombieModel(1.0F, false)));
        RenderingRegistry.registerEntityRenderingHandler(AtumEntities.SUNSPEAKER, AtumBipedRender::new);
        RenderingRegistry.registerEntityRenderingHandler(AtumEntities.BONESTORM, manager -> new AtumMobRender<>(manager, new BonestormModel<>()));
        RenderingRegistry.registerEntityRenderingHandler(AtumEntities.STONEGUARD, AtumBipedRender::new);
        RenderingRegistry.registerEntityRenderingHandler(AtumEntities.STONEWARDEN, StonewardenRender::new);
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
        RenderingRegistry.registerEntityRenderingHandler(AtumEntities.TEFNUTS_CALL, TridentRenderer::new); //TODO
        RenderingRegistry.registerEntityRenderingHandler(AtumEntities.HEART_OF_RA, HeartOfRaRender::new);
        RenderingRegistry.registerEntityRenderingHandler(AtumEntities.CAMEL_SPIT, LlamaSpitRenderer::new);
    }

    public static void addToChestAtlas(ResourceLocation location) {
        if (!CHEST_ATLAS_TEXTURES.contains(location)) {
            CHEST_ATLAS_TEXTURES.add(location);
        }
    }

    @SubscribeEvent
    public static void onTextureStitch(TextureStitchEvent.Pre event) {
        if (event.getMap().getTextureLocation().equals(Atlases.CHEST_ATLAS)) {
            for (ResourceLocation location : CHEST_ATLAS_TEXTURES) {
                event.addSprite(location);
            }
        }
    }
}