package com.teammetallurgy.atum.client;

import com.google.common.collect.Maps;
import com.teammetallurgy.atum.Atum;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.ProgressManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.io.IOUtils;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.Map;

@SideOnly(Side.CLIENT)
public class TextureMapParticles extends TextureMap { //Originally made by Mezz for Forestry. Used with permission.

    public TextureMapParticles(String basePath) {
        super(basePath, null, true);
    }

    @Override
    public void loadTexture(@Nonnull IResourceManager resourceManager) {
        this.initMissingImage();
        this.deleteGlTexture();
        this.loadTextureAtlas(resourceManager);
    }

    @Override
    public void loadTextureAtlas(@Nonnull IResourceManager resourceManager) {
        int size = Minecraft.getGLMaximumTextureSize();
        Stitcher stitcher = new Stitcher(size, size, 0, 0);
        this.mapUploadedSprites.clear();
        this.listAnimatedSprites.clear();

        ProgressManager.ProgressBar bar = ProgressManager.push("Texture stitching", this.mapRegisteredSprites.size());

        for (Map.Entry<String, TextureAtlasSprite> entry : this.mapRegisteredSprites.entrySet()) {
            TextureAtlasSprite textureatlassprite = entry.getValue();
            ResourceLocation location = this.getResourceLocation(textureatlassprite);
            bar.step(location.getPath());
            IResource iResource = null;

            if (textureatlassprite.hasCustomLoader(resourceManager, location)) {
                if (textureatlassprite.load(resourceManager, location, l -> mapRegisteredSprites.get(l.toString()))) {
                    continue;
                }
            } else {
                try {
                    PngSizeInfo sizeInfo = PngSizeInfo.makeFromResource(resourceManager.getResource(location));
                    iResource = resourceManager.getResource(location);
                    boolean flag = iResource.getMetadata("animation") != null;
                    textureatlassprite.loadSprite(sizeInfo, flag);
                } catch (RuntimeException e) {
                    FMLClientHandler.instance().trackBrokenTexture(location, e.getMessage());
                    continue;
                } catch (IOException e) {
                    FMLClientHandler.instance().trackMissingTexture(location);
                    continue;
                } finally {
                    IOUtils.closeQuietly(iResource);
                }
            }

            stitcher.addSprite(textureatlassprite);
        }

        ProgressManager.pop(bar);

        this.missingImage.generateMipmaps(0);
        stitcher.addSprite(this.missingImage);
        bar = ProgressManager.push("Particle texture creation", 2);

        bar.step("Stitching");
        stitcher.doStitch();

        Atum.LOG.info("Created: {}x{} particle-atlas {}", stitcher.getCurrentWidth(), stitcher.getCurrentHeight(), this.basePath);
        bar.step("Allocating GL texture");
        TextureUtil.allocateTextureImpl(this.getGlTextureId(), 0, stitcher.getCurrentWidth(), stitcher.getCurrentHeight());
        Map<String, TextureAtlasSprite> map = Maps.newHashMap(this.mapRegisteredSprites);

        ProgressManager.pop(bar);
        bar = ProgressManager.push("Particle texture mipmap and upload", stitcher.getStichSlots().size());

        for (TextureAtlasSprite sprite : stitcher.getStichSlots()) {
            bar.step(sprite.getIconName());
            if (sprite == this.missingImage || this.generateMipmaps(resourceManager, sprite)) {
                String iconName = sprite.getIconName();
                map.remove(iconName);
                this.mapUploadedSprites.put(iconName, sprite);

                try {
                    TextureUtil.uploadTextureMipmap(sprite.getFrameTextureData(0), sprite.getIconWidth(), sprite.getIconHeight(), sprite.getOriginX(), sprite.getOriginY(), false, false);
                } catch (Throwable throwable) {
                    CrashReport crashReport = CrashReport.makeCrashReport(throwable, "Stitching particle texture atlas");
                    CrashReportCategory crashreportcategory = crashReport.makeCategory("Particle texture being stitched together");
                    crashreportcategory.addCrashSection("Particle atlas path", this.basePath);
                    crashreportcategory.addCrashSection("Particle sprite", sprite);
                    throw new ReportedException(crashReport);
                }
                if (sprite.hasAnimationMetadata()) {
                    this.listAnimatedSprites.add(sprite);
                }
            }
        }
        for (TextureAtlasSprite sprite : map.values()) {
            sprite.copyFrom(this.missingImage);
        }
        ProgressManager.pop(bar);
    }

    private boolean generateMipmaps(IResourceManager resourceManager, final TextureAtlasSprite sprite) {
        ResourceLocation location = this.getResourceLocation(sprite);
        IResource iResource = null;
        label9:
        {
            boolean shouldGenerate;
            if (sprite.hasCustomLoader(resourceManager, location)) {
                break label9;
            }
            try {
                iResource = resourceManager.getResource(location);
                sprite.loadSpriteFrames(iResource, 1);
                break label9;
            } catch (RuntimeException runtimeexception) {
                Atum.LOG.error("Unable to parse metadata from {}", location, runtimeexception);
                shouldGenerate = false;
            } catch (IOException ioexception) {
                Atum.LOG.error("Using missing texture, unable to load {}", location, ioexception);
                shouldGenerate = false;
                return shouldGenerate;
            } finally {
                IOUtils.closeQuietly(iResource);
            }
            return shouldGenerate;
        }

        try {
            sprite.generateMipmaps(0);
            return true;
        } catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Applying mipmap");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Sprite being mipmapped");
            crashreportcategory.addDetail("Sprite name", sprite::getIconName);
            crashreportcategory.addDetail("Sprite size", () -> sprite.getIconWidth() + " x " + sprite.getIconHeight());
            crashreportcategory.addDetail("Sprite frames", () -> sprite.getFrameCount() + " frames");
            crashreportcategory.addCrashSection("Mipmap levels", 0);
            throw new ReportedException(crashreport);
        }
    }

    private ResourceLocation getResourceLocation(TextureAtlasSprite sprite) {
        ResourceLocation location = new ResourceLocation(sprite.getIconName());
        return new ResourceLocation(location.getNamespace(), String.format("%s/%s%s", this.basePath, location.getPath(), ".png"));
    }
}