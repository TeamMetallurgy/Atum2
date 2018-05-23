package com.teammetallurgy.atum.client.particle;

import net.minecraft.client.particle.Particle;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

@SideOnly(Side.CLIENT)
public interface IAtumParticleFactory {
    @Nullable
    Particle createParticle(String particleName, World world, double xCoord, double yCoord, double zCoord, double xSpeed, double ySpeed, double zSpeed);
}