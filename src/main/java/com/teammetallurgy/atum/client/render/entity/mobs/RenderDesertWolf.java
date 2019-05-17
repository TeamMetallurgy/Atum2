package com.teammetallurgy.atum.client.render.entity.mobs;

import com.google.common.collect.Maps;
import com.teammetallurgy.atum.client.render.entity.layer.LayerDesertWolfCollar;
import com.teammetallurgy.atum.entity.animal.EntityDesertWolf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.LayeredTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.Map;

@SideOnly(Side.CLIENT)
public class RenderDesertWolf extends RenderLiving<EntityDesertWolf> {
    private static final Map<String, ResourceLocation> CACHE = Maps.newHashMap();

    public RenderDesertWolf(RenderManager renderManager, ModelBase modelBase, float shadowSize) {
        super(renderManager, modelBase, shadowSize);
        this.addLayer(new LayerDesertWolfCollar(this));
    }

    @Override
    protected float handleRotationFloat(EntityDesertWolf desertWolf, float rotation) {
        return desertWolf.getTailRotation();
    }

    @Override
    public void doRender(@Nonnull EntityDesertWolf desertWolf, double x, double y, double z, float entityYaw, float partialTicks) {
        if (desertWolf.isWolfWet()) {
            GlStateManager.pushMatrix();
            float f = desertWolf.getBrightness() * desertWolf.getShadingWhileWet(partialTicks);
            GlStateManager.color(f, f, f);
            GlStateManager.popMatrix();
        }
        super.doRender(desertWolf, x, y, z, entityYaw, partialTicks);
    }

    @Override
    protected ResourceLocation getEntityTexture(@Nonnull EntityDesertWolf desertWolf) {
        String textureName = desertWolf.getTextureName();

        ResourceLocation location = CACHE.get(textureName);
        if (location == null) {
            location = new ResourceLocation(textureName);
            Minecraft.getMinecraft().getTextureManager().loadTexture(location, new LayeredTexture(desertWolf.getTextures()));
            CACHE.put(textureName, location);
        }
        return location;
    }

    @Override
    protected void preRenderCallback(EntityDesertWolf desertWolf, float partialTickTime) {
        if (desertWolf.isAlpha()) {
            float scale = 1.5F;
            GlStateManager.scale(scale, scale, scale);
        }
    }
}