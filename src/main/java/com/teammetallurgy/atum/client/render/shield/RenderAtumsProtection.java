package com.teammetallurgy.atum.client.render.shield;

import com.teammetallurgy.atum.client.model.shield.AtumsProtectionModel;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class RenderAtumsProtection extends TileEntityItemStackRenderer {
    private static final ResourceLocation ATUM_PROTECTION_TEXTURE = new ResourceLocation(Constants.MOD_ID, "textures/shield/atums_protection.png");
    private final AtumsProtectionModel modelShield = new AtumsProtectionModel();

    @Override
    public void renderByItem(@Nonnull ItemStack stack, float partialTicks) {
        GlStateManager.pushMatrix();
        Minecraft.getInstance().getTextureManager().bindTexture(ATUM_PROTECTION_TEXTURE);
        this.modelShield.render();
        GlStateManager.popMatrix();
    }
}