package com.teammetallurgy.atum.client.particle;

import net.minecraft.client.particle.*;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;

import javax.annotation.Nonnull;

public class NebuFlameParticle extends RisingParticle {

    public NebuFlameParticle(ClientLevel world, double x, double y, double z, double motionX, double motionY, double motionZ) {
        super(world, x, y, z, motionX, motionY, motionZ);
    }

    @Override
    @Nonnull
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public void move(double x, double y, double z) {
        this.setBoundingBox(this.getBoundingBox().move(x, y, z));
        this.setLocationFromBoundingbox();
    }

    @Override
    public float getQuadSize(float scaleFactor) {
        float f = ((float) this.age + scaleFactor) / (float) this.lifetime;
        return this.quadSize * (1.0F - f * f * 0.5F);
    }

    @Override
    public int getLightColor(float partialTick) {
        float f = ((float) this.age + partialTick) / (float) this.lifetime;
        f = Mth.clamp(f, 0.0F, 1.0F);
        int i = super.getLightColor(partialTick);
        int j = i & 255;
        int k = i >> 16 & 255;
        j = j + (int) (f * 15.0F * 16.0F);
        if (j > 240) {
            j = 240;
        }

        return j | k << 16;
    }

    public static class Nebu implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public Nebu(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle createParticle(@Nonnull SimpleParticleType type, @Nonnull ClientLevel world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            NebuFlameParticle nebuFireParticle = new NebuFlameParticle(world, x, y, z, xSpeed, ySpeed, zSpeed);
            nebuFireParticle.pickSprite(this.spriteSet);
            return nebuFireParticle;
        }
    }
}