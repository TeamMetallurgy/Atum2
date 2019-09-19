package com.teammetallurgy.atum.client.render.shield;

import com.teammetallurgy.atum.client.model.shield.ModelStoneguardShield;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class RenderStoneguardShield extends TileEntityItemStackRenderer {
    private static final ResourceLocation STONEGUARD_SHIELD_TEXTURE = new ResourceLocation(Constants.MOD_ID, "textures/shield/stoneguard_shield.png");
    private final ModelStoneguardShield modelShield = new ModelStoneguardShield();

    @Override
    public void renderByItem(@Nonnull ItemStack stack, float partialTicks) {
        GlStateManager.pushMatrix();
        Minecraft.getInstance().getTextureManager().bindTexture(STONEGUARD_SHIELD_TEXTURE);
        this.modelShield.render();
        GlStateManager.popMatrix();
    }
}