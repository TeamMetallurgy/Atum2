package com.teammetallurgy.atum.client.render.entity.mobs;

import com.google.common.collect.Maps;
import com.teammetallurgy.atum.entity.EntityCamel;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

public class RenderCamel extends RenderLiving<EntityCamel> {
    private static final Map<String, ResourceLocation> CACHE = Maps.newHashMap();

    public RenderCamel(RenderManager renderManager, ModelBase modelBase, float shadowSize) {
        super(renderManager, modelBase, shadowSize);
    }

    @Override
    protected float handleRotationFloat(EntityCamel camel, float rotation) {
        return camel.getRotationYawHead();
    }

    @Override
    public void doRender(@Nonnull EntityCamel camel, double x, double y, double z, float entityYaw, float partialTicks) {
        super.doRender(camel, x, y, z, entityYaw, partialTicks);
    }

    @Override
    @Nullable
    protected ResourceLocation getEntityTexture(@Nonnull EntityCamel camel) {
        String texture = camel.getTexture();
        ResourceLocation location = CACHE.get(texture);

        if (location == null) {
            location = new ResourceLocation(texture);
            CACHE.put(texture, location);
        }
        return location;
    }

    @Override
    protected void preRenderCallback(EntityCamel camel, float partialTickTime) {
        float scale = 1F;
        GlStateManager.scale(scale, scale, scale);
    }
}