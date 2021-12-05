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
import com.teammetallurgy.atum.client.model.TefnutsCallModel;
import com.teammetallurgy.atum.client.model.shield.AbstractShieldModel;
import com.teammetallurgy.atum.client.model.shield.AtemsProtectionModel;
import com.teammetallurgy.atum.client.model.shield.BrigandShieldModel;
import com.teammetallurgy.atum.client.model.shield.StoneguardShieldModel;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumItems;
import net.minecraft.world.level.block.Block;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.Map;

public class ItemStackRenderer extends BlockEntityWithoutLevelRenderer {
    private static final Map<String, ResourceLocation> SHIELD_CACHE = Maps.newHashMap();
    private static final Material ATEMS_PROTECTION_MATERIAL = getShieldMaterial("atems_protection");
    private static final Material BRIGAND_SHIELD_MATERIAL = getShieldMaterial("brigand_shield");
    private static final Material STONEGUARD_SHIELD_MATERIAL = getShieldMaterial("stoneguard_shield");
    private static final AtemsProtectionModel ATEMS_PROTECTION = new AtemsProtectionModel();
    private static final BrigandShieldModel BRIGAND_SHIELD = new BrigandShieldModel();
    private static final StoneguardShieldModel STONEGUARD_SHIELD = new StoneguardShieldModel();
    private final TefnutsCallModel tefnutsCall = new TefnutsCallModel();

    @Override
    public void renderByItem(@Nonnull ItemStack stack, @Nonnull ItemTransforms.TransformType transformType, @Nonnull PoseStack matrixStack, @Nonnull MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        Item item = stack.getItem();

        if (item instanceof BlockItem) {
            Block block = ((BlockItem) item).getBlock();

            if (block instanceof LimestoneChestBlock) {
                BlockEntityRenderDispatcher.instance.renderItem(new LimestoneChestTileEntity(), matrixStack, buffer, combinedLight, combinedOverlay);
            } else if (block instanceof SarcophagusBlock) {
                BlockEntityRenderDispatcher.instance.renderItem(new SarcophagusTileEntity(), matrixStack, buffer, combinedLight, combinedOverlay);
            } else if (block == AtumBlocks.PALM_CURIO_DISPLAY) {
                BlockEntityRenderDispatcher.instance.renderItem(new PalmCurioDisplayTileEntity(), matrixStack, buffer, combinedLight, combinedOverlay);
            } else if (block == AtumBlocks.DEADWOOD_CURIO_DISPLAY) {
                BlockEntityRenderDispatcher.instance.renderItem(new DeadwoodCurioDisplayTileEntity(), matrixStack, buffer, combinedLight, combinedOverlay);
            } else if (block == AtumBlocks.ACACIA_CURIO_DISPLAY) {
                BlockEntityRenderDispatcher.instance.renderItem(new AcaciaCurioDisplayTileEntity(), matrixStack, buffer, combinedLight, combinedOverlay);
            } else if (block == AtumBlocks.LIMESTONE_CURIO_DISPLAY) {
                BlockEntityRenderDispatcher.instance.renderItem(new LimestoneCurioDisplayTileEntity(), matrixStack, buffer, combinedLight, combinedOverlay);
            } else if (block == AtumBlocks.ALABASTER_CURIO_DISPLAY) {
                BlockEntityRenderDispatcher.instance.renderItem(new AlabasterCurioDisplayTileEntity(), matrixStack, buffer, combinedLight, combinedOverlay);
            } else if (block == AtumBlocks.PORPHYRY_CURIO_DISPLAY) {
                BlockEntityRenderDispatcher.instance.renderItem(new PorphyryCurioDisplayTileEntity(), matrixStack, buffer, combinedLight, combinedOverlay);
            } else if (block == AtumBlocks.NEBU_CURIO_DISPLAY) {
                BlockEntityRenderDispatcher.instance.renderItem(new NebuCurioDisplayTileEntity(), matrixStack, buffer, combinedLight, combinedOverlay);
            }
        } else {
            if (item == AtumItems.BRIGAND_SHIELD) {
                renderShield(stack, BRIGAND_SHIELD, BRIGAND_SHIELD_MATERIAL, matrixStack, buffer, combinedLight, combinedOverlay);
            } else if (item == AtumItems.STONEGUARD_SHIELD) {
                renderShield(stack, STONEGUARD_SHIELD, STONEGUARD_SHIELD_MATERIAL, matrixStack, buffer, combinedLight, combinedOverlay);
            } else if (item == AtumItems.ATEMS_PROTECTION) {
                renderShield(stack, ATEMS_PROTECTION, ATEMS_PROTECTION_MATERIAL, matrixStack, buffer, combinedLight, combinedOverlay);
            } /*else if (item == AtumItems.NEPTHYS_CONSECRATION) {
                renderShield(stack, NEPTHYS_CONSECRATION, NEPTHYS_CONSECRATION_MATERIAL, matrixStack, buffer, combinedLight, combinedOverlay);
            } */else if (item == AtumItems.TEFNUTS_CALL) {
                matrixStack.pushPose();
                matrixStack.scale(1.0F, -1.0F, -1.0F);
                VertexConsumer vertexBuilder = ItemRenderer.getFoilBuffer(buffer, this.tefnutsCall.renderType(TefnutsCallModel.TEFNUTS_CALL_TEXTURE), false, stack.hasFoil());
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