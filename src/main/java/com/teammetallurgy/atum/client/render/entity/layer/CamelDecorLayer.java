package com.teammetallurgy.atum.client.render.entity.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.client.model.entity.CamelModel;
import com.teammetallurgy.atum.entity.animal.CamelEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;

import javax.annotation.Nonnull;

public class CamelDecorLayer extends RenderLayer<CamelEntity, CamelModel<CamelEntity>> {
    private static final ResourceLocation[] CAMEL_DECOR_TEXTURES = new ResourceLocation[]{new ResourceLocation(Atum.MOD_ID, "textures/entity/camel_carpet/camel_carpet_white.png"), new ResourceLocation(Atum.MOD_ID, "textures/entity/camel_carpet/camel_carpet_orange.png"), new ResourceLocation(Atum.MOD_ID, "textures/entity/camel_carpet/camel_carpet_magenta.png"), new ResourceLocation(Atum.MOD_ID, "textures/entity/camel_carpet/camel_carpet_light_blue.png"), new ResourceLocation(Atum.MOD_ID, "textures/entity/camel_carpet/camel_carpet_yellow.png"), new ResourceLocation(Atum.MOD_ID, "textures/entity/camel_carpet/camel_carpet_lime.png"), new ResourceLocation(Atum.MOD_ID, "textures/entity/camel_carpet/camel_carpet_pink.png"), new ResourceLocation(Atum.MOD_ID, "textures/entity/camel_carpet/camel_carpet_gray.png"), new ResourceLocation(Atum.MOD_ID, "textures/entity/camel_carpet/camel_carpet_light_gray.png"), new ResourceLocation(Atum.MOD_ID, "textures/entity/camel_carpet/camel_carpet_cyan.png"), new ResourceLocation(Atum.MOD_ID, "textures/entity/camel_carpet/camel_carpet_purple.png"), new ResourceLocation(Atum.MOD_ID, "textures/entity/camel_carpet/camel_carpet_blue.png"), new ResourceLocation(Atum.MOD_ID, "textures/entity/camel_carpet/camel_carpet_brown.png"), new ResourceLocation(Atum.MOD_ID, "textures/entity/camel_carpet/camel_carpet_green.png"), new ResourceLocation(Atum.MOD_ID, "textures/entity/camel_carpet/camel_carpet_red.png"), new ResourceLocation(Atum.MOD_ID, "textures/entity/camel_carpet/camel_carpet_black.png")};
    private final CamelModel<CamelEntity> model = new CamelModel<>(0.5F);

    public CamelDecorLayer(RenderLayerParent<CamelEntity, CamelModel<CamelEntity>> entityRenderer) {
        super(entityRenderer);
    }

    @Override
    public void render(@Nonnull PoseStack matrixStack, @Nonnull MultiBufferSource buffer, int packedLight, CamelEntity camel, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        DyeColor dyeColor = camel.getColor();
        ResourceLocation location;
        if (dyeColor != null) {
            location = CAMEL_DECOR_TEXTURES[dyeColor.getId()];

            this.getParentModel().copyPropertiesTo(this.model);
            this.model.setupAnim(camel, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            VertexConsumer ivertexbuilder = buffer.getBuffer(RenderType.entityCutoutNoCull(location));
            this.model.renderToBuffer(matrixStack, ivertexbuilder, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
}