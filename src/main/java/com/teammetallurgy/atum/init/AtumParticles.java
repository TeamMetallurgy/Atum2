package com.teammetallurgy.atum.init;

import com.google.common.collect.Maps;
import com.google.common.collect.Queues;
import com.teammetallurgy.atum.client.particle.*;
import net.minecraft.client.particle.Particle;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Queue;

@SideOnly(Side.CLIENT)
public class AtumParticles {
    private static final Map<String, IAtumParticleFactory> particleTypes = Maps.newHashMap();
    private static final Queue<Particle> queue = Queues.newArrayDeque();

    public AtumParticles() {
        this.register();
    }

    public void register() {
        this.registerParticle(Types.HORUS.getParticleName(), new ParticleSwirl.Horus());
        this.registerParticle(Types.ISIS.getParticleName(), new ParticleSwirl.Isis());
        this.registerParticle(Types.LIGHT_SPARKLE.getParticleName(), new ParticleLightSparkle.Factory());
        this.registerParticle(Types.NUIT.getParticleName(), new ParticleSwirl.Nuit());
        this.registerParticle(Types.SETH.getParticleName(), new ParticleSeth.Factory());
        this.registerParticle(Types.SHU.getParticleName(), new ParticleSwirl.Shu());
        this.registerParticle(Types.TEFNUT.getParticleName(), new ParticleTefnut.Factory());
    }

    public void registerParticle(String name, IAtumParticleFactory factory) {
        particleTypes.put(name, factory);
    }

    @Nullable
    public Particle spawnEffectParticle(String particleName, World world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        IAtumParticleFactory factory = particleTypes.get(particleName);
        if (factory != null) {
            Particle particle = factory.createParticle(particleName, world, x, y, z, xSpeed, ySpeed, zSpeed);
            if (particle != null) {
                addEffect(particle);
                return particle;
            }
        }
        return null;
    }

    public void addEffect(Particle effect) {
        if (effect == null) return;
        queue.add(effect);
    }

    public enum Types {
        HORUS("horus"),
        ISIS("isis"),
        LIGHT_SPARKLE("light_sprakle"),
        NUIT("nuit"),
        SETH("seth"),
        SHU("shu"),
        TEFNUT("tefnut");

        private static final Map<String, Types> PARTICLES = Maps.newHashMap();
        private final String particleName;

        Types(String particleName) {
            this.particleName = particleName;
        }

        public String getParticleName() {
            return particleName;
        }

        @Nullable
        public static Types getParticleFromName(String name) {
            return PARTICLES.get(name);
        }

        static {
            for (Types types : Types.values()) {
                PARTICLES.put(types.particleName, types);
            }
        }
    }
}