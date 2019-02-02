package com.teammetallurgy.atum.client.render.shield;

import com.teammetallurgy.atum.client.model.shield.ModelAtumsProtection;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class RenderAtumsProtection extends TileEntityItemStackRenderer {
    private final ModelAtumsProtection modelShield = new ModelAtumsProtection();
    private static String textureName;

    public RenderAtumsProtection(String textureName) {
        RenderAtumsProtection.textureName = textureName;
    }

    @Override
    public void renderByItem(@Nonnull ItemStack stack, float partialTicks) {
        GlStateManager.pushMatrix();
        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(Constants.MOD_ID, "textures/shield/" + textureName + ".png"));
        this.modelShield.render();
        GlStateManager.popMatrix();
    }
}