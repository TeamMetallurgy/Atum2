package com.teammetallurgy.atum.client.render.entity.mobs;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.teammetallurgy.atum.entity.villager.AtumVillagerEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.VillagerLevelPendantLayer;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class AtumVillagerRenderer extends MobRenderer<AtumVillagerEntity, PlayerModel<AtumVillagerEntity>> {
    private static final ResourceLocation VILLAGER_TEXTURES = new ResourceLocation("textures/entity/villager/villager.png");

    public AtumVillagerRenderer(EntityRendererManager renderManager, IReloadableResourceManager resourceManager) {
        super(renderManager, new PlayerModel<>(0.0F, false), 0.5F);
        //this.addLayer(new VillagerLevelPendantLayer(this, resourceManager, "villager")); //TODO Add custom ones
    }

    @Override
    @Nonnull
    public ResourceLocation getEntityTexture(@Nonnull AtumVillagerEntity entity) {
        return VILLAGER_TEXTURES;
    }

    @Override
    protected void preRenderCallback(AtumVillagerEntity atumVillagerEntity, @Nonnull MatrixStack matrixStack, float partialTickTime) {
        float f = 0.9375F;
        if (atumVillagerEntity.isChild()) {
            f = (float) ((double) f * 0.5D);
            this.shadowSize = 0.25F;
        } else {
            this.shadowSize = 0.5F;
        }
        matrixStack.scale(f, f, f);
    }
}