package com.teammetallurgy.atum.client.render.entity.layer;

import com.teammetallurgy.atum.client.render.entity.RenderDesertWolf;
import com.teammetallurgy.atum.entity.EntityDesertWolf;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class LayerDesertWolfCollar implements LayerRenderer<EntityDesertWolf> { //TODO Check if the custom collar works as intended
    private static final ResourceLocation DESERT_WOLF_COLLAR = new ResourceLocation(Constants.MOD_ID, "textures/entities/desert_wolf_collar.png");
    private final RenderDesertWolf wolfRenderer;

    public LayerDesertWolfCollar(RenderDesertWolf renderDesertWolf) {
        this.wolfRenderer = renderDesertWolf;
    }

    @Override
    public void doRenderLayer(@Nonnull EntityDesertWolf desertWolf, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (desertWolf.isTamed() && !desertWolf.isInvisible()) {
            this.wolfRenderer.bindTexture(DESERT_WOLF_COLLAR);
            EnumDyeColor color = EnumDyeColor.byMetadata(desertWolf.getCollarColor().getMetadata());
            float[] rgb = EntitySheep.getDyeRgb(color);
            GlStateManager.color(rgb[0], rgb[1], rgb[2]);
            this.wolfRenderer.getMainModel().render(desertWolf, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }
    }

    @Override
    public boolean shouldCombineTextures() {
        return true;
    }
}