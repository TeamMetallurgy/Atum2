package com.teammetallurgy.atum.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class RenderUtils {

    public static void renderItem(TileEntity tileEntity, ItemStack stack, double x, double y, double z, float rotation, boolean drawStackSize) {
        if (!stack.isEmpty()) {
            GlStateManager.pushMatrix();
            RenderHelper.disableStandardItemLighting();

            GlStateManager.translate(x + 0.5F, y + 1.225F, z + 0.5F);
            GlStateManager.disableLighting();

            GlStateManager.rotate(rotation, 0.0F, 1.0F, 0.0F);
            GlStateManager.scale(0.25F, 0.25F, 0.25F);
            Minecraft.getMinecraft().getRenderItem().renderItem(stack, ItemCameraTransforms.TransformType.FIXED);

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
        Entity entity = rendererDispatcher.entity;
        double distance = te.getDistanceSq(entity.posX, entity.posY, entity.posZ);

        if (distance <= (double) (14 * 14)) {
            float f = rendererDispatcher.entityYaw;
            float f1 = rendererDispatcher.entityPitch;
            FontRenderer fontRenderer = rendererDispatcher.getFontRenderer();
            GlStateManager.pushMatrix();

            GlStateManager.translate(x, y, z);
            GlStateManager.glNormal3f(0.0F, 1.0F, 0.0F);

            GlStateManager.rotate(-f, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(f1, 1.0F, 0.0F, 0.0F);
            GlStateManager.scale(-0.015F + scaleModifier, -0.015F + scaleModifier, 0.015F + scaleModifier);
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            GlStateManager.enableBlend();

            GlStateManager.enableDepth();
            GlStateManager.depthMask(true);
            fontRenderer.drawString(str, -fontRenderer.getStringWidth(str) / 2, 0, -1);

            GlStateManager.enableLighting();
            GlStateManager.disableBlend();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.popMatrix();
        }
        setLightmapDisabled(false);
    }

    private static void setLightmapDisabled(boolean disabled) {
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        if (disabled) {
            GlStateManager.disableTexture2D();
        } else {
            GlStateManager.enableTexture2D();
        }
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }
}