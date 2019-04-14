package com.teammetallurgy.atum.proxy;

import com.teammetallurgy.atum.blocks.base.IRenderMapper;
import com.teammetallurgy.atum.blocks.base.tileentity.TileEntityChestBase;
import com.teammetallurgy.atum.blocks.beacon.tileentity.TileEntityHeartOfRa;
import com.teammetallurgy.atum.blocks.beacon.tileentity.TileEntityRadiantBeacon;
import com.teammetallurgy.atum.blocks.machines.tileentity.TileEntityQuern;
import com.teammetallurgy.atum.blocks.wood.BlockAtumPlank;
import com.teammetallurgy.atum.blocks.wood.BlockLeave;
import com.teammetallurgy.atum.blocks.wood.tileentity.crate.TileEntityCrate;
import com.teammetallurgy.atum.client.TextureManagerParticles;
import com.teammetallurgy.atum.client.TextureMapParticles;
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
import com.teammetallurgy.atum.entity.EntityHeartOfRa;
import com.teammetallurgy.atum.entity.animal.*;
import com.teammetallurgy.atum.entity.bandit.*;
import com.teammetallurgy.atum.entity.efreet.EntitySunspeaker;
import com.teammetallurgy.atum.entity.projectile.EntityCamelSpit;
import com.teammetallurgy.atum.entity.projectile.EntitySmallBone;
import com.teammetallurgy.atum.entity.projectile.arrow.CustomArrow;
import com.teammetallurgy.atum.entity.projectile.arrow.EntityTefnutsCall;
import com.teammetallurgy.atum.entity.stone.EntityStoneguard;
import com.teammetallurgy.atum.entity.stone.EntityStonewarden;
import com.teammetallurgy.atum.entity.undead.*;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.init.AtumParticles;
import com.teammetallurgy.atum.integration.IntegrationHandler;
import com.teammetallurgy.atum.items.ItemTexturedArmor;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.entity.RenderArrow;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, value = Side.CLIENT)
public class ClientProxy extends CommonProxy {
    public static AtumParticles atumParticles;

