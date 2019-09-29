package com.teammetallurgy.atum.client.render.entity.layer;

import com.mojang.blaze3d.platform.GlStateManager;
import com.teammetallurgy.atum.client.model.entity.DesertWolfModel;
import com.teammetallurgy.atum.entity.animal.DesertWolfEntity;
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
    public void render(@Nonnull DesertWolfEntity desertWolf, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (desertWolf.isTamed() && !desertWolf.isInvisible()) {
            GlStateManager.pushMatrix();
            this.bindTexture(COLLAR_TEXTURE);
            float[] afloat = desertWolf.getCollarColor().getColorComponentValues();
            GlStateManager.color3f(afloat[0], afloat[1], afloat[2]);
            this.getEntityModel().render(desertWolf, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            GlStateManager.popMatrix();
        }
    }

    @Override
    public boolean shouldCombineTextures() {
        return true;
    }
}