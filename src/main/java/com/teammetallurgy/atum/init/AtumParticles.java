package com.teammetallurgy.atum.init;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.api.God;
import com.teammetallurgy.atum.blocks.lighting.AtumTorchBlock;
import com.teammetallurgy.atum.client.particle.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AtumParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_DEFERRED = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Atum.MOD_ID);
    public static final RegistryObject<SimpleParticleType> ANUBIS = registerParticle("anubis");
    public static final RegistryObject<SimpleParticleType> ANUBIS_SKULL = registerParticle("anubis_skull");
    public static final RegistryObject<SimpleParticleType> EMPTY = registerParticle("empty");
    public static final RegistryObject<SimpleParticleType> GAS = registerParticle("gas");
    public static final RegistryObject<SimpleParticleType> GEB = registerParticle("geb");
    public static final RegistryObject<SimpleParticleType> HORUS = registerParticle("horus");
    public static final RegistryObject<SimpleParticleType> ISIS = registerParticle("isis");
    public static final RegistryObject<SimpleParticleType> LIGHT_SPARKLE = registerParticle("light_sparkle");
    public static final RegistryObject<SimpleParticleType> MONTU = registerParticle("montu");
    public static final RegistryObject<SimpleParticleType> NEBU_FLAME = registerParticle("nebu_flame");
    public static final RegistryObject<SimpleParticleType> NUIT_BLACK = registerParticle("nuit_black");
    public static final RegistryObject<SimpleParticleType> NUIT_WHITE = registerParticle("nuit_white");
    public static final RegistryObject<SimpleParticleType> RA_FIRE = registerParticle("ra_fire");
    public static final RegistryObject<SimpleParticleType> SETH = registerParticle("seth");
    public static final RegistryObject<SimpleParticleType> SHU = registerParticle("shu");
    public static final RegistryObject<SimpleParticleType> TAR = registerParticle("tar");
    public static final RegistryObject<SimpleParticleType> TEFNUT = registerParticle("tefnut");
    public static final RegistryObject<SimpleParticleType> TEFNUT_DROP = registerParticle("tefnut_drop");

    //God Flames
    public static final RegistryObject<SimpleParticleType> ANPUT_FLAME = registerGodFlame("anput_flame", God.ANPUT);
    public static final RegistryObject<SimpleParticleType> ANUBIS_FLAME = registerGodFlame("anubis_flame", God.ANUBIS);
    public static final RegistryObject<SimpleParticleType> ATEM_FLAME = registerGodFlame("atem_flame", God.ATEM);
    public static final RegistryObject<SimpleParticleType> GEB_FLAME = registerGodFlame("geb_flame", God.GEB);
    public static final RegistryObject<SimpleParticleType> HORUS_FLAME = registerGodFlame("horus_flame", God.HORUS);
    public static final RegistryObject<SimpleParticleType> ISIS_FLAME = registerGodFlame("isis_flame", God.ISIS);
    public static final RegistryObject<SimpleParticleType> MONTU_FLAME = registerGodFlame("montu_flame", God.MONTU);
    public static final RegistryObject<SimpleParticleType> NEPTHYS_FLAME = registerGodFlame("nepthys_flame", God.NEPTHYS);
    public static final RegistryObject<SimpleParticleType> NUIT_FLAME = registerGodFlame("nuit_flame", God.NUIT);
    public static final RegistryObject<SimpleParticleType> OSIRIS_FLAME = registerGodFlame("osiris_flame", God.OSIRIS);
    public static final RegistryObject<SimpleParticleType> PTAH_FLAME = registerGodFlame("ptah_flame", God.PTAH);
    public static final RegistryObject<SimpleParticleType> RA_FLAME = registerGodFlame("ra_flame", God.RA);
    public static final RegistryObject<SimpleParticleType> SETH_FLAME = registerGodFlame("seth_flame", God.SETH);
    public static final RegistryObject<SimpleParticleType> SHU_FLAME = registerGodFlame("shu_flame", God.SHU);
    public static final RegistryObject<SimpleParticleType> TEFNUT_FLAME = registerGodFlame("tefnut_flame", God.TEFNUT);

    @SubscribeEvent
    public static void registerParticleFactories(ParticleFactoryRegisterEvent event) {
        registerFactory(ANUBIS.get(), SwirlParticle.Anubis::new);
        registerFactory(ANUBIS_SKULL.get(), SwirlParticle.AnubisSkull::new);
        registerFactory(GAS.get(), SwirlParticle.Gas::new);
        registerFactory(GEB.get(), SwirlParticle.Geb::new);
        registerFactory(HORUS.get(), SwirlParticle.Horus::new);
        registerFactory(ISIS.get(), SwirlParticle.Isis::new);
        registerFactory(LIGHT_SPARKLE.get(), LightSparkleParticle.Factory::new);
        registerFactory(MONTU.get(), MontuParticle.Factory::new);
        registerFactory(NEBU_FLAME.get(), NebuFlameParticle.Nebu::new);
        registerFactory(NUIT_BLACK.get(), SwirlParticle.NuitBlack::new);
        registerFactory(NUIT_WHITE.get(), SwirlParticle.NuitWhite::new);
        registerFactory(RA_FIRE.get(), RaFireParticle.Factory::new);
        registerFactory(SETH.get(), DropParticle.Seth::new);
        registerFactory(SHU.get(), SwirlParticle.Shu::new);
        registerFactory(TAR.get(), DropParticle.Tar::new);
        registerFactory(TEFNUT.get(), TefnutParticle.Factory::new);
        registerFactory(TEFNUT_DROP.get(), DropParticle.Tefnut::new);

        //God Flames
        registerFactory(ANPUT_FLAME.get(), NebuFlameParticle.Nebu::new);
        registerFactory(ANUBIS_FLAME.get(), NebuFlameParticle.Nebu::new);
        registerFactory(ATEM_FLAME.get(), NebuFlameParticle.Nebu::new);
        registerFactory(GEB_FLAME.get(), NebuFlameParticle.Nebu::new);
        registerFactory(HORUS_FLAME.get(), NebuFlameParticle.Nebu::new);
        registerFactory(ISIS_FLAME.get(), NebuFlameParticle.Nebu::new);
        registerFactory(MONTU_FLAME.get(), NebuFlameParticle.Nebu::new);
        registerFactory(NEPTHYS_FLAME.get(), NebuFlameParticle.Nebu::new);
        registerFactory(NUIT_FLAME.get(), NebuFlameParticle.Nebu::new);
        registerFactory(OSIRIS_FLAME.get(), NebuFlameParticle.Nebu::new);
        registerFactory(PTAH_FLAME.get(), NebuFlameParticle.Nebu::new);
        registerFactory(RA_FLAME.get(), NebuFlameParticle.Nebu::new);
        registerFactory(SETH_FLAME.get(), NebuFlameParticle.Nebu::new);
        registerFactory(SHU_FLAME.get(), NebuFlameParticle.Nebu::new);
        registerFactory(TEFNUT_FLAME.get(), NebuFlameParticle.Nebu::new);
    }

    public static <T extends ParticleOptions> void registerFactory(ParticleType<T> particleType, ParticleEngine.SpriteParticleRegistration<T> particleMetaFactory) {
        ParticleEngine particleManager = Minecraft.getInstance().particleEngine;
        particleManager.register(particleType, particleMetaFactory);
    }

    /**
     * Registers a particle
     *
     * @param name The name to register the sound with
     * @return The Sound that was registered
     */
    public static RegistryObject<SimpleParticleType> registerParticle(String name) {
        return PARTICLE_DEFERRED.register(name, () -> new SimpleParticleType(false));
    }

    public static RegistryObject<SimpleParticleType> registerGodFlame(String name, God god) {
        RegistryObject<SimpleParticleType> particleType = registerParticle(name);
        AtumTorchBlock.GOD_FLAMES.put(god, particleType.get());
        AtumTorchBlock.GODS.put(particleType.get(), god);
        return particleType;
    }
}