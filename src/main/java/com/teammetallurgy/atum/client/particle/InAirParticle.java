package com.teammetallurgy.atum.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

public class InAirParticle extends TextureSheetParticle {

    public InAirParticle(ClientLevel clientLevel, SpriteSet spriteSet, double d1, double d2, double d3) {
        super(clientLevel, d1, d2 - 0.125D, d3);
        this.setSize(0.01F, 0.01F);
        this.pickSprite(spriteSet);
        this.quadSize *= this.random.nextFloat() * 0.6F + 0.2F;
        this.lifetime = (int) (16.0D / (Math.random() * 0.8D + 0.2D));
        this.hasPhysics = false;
        this.friction = 1.0F;
        this.gravity = 0.0F;
    }

    public InAirParticle(ClientLevel clientLevel, SpriteSet spriteSet, double d1, double d2, double d3, double d4, double d5, double d6) {
        super(clientLevel, d1, d2 - 0.125D, d3, d4, d5, d6);
        this.setSize(0.01F, 0.01F);
        this.pickSprite(spriteSet);
        this.quadSize *= this.random.nextFloat() * 0.6F + 0.6F;
        this.lifetime = (int) (16.0D / (Math.random() * 0.8D + 0.2D));
        this.hasPhysics = false;
        this.friction = 1.0F;
        this.gravity = 0.0F;
    }

    @Override
    @Nonnull
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @OnlyIn(Dist.CLIENT)
    public static class SandAirParticle implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprite;

        public SandAirParticle(SpriteSet p_108042_) {
            this.sprite = p_108042_;
        }

        public Particle createParticle(SimpleParticleType p_108053_, ClientLevel p_108054_, double p_108055_, double p_108056_, double p_108057_, double p_108058_, double p_108059_, double p_108060_) {
            RandomSource randomsource = p_108054_.random;
            double d0 = randomsource.nextGaussian() * (double) 1.0E-6F;
            double d1 = randomsource.nextGaussian() * (double) 1.0E-4F;
            double d2 = randomsource.nextGaussian() * (double) 1.0E-6F;
            InAirParticle inAirParticle = new InAirParticle(p_108054_, this.sprite, p_108055_, p_108056_, p_108057_, d0, d1, d2);
            inAirParticle.setColor(0.816F, 0.706F, 0.557F);
            return inAirParticle;
        }
    }
}