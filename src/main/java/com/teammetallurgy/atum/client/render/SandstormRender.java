package com.teammetallurgy.atum.client.render;

import com.teammetallurgy.atum.init.AtumBiomes;
import com.teammetallurgy.atum.items.artifacts.atum.ItemEyesOfAtum;
import com.teammetallurgy.atum.utils.AtumConfig;
import com.teammetallurgy.atum.utils.Constants;
import com.teammetallurgy.atum.world.WorldProviderAtum;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.IRenderHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SandstormRender extends IRenderHandler {
    private static final ResourceLocation SAND_BLUR_TEX_PATH = new ResourceLocation(Constants.MOD_ID, "textures/hud/sandstormwip.png");
    private static float intensity = 1;

    @Override
    @SideOnly(Side.CLIENT)
    public void render(float partialTicks, WorldClient world, Minecraft mc) {
        float baseDarkness = AtumConfig.SAND_DARKNESS;
        float baseAlpha = AtumConfig.SAND_ALPHA;
        float eyesOfAtumAlpha = AtumConfig.SAND_EYES_ALPHA;

        if (Minecraft.getMinecraft().player.dimension == AtumConfig.DIMENSION_ID) {
            WorldProviderAtum provider = (WorldProviderAtum) Minecraft.getMinecraft().player.world.provider;
            float stormStrength = provider.stormStrength;

            if (stormStrength < 0.0001F) {
                return;
            }

            float light = Minecraft.getMinecraft().player.world.getSunBrightness(partialTicks);

            GlStateManager.pushMatrix();
            GlStateManager.pushAttrib();
            ScaledResolution scaledRes = new ScaledResolution(Minecraft.getMinecraft());
            Minecraft.getMinecraft().entityRenderer.setupOverlayRendering();
            GlStateManager.enableBlend();
            GlStateManager.disableDepth();
            GlStateManager.depthMask(false);
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.disableAlpha();
            Minecraft.getMinecraft().getTextureManager().bindTexture(SAND_BLUR_TEX_PATH);

            EntityPlayerSP player = Minecraft.getMinecraft().player;
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);

            BlockPos playerPos = new BlockPos(player.posX, player.posY, player.posZ);
            boolean sky = player.world.canBlockSeeSky(playerPos);
            if (!sky || player.world.getBiome(playerPos) == AtumBiomes.OASIS) {
                intensity -= 0.001f * partialTicks;
                intensity = Math.max(0, intensity);
            } else {
                intensity += 0.01f * partialTicks;
                intensity = Math.min(stormStrength, intensity);
            }

            //for (int i : layers) {
            int i = 1;
                float scale = 0.2f / (float) i;
                float alpha = (float) Math.pow(intensity - baseAlpha, i) * intensity;

                // Make it easier to see
                ItemStack helmet = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
                if (helmet.getItem() instanceof ItemEyesOfAtum) {
                    alpha *= eyesOfAtumAlpha;
                }

                GlStateManager.color(baseDarkness * light, baseDarkness * light, baseDarkness * light, alpha);
                float scaleX = 0.01f * scaledRes.getScaledHeight() * scale * scaledRes.getScaleFactor();
                float scaleY = 0.01f * scaledRes.getScaledWidth() * scale * scaledRes.getScaleFactor();
                float speed = 500f - i * 15;
                float movement = -(System.currentTimeMillis() % (int) speed) / speed;
                float yaw = 0.25f * (Minecraft.getMinecraft().player.rotationYaw % 360 / 360f) / scale;
                float pitch = 0.5f * (Minecraft.getMinecraft().player.rotationPitch % 360 / 360f) / scale;

                bufferbuilder.pos(0.0D, (double) scaledRes.getScaledHeight(), 90.0D).tex(movement + yaw, 1.0D / scaleY + pitch).endVertex();
                bufferbuilder.pos((double) scaledRes.getScaledWidth(), (double) scaledRes.getScaledHeight(), 90.0D).tex(1.0D / scaleX + movement + yaw, 1.0D / scaleY + pitch).endVertex();
                bufferbuilder.pos((double) scaledRes.getScaledWidth(), 0.0D, 90.0D).tex(1.0D / scaleX + movement + yaw, 0.0D + pitch).endVertex();
                bufferbuilder.pos(0.0D, 0.0D, 90.0D).tex(movement + yaw, 0.0D + pitch).endVertex();
            //}
            tessellator.draw();

            GlStateManager.depthMask(true);
            GlStateManager.enableDepth();
            GlStateManager.enableAlpha();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.popAttrib();
            GlStateManager.popMatrix();
        }
    }
}