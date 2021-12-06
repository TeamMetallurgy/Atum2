package com.teammetallurgy.atum.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import com.teammetallurgy.atum.client.model.entity.PharaohOrbModel;
import com.teammetallurgy.atum.entity.projectile.PharaohOrbEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;

public class PharaohOrbRender extends EntityRenderer<PharaohOrbEntity> {
    private final PharaohOrbModel model = new PharaohOrbModel();

    public PharaohOrbRender(EntityRenderDispatcher renderManager) {
        super(renderManager);
    }

    @Override
    public void render(PharaohOrbEntity pharaohOrb, float entityYaw, float partialTicks, @Nonnull PoseStack matrixStack, @Nonnull MultiBufferSource buffer, int packedLight) {
        if (pharaohOrb.tickCount >= 1 || !(this.entityRenderDispatcher.camera.getEntity().distanceToSqr(pharaohOrb) < 16.0D)) {
            matrixStack.pushPose();
            matrixStack.scale(0.5F, 0.5F, 0.5F);
            matrixStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
            matrixStack.mulPose(Vector3f.YP.rotationDegrees(180.0F));
            VertexConsumer vertexBuilder = buffer.getBuffer(this.model.renderType(pharaohOrb.getTexture()));
            this.model.renderToBuffer(matrixStack, vertexBuilder, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            matrixStack.popPose();
            super.render(pharaohOrb, entityYaw, partialTicks, matrixStack, buffer, packedLight);
        }
    }

    @Override
    @Nonnull
    public ResourceLocation getTextureLocation(@Nonnull PharaohOrbEntity pharaohOrb) {
        return pharaohOrb.getTexture();
    }
}