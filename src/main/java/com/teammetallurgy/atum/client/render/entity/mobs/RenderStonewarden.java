package com.teammetallurgy.atum.client.render.entity.mobs;

import com.google.common.collect.Maps;
import com.teammetallurgy.atum.client.model.entity.ModelStonewarden;
import com.teammetallurgy.atum.entity.stone.StonewardenEntity;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class RenderStonewarden extends RenderLiving<StonewardenEntity> {
    private static final Map<Integer, ResourceLocation> CACHE = Maps.newHashMap();

    public RenderStonewarden(RenderManager manager) {
        super(manager, new ModelStonewarden(), 0.5F);
    }

    @Override
    protected void applyRotations(StonewardenEntity stonewarden, float ageInTicks, float rotationYaw, float partialTicks) {
        super.applyRotations(stonewarden, ageInTicks, rotationYaw, partialTicks);
        if ((double) stonewarden.limbSwingAmount >= 0.01D) {
            float swingValue = stonewarden.limbSwing - stonewarden.limbSwingAmount * (1.0F - partialTicks) + 6.0F;
            float swing = (Math.abs(swingValue % 13.0F - 6.5F) - 3.25F) / 3.25F;
            GlStateManager.rotate(6.5F * swing, 0.0F, 0.0F, 1.0F);
        }
    }

    @Override
    protected ResourceLocation getEntityTexture(@Nonnull StonewardenEntity stonewarden) {
        ResourceLocation location = CACHE.get(stonewarden.getVariant());

        if (location == null) {
            location = new ResourceLocation(Constants.MOD_ID, "textures/entity/stonewarden_" + stonewarden.getVariant() + ".png");
            CACHE.put(stonewarden.getVariant(), location);
        }
        return location;
    }
}