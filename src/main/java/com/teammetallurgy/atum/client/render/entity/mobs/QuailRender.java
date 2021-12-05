package com.teammetallurgy.atum.client.render.entity.mobs;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.client.model.entity.QuailModel;
import com.teammetallurgy.atum.entity.animal.QuailEntity;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import javax.annotation.Nonnull;

public class QuailRender extends MobRenderer<QuailEntity, QuailModel<QuailEntity>> {
    private static final ResourceLocation QUAIL = new ResourceLocation(Atum.MOD_ID, "textures/entity/quail.png");

    public QuailRender(EntityRenderDispatcher renderManager) {
        super(renderManager, new QuailModel<>(), 0.25F);
    }

    @Override
    @Nonnull
    public ResourceLocation getTextureLocation(@Nonnull QuailEntity quail) {
        return QUAIL;
    }

    @Override
    protected float getBob(QuailEntity quail, float partialTicks) {
        float f = Mth.lerp(partialTicks, quail.oFlap, quail.wingRotation);
        float f1 = Mth.lerp(partialTicks, quail.oFlapSpeed, quail.destPos);
        return (Mth.sin(f) + 1.0F) * f1;
    }
}