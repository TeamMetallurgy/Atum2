package com.teammetallurgy.atum.client.render.entity.mobs;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.client.model.entity.QuailModel;
import com.teammetallurgy.atum.entity.animal.QuailEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nonnull;

public class QuailRender extends MobRenderer<QuailEntity, QuailModel<QuailEntity>> {
    private static final ResourceLocation QUAIL = new ResourceLocation(Atum.MOD_ID, "textures/entity/quail.png");

    public QuailRender(EntityRendererManager renderManager) {
        super(renderManager, new QuailModel<>(), 0.25F);
    }

    @Override
    @Nonnull
    public ResourceLocation getEntityTexture(@Nonnull QuailEntity quail) {
        return QUAIL;
    }

    @Override
    protected float handleRotationFloat(QuailEntity quail, float partialTicks) {
        float f = MathHelper.lerp(partialTicks, quail.oFlap, quail.wingRotation);
        float f1 = MathHelper.lerp(partialTicks, quail.oFlapSpeed, quail.destPos);
        return (MathHelper.sin(f) + 1.0F) * f1;
    }
}