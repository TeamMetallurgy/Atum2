package com.teammetallurgy.atum.client.render.entity.arrow;

import com.teammetallurgy.atum.entity.projectile.EntityBone;
import com.teammetallurgy.atum.init.AtumItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

@SideOnly(Side.CLIENT)
public class RenderBone extends Render<EntityBone> {
    private float scale;

    public RenderBone(RenderManager renderManager, float scale) {
        super(renderManager);
        this.scale = scale;
    }

    @Override
    public void doRender(@Nonnull EntityBone entityBone, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.pushMatrix();
        this.bindEntityTexture(entityBone);
        GlStateManager.translate((float) x, (float) y, (float) z);
        GlStateManager.enableRescaleNormal();
        GlStateManager.scale(this.scale, this.scale, this.scale);
        TextureAtlasSprite dustyBoneSprite = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getParticleIcon(AtumItems.DUSTY_BONE);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        float f = dustyBoneSprite.getMinU();
        float f1 = dustyBoneSprite.getMaxU();
        float f2 = dustyBoneSprite.getMinV();
        float f3 = dustyBoneSprite.getMaxV();
        GlStateManager.rotate(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate((float) (this.renderManager.options.thirdPersonView == 2 ? -1 : 1) * -this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);

        if (this.renderOutlines) {
            GlStateManager.enableColorMaterial();
            GlStateManager.enableOutlineMode(this.getTeamColor(entityBone));
        }

        bufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX_NORMAL);
        bufferBuilder.pos(-0.5D, -0.25D, 0.0D).tex((double) f, (double) f3).normal(0.0F, 1.0F, 0.0F).endVertex();
        bufferBuilder.pos(0.5D, -0.25D, 0.0D).tex((double) f1, (double) f3).normal(0.0F, 1.0F, 0.0F).endVertex();
        bufferBuilder.pos(0.5D, 0.75D, 0.0D).tex((double) f1, (double) f2).normal(0.0F, 1.0F, 0.0F).endVertex();
        bufferBuilder.pos(-0.5D, 0.75D, 0.0D).tex((double) f, (double) f2).normal(0.0F, 1.0F, 0.0F).endVertex();
        tessellator.draw();

        if (this.renderOutlines) {
            GlStateManager.disableOutlineMode();
            GlStateManager.disableColorMaterial();
        }

        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        super.doRender(entityBone, x, y, z, entityYaw, partialTicks);
    }

    @Override
    protected ResourceLocation getEntityTexture(@Nonnull EntityBone entityBone) {
        return TextureMap.LOCATION_BLOCKS_TEXTURE;
    }
}