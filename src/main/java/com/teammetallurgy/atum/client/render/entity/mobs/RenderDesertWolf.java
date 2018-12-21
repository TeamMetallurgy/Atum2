package com.teammetallurgy.atum.client.render.entity.mobs;

import com.teammetallurgy.atum.client.render.entity.layer.LayerDesertWolfCollar;
import com.teammetallurgy.atum.entity.animal.EntityDesertWolf;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

@SideOnly(Side.CLIENT)
public class RenderDesertWolf extends RenderLiving<EntityDesertWolf> {
    private static final ResourceLocation TAMED_DESERT_WOLF_TEXTURES = new ResourceLocation(Constants.MOD_ID, "textures/entities/desert_wolf_tame.png");
    private static final ResourceLocation ANGRY_DESERT_WOLF_TEXTURES = new ResourceLocation(Constants.MOD_ID, "textures/entities/desert_wolf_angry.png");

    public RenderDesertWolf(RenderManager renderManager, ModelBase modelBase, float shadowSize) {
        super(renderManager, modelBase, shadowSize);
        this.addLayer(new LayerDesertWolfCollar(this));
    }

    @Override
    protected float handleRotationFloat(EntityDesertWolf entityDesertWolf, float rotation) {
        return entityDesertWolf.getTailRotation();
    }

    @Override
    public void doRender(@Nonnull EntityDesertWolf entityDesertWolf, double x, double y, double z, float entityYaw, float partialTicks) {
        if (entityDesertWolf.isWolfWet()) {
            float f = entityDesertWolf.getBrightness() * entityDesertWolf.getShadingWhileWet(partialTicks);
            GlStateManager.color(f, f, f);
        }
        super.doRender(entityDesertWolf, x, y, z, entityYaw, partialTicks);
    }

    @Override
    protected ResourceLocation getEntityTexture(@Nonnull EntityDesertWolf entityDesertWolf) {
        return entityDesertWolf.isTamed() ? TAMED_DESERT_WOLF_TEXTURES : ANGRY_DESERT_WOLF_TEXTURES;
    }

    @Override
    protected void preRenderCallback(EntityDesertWolf entityDesertWolf, float partialTickTime)
    {
    	if(entityDesertWolf.isAlpha())
    	{
    		float scale = 1.5f;
        	GlStateManager.scale(scale, scale, scale);
    	}
    }
}