package com.teammetallurgy.atum.client.render.entity.layer;

import com.teammetallurgy.atum.client.render.entity.mobs.RenderDesertWolf;
import com.teammetallurgy.atum.entity.animal.EntityDesertWolf;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

@SideOnly(Side.CLIENT)
public class LayerWolfSaddle implements LayerRenderer<EntityDesertWolf> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Constants.MOD_ID, "textures/entities/desert_wolf_saddle.png");
    private final RenderDesertWolf desertWolfRender;

    public LayerWolfSaddle(RenderDesertWolf desertWolfRender) {
        this.desertWolfRender = desertWolfRender;
    }

    @Override
    public void doRenderLayer(@Nonnull EntityDesertWolf desertWolf, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (desertWolf.isSaddled()) {
            this.desertWolfRender.bindTexture(TEXTURE);
            this.desertWolfRender.getMainModel().render(desertWolf, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}