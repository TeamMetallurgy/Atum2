package com.teammetallurgy.atum.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

import javax.annotation.Nonnull;

public class AtumSpecialEffects extends DimensionSpecialEffects {

    public AtumSpecialEffects() {
        super(220, true, DimensionSpecialEffects.SkyType.NORMAL, false, false);
    }

    @Override
    @Nonnull
    public Vec3 getBrightnessDependentFogColor(Vec3 vec3, float f) { //Same as overworld
        return vec3.multiply(f * 0.94F + 0.06F, f * 0.94F + 0.06F, (double) (f * 0.91F + 0.09F));
    }

    @Override
    public boolean isFoggyAt(int x, int y) {
        return false; //TODO
    }

    @Override
    public boolean renderSky(@Nonnull ClientLevel level, int ticks, float partialTick, @Nonnull PoseStack poseStack, @Nonnull Camera camera, @Nonnull Matrix4f projectionMatrix, boolean isFoggy, @Nonnull Runnable setupFog) {
        return AtumSkyRenderer.renderSky(level, ticks, partialTick, poseStack, camera, projectionMatrix, isFoggy, setupFog);
    }

    @Override
    public boolean renderClouds(@Nonnull ClientLevel level, int ticks, float partialTick, @Nonnull PoseStack poseStack, double camX, double camY, double camZ, @Nonnull Matrix4f projectionMatrix) {
        return AtumCloudRenderer.renderClouds(level, ticks, partialTick, poseStack, camX, camY, camZ, projectionMatrix);
    }

    @Override
    public boolean renderSnowAndRain(@Nonnull ClientLevel level, int ticks, float partialTick, @Nonnull LightTexture lightTexture, double camX, double camY, double camZ) {
        return true; //Setting to true prevents rendering
    }

    @Override
    public boolean tickRain(@Nonnull ClientLevel level, int ticks, @Nonnull Camera camera) {
        return true; //Setting to true prevents rendering
    }
}