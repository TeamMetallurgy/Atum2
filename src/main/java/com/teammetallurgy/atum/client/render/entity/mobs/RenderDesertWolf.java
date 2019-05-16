package com.teammetallurgy.atum.client.render.entity.mobs;

import com.google.common.collect.Maps;
import com.teammetallurgy.atum.client.render.entity.layer.LayerDesertWolfCollar;
import com.teammetallurgy.atum.client.render.entity.layer.LayerWolfSaddle;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.Map;

@SideOnly(Side.CLIENT)
public class RenderDesertWolf extends RenderLiving<EntityDesertWolf> {
    private static final Map<String, ResourceLocation> CACHE = Maps.newHashMap();

    private static final ResourceLocation TAMED_DESERT_WOLF_TEXTURES = new ResourceLocation(Constants.MOD_ID, "textures/entity/desert_wolf_tame.png");
    private static final ResourceLocation ANGRY_DESERT_WOLF_TEXTURES = new ResourceLocation(Constants.MOD_ID, "textures/entity/desert_wolf_angry.png");

    public RenderDesertWolf(RenderManager renderManager, ModelBase modelBase, float shadowSize) {
        super(renderManager, modelBase, shadowSize);
        this.addLayer(new LayerWolfSaddle(this));
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
        ItemStack wolfArmor = desertWolf.getArmor();
        if (desertWolf.isTamed()) {
            if (desertWolf.isArmor(wolfArmor)) {
                EntityDesertWolf.ArmorType armorType = EntityDesertWolf.ArmorType.getByItemStack(wolfArmor);
                if(!CACHE.containsKey(armorType.getTextureName())) {
                    ResourceLocation armorLocation = new ResourceLocation(armorType.getTextureName());
                    Minecraft.getMinecraft().getTextureManager().loadTexture(armorLocation, new LayeredTexture(armorType.getTextureName()));
                    CACHE.put(armorType.getTextureName(), armorLocation);
                }
                return CACHE.get(armorType.getTextureName());
            }

            if(!CACHE.containsKey(TAMED_DESERT_WOLF_TEXTURES.toString())) {
                Minecraft.getMinecraft().getTextureManager().loadTexture(TAMED_DESERT_WOLF_TEXTURES, new LayeredTexture(TAMED_DESERT_WOLF_TEXTURES.toString()));
                CACHE.put(TAMED_DESERT_WOLF_TEXTURES.toString(), TAMED_DESERT_WOLF_TEXTURES);
            }
            return TAMED_DESERT_WOLF_TEXTURES;
        } else {
            return ANGRY_DESERT_WOLF_TEXTURES;
        }
    }

    @Override
    protected void preRenderCallback(EntityDesertWolf desertWolf, float partialTickTime) {
        if (desertWolf.isAlpha()) {
            float scale = 1.5F;
            GlStateManager.scale(scale, scale, scale);
        }
    }
}