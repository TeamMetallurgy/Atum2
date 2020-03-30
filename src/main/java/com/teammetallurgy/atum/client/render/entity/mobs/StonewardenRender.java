package com.teammetallurgy.atum.client.render.entity.mobs;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.teammetallurgy.atum.client.model.entity.StonewardenModel;
import com.teammetallurgy.atum.entity.stone.StonewardenEntity;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class StonewardenRender extends MobRenderer<StonewardenEntity, StonewardenModel<StonewardenEntity>> {
    private static final Map<Integer, ResourceLocation> CACHE = Maps.newHashMap();

    public StonewardenRender(EntityRendererManager manager) {
        super(manager, new StonewardenModel<>(), 0.5F);
    }

    @Override
    protected void applyRotations(StonewardenEntity stonewarden, @Nonnull MatrixStack matrixStack, float ageInTicks, float rotationYaw, float partialTicks) {
        super.applyRotations(stonewarden, matrixStack, ageInTicks, rotationYaw, partialTicks);
        if ((double) stonewarden.limbSwingAmount >= 0.01D) {
            float swingValue = stonewarden.limbSwing - stonewarden.limbSwingAmount * (1.0F - partialTicks) + 6.0F;
            float swing = (Math.abs(swingValue % 13.0F - 6.5F) - 3.25F) / 3.25F;
            matrixStack.rotate(Vector3f.ZP.rotationDegrees(6.5F * swing));
        }
    }

    @Override
    @Nonnull
    public ResourceLocation getEntityTexture(@Nonnull StonewardenEntity stonewarden) {
        ResourceLocation location = CACHE.get(stonewarden.getVariant());

        if (location == null) {
            location = new ResourceLocation(Constants.MOD_ID, "textures/entity/stonewarden_" + stonewarden.getVariant() + ".png");
            CACHE.put(stonewarden.getVariant(), location);
        }
        return location;
    }
}