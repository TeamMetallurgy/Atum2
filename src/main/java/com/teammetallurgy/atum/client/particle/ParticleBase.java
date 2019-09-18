package com.teammetallurgy.atum.client.particle;

import com.teammetallurgy.atum.client.TextureManagerParticles;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT)
@OnlyIn(Dist.CLIENT)
public class ParticleBase extends Particle {

    ParticleBase(World world, double posX, double posY, double posZ) {
        super(world, posX, posY, posZ);
    }

    ParticleBase(World world, double xCoord, double yCoord, double zCoord, double xSpeed, double ySpeed, double zSpeed) {
        this(world, xCoord, yCoord, zCoord);
        this.motionX = xSpeed + (Math.random() * 2.0D - 1.0D) * 0.4000000059604645D;
        this.motionY = ySpeed + (Math.random() * 2.0D - 1.0D) * 0.4000000059604645D;
        this.motionZ = zSpeed + (Math.random() * 2.0D - 1.0D) * 0.4000000059604645D;
        float f = (float) (Math.random() + Math.random() + 1.0D) * 0.15F;
        float f1 = MathHelper.sqrt(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
        this.motionX = this.motionX / (double) f1 * (double) f * 0.4000000059604645D;
        this.motionY = this.motionY / (double) f1 * (double) f * 0.4000000059604645D + 0.10000000149011612D;
        this.motionZ = this.motionZ / (double) f1 * (double) f * 0.4000000059604645D;
    }

    @Override
    public void setParticleTexture(@Nonnull TextureAtlasSprite texture) {
        this.particleTexture = texture;
    }

    static void registerSprite(ResourceLocation location) {
        TextureManagerParticles.INSTANCE.registerSprite(location);
    }

    static TextureAtlasSprite getSprite(ResourceLocation location) {
        return TextureManagerParticles.INSTANCE.getSprite(location);
    }

    @Override
    public int getFXLayer() {
        return 0;
    }
}