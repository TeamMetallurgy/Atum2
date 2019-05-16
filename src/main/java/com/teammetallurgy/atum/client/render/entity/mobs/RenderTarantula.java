package com.teammetallurgy.atum.client.render.entity.mobs;

import com.teammetallurgy.atum.entity.animal.EntityTarantula;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.client.model.ModelSpider;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@SideOnly(Side.CLIENT)
public class RenderTarantula extends RenderLiving<EntityTarantula> {
    private static final ResourceLocation TARANTULA_TEXTURE = new ResourceLocation(Constants.MOD_ID, "textures/entity/tarantula.png");

    public RenderTarantula(RenderManager manager) {
        super(manager, new ModelSpider(), 0.6F);
    }

    @Override
    protected float getDeathMaxRotation(EntityTarantula tarantula)
    {
        return 180.0F;
    }

    @Override
    protected void preRenderCallback(EntityTarantula tarantula, float partialTickTime) {
        GlStateManager.scale(0.6F, 0.6F, 0.6F);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(@Nonnull EntityTarantula tarantula) {
        return TARANTULA_TEXTURE;
    }
}