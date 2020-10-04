package com.teammetallurgy.atum.misc;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;

import javax.annotation.Nonnull;

public class RenderUtils {

    public static void renderItem(TileEntity tileEntity, @Nonnull ItemStack stack, float rotation, double yOffset, boolean drawStackSize, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        if (!stack.isEmpty()) {
            matrixStack.push();
            matrixStack.translate(0.5F, yOffset + 1.225F, 0.5F);

            matrixStack.rotate(Vector3f.YP.rotationDegrees(rotation));
            matrixStack.scale(0.25F, 0.25F, 0.25F);
            Minecraft.getInstance().getItemRenderer().renderItem(stack, ItemCameraTransforms.TransformType.FIXED, combinedLight, combinedOverlay, matrixStack, buffer);
            matrixStack.pop();

            if (drawStackSize) {
                String stackSize = String.valueOf(stack.getCount());
                drawString(tileEntity, stackSize, 0.5F, 0.75D, 0.5F, 0.003F, matrixStack, buffer, combinedLight, combinedOverlay);
            }
        }
    }

    public static void drawString(TileEntity te, String str, double xOffset, double yOffset, double zOffset, float scaleModifier, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        TileEntityRendererDispatcher rendererDispatcher = TileEntityRendererDispatcher.instance;
        Entity entity = rendererDispatcher.renderInfo.getRenderViewEntity();
        BlockPos tePos = te.getPos();
        double distance = new Vector3d(entity.getPosX(), entity.getPosY(), entity.getPosZ()).squareDistanceTo(tePos.getX(), tePos.getY(), tePos.getZ()); //TODO Test

        if (distance <= (double) (14 * 14)) {
            float yaw = rendererDispatcher.renderInfo.getYaw();
            float pitch = rendererDispatcher.renderInfo.getPitch();
            FontRenderer font = rendererDispatcher.getFontRenderer();
            matrixStack.push();
            matrixStack.translate(xOffset, yOffset, zOffset);

            matrixStack.rotate(Vector3f.YP.rotationDegrees(-yaw));
            matrixStack.rotate(Vector3f.XP.rotationDegrees(pitch));
            matrixStack.scale(-0.015F + scaleModifier, -0.015F + scaleModifier, 0.015F + scaleModifier);

            font.renderString(str, -font.getStringWidth(str) / 2, 0, 1, false, matrixStack.getLast().getMatrix(), buffer, false, 0, 0);
            matrixStack.pop();
        }
    }
}