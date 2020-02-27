package com.teammetallurgy.atum.client.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.teammetallurgy.atum.client.model.shield.AtumsProtectionModel;
import com.teammetallurgy.atum.client.model.shield.BrigandShieldModel;
import com.teammetallurgy.atum.client.model.shield.ShieldModel;
import com.teammetallurgy.atum.client.model.shield.StoneguardShieldModel;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.items.artifacts.atum.AtumsProtectionItem;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
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
    public void renderByItem(@Nonnull ItemStack stack) {
        Item item = stack.getItem();

        if (item instanceof BlockItem) {
            Block block = ((BlockItem) item).getBlock();

        } else {
            if (item instanceof AtumsProtectionItem) {
                renderShield(stack, ATUMS_PROTECTION, ATUMS_PROTECTION_TEXTURE);
            } else if (item == AtumItems.BRIGAND_SHIELD) {
                renderShield(stack, BRIGAND_SHIELD, BRIGAND_SHIELD_TEXTURE);
            } else if (item == AtumItems.STONEGUARD_SHIELD) {
                renderShield(stack, STONEGUARD_SHIELD, STONEGUARD_SHIELD_TEXTURE);
            }
        }
    }

    private void renderShield(@Nonnull ItemStack stack, ShieldModel shieldModel, ResourceLocation texture) {
        GlStateManager.pushMatrix();
        TextureManager textureManager = Minecraft.getInstance().getTextureManager();
        textureManager.bindTexture(texture);
        shieldModel.render();
        if (stack.hasEffect()) {
            this.renderEffect(textureManager, shieldModel::render);
        }
        GlStateManager.popMatrix();
    }

    private void renderEffect(TextureManager textureManager, Runnable runnable) {
        GlStateManager.color3f(0.5019608F, 0.2509804F, 0.8F);
        textureManager.bindTexture(ItemRenderer.RES_ITEM_GLINT);
        ItemRenderer.renderEffect(Minecraft.getInstance().getTextureManager(), runnable, 1);
    }
}