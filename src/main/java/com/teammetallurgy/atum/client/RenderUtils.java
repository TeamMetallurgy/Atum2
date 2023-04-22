package com.teammetallurgy.atum.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ToolActions;
import org.joml.Quaternionf;

import javax.annotation.Nonnull;

public class RenderUtils {

    public static void renderItem(BlockEntity tileEntity, @Nonnull ItemStack stack, float rotation, double yOffset, boolean drawStackSize, boolean rotateEastWest, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        renderItem(tileEntity, stack, Axis.YP.rotationDegrees(rotation), yOffset, drawStackSize, rotateEastWest, 90.0F, poseStack, buffer, combinedLight, combinedOverlay);
    }

    public static void renderItem(BlockEntity tileEntity, @Nonnull ItemStack stack, Quaternionf rotation, double yOffset, boolean drawStackSize, boolean rotateEastWest, float eastWestDegress, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        if (!stack.isEmpty()) {
            poseStack.pushPose();
            poseStack.translate(0.5F, yOffset + 1.225F, 0.5F);

            if (!(stack.getItem() instanceof BlockItem)) {
                poseStack.mulPose(rotation);
            }

            if (stack.getItem().canPerformAction(stack, ToolActions.SHIELD_BLOCK)) {
                poseStack.mulPose(Axis.YP.rotationDegrees(180));
                poseStack.translate(0.15, 0.1F, 0.15F);
            }
            BlockState state = tileEntity.getBlockState();
            if (state.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
                Direction facing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
                if (rotateEastWest) {
                    if (facing == Direction.EAST || facing == Direction.WEST) {
                        poseStack.mulPose(Axis.YP.rotationDegrees(eastWestDegress));
                    }
                } else {
                    if (facing == Direction.EAST) {
                        poseStack.mulPose(Axis.YP.rotationDegrees(90.0F));
                    }
                    if (facing == Direction.WEST) {
                        poseStack.mulPose(Axis.YP.rotationDegrees(-90.0F));
                    }
                    if (facing == Direction.NORTH) {
                        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
                    }
                }
            }
            poseStack.scale(0.25F, 0.25F, 0.25F);
            Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemDisplayContext.NONE, combinedLight, combinedOverlay, poseStack, buffer, null, 0);
            poseStack.popPose();

            if (drawStackSize) {
                String stackSize = String.valueOf(stack.getCount());
                drawString(tileEntity, stackSize, 0.5F, 0.75D, 0.5F, 0.003F, poseStack, buffer, combinedLight, combinedOverlay);
            }
        }
    }

    public static void drawString(BlockEntity te, String str, double xOffset, double yOffset, double zOffset, float scaleModifier, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        BlockEntityRenderDispatcher rendererDispatcher = Minecraft.getInstance().getBlockEntityRenderDispatcher();
        Entity entity = rendererDispatcher.camera.getEntity();
        BlockPos tePos = te.getBlockPos();
        double distance = new Vec3(entity.getX(), entity.getY(), entity.getZ()).distanceToSqr(tePos.getX(), tePos.getY(), tePos.getZ());

        if (distance <= (double) (14 * 14)) {
            float yaw = rendererDispatcher.camera.getYRot();
            float pitch = rendererDispatcher.camera.getXRot();
            Font font = rendererDispatcher.font;
            poseStack.pushPose();
            poseStack.translate(xOffset, yOffset, zOffset);

            poseStack.mulPose(Axis.YP.rotationDegrees(-yaw));
            poseStack.mulPose(Axis.XP.rotationDegrees(pitch));
            poseStack.scale(-0.015F + scaleModifier, -0.015F + scaleModifier, 0.015F + scaleModifier);

            font.drawInBatch(str, -font.width(str) / 2, 0, 1, false, poseStack.last().pose(), buffer, Font.DisplayMode.NORMAL, 0, 0);
            poseStack.popPose();
        }
    }
}