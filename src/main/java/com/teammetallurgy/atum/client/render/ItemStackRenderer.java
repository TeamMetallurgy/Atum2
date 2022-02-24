package com.teammetallurgy.atum.client.render;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.blocks.curio.tileentity.*;
import com.teammetallurgy.atum.blocks.stone.limestone.chest.LimestoneChestBlock;
import com.teammetallurgy.atum.blocks.stone.limestone.chest.SarcophagusBlock;
import com.teammetallurgy.atum.blocks.stone.limestone.chest.tileentity.LimestoneChestTileEntity;
import com.teammetallurgy.atum.blocks.stone.limestone.chest.tileentity.SarcophagusTileEntity;
import com.teammetallurgy.atum.client.ClientHandler;
import com.teammetallurgy.atum.client.model.shield.AbstractShieldModel;
import com.teammetallurgy.atum.client.model.shield.AtemsProtectionModel;
import com.teammetallurgy.atum.client.model.shield.BrigandShieldModel;
import com.teammetallurgy.atum.client.model.shield.StoneguardShieldModel;
import com.teammetallurgy.atum.client.render.entity.TefnutsCallRender;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.TridentModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;
import java.util.Map;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID, value = Dist.CLIENT)
public class ItemStackRenderer extends BlockEntityWithoutLevelRenderer {
    private static final Map<String, ResourceLocation> SHIELD_CACHE = Maps.newHashMap();
    private static final Material ATEMS_PROTECTION_MATERIAL = getShieldMaterial("atems_protection");
    private static final Material BRIGAND_SHIELD_MATERIAL = getShieldMaterial("brigand_shield");
    private static final Material STONEGUARD_SHIELD_MATERIAL = getShieldMaterial("stoneguard_shield");
    private final AtemsProtectionModel atemsProtection;
    private final BrigandShieldModel brigandShield;
    private final StoneguardShieldModel stoneguardShield;
    private final TridentModel tefnutsCall;

    public ItemStackRenderer(BlockEntityRenderDispatcher dispatcher, EntityModelSet modelSet) {
        super(dispatcher, modelSet);
        this.tefnutsCall = new TridentModel(modelSet.bakeLayer(ClientHandler.TEFNUTS_CALL));
        this.atemsProtection = new AtemsProtectionModel(modelSet.bakeLayer(ClientHandler.ATEMS_PROTECTION));
        this.brigandShield = new BrigandShieldModel(modelSet.bakeLayer(ClientHandler.BRIGAND_SHIELD));
        this.stoneguardShield = new StoneguardShieldModel(modelSet.bakeLayer(ClientHandler.STONEGUARD_SHIELD));
    }

