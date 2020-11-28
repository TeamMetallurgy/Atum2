package com.teammetallurgy.atum.init;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.client.particle.*;
import com.teammetallurgy.atum.entity.undead.PharaohEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;
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
    public static final BasicParticleType ANUBIS = registerParticle("anubis");
    public static final BasicParticleType ANUBIS_SKULL = registerParticle("anubis_skull");
    public static final BasicParticleType EMPTY = registerParticle("empty");
    public static final BasicParticleType GAS = registerParticle("gas");
    public static final BasicParticleType GEB = registerParticle("geb");
    public static final BasicParticleType HORUS = registerParticle("horus");
    public static final BasicParticleType ISIS = registerParticle("isis");
    public static final BasicParticleType LIGHT_SPARKLE = registerParticle("light_sparkle");
    public static final BasicParticleType MONTU = registerParticle("montu");
    public static final BasicParticleType NEBU_FLAME = registerParticle("nebu_flame");
    public static final BasicParticleType NUIT_BLACK = registerParticle("nuit_black");
    public static final BasicParticleType NUIT_WHITE = registerParticle("nuit_white");
    public static final BasicParticleType RA_FIRE = registerParticle("ra_fire");
    public static final BasicParticleType SETH = registerParticle("seth");
    public static final BasicParticleType SHU = registerParticle("shu");
    public static final BasicParticleType TAR = registerParticle("tar");
    public static final BasicParticleType TEFNUT = registerParticle("tefnut");
    public static final BasicParticleType TEFNUT_DROP = registerParticle("tefnut_drop");

    //God Flames
    public static final BasicParticleType ANPUT_FLAME = registerGodFlame("anput_flame", PharaohEntity.God.ANPUT);
    public static final BasicParticleType ANUBIS_FLAME = registerGodFlame("anubis_flame", PharaohEntity.God.ANUBIS);
    public static final BasicParticleType ATEM_FLAME = registerGodFlame("atem_flame", PharaohEntity.God.ATEM);
    public static final BasicParticleType GEB_FLAME = registerGodFlame("geb_flame", PharaohEntity.God.GEB);
    public static final BasicParticleType HORUS_FLAME = registerGodFlame("horus_flame", PharaohEntity.God.HORUS);
    public static final BasicParticleType ISIS_FLAME = registerGodFlame("isis_flame", PharaohEntity.God.ISIS);
    public static final BasicParticleType MONTU_FLAME = registerGodFlame("montu_flame", PharaohEntity.God.MONTU);
    public static final BasicParticleType NEPTHYS_FLAME = registerGodFlame("nepthys_flame", PharaohEntity.God.NEPTHYS);
    public static final BasicParticleType NUIT_FLAME = registerGodFlame("nuit_flame", PharaohEntity.God.NUIT);
    public static final BasicParticleType OSIRIS_FLAME = registerGodFlame("osiris_flame", PharaohEntity.God.OSIRIS);
    public static final BasicParticleType PTAH_FLAME = registerGodFlame("ptah_flame", PharaohEntity.God.PTAH);
    public static final BasicParticleType RA_FLAME = registerGodFlame("ra_flame", PharaohEntity.God.RA);
    public static final BasicParticleType SETH_FLAME = registerGodFlame("seth_flame", PharaohEntity.God.SETH);
    public static final BasicParticleType SHU_FLAME = registerGodFlame("shu_flame", PharaohEntity.God.SHU);
    public static final BasicParticleType TEFNUT_FLAME = registerGodFlame("tefnut_flame", PharaohEntity.God.TEFNUT);

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

    public static <T extends IParticleData> void registerFactory(ParticleType<T> particleType, ParticleManager.IParticleMetaFactory<T> particleMetaFactory) {
        ParticleManager particleManager = Minecraft.getInstance().particles;
        particleManager.registerFactory(particleType, particleMetaFactory);
    }
}