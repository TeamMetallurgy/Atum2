package com.teammetallurgy.atum.client;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import com.teammetallurgy.atum.Atum;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.material.FogType;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class AtumSkyRenderer {
    private static final ResourceLocation SUN_LOCATION = new ResourceLocation("textures/environment/sun.png");
    private static final ResourceLocation MOON_LOCATION = new ResourceLocation("textures/environment/moon_phases.png");
    private static final ResourceLocation PLANET_TEST = new ResourceLocation(Atum.MOD_ID, "textures/environment/planet_test.png");
    @Nullable
    private static VertexBuffer starBuffer;
    @Nullable
    private static VertexBuffer skyBuffer;
    @Nullable
    private static VertexBuffer darkBuffer;

    public static boolean renderSky(@Nonnull ClientLevel level, int ticks, float partialTick, @Nonnull PoseStack poseStack, @Nonnull Camera camera, @Nonnull Matrix4f projectionMatrix, boolean isFoggy, @Nonnull Runnable setupFog) {
        Minecraft minecraft = Minecraft.getInstance();
        FogType fogtype = camera.getFluidInCamera();
        setupFog.run();
        if (!isFoggy && fogtype != FogType.POWDER_SNOW && fogtype != FogType.LAVA && !doesMobEffectBlockSky(camera)) {
            Vec3 vec3 = level.getSkyColor(minecraft.gameRenderer.getMainCamera().getPosition(), partialTick);
            float f = (float) vec3.x;
            float f1 = (float) vec3.y;
            float f2 = (float) vec3.z;
            FogRenderer.levelFogColor();
            BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
            RenderSystem.depthMask(false);
            RenderSystem.setShaderColor(f, f1, f2, 1.0F);
            ShaderInstance shaderinstance = RenderSystem.getShader();
            skyBuffer.bind();
            skyBuffer.drawWithShader(poseStack.last().pose(), projectionMatrix, shaderinstance);
            VertexBuffer.unbind();
            RenderSystem.enableBlend();
            float[] afloat = level.effects().getSunriseColor(level.getTimeOfDay(partialTick), partialTick);
            if (afloat != null) {
                RenderSystem.setShader(GameRenderer::getPositionColorShader);
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                poseStack.pushPose();
                poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
                float f3 = Mth.sin(level.getSunAngle(partialTick)) < 0.0F ? 180.0F : 0.0F;
                poseStack.mulPose(Axis.ZP.rotationDegrees(f3));
                poseStack.mulPose(Axis.ZP.rotationDegrees(90.0F));
                float f4 = afloat[0];
                float f5 = afloat[1];
                float f6 = afloat[2];
                Matrix4f matrix4f = poseStack.last().pose();
                bufferbuilder.begin(VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
                bufferbuilder.vertex(matrix4f, 0.0F, 100.0F, 0.0F).color(f4, f5, f6, afloat[3]).endVertex();
                int i = 16;

                for (int j = 0; j <= 16; ++j) {
                    float f7 = (float) j * (float) (Math.PI * 2) / 16.0F;
                    float f8 = Mth.sin(f7);
                    float f9 = Mth.cos(f7);
                    bufferbuilder.vertex(matrix4f, f8 * 120.0F, f9 * 120.0F, -f9 * 40.0F * afloat[3])
                            .color(afloat[0], afloat[1], afloat[2], 0.0F)
                            .endVertex();
                }

                BufferUploader.drawWithShader(bufferbuilder.end());
                poseStack.popPose();
            }

            RenderSystem.disableBlend();
            additionalPlanet(level, poseStack, level.getTimeOfDay(partialTick) * 360.0F, -180.0F, 0.0F, -50.0F, 0.0F, PLANET_TEST);
            additionalPlanet(level, poseStack, level.getTimeOfDay(partialTick) * 360.0F, -35.0F, level.getTimeOfDay(partialTick) * 360.0F, -150.0F, 0.0F, PLANET_TEST);
            RenderSystem.enableBlend();

            RenderSystem.blendFuncSeparate(
                    GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO
            );
            poseStack.pushPose();
            float f11 = 1.0F - level.getRainLevel(partialTick);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, f11);
            poseStack.mulPose(Axis.YP.rotationDegrees(-90.0F));
            poseStack.mulPose(Axis.XP.rotationDegrees(level.getTimeOfDay(partialTick) * 360.0F));
            Matrix4f matrix4f1 = poseStack.last().pose();
            float f12 = 30.0F;
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, SUN_LOCATION);
            bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
            bufferbuilder.vertex(matrix4f1, -f12, 100.0F, -f12).uv(0.0F, 0.0F).endVertex();
            bufferbuilder.vertex(matrix4f1, f12, 100.0F, -f12).uv(1.0F, 0.0F).endVertex();
            bufferbuilder.vertex(matrix4f1, f12, 100.0F, f12).uv(1.0F, 1.0F).endVertex();
            bufferbuilder.vertex(matrix4f1, -f12, 100.0F, f12).uv(0.0F, 1.0F).endVertex();
            BufferUploader.drawWithShader(bufferbuilder.end());
            f12 = 20.0F;
            RenderSystem.setShaderTexture(0, MOON_LOCATION);
            int k = level.getMoonPhase();
            int l = k % 4;
            int i1 = k / 4 % 2;
            float f13 = (float) (l + 0) / 4.0F;
            float f14 = (float) (i1 + 0) / 2.0F;
            float f15 = (float) (l + 1) / 4.0F;
            float f16 = (float) (i1 + 1) / 2.0F;
            bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
            //Bigger moon
            bufferbuilder.vertex(matrix4f1, -f12, -50.0F, f12).uv(f15, f16).endVertex();
            bufferbuilder.vertex(matrix4f1, f12, -50.0F, f12).uv(f13, f16).endVertex();
            bufferbuilder.vertex(matrix4f1, f12, -50.0F, -f12).uv(f13, f14).endVertex();
            bufferbuilder.vertex(matrix4f1, -f12, -50.0F, -f12).uv(f15, f14).endVertex();

            BufferUploader.drawWithShader(bufferbuilder.end());
            float f10 = level.getStarBrightness(partialTick) * f11;
            if (f10 > 0.0F) {
                RenderSystem.setShaderColor(f10, f10, f10, f10);
                FogRenderer.setupNoFog();
                starBuffer.bind();
                starBuffer.drawWithShader(poseStack.last().pose(), projectionMatrix, GameRenderer.getPositionShader());
                VertexBuffer.unbind();
                setupFog.run();
            }

            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.disableBlend();
            RenderSystem.defaultBlendFunc();
            poseStack.popPose();
            RenderSystem.setShaderColor(0.0F, 0.0F, 0.0F, 1.0F);
            double d0 = minecraft.player.getEyePosition(partialTick).y - level.getLevelData().getHorizonHeight(level);
            if (d0 < 0.0) {
                poseStack.pushPose();
                poseStack.translate(0.0F, 12.0F, 0.0F);
                darkBuffer.bind();
                darkBuffer.drawWithShader(poseStack.last().pose(), projectionMatrix, shaderinstance);
                VertexBuffer.unbind();
                poseStack.popPose();
            }

            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.depthMask(true);

        }
        return true; //Setting to true prevents rendering
    }

    public static void additionalPlanet(@Nonnull ClientLevel level, @Nonnull PoseStack poseStack, float xDegrees, float yDegrees, float zDegrees, float sizeModifier, float renderOffset, ResourceLocation location) {
        BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
        VertexBuffer.unbind();
        RenderSystem.enableBlend();

        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        poseStack.pushPose();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.mulPose(Axis.ZP.rotationDegrees(zDegrees));
        poseStack.mulPose(Axis.YP.rotationDegrees(yDegrees));
        poseStack.mulPose(Axis.XP.rotationDegrees(xDegrees));
        Matrix4f matrix4f1 = poseStack.last().pose();
        float f12 = 20.0F;
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, location);
        int k = level.getMoonPhase();
        int l = k % 4;
        int i1 = k / 4 % 2;
        float f13 = (float) (l + 0) / 4.0F;
        float f14 = (float) (i1 + 0) / 2.0F;
        float f15 = (float) (l + 1) / 4.0F;
        float f16 = (float) (i1 + 1) / 2.0F;
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        //Planet
        bufferbuilder.vertex(matrix4f1, -f12 + renderOffset, sizeModifier, f12).uv(f15, f16).endVertex();
        bufferbuilder.vertex(matrix4f1, f12, sizeModifier, f12).uv(f13, f16).endVertex();
        bufferbuilder.vertex(matrix4f1, f12, sizeModifier, -f12 + renderOffset).uv(f13, f14).endVertex();
        bufferbuilder.vertex(matrix4f1, -f12 + renderOffset, sizeModifier, -f12 + renderOffset).uv(f15, f14).endVertex();

        BufferUploader.drawWithShader(bufferbuilder.end());

        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();
        poseStack.popPose();
    }

    private static boolean doesMobEffectBlockSky(Camera camera) {
        Entity entity = camera.getEntity();
        if (!(entity instanceof LivingEntity livingentity)) {
            return false;
        } else {
            return livingentity.hasEffect(MobEffects.BLINDNESS) || livingentity.hasEffect(MobEffects.DARKNESS);
        }
    }

    public static void createStars() {
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tesselator.getBuilder();
        RenderSystem.setShader(GameRenderer::getPositionShader);
        if (starBuffer != null) {
            starBuffer.close();
        }

        starBuffer = new VertexBuffer(VertexBuffer.Usage.STATIC);
        BufferBuilder.RenderedBuffer bufferbuilder$renderedbuffer = drawStars(bufferbuilder);
        starBuffer.bind();
        starBuffer.upload(bufferbuilder$renderedbuffer);
        VertexBuffer.unbind();
    }

    public static BufferBuilder.RenderedBuffer drawStars(BufferBuilder p_234260_) {
        RandomSource randomsource = RandomSource.create(10842L);
        p_234260_.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);

        for (int i = 0; i < 1500; ++i) {
            double d0 = (double) (randomsource.nextFloat() * 2.0F - 1.0F);
            double d1 = (double) (randomsource.nextFloat() * 2.0F - 1.0F);
            double d2 = (double) (randomsource.nextFloat() * 2.0F - 1.0F);
            double d3 = (double) (0.15F + randomsource.nextFloat() * 0.1F);
            double d4 = d0 * d0 + d1 * d1 + d2 * d2;
            if (d4 < 1.0 && d4 > 0.01) {
                d4 = 1.0 / Math.sqrt(d4);
                d0 *= d4;
                d1 *= d4;
                d2 *= d4;
                double d5 = d0 * 100.0;
                double d6 = d1 * 100.0;
                double d7 = d2 * 100.0;
                double d8 = Math.atan2(d0, d2);
                double d9 = Math.sin(d8);
                double d10 = Math.cos(d8);
                double d11 = Math.atan2(Math.sqrt(d0 * d0 + d2 * d2), d1);
                double d12 = Math.sin(d11);
                double d13 = Math.cos(d11);
                double d14 = randomsource.nextDouble() * Math.PI * 2.0;
                double d15 = Math.sin(d14);
                double d16 = Math.cos(d14);

                for (int j = 0; j < 4; ++j) {
                    double d17 = 0.0;
                    double d18 = (double) ((j & 2) - 1) * d3;
                    double d19 = (double) ((j + 1 & 2) - 1) * d3;
                    double d20 = 0.0;
                    double d21 = d18 * d16 - d19 * d15;
                    double d22 = d19 * d16 + d18 * d15;
                    double d23 = d21 * d12 + 0.0 * d13;
                    double d24 = 0.0 * d12 - d21 * d13;
                    double d25 = d24 * d9 - d22 * d10;
                    double d26 = d22 * d9 + d24 * d10;
                    p_234260_.vertex(d5 + d25, d6 + d23, d7 + d26).endVertex();
                }
            }
        }

        return p_234260_.end();
    }

    public static void createDarkSky() {
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tesselator.getBuilder();
        if (darkBuffer != null) {
            darkBuffer.close();
        }

        darkBuffer = new VertexBuffer(VertexBuffer.Usage.STATIC);
        BufferBuilder.RenderedBuffer bufferbuilder$renderedbuffer = buildSkyDisc(bufferbuilder, -16.0F);
        darkBuffer.bind();
        darkBuffer.upload(bufferbuilder$renderedbuffer);
        VertexBuffer.unbind();
    }

    public static void createLightSky() {
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tesselator.getBuilder();
        if (skyBuffer != null) {
            skyBuffer.close();
        }

        skyBuffer = new VertexBuffer(VertexBuffer.Usage.STATIC);
        BufferBuilder.RenderedBuffer bufferbuilder$renderedbuffer = buildSkyDisc(bufferbuilder, 16.0F);
        skyBuffer.bind();
        skyBuffer.upload(bufferbuilder$renderedbuffer);
        VertexBuffer.unbind();
    }

    private static BufferBuilder.RenderedBuffer buildSkyDisc(BufferBuilder p_234268_, float p_234269_) {
        float f = Math.signum(p_234269_) * 512.0F;
        float f1 = 512.0F;
        RenderSystem.setShader(GameRenderer::getPositionShader);
        p_234268_.begin(VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION);
        p_234268_.vertex(0.0, (double) p_234269_, 0.0).endVertex();

        for (int i = -180; i <= 180; i += 45) {
            p_234268_.vertex(
                            (double) (f * Mth.cos((float) i * (float) (Math.PI / 180.0))),
                            (double) p_234269_,
                            (double) (512.0F * Mth.sin((float) i * (float) (Math.PI / 180.0)))
                    )
                    .endVertex();
        }

        return p_234268_.end();
    }
}