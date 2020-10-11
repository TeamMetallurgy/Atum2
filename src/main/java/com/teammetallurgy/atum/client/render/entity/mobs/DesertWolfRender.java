package com.teammetallurgy.atum.client.render.entity.mobs;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.client.model.entity.DesertWolfModel;
import com.teammetallurgy.atum.client.render.entity.layer.DesertWolfArmorLayer;
import com.teammetallurgy.atum.client.render.entity.layer.DesertWolfCollarLayer;
import com.teammetallurgy.atum.client.render.entity.layer.DesertWolfSaddleLayer;
import com.teammetallurgy.atum.entity.animal.DesertWolfEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class DesertWolfRender extends MobRenderer<DesertWolfEntity, DesertWolfModel<DesertWolfEntity>> {
    private static final Map<String, ResourceLocation> CACHE = Maps.newHashMap();
    private static final ResourceLocation TAMED_DESERT_WOLF_TEXTURES = new ResourceLocation(Atum.MOD_ID, "textures/entity/desert_wolf_tame.png");
    private static final ResourceLocation ANGRY_DESERT_WOLF_TEXTURES = new ResourceLocation(Atum.MOD_ID, "textures/entity/desert_wolf_angry.png");

    public DesertWolfRender(EntityRendererManager renderManager) {
        super(renderManager, new DesertWolfModel<>(0.0F), 0.5F);
        this.addLayer(new DesertWolfCollarLayer(this));
        this.addLayer(new DesertWolfSaddleLayer(this));
        this.addLayer(new DesertWolfArmorLayer(this));
    }

    @Override
    protected float handleRotationFloat(DesertWolfEntity desertWolf, float rotation) {
        return desertWolf.getTailRotation();
    }

    @Override
    public void render(@Nonnull DesertWolfEntity desertWolf, float entityYaw, float partialTicks, @Nonnull MatrixStack matrixStack, @Nonnull IRenderTypeBuffer buffer, int i) {
        if (desertWolf.isWolfWet()) {
            float f = desertWolf.getBrightness() * desertWolf.getShadingWhileWet(partialTicks);
            this.entityModel.setTint(f, f, f);
        }
        super.render(desertWolf, entityYaw, partialTicks, matrixStack, buffer, i);
        if (desertWolf.isWolfWet()) {
            this.entityModel.setTint(1.0F, 1.0F, 1.0F);
        }
    }

    @Override
    @Nonnull
    public ResourceLocation getEntityTexture(@Nonnull DesertWolfEntity desertWolf) {
        String textureName = desertWolf.getTexture();

        ResourceLocation location = CACHE.get(textureName);
        if (location == null) {
            location = desertWolf.func_233678_J__() ? ANGRY_DESERT_WOLF_TEXTURES : TAMED_DESERT_WOLF_TEXTURES;
            CACHE.put(textureName, location);
        }
        return location;
    }

    @Override
    protected void preRenderCallback(DesertWolfEntity desertWolf, @Nonnull MatrixStack matrixStack, float partialTickTime) {
        if (desertWolf.isAlpha()) {
            float scale = 1.5F;
            matrixStack.scale(scale, scale, scale);
        }
    }
}