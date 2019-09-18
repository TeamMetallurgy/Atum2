package com.teammetallurgy.atum.utils;

import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class RenderUtils {

    public static void renderItem(TileEntity tileEntity, ItemStack stack, double x, double y, double z, float rotation, boolean drawStackSize) {
        if (!stack.isEmpty()) {
            GlStateManager.pushMatrix();
            RenderHelper.disableStandardItemLighting();

            GlStateManager.translated(x + 0.5F, y + 1.225F, z + 0.5F);
            GlStateManager.disableLighting();

            GlStateManager.rotatef(rotation, 0.0F, 1.0F, 0.0F);
            GlStateManager.scalef(0.25F, 0.25F, 0.25F);
            Minecraft.getInstance().getItemRenderer().renderItem(stack, ItemCameraTransforms.TransformType.FIXED);

            RenderHelper.enableStandardItemLighting();
            GlStateManager.popMatrix();

            if (drawStackSize) {
                GlStateManager.pushMatrix();
                String stackSize = String.valueOf(stack.getCount());
                drawString(tileEntity, stackSize, x + 0.5F, y + 1.475D, z + 0.5F, 0.003F);
                GlStateManager.popMatrix();
            }
        }
    }

    public static void drawString(TileEntity te, String str, double x, double y, double z, float scaleModifier) {
        setLightmapDisabled(true);
        TileEntityRendererDispatcher rendererDispatcher = TileEntityRendererDispatcher.instance;
        Entity entity = rendererDispatcher.renderInfo.getRenderViewEntity();
        double distance = te.getDistanceSq(entity.posX, entity.posY, entity.posZ);

        if (distance <= (double) (14 * 14)) {
            float yaw = rendererDispatcher.renderInfo.getYaw();
            float pitch = rendererDispatcher.renderInfo.getPitch();
            FontRenderer fontRenderer = rendererDispatcher.getFontRenderer();
            GlStateManager.pushMatrix();

            GlStateManager.translated(x, y, z);
            GlStateManager.normal3f(0.0F, 1.0F, 0.0F);

            GlStateManager.rotatef(-yaw, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotatef(pitch, 1.0F, 0.0F, 0.0F);
            GlStateManager.scalef(-0.015F + scaleModifier, -0.015F + scaleModifier, 0.015F + scaleModifier);
            GlStateManager.disableLighting();
            GlStateManager.disableDepthTest();
            GlStateManager.enableBlend();

            GlStateManager.enableDepthTest();
            GlStateManager.depthMask(true);
            fontRenderer.drawString(str, -fontRenderer.getStringWidth(str) / 2, 0, -1);

            GlStateManager.enableLighting();
            GlStateManager.disableBlend();
            GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.popMatrix();
        }
        setLightmapDisabled(false);
    }

    private static void setLightmapDisabled(boolean disabled) {
        GlStateManager.activeTexture(GLX.GL_TEXTURE1);
        if (disabled) {
            GlStateManager.disableTexture();
        } else {
            GlStateManager.enableTexture();
        }
        GlStateManager.activeTexture(GLX.GL_TEXTURE0);
    }
}