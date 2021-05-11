package com.teammetallurgy.atum.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.Entity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;

import javax.annotation.Nonnull;

public class RenderUtils {

    public static void renderItem(TileEntity tileEntity, @Nonnull ItemStack stack, float rotation, double yOffset, boolean drawStackSize, boolean rotateEastWest, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        renderItem(tileEntity, stack, Vector3f.YP.rotationDegrees(rotation), yOffset, drawStackSize, rotateEastWest, 90.0F, matrixStack, buffer, combinedLight, combinedOverlay);
    }

    public static void renderItem(TileEntity tileEntity, @Nonnull ItemStack stack, Quaternion rotation, double yOffset, boolean drawStackSize, boolean rotateEastWest, float eastWestDegress, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        if (!stack.isEmpty()) {
            matrixStack.push();
            matrixStack.translate(0.5F, yOffset + 1.225F, 0.5F);

            if (!(stack.getItem() instanceof BlockItem)) {
                matrixStack.rotate(rotation);
            }

            if (stack.getItem().isShield(stack, null)) {
                matrixStack.rotate(Vector3f.YP.rotationDegrees(180));
                matrixStack.translate(0.15, 0.1F, 0.15F);
            }
            BlockState state = tileEntity.getBlockState();
            if (state.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
                Direction facing = state.get(BlockStateProperties.HORIZONTAL_FACING);
                if (rotateEastWest) {
                    if (facing == Direction.EAST || facing == Direction.WEST) {
                        matrixStack.rotate(Vector3f.YP.rotationDegrees(eastWestDegress));
                    }
                } else {
                    if (facing == Direction.EAST) {
                        matrixStack.rotate(Vector3f.YP.rotationDegrees(90.0F));
                    }
                    if (facing == Direction.WEST) {
                        matrixStack.rotate(Vector3f.YP.rotationDegrees(-90.0F));
                    }
                    if (facing == Direction.NORTH) {
                        matrixStack.rotate(Vector3f.YP.rotationDegrees(180.0F));
                    }
                }
            }
            matrixStack.scale(0.25F, 0.25F, 0.25F);
            Minecraft.getInstance().getItemRenderer().renderItem(stack, ItemCameraTransforms.TransformType.NONE, combinedLight, combinedOverlay, matrixStack, buffer);
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
        double distance = new Vector3d(entity.getPosX(), entity.getPosY(), entity.getPosZ()).squareDistanceTo(tePos.getX(), tePos.getY(), tePos.getZ());

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