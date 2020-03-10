package com.teammetallurgy.atum.utils;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;

public class RenderUtils {

    public static void renderItem(TileEntity tileEntity, @Nonnull ItemStack stack, float rotation, double yOffset, boolean drawStackSize, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        if (!stack.isEmpty()) {
            RenderSystem.pushMatrix();
            RenderHelper.disableStandardItemLighting();
            RenderSystem.disableLighting();

            matrixStack.push();
            matrixStack.translate(0.5F, yOffset + 1.225F, 0.5F);

            matrixStack.rotate(Vector3f.YP.rotationDegrees(rotation));
            matrixStack.scale(0.25F, 0.25F, 0.25F);
            Minecraft.getInstance().getItemRenderer().renderItem(stack, ItemCameraTransforms.TransformType.FIXED, combinedLight, combinedOverlay, matrixStack, buffer);
            matrixStack.pop();

            RenderSystem.enableLighting();
            RenderHelper.enableStandardItemLighting();
            RenderSystem.popMatrix();

            if (drawStackSize) {
                String stackSize = String.valueOf(stack.getCount());
                drawString(tileEntity, stackSize, 0.5F, 1.475D, 0.5F, 0.003F);
            }
        }
    }

    public static void drawString(TileEntity te, String str, double x, double y, double z, float scaleModifier) {
        setLightmapDisabled(true);
        TileEntityRendererDispatcher rendererDispatcher = TileEntityRendererDispatcher.instance;
        Entity entity = rendererDispatcher.renderInfo.getRenderViewEntity();
        double distance = te.getDistanceSq(entity.getPosX(), entity.getPosY(), entity.getPosZ());

        if (distance <= (double) (14 * 14)) {
            float yaw = rendererDispatcher.renderInfo.getYaw();
            float pitch = rendererDispatcher.renderInfo.getPitch();
            FontRenderer font = rendererDispatcher.getFontRenderer();
            RenderSystem.pushMatrix();

            BlockPos pos = te.getPos();
            RenderSystem.translated(pos.getX() + x, pos.getY() + y, pos.getZ() + z);
            RenderSystem.normal3f(0.0F, 1.0F, 0.0F);

            RenderSystem.rotatef(-yaw, 0.0F, 1.0F, 0.0F);
            RenderSystem.rotatef(pitch, 1.0F, 0.0F, 0.0F);
            RenderSystem.scalef(-0.015F + scaleModifier, -0.015F + scaleModifier, 0.015F + scaleModifier);
            RenderSystem.disableLighting();
            RenderSystem.disableDepthTest();
            RenderSystem.enableBlend();

            RenderSystem.enableDepthTest();
            RenderSystem.depthMask(true);
            font.drawString(str, -font.getStringWidth(str) / 2, 0, -1);

            RenderSystem.enableLighting();
            RenderSystem.disableBlend();
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.popMatrix();
        }
        setLightmapDisabled(false);
    }

    private static void setLightmapDisabled(boolean disabled) {
        RenderSystem.activeTexture(33986);
        if (disabled) {
            RenderSystem.disableTexture();
        } else {
            RenderSystem.enableTexture();
        }
        RenderSystem.activeTexture(33984);
    }
}