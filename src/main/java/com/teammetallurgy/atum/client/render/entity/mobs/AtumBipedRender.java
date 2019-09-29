package com.teammetallurgy.atum.client.render.entity.mobs;

import com.google.common.collect.Maps;
import com.teammetallurgy.atum.entity.ITexture;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.entity.MobEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class AtumBipedRender<T extends MobEntity & ITexture, M extends BipedModel<T>> extends BipedRenderer<T, M> {
    private static final Map<String, ResourceLocation> CACHE = Maps.newHashMap();

    public AtumBipedRender(EntityRendererManager manager) {
        this(manager, (M) new PlayerModel(0.0F, false), (M) new PlayerModel(0.5F, false), (M) new PlayerModel(1.0F, false));
    }

    public AtumBipedRender(EntityRendererManager renderManager, M model, M modelArmorHalf, M modelArmorFull) {
        super(renderManager, model, 0.5F);
        this.addLayer(new BipedArmorLayer(this, modelArmorHalf, modelArmorFull));
    }

    @Override
    @Nullable
    protected ResourceLocation getEntityTexture(T entity) {
        String texture = entity.getTexture();
        ResourceLocation location = CACHE.get(texture);

        if (location == null) {
            location = new ResourceLocation(texture);
            CACHE.put(texture, location);
        }
        return location;
    }
}