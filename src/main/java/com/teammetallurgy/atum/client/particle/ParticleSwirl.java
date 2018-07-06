package com.teammetallurgy.atum.client.particle;

import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(value = Side.CLIENT)
public class ParticleSwirl extends ParticleBase {
    private static final ResourceLocation ANUBIS = new ResourceLocation(Constants.MOD_ID, "particle/anubis");
    private static final ResourceLocation GAS = new ResourceLocation(Constants.MOD_ID, "particle/gas");
    private static final ResourceLocation GEB = new ResourceLocation(Constants.MOD_ID, "particle/geb");
    private static final ResourceLocation HORUS = new ResourceLocation(Constants.MOD_ID, "particle/shu");
    private static final ResourceLocation ISIS = new ResourceLocation(Constants.MOD_ID, "particle/isis");
    private static final ResourceLocation NUIT = new ResourceLocation(Constants.MOD_ID, "particle/nuit");
    private static final ResourceLocation SHU = new ResourceLocation(Constants.MOD_ID, "particle/shu");
    private final float scale;

    protected ParticleSwirl(World world, double xCoord, double yCoord, double zCoord, double xSpeed, double ySpeed, double zSpeed) {
        super(world, xCoord, yCoord, zCoord, xSpeed, ySpeed, zSpeed);
        this.motionX = this.motionX * 0.009999999776482582D + xSpeed;
        this.motionY = this.motionY * 0.009999999776482582D + ySpeed;
        this.motionZ = this.motionZ * 0.009999999776482582D + zSpeed;
        this.posX += (double) ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.05F);
        this.posY += (double) ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.05F);
        this.posZ += (double) ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.05F);
        this.scale = this.particleScale;
        this.particleRed = 1.0F;
        this.particleGreen = 1.0F;
        this.particleBlue = 1.0F;
        this.particleMaxAge = (int) (8.0D / (Math.random() * 0.8D + 0.2D)) + 4;
    }

    @Override
    public void move(double x, double y, double z) {
        this.setBoundingBox(this.getBoundingBox().offset(x, y, z));
        this.resetPositionToBB();
    }

    @Override
    public void renderParticle(BufferBuilder buffer, Entity entity, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
        float f = ((float) this.particleAge + partialTicks) / (float) this.particleMaxAge;
        this.particleScale = this.scale * (1.0F - f * f * 0.5F);
        super.renderParticle(buffer, entity, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
    }

    @Override
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        if (this.particleAge++ >= this.particleMaxAge) {
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


    @SubscribeEvent
    public static void onTextureStitch(TextureStitchEvent.Pre event) {
        event.getMap().registerSprite(ANUBIS);
        event.getMap().registerSprite(GAS);
        event.getMap().registerSprite(GEB);
        event.getMap().registerSprite(HORUS);
        event.getMap().registerSprite(ISIS);
        event.getMap().registerSprite(NUIT);
        event.getMap().registerSprite(SHU);
    }

    @SideOnly(Side.CLIENT)
    public static class Anubis implements IAtumParticleFactory {
        public Particle createParticle(String name, @Nonnull World world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            Particle particle = new ParticleSwirl(world, x, y, z, xSpeed, ySpeed, zSpeed);
            particle.setParticleTexture(getSprite(ANUBIS));
            return particle;
        }
    }

    @SideOnly(Side.CLIENT)
    public static class Gas implements IAtumParticleFactory {
        public Particle createParticle(String name, @Nonnull World world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            Particle particle = new ParticleSwirl(world, x, y, z, xSpeed, ySpeed, zSpeed);
            particle.setParticleTexture(getSprite(GAS));
            return particle;
        }
    }

    @SideOnly(Side.CLIENT)
    public static class Geb implements IAtumParticleFactory {
        public Particle createParticle(String name, @Nonnull World world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            Particle particle = new ParticleSwirl(world, x, y, z, xSpeed, ySpeed, zSpeed);
            particle.setParticleTexture(getSprite(GEB));
            return particle;
        }
    }

    @SideOnly(Side.CLIENT)
    public static class Horus implements IAtumParticleFactory {
        public Particle createParticle(String name, @Nonnull World world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            Particle particle = new ParticleSwirl(world, x, y, z, xSpeed, ySpeed, zSpeed);
            particle.setParticleTexture(getSprite(HORUS));
            return particle;
        }
    }

    @SideOnly(Side.CLIENT)
    public static class Isis implements IAtumParticleFactory {
        public Particle createParticle(String name, @Nonnull World world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            Particle particle = new ParticleSwirl(world, x, y, z, xSpeed, ySpeed, zSpeed);
            particle.setParticleTexture(getSprite(ISIS));
            return particle;
        }
    }

    @SideOnly(Side.CLIENT)
    public static class Nuit implements IAtumParticleFactory {
        public Particle createParticle(String name, @Nonnull World world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            Particle particle = new ParticleSwirl(world, x, y, z, xSpeed, ySpeed, zSpeed);
            particle.setParticleTexture(getSprite(NUIT));
            return particle;
        }
    }

    @SideOnly(Side.CLIENT)
    public static class Shu implements IAtumParticleFactory {
        public Particle createParticle(String name, @Nonnull World world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            Particle particle = new ParticleSwirl(world, x, y, z, xSpeed, ySpeed, zSpeed);
            particle.setParticleTexture(getSprite(SHU));
            return particle;
        }
    }
}