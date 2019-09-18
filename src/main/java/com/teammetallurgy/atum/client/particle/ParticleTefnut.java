package com.teammetallurgy.atum.client.particle;

import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.client.particle.Particle;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT)
public class ParticleTefnut extends ParticleBase {
    private static final ResourceLocation TEFNUT = new ResourceLocation(Constants.MOD_ID, "particle/tefnut");

    public ParticleTefnut(World world, double xCoord, double yCoord, double zCoord, double xSpeed, double ySpeed, double speed) {
        super(world, xCoord, yCoord, zCoord, xSpeed, ySpeed, speed);
        this.setSize(0.02F, 0.02F);
        this.particleScale *= this.rand.nextFloat() * 0.6F;
        this.motionX *= 0.019999999552965164D;
        this.motionY *= 0.019999999552965164D;
        this.motionZ *= 0.019999999552965164D;
        this.particleMaxAge = (int) (20.0D / (Math.random() * 0.8D + 0.2D));
        this.setParticleTexture(getSprite(TEFNUT));
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
        registerSprite(TEFNUT);
    }

    @OnlyIn(Dist.CLIENT)
    public static class Factory implements IAtumParticleFactory {
        public Particle createParticle(String name, @Nonnull World world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new ParticleTefnut(world, x, y, z, xSpeed, ySpeed, zSpeed);
        }
    }
}