package com.teammetallurgy.atum.client.render.entity.mobs;

import com.google.common.collect.Maps;
import com.teammetallurgy.atum.entity.ITexture;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class AtumBipedRender<T extends Mob & ITexture, M extends HumanoidModel<T>> extends HumanoidMobRenderer<T, M> {
    private static final Map<String, ResourceLocation> CACHE = Maps.newHashMap();

    public AtumBipedRender(EntityRenderDispatcher manager) {
        this(manager, (M) new PlayerModel(0.0F, false), (M) new PlayerModel(0.5F, false), (M) new PlayerModel(1.0F, false));
    }

    public AtumBipedRender(EntityRenderDispatcher renderManager, M model, M modelArmorHalf, M modelArmorFull) {
        super(renderManager, model, 0.5F);
        this.addLayer(new HumanoidArmorLayer<>(this, modelArmorHalf, modelArmorFull));
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