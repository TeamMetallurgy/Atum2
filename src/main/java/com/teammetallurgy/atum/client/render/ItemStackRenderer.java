package com.teammetallurgy.atum.client.render;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.blocks.stone.limestone.chest.LimestoneChestBlock;
import com.teammetallurgy.atum.blocks.stone.limestone.chest.SarcophagusBlock;
import com.teammetallurgy.atum.client.ClientHandler;
import com.teammetallurgy.atum.client.model.shield.AbstractShieldModel;
import com.teammetallurgy.atum.client.model.shield.AtemsProtectionModel;
import com.teammetallurgy.atum.client.model.shield.BrigandShieldModel;
import com.teammetallurgy.atum.client.model.shield.StoneguardShieldModel;
import com.teammetallurgy.atum.client.render.entity.TefnutsCallRender;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.init.AtumTileEntities;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.TridentModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import javax.annotation.Nonnull;
import java.util.Map;

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
    public void renderByItem(@Nonnull ItemStack stack, @Nonnull ItemDisplayContext displayContext, @Nonnull PoseStack poseStack, @Nonnull MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        Item item = stack.getItem();
        BlockEntityRenderDispatcher dispatcher = Minecraft.getInstance().getBlockEntityRenderDispatcher();

        if (item instanceof BlockItem) {
            Block block = ((BlockItem) item).getBlock();

            if (block instanceof LimestoneChestBlock) {
                dispatcher.renderItem(AtumTileEntities.LIMESTONE_CHEST.get().create(BlockPos.ZERO, AtumBlocks.LIMESTONE_CHEST.get().defaultBlockState()), poseStack, buffer, combinedLight, combinedOverlay);
            } else if (block instanceof SarcophagusBlock) {
                dispatcher.renderItem(AtumTileEntities.SARCOPHAGUS.get().create(BlockPos.ZERO, AtumBlocks.SARCOPHAGUS.get().defaultBlockState()), poseStack, buffer, combinedLight, combinedOverlay);
            } else if (block == AtumBlocks.PALM_CURIO_DISPLAY.get()) {
                dispatcher.renderItem(AtumTileEntities.PALM_CURIO_DISPLAY.get().create(BlockPos.ZERO, AtumBlocks.PALM_CURIO_DISPLAY.get().defaultBlockState()), poseStack, buffer, combinedLight, combinedOverlay);
            } else if (block == AtumBlocks.DEADWOOD_CURIO_DISPLAY.get()) {
                dispatcher.renderItem(AtumTileEntities.DEADWOOD_CURIO_DISPLAY.get().create(BlockPos.ZERO, AtumBlocks.DEADWOOD_CURIO_DISPLAY.get().defaultBlockState()), poseStack, buffer, combinedLight, combinedOverlay);
            } else if (block == AtumBlocks.ACACIA_CURIO_DISPLAY.get()) {
                dispatcher.renderItem(AtumTileEntities.ACACIA_CURIO_DISPLAY.get().create(BlockPos.ZERO, AtumBlocks.ACACIA_CURIO_DISPLAY.get().defaultBlockState()), poseStack, buffer, combinedLight, combinedOverlay);
            } else if (block == AtumBlocks.LIMESTONE_CURIO_DISPLAY.get()) {
                dispatcher.renderItem(AtumTileEntities.LIMESTONE_CURIO_DISPLAY.get().create(BlockPos.ZERO, AtumBlocks.LIMESTONE_CURIO_DISPLAY.get().defaultBlockState()), poseStack, buffer, combinedLight, combinedOverlay);
            } else if (block == AtumBlocks.ALABASTER_CURIO_DISPLAY.get()) {
                dispatcher.renderItem(AtumTileEntities.ALABASTER_CURIO_DISPLAY.get().create(BlockPos.ZERO, AtumBlocks.ALABASTER_CURIO_DISPLAY.get().defaultBlockState()), poseStack, buffer, combinedLight, combinedOverlay);
            } else if (block == AtumBlocks.PORPHYRY_CURIO_DISPLAY.get()) {
                dispatcher.renderItem(AtumTileEntities.PORPHYRY_CURIO_DISPLAY.get().create(BlockPos.ZERO, AtumBlocks.PORPHYRY_CURIO_DISPLAY.get().defaultBlockState()), poseStack, buffer, combinedLight, combinedOverlay);
            } else if (block == AtumBlocks.NEBU_CURIO_DISPLAY.get()) {
                dispatcher.renderItem(AtumTileEntities.NEBU_CURIO_DISPLAY.get().create(BlockPos.ZERO, AtumBlocks.NEBU_CURIO_DISPLAY.get().defaultBlockState()), poseStack, buffer, combinedLight, combinedOverlay);
            }
        } else {
            if (item == AtumItems.BRIGAND_SHIELD.get()) {
                renderShield(brigandShield, BRIGAND_SHIELD_MATERIAL, stack, displayContext, poseStack, buffer, combinedLight, combinedOverlay);
            } else if (item == AtumItems.STONEGUARD_SHIELD.get()) {
                renderShield(stoneguardShield, STONEGUARD_SHIELD_MATERIAL, stack, displayContext, poseStack, buffer, combinedLight, combinedOverlay);
            } else if (item == AtumItems.ATEMS_PROTECTION.get()) {
                renderShield(atemsProtection, ATEMS_PROTECTION_MATERIAL, stack, displayContext, poseStack, buffer, combinedLight, combinedOverlay);
            } /*else if (item == AtumItems.NEPTHYS_CONSECRATION) {
                renderShield(stack, NEPTHYS_CONSECRATION, NEPTHYS_CONSECRATION_MATERIAL, poseStack, buffer, combinedLight, combinedOverlay);
            } */ else if (item == AtumItems.TEFNUTS_CALL.get()) {
                poseStack.pushPose();
                poseStack.scale(1.0F, -1.0F, -1.0F);
                VertexConsumer vertexBuilder = ItemRenderer.getFoilBuffer(buffer, this.tefnutsCall.renderType(TefnutsCallRender.TEFNUTS_CALL_TEXTURE), false, stack.hasFoil());

                LocalPlayer player = Minecraft.getInstance().player;
                if (player != null && player.isUsingItem() && (displayContext == ItemDisplayContext.THIRD_PERSON_LEFT_HAND || displayContext == ItemDisplayContext.THIRD_PERSON_RIGHT_HAND)) {
                    poseStack.rotateAround(Axis.XP.rotationDegrees(180.0F), 0.0F, 0.75F, 0.0F);
                }
                this.tefnutsCall.renderToBuffer(poseStack, vertexBuilder, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
                poseStack.popPose();
            }
        }
    }

    private void renderShield(AbstractShieldModel shieldModel, Material material, @Nonnull ItemStack stack, @Nonnull ItemDisplayContext displayContext, @Nonnull PoseStack poseStack, @Nonnull MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        poseStack.pushPose();
        if (displayContext.firstPerson()) {
            LocalPlayer player = Minecraft.getInstance().player;
            if (player != null && player.isUsingItem() && player.getUseItem() == stack) {
                if (displayContext == ItemDisplayContext.FIRST_PERSON_RIGHT_HAND) {
                    poseStack.translate(0.2F, 0.1F, 0.2F);
                } else if (displayContext == ItemDisplayContext.FIRST_PERSON_LEFT_HAND) {
                    poseStack.translate(-0.2F, 0.1F, 0.25F);
                }
            }
        }

        VertexConsumer builder = material.sprite().wrap(ItemRenderer.getFoilBuffer(buffer, shieldModel.renderType(material.atlasLocation()), false, stack.hasFoil()));
        shieldModel.renderToBuffer(poseStack, builder, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.popPose();
    }

    private static Material getShieldMaterial(String fileName) {
        ResourceLocation shieldTexture = SHIELD_CACHE.get(fileName);
        if (shieldTexture == null) {
            shieldTexture = new ResourceLocation(Atum.MOD_ID, "shield/" + fileName);
            SHIELD_CACHE.put(fileName, shieldTexture);
        }
        return new Material(Sheets.SHIELD_SHEET, shieldTexture);
    }
}