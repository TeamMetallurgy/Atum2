package com.teammetallurgy.atum.client;

import com.teammetallurgy.atum.Atum;
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
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterDimensionSpecialEffectsEvent;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientHandler {
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
        blockColors.register((state, level, pos, tintIndex) -> level != null && pos != null ? BiomeColors.getAverageFoliageColor(level, pos) : FoliageColor.getDefaultColor(), AtumBlocks.PALM_LEAVES.get(), AtumBlocks.DRY_LEAVES.get());
        //Dyeable armor
        itemColor.register((stack, tintIndex) -> tintIndex > 0 ? -1 : ((WandererDyeableArmor) stack.getItem()).getColor(stack), AtumItems.WANDERER_HELMET.get(), AtumItems.WANDERER_CHEST.get(), AtumItems.WANDERER_LEGS.get(), AtumItems.WANDERER_BOOTS.get(), AtumItems.DESERT_HELMET_IRON.get(), AtumItems.DESERT_CHEST_IRON.get(), AtumItems.DESERT_LEGS_IRON.get(), AtumItems.DESERT_BOOTS_IRON.get(), AtumItems.DESERT_HELMET_GOLD.get(), AtumItems.DESERT_CHEST_GOLD.get(), AtumItems.DESERT_LEGS_GOLD.get(), AtumItems.DESERT_BOOTS_GOLD.get(), AtumItems.DESERT_HELMET_DIAMOND.get(), AtumItems.DESERT_CHEST_DIAMOND.get(), AtumItems.DESERT_LEGS_DIAMOND.get(), AtumItems.DESERT_BOOTS_DIAMOND.get());
        //Dead Grass
        itemColor.register((stack, tintIndex) -> {
            return 12889745;
        }, AtumBlocks.DRY_GRASS.get(), AtumBlocks.TALL_DRY_GRASS.get());
        blockColors.register((state, level, pos, tintIndex) -> {
            if (level != null && pos != null) {
                return BiomeColors.getAverageGrassColor(level, pos);
            } else {
                return 12889745;
            }
        }, AtumBlocks.DRY_GRASS.get(), AtumBlocks.TALL_DRY_GRASS.get());
        ItemProperties.register(AtumItems.ANUBIS_WRATH.get(), new ResourceLocation("tier"), (stack, level, entity, i) -> AnubisWrathItem.getTier(stack));
        //ItemProperties.register(AtumItems.TEFNUTS_CALL.get(), new ResourceLocation("throwing"), (stack, level, entity, i) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);
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
        event.registerLayerDefinition(CAMEL, () -> LayerDefinition.create(CamelModel.createMesh(new CubeDeformation(0.05F)), 128, 128));
        event.registerLayerDefinition(CAMEL_ARMOR, () -> LayerDefinition.create(CamelModel.createMesh(new CubeDeformation(0.07F)), 128, 128));
        event.registerLayerDefinition(CAMEL_DECOR, () -> LayerDefinition.create(CamelModel.createMesh(new CubeDeformation(0.06F)), 128, 128));
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
        ItemProperties.register(item, new ResourceLocation("pull"), (stack, level, entity, i) -> {
            if (entity == null) {
                return 0.0F;
            } else {
                return entity.getUseItem() != stack ? 0.0F : ((BaseBowItem) item).getDrawbackSpeed(stack, entity);
            }
        });
        ItemProperties.register(item, new ResourceLocation("pulling"), (stack, level, entity, i) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);
    }

    public static void registerShieldModelProperties(Item shield) {
        ItemProperties.register(shield, new ResourceLocation("blocking"), (stack, level, entity, i) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);
    }

    @SubscribeEvent
    public static void registerDimensionSpecialEffects(RegisterDimensionSpecialEffectsEvent event) {
        AtumSkyRenderer.createStars();
        AtumSkyRenderer.createLightSky();
        AtumSkyRenderer.createDarkSky();
        event.register(new ResourceLocation(Atum.MOD_ID, "atum_effects"), new AtumSpecialEffects());
    }
}