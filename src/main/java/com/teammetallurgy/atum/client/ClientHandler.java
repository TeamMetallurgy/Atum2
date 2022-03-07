package com.teammetallurgy.atum.client;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.blocks.lighting.AtumTorchUnlitBlock;
import com.teammetallurgy.atum.client.gui.block.GodforgeScreen;
import com.teammetallurgy.atum.client.gui.block.KilnScreen;
import com.teammetallurgy.atum.client.gui.block.TrapScreen;
import com.teammetallurgy.atum.client.gui.entity.AlphaDesertWolfScreen;
import com.teammetallurgy.atum.client.gui.entity.CamelScreen;
import com.teammetallurgy.atum.client.model.armor.AtemArmorModel;
import com.teammetallurgy.atum.client.model.armor.RaArmorModel;
import com.teammetallurgy.atum.client.model.entity.*;
import com.teammetallurgy.atum.client.model.shield.AtemsProtectionModel;
import com.teammetallurgy.atum.client.model.shield.BrigandShieldModel;
import com.teammetallurgy.atum.client.model.shield.StoneguardShieldModel;
import com.teammetallurgy.atum.client.render.entity.PharaohOrbRender;
import com.teammetallurgy.atum.client.render.entity.TefnutsCallRender;
import com.teammetallurgy.atum.client.render.entity.mobs.*;
import com.teammetallurgy.atum.client.render.tileentity.*;
import com.teammetallurgy.atum.entity.projectile.arrow.CustomArrow;
import com.teammetallurgy.atum.init.*;
import com.teammetallurgy.atum.items.WandererDyeableArmor;
import com.teammetallurgy.atum.items.artifacts.anubis.AnubisWrathItem;
import com.teammetallurgy.atum.items.tools.BaseBowItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.model.BlazeModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.TridentModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.LlamaSpitRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientHandler {
    private static final List<ResourceLocation> CHEST_ATLAS_TEXTURES = new ArrayList<>();
    private static final List<ResourceLocation> SHIELD_ATLAS_TEXTURES = new ArrayList<>();

    /**
     * Model Layers
     **/
    //Blocks
    public static final ModelLayerLocation CRATE = new ModelLayerLocation(new ResourceLocation(Atum.MOD_ID, "crate"), "crate");
    public static final ModelLayerLocation QUERN = new ModelLayerLocation(new ResourceLocation(Atum.MOD_ID, "quern"), "quern");
    public static final ModelLayerLocation SARCOPHAGUS = new ModelLayerLocation(new ResourceLocation(Atum.MOD_ID, "sarcophagus"), "sarcophagus");
    public static final ModelLayerLocation CURIO_DISPLAY = new ModelLayerLocation(new ResourceLocation(Atum.MOD_ID, "curio_display"), "curio_display");
    //Entities
    public static final ModelLayerLocation FORSAKEN = new ModelLayerLocation(new ResourceLocation(Atum.MOD_ID, "forsaken"), "forsaken");
    public static final ModelLayerLocation MUMMY = new ModelLayerLocation(new ResourceLocation(Atum.MOD_ID, "mummy"), "mummy");
    public static final ModelLayerLocation WRAITH = new ModelLayerLocation(new ResourceLocation(Atum.MOD_ID, "wraith"), "wraith");
    public static final ModelLayerLocation BONESTORM = new ModelLayerLocation(new ResourceLocation(Atum.MOD_ID, "bonestorm"), "bonestorm");
    public static final ModelLayerLocation PHARAOH = new ModelLayerLocation(new ResourceLocation(Atum.MOD_ID, "pharaoh"), "pharaoh");
    public static final ModelLayerLocation PHARAOH_ORB = new ModelLayerLocation(new ResourceLocation(Atum.MOD_ID, "pharaoh_orb"), "pharaoh_orb");
    public static final ModelLayerLocation STONEWARDEN = new ModelLayerLocation(new ResourceLocation(Atum.MOD_ID, "stonewarden"), "stonewarden");
    public static final ModelLayerLocation QUAIL = new ModelLayerLocation(new ResourceLocation(Atum.MOD_ID, "quail"), "quail");
    public static final ModelLayerLocation CAMEL = new ModelLayerLocation(new ResourceLocation(Atum.MOD_ID, "camel"), "camel");
    public static final ModelLayerLocation CAMEL_DECOR = new ModelLayerLocation(new ResourceLocation(Atum.MOD_ID, "camel"), "camel_decor");
    public static final ModelLayerLocation CAMEL_ARMOR = new ModelLayerLocation(new ResourceLocation(Atum.MOD_ID, "camel"), "camel_armor");
    public static final ModelLayerLocation DESERT_WOLF = new ModelLayerLocation(new ResourceLocation(Atum.MOD_ID, "desert_wolf"), "desert_wolf");
    public static final ModelLayerLocation DESERT_WOLF_ARMOR = new ModelLayerLocation(new ResourceLocation(Atum.MOD_ID, "desert_wolf"), "desert_wolf_armor");
    public static final ModelLayerLocation DESERT_WOLF_SADDLE = new ModelLayerLocation(new ResourceLocation(Atum.MOD_ID, "desert_wolf"), "desert_wolf_saddle");
    public static final ModelLayerLocation SERVAL = new ModelLayerLocation(new ResourceLocation(Atum.MOD_ID, "serval"), "serval");
    public static final ModelLayerLocation SERVAL_COLLAR = new ModelLayerLocation(new ResourceLocation(Atum.MOD_ID, "serval"), "serval_collar");
    public static final ModelLayerLocation TEFNUTS_CALL = new ModelLayerLocation(new ResourceLocation(Atum.MOD_ID, "tefnuts_call"), "tefnuts_call");
    //Items
    public static final ModelLayerLocation ATEM_ARMOR = new ModelLayerLocation(new ResourceLocation(Atum.MOD_ID, "atem_armor"), "atem_armor");
    public static final ModelLayerLocation RA_ARMOR = new ModelLayerLocation(new ResourceLocation(Atum.MOD_ID, "ra_armor"), "ra_armor");
    public static final ModelLayerLocation ATEMS_PROTECTION = new ModelLayerLocation(new ResourceLocation(Atum.MOD_ID, "atems_protection"), "atems_protection");
    public static final ModelLayerLocation BRIGAND_SHIELD = new ModelLayerLocation(new ResourceLocation(Atum.MOD_ID, "brigand_shield"), "brigand_shield");
    public static final ModelLayerLocation STONEGUARD_SHIELD = new ModelLayerLocation(new ResourceLocation(Atum.MOD_ID, "stoneguad_shield"), "stoneguad_shield");

    public static void init() {
        //Screens
        MenuScreens.register(AtumMenuType.ALPHA_DESERT_WOLF.get(), AlphaDesertWolfScreen::new);
        MenuScreens.register(AtumMenuType.CAMEL.get(), CamelScreen::new);
        MenuScreens.register(AtumMenuType.KILN.get(), KilnScreen::new);
        MenuScreens.register(AtumMenuType.TRAP.get(), TrapScreen::new);
        MenuScreens.register(AtumMenuType.GODFORGE.get(), GodforgeScreen::new);
        //Colors
        BlockColors blockColors = Minecraft.getInstance().getBlockColors();
        ItemColors itemColor = Minecraft.getInstance().getItemColors();
        //Palm Leave color
        itemColor.register((stack, tintIndex) -> {
            BlockState state = ((BlockItem) stack.getItem()).getBlock().defaultBlockState();
            return Minecraft.getInstance().getBlockColors().getColor(state, null, null, tintIndex);
        }, AtumBlocks.PALM_LEAVES.get(), AtumBlocks.DRY_LEAVES.get());
        blockColors.register((state, world, pos, tintIndex) -> world != null && pos != null ? BiomeColors.getAverageFoliageColor(world, pos) : FoliageColor.getDefaultColor(), AtumBlocks.PALM_LEAVES.get(), AtumBlocks.DRY_LEAVES.get());
        //Dyeable armor
        itemColor.register((stack, tintIndex) -> tintIndex > 0 ? -1 : ((WandererDyeableArmor) stack.getItem()).getColor(stack), AtumItems.WANDERER_HELMET.get(), AtumItems.WANDERER_CHEST.get(), AtumItems.WANDERER_LEGS.get(), AtumItems.WANDERER_BOOTS.get(), AtumItems.DESERT_HELMET_IRON.get(), AtumItems.DESERT_CHEST_IRON.get(), AtumItems.DESERT_LEGS_IRON.get(), AtumItems.DESERT_BOOTS_IRON.get(), AtumItems.DESERT_HELMET_GOLD.get(), AtumItems.DESERT_CHEST_GOLD.get(), AtumItems.DESERT_LEGS_GOLD.get(), AtumItems.DESERT_BOOTS_GOLD.get(), AtumItems.DESERT_HELMET_DIAMOND.get(), AtumItems.DESERT_CHEST_DIAMOND.get(), AtumItems.DESERT_LEGS_DIAMOND.get(), AtumItems.DESERT_BOOTS_DIAMOND.get());
        //Dead Grass
        itemColor.register((stack, tintIndex) -> {
            return 12889745;
        }, AtumBlocks.DRY_GRASS.get(), AtumBlocks.TALL_DRY_GRASS.get());
        blockColors.register((state, world, pos, tintIndex) -> {
            if (world != null && pos != null) {
                return BiomeColors.getAverageGrassColor(world, pos);
            } else {
                return 12889745;
            }
        }, AtumBlocks.DRY_GRASS.get(), AtumBlocks.TALL_DRY_GRASS.get());
        ItemProperties.register(AtumItems.ANUBIS_WRATH.get(), new ResourceLocation("tier"), (stack, world, entity, i) -> AnubisWrathItem.getTier(stack));
        ItemProperties.register(AtumItems.TEFNUTS_CALL.get(), new ResourceLocation("throwing"), (stack, world, entity, i) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);
        registerBowModelProperties(AtumItems.SHORT_BOW.get());
        registerBowModelProperties(AtumItems.ANPUTS_GROUNDING.get());
        registerBowModelProperties(AtumItems.HORUS_SOARING.get());
        registerBowModelProperties(AtumItems.MONTUS_BLAST.get());
        registerBowModelProperties(AtumItems.ISIS_DIVISION.get());
        registerBowModelProperties(AtumItems.RAS_FURY.get());
        registerBowModelProperties(AtumItems.SETHS_VENOM.get());
        registerBowModelProperties(AtumItems.SHUS_BREATH.get());
        registerBowModelProperties(AtumItems.TEFNUTS_RAIN.get());
        registerShieldModelProperties(AtumItems.NUITS_IRE.get());
        registerShieldModelProperties(AtumItems.NUITS_QUARTER.get());
        registerShieldModelProperties(AtumItems.BRIGAND_SHIELD.get());
        registerShieldModelProperties(AtumItems.STONEGUARD_SHIELD.get());
        registerShieldModelProperties(AtumItems.ATEMS_PROTECTION.get());

        Sheets.addWoodType(Atum.PALM);
        Sheets.addWoodType(Atum.DEADWOOD);
    }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        RenderType cutout = RenderType.cutout();
        RenderType cutoutMipped = RenderType.cutoutMipped();
        RenderType translucent = RenderType.translucent();
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.ANPUTS_FINGERS.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.OASIS_GRASS.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.DRY_GRASS.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.TALL_DRY_GRASS.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.SHRUB.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.WEED.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.OPHIDIAN_TONGUE.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.BONE_LADDER.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.CRYSTAL_GLASS.get(), translucent);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.WHITE_STAINED_CRYSTAL_GLASS.get(), translucent);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.ORANGE_STAINED_CRYSTAL_GLASS.get(), translucent);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.MAGENTA_STAINED_CRYSTAL_GLASS.get(), translucent);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.LIGHT_BLUE_STAINED_CRYSTAL_GLASS.get(), translucent);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.YELLOW_STAINED_CRYSTAL_GLASS.get(), translucent);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.LIME_STAINED_CRYSTAL_GLASS.get(), translucent);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.PINK_STAINED_CRYSTAL_GLASS.get(), translucent);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.GRAY_STAINED_CRYSTAL_GLASS.get(), translucent);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.LIGHT_GRAY_STAINED_CRYSTAL_GLASS.get(), translucent);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.CYAN_STAINED_CRYSTAL_GLASS.get(), translucent);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.PURPLE_STAINED_CRYSTAL_GLASS.get(), translucent);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.BLUE_STAINED_CRYSTAL_GLASS.get(), translucent);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.BROWN_STAINED_CRYSTAL_GLASS.get(), translucent);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.GREEN_STAINED_CRYSTAL_GLASS.get(), translucent);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.RED_STAINED_CRYSTAL_GLASS.get(), translucent);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.BLACK_STAINED_CRYSTAL_GLASS.get(), translucent);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.CRYSTAL_GLASS_PANE.get(), translucent);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.WHITE_STAINED_CRYSTAL_GLASS_PANE.get(), translucent);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.ORANGE_STAINED_CRYSTAL_GLASS_PANE.get(), translucent);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.MAGENTA_STAINED_CRYSTAL_GLASS_PANE.get(), translucent);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.LIGHT_BLUE_STAINED_CRYSTAL_GLASS_PANE.get(), translucent);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.YELLOW_STAINED_CRYSTAL_GLASS_PANE.get(), translucent);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.LIME_STAINED_CRYSTAL_GLASS_PANE.get(), translucent);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.PINK_STAINED_CRYSTAL_GLASS_PANE.get(), translucent);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.GRAY_STAINED_CRYSTAL_GLASS_PANE.get(), translucent);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.LIGHT_GRAY_STAINED_CRYSTAL_GLASS_PANE.get(), translucent);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.CYAN_STAINED_CRYSTAL_GLASS_PANE.get(), translucent);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.PURPLE_STAINED_CRYSTAL_GLASS_PANE.get(), translucent);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.BLUE_STAINED_CRYSTAL_GLASS_PANE.get(), translucent);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.BROWN_STAINED_CRYSTAL_GLASS_PANE.get(), translucent);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.GREEN_STAINED_CRYSTAL_GLASS_PANE.get(), translucent);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.RED_STAINED_CRYSTAL_GLASS_PANE.get(), translucent);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.BLACK_STAINED_CRYSTAL_GLASS_PANE.get(), translucent);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.PALM_FRAMED_CRYSTAL_GLASS.get(), translucent);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.WHITE_STAINED_PALM_FRAMED_CRYSTAL_GLASS.get(), translucent);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.ORANGE_STAINED_PALM_FRAMED_CRYSTAL_GLASS.get(), translucent);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.ORANGE_STAINED_PALM_FRAMED_CRYSTAL_GLASS.get(), translucent);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.LIGHT_BLUE_STAINED_PALM_FRAMED_CRYSTAL_GLASS.get(), translucent);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.YELLOW_STAINED_PALM_FRAMED_CRYSTAL_GLASS.get(), translucent);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.LIME_STAINED_PALM_FRAMED_CRYSTAL_GLASS.get(), translucent);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.PINK_STAINED_PALM_FRAMED_CRYSTAL_GLASS.get(), translucent);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.GRAY_STAINED_PALM_FRAMED_CRYSTAL_GLASS.get(), translucent);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.LIGHT_GRAY_STAINED_PALM_FRAMED_CRYSTAL_GLASS.get(), translucent);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.CYAN_STAINED_PALM_FRAMED_CRYSTAL_GLASS.get(), translucent);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.PURPLE_STAINED_PALM_FRAMED_CRYSTAL_GLASS.get(), translucent);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.BLUE_STAINED_PALM_FRAMED_CRYSTAL_GLASS.get(), translucent);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.BROWN_STAINED_PALM_FRAMED_CRYSTAL_GLASS.get(), translucent);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.GREEN_STAINED_PALM_FRAMED_CRYSTAL_GLASS.get(), translucent);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.RED_STAINED_PALM_FRAMED_CRYSTAL_GLASS.get(), translucent);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.BLACK_STAINED_PALM_FRAMED_CRYSTAL_GLASS.get(), translucent);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.PALM_FRAMED_CRYSTAL_GLASS_PANE.get(), translucent);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.WHITE_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE.get(), translucent);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.ORANGE_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE.get(), translucent);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.MAGENTA_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE.get(), translucent);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.LIGHT_BLUE_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE.get(), translucent);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.YELLOW_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE.get(), translucent);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.LIME_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE.get(), translucent);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.PINK_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE.get(), translucent);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.GRAY_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE.get(), translucent);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.LIGHT_GRAY_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE.get(), translucent);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.CYAN_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE.get(), translucent);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.PURPLE_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE.get(), translucent);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.BLUE_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE.get(), translucent);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.BROWN_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE.get(), translucent);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.GREEN_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE.get(), translucent);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.RED_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE.get(), translucent);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.BLACK_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE.get(), translucent);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.DEADWOOD_FRAMED_CRYSTAL_GLASS.get(), translucent);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.WHITE_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS.get(), translucent);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.ORANGE_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS.get(), translucent);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.ORANGE_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS.get(), translucent);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.LIGHT_BLUE_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS.get(), translucent);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.YELLOW_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS.get(), translucent);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.LIME_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS.get(), translucent);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.PINK_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS.get(), translucent);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.GRAY_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS.get(), translucent);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.LIGHT_GRAY_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS.get(), translucent);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.CYAN_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS.get(), translucent);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.PURPLE_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS.get(), translucent);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.BLUE_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS.get(), translucent);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.BROWN_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS.get(), translucent);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.GREEN_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS.get(), translucent);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.RED_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS.get(), translucent);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.BLACK_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS.get(), translucent);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE.get(), translucent);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.WHITE_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE.get(), translucent);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.ORANGE_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE.get(), translucent);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.MAGENTA_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE.get(), translucent);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.LIGHT_BLUE_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE.get(), translucent);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.YELLOW_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE.get(), translucent);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.LIME_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE.get(), translucent);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.PINK_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE.get(), translucent);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.GRAY_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE.get(), translucent);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.LIGHT_GRAY_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE.get(), translucent);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.CYAN_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE.get(), translucent);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.PURPLE_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE.get(), translucent);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.BLUE_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE.get(), translucent);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.BROWN_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE.get(), translucent);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.GREEN_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE.get(), translucent);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.RED_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE.get(), translucent);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.BLACK_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE.get(), translucent);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.PALM_SAPLING.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.POTTED_PALM_SAPLING.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.PALM_LEAVES.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.DRY_LEAVES.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.PALM_LADDER.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.DEADWOOD_LADDER.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.DEADWOOD_HATCH.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.DEADWOOD_DOOR.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.PAPYRUS.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.FLAX.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.EMMER_WHEAT.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.PALM_SCAFFOLDING.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.DEADWOOD_SCAFFOLDING.get(), cutout);
        for (Supplier<? extends Block> torch : AtumTorchUnlitBlock.ALL_TORCHES) {
            ItemBlockRenderTypes.setRenderLayer(torch.get(), cutout);
        }
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.NEBU_LANTERN.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.LANTERN_OF_ANPUT.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.LANTERN_OF_ANUBIS.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.LANTERN_OF_ATEM.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.LANTERN_OF_GEB.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.LANTERN_OF_HORUS.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.LANTERN_OF_ISIS.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.LANTERN_OF_MONTU.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.LANTERN_OF_NEPTHYS.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.LANTERN_OF_NUIT.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.LANTERN_OF_OSIRIS.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.LANTERN_OF_PTAH.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.LANTERN_OF_RA.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.LANTERN_OF_SETH.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.LANTERN_OF_SHU.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.LANTERN_OF_TEFNUT.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(AtumBlocks.NEBU_CHAIN.get(), cutoutMipped);

        BlockEntityRenderers.register(AtumTileEntities.LIMESTONE_CHEST.get(), AtumChestRenderer::new);
        BlockEntityRenderers.register(AtumTileEntities.SARCOPHAGUS.get(), SarcophagusRender::new);
        BlockEntityRenderers.register(AtumTileEntities.CRATE.get(), CrateRender::new);
        BlockEntityRenderers.register(AtumTileEntities.QUERN.get(), QuernRender::new);
        BlockEntityRenderers.register(AtumTileEntities.SIGN.get(), SignRenderer::new);
        BlockEntityRenderers.register(AtumTileEntities.PALM_CURIO_DISPLAY.get(), r -> new CurioDisplayTileEntityRender(r) {
            @Override
            public Block getBlock() {
                return AtumBlocks.PALM_CURIO_DISPLAY.get();
            }
        });
        BlockEntityRenderers.register(AtumTileEntities.DEADWOOD_CURIO_DISPLAY.get(), r -> new CurioDisplayTileEntityRender(r) {
            @Override
            public Block getBlock() {
                return AtumBlocks.DEADWOOD_CURIO_DISPLAY.get();
            }
        });
        BlockEntityRenderers.register(AtumTileEntities.ACACIA_CURIO_DISPLAY.get(), r -> new CurioDisplayTileEntityRender(r) {
            @Override
            public Block getBlock() {
                return AtumBlocks.ACACIA_CURIO_DISPLAY.get();
            }
        });
        BlockEntityRenderers.register(AtumTileEntities.LIMESTONE_CURIO_DISPLAY.get(), r -> new CurioDisplayTileEntityRender(r) {
            @Override
            public Block getBlock() {
                return AtumBlocks.LIMESTONE_CURIO_DISPLAY.get();
            }
        });
        BlockEntityRenderers.register(AtumTileEntities.ALABASTER_CURIO_DISPLAY.get(), r -> new CurioDisplayTileEntityRender(r) {
            @Override
            public Block getBlock() {
                return AtumBlocks.ALABASTER_CURIO_DISPLAY.get();
            }
        });
        BlockEntityRenderers.register(AtumTileEntities.PORPHYRY_CURIO_DISPLAY.get(), r -> new CurioDisplayTileEntityRender(r) {
            @Override
            public Block getBlock() {
                return AtumBlocks.PORPHYRY_CURIO_DISPLAY.get();
            }
        });
        BlockEntityRenderers.register(AtumTileEntities.NEBU_CURIO_DISPLAY.get(), r -> new CurioDisplayTileEntityRender(r) {
            @Override
            public Block getBlock() {
                return AtumBlocks.NEBU_CURIO_DISPLAY.get();
            }
        });
    }

    @SubscribeEvent
    public static void registerEntityRenders(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(AtumEntities.TARANTULA.get(), TarantulaRender::new);
        event.registerEntityRenderer(AtumEntities.ASSASSIN.get(), AtumBipedRender::new);
        event.registerEntityRenderer(AtumEntities.SERGEANT.get(), AtumBipedRender::new);
        event.registerEntityRenderer(AtumEntities.BRIGAND.get(), AtumBipedRender::new);
        event.registerEntityRenderer(AtumEntities.BARBARIAN.get(), AtumBipedRender::new);
        event.registerEntityRenderer(AtumEntities.NOMAD.get(), context -> new AtumBipedRender<>(context, new NomadModel<>(context), false));
        event.registerEntityRenderer(AtumEntities.BANDIT_WARLORD.get(), AtumBipedRender::new);
        event.registerEntityRenderer(AtumEntities.PHARAOH.get(), context -> new AtumBipedRender<>(context, new PharaohModel<>(context.bakeLayer(PHARAOH)), false));
        event.registerEntityRenderer(AtumEntities.MUMMY.get(), context -> new AtumBipedRender<>(context, new MonsterModel<>(context.bakeLayer(MUMMY)), false));
        event.registerEntityRenderer(AtumEntities.FORSAKEN.get(), context -> new AtumBipedRender<>(context, new NonRangedSkeletonModel(context.bakeLayer(FORSAKEN)), false));
        event.registerEntityRenderer(AtumEntities.WRAITH.get(), context -> new AtumBipedRender<>(context, new MonsterModel<>(context.bakeLayer(WRAITH)), false));
        event.registerEntityRenderer(AtumEntities.VILLAGER_MALE.get(), context -> new AtumVillagerRenderer(context, false));
        event.registerEntityRenderer(AtumEntities.VILLAGER_FEMALE.get(), context -> new AtumVillagerRenderer(context, true));
        event.registerEntityRenderer(AtumEntities.BONESTORM.get(), context -> new AtumMobRender<>(context, new BlazeModel<>(context.bakeLayer(BONESTORM))));
        event.registerEntityRenderer(AtumEntities.STONEGUARD.get(), AtumBipedRender::new);
        event.registerEntityRenderer(AtumEntities.STONEWARDEN.get(), StonewardenRender::new);
        event.registerEntityRenderer(AtumEntities.STONEGUARD_FRIENDLY.get(), AtumBipedRender::new);
        event.registerEntityRenderer(AtumEntities.STONEWARDEN_FRIENDLY.get(), StonewardenRender::new);
        event.registerEntityRenderer(AtumEntities.DESERT_WOLF.get(), DesertWolfRender::new);
        event.registerEntityRenderer(AtumEntities.CAMEL.get(), CamelRender::new);
        event.registerEntityRenderer(AtumEntities.SERVAL.get(), ServalRenderer::new);
        event.registerEntityRenderer(AtumEntities.SCARAB.get(), ScarabRender::new);
        event.registerEntityRenderer(AtumEntities.DESERT_RABBIT.get(), DesertRabbitRender::new);
        event.registerEntityRenderer(AtumEntities.QUAIL.get(), QuailRender::new);
        event.registerEntityRenderer(AtumEntities.PHARAOH_ORB.get(), PharaohOrbRender::new);
        registerArrowRender(AtumEntities.DOUBLE_ARROW.get(), event);
        registerArrowRender(AtumEntities.EXPLOSIVE_ARROW.get(), event);
        registerArrowRender(AtumEntities.FIRE_ARROW.get(), event);
        registerArrowRender(AtumEntities.POISON_ARROW.get(), event);
        registerArrowRender(AtumEntities.QUICKDRAW_ARROW.get(), event);
        registerArrowRender(AtumEntities.RAIN_ARROW.get(), event);
        registerArrowRender(AtumEntities.SLOWNESS_ARROW.get(), event);
        registerArrowRender(AtumEntities.STRAIGHT_ARROW.get(), event);
        event.registerEntityRenderer(AtumEntities.SMALL_BONE.get(), manager -> new ThrownItemRenderer<>(manager, 0.35F, true));
        event.registerEntityRenderer(AtumEntities.TEFNUTS_CALL.get(), TefnutsCallRender::new);
        event.registerEntityRenderer(AtumEntities.CAMEL_SPIT.get(), LlamaSpitRenderer::new);
        event.registerEntityRenderer(AtumEntities.QUAIL_EGG.get(), ThrownItemRenderer::new);
    }

    @SubscribeEvent
    public static void registerLayerDefinition(EntityRenderersEvent.RegisterLayerDefinitions event) {
        LayerDefinition zombie = LayerDefinition.create(HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F), 64, 64);

        event.registerLayerDefinition(CRATE, CrateRender::createLayer);
        event.registerLayerDefinition(QUERN, QuernRender::createLayer);
        event.registerLayerDefinition(SARCOPHAGUS, SarcophagusRender::createLayer);
        event.registerLayerDefinition(CURIO_DISPLAY, CurioDisplayTileEntityRender::createLayer);
        event.registerLayerDefinition(FORSAKEN, NonRangedSkeletonModel::createLayer);
        event.registerLayerDefinition(MUMMY, () -> zombie);
        event.registerLayerDefinition(WRAITH, () -> zombie);
        event.registerLayerDefinition(BONESTORM, BlazeModel::createBodyLayer);
        event.registerLayerDefinition(PHARAOH, PharaohModel::createLayer);
        event.registerLayerDefinition(PHARAOH_ORB, PharaohOrbModel::createLayer);
        event.registerLayerDefinition(CAMEL, () -> LayerDefinition.create(CamelModel.createMesh(CubeDeformation.NONE), 128, 64));
        event.registerLayerDefinition(CAMEL_ARMOR, () -> LayerDefinition.create(CamelModel.createMesh(new CubeDeformation(0.02F)), 128, 64));
        event.registerLayerDefinition(CAMEL_DECOR, () -> LayerDefinition.create(CamelModel.createMesh(new CubeDeformation(0.01F)), 128, 64));
        event.registerLayerDefinition(QUAIL, QuailModel::createLayer);
        event.registerLayerDefinition(STONEWARDEN, StonewardenModel::createLayer);
        event.registerLayerDefinition(DESERT_WOLF, () -> LayerDefinition.create(DesertWolfModel.createMesh(CubeDeformation.NONE), 64, 32));
        event.registerLayerDefinition(DESERT_WOLF_ARMOR, () -> LayerDefinition.create(DesertWolfModel.createMesh(new CubeDeformation(0.02F)), 64, 32));
        event.registerLayerDefinition(DESERT_WOLF_SADDLE, () -> LayerDefinition.create(DesertWolfModel.createMesh(new CubeDeformation(0.01F)), 64, 32));
        event.registerLayerDefinition(SERVAL, () -> LayerDefinition.create(ServalModel.createMesh(CubeDeformation.NONE), 64, 32));
        event.registerLayerDefinition(SERVAL_COLLAR, () -> LayerDefinition.create(ServalModel.createMesh(new CubeDeformation(0.01F)), 64, 32));
        event.registerLayerDefinition(TEFNUTS_CALL, TridentModel::createLayer);
        event.registerLayerDefinition(ATEM_ARMOR, AtemArmorModel::createLayer);
        event.registerLayerDefinition(RA_ARMOR, RaArmorModel::createLayer);
        event.registerLayerDefinition(ATEMS_PROTECTION, AtemsProtectionModel::createLayer);
        event.registerLayerDefinition(BRIGAND_SHIELD, BrigandShieldModel::createLayer);
        event.registerLayerDefinition(STONEGUARD_SHIELD, StoneguardShieldModel::createLayer);
    }

    public static void registerArrowRender(EntityType<? extends CustomArrow> arrow, EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(arrow, manager -> new ArrowRenderer<CustomArrow>(manager) {
            @Override
            @Nonnull
            public ResourceLocation getTextureLocation(@Nonnull CustomArrow entity) {
                return entity.getTexture();
            }
        });
    }

    public static void registerBowModelProperties(Item item) {
        ItemProperties.register(item, new ResourceLocation("pull"), (stack, world, entity, i) -> {
            if (entity == null) {
                return 0.0F;
            } else {
                return entity.getUseItem() != stack ? 0.0F : ((BaseBowItem) item).getDrawbackSpeed(stack, entity);
            }
        });
        ItemProperties.register(item, new ResourceLocation("pulling"), (stack, world, entity, i) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);
    }

    public static void registerShieldModelProperties(Item shield) {
        ItemProperties.register(shield, new ResourceLocation("blocking"), (stack, world, entity, i) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);
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
        if (event.getAtlas().location().equals(Sheets.CHEST_SHEET)) {
            for (ResourceLocation location : CHEST_ATLAS_TEXTURES) {
                event.addSprite(location);
            }
        }
        if (event.getAtlas().location().equals(Sheets.SHIELD_SHEET)) {
            for (ResourceLocation location : SHIELD_ATLAS_TEXTURES) {
                event.addSprite(location);
            }
        }
    }
}