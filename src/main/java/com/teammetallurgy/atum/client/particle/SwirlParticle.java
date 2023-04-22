package com.teammetallurgy.atum.client.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class SwirlParticle extends TextureSheetParticle {
    private float scale;

    protected SwirlParticle(ClientLevel level, double xCoord, double yCoord, double zCoord, double xSpeed, double ySpeed, double zSpeed, SpriteSet spriteSet) {
        super(level, xCoord, yCoord, zCoord, xSpeed, ySpeed, zSpeed);
        this.xd = this.xd * 0.009999999776482582D + xSpeed;
        this.yd = this.yd * 0.009999999776482582D + ySpeed;
        this.zd = this.zd * 0.009999999776482582D + zSpeed;
        this.x += (this.random.nextFloat() - this.random.nextFloat()) * 0.05F;
        this.y += (this.random.nextFloat() - this.random.nextFloat()) * 0.05F;
        this.z += (this.random.nextFloat() - this.random.nextFloat()) * 0.05F;
        this.scale = this.quadSize;
        this.rCol = 1.0F;
        this.gCol = 1.0F;
        this.bCol = 1.0F;
        this.lifetime = (int) (8.0D / (Math.random() * 0.8D + 0.2D)) + 4;
        this.setSpriteFromAge(spriteSet);
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
    public void render(@Nonnull VertexConsumer builder, @Nonnull Camera renderInfo, float partialTicks) {
        float f = ((float) this.age + partialTicks) / (float) this.lifetime;
        this.quadSize = this.scale * (1.0F - f * f * 0.5F);
        super.render(builder, renderInfo, partialTicks);
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        if (this.age++ >= this.lifetime) {
            this.remove();
        }
        this.move(this.xd, this.yd, this.zd);
        this.xd *= 0.9599999785423279D;
        this.yd *= 0.9599999785423279D;
        this.zd *= 0.9599999785423279D;

        if (this.onGround) {
            this.xd *= 0.699999988079071D;
            this.zd *= 0.699999988079071D;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class Anubis implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public Anubis(SpriteSet sprite) {
            this.spriteSet = sprite;
        }

        @Override
        public Particle createParticle(@Nonnull SimpleParticleType particleType, @Nonnull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new SwirlParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class AnubisSkull implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public AnubisSkull(SpriteSet sprite) {
            this.spriteSet = sprite;
        }

        @Override
        public Particle createParticle(@Nonnull SimpleParticleType particleType, @Nonnull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new SwirlParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class Gas implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public Gas(SpriteSet sprite) {
            this.spriteSet = sprite;
        }

        @Override
        public Particle createParticle(@Nonnull SimpleParticleType particleType, @Nonnull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            SwirlParticle particle = new SwirlParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
            particle.scale = Mth.nextFloat(level.random, 0.05F, 0.18F);
            particle.hasPhysics = true;
            return particle;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class Geb implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public Geb(SpriteSet sprite) {
            this.spriteSet = sprite;
        }

        @Override
        public Particle createParticle(@Nonnull SimpleParticleType particleType, @Nonnull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new SwirlParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class Horus implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public Horus(SpriteSet sprite) {
            this.spriteSet = sprite;
        }

        @Override
        public Particle createParticle(@Nonnull SimpleParticleType particleType, @Nonnull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new SwirlParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class Isis implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public Isis(SpriteSet sprite) {
            this.spriteSet = sprite;
        }

        @Override
        public Particle createParticle(@Nonnull SimpleParticleType particleType, @Nonnull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            SwirlParticle particle = new SwirlParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
            particle.scale = Mth.nextFloat(level.random, 0.05F, 0.15F);
            return particle;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class NuitBlack implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public NuitBlack(SpriteSet sprite) {
            this.spriteSet = sprite;
        }

        @Override
        public Particle createParticle(@Nonnull SimpleParticleType particleType, @Nonnull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new SwirlParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class NuitWhite implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public NuitWhite(SpriteSet sprite) {
            this.spriteSet = sprite;
        }

        @Override
        public Particle createParticle(@Nonnull SimpleParticleType particleType, @Nonnull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new SwirlParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class Shu implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public Shu(SpriteSet sprite) {
            this.spriteSet = sprite;
        }

        @Override
        public Particle createParticle(@Nonnull SimpleParticleType particleType, @Nonnull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new SwirlParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
        }
    }
}