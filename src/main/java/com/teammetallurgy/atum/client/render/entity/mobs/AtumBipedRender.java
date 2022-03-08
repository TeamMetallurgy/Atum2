package com.teammetallurgy.atum.client.render.entity.mobs;

import com.google.common.collect.Maps;
import com.teammetallurgy.atum.entity.ITexture;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
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

    public AtumBipedRender(EntityRendererProvider.Context context) {
        this(context, false);
    }

    public AtumBipedRender(EntityRendererProvider.Context context, boolean isSlim) {
        this(context, (M) new PlayerModel(context.bakeLayer(isSlim ? ModelLayers.PLAYER_SLIM : ModelLayers.PLAYER), false), isSlim);
    }

    public AtumBipedRender(EntityRendererProvider.Context context, M model, boolean isSlim) {
        super(context, model, 0.5F);
        this.addLayer(new HumanoidArmorLayer<>(this, new HumanoidModel<>(context.bakeLayer(isSlim ? ModelLayers.PLAYER_SLIM_INNER_ARMOR : ModelLayers.PLAYER_INNER_ARMOR)), new HumanoidModel<>(context.bakeLayer(isSlim ? ModelLayers.PLAYER_SLIM_OUTER_ARMOR : ModelLayers.PLAYER_OUTER_ARMOR))));
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