package com.teammetallurgy.atum.client.render.entity.arrow;

import com.teammetallurgy.atum.entity.arrow.EntityNutsCall;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;

@SideOnly(Side.CLIENT)
public class RenderNutsCall extends Render<EntityNutsCall> {

    public RenderNutsCall(RenderManager renderManager) {
        super(renderManager);
    }

    @Override
    public void doRender(@Nonnull EntityNutsCall nutsCall, double x, double y, double z, float entityYaw, float partialTicks) { //TODO
        this.bindEntityTexture(nutsCall);
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x, (float) y, (float) z);
        GlStateManager.rotate(nutsCall.prevRotationYaw + (nutsCall.rotationYaw - nutsCall.prevRotationYaw) * partialTicks - 90.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(nutsCall.prevRotationPitch + (nutsCall.rotationPitch - nutsCall.prevRotationPitch) * partialTicks, 0.0F, 0.0F, 1.0F);
        float f = 0.15625F;
        GlStateManager.enableRescaleNormal();
        float f1 = nutsCall.arrowShake - partialTicks;

        if (f1 > 0.0F) {
            float f2 = -MathHelper.sin(f1 * 3.0F) * f1;
            GL11.glRotatef(f2, 0.0F, 0.0F, 1.0F);
        }

        GlStateManager.scale(2F, 1.5F, 1.5F);
        GlStateManager.translate(-0.85F, 0.0F, 0.0F);
        GL11.glNormal3f(f, 0.0F, 0.0F);

        Minecraft.getMinecraft().getRenderItem().renderItem(new ItemStack(AtumItems.NUTS_CALL), ItemCameraTransforms.TransformType.GROUND); //TODO Fix rotation etc.

        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
    }

    @Override
    protected ResourceLocation getEntityTexture(@Nonnull EntityNutsCall nutsCall) {
        return new ResourceLocation(Constants.MOD_ID, "textures/arrow/nutscall.png");
    }
}