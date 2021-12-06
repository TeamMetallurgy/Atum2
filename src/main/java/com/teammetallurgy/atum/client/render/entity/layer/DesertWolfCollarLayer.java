package com.teammetallurgy.atum.client.render.entity.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.teammetallurgy.atum.client.model.entity.DesertWolfModel;
import com.teammetallurgy.atum.entity.animal.DesertWolfEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class DesertWolfCollarLayer extends RenderLayer<DesertWolfEntity, DesertWolfModel<DesertWolfEntity>> {
    private static final ResourceLocation COLLAR_TEXTURE = new ResourceLocation("textures/entity/wolf/wolf_collar.png");

    public DesertWolfCollarLayer(RenderLayerParent<DesertWolfEntity, DesertWolfModel<DesertWolfEntity>> renderDesertWolf) {
        super(renderDesertWolf);
    }

    @Override
    public void render(@Nonnull PoseStack matrixStack, @Nonnull MultiBufferSource buffer, int p_225628_3_, DesertWolfEntity desertWolf, float p_225628_5_, float p_225628_6_, float p_225628_7_, float p_225628_8_, float p_225628_9_, float p_225628_10_) {
        if (desertWolf.isTame() && !desertWolf.isInvisible()) {
            float[] color = desertWolf.getCollarColor().getTextureDiffuseColors();
            renderColoredCutoutModel(this.getParentModel(), COLLAR_TEXTURE, matrixStack, buffer, p_225628_3_, desertWolf, color[0], color[1], color[2]);
        }
    }
}