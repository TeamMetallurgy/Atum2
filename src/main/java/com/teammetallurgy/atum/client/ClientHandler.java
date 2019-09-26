package com.teammetallurgy.atum.client;

import com.teammetallurgy.atum.blocks.base.IRenderMapper;
import com.teammetallurgy.atum.blocks.base.tileentity.TileEntityChestBase;
import com.teammetallurgy.atum.blocks.beacon.tileentity.TileEntityHeartOfRa;
import com.teammetallurgy.atum.blocks.beacon.tileentity.TileEntityRadiantBeacon;
import com.teammetallurgy.atum.blocks.machines.tileentity.TileEntityQuern;
import com.teammetallurgy.atum.blocks.wood.BlockAtumPlank;
import com.teammetallurgy.atum.blocks.wood.BlockLeave;
import com.teammetallurgy.atum.blocks.wood.tileentity.crate.TileEntityCrate;
import com.teammetallurgy.atum.client.model.entity.ModelDesertWolf;
import com.teammetallurgy.atum.client.model.entity.ModelDustySkeleton;
import com.teammetallurgy.atum.client.model.entity.ModelNomad;
import com.teammetallurgy.atum.client.render.entity.RenderHeartOfRa;
import com.teammetallurgy.atum.client.render.entity.arrow.RenderBone;
import com.teammetallurgy.atum.client.render.entity.arrow.RenderCamelSpit;
import com.teammetallurgy.atum.client.render.entity.arrow.RenderTefnutsCall;
import com.teammetallurgy.atum.client.render.entity.mobs.*;
import com.teammetallurgy.atum.client.render.shield.RenderAtumsProtection;
import com.teammetallurgy.atum.client.render.shield.RenderBrigandShield;
import com.teammetallurgy.atum.client.render.shield.RenderStoneguardShield;
import com.teammetallurgy.atum.client.render.tileentity.*;
import com.teammetallurgy.atum.entity.HeartOfRaEntity;
import com.teammetallurgy.atum.entity.animal.*;
import com.teammetallurgy.atum.entity.bandit.*;
import com.teammetallurgy.atum.entity.efreet.SunspeakerEntity;
import com.teammetallurgy.atum.entity.projectile.CamelSpitEntity;
import com.teammetallurgy.atum.entity.projectile.SmallBoneEntity;
import com.teammetallurgy.atum.entity.projectile.arrow.CustomArrow;
import com.teammetallurgy.atum.entity.projectile.arrow.TefnutsCallEntity;
import com.teammetallurgy.atum.entity.stone.StoneguardEntity;
import com.teammetallurgy.atum.entity.stone.StonewardenEntity;
import com.teammetallurgy.atum.entity.undead.*;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.items.TexturedArmorItem;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.properties.IProperty;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.entity.model.ZombieModel;
import net.minecraft.item.BlockItem;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT)
public class ClientHandler {

