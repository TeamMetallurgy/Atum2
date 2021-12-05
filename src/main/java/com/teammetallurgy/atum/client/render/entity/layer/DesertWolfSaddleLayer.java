package com.teammetallurgy.atum.client.render.entity.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.client.model.entity.DesertWolfModel;
import com.teammetallurgy.atum.entity.animal.DesertWolfEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;

public class DesertWolfSaddleLayer extends RenderLayer<DesertWolfEntity, DesertWolfModel<DesertWolfEntity>> {
    private static final ResourceLocation SADDLE_DESERT_WOLF_TEXTURE = new ResourceLocation(Atum.MOD_ID, "textures/entity/desert_wolf_saddle.png");
    private final DesertWolfModel<DesertWolfEntity> model = new DesertWolfModel<>(0.5F);

    public DesertWolfSaddleLayer(RenderLayerParent<DesertWolfEntity, DesertWolfModel<DesertWolfEntity>> entityRenderer) {
        super(entityRenderer);
    }

    @Override
    public void render(@Nonnull PoseStack matrixStack, @Nonnull MultiBufferSource buffer, int packedLight, DesertWolfEntity desertWolf, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (desertWolf.isSaddled()) {
            this.getParentModel().copyPropertiesTo(this.model);
            this.model.prepareMobModel(desertWolf, limbSwing, limbSwingAmount, partialTicks);
            this.model.setupAnim(desertWolf, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            VertexConsumer ivertexbuilder = buffer.getBuffer(RenderType.entityCutoutNoCull(SADDLE_DESERT_WOLF_TEXTURE));
            this.model.renderToBuffer(matrixStack, ivertexbuilder, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
}