package com.teammetallurgy.atum.client.render.entity.mobs;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.client.ClientHandler;
import com.teammetallurgy.atum.client.model.entity.DesertWolfModel;
import com.teammetallurgy.atum.client.render.entity.layer.DesertWolfArmorLayer;
import com.teammetallurgy.atum.client.render.entity.layer.DesertWolfCollarLayer;
import com.teammetallurgy.atum.client.render.entity.layer.DesertWolfSaddleLayer;
import com.teammetallurgy.atum.entity.animal.DesertWolfEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class DesertWolfRender extends MobRenderer<DesertWolfEntity, DesertWolfModel<DesertWolfEntity>> {
    private static final Map<String, ResourceLocation> CACHE = Maps.newHashMap();
    private static final ResourceLocation TAMED_DESERT_WOLF_TEXTURES = new ResourceLocation(Atum.MOD_ID, "textures/entity/desert_wolf_tame.png");
    private static final ResourceLocation ANGRY_DESERT_WOLF_TEXTURES = new ResourceLocation(Atum.MOD_ID, "textures/entity/desert_wolf_angry.png");

    public DesertWolfRender(EntityRendererProvider.Context context) {
        super(context, new DesertWolfModel<>(context.bakeLayer(ClientHandler.DESERT_WOLF)), 0.5F);
        this.addLayer(new DesertWolfCollarLayer(this));
        this.addLayer(new DesertWolfSaddleLayer(this, context.getModelSet()));
        this.addLayer(new DesertWolfArmorLayer(this, context.getModelSet()));
    }

    @Override
    protected float getBob(DesertWolfEntity desertWolf, float rotation) {
        return desertWolf.getTailRotation();
    }

    @Override
    public void render(@Nonnull DesertWolfEntity desertWolf, float entityYaw, float partialTicks, @Nonnull PoseStack matrixStack, @Nonnull MultiBufferSource buffer, int i) {
        if (desertWolf.isWolfWet()) {
            float f = desertWolf.getBrightness() * desertWolf.getShadingWhileWet(partialTicks);
            this.model.setColor(f, f, f);
        }
        super.render(desertWolf, entityYaw, partialTicks, matrixStack, buffer, i);
        if (desertWolf.isWolfWet()) {
            this.model.setColor(1.0F, 1.0F, 1.0F);
        }
    }

    @Override
    @Nonnull
    public ResourceLocation getTextureLocation(@Nonnull DesertWolfEntity desertWolf) {
        String textureName = desertWolf.getTexture();

        ResourceLocation location = CACHE.get(textureName);
        if (location == null) {
            location = desertWolf.isAngry() ? ANGRY_DESERT_WOLF_TEXTURES : TAMED_DESERT_WOLF_TEXTURES;
            CACHE.put(textureName, location);
        }
        return location;
    }

    @Override
    protected void scale(DesertWolfEntity desertWolf, @Nonnull PoseStack matrixStack, float partialTickTime) {
        if (desertWolf.isAlpha()) {
            float scale = 1.5F;
            matrixStack.scale(scale, scale, scale);
        }
    }
}