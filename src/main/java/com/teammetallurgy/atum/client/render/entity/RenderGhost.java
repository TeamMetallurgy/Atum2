package com.teammetallurgy.atum.client.render.entity;

import com.teammetallurgy.atum.entity.EntityWraith;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderGhost extends RenderLivingBase<EntityWraith> {

    public RenderGhost(RenderManager renderManager, ModelBase modelBase, float shadowSize) {
        super(renderManager, modelBase, shadowSize);
        this.mainModel = modelBase;
        this.shadowSize = shadowSize;
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityWraith EntityGhost) {
        return new ResourceLocation("atum", "textures/entities/wraith.png");
    }
}