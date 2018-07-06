package com.teammetallurgy.atum.client.particle;

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
public class ParticleDrop extends ParticleBase {
    public float dropGravity;
    private int bobTimer;
    private static final ResourceLocation SETH = new ResourceLocation(Constants.MOD_ID, "particle/seth");
    private static final ResourceLocation TAR = new ResourceLocation(Constants.MOD_ID, "particle/tar");

    protected ParticleDrop(World world, double xCoord, double yCoord, double zCoord) {
        super(world, xCoord, yCoord, zCoord, 0.0D, 0.0D, 0.0D);
        this.motionX = 0.0D;
        this.motionY = 0.0D;
        this.motionZ = 0.0D;
        this.setSize(0.01F, 0.01F);
        this.bobTimer = 40;
        this.motionX = 0.0D;
        this.motionY = 0.0D;
        this.motionZ = 0.0D;
    }

    @Override
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        this.motionY -= (double) this.dropGravity;

        if (this.bobTimer-- > 0) {
            this.motionX *= 0.02D;
            this.motionY *= 0.02D;
            this.motionZ *= 0.02D;
        }

        this.move(this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.9800000190734863D;
        this.motionY *= 0.9800000190734863D;
        this.motionZ *= 0.9800000190734863D;

        if (this.particleMaxAge-- <= 0) {
            this.setExpired();
        }

        if (this.onGround) {
            this.motionX *= 0.699999988079071D;
            this.motionZ *= 0.699999988079071D;
        }
    }

    @SubscribeEvent
    public static void onTextureStitch(TextureStitchEvent.Pre event) {
        event.getMap().registerSprite(SETH);
        event.getMap().registerSprite(TAR);
    }

    @SideOnly(Side.CLIENT)
    public static class Seth implements IAtumParticleFactory {
        public Particle createParticle(String name, @Nonnull World world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            Particle particle = new ParticleDrop(world, x, y, z);
            particle.setParticleTexture(getSprite(SETH));
            ((ParticleDrop) particle).dropGravity = 8.0F;
            particle.setMaxAge((int) (64.0D / (Math.random() * 0.8D + 0.2D)));
            return particle;
        }
    }

    @SideOnly(Side.CLIENT)
    public static class Tar implements IAtumParticleFactory {
        public Particle createParticle(String name, @Nonnull World world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            Particle particle = new ParticleDrop(world, x, y, z);
            particle.setParticleTexture(getSprite(TAR));
            ((ParticleDrop) particle).dropGravity = 4.0F;
            particle.setMaxAge((int) (16.0D / (Math.random() * 0.8D + 0.2D)));
            return particle;
        }
    }
}