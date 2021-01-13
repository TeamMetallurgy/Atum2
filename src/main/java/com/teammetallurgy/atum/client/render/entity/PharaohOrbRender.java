package com.teammetallurgy.atum.client.render.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.teammetallurgy.atum.client.model.entity.PharaohOrbModel;
import com.teammetallurgy.atum.entity.projectile.PharaohOrbEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.model.LlamaSpitModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;

import javax.annotation.Nonnull;

public class PharaohOrbRender extends EntityRenderer<PharaohOrbEntity> {
    private final PharaohOrbModel model = new PharaohOrbModel();

    public PharaohOrbRender(EntityRendererManager renderManager) {
        super(renderManager);
    }

    @Override
    public void render(PharaohOrbEntity pharaohOrb, float entityYaw, float partialTicks, @Nonnull MatrixStack matrixStack, @Nonnull IRenderTypeBuffer buffer, int packedLight) {
        if (pharaohOrb.ticksExisted >= 1 || !(this.renderManager.info.getRenderViewEntity().getDistanceSq(pharaohOrb) < 16.0D)) {
            matrixStack.push();
            matrixStack.scale(0.5F, 0.5F, 0.5F);
            matrixStack.rotate(this.renderManager.getCameraOrientation());
            matrixStack.rotate(Vector3f.YP.rotationDegrees(180.0F));
            IVertexBuilder vertexBuilder = buffer.getBuffer(this.model.getRenderType(pharaohOrb.getTexture()));
            this.model.render(matrixStack, vertexBuilder, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            matrixStack.pop();
            super.render(pharaohOrb, entityYaw, partialTicks, matrixStack, buffer, packedLight);
        }
    }

    @Override
    @Nonnull
    public ResourceLocation getEntityTexture(@Nonnull PharaohOrbEntity pharaohOrb) {
        return pharaohOrb.getTexture();
    }
}