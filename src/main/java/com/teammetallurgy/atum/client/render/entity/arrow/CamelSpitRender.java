package com.teammetallurgy.atum.client.render.entity.arrow;

import com.mojang.blaze3d.platform.GlStateManager;
import com.teammetallurgy.atum.entity.projectile.CamelSpitEntity;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.model.LlamaSpitModel;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class CamelSpitRender extends EntityRenderer<CamelSpitEntity> {
    private static final ResourceLocation LLAMA_SPIT_TEXTURE = new ResourceLocation("textures/entity/llama/spit.png");
    private final LlamaSpitModel model = new LlamaSpitModel();

    public CamelSpitRender(EntityRendererManager manager) {
        super(manager);
    }

    @Override
    public void doRender(CamelSpitEntity entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.translated((float) x, (float) y + 0.15F, (float) z);
        GlStateManager.rotatef(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks - 90.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotatef(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks, 0.0F, 0.0F, 1.0F);
        this.bindEntityTexture(entity);

        if (this.renderOutlines) {
            GlStateManager.enableColorMaterial();
            GlStateManager.setupSolidRenderingTextureCombine(this.getTeamColor(entity));
        }

        this.model.render(entity, partialTicks, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);

        if (this.renderOutlines) {
            GlStateManager.tearDownSolidRenderingTextureCombine();
            GlStateManager.disableColorMaterial();
        }

        GlStateManager.popMatrix();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    @Override
    protected ResourceLocation getEntityTexture(@Nonnull CamelSpitEntity entity) {
        return LLAMA_SPIT_TEXTURE;
    }
}