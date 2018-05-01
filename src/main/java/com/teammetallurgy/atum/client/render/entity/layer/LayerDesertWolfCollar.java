package com.teammetallurgy.atum.client.render.entity.layer;

import com.teammetallurgy.atum.client.render.entity.RenderDesertWolf;
import com.teammetallurgy.atum.entity.EntityDesertWolf;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.ResourceLocation;

public class LayerDesertWolfCollar implements LayerRenderer<EntityDesertWolf> { //TODO Check if the custom collar works as intended
    private static final ResourceLocation DESERT_WOLF_COLLAR = new ResourceLocation("atum:textures/entities/desert_wolf_collar.png");
    private final RenderDesertWolf wolfRenderer;

    public LayerDesertWolfCollar(RenderDesertWolf renderDesertWolf) {
        this.wolfRenderer = renderDesertWolf;
    }

    @Override
    public void doRenderLayer(EntityDesertWolf entityDesertWolf, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (entityDesertWolf.isTamed() && !entityDesertWolf.isInvisible()) {
            this.wolfRenderer.bindTexture(DESERT_WOLF_COLLAR);
            EnumDyeColor enumdyecolor = EnumDyeColor.byMetadata(entityDesertWolf.getCollarColor().getMetadata());
            float[] afloat = EntitySheep.getDyeRgb(enumdyecolor);
            GlStateManager.color(afloat[0], afloat[1], afloat[2]);
            this.wolfRenderer.getMainModel().render(entityDesertWolf, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }
    }

    @Override
    public boolean shouldCombineTextures() {
        return true;
    }
}