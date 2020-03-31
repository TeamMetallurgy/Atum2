package com.teammetallurgy.atum.init;

import com.teammetallurgy.atum.client.particle.*;
import com.teammetallurgy.atum.utils.Constants;
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

import static com.teammetallurgy.atum.utils.AtumRegistry.registerParticle;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(value = Constants.MOD_ID)
public class AtumParticles {
    public static final BasicParticleType ANUBIS = registerParticle("anubis");
    public static final BasicParticleType ANUBIS_SKULL = registerParticle("anubis_skull");
    public static final BasicParticleType GAS = registerParticle("gas");
    public static final BasicParticleType GEB = registerParticle("geb");
    public static final BasicParticleType HORUS = registerParticle("horus");
    public static final BasicParticleType ISIS = registerParticle("isis");
    public static final BasicParticleType LIGHT_SPARKLE = registerParticle("light_sparkle");
    public static final BasicParticleType MONTU = registerParticle("montu");
    public static final BasicParticleType NUIT_BLACK = registerParticle("nuit_black");
    public static final BasicParticleType NUIT_WHITE = registerParticle("nuit_white");
    public static final BasicParticleType RA_FIRE = registerParticle("ra_fire");
    public static final BasicParticleType SETH = registerParticle("seth");
    public static final BasicParticleType SHU = registerParticle("shu");
    public static final BasicParticleType TAR = registerParticle("tar");
    public static final BasicParticleType TEFNUT = registerParticle("tefnut");
    public static final BasicParticleType TEFNUT_DROP = registerParticle("tefnut_drop");

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
        registerFactory(NUIT_BLACK, SwirlParticle.NuitBlack::new);
        registerFactory(NUIT_WHITE, SwirlParticle.NuitWhite::new);
        registerFactory(RA_FIRE, RaFireParticle.Factory::new);
        registerFactory(SETH, DropParticle.Seth::new);
        registerFactory(SHU, SwirlParticle.Shu::new);
        registerFactory(TAR, DropParticle.Tar::new);
        registerFactory(TEFNUT, TefnutParticle.Factory::new);
        registerFactory(TEFNUT_DROP, DropParticle.Tefnut::new);
    }

    public static <T extends IParticleData> void registerFactory(ParticleType<T> particleType, ParticleManager.IParticleMetaFactory<T> particleMetaFactory) {
        ParticleManager particleManager = Minecraft.getInstance().particles;
        particleManager.registerFactory(particleType, particleMetaFactory);
    }
}