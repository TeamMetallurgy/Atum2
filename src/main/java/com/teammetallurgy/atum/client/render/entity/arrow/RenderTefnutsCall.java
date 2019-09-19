package com.teammetallurgy.atum.client.render.entity.arrow;

import com.teammetallurgy.atum.entity.projectile.arrow.EntityTefnutsCall;
import com.teammetallurgy.atum.init.AtumItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class RenderTefnutsCall extends Render<EntityTefnutsCall> {

    public RenderTefnutsCall(RenderManager renderManager) {
        super(renderManager);
    }

    @Override
    public void doRender(@Nonnull EntityTefnutsCall tefnutsCall, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x, (float) y, (float) z);
        GlStateManager.rotate((tefnutsCall.prevRotationYaw + (tefnutsCall.rotationYaw - tefnutsCall.prevRotationYaw) * partialTicks) - 90.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate((tefnutsCall.prevRotationPitch + (tefnutsCall.rotationPitch - tefnutsCall.prevRotationPitch) * partialTicks) -45, 0.0F, 0.0F, 1.0F);
        GlStateManager.enableRescaleNormal();
        float f9 = (float) tefnutsCall.arrowShake - partialTicks;

        if (f9 > 0.0F) {
            float f10 = -MathHelper.sin(f9 * 3.0F) * f9;
            GlStateManager.rotate(f10, 0.0F, 0.0F, 1.0F);
        }

        GlStateManager.scale(2.0F, 2.0F, 2.0F);

        if (this.renderOutlines) {
            GlStateManager.enableColorMaterial();
            GlStateManager.enableOutlineMode(this.getTeamColor(tefnutsCall));
        }

        Minecraft.getInstance().getRenderItem().renderItem(new ItemStack(AtumItems.TEFNUTS_CALL), ItemCameraTransforms.TransformType.GROUND);

        if (this.renderOutlines) {
            GlStateManager.disableOutlineMode();
            GlStateManager.disableColorMaterial();
        }

        GlStateManager.disableRescaleNormal();
        GlStateManager.enableLighting();
        GlStateManager.popMatrix();
        super.doRender(tefnutsCall, x, y, z, entityYaw, partialTicks);
    }

    @Override
    protected ResourceLocation getEntityTexture(@Nonnull EntityTefnutsCall tefnutsCall) {
        return TextureMap.LOCATION_BLOCKS_TEXTURE;
    }
}