    @Override
    public void renderByItem(@Nonnull ItemStack stack, @Nonnull ItemTransforms.TransformType transformType, @Nonnull PoseStack matrixStack, @Nonnull MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        Item item = stack.getItem();
        BlockEntityRenderDispatcher dispatcher = Minecraft.getInstance().getBlockEntityRenderDispatcher();;

        if (item instanceof BlockItem) {
            Block block = ((BlockItem) item).getBlock();

            if (block instanceof LimestoneChestBlock) {
                dispatcher.renderItem(new LimestoneChestTileEntity(BlockPos.ZERO, AtumBlocks.LIMESTONE_CHEST.defaultBlockState()), matrixStack, buffer, combinedLight, combinedOverlay);
            } else if (block instanceof SarcophagusBlock) {
                dispatcher.renderItem(new SarcophagusTileEntity(BlockPos.ZERO, AtumBlocks.SARCOPHAGUS.defaultBlockState()), matrixStack, buffer, combinedLight, combinedOverlay);
            } else if (block == AtumBlocks.PALM_CURIO_DISPLAY) {
                dispatcher.renderItem(new PalmCurioDisplayTileEntity(BlockPos.ZERO, AtumBlocks.PALM_CURIO_DISPLAY.defaultBlockState()), matrixStack, buffer, combinedLight, combinedOverlay);
            } else if (block == AtumBlocks.DEADWOOD_CURIO_DISPLAY) {
                dispatcher.renderItem(new DeadwoodCurioDisplayTileEntity(BlockPos.ZERO, AtumBlocks.DEADWOOD_CURIO_DISPLAY.defaultBlockState()), matrixStack, buffer, combinedLight, combinedOverlay);
            } else if (block == AtumBlocks.ACACIA_CURIO_DISPLAY) {
                dispatcher.renderItem(new AcaciaCurioDisplayTileEntity(BlockPos.ZERO, AtumBlocks.ACACIA_CURIO_DISPLAY.defaultBlockState()), matrixStack, buffer, combinedLight, combinedOverlay);
            } else if (block == AtumBlocks.LIMESTONE_CURIO_DISPLAY) {
                dispatcher.renderItem(new LimestoneCurioDisplayTileEntity(BlockPos.ZERO, AtumBlocks.LIMESTONE_CURIO_DISPLAY.defaultBlockState()), matrixStack, buffer, combinedLight, combinedOverlay);
            } else if (block == AtumBlocks.ALABASTER_CURIO_DISPLAY) {
                dispatcher.renderItem(new AlabasterCurioDisplayTileEntity(BlockPos.ZERO, AtumBlocks.ALABASTER_CURIO_DISPLAY.defaultBlockState()), matrixStack, buffer, combinedLight, combinedOverlay);
            } else if (block == AtumBlocks.PORPHYRY_CURIO_DISPLAY) {
                dispatcher.renderItem(new PorphyryCurioDisplayTileEntity(BlockPos.ZERO, AtumBlocks.PORPHYRY_CURIO_DISPLAY.defaultBlockState()), matrixStack, buffer, combinedLight, combinedOverlay);
            } else if (block == AtumBlocks.NEBU_CURIO_DISPLAY) {
                dispatcher.renderItem(new NebuCurioDisplayTileEntity(BlockPos.ZERO, AtumBlocks.NEBU_CURIO_DISPLAY.defaultBlockState()), matrixStack, buffer, combinedLight, combinedOverlay);
            }
        } else {
            if (item == AtumItems.BRIGAND_SHIELD) {
                renderShield(stack, brigandShield, BRIGAND_SHIELD_MATERIAL, matrixStack, buffer, combinedLight, combinedOverlay);
            } else if (item == AtumItems.STONEGUARD_SHIELD) {
                renderShield(stack, stoneguardShield, STONEGUARD_SHIELD_MATERIAL, matrixStack, buffer, combinedLight, combinedOverlay);
            } else if (item == AtumItems.ATEMS_PROTECTION) {
                renderShield(stack, atemsProtection, ATEMS_PROTECTION_MATERIAL, matrixStack, buffer, combinedLight, combinedOverlay);
            } /*else if (item == AtumItems.NEPTHYS_CONSECRATION) {
                renderShield(stack, NEPTHYS_CONSECRATION, NEPTHYS_CONSECRATION_MATERIAL, matrixStack, buffer, combinedLight, combinedOverlay);
            } */else if (item == AtumItems.TEFNUTS_CALL) {
                matrixStack.pushPose();
                matrixStack.scale(1.0F, -1.0F, -1.0F);
                VertexConsumer vertexBuilder = ItemRenderer.getFoilBuffer(buffer, this.tefnutsCall.renderType(TefnutsCallRender.TEFNUTS_CALL_TEXTURE), false, stack.hasFoil());
                this.tefnutsCall.renderToBuffer(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
                matrixStack.popPose();
            }
        }
    }

    private void renderShield(@Nonnull ItemStack stack, AbstractShieldModel shieldModel, Material material, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        VertexConsumer builder = material.sprite().wrap(ItemRenderer.getFoilBuffer(buffer, shieldModel.renderType(material.atlasLocation()), false, stack.hasFoil()));
        shieldModel.renderToBuffer(matrixStack, builder, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
    }

    private static Material getShieldMaterial(String fileName) {
        ResourceLocation shieldTexture = SHIELD_CACHE.get(fileName);
        if (shieldTexture == null) {
            shieldTexture = new ResourceLocation(Atum.MOD_ID, "shield/" + fileName);
            SHIELD_CACHE.put(fileName, shieldTexture);
        }

        ClientHandler.addToShieldAtlas(shieldTexture);
        return new Material(Sheets.SHIELD_SHEET, shieldTexture);
    }
}