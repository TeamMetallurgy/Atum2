package com.teammetallurgy.atum.init;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.api.God;
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
import net.minecraftforge.registries.ObjectHolder;

import static com.teammetallurgy.atum.misc.AtumRegistry.registerGodFlame;
import static com.teammetallurgy.atum.misc.AtumRegistry.registerParticle;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(value = Atum.MOD_ID)
public class AtumParticles {
    public static final SimpleParticleType ANUBIS = registerParticle("anubis");
    public static final SimpleParticleType ANUBIS_SKULL = registerParticle("anubis_skull");
    public static final SimpleParticleType EMPTY = registerParticle("empty");
    public static final SimpleParticleType GAS = registerParticle("gas");
    public static final SimpleParticleType GEB = registerParticle("geb");
    public static final SimpleParticleType HORUS = registerParticle("horus");
    public static final SimpleParticleType ISIS = registerParticle("isis");
    public static final SimpleParticleType LIGHT_SPARKLE = registerParticle("light_sparkle");
    public static final SimpleParticleType MONTU = registerParticle("montu");
    public static final SimpleParticleType NEBU_FLAME = registerParticle("nebu_flame");
    public static final SimpleParticleType NUIT_BLACK = registerParticle("nuit_black");
    public static final SimpleParticleType NUIT_WHITE = registerParticle("nuit_white");
    public static final SimpleParticleType RA_FIRE = registerParticle("ra_fire");
    public static final SimpleParticleType SETH = registerParticle("seth");
    public static final SimpleParticleType SHU = registerParticle("shu");
    public static final SimpleParticleType TAR = registerParticle("tar");
    public static final SimpleParticleType TEFNUT = registerParticle("tefnut");
    public static final SimpleParticleType TEFNUT_DROP = registerParticle("tefnut_drop");

    //God Flames
    public static final SimpleParticleType ANPUT_FLAME = registerGodFlame("anput_flame", God.ANPUT);
    public static final SimpleParticleType ANUBIS_FLAME = registerGodFlame("anubis_flame", God.ANUBIS);
    public static final SimpleParticleType ATEM_FLAME = registerGodFlame("atem_flame", God.ATEM);
    public static final SimpleParticleType GEB_FLAME = registerGodFlame("geb_flame", God.GEB);
    public static final SimpleParticleType HORUS_FLAME = registerGodFlame("horus_flame", God.HORUS);
    public static final SimpleParticleType ISIS_FLAME = registerGodFlame("isis_flame", God.ISIS);
    public static final SimpleParticleType MONTU_FLAME = registerGodFlame("montu_flame", God.MONTU);
    public static final SimpleParticleType NEPTHYS_FLAME = registerGodFlame("nepthys_flame", God.NEPTHYS);
    public static final SimpleParticleType NUIT_FLAME = registerGodFlame("nuit_flame", God.NUIT);
    public static final SimpleParticleType OSIRIS_FLAME = registerGodFlame("osiris_flame", God.OSIRIS);
    public static final SimpleParticleType PTAH_FLAME = registerGodFlame("ptah_flame", God.PTAH);
    public static final SimpleParticleType RA_FLAME = registerGodFlame("ra_flame", God.RA);
    public static final SimpleParticleType SETH_FLAME = registerGodFlame("seth_flame", God.SETH);
    public static final SimpleParticleType SHU_FLAME = registerGodFlame("shu_flame", God.SHU);
    public static final SimpleParticleType TEFNUT_FLAME = registerGodFlame("tefnut_flame", God.TEFNUT);

    @SubscribeEvent
    public static void registerParticleFactories(ParticleFactoryRegisterEvent event) {
        registerFactory(ANUBIS, SwirlParticle.Anubis::new);
        registerFactory(ANUBIS_SKULL, SwirlParticle.AnubisSkull::new);
        registerFactory(GAS, SwirlParticle.Gas::new);
        registerFactory(GEB, SwirlParticle.Geb::new);
        registerFactory(HORUS, SwirlParticle.Horus::new);
        registerFactory(ISIS, SwirlParticle.Isis::new);
        registerFactory(LIGHT_SPARKLE, LightSparkleParticle.Factory::new);
        registerFactory(MONTU, MontuParticle.Factory::new);
        registerFactory(NEBU_FLAME, NebuFlameParticle.Nebu::new);
        registerFactory(NUIT_BLACK, SwirlParticle.NuitBlack::new);
        registerFactory(NUIT_WHITE, SwirlParticle.NuitWhite::new);
        registerFactory(RA_FIRE, RaFireParticle.Factory::new);
        registerFactory(SETH, DropParticle.Seth::new);
        registerFactory(SHU, SwirlParticle.Shu::new);
        registerFactory(TAR, DropParticle.Tar::new);
        registerFactory(TEFNUT, TefnutParticle.Factory::new);
        registerFactory(TEFNUT_DROP, DropParticle.Tefnut::new);

        //God Flames
        registerFactory(ANPUT_FLAME, NebuFlameParticle.Nebu::new);
        registerFactory(ANUBIS_FLAME, NebuFlameParticle.Nebu::new);
        registerFactory(ATEM_FLAME, NebuFlameParticle.Nebu::new);
        registerFactory(GEB_FLAME, NebuFlameParticle.Nebu::new);
        registerFactory(HORUS_FLAME, NebuFlameParticle.Nebu::new);
        registerFactory(ISIS_FLAME, NebuFlameParticle.Nebu::new);
        registerFactory(MONTU_FLAME, NebuFlameParticle.Nebu::new);
        registerFactory(NEPTHYS_FLAME, NebuFlameParticle.Nebu::new);
        registerFactory(NUIT_FLAME, NebuFlameParticle.Nebu::new);
        registerFactory(OSIRIS_FLAME, NebuFlameParticle.Nebu::new);
        registerFactory(PTAH_FLAME, NebuFlameParticle.Nebu::new);
        registerFactory(RA_FLAME, NebuFlameParticle.Nebu::new);
        registerFactory(SETH_FLAME, NebuFlameParticle.Nebu::new);
        registerFactory(SHU_FLAME, NebuFlameParticle.Nebu::new);
        registerFactory(TEFNUT_FLAME, NebuFlameParticle.Nebu::new);
    }

    public static <T extends ParticleOptions> void registerFactory(ParticleType<T> particleType, ParticleEngine.SpriteParticleRegistration<T> particleMetaFactory) {
        ParticleEngine particleManager = Minecraft.getInstance().particleEngine;
        particleManager.register(particleType, particleMetaFactory);
    }
}