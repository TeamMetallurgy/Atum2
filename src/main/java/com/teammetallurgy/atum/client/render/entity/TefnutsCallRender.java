package com.teammetallurgy.atum.client.render.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.teammetallurgy.atum.entity.projectile.arrow.TefnutsCallEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.model.TridentModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nonnull;

public class TefnutsCallRender extends EntityRenderer<TefnutsCallEntity> {
    public static final ResourceLocation TEFNUTS_CALL = new ResourceLocation("textures/entity/trident.png"); //TODO
    private final TridentModel tridentModel = new TridentModel();

    public TefnutsCallRender(EntityRendererManager renderManager) {
        super(renderManager);
    }

    @Override
    public void render(TefnutsCallEntity entity, float entityYaw, float partialTicks, MatrixStack matrixStack, @Nonnull IRenderTypeBuffer buffer, int packedLight) {
        matrixStack.push();
        matrixStack.rotate(Vector3f.YP.rotationDegrees(MathHelper.lerp(partialTicks, entity.prevRotationYaw, entity.rotationYaw) - 90.0F));
        matrixStack.rotate(Vector3f.ZP.rotationDegrees(MathHelper.lerp(partialTicks, entity.prevRotationPitch, entity.rotationPitch) + 90.0F));
        IVertexBuilder vertexBuilder = ItemRenderer.getBuffer(buffer, this.tridentModel.getRenderType(this.getEntityTexture(entity)), false, true);
        this.tridentModel.render(matrixStack, vertexBuilder, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStack.pop();
        super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
    }

    @Override
    @Nonnull
    public ResourceLocation getEntityTexture(@Nonnull TefnutsCallEntity entity) {
        return TEFNUTS_CALL;
    }
}