    @Override
    public void init() {
        IntegrationHandler.INSTANCE.clientSide();
        //TextureMap Particles
        atumParticles = new AtumParticles();
        TextureManagerParticles managerParticles = TextureManagerParticles.INSTANCE;
        TextureMapParticles textureMap = managerParticles.getTextureMap();
        Minecraft.getMinecraft().renderEngine.loadTickableTexture(TextureManagerParticles.LOCATION_PARTICLES, textureMap);

        //Colors
        BlockColors blockColors = Minecraft.getMinecraft().getBlockColors();
        ItemColors itemColor = Minecraft.getMinecraft().getItemColors();
        //Palm Leave color
        itemColor.registerItemColorHandler((stack, tintIndex) -> {
            IBlockState state = ((ItemBlock) stack.getItem()).getBlock().getDefaultState();
            return Minecraft.getMinecraft().getBlockColors().colorMultiplier(state, null, null, tintIndex);
        }, BlockLeave.getLeave(BlockAtumPlank.WoodType.PALM), BlockLeave.getLeave(BlockAtumPlank.WoodType.DEADWOOD));
        blockColors.registerBlockColorHandler((state, world, pos, tintIndex) -> world != null && pos != null ? BiomeColorHelper.getFoliageColorAtPos(world, pos) : ColorizerFoliage.getFoliageColorBasic(), BlockLeave.getLeave(BlockAtumPlank.WoodType.PALM), BlockLeave.getLeave(BlockAtumPlank.WoodType.DEADWOOD));
        //Dyeable armor
        itemColor.registerItemColorHandler((stack, tintIndex) -> tintIndex > 0 ? -1 : ((ItemTexturedArmor) stack.getItem()).getColor(stack), AtumItems.WANDERER_HELMET, AtumItems.WANDERER_CHEST, AtumItems.WANDERER_LEGS, AtumItems.WANDERER_BOOTS, AtumItems.DESERT_HELMET_IRON, AtumItems.DESERT_CHEST_IRON, AtumItems.DESERT_LEGS_IRON, AtumItems.DESERT_BOOTS_IRON, AtumItems.DESERT_HELMET_GOLD, AtumItems.DESERT_CHEST_GOLD, AtumItems.DESERT_LEGS_GOLD, AtumItems.DESERT_BOOTS_GOLD, AtumItems.DESERT_HELMET_DIAMOND, AtumItems.DESERT_CHEST_DIAMOND, AtumItems.DESERT_LEGS_DIAMOND, AtumItems.DESERT_BOOTS_DIAMOND);
        //Dead Grass
        itemColor.registerItemColorHandler((stack, tintIndex) -> {
            IBlockState iblockstate = ((ItemBlock) stack.getItem()).getBlock().getDefaultState();
            return blockColors.colorMultiplier(iblockstate, null, null, tintIndex);
        }, AtumBlocks.DEAD_GRASS);
        blockColors.registerBlockColorHandler((state, worldIn, pos, tintIndex) -> {
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
        AtumItems.ATUMS_PROTECTION.setTileEntityItemStackRenderer(new RenderAtumsProtection("atums_protection"));
        AtumItems.BRIGAND_SHIELD.setTileEntityItemStackRenderer(new RenderBrigandShield("brigand_shield"));
        AtumItems.STONEGUARD_SHIELD.setTileEntityItemStackRenderer(new RenderStoneguardShield("stoneguard_shield"));
        ModelLoader.setCustomMeshDefinition(AtumItems.THOTHS_BEARINGS, stack -> new ModelResourceLocation(new ResourceLocation(Constants.MOD_ID, "thoths_bearings"), "inventory"));
        ModelLoader.setCustomMeshDefinition(AtumItems.GRAVEROBBERS_MAP, stack -> new ModelResourceLocation(new ResourceLocation(Constants.MOD_ID, "graverobbers_map"), "inventory"));
        RenderingRegistry.registerEntityRenderingHandler(EntityTarantula.class, RenderTarantula::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityAssassin.class, RenderBandit::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityBrigand.class, RenderBandit::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityBarbarian.class, RenderBandit::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityNomad.class, manager -> new RenderBandit(manager, new ModelNomad()));
        RenderingRegistry.registerEntityRenderingHandler(EntityWarlord.class, RenderBandit::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityPharaoh.class, manager -> new RenderUndead(manager, new ModelPlayer(0.0F, false)));
        RenderingRegistry.registerEntityRenderingHandler(EntityMummy.class, manager -> new RenderUndead(manager, new ModelZombie()));
        RenderingRegistry.registerEntityRenderingHandler(EntityForsaken.class, manager -> new RenderUndead(manager, new ModelDustySkeleton()));
        RenderingRegistry.registerEntityRenderingHandler(EntityWraith.class, manager -> new RenderUndead(manager, new ModelZombie()));
        RenderingRegistry.registerEntityRenderingHandler(EntitySunspeaker.class, RenderEfreet::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityBonestorm.class, RenderBonestorm::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityStoneguard.class, RenderStoneguard::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityStonewarden.class, RenderStonewarden::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityDesertWolf.class, manager -> new RenderDesertWolf(manager, new ModelDesertWolf(), 0.5F));
        RenderingRegistry.registerEntityRenderingHandler(EntityCamel.class, RenderCamel::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityScarab.class, RenderScarab::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityDesertRabbit.class, RenderDesertRabbit::new);
        RenderingRegistry.registerEntityRenderingHandler(CustomArrow.class, manager -> new RenderArrow<CustomArrow>(manager) {
            @Override
            protected ResourceLocation getEntityTexture(@Nonnull CustomArrow entity) {
                return entity.getTexture();
            }
        });
        RenderingRegistry.registerEntityRenderingHandler(EntitySmallBone.class, manager -> new RenderBone(manager, 0.35F));
        RenderingRegistry.registerEntityRenderingHandler(EntityTefnutsCall.class, RenderTefnutsCall::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityHeartOfRa.class, RenderHeartOfRa::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityCamelSpit.class, RenderCamelSpit::new);
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

    @Override
    public void spawnParticle(AtumParticles.Types particleType, Entity entity, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        ClientProxy.atumParticles.addEffect(ClientProxy.atumParticles.spawnEffectParticle(particleType.getParticleName(), Minecraft.getMinecraft().world, x, y, z, xSpeed, ySpeed, zSpeed));
    }
}