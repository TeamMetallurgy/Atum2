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
public class ParticleSeth extends ParticleBase {
    private static final ResourceLocation SETH = new ResourceLocation(Constants.MOD_ID, "particle/seth");
    private int bobTimer;

    protected ParticleSeth(World world, double xCoord, double yCoord, double zCoord) {
        super(world, xCoord, yCoord, zCoord, 0.0D, 0.0D, 0.0D);
        this.motionX = 0.0D;
        this.motionY = 0.0D;
        this.motionZ = 0.0D;
        this.setSize(0.01F, 0.01F);
        this.particleGravity = 0.06F;
        this.bobTimer = 40;
        this.particleMaxAge = (int) (64.0D / (Math.random() * 0.8D + 0.2D));
        this.motionX = 0.0D;
        this.motionY = 0.0D;
        this.motionZ = 0.0D;
        this.setParticleTexture(getSprite(SETH));
    }

    @Override
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        this.motionY -= (double) this.particleGravity;

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
    }

    @SideOnly(Side.CLIENT)
    public static class Factory implements IAtumParticleFactory {
        public Particle createParticle(String name, @Nonnull World world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new ParticleSeth(world, x, y, z);
        }
    }
}