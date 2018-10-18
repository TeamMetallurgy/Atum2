package com.teammetallurgy.atum.proxy;

import com.teammetallurgy.atum.blocks.base.IRenderMapper;
import com.teammetallurgy.atum.blocks.base.tileentity.TileEntityChestBase;
import com.teammetallurgy.atum.blocks.wood.BlockAtumPlank;
import com.teammetallurgy.atum.blocks.wood.BlockLeave;
import com.teammetallurgy.atum.client.model.entity.ModelDesertWolf;
import com.teammetallurgy.atum.client.model.entity.ModelDustySkeleton;
import com.teammetallurgy.atum.client.model.entity.ModelNomad;
import com.teammetallurgy.atum.client.render.RenderTileChest;
import com.teammetallurgy.atum.client.render.entity.*;
import com.teammetallurgy.atum.client.render.entity.arrow.RenderBone;
import com.teammetallurgy.atum.client.render.entity.arrow.RenderTefnutsCall;
import com.teammetallurgy.atum.client.render.shield.RenderAtumsProtection;
import com.teammetallurgy.atum.client.render.shield.RenderBrigandShield;
import com.teammetallurgy.atum.entity.EntityDesertWolf;
import com.teammetallurgy.atum.entity.EntityTarantula;
import com.teammetallurgy.atum.entity.bandit.*;
import com.teammetallurgy.atum.entity.projectile.EntitySmallBone;
import com.teammetallurgy.atum.entity.projectile.arrow.CustomArrow;
import com.teammetallurgy.atum.entity.projectile.arrow.EntityTefnutsCall;
import com.teammetallurgy.atum.entity.stone.EntityStoneguard;
import com.teammetallurgy.atum.entity.undead.*;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.init.AtumParticles;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.renderer.entity.RenderArrow;
import net.minecraft.client.renderer.entity.RenderBiped;
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

@Mod.EventBusSubscriber(value = Side.CLIENT)
public class ClientProxy extends CommonProxy {
    private static final ModelResourceLocation BRIGAND_SHIELD = new ModelResourceLocation(new ResourceLocation(Constants.MOD_ID, "brigand_shield"), "inventory");
    private static final ModelResourceLocation THOTHS_BEARINGS = new ModelResourceLocation(new ResourceLocation(Constants.MOD_ID, "thoths_bearings"), "inventory");
    public static AtumParticles atumParticles = new AtumParticles();

    @Override
    public void init() {
        //Palm Leave color
        Minecraft.getMinecraft().getItemColors().registerItemColorHandler((stack, tintIndex) -> {
            IBlockState state = ((ItemBlock) stack.getItem()).getBlock().getDefaultState();
            return Minecraft.getMinecraft().getBlockColors().colorMultiplier(state, null, null, tintIndex);
        }, BlockLeave.getLeave(BlockAtumPlank.WoodType.PALM));
        Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler((state, world, pos, tintIndex) -> world != null && pos != null ? BiomeColorHelper.getFoliageColorAtPos(world, pos) : ColorizerFoliage.getFoliageColorBasic(), BlockLeave.getLeave(BlockAtumPlank.WoodType.PALM));
    }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        atumParticles.register();
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityChestBase.class, new RenderTileChest());
        AtumItems.ATUMS_PROTECTION.setTileEntityItemStackRenderer(new RenderAtumsProtection());
        AtumItems.BRIGAND_SHIELD.setTileEntityItemStackRenderer(new RenderBrigandShield());
        ModelLoader.setCustomMeshDefinition(AtumItems.BRIGAND_SHIELD, stack -> BRIGAND_SHIELD);
        ModelLoader.setCustomMeshDefinition(AtumItems.THOTHS_BEARINGS, stack -> THOTHS_BEARINGS);
        RenderingRegistry.registerEntityRenderingHandler(EntityTarantula.class, RenderTarantula::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityAssassin.class, manager -> new RenderBandit(manager, new ModelBiped()));
        RenderingRegistry.registerEntityRenderingHandler(EntityBrigand.class, manager -> new RenderBandit(manager, new ModelPlayer(0.0F, false)));
        RenderingRegistry.registerEntityRenderingHandler(EntityBarbarian.class, manager -> new RenderBandit(manager, new ModelPlayer(0.0F, false)));
        RenderingRegistry.registerEntityRenderingHandler(EntityNomad.class, manager -> new RenderBandit(manager, new ModelNomad()));
        RenderingRegistry.registerEntityRenderingHandler(EntityWarlord.class, manager -> new RenderBandit(manager, new ModelPlayer(0.0F, false)));
        RenderingRegistry.registerEntityRenderingHandler(EntityPharaoh.class, manager -> new RenderUndead(manager, new ModelPlayer(0.0F, false)));
        RenderingRegistry.registerEntityRenderingHandler(EntityMummy.class, manager -> new RenderUndead(manager, new ModelZombie()));
        RenderingRegistry.registerEntityRenderingHandler(EntityForsaken.class, manager -> new RenderUndead(manager, new ModelDustySkeleton()));
        RenderingRegistry.registerEntityRenderingHandler(EntityWraith.class, manager -> new RenderUndead(manager, new ModelZombie()));
        RenderingRegistry.registerEntityRenderingHandler(EntityBonestorm.class, RenderBonestorm::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityStoneguard.class, manager -> new RenderBiped<EntityStoneguard>(manager, new ModelBiped(), 0.5F) {
            @Override
            protected ResourceLocation getEntityTexture(@Nonnull EntityStoneguard entity) {
                return new ResourceLocation(Constants.MOD_ID, "textures/entities/stoneguard.png");
            }
        });
        RenderingRegistry.registerEntityRenderingHandler(EntityDesertWolf.class, manager -> new RenderDesertWolf(manager, new ModelDesertWolf(), 0.5F));
        RenderingRegistry.registerEntityRenderingHandler(CustomArrow.class, manager -> new RenderArrow<CustomArrow>(manager) {
            @Override
            protected ResourceLocation getEntityTexture(@Nonnull CustomArrow entity) {
                return entity.getTexture();
            }
        });
        RenderingRegistry.registerEntityRenderingHandler(EntitySmallBone.class, manager -> new RenderBone(manager, 0.35F));
        RenderingRegistry.registerEntityRenderingHandler(EntityTefnutsCall.class, RenderTefnutsCall::new);
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