package com.teammetallurgy.atum.init;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.api.God;
import com.teammetallurgy.atum.blocks.lighting.AtumTorchBlock;
import com.teammetallurgy.atum.client.particle.*;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AtumParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_DEFERRED = DeferredRegister.create(Registries.PARTICLE_TYPE, Atum.MOD_ID);
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> ANUBIS = registerParticle("anubis");
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> ANUBIS_SKULL = registerParticle("anubis_skull");
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> EMPTY = registerParticle("empty");
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> GAS = registerParticle("gas");
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> GEB = registerParticle("geb");
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> HORUS = registerParticle("horus");
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> ISIS = registerParticle("isis");
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> LIGHT_SPARKLE = registerParticle("light_sparkle");
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> MONTU = registerParticle("montu");
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> NEBU_FLAME = registerParticle("nebu_flame");
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> NUIT_BLACK = registerParticle("nuit_black");
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> NUIT_WHITE = registerParticle("nuit_white");
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> RA_FIRE = registerParticle("ra_fire");
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SETH = registerParticle("seth");
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SHU = registerParticle("shu");
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> TAR = registerParticle("tar");
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> TEFNUT = registerParticle("tefnut");
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> TEFNUT_DROP = registerParticle("tefnut_drop");
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SAND_AIR = registerParticle("sand_air");

    //God Flames
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> ANPUT_FLAME = registerGodFlame("anput_flame", God.ANPUT);
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> ANUBIS_FLAME = registerGodFlame("anubis_flame", God.ANUBIS);
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> ATEM_FLAME = registerGodFlame("atem_flame", God.ATEM);
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> GEB_FLAME = registerGodFlame("geb_flame", God.GEB);
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> HORUS_FLAME = registerGodFlame("horus_flame", God.HORUS);
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> ISIS_FLAME = registerGodFlame("isis_flame", God.ISIS);
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> MONTU_FLAME = registerGodFlame("montu_flame", God.MONTU);
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> NEPTHYS_FLAME = registerGodFlame("nepthys_flame", God.NEPTHYS);
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> NUIT_FLAME = registerGodFlame("nuit_flame", God.NUIT);
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> OSIRIS_FLAME = registerGodFlame("osiris_flame", God.OSIRIS);
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> PTAH_FLAME = registerGodFlame("ptah_flame", God.PTAH);
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> RA_FLAME = registerGodFlame("ra_flame", God.RA);
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SETH_FLAME = registerGodFlame("seth_flame", God.SETH);
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SHU_FLAME = registerGodFlame("shu_flame", God.SHU);
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> TEFNUT_FLAME = registerGodFlame("tefnut_flame", God.TEFNUT);

    @SubscribeEvent
    public static void registerParticleFactories(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ANUBIS.get(), SwirlParticle.Anubis::new);
        event.registerSpriteSet(ANUBIS_SKULL.get(), SwirlParticle.AnubisSkull::new);
        event.registerSpriteSet(GAS.get(), SwirlParticle.Gas::new);
        event.registerSpriteSet(GEB.get(), SwirlParticle.Geb::new);
        event.registerSpriteSet(HORUS.get(), SwirlParticle.Horus::new);
        event.registerSpriteSet(ISIS.get(), SwirlParticle.Isis::new);
        event.registerSpriteSet(LIGHT_SPARKLE.get(), LightSparkleParticle.Factory::new);
        event.registerSpriteSet(MONTU.get(), MontuParticle.Factory::new);
        event.registerSpriteSet(NEBU_FLAME.get(), NebuFlameParticle.Nebu::new);
        event.registerSpriteSet(NUIT_BLACK.get(), SwirlParticle.NuitBlack::new);
        event.registerSpriteSet(NUIT_WHITE.get(), SwirlParticle.NuitWhite::new);
        event.registerSpriteSet(RA_FIRE.get(), RaFireParticle.Factory::new);
        event.registerSpriteSet(SETH.get(), DropParticle.Seth::new);
        event.registerSpriteSet(SHU.get(), SwirlParticle.Shu::new);
        event.registerSpriteSet(TAR.get(), DropParticle.Tar::new);
        event.registerSpriteSet(TEFNUT.get(), TefnutParticle.Factory::new);
        event.registerSpriteSet(TEFNUT_DROP.get(), DropParticle.Tefnut::new);
        event.registerSpriteSet(SAND_AIR.get(), InAirParticle.SandAirParticle::new);

        //God Flames
        event.registerSpriteSet(ANPUT_FLAME.get(), NebuFlameParticle.Nebu::new);
        event.registerSpriteSet(ANUBIS_FLAME.get(), NebuFlameParticle.Nebu::new);
        event.registerSpriteSet(ATEM_FLAME.get(), NebuFlameParticle.Nebu::new);
        event.registerSpriteSet(GEB_FLAME.get(), NebuFlameParticle.Nebu::new);
        event.registerSpriteSet(HORUS_FLAME.get(), NebuFlameParticle.Nebu::new);
        event.registerSpriteSet(ISIS_FLAME.get(), NebuFlameParticle.Nebu::new);
        event.registerSpriteSet(MONTU_FLAME.get(), NebuFlameParticle.Nebu::new);
        event.registerSpriteSet(NEPTHYS_FLAME.get(), NebuFlameParticle.Nebu::new);
        event.registerSpriteSet(NUIT_FLAME.get(), NebuFlameParticle.Nebu::new);
        event.registerSpriteSet(OSIRIS_FLAME.get(), NebuFlameParticle.Nebu::new);
        event.registerSpriteSet(PTAH_FLAME.get(), NebuFlameParticle.Nebu::new);
        event.registerSpriteSet(RA_FLAME.get(), NebuFlameParticle.Nebu::new);
        event.registerSpriteSet(SETH_FLAME.get(), NebuFlameParticle.Nebu::new);
        event.registerSpriteSet(SHU_FLAME.get(), NebuFlameParticle.Nebu::new);
        event.registerSpriteSet(TEFNUT_FLAME.get(), NebuFlameParticle.Nebu::new);
    }

    /**
     * Registers a particle
     *
     * @param name The name to register the sound with
     * @return The Sound that was registered
     */
    public static DeferredHolder<ParticleType<?>, SimpleParticleType> registerParticle(String name) {
        return PARTICLE_DEFERRED.register(name, () -> new SimpleParticleType(false));
    }

    public static DeferredHolder<ParticleType<?>, SimpleParticleType> registerGodFlame(String name, God god) {
        DeferredHolder<ParticleType<?>, SimpleParticleType> particleType = registerParticle(name);
        AtumTorchBlock.GOD_FLAMES.put(god, particleType);
        AtumTorchBlock.GODS.put(particleType, god);
        return particleType;
    }
}