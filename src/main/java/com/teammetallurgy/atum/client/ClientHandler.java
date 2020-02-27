package com.teammetallurgy.atum.client;

import com.teammetallurgy.atum.blocks.base.tileentity.ChestBaseTileEntity;
import com.teammetallurgy.atum.blocks.beacon.tileentity.HeartOfRaTileEntity;
import com.teammetallurgy.atum.blocks.beacon.tileentity.RadiantBeaconTileEntity;
import com.teammetallurgy.atum.blocks.machines.tileentity.QuernTileEntity;
import com.teammetallurgy.atum.blocks.wood.tileentity.crate.CrateTileEntity;
import com.teammetallurgy.atum.client.gui.block.KilnScreen;
import com.teammetallurgy.atum.client.gui.block.TrapScreen;
import com.teammetallurgy.atum.client.gui.entity.AlphaDesertWolfScreen;
import com.teammetallurgy.atum.client.gui.entity.CamelScreen;
import com.teammetallurgy.atum.client.model.entity.BonestormModel;
import com.teammetallurgy.atum.client.model.entity.DustySkeletonModel;
import com.teammetallurgy.atum.client.model.entity.NomadModel;
import com.teammetallurgy.atum.client.render.entity.HeartOfRaRender;
import com.teammetallurgy.atum.client.render.entity.arrow.CamelSpitRender;
import com.teammetallurgy.atum.client.render.entity.mobs.*;
import com.teammetallurgy.atum.client.render.tileentity.*;
import com.teammetallurgy.atum.entity.HeartOfRaEntity;
import com.teammetallurgy.atum.entity.animal.*;
import com.teammetallurgy.atum.entity.bandit.*;
import com.teammetallurgy.atum.entity.efreet.SunspeakerEntity;
import com.teammetallurgy.atum.entity.projectile.CamelSpitEntity;
import com.teammetallurgy.atum.entity.projectile.SmallBoneEntity;
import com.teammetallurgy.atum.entity.projectile.arrow.CustomArrow;
import com.teammetallurgy.atum.entity.stone.StoneguardEntity;
import com.teammetallurgy.atum.entity.stone.StonewardenEntity;
import com.teammetallurgy.atum.entity.undead.*;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumGuis;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.items.TexturedArmorItem;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraft.client.renderer.entity.model.ZombieModel;
import net.minecraft.item.BlockItem;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.FoliageColors;
import net.minecraft.world.biome.BiomeColors;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientHandler {

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
        ClientRegistry.bindTileEntitySpecialRenderer(ChestBaseTileEntity.class, new TileChestRender());
        ClientRegistry.bindTileEntitySpecialRenderer(CrateTileEntity.class, new CrateRender());
        ClientRegistry.bindTileEntitySpecialRenderer(HeartOfRaTileEntity.class, new HeartOfRaBaseRender());
        ClientRegistry.bindTileEntitySpecialRenderer(RadiantBeaconTileEntity.class, new RadiantBeaconRender());
        ClientRegistry.bindTileEntitySpecialRenderer(QuernTileEntity.class, new QuernRender());
        RenderingRegistry.registerEntityRenderingHandler(TarantulaEntity.class, TarantulaRender::new);
        RenderingRegistry.registerEntityRenderingHandler(AssassinEntity.class, AtumBipedRender::new);
        RenderingRegistry.registerEntityRenderingHandler(BrigandEntity.class, AtumBipedRender::new);
        RenderingRegistry.registerEntityRenderingHandler(BarbarianEntity.class, AtumBipedRender::new);
        RenderingRegistry.registerEntityRenderingHandler(NomadEntity.class, manager -> new AtumBipedRender<>(manager, new NomadModel<>(), new NomadModel<>(0.5F), new NomadModel<>(1.0F)));
        RenderingRegistry.registerEntityRenderingHandler(WarlordEntity.class, AtumBipedRender::new);
        RenderingRegistry.registerEntityRenderingHandler(PharaohEntity.class, AtumBipedRender::new);
        RenderingRegistry.registerEntityRenderingHandler(MummyEntity.class, manager -> new AtumBipedRender<>(manager, new ZombieModel(), new ZombieModel(0.5F, false), new ZombieModel(1.0F, false)));
        RenderingRegistry.registerEntityRenderingHandler(ForsakenEntity.class, manager -> new AtumBipedRender(manager, new DustySkeletonModel(), new DustySkeletonModel(0.5F), new DustySkeletonModel(1.0F)));
        RenderingRegistry.registerEntityRenderingHandler(WraithEntity.class, manager -> new AtumBipedRender<>(manager, new ZombieModel(), new ZombieModel(0.5F, false), new ZombieModel(1.0F, false)));
        RenderingRegistry.registerEntityRenderingHandler(SunspeakerEntity.class, AtumBipedRender::new);
        RenderingRegistry.registerEntityRenderingHandler(BonestormEntity.class, manager -> new AtumMobRender<>(manager, new BonestormModel<>()));
        RenderingRegistry.registerEntityRenderingHandler(StoneguardEntity.class, AtumBipedRender::new);
        RenderingRegistry.registerEntityRenderingHandler(StonewardenEntity.class, StonewardenRender::new);
        RenderingRegistry.registerEntityRenderingHandler(DesertWolfEntity.class, DesertWolfRender::new);
        RenderingRegistry.registerEntityRenderingHandler(CamelEntity.class, CamelRender::new);
        RenderingRegistry.registerEntityRenderingHandler(ScarabEntity.class, ScarabRender::new);
        RenderingRegistry.registerEntityRenderingHandler(DesertRabbitEntity.class, DesertRabbitRender::new);
        RenderingRegistry.registerEntityRenderingHandler(CustomArrow.class, manager -> new ArrowRenderer<CustomArrow>(manager) {
            @Override
            protected ResourceLocation getEntityTexture(@Nonnull CustomArrow entity) {
                return entity.getTexture();
            }
        });
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        RenderingRegistry.registerEntityRenderingHandler(SmallBoneEntity.class, manager -> new SpriteRenderer<>(manager, itemRenderer, 0.35F));
        //RenderingRegistry.registerEntityRenderingHandler(TefnutsCallEntity.class, TefnutsCallReder::new); //TODO
        RenderingRegistry.registerEntityRenderingHandler(HeartOfRaEntity.class, HeartOfRaRender::new);
        RenderingRegistry.registerEntityRenderingHandler(CamelSpitEntity.class, CamelSpitRender::new);
    }
}