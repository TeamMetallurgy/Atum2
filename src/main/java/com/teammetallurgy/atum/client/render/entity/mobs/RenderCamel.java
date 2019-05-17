package com.teammetallurgy.atum.client.render.entity.mobs;

import com.google.common.collect.Maps;
import com.teammetallurgy.atum.client.model.entity.ModelCamel;
import com.teammetallurgy.atum.entity.animal.EntityCamel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.LayeredTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

@SideOnly(Side.CLIENT)
public class RenderCamel extends RenderLiving<EntityCamel> {
    private static final Map<String, ResourceLocation> CACHE = Maps.newHashMap();

    public RenderCamel(RenderManager renderManager) {
        super(renderManager, new ModelCamel(0.0F), 0.7F);
    }

    @Override
    @Nullable
    protected ResourceLocation getEntityTexture(@Nonnull EntityCamel camel) {
        String textureName = camel.getTextureName();

        ResourceLocation location = CACHE.get(textureName);
        if (location == null) {
            location = new ResourceLocation(textureName);
            Minecraft.getMinecraft().getTextureManager().loadTexture(location, new LayeredTexture(camel.getTextures()));
            CACHE.put(textureName, location);
        }

        return location;
    }
}