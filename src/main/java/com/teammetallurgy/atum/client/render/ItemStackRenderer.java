package com.teammetallurgy.atum.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.teammetallurgy.atum.blocks.stone.limestone.chest.ChestSpawnerBlock;
import com.teammetallurgy.atum.blocks.stone.limestone.chest.LimestoneChestBlock;
import com.teammetallurgy.atum.blocks.stone.limestone.chest.SarcophagusBlock;
import com.teammetallurgy.atum.client.model.shield.AbstractShieldModel;
import com.teammetallurgy.atum.client.model.shield.AtumsProtectionModel;
import com.teammetallurgy.atum.client.model.shield.BrigandShieldModel;
import com.teammetallurgy.atum.client.model.shield.StoneguardShieldModel;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.init.AtumTileEntities;
import com.teammetallurgy.atum.items.artifacts.atum.AtumsProtectionItem;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.model.Material;
import net.minecraft.client.renderer.model.ModelBakery;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class ItemStackRenderer extends ItemStackTileEntityRenderer {
    private static final ResourceLocation ATUMS_PROTECTION_TEXTURE = new ResourceLocation(Constants.MOD_ID, "textures/shield/atums_protection.png");
    private static final ResourceLocation BRIGAND_SHIELD_TEXTURE = new ResourceLocation(Constants.MOD_ID, "textures/shield/brigand_shield.png");
    private static final ResourceLocation STONEGUARD_SHIELD_TEXTURE = new ResourceLocation(Constants.MOD_ID, "textures/shield/stoneguard_shield.png");
    private static final AtumsProtectionModel ATUMS_PROTECTION = new AtumsProtectionModel();
    private static final BrigandShieldModel BRIGAND_SHIELD = new BrigandShieldModel();
    private static final StoneguardShieldModel STONEGUARD_SHIELD = new StoneguardShieldModel();

    @Override
    public void render(@Nonnull ItemStack stack, @Nonnull MatrixStack matrixStack, @Nonnull IRenderTypeBuffer buffer, int i, int i1) {
        Item item = stack.getItem();

        if (item instanceof BlockItem) {
            Block block = ((BlockItem) item).getBlock();

            if (block instanceof LimestoneChestBlock || block instanceof ChestSpawnerBlock) {
                TileEntityRendererDispatcher.instance.renderItem(AtumTileEntities.LIMESTONE_CHEST.create(), matrixStack, buffer, i, i1);
            } else if (block instanceof SarcophagusBlock) {
                TileEntityRendererDispatcher.instance.renderItem(AtumTileEntities.SARCOPHAGUS.create(), matrixStack, buffer, i, i1);
            }
        } else {
            if (item instanceof AtumsProtectionItem) {
                renderShield(stack, ATUMS_PROTECTION, ATUMS_PROTECTION_TEXTURE, matrixStack, buffer, i, i1);
            } else if (item == AtumItems.BRIGAND_SHIELD) {
                renderShield(stack, BRIGAND_SHIELD, BRIGAND_SHIELD_TEXTURE, matrixStack, buffer, i, i);
            } else if (item == AtumItems.STONEGUARD_SHIELD) {
                renderShield(stack, STONEGUARD_SHIELD, STONEGUARD_SHIELD_TEXTURE, matrixStack, buffer, i, i);
            }
        }
    }

    private void renderShield(@Nonnull ItemStack stack, AbstractShieldModel shieldModel, ResourceLocation texture, MatrixStack matrixStack, IRenderTypeBuffer buffer, int i, int i1) {
        matrixStack.push();
        matrixStack.scale(1.0F, -1.0F, -1.0F);
        Material material = ModelBakery.LOCATION_SHIELD_NO_PATTERN;
        IVertexBuilder builder = material.getSprite().wrapBuffer(ItemRenderer.getBuffer(buffer, shieldModel.getRenderType(material.getAtlasLocation()), false, stack.hasEffect()));
        shieldModel.getHandle().render(matrixStack, builder, i, i1, 1.0F, 1.0F, 1.0F, 1.0F);
        shieldModel.getPlate().render(matrixStack, builder, i, i1, 1.0F, 1.0F, 1.0F, 1.0F);
        if (shieldModel.getOptional() != null) {
            shieldModel.getOptional().render(matrixStack, builder, i, i1, 1.0F, 1.0F, 1.0F, 1.0F);
        }
        matrixStack.pop();
    }
}