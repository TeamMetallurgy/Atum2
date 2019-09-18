package com.teammetallurgy.atum.client.render.entity.mobs;

import com.google.common.collect.Maps;
import com.teammetallurgy.atum.client.render.entity.layer.LayerDesertWolfCollar;
import com.teammetallurgy.atum.entity.animal.EntityDesertWolf;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.LayeredTexture;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class RenderDesertWolf extends RenderLiving<EntityDesertWolf> {
    private static final Map<String, ResourceLocation> CACHE = Maps.newHashMap();
    private static final ResourceLocation TAMED_DESERT_WOLF_TEXTURES = new ResourceLocation(Constants.MOD_ID, "textures/entity/desert_wolf_tame.png");
    private static final ResourceLocation ANGRY_DESERT_WOLF_TEXTURES = new ResourceLocation(Constants.MOD_ID, "textures/entity/desert_wolf_angry.png");
    private static final ResourceLocation SADDLE_DESERT_WOLF_TEXTURE = new ResourceLocation(Constants.MOD_ID, "textures/entity/desert_wolf_saddle.png");

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
        String textureName = desertWolf.getTexture();

        ResourceLocation location = CACHE.get(textureName);
        if (location == null) {
            location = new ResourceLocation(textureName);
            String[] texturePath = new String[3];
            texturePath[0] = desertWolf.isAngry() ? ANGRY_DESERT_WOLF_TEXTURES.toString() : TAMED_DESERT_WOLF_TEXTURES.toString();

            ItemStack armor = desertWolf.getArmor();
            if (!armor.isEmpty()) {
                EntityDesertWolf.ArmorType armorType = EntityDesertWolf.ArmorType.getByItemStack(armor);
                texturePath[1] = armorType.getTextureName();
            }

            if (desertWolf.isSaddled()) {
                texturePath[2] = SADDLE_DESERT_WOLF_TEXTURE.toString();
            }
            Minecraft.getMinecraft().getTextureManager().loadTexture(location, new LayeredTexture(texturePath));
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