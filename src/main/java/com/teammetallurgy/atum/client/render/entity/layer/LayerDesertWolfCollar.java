package com.teammetallurgy.atum.client.render.entity.layer;

import com.teammetallurgy.atum.client.render.entity.mobs.RenderDesertWolf;
import com.teammetallurgy.atum.entity.animal.EntityDesertWolf;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

@SideOnly(Side.CLIENT)
public class LayerDesertWolfCollar implements LayerRenderer<EntityDesertWolf> {
    private static final ResourceLocation COLLAR = new ResourceLocation("textures/entity/wolf/wolf_collar.png");
    private final RenderDesertWolf wolfRenderer;

    public LayerDesertWolfCollar(RenderDesertWolf renderDesertWolf) {
        this.wolfRenderer = renderDesertWolf;
    }

    @Override
    public void doRenderLayer(@Nonnull EntityDesertWolf desertWolf, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (desertWolf.isTamed() && !desertWolf.isInvisible()) {
            GlStateManager.pushMatrix();
            this.wolfRenderer.bindTexture(COLLAR);
            float[] afloat = desertWolf.getCollarColor().getColorComponentValues();
            GlStateManager.color(afloat[0], afloat[1], afloat[2]);
            this.wolfRenderer.getMainModel().render(desertWolf, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            GlStateManager.popMatrix();
        }
    }

    @Override
    public boolean shouldCombineTextures() {
        return true;
    }
}