    public static void init() {
        //Colors
        BlockColors blockColors = Minecraft.getInstance().getBlockColors();
        ItemColors itemColor = Minecraft.getInstance().getItemColors();
        //Palm Leave color
        itemColor.register((stack, tintIndex) -> {
            BlockState state = ((BlockItem) stack.getItem()).getBlock().getDefaultState();
            return Minecraft.getInstance().getBlockColors().colorMultiplier(state, null, null, tintIndex);
        }, BlockLeave.getLeave(BlockAtumPlank.WoodType.PALM), BlockLeave.getLeave(BlockAtumPlank.WoodType.DEADWOOD));
        blockColors.register((state, world, pos, tintIndex) -> world != null && pos != null ? BiomeColorHelper.getFoliageColorAtPos(world, pos) : ColorizerFoliage.getFoliageColorBasic(), BlockLeave.getLeave(BlockAtumPlank.WoodType.PALM), BlockLeave.getLeave(BlockAtumPlank.WoodType.DEADWOOD));
        //Dyeable armor
        itemColor.register((stack, tintIndex) -> tintIndex > 0 ? -1 : ((TexturedArmorItem) stack.getItem()).getColor(stack), AtumItems.WANDERER_HELMET, AtumItems.WANDERER_CHEST, AtumItems.WANDERER_LEGS, AtumItems.WANDERER_BOOTS, AtumItems.DESERT_HELMET_IRON, AtumItems.DESERT_CHEST_IRON, AtumItems.DESERT_LEGS_IRON, AtumItems.DESERT_BOOTS_IRON, AtumItems.DESERT_HELMET_GOLD, AtumItems.DESERT_CHEST_GOLD, AtumItems.DESERT_LEGS_GOLD, AtumItems.DESERT_BOOTS_GOLD, AtumItems.DESERT_HELMET_DIAMOND, AtumItems.DESERT_CHEST_DIAMOND, AtumItems.DESERT_LEGS_DIAMOND, AtumItems.DESERT_BOOTS_DIAMOND);
        //Dead Grass
        itemColor.register((stack, tintIndex) -> {
            BlockState BlockState = ((BlockItem) stack.getItem()).getBlock().getDefaultState();
            return blockColors.colorMultiplier(BlockState, null, null, tintIndex);
        }, AtumBlocks.DEAD_GRASS);
        blockColors.register((state, worldIn, pos, tintIndex) -> {
            if (worldIn != null && pos != null) {
                return BiomeColorHelper.getGrassColorAtPos(worldIn, pos);
            } else {
                return 12889745;
            }
        }, AtumBlocks.DEAD_GRASS);
    }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityChestBase.class, new RenderTileChest());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCrate.class, new RenderCrate());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityHeartOfRa.class, new RenderHeartOfRaBase());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityRadiantBeacon.class, new RenderRadiantBeacon());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityQuern.class, new RenderQuern());
        AtumItems.ATUMS_PROTECTION.setTileEntityItemStackRenderer(new RenderAtumsProtection());
        AtumItems.BRIGAND_SHIELD.setTileEntityItemStackRenderer(new RenderBrigandShield());
        AtumItems.STONEGUARD_SHIELD.setTileEntityItemStackRenderer(new RenderStoneguardShield());
        RenderingRegistry.registerEntityRenderingHandler(TarantulaEntity.class, RenderTarantula::new);
        RenderingRegistry.registerEntityRenderingHandler(AssassinEntity.class, RenderBandit::new);
        RenderingRegistry.registerEntityRenderingHandler(BrigandEntity.class, RenderBandit::new);
        RenderingRegistry.registerEntityRenderingHandler(BarbarianEntity.class, RenderBandit::new);
        RenderingRegistry.registerEntityRenderingHandler(NomadEntity.class, manager -> new RenderBandit(manager, new ModelNomad()));
        RenderingRegistry.registerEntityRenderingHandler(WarlordEntity.class, RenderBandit::new);
        RenderingRegistry.registerEntityRenderingHandler(PharaohEntity.class, manager -> new RenderUndead(manager, new PlayerModel(0.0F, false)));
        RenderingRegistry.registerEntityRenderingHandler(MummyEntity.class, manager -> new RenderUndead(manager, new ZombieModel()));
        RenderingRegistry.registerEntityRenderingHandler(ForsakenEntity.class, manager -> new RenderUndead(manager, new ModelDustySkeleton()));
        RenderingRegistry.registerEntityRenderingHandler(WraithEntity.class, manager -> new RenderUndead(manager, new ZombieModel()));
        RenderingRegistry.registerEntityRenderingHandler(SunspeakerEntity.class, RenderEfreet::new);
        RenderingRegistry.registerEntityRenderingHandler(BonestormEntity.class, RenderBonestorm::new);
        RenderingRegistry.registerEntityRenderingHandler(StoneguardEntity.class, RenderStoneguard::new);
        RenderingRegistry.registerEntityRenderingHandler(StonewardenEntity.class, RenderStonewarden::new);
        RenderingRegistry.registerEntityRenderingHandler(DesertWolfEntity.class, manager -> new RenderDesertWolf(manager, new ModelDesertWolf(), 0.5F));
        RenderingRegistry.registerEntityRenderingHandler(CamelEntity.class, RenderCamel::new);
        RenderingRegistry.registerEntityRenderingHandler(ScarabEntity.class, RenderScarab::new);
        RenderingRegistry.registerEntityRenderingHandler(DesertRabbitEntity.class, RenderDesertRabbit::new);
        RenderingRegistry.registerEntityRenderingHandler(CustomArrow.class, manager -> new ArrowRenderer<CustomArrow>(manager) {
            @Override
            protected ResourceLocation getEntityTexture(@Nonnull CustomArrow entity) {
                return entity.getTexture();
            }
        });
        RenderingRegistry.registerEntityRenderingHandler(SmallBoneEntity.class, manager -> new RenderBone(manager, 0.35F));
        RenderingRegistry.registerEntityRenderingHandler(TefnutsCallEntity.class, RenderTefnutsCall::new);
        RenderingRegistry.registerEntityRenderingHandler(HeartOfRaEntity.class, RenderHeartOfRa::new);
        RenderingRegistry.registerEntityRenderingHandler(CamelSpitEntity.class, RenderCamelSpit::new);
    }

    public static void ignoreRenderProperty(Block block) {
        if (block instanceof IRenderMapper) {
            IRenderMapper mapper = (IRenderMapper) block;
            IProperty[] nonRenderingProperties = mapper.getNonRenderingProperties();

            if (nonRenderingProperties.length != 0) {
                IStateMapper stateMapper = (new StateMap.Builder()).ignore(nonRenderingProperties).build();
                ModelLoader.setCustomStateMapper(block, stateMapper);
            }
        }
    }
}