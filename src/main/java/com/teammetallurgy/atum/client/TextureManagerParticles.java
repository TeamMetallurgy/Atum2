package com.teammetallurgy.atum.client;

import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TextureManagerParticles {
    public static final ResourceLocation LOCATION_PARTICLES = new ResourceLocation(Constants.MOD_ID, "textures/atlas/particles");
    public static final TextureManagerParticles INSTANCE = new TextureManagerParticles();
    private final TextureMapParticles textureMap;

    private TextureManagerParticles() {
        this.textureMap = new TextureMapParticles("textures");
    }

    public TextureMapParticles getTextureMap() {
        return textureMap;
    }

    public TextureAtlasSprite registerSprite(ResourceLocation location) {
        return getTextureMap().registerSprite(location);
    }

    public TextureAtlasSprite getSprite(ResourceLocation location) {
        return getTextureMap().getAtlasSprite(location.toString());
    }

    public void bindTextureMap() {
        TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();
        textureManager.bindTexture(LOCATION_PARTICLES);
    }
}