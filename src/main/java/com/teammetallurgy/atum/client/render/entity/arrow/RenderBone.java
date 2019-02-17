package com.teammetallurgy.atum.client.render.entity.arrow;

import com.teammetallurgy.atum.entity.projectile.EntityBone;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.utils.Constants;
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
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(modid = Constants.MOD_ID, value = Side.CLIENT)
public class RenderBone extends Render<EntityBone> {
    private static TextureAtlasSprite sprite;
    private float scale;

    public RenderBone(RenderManager renderManager, float scale) {
        super(renderManager);
        this.scale = scale;
    }

    @Override
    public void doRender(@Nonnull EntityBone entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.pushMatrix();
        this.bindEntityTexture(entity);
        GlStateManager.translate((float) x, (float) y, (float) z);
        GlStateManager.enableRescaleNormal();
        GlStateManager.scale(this.scale, this.scale, this.scale);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        float f = sprite.getMinU();
        float f1 = sprite.getMaxU();
        float f2 = sprite.getMinV();
        float f3 = sprite.getMaxV();
        GlStateManager.rotate(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate((float) (this.renderManager.options.thirdPersonView == 2 ? -1 : 1) * -this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);

        if (this.renderOutlines) {
            GlStateManager.enableColorMaterial();
            GlStateManager.enableOutlineMode(this.getTeamColor(entity));
        }

        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_NORMAL);
        bufferbuilder.pos(-0.5D, -0.25D, 0.0D).tex((double) f, (double) f3).normal(0.0F, 1.0F, 0.0F).endVertex();
        bufferbuilder.pos(0.5D, -0.25D, 0.0D).tex((double) f1, (double) f3).normal(0.0F, 1.0F, 0.0F).endVertex();
        bufferbuilder.pos(0.5D, 0.75D, 0.0D).tex((double) f1, (double) f2).normal(0.0F, 1.0F, 0.0F).endVertex();
        bufferbuilder.pos(-0.5D, 0.75D, 0.0D).tex((double) f, (double) f2).normal(0.0F, 1.0F, 0.0F).endVertex();
        tessellator.draw();

        if (this.renderOutlines) {
            GlStateManager.disableOutlineMode();
            GlStateManager.disableColorMaterial();
        }

        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    @Override
    protected ResourceLocation getEntityTexture(@Nonnull EntityBone entity) {
        return TextureMap.LOCATION_BLOCKS_TEXTURE;
    }

    @SubscribeEvent
    public static void onTextureStitch(TextureStitchEvent event) {
        ResourceLocation bonestormProjectile = new ResourceLocation(Constants.MOD_ID, "arrow/bonestorm_projectile");
        event.getMap().registerSprite(bonestormProjectile);
        sprite = event.getMap().getAtlasSprite(bonestormProjectile.toString());
    }
}