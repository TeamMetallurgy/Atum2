package com.teammetallurgy.atum.client.render.entity.mobs;

import com.google.common.collect.Maps;
import com.teammetallurgy.atum.entity.ITexture;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class AtumMobRender<T extends Mob & ITexture, M extends EntityModel<T>> extends MobRenderer<T, M> {
    private static final Map<String, ResourceLocation> CACHE = Maps.newHashMap();

    public AtumMobRender(EntityRendererProvider.Context context, M model) {
        super(context, model, 0.5F);
    }

    @Override
    @Nonnull
    public ResourceLocation getTextureLocation(T entity) {
        String texture = entity.getTexture();
        ResourceLocation location = CACHE.get(texture);

        if (location == null) {
            location = new ResourceLocation(texture);
            CACHE.put(texture, location);
        }
        return location;
    }
}