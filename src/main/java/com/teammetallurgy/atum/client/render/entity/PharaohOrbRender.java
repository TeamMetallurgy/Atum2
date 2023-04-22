package com.teammetallurgy.atum.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.teammetallurgy.atum.client.ClientHandler;
import com.teammetallurgy.atum.client.model.entity.PharaohOrbModel;
import com.teammetallurgy.atum.entity.projectile.PharaohOrbEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;

public class PharaohOrbRender extends EntityRenderer<PharaohOrbEntity> {
    private final PharaohOrbModel model;

    public PharaohOrbRender(EntityRendererProvider.Context context) {
        super(context);
        this.model = new PharaohOrbModel(context.bakeLayer(ClientHandler.PHARAOH_ORB));
    }

    @Override
    public void render(PharaohOrbEntity pharaohOrb, float entityYaw, float partialTicks, @Nonnull PoseStack poseStack, @Nonnull MultiBufferSource buffer, int packedLight) {
        if (pharaohOrb.tickCount >= 1 || !(this.entityRenderDispatcher.camera.getEntity().distanceToSqr(pharaohOrb) < 16.0D)) {
            poseStack.pushPose();
            poseStack.scale(0.5F, 0.5F, 0.5F);
            poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
            poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
            VertexConsumer vertexBuilder = buffer.getBuffer(this.model.renderType(pharaohOrb.getTexture()));
            this.model.renderToBuffer(poseStack, vertexBuilder, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            poseStack.popPose();
            super.render(pharaohOrb, entityYaw, partialTicks, poseStack, buffer, packedLight);
        }
    }

    @Override
    @Nonnull
    public ResourceLocation getTextureLocation(@Nonnull PharaohOrbEntity pharaohOrb) {
        return pharaohOrb.getTexture();
    }
}