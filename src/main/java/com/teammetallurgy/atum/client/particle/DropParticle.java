package com.teammetallurgy.atum.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class DropParticle extends TextureSheetParticle {
    public float dropGravity;
    private int bobTimer;

    protected DropParticle(ClientLevel level, double xCoord, double yCoord, double zCoord, SpriteSet spriteSet) {
        super(level, xCoord, yCoord, zCoord, 0.0D, 0.0D, 0.0D);
        this.xd = 0.0D;
        this.yd = 0.0D;
        this.zd = 0.0D;
        this.setSize(0.01F, 0.01F);
        this.bobTimer = 40;
        this.xd = 0.0D;
        this.yd = 0.0D;
        this.zd = 0.0D;
        this.pickSprite(spriteSet);
    }

    @Override
    @Nonnull
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        this.yd -= this.dropGravity;

        if (this.bobTimer-- > 0) {
            this.xd *= 0.02D;
            this.yd *= 0.02D;
            this.zd *= 0.02D;
        }

        this.move(this.xd, this.yd, this.zd);
        this.xd *= 0.9800000190734863D;
        this.yd *= 0.9800000190734863D;
        this.zd *= 0.9800000190734863D;

        if (this.lifetime-- <= 0) {
            this.remove();
        }

        if (this.onGround) {
            this.xd *= 0.699999988079071D;
            this.zd *= 0.699999988079071D;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class Seth implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public Seth(SpriteSet sprite) {
            this.spriteSet = sprite;
        }

        @Override
        public Particle createParticle(@Nonnull SimpleParticleType particleType, @Nonnull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            DropParticle particle = new DropParticle(level, x, y, z, this.spriteSet);
            particle.quadSize = 0.05F;
            particle.dropGravity = 8.0F;
            particle.setLifetime((int) (64.0D / (Math.random() * 0.8D + 0.2D)));
            return particle;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class Tefnut implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public Tefnut(SpriteSet sprite) {
            this.spriteSet = sprite;
        }

        @Override
        public Particle createParticle(@Nonnull SimpleParticleType particleType, @Nonnull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            DropParticle particle = new DropParticle(level, x, y, z, this.spriteSet);
            particle.quadSize = 0.05F;
            particle.dropGravity = 8.0F;
            particle.setLifetime((int) (64.0D / (Math.random() * 0.8D + 0.2D)));
            return particle;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class Tar implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public Tar(SpriteSet sprite) {
            this.spriteSet = sprite;
        }

        @Override
        public Particle createParticle(@Nonnull SimpleParticleType particleType, @Nonnull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            DropParticle particle = new DropParticle(level, x, y, z, this.spriteSet);
            particle.quadSize = 0.15F;
            particle.dropGravity = 4.0F;
            particle.setLifetime((int) (16.0D / (Math.random() * 0.8D + 0.2D)));
            return particle;
        }
    }
}