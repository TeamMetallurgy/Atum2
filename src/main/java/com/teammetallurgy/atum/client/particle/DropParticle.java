package com.teammetallurgy.atum.client.particle;

import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class DropParticle extends SpriteTexturedParticle {
    public float dropGravity;
    private int bobTimer;

    protected DropParticle(ClientWorld world, double xCoord, double yCoord, double zCoord, IAnimatedSprite spriteSet) {
        super(world, xCoord, yCoord, zCoord, 0.0D, 0.0D, 0.0D);
        this.motionX = 0.0D;
        this.motionY = 0.0D;
        this.motionZ = 0.0D;
        this.setSize(0.01F, 0.01F);
        this.bobTimer = 40;
        this.motionX = 0.0D;
        this.motionY = 0.0D;
        this.motionZ = 0.0D;
        this.selectSpriteRandomly(spriteSet);
    }

    @Override
    @Nonnull
    public IParticleRenderType getRenderType() {
        return IParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public void tick() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        this.motionY -= this.dropGravity;

        if (this.bobTimer-- > 0) {
            this.motionX *= 0.02D;
            this.motionY *= 0.02D;
            this.motionZ *= 0.02D;
        }

        this.move(this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.9800000190734863D;
        this.motionY *= 0.9800000190734863D;
        this.motionZ *= 0.9800000190734863D;

        if (this.maxAge-- <= 0) {
            this.setExpired();
        }

        if (this.onGround) {
            this.motionX *= 0.699999988079071D;
            this.motionZ *= 0.699999988079071D;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class Seth implements IParticleFactory<BasicParticleType> {
        private final IAnimatedSprite spriteSet;

        public Seth(IAnimatedSprite sprite) {
            this.spriteSet = sprite;
        }

        @Override
        public Particle makeParticle(@Nonnull BasicParticleType particleType, @Nonnull ClientWorld world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            DropParticle particle = new DropParticle(world, x, y, z, this.spriteSet);
            particle.particleScale = 0.05F;
            particle.dropGravity = 8.0F;
            particle.setMaxAge((int) (64.0D / (Math.random() * 0.8D + 0.2D)));
            return particle;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class Tefnut implements IParticleFactory<BasicParticleType> {
        private final IAnimatedSprite spriteSet;

        public Tefnut(IAnimatedSprite sprite) {
            this.spriteSet = sprite;
        }

        @Override
        public Particle makeParticle(@Nonnull BasicParticleType particleType, @Nonnull ClientWorld world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            DropParticle particle = new DropParticle(world, x, y, z, this.spriteSet);
            particle.particleScale = 0.05F;
            particle.dropGravity = 8.0F;
            particle.setMaxAge((int) (64.0D / (Math.random() * 0.8D + 0.2D)));
            return particle;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class Tar implements IParticleFactory<BasicParticleType> {
        private final IAnimatedSprite spriteSet;

        public Tar(IAnimatedSprite sprite) {
            this.spriteSet = sprite;
        }

        @Override
        public Particle makeParticle(@Nonnull BasicParticleType particleType, @Nonnull ClientWorld world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            DropParticle particle = new DropParticle(world, x, y, z, this.spriteSet);
            particle.particleScale = 0.15F;
            particle.dropGravity = 4.0F;
            particle.setMaxAge((int) (16.0D / (Math.random() * 0.8D + 0.2D)));
            return particle;
        }
    }
}