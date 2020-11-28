package com.teammetallurgy.atum.client.particle;

import com.teammetallurgy.atum.entity.undead.PharaohEntity;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nonnull;
import java.util.HashMap;

public class NebuFlameParticle extends DeceleratingParticle {
    public static final HashMap<PharaohEntity.God, BasicParticleType> GOD_FLAMES = new HashMap<>();

    public NebuFlameParticle(ClientWorld world, double x, double y, double z, double motionX, double motionY, double motionZ) {
        super(world, x, y, z, motionX, motionY, motionZ);
    }

    @Override
    @Nonnull
    public IParticleRenderType getRenderType() {
        return IParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public void move(double x, double y, double z) {
        this.setBoundingBox(this.getBoundingBox().offset(x, y, z));
        this.resetPositionToBB();
    }

    @Override
    public float getScale(float scaleFactor) {
        float f = ((float) this.age + scaleFactor) / (float) this.maxAge;
        return this.particleScale * (1.0F - f * f * 0.5F);
    }

    @Override
    public int getBrightnessForRender(float partialTick) {
        float f = ((float) this.age + partialTick) / (float) this.maxAge;
        f = MathHelper.clamp(f, 0.0F, 1.0F);
        int i = super.getBrightnessForRender(partialTick);
        int j = i & 255;
        int k = i >> 16 & 255;
        j = j + (int) (f * 15.0F * 16.0F);
        if (j > 240) {
            j = 240;
        }

        return j | k << 16;
    }

    public static class Nebu implements IParticleFactory<BasicParticleType> {
        private final IAnimatedSprite spriteSet;

        public Nebu(IAnimatedSprite spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle makeParticle(@Nonnull BasicParticleType type, @Nonnull ClientWorld world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            NebuFlameParticle nebuFireParticle = new NebuFlameParticle(world, x, y, z, xSpeed, ySpeed, zSpeed);
            nebuFireParticle.selectSpriteRandomly(this.spriteSet);
            return nebuFireParticle;
        }
    }
}