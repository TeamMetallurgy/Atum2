package com.teammetallurgy.atum.client.render.entity.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.teammetallurgy.atum.client.ClientHandler;
import com.teammetallurgy.atum.client.model.entity.ServalModel;
import com.teammetallurgy.atum.entity.animal.ServalEntity;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;

public class ServalCollorLayer extends RenderLayer<ServalEntity, ServalModel<ServalEntity>> {
    private static final ResourceLocation CAT_COLLAR = new ResourceLocation("textures/entity/cat/cat_collar.png");
    private final ServalModel<ServalEntity> model;

    public ServalCollorLayer(RenderLayerParent<ServalEntity, ServalModel<ServalEntity>> model, EntityModelSet modelSet) {
        super(model);
        this.model = new ServalModel<>(modelSet.bakeLayer(ClientHandler.SERVAL_COLLAR));
    }

    @Override
    public void render(@Nonnull PoseStack poseStack, @Nonnull MultiBufferSource buffer, int packedLight, ServalEntity serval, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (serval.isTame()) {
            float[] color = serval.getCollarColor().getTextureDiffuseColors();
            coloredCutoutModelCopyLayerRender(this.getParentModel(), this.model, CAT_COLLAR, poseStack, buffer, packedLight, serval, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, partialTicks, color[0], color[1], color[2]);
        }
    }
}