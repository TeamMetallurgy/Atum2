package com.teammetallurgy.atum.client.render.shield;

import com.teammetallurgy.atum.client.model.shield.ModelBrigandShield;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class RenderBrigandShield extends TileEntityItemStackRenderer {
    private static final ResourceLocation BRIGAND_SHIELD_TEXTURE = new ResourceLocation(Constants.MOD_ID, "textures/shield/brigand_shield.png");
    private final ModelBrigandShield modelShield = new ModelBrigandShield();

    @Override
    public void renderByItem(@Nonnull ItemStack stack, float partialTicks) {
        GlStateManager.pushMatrix();
        Minecraft.getMinecraft().getTextureManager().bindTexture(BRIGAND_SHIELD_TEXTURE);
        this.modelShield.render();
        GlStateManager.popMatrix();
    }
}