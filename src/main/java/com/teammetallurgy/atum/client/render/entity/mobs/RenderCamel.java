package com.teammetallurgy.atum.client.render.entity.mobs;

import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.google.common.collect.Maps;
import com.teammetallurgy.atum.entity.EntityCamel;
import com.teammetallurgy.atum.entity.EntityDesertWolf;
import com.teammetallurgy.atum.entity.bandit.EntityBanditBase;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderCamel extends RenderLiving<EntityCamel> {
    private static final Map<String, ResourceLocation> CACHE = Maps.newHashMap();

    public RenderCamel(RenderManager renderManager, ModelBase modelBase, float shadowSize) {
        super(renderManager, modelBase, shadowSize);
    }

    @Override
    protected float handleRotationFloat(EntityCamel entityDesertWolf, float rotation) {
        return entityDesertWolf.getRotationYawHead();
    }

    @Override
    public void doRender(@Nonnull EntityCamel entityDesertWolf, double x, double y, double z, float entityYaw, float partialTicks) {
        super.doRender(entityDesertWolf, x, y, z, entityYaw, partialTicks);
    }

    @Override
    @Nullable
    protected ResourceLocation getEntityTexture(@Nonnull EntityCamel entity) {
        String texture = entity.getTexture();
        ResourceLocation location = CACHE.get(texture);

        if (location == null) {
            location = new ResourceLocation(texture);
            CACHE.put(texture, location);
        }
        return location;
    }

    @Override
    protected void preRenderCallback(EntityCamel entityDesertWolf, float partialTickTime)
    {
        float scale = 1f;
        GlStateManager.scale(scale, scale, scale);
    }
}
