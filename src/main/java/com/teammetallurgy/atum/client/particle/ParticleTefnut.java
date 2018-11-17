package com.teammetallurgy.atum.client.particle;

import com.teammetallurgy.atum.client.TextureManagerParticles;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.client.particle.Particle;
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
public class ParticleTefnut extends ParticleBase {
    private static final ResourceLocation TEFNUT = new ResourceLocation(Constants.MOD_ID, "particle/tefnut");

    public ParticleTefnut(World world, double xCoord, double yCoord, double zCoord, double xSpeed, double ySpeed, double speed) {
        super(world, xCoord, yCoord, zCoord, xSpeed, ySpeed, speed);
        float f = this.rand.nextFloat() * 0.1F + 0.2F;
        this.particleRed = f;
        this.particleGreen = f;
        this.particleBlue = f;
        this.setSize(0.02F, 0.02F);
        this.particleScale *= this.rand.nextFloat() * 0.6F + 0.5F;
        this.motionX *= 0.019999999552965164D;
        this.motionY *= 0.019999999552965164D;
        this.motionZ *= 0.019999999552965164D;
        this.particleMaxAge = (int) (20.0D / (Math.random() * 0.8D + 0.2D));
        this.setParticleTexture(TextureManagerParticles.INSTANCE.getSprite(TEFNUT));
    }

    @Override
    public void move(double x, double y, double z) {
        this.setBoundingBox(this.getBoundingBox().offset(x, y, z));
        this.resetPositionToBB();
    }

    @Override
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.move(this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.99D;
        this.motionY *= 0.99D;
        this.motionZ *= 0.99D;

        if (this.particleMaxAge-- <= 0) {
            this.setExpired();
        }
    }

    @SubscribeEvent
    public static void onTextureStitch(TextureStitchEvent.Pre event) {
        TextureManagerParticles.INSTANCE.registerSprite(TEFNUT);
    }

    @SideOnly(Side.CLIENT)
    public static class Factory implements IAtumParticleFactory {
        public Particle createParticle(String name, @Nonnull World world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new ParticleTefnut(world, x, y, z, xSpeed, ySpeed, zSpeed);
        }
    }
}