package com.teammetallurgy.atum.client.render.entity;

import com.mojang.blaze3d.platform.GlStateManager;
import com.teammetallurgy.atum.entity.HeartOfRaEntity;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.model.EnderCrystalModel;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class RenderHeartOfRa extends EntityRenderer<HeartOfRaEntity> {
    private static final ResourceLocation HEART_OF_RA_TEXTURE = new ResourceLocation(Constants.MOD_ID, "textures/entity/heart_of_ra.png");
    private final EntityModel modelCrystalNoBase = new EnderCrystalModel(0.0F, false);

    public RenderHeartOfRa(EntityRendererManager renderManagerIn) {
        super(renderManagerIn);
        this.shadowSize = 0.5F;
    }

    @Override
    public void doRender(@Nonnull HeartOfRaEntity entity, double x, double y, double z, float entityYaw, float partialTicks) {
        float rotationTicks = (float) entity.innerRotation + partialTicks;
        GlStateManager.pushMatrix();
        GlStateManager.translatef((float) x, (float) y + 1.0F, (float) z);
        this.bindTexture(HEART_OF_RA_TEXTURE);
        float rotation = MathHelper.sin(rotationTicks * 0.2F) / 2.0F + 0.5F;
        rotation = rotation * rotation + rotation;

        if (this.renderOutlines) {
            GlStateManager.enableColorMaterial();
            GlStateManager.setupSolidRenderingTextureCombine(this.getTeamColor(entity));
        }

        this.modelCrystalNoBase.render(entity, 0.0F, rotationTicks * 3.0F, rotation * 0.2F, 0.0F, 0.0F, 0.0625F);

        if (this.renderOutlines) {
            GlStateManager.tearDownSolidRenderingTextureCombine();
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