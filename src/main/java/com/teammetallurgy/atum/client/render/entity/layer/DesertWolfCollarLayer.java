package com.teammetallurgy.atum.client.render.entity.layer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.teammetallurgy.atum.client.model.entity.DesertWolfModel;
import com.teammetallurgy.atum.entity.animal.DesertWolfEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class DesertWolfCollarLayer extends LayerRenderer<DesertWolfEntity, DesertWolfModel<DesertWolfEntity>> {
    private static final ResourceLocation COLLAR_TEXTURE = new ResourceLocation("textures/entity/wolf/wolf_collar.png");

    public DesertWolfCollarLayer(IEntityRenderer<DesertWolfEntity, DesertWolfModel<DesertWolfEntity>> renderDesertWolf) {
        super(renderDesertWolf);
    }

    @Override
    public void render(@Nonnull MatrixStack matrixStack, IRenderTypeBuffer buffer, int p_225628_3_, DesertWolfEntity desertWolf, float p_225628_5_, float p_225628_6_, float p_225628_7_, float p_225628_8_, float p_225628_9_, float p_225628_10_) {
        if (desertWolf.isTamed() && !desertWolf.isInvisible()) {
            float[] color = desertWolf.getCollarColor().getColorComponentValues();
            renderCutoutModel(this.getEntityModel(), COLLAR_TEXTURE, matrixStack, buffer, p_225628_3_, desertWolf, color[0], color[1], color[2]);
        }
    }
}