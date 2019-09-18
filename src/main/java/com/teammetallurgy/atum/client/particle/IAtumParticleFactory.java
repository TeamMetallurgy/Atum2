package com.teammetallurgy.atum.client.particle;

import net.minecraft.client.particle.Particle;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public interface IAtumParticleFactory {
    @Nullable
    Particle createParticle(String particleName, World world, double xCoord, double yCoord, double zCoord, double xSpeed, double ySpeed, double zSpeed);
}