package com.teammetallurgy.atum.client.render.entity.mobs;

import com.mojang.blaze3d.platform.GlStateManager;
import com.teammetallurgy.atum.entity.animal.TarantulaEntity;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.SpiderModel;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public class TarantulaRender<T extends TarantulaEntity> extends MobRenderer<T, SpiderModel<T>> {
    private static final ResourceLocation TARANTULA_TEXTURE = new ResourceLocation(Constants.MOD_ID, "textures/entity/tarantula.png");

    public TarantulaRender(EntityRendererManager manager) {
        super(manager, new SpiderModel<>(), 0.6F);
    }

    @Override
    protected float getDeathMaxRotation(T tarantula) {
        return 180.0F;
    }

    @Override
    protected void preRenderCallback(T tarantula, float partialTickTime) {
        GlStateManager.scalef(0.6F, 0.6F, 0.6F);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(@Nonnull T tarantula) {
        return TARANTULA_TEXTURE;
    }
}