package com.teammetallurgy.atum.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ToolActions;
import org.joml.Quaternionf;

import javax.annotation.Nonnull;

public class RenderUtils {

    public static void renderItem(BlockEntity tileEntity, @Nonnull ItemStack stack, float rotation, double yOffset, boolean drawStackSize, boolean rotateEastWest, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        renderItem(tileEntity, stack, Axis.YP.rotationDegrees(rotation), yOffset, drawStackSize, rotateEastWest, 90.0F, matrixStack, buffer, combinedLight, combinedOverlay);
    }

    public static void renderItem(BlockEntity tileEntity, @Nonnull ItemStack stack, Quaternionf rotation, double yOffset, boolean drawStackSize, boolean rotateEastWest, float eastWestDegress, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        if (!stack.isEmpty()) {
            matrixStack.pushPose();
            matrixStack.translate(0.5F, yOffset + 1.225F, 0.5F);

            if (!(stack.getItem() instanceof BlockItem)) {
                matrixStack.mulPose(rotation);
            }

            if (stack.getItem().canPerformAction(stack, ToolActions.SHIELD_BLOCK)) {
                matrixStack.mulPose(Axis.YP.rotationDegrees(180));
                matrixStack.translate(0.15, 0.1F, 0.15F);
            }
            BlockState state = tileEntity.getBlockState();
            if (state.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
                Direction facing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
                if (rotateEastWest) {
                    if (facing == Direction.EAST || facing == Direction.WEST) {
                        matrixStack.mulPose(Axis.YP.rotationDegrees(eastWestDegress));
                    }
                } else {
                    if (facing == Direction.EAST) {
                        matrixStack.mulPose(Axis.YP.rotationDegrees(90.0F));
                    }
                    if (facing == Direction.WEST) {
                        matrixStack.mulPose(Axis.YP.rotationDegrees(-90.0F));
                    }
                    if (facing == Direction.NORTH) {
                        matrixStack.mulPose(Axis.YP.rotationDegrees(180.0F));
                    }
                }
            }
            matrixStack.scale(0.25F, 0.25F, 0.25F);
            Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemTransforms.TransformType.NONE, combinedLight, combinedOverlay, matrixStack, buffer, 0);
            matrixStack.popPose();

            if (drawStackSize) {
                String stackSize = String.valueOf(stack.getCount());
                drawString(tileEntity, stackSize, 0.5F, 0.75D, 0.5F, 0.003F, matrixStack, buffer, combinedLight, combinedOverlay);
            }
        }
    }

    public static void drawString(BlockEntity te, String str, double xOffset, double yOffset, double zOffset, float scaleModifier, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        BlockEntityRenderDispatcher rendererDispatcher = Minecraft.getInstance().getBlockEntityRenderDispatcher();
        Entity entity = rendererDispatcher.camera.getEntity();
        BlockPos tePos = te.getBlockPos();
        double distance = new Vec3(entity.getX(), entity.getY(), entity.getZ()).distanceToSqr(tePos.getX(), tePos.getY(), tePos.getZ());

        if (distance <= (double) (14 * 14)) {
            float yaw = rendererDispatcher.camera.getYRot();
            float pitch = rendererDispatcher.camera.getXRot();
            Font font = rendererDispatcher.font;
            matrixStack.pushPose();
            matrixStack.translate(xOffset, yOffset, zOffset);

            matrixStack.mulPose(Axis.YP.rotationDegrees(-yaw));
            matrixStack.mulPose(Axis.XP.rotationDegrees(pitch));
            matrixStack.scale(-0.015F + scaleModifier, -0.015F + scaleModifier, 0.015F + scaleModifier);

            font.drawInBatch(str, -font.width(str) / 2, 0, 1, false, matrixStack.last().pose(), buffer, false, 0, 0);
            matrixStack.popPose();
        }
    }
}