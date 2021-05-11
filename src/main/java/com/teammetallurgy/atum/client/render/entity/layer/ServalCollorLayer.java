package com.teammetallurgy.atum.client.render.entity.layer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.teammetallurgy.atum.client.model.entity.ServalModel;
import com.teammetallurgy.atum.entity.animal.ServalEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class ServalCollorLayer extends LayerRenderer<ServalEntity, ServalModel<ServalEntity>> {
    private static final ResourceLocation CAT_COLLAR = new ResourceLocation("textures/entity/cat/cat_collar.png");
    private final ServalModel<ServalEntity> model = new ServalModel<>(0.01F);

    public ServalCollorLayer(IEntityRenderer<ServalEntity, ServalModel<ServalEntity>> model) {
        super(model);
    }

    @Override
    public void render(@Nonnull MatrixStack matrixStack, @Nonnull IRenderTypeBuffer buffer, int packedLight, ServalEntity serval, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (serval.isTamed()) {
            float[] color = serval.getCollarColor().getColorComponentValues();
            renderCopyCutoutModel(this.getEntityModel(), this.model, CAT_COLLAR, matrixStack, buffer, packedLight, serval, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, partialTicks, color[0], color[1], color[2]);
        }
    }
}