package com.teammetallurgy.atum.client.model.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.teammetallurgy.atum.entity.projectile.PharaohOrbEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;

import javax.annotation.Nonnull;

public class PharaohOrbModel extends EntityModel<PharaohOrbEntity> {
    private final ModelPart main;

    public PharaohOrbModel() {
        this.texWidth = 16;
        this.texHeight = 16;

        this.main = new ModelPart(this);
        this.main.setPos(0.0F, 24.0F, 0.0F);
        this.main.texOffs(0, 0).addBox(-8.0F, -16.0F, 0.0F, 16.0F, 16.0F, 0.0F, 0.0F, false);
    }

    @Override
    public void renderToBuffer(@Nonnull PoseStack matrixStack, @Nonnull VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        main.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    @Override
    public void setupAnim(@Nonnull PharaohOrbEntity pharaohOrbEntity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }
}