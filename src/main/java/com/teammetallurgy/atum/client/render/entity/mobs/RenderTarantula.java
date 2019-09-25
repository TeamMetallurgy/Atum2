package com.teammetallurgy.atum.client.render.entity.mobs;

import com.teammetallurgy.atum.entity.animal.TarantulaEntity;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.client.model.ModelSpider;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public class RenderTarantula extends RenderLiving<TarantulaEntity> {
    private static final ResourceLocation TARANTULA_TEXTURE = new ResourceLocation(Constants.MOD_ID, "textures/entity/tarantula.png");

    public RenderTarantula(RenderManager manager) {
        super(manager, new ModelSpider(), 0.6F);
    }

    @Override
    protected float getDeathMaxRotation(TarantulaEntity tarantula)
    {
        return 180.0F;
    }

    @Override
    protected void preRenderCallback(TarantulaEntity tarantula, float partialTickTime) {
        GlStateManager.scale(0.6F, 0.6F, 0.6F);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(@Nonnull TarantulaEntity tarantula) {
        return TARANTULA_TEXTURE;
    }
}