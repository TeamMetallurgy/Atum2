package com.teammetallurgy.atum.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.client.ClientHandler;
import com.teammetallurgy.atum.entity.projectile.arrow.TefnutsCallEntity;
import net.minecraft.client.model.TridentModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import javax.annotation.Nonnull;

public class TefnutsCallRender extends EntityRenderer<TefnutsCallEntity> {
    public static final ResourceLocation TEFNUTS_CALL_TEXTURE = new ResourceLocation(Atum.MOD_ID, "textures/entity/tefnuts_call.png");
    private final TridentModel tefnutsCallModel;

    public TefnutsCallRender(EntityRendererProvider.Context context) {
        super(context);
        this.tefnutsCallModel = new TridentModel(context.bakeLayer(ClientHandler.TEFNUTS_CALL));
    }

    @Override
    public void render(TefnutsCallEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, @Nonnull MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, entity.yRotO, entity.getYRot()) - 90.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTicks, entity.xRotO, entity.getXRot()) + 90.0F));
        VertexConsumer vertexBuilder = ItemRenderer.getFoilBuffer(buffer, this.tefnutsCallModel.renderType(this.getTextureLocation(entity)), false, true);
        this.tefnutsCallModel.renderToBuffer(poseStack, vertexBuilder, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    @Nonnull
    public ResourceLocation getTextureLocation(@Nonnull TefnutsCallEntity entity) {
        return TEFNUTS_CALL_TEXTURE;
    }
}