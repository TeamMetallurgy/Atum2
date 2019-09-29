package com.teammetallurgy.atum.client.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.teammetallurgy.atum.client.model.shield.AtumsProtectionModel;
import com.teammetallurgy.atum.client.model.shield.BrigandShieldModel;
import com.teammetallurgy.atum.client.model.shield.StoneguardShieldModel;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.items.artifacts.atum.AtumsProtectionItem;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class ShieldRenderer extends ItemStackTileEntityRenderer {
    private static final ResourceLocation ATUMS_PROTECTION_TEXTURE = new ResourceLocation(Constants.MOD_ID, "textures/shield/atums_protection.png");
    private static final ResourceLocation BRIGAND_SHIELD_TEXTURE = new ResourceLocation(Constants.MOD_ID, "textures/shield/brigand_shield.png");
    private static final ResourceLocation STONEGUARD_SHIELD_TEXTURE = new ResourceLocation(Constants.MOD_ID, "textures/shield/stoneguard_shield.png");
    private static final AtumsProtectionModel ATUMS_PROTECTION = new AtumsProtectionModel();
    private static final BrigandShieldModel BRIGAND_SHIELD = new BrigandShieldModel();
    private static final StoneguardShieldModel STONEGUARD_SHIELD = new StoneguardShieldModel();

    @Override
    public void renderByItem(@Nonnull ItemStack stack) {
        Item item = stack.getItem();
        TextureManager textureManager = Minecraft.getInstance().getTextureManager();

        GlStateManager.pushMatrix();
        if (item instanceof AtumsProtectionItem) {
            textureManager.bindTexture(ATUMS_PROTECTION_TEXTURE);
            ATUMS_PROTECTION.render();
        } else if (item == AtumItems.BRIGAND_SHIELD) {
            textureManager.bindTexture(BRIGAND_SHIELD_TEXTURE);
            BRIGAND_SHIELD.render();
        } else if (item == AtumItems.STONEGUARD_SHIELD) {
            textureManager.bindTexture(STONEGUARD_SHIELD_TEXTURE);
            STONEGUARD_SHIELD.render();
        }
        GlStateManager.popMatrix();
    }
}