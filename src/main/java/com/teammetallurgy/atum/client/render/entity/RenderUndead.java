package com.teammetallurgy.atum.client.render.entity;

import com.google.common.collect.Maps;
import com.teammetallurgy.atum.entity.undead.EntityUndeadBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

public class RenderUndead extends RenderBiped<EntityUndeadBase> {
    private static final Map<String, ResourceLocation> CACHE = Maps.newHashMap();

    public RenderUndead(RenderManager renderManager, ModelBiped modelBiped) {
        super(renderManager, modelBiped, 0.5F);
    }

    @Override
    @Nullable
    protected ResourceLocation getEntityTexture(@Nonnull EntityUndeadBase entity) {
        String texture = entity.getTexture();
        ResourceLocation location = CACHE.get(texture);

        if (location == null) {
            location = new ResourceLocation(texture);
            CACHE.put(texture, location);
        }
        return location;
    }
}