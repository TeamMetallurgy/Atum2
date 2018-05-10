package com.teammetallurgy.atum.client.render.entity;

import com.teammetallurgy.atum.entity.EntityWraith;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

@SideOnly(Side.CLIENT)
public class RenderGhost extends RenderBiped<EntityWraith> {

    public RenderGhost(RenderManager renderManager, ModelBiped model, float shadowSize) {
        super(renderManager, model, shadowSize);
        this.mainModel = model;
        this.shadowSize = shadowSize;
    }

    @Override
    protected ResourceLocation getEntityTexture(@Nonnull EntityWraith wraith) {
        return new ResourceLocation(Constants.MOD_ID, "textures/entities/wraith.png");
    }
}