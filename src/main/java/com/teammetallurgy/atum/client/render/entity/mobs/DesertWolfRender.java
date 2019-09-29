package com.teammetallurgy.atum.client.render.entity.mobs;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.platform.GlStateManager;
import com.teammetallurgy.atum.client.model.entity.DesertWolfModel;
import com.teammetallurgy.atum.client.render.entity.layer.DesertWolfCollarLayer;
import com.teammetallurgy.atum.entity.animal.DesertWolfEntity;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.texture.LayeredTexture;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class DesertWolfRender extends MobRenderer<DesertWolfEntity, DesertWolfModel<DesertWolfEntity>> {
    private static final Map<String, ResourceLocation> CACHE = Maps.newHashMap();
    private static final ResourceLocation TAMED_DESERT_WOLF_TEXTURES = new ResourceLocation(Constants.MOD_ID, "textures/entity/desert_wolf_tame.png");
    private static final ResourceLocation ANGRY_DESERT_WOLF_TEXTURES = new ResourceLocation(Constants.MOD_ID, "textures/entity/desert_wolf_angry.png");
    private static final ResourceLocation SADDLE_DESERT_WOLF_TEXTURE = new ResourceLocation(Constants.MOD_ID, "textures/entity/desert_wolf_saddle.png");

    public DesertWolfRender(EntityRendererManager renderManager) {
        super(renderManager, new DesertWolfModel<>(), 0.5F);
        this.addLayer(new DesertWolfCollarLayer(this));
    }

    @Override
    protected float handleRotationFloat(DesertWolfEntity desertWolf, float rotation) {
        return desertWolf.getTailRotation();
    }

    @Override
    public void doRender(@Nonnull DesertWolfEntity desertWolf, double x, double y, double z, float entityYaw, float partialTicks) {
        if (desertWolf.isWolfWet()) {
            GlStateManager.pushMatrix();
            float f = desertWolf.getBrightness() * desertWolf.getShadingWhileWet(partialTicks);
            GlStateManager.color3f(f, f, f);
            GlStateManager.popMatrix();
        }
        super.doRender(desertWolf, x, y, z, entityYaw, partialTicks);
    }

    @Override
    protected ResourceLocation getEntityTexture(@Nonnull DesertWolfEntity desertWolf) {
        String textureName = desertWolf.getTexture();

        ResourceLocation location = CACHE.get(textureName);
        if (location == null) {
            location = new ResourceLocation(textureName);
            String[] texturePath = new String[3];
            texturePath[0] = desertWolf.isAngry() ? ANGRY_DESERT_WOLF_TEXTURES.toString() : TAMED_DESERT_WOLF_TEXTURES.toString();

            ItemStack armor = desertWolf.getArmor();
            if (!armor.isEmpty()) {
                DesertWolfEntity.ArmorType armorType = DesertWolfEntity.ArmorType.getByItemStack(armor);
                texturePath[1] = armorType.getTextureName();
            }

            if (desertWolf.isSaddled()) {
                texturePath[2] = SADDLE_DESERT_WOLF_TEXTURE.toString();
            }
            Minecraft.getInstance().getTextureManager().loadTexture(location, new LayeredTexture(texturePath));
            CACHE.put(textureName, location);
        }
        return location;
    }

    @Override
    protected void preRenderCallback(DesertWolfEntity desertWolf, float partialTickTime) {
        if (desertWolf.isAlpha()) {
            float scale = 1.5F;
            GlStateManager.scalef(scale, scale, scale);
        }
    }
}