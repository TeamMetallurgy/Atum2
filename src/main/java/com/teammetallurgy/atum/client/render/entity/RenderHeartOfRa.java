package com.teammetallurgy.atum.client.render.entity;

import com.teammetallurgy.atum.entity.HeartOfRaEntity;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelEnderCrystal;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class RenderHeartOfRa extends Render<HeartOfRaEntity> {
    private static final ResourceLocation HEART_OF_RA_TEXTURE = new ResourceLocation(Constants.MOD_ID, "textures/entity/heart_of_ra.png");
    private final ModelBase modelCrystalNoBase = new ModelEnderCrystal(0.0F, false);

    public RenderHeartOfRa(RenderManager renderManagerIn) {
        super(renderManagerIn);
        this.shadowSize = 0.5F;
    }

    @Override
    public void doRender(@Nonnull HeartOfRaEntity entity, double x, double y, double z, float entityYaw, float partialTicks) {
        float rotationTicks = (float) entity.innerRotation + partialTicks;
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x, (float) y + 1.0F, (float) z);
        this.bindTexture(HEART_OF_RA_TEXTURE);
        float rotation = MathHelper.sin(rotationTicks * 0.2F) / 2.0F + 0.5F;
        rotation = rotation * rotation + rotation;

        if (this.renderOutlines) {
            GlStateManager.enableColorMaterial();
            GlStateManager.enableOutlineMode(this.getTeamColor(entity));
        }

        this.modelCrystalNoBase.render(entity, 0.0F, rotationTicks * 3.0F, rotation * 0.2F, 0.0F, 0.0F, 0.0625F);

        if (this.renderOutlines) {
            GlStateManager.disableOutlineMode();
            GlStateManager.disableColorMaterial();
        }
        GlStateManager.popMatrix();

        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    @Override
    protected ResourceLocation getEntityTexture(@Nonnull HeartOfRaEntity entity) {
        return HEART_OF_RA_TEXTURE;
    }
}