package com.teammetallurgy.atum.init;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Queues;
import com.teammetallurgy.atum.client.TextureManagerParticles;
import com.teammetallurgy.atum.client.particle.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEmitter;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.util.ReportedException;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.*;

@OnlyIn(Dist.CLIENT)
public class AtumParticles {
    private static final Map<String, IAtumParticleFactory> particleTypes = Maps.newHashMap();
    private final ArrayDeque<Particle>[][] fxLayers = new ArrayDeque[4][];
    private final Queue<ParticleEmitter> particleEmitters = Queues.newArrayDeque();
    private final Queue<Particle> queue = Queues.newArrayDeque();
    private World world = Minecraft.getMinecraft().world;

    public AtumParticles() {
        for (int i = 0; i < 4; ++i) {
            this.fxLayers[i] = new ArrayDeque[2];

            for (int j = 0; j < 2; ++j) {
                this.fxLayers[i][j] = Queues.newArrayDeque();
            }
        }

        this.register();
    }

    public void register() {
        this.registerParticle(Types.ANUBIS.getParticleName(), new ParticleSwirl.Anubis());
        this.registerParticle(Types.ANUBIS_SKULL.getParticleName(), new ParticleSwirl.AnubisSkull());
        this.registerParticle(Types.GAS.getParticleName(), new ParticleSwirl.Gas());
        this.registerParticle(Types.GEB.getParticleName(), new ParticleSwirl.Geb());
        this.registerParticle(Types.HORUS.getParticleName(), new ParticleSwirl.Horus());
        this.registerParticle(Types.ISIS.getParticleName(), new ParticleSwirl.Isis());
        this.registerParticle(Types.LIGHT_SPARKLE.getParticleName(), new ParticleLightSparkle.Factory());
        this.registerParticle(Types.MONTU.getParticleName(), new ParticleMontu.Factory());
        this.registerParticle(Types.NUIT_BLACK.getParticleName(), new ParticleSwirl.NuitBlack());
        this.registerParticle(Types.NUIT_WHITE.getParticleName(), new ParticleSwirl.NuitWhite());
        this.registerParticle(Types.SETH.getParticleName(), new ParticleDrop.Seth());
        this.registerParticle(Types.SHU.getParticleName(), new ParticleSwirl.Shu());
        this.registerParticle(Types.RA_FIRE.getParticleName(), new ParticleRaFire.Factory());
        this.registerParticle(Types.TEFNUT.getParticleName(), new ParticleTefnut.Factory());
        this.registerParticle(Types.TEFNUT_DROP.getParticleName(), new ParticleDrop.Tefnut());
        this.registerParticle(Types.TAR.getParticleName(), new ParticleDrop.Tar());
    }

    private void registerParticle(String name, IAtumParticleFactory factory) {
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

    public void updateEffects() {
        for (int i = 0; i < 4; ++i) {
            this.updateEffectLayer(i);
        }

        if (!this.particleEmitters.isEmpty()) {
            List<ParticleEmitter> list = Lists.newArrayList();

            for (ParticleEmitter particleemitter : this.particleEmitters) {
                particleemitter.onUpdate();

                if (!particleemitter.isAlive()) {
                    list.add(particleemitter);
                }
            }

            this.particleEmitters.removeAll(list);
        }

        if (!this.queue.isEmpty()) {
            for (Particle particle = this.queue.poll(); particle != null; particle = this.queue.poll()) {
                int j = particle.getFXLayer();
                int k = particle.shouldDisableDepth() ? 0 : 1;

                if (this.fxLayers[j][k].size() >= 16384) {
                    this.fxLayers[j][k].removeFirst();
                }

                this.fxLayers[j][k].add(particle);
            }
        }
    }

    private void updateEffectLayer(int layer) {
        this.world.profiler.startSection(String.valueOf(layer));

        for (int i = 0; i < 2; ++i) {
            this.world.profiler.startSection(String.valueOf(i));
            this.tickParticleList(this.fxLayers[layer][i]);
            this.world.profiler.endSection();
        }

        this.world.profiler.endSection();
    }

    private void tickParticleList(Queue<Particle> particleQueue) {
        if (!particleQueue.isEmpty()) {
            Iterator<Particle> iterator = particleQueue.iterator();

            while (iterator.hasNext()) {
                Particle particle = iterator.next();
                this.tickParticle(particle);

                if (!particle.isAlive()) {
                    iterator.remove();
                }
            }
        }
    }

    private void tickParticle(final Particle particle) {
        try {
            particle.onUpdate();
        } catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Ticking Particle");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Particle being ticked");
            final int i = particle.getFXLayer();
            crashreportcategory.addDetail("Particle", particle::toString);
            crashreportcategory.addDetail("Particle Type", () -> {
                if (i == 0) {
                    return "MISC_TEXTURE";
                } else if (i == 1) {
                    return "TERRAIN_TEXTURE";
                } else {
                    return i == 3 ? "ENTITY_PARTICLE_TEXTURE" : "Unknown - " + i;
                }
            });
            throw new ReportedException(crashreport);
        }
    }

    public void renderParticles(Entity entity, float partialTicks) {
        float f = ActiveRenderInfo.getRotationX();
        float f1 = ActiveRenderInfo.getRotationZ();
        float f2 = ActiveRenderInfo.getRotationYZ();
        float f3 = ActiveRenderInfo.getRotationXY();
        float f4 = ActiveRenderInfo.getRotationXZ();
        Particle.interpPosX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double) partialTicks;
        Particle.interpPosY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double) partialTicks;
        Particle.interpPosZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double) partialTicks;
        Particle.cameraViewDir = entity.getLook(partialTicks);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.alphaFunc(516, 0.003921569F);

        for (int i_nf = 0; i_nf < 3; ++i_nf) {
            final int i = i_nf;

            for (int j = 0; j < 2; ++j) {
                if (!this.fxLayers[i][j].isEmpty()) {
                    switch (j) {
                        case 0:
                            GlStateManager.depthMask(false);
                            break;
                        case 1:
                            GlStateManager.depthMask(true);
                    }

                    switch (i) {
                        case 0:
                        default:
                            TextureManagerParticles.INSTANCE.bindTextureMap();
                            break;
                        case 1:
                            Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
                    }

                    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                    Tessellator tessellator = Tessellator.getInstance();
                    BufferBuilder bufferbuilder = tessellator.getBuffer();
                    bufferbuilder.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);

                    for (final Particle particle : this.fxLayers[i][j]) {
                        try {
                            particle.renderParticle(bufferbuilder, entity, partialTicks, f, f4, f1, f2, f3);
                        } catch (Throwable throwable) {
                            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Rendering Particle");
                            CrashReportCategory crashreportcategory = crashreport.makeCategory("Particle being rendered");
                            crashreportcategory.addDetail("Particle", particle::toString);
                            crashreportcategory.addDetail("Particle Type", () -> {
                                if (i == 0) {
                                    return "MISC_TEXTURE";
                                } else if (i == 1) {
                                    return "TERRAIN_TEXTURE";
                                } else {
                                    return i == 3 ? "ENTITY_PARTICLE_TEXTURE" : "Unknown - " + i;
                                }
                            });
                            throw new ReportedException(crashreport);
                        }
                    }
                    tessellator.draw();
                }
            }
        }
        GlStateManager.depthMask(true);
        GlStateManager.disableBlend();
        GlStateManager.alphaFunc(516, 0.1F);
    }

    public void renderLitParticles(Entity entity, float partialTick) {
        float f = 0.017453292F;
        float f1 = MathHelper.cos(entity.rotationYaw * f);
        float f2 = MathHelper.sin(entity.rotationYaw * f);
        float f3 = -f2 * MathHelper.sin(entity.rotationPitch * f);
        float f4 = f1 * MathHelper.sin(entity.rotationPitch * f);
        float f5 = MathHelper.cos(entity.rotationPitch * f);

        for (int i = 0; i < 2; ++i) {
            Queue<Particle> queue = this.fxLayers[3][i];
            if (!queue.isEmpty()) {
                Tessellator tessellator = Tessellator.getInstance();
                BufferBuilder bufferbuilder = tessellator.getBuffer();
                for (Particle particle : queue) {
                    particle.renderParticle(bufferbuilder, entity, partialTick, f1, f5, f2, f3, f4);
                }
            }
        }
    }

    public void clearEffects(@Nullable World world) {
        this.world = world;
        for (int i = 0; i < 4; ++i) {
            for (int j = 0; j < 2; ++j) {
                this.fxLayers[i][j].clear();
            }
        }
        this.particleEmitters.clear();
    }

    public enum Types {
        ANUBIS("anubis"),
        ANUBIS_DROP("anubis_drop"),
        ANUBIS_SKULL("anubis_skull"),
        GAS("gas"),
        GEB("geb"),
        HORUS("horus"),
        ISIS("isis"),
        LIGHT_SPARKLE("light_sprakle"),
        MONTU("montu"),
        NUIT_BLACK("nuit_black"),
        NUIT_WHITE("nuit_white"),
        RA_FIRE("ra_fire"),
        SETH("seth"),
        SHU("shu"),
        TEFNUT("tefnut"),
        TEFNUT_DROP("tefnut_drop"),
        TAR("tar");

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