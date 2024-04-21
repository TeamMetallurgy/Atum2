package com.teammetallurgy.atum.client;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.teammetallurgy.atum.Atum;
import net.minecraft.client.CloudStatus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class AtumCloudRenderer { //Same as vanilla, just changes clouds texture
    private static final ResourceLocation CLOUDS_LOCATION = new ResourceLocation(Atum.MOD_ID, "textures/environment/clouds.png");
    @Nullable
    private static VertexBuffer cloudBuffer;
    private static int prevCloudX = Integer.MIN_VALUE;
    private static int prevCloudY = Integer.MIN_VALUE;
    private static int prevCloudZ = Integer.MIN_VALUE;
    private static Vec3 prevCloudColor = Vec3.ZERO;
    @Nullable
    private static CloudStatus prevCloudsType;

    //Copied from LevelRenderer
    public static boolean renderClouds(@Nonnull ClientLevel level, int ticks, float partialTick, @Nonnull PoseStack poseStack, double camX, double camY, double camZ, @Nonnull Matrix4f projectionMatrix) {
        float f = level.effects().getCloudHeight();
        Minecraft minecraft = Minecraft.getInstance();
        LevelRenderer levelRenderer = minecraft.levelRenderer; //Added + calls to levelRenderer.generateClouds
        if (!Float.isNaN(f)) {
            RenderSystem.disableCull();
            RenderSystem.enableBlend();
            RenderSystem.enableDepthTest();
            RenderSystem.blendFuncSeparate(
                    GlStateManager.SourceFactor.SRC_ALPHA,
                    GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                    GlStateManager.SourceFactor.ONE,
                    GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA
            );
            RenderSystem.depthMask(true);
            float f1 = 12.0F;
            float f2 = 4.0F;
            double d0 = 2.0E-4;
            double d1 = ((float) ticks + partialTick) * 0.03F;
            double d2 = (camX + d1) / 12.0;
            double d3 = f - (float) camY + 0.33F;
            double d4 = camZ / 12.0 + 0.33F;
            d2 -= Mth.floor(d2 / 2048.0) * 2048;
            d4 -= Mth.floor(d4 / 2048.0) * 2048;
            float f3 = (float) (d2 - (double) Mth.floor(d2));
            float f4 = (float) (d3 / 4.0 - (double) Mth.floor(d3 / 4.0)) * 4.0F;
            float f5 = (float) (d4 - (double) Mth.floor(d4));
            Vec3 vec3 = level.getCloudColor(partialTick);
            int i = (int) Math.floor(d2);
            int j = (int) Math.floor(d3 / 4.0);
            int k = (int) Math.floor(d4);
            if (i != prevCloudX
                    || j != prevCloudY
                    || k != prevCloudZ
                    || minecraft.options.getCloudsType() != prevCloudsType
                    || prevCloudColor.distanceToSqr(vec3) > 2.0E-4) {
                prevCloudX = i;
                prevCloudY = j;
                prevCloudZ = k;
                prevCloudColor = vec3;
                prevCloudsType = minecraft.options.getCloudsType();
                levelRenderer.generateClouds = true;
            }

            if (levelRenderer.generateClouds) {
                levelRenderer.generateClouds = false;
                BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
                if (cloudBuffer != null) {
                    cloudBuffer.close();
                }

                cloudBuffer = new VertexBuffer(VertexBuffer.Usage.STATIC);
                BufferBuilder.RenderedBuffer bufferbuilder$renderedbuffer = buildClouds(bufferbuilder, d2, d3, d4, vec3);
                cloudBuffer.bind();
                cloudBuffer.upload(bufferbuilder$renderedbuffer);
                VertexBuffer.unbind();
            }

            RenderSystem.setShader(GameRenderer::getPositionTexColorNormalShader);
            RenderSystem.setShaderTexture(0, CLOUDS_LOCATION);
            FogRenderer.levelFogColor();
            poseStack.pushPose();
            poseStack.scale(12.0F, 1.0F, 12.0F);
            poseStack.translate(-f3, f4, -f5);
            if (cloudBuffer != null) {
                cloudBuffer.bind();
                int l = prevCloudsType == CloudStatus.FANCY ? 0 : 1;

                for (int i1 = l; i1 < 2; ++i1) {
                    if (i1 == 0) {
                        RenderSystem.colorMask(false, false, false, false);
                    } else {
                        RenderSystem.colorMask(true, true, true, true);
                    }

                    ShaderInstance shaderinstance = RenderSystem.getShader();
                    cloudBuffer.drawWithShader(poseStack.last().pose(), projectionMatrix, shaderinstance);
                }

                VertexBuffer.unbind();
            }

            poseStack.popPose();
            RenderSystem.enableCull();
            RenderSystem.disableBlend();
            RenderSystem.defaultBlendFunc();
        }
        return true;
    }

    //Copied from LevelRenderer
    private static BufferBuilder.RenderedBuffer buildClouds(BufferBuilder p_234262_, double p_234263_, double p_234264_, double p_234265_, Vec3 p_234266_) {
        float f = 4.0F;
        float f1 = 0.00390625F;
        int i = 8;
        int j = 4;
        float f2 = 9.765625E-4F;
        float f3 = (float)Mth.floor(p_234263_) * 0.00390625F;
        float f4 = (float)Mth.floor(p_234265_) * 0.00390625F;
        float f5 = (float)p_234266_.x;
        float f6 = (float)p_234266_.y;
        float f7 = (float)p_234266_.z;
        float f8 = f5 * 0.9F;
        float f9 = f6 * 0.9F;
        float f10 = f7 * 0.9F;
        float f11 = f5 * 0.7F;
        float f12 = f6 * 0.7F;
        float f13 = f7 * 0.7F;
        float f14 = f5 * 0.8F;
        float f15 = f6 * 0.8F;
        float f16 = f7 * 0.8F;
        RenderSystem.setShader(GameRenderer::getPositionTexColorNormalShader);
        p_234262_.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR_NORMAL);
        float f17 = (float)Math.floor(p_234264_ / 4.0) * 4.0F;
        if (prevCloudsType == CloudStatus.FANCY) {
            for(int k = -3; k <= 4; ++k) {
                for(int l = -3; l <= 4; ++l) {
                    float f18 = (float)(k * 8);
                    float f19 = (float)(l * 8);
                    if (f17 > -5.0F) {
                        p_234262_.vertex((double)(f18 + 0.0F), (double)(f17 + 0.0F), (double)(f19 + 8.0F))
                                .uv((f18 + 0.0F) * 0.00390625F + f3, (f19 + 8.0F) * 0.00390625F + f4)
                                .color(f11, f12, f13, 0.8F)
                                .normal(0.0F, -1.0F, 0.0F)
                                .endVertex();
                        p_234262_.vertex((double)(f18 + 8.0F), (double)(f17 + 0.0F), (double)(f19 + 8.0F))
                                .uv((f18 + 8.0F) * 0.00390625F + f3, (f19 + 8.0F) * 0.00390625F + f4)
                                .color(f11, f12, f13, 0.8F)
                                .normal(0.0F, -1.0F, 0.0F)
                                .endVertex();
                        p_234262_.vertex((double)(f18 + 8.0F), (double)(f17 + 0.0F), (double)(f19 + 0.0F))
                                .uv((f18 + 8.0F) * 0.00390625F + f3, (f19 + 0.0F) * 0.00390625F + f4)
                                .color(f11, f12, f13, 0.8F)
                                .normal(0.0F, -1.0F, 0.0F)
                                .endVertex();
                        p_234262_.vertex((double)(f18 + 0.0F), (double)(f17 + 0.0F), (double)(f19 + 0.0F))
                                .uv((f18 + 0.0F) * 0.00390625F + f3, (f19 + 0.0F) * 0.00390625F + f4)
                                .color(f11, f12, f13, 0.8F)
                                .normal(0.0F, -1.0F, 0.0F)
                                .endVertex();
                    }

                    if (f17 <= 5.0F) {
                        p_234262_.vertex((double)(f18 + 0.0F), (double)(f17 + 4.0F - 9.765625E-4F), (double)(f19 + 8.0F))
                                .uv((f18 + 0.0F) * 0.00390625F + f3, (f19 + 8.0F) * 0.00390625F + f4)
                                .color(f5, f6, f7, 0.8F)
                                .normal(0.0F, 1.0F, 0.0F)
                                .endVertex();
                        p_234262_.vertex((double)(f18 + 8.0F), (double)(f17 + 4.0F - 9.765625E-4F), (double)(f19 + 8.0F))
                                .uv((f18 + 8.0F) * 0.00390625F + f3, (f19 + 8.0F) * 0.00390625F + f4)
                                .color(f5, f6, f7, 0.8F)
                                .normal(0.0F, 1.0F, 0.0F)
                                .endVertex();
                        p_234262_.vertex((double)(f18 + 8.0F), (double)(f17 + 4.0F - 9.765625E-4F), (double)(f19 + 0.0F))
                                .uv((f18 + 8.0F) * 0.00390625F + f3, (f19 + 0.0F) * 0.00390625F + f4)
                                .color(f5, f6, f7, 0.8F)
                                .normal(0.0F, 1.0F, 0.0F)
                                .endVertex();
                        p_234262_.vertex((double)(f18 + 0.0F), (double)(f17 + 4.0F - 9.765625E-4F), (double)(f19 + 0.0F))
                                .uv((f18 + 0.0F) * 0.00390625F + f3, (f19 + 0.0F) * 0.00390625F + f4)
                                .color(f5, f6, f7, 0.8F)
                                .normal(0.0F, 1.0F, 0.0F)
                                .endVertex();
                    }

                    if (k > -1) {
                        for(int i1 = 0; i1 < 8; ++i1) {
                            p_234262_.vertex((double)(f18 + (float)i1 + 0.0F), (double)(f17 + 0.0F), (double)(f19 + 8.0F))
                                    .uv((f18 + (float)i1 + 0.5F) * 0.00390625F + f3, (f19 + 8.0F) * 0.00390625F + f4)
                                    .color(f8, f9, f10, 0.8F)
                                    .normal(-1.0F, 0.0F, 0.0F)
                                    .endVertex();
                            p_234262_.vertex((double)(f18 + (float)i1 + 0.0F), (double)(f17 + 4.0F), (double)(f19 + 8.0F))
                                    .uv((f18 + (float)i1 + 0.5F) * 0.00390625F + f3, (f19 + 8.0F) * 0.00390625F + f4)
                                    .color(f8, f9, f10, 0.8F)
                                    .normal(-1.0F, 0.0F, 0.0F)
                                    .endVertex();
                            p_234262_.vertex((double)(f18 + (float)i1 + 0.0F), (double)(f17 + 4.0F), (double)(f19 + 0.0F))
                                    .uv((f18 + (float)i1 + 0.5F) * 0.00390625F + f3, (f19 + 0.0F) * 0.00390625F + f4)
                                    .color(f8, f9, f10, 0.8F)
                                    .normal(-1.0F, 0.0F, 0.0F)
                                    .endVertex();
                            p_234262_.vertex((double)(f18 + (float)i1 + 0.0F), (double)(f17 + 0.0F), (double)(f19 + 0.0F))
                                    .uv((f18 + (float)i1 + 0.5F) * 0.00390625F + f3, (f19 + 0.0F) * 0.00390625F + f4)
                                    .color(f8, f9, f10, 0.8F)
                                    .normal(-1.0F, 0.0F, 0.0F)
                                    .endVertex();
                        }
                    }

                    if (k <= 1) {
                        for(int j2 = 0; j2 < 8; ++j2) {
                            p_234262_.vertex((double)(f18 + (float)j2 + 1.0F - 9.765625E-4F), (double)(f17 + 0.0F), (double)(f19 + 8.0F))
                                    .uv((f18 + (float)j2 + 0.5F) * 0.00390625F + f3, (f19 + 8.0F) * 0.00390625F + f4)
                                    .color(f8, f9, f10, 0.8F)
                                    .normal(1.0F, 0.0F, 0.0F)
                                    .endVertex();
                            p_234262_.vertex((double)(f18 + (float)j2 + 1.0F - 9.765625E-4F), (double)(f17 + 4.0F), (double)(f19 + 8.0F))
                                    .uv((f18 + (float)j2 + 0.5F) * 0.00390625F + f3, (f19 + 8.0F) * 0.00390625F + f4)
                                    .color(f8, f9, f10, 0.8F)
                                    .normal(1.0F, 0.0F, 0.0F)
                                    .endVertex();
                            p_234262_.vertex((double)(f18 + (float)j2 + 1.0F - 9.765625E-4F), (double)(f17 + 4.0F), (double)(f19 + 0.0F))
                                    .uv((f18 + (float)j2 + 0.5F) * 0.00390625F + f3, (f19 + 0.0F) * 0.00390625F + f4)
                                    .color(f8, f9, f10, 0.8F)
                                    .normal(1.0F, 0.0F, 0.0F)
                                    .endVertex();
                            p_234262_.vertex((double)(f18 + (float)j2 + 1.0F - 9.765625E-4F), (double)(f17 + 0.0F), (double)(f19 + 0.0F))
                                    .uv((f18 + (float)j2 + 0.5F) * 0.00390625F + f3, (f19 + 0.0F) * 0.00390625F + f4)
                                    .color(f8, f9, f10, 0.8F)
                                    .normal(1.0F, 0.0F, 0.0F)
                                    .endVertex();
                        }
                    }

                    if (l > -1) {
                        for(int k2 = 0; k2 < 8; ++k2) {
                            p_234262_.vertex((double)(f18 + 0.0F), (double)(f17 + 4.0F), (double)(f19 + (float)k2 + 0.0F))
                                    .uv((f18 + 0.0F) * 0.00390625F + f3, (f19 + (float)k2 + 0.5F) * 0.00390625F + f4)
                                    .color(f14, f15, f16, 0.8F)
                                    .normal(0.0F, 0.0F, -1.0F)
                                    .endVertex();
                            p_234262_.vertex((double)(f18 + 8.0F), (double)(f17 + 4.0F), (double)(f19 + (float)k2 + 0.0F))
                                    .uv((f18 + 8.0F) * 0.00390625F + f3, (f19 + (float)k2 + 0.5F) * 0.00390625F + f4)
                                    .color(f14, f15, f16, 0.8F)
                                    .normal(0.0F, 0.0F, -1.0F)
                                    .endVertex();
                            p_234262_.vertex((double)(f18 + 8.0F), (double)(f17 + 0.0F), (double)(f19 + (float)k2 + 0.0F))
                                    .uv((f18 + 8.0F) * 0.00390625F + f3, (f19 + (float)k2 + 0.5F) * 0.00390625F + f4)
                                    .color(f14, f15, f16, 0.8F)
                                    .normal(0.0F, 0.0F, -1.0F)
                                    .endVertex();
                            p_234262_.vertex((double)(f18 + 0.0F), (double)(f17 + 0.0F), (double)(f19 + (float)k2 + 0.0F))
                                    .uv((f18 + 0.0F) * 0.00390625F + f3, (f19 + (float)k2 + 0.5F) * 0.00390625F + f4)
                                    .color(f14, f15, f16, 0.8F)
                                    .normal(0.0F, 0.0F, -1.0F)
                                    .endVertex();
                        }
                    }

                    if (l <= 1) {
                        for(int l2 = 0; l2 < 8; ++l2) {
                            p_234262_.vertex((double)(f18 + 0.0F), (double)(f17 + 4.0F), (double)(f19 + (float)l2 + 1.0F - 9.765625E-4F))
                                    .uv((f18 + 0.0F) * 0.00390625F + f3, (f19 + (float)l2 + 0.5F) * 0.00390625F + f4)
                                    .color(f14, f15, f16, 0.8F)
                                    .normal(0.0F, 0.0F, 1.0F)
                                    .endVertex();
                            p_234262_.vertex((double)(f18 + 8.0F), (double)(f17 + 4.0F), (double)(f19 + (float)l2 + 1.0F - 9.765625E-4F))
                                    .uv((f18 + 8.0F) * 0.00390625F + f3, (f19 + (float)l2 + 0.5F) * 0.00390625F + f4)
                                    .color(f14, f15, f16, 0.8F)
                                    .normal(0.0F, 0.0F, 1.0F)
                                    .endVertex();
                            p_234262_.vertex((double)(f18 + 8.0F), (double)(f17 + 0.0F), (double)(f19 + (float)l2 + 1.0F - 9.765625E-4F))
                                    .uv((f18 + 8.0F) * 0.00390625F + f3, (f19 + (float)l2 + 0.5F) * 0.00390625F + f4)
                                    .color(f14, f15, f16, 0.8F)
                                    .normal(0.0F, 0.0F, 1.0F)
                                    .endVertex();
                            p_234262_.vertex((double)(f18 + 0.0F), (double)(f17 + 0.0F), (double)(f19 + (float)l2 + 1.0F - 9.765625E-4F))
                                    .uv((f18 + 0.0F) * 0.00390625F + f3, (f19 + (float)l2 + 0.5F) * 0.00390625F + f4)
                                    .color(f14, f15, f16, 0.8F)
                                    .normal(0.0F, 0.0F, 1.0F)
                                    .endVertex();
                        }
                    }
                }
            }
        } else {
            int j1 = 1;
            int k1 = 32;

            for(int l1 = -32; l1 < 32; l1 += 32) {
                for(int i2 = -32; i2 < 32; i2 += 32) {
                    p_234262_.vertex((double)(l1 + 0), (double)f17, (double)(i2 + 32))
                            .uv((float)(l1 + 0) * 0.00390625F + f3, (float)(i2 + 32) * 0.00390625F + f4)
                            .color(f5, f6, f7, 0.8F)
                            .normal(0.0F, -1.0F, 0.0F)
                            .endVertex();
                    p_234262_.vertex((double)(l1 + 32), (double)f17, (double)(i2 + 32))
                            .uv((float)(l1 + 32) * 0.00390625F + f3, (float)(i2 + 32) * 0.00390625F + f4)
                            .color(f5, f6, f7, 0.8F)
                            .normal(0.0F, -1.0F, 0.0F)
                            .endVertex();
                    p_234262_.vertex((double)(l1 + 32), (double)f17, (double)(i2 + 0))
                            .uv((float)(l1 + 32) * 0.00390625F + f3, (float)(i2 + 0) * 0.00390625F + f4)
                            .color(f5, f6, f7, 0.8F)
                            .normal(0.0F, -1.0F, 0.0F)
                            .endVertex();
                    p_234262_.vertex((double)(l1 + 0), (double)f17, (double)(i2 + 0))
                            .uv((float)(l1 + 0) * 0.00390625F + f3, (float)(i2 + 0) * 0.00390625F + f4)
                            .color(f5, f6, f7, 0.8F)
                            .normal(0.0F, -1.0F, 0.0F)
                            .endVertex();
                }
            }
        }

        return p_234262_.end();
    }
}