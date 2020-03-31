package com.teammetallurgy.atum.client.particle;

import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class SwirlParticle extends SpriteTexturedParticle {
    private float scale;

    protected SwirlParticle(World world, double xCoord, double yCoord, double zCoord, double xSpeed, double ySpeed, double zSpeed, IAnimatedSprite spriteSet) {
        super(world, xCoord, yCoord, zCoord, xSpeed, ySpeed, zSpeed);
        this.motionX = this.motionX * 0.009999999776482582D + xSpeed;
        this.motionY = this.motionY * 0.009999999776482582D + ySpeed;
        this.motionZ = this.motionZ * 0.009999999776482582D + zSpeed;
        this.posX += (this.rand.nextFloat() - this.rand.nextFloat()) * 0.05F;
        this.posY += (this.rand.nextFloat() - this.rand.nextFloat()) * 0.05F;
        this.posZ += (this.rand.nextFloat() - this.rand.nextFloat()) * 0.05F;
        this.scale = this.particleScale;
        this.particleRed = 1.0F;
        this.particleGreen = 1.0F;
        this.particleBlue = 1.0F;
        this.maxAge = (int) (8.0D / (Math.random() * 0.8D + 0.2D)) + 4;
        this.selectSpriteWithAge(spriteSet);
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
    public void renderParticle(IVertexBuilder builder, ActiveRenderInfo renderInfo, float partialTicks) {
        float f = ((float) this.age + partialTicks) / (float) this.maxAge;
        this.particleScale = this.scale * (1.0F - f * f * 0.5F);
        super.renderParticle(builder, renderInfo, partialTicks);
    }

    @Override
    public void tick() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        if (this.age++ >= this.maxAge) {
            this.setExpired();
        }
        this.move(this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.9599999785423279D;
        this.motionY *= 0.9599999785423279D;
        this.motionZ *= 0.9599999785423279D;

        if (this.onGround) {
            this.motionX *= 0.699999988079071D;
            this.motionZ *= 0.699999988079071D;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class Anubis implements IParticleFactory<BasicParticleType> {
        private final IAnimatedSprite spriteSet;

        public Anubis(IAnimatedSprite sprite) {
            this.spriteSet = sprite;
        }

        @Override
        public Particle makeParticle(@Nonnull BasicParticleType particleType, @Nonnull World world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new SwirlParticle(world, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class AnubisSkull implements IParticleFactory<BasicParticleType> {
        private final IAnimatedSprite spriteSet;

        public AnubisSkull(IAnimatedSprite sprite) {
            this.spriteSet = sprite;
        }

        @Override
        public Particle makeParticle(@Nonnull BasicParticleType particleType, @Nonnull World world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new SwirlParticle(world, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class Gas implements IParticleFactory<BasicParticleType> {
        private final IAnimatedSprite spriteSet;

        public Gas(IAnimatedSprite sprite) {
            this.spriteSet = sprite;
        }

        @Override
        public Particle makeParticle(@Nonnull BasicParticleType particleType, @Nonnull World world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            SwirlParticle particle = new SwirlParticle(world, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
            particle.scale = MathHelper.nextFloat(world.rand, 0.05F, 0.18F);
            particle.canCollide = true;
            return particle;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class Geb implements IParticleFactory<BasicParticleType> {
        private final IAnimatedSprite spriteSet;

        public Geb(IAnimatedSprite sprite) {
            this.spriteSet = sprite;
        }

        @Override
        public Particle makeParticle(@Nonnull BasicParticleType particleType, @Nonnull World world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new SwirlParticle(world, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class Horus implements IParticleFactory<BasicParticleType> {
        private final IAnimatedSprite spriteSet;

        public Horus(IAnimatedSprite sprite) {
            this.spriteSet = sprite;
        }

        @Override
        public Particle makeParticle(@Nonnull BasicParticleType particleType, @Nonnull World world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new SwirlParticle(world, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class Isis implements IParticleFactory<BasicParticleType> {
        private final IAnimatedSprite spriteSet;

        public Isis(IAnimatedSprite sprite) {
            this.spriteSet = sprite;
        }

        @Override
        public Particle makeParticle(@Nonnull BasicParticleType particleType, @Nonnull World world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            SwirlParticle particle = new SwirlParticle(world, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
            particle.scale = MathHelper.nextFloat(world.rand, 0.05F, 0.15F);
            return particle;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class NuitBlack implements IParticleFactory<BasicParticleType> {
        private final IAnimatedSprite spriteSet;

        public NuitBlack(IAnimatedSprite sprite) {
            this.spriteSet = sprite;
        }

        @Override
        public Particle makeParticle(@Nonnull BasicParticleType particleType, @Nonnull World world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new SwirlParticle(world, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class NuitWhite implements IParticleFactory<BasicParticleType> {
        private final IAnimatedSprite spriteSet;

        public NuitWhite(IAnimatedSprite sprite) {
            this.spriteSet = sprite;
        }

        @Override
        public Particle makeParticle(@Nonnull BasicParticleType particleType, @Nonnull World world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new SwirlParticle(world, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class Shu implements IParticleFactory<BasicParticleType> {
        private final IAnimatedSprite spriteSet;

        public Shu(IAnimatedSprite sprite) {
            this.spriteSet = sprite;
        }

        @Override
        public Particle makeParticle(@Nonnull BasicParticleType particleType, @Nonnull World world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new SwirlParticle(world, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
        }
    }
}