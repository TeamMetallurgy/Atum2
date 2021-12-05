package com.teammetallurgy.atum.client.render.entity.mobs;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.teammetallurgy.atum.client.render.entity.layer.VillagerLayer;
import com.teammetallurgy.atum.entity.villager.AtumVillagerEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.Map;

public class AtumVillagerRenderer extends MobRenderer<AtumVillagerEntity, PlayerModel<AtumVillagerEntity>> {
    private static final Map<String, ResourceLocation> CACHE = Maps.newHashMap();

    public AtumVillagerRenderer(EntityRendererManager renderManager, boolean isFemale) {
        super(renderManager, new PlayerModel<>(0.0F, isFemale), 0.5F);
        this.addLayer(new VillagerLayer<>(this, "villager"));
    }

    @Override
    protected void preRenderCallback(AtumVillagerEntity atumVillagerEntity, @Nonnull MatrixStack matrixStack, float partialTickTime) {
        float f = 0.9375F;
        if (atumVillagerEntity.isChild()) {
            f = (float) ((double) f * 0.8D);
            this.shadowSize = 0.25F;
        } else {
            this.shadowSize = 0.5F;
        }
        matrixStack.scale(f, f, f);
    }

    @Override
    @Nonnull
    public ResourceLocation getEntityTexture(@Nonnull AtumVillagerEntity entity) {
        String texture = entity.getTexture();
        ResourceLocation location = CACHE.get(texture);

        if (location == null) {
            location = new ResourceLocation(texture);
            CACHE.put(texture, location);
        }
        return location;
    }
}