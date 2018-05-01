package com.teammetallurgy.atum.client.render.entity;

import com.teammetallurgy.atum.client.render.entity.layer.LayerDesertWolfCollar;
import com.teammetallurgy.atum.entity.EntityDesertWolf;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderDesertWolf extends RenderLiving<EntityDesertWolf> {
    private static final ResourceLocation tamedDesertWolfTextures = new ResourceLocation(Constants.MOD_ID + ":" + "textures/entities/desert_wolf_tame.png");
    private static final ResourceLocation angryDesertWolfTextures = new ResourceLocation(Constants.MOD_ID + ":" + "textures/entities/desert_wolf_angry.png");

    public RenderDesertWolf(RenderManager renderManager, ModelBase modelBase, float shadowSize) {
        super(renderManager, modelBase, shadowSize);
        this.addLayer(new LayerDesertWolfCollar(this));
    }

    @Override
    protected float handleRotationFloat(EntityDesertWolf entityDesertWolf, float rotation) {
        return entityDesertWolf.getTailRotation();
    }

    @Override
    public void doRender(EntityDesertWolf entityDesertWolf, double x, double y, double z, float entityYaw, float partialTicks) {
        if (entityDesertWolf.isWolfWet()) {
            float f = entityDesertWolf.getBrightness() * entityDesertWolf.getShadingWhileWet(partialTicks);
            GlStateManager.color(f, f, f);
        }

        super.doRender(entityDesertWolf, x, y, z, entityYaw, partialTicks);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityDesertWolf entityDesertWolf) {
        return entityDesertWolf.isAngry() ? angryDesertWolfTextures : tamedDesertWolfTextures;
    }
}