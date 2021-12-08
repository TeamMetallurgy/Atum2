package com.teammetallurgy.atum.client.render.entity.mobs;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import com.teammetallurgy.atum.client.render.entity.layer.VillagerLayer;
import com.teammetallurgy.atum.entity.villager.AtumVillagerEntity;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.Map;

public class AtumVillagerRenderer extends MobRenderer<AtumVillagerEntity, PlayerModel<AtumVillagerEntity>> {
    private static final Map<String, ResourceLocation> CACHE = Maps.newHashMap();

    public AtumVillagerRenderer(EntityRendererProvider.Context context, boolean isFemale) {
        super(context, new PlayerModel<>(context.bakeLayer(ModelLayers.PLAYER), isFemale), 0.5F);
        this.addLayer(new VillagerLayer<>(this, "villager"));
    }

    @Override
    protected void scale(AtumVillagerEntity atumVillagerEntity, @Nonnull PoseStack matrixStack, float partialTickTime) {
        float f = 0.9375F;
        if (atumVillagerEntity.isBaby()) {
            f = (float) ((double) f * 0.8D);
            this.shadowRadius = 0.25F;
        } else {
            this.shadowRadius = 0.5F;
        }
        matrixStack.scale(f, f, f);
    }

    @Override
    @Nonnull
    public ResourceLocation getTextureLocation(@Nonnull AtumVillagerEntity entity) {
        String texture = entity.getTexture();
        ResourceLocation location = CACHE.get(texture);

        if (location == null) {
            location = new ResourceLocation(texture);
            CACHE.put(texture, location);
        }
        return location;
    }
}