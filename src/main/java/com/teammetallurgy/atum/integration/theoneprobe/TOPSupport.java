/*package com.teammetallurgy.atum.integration.theoneprobe;

import com.teammetallurgy.atum.integration.IModIntegration;
import mcjty.theoneprobe.api.IProbeConfig;
import mcjty.theoneprobe.api.ITheOneProbe;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.function.Function;

public class TOPSupport implements IModIntegration, Function<ITheOneProbe, Void> {
    public static final String THE_ONE_PROBE = "theoneprobe";

    private static ITheOneProbe theOneProbe;

    @Override
    public void setup() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::sendIMC);
    }

    public void sendIMC(InterModEnqueueEvent event) {
        InterModComms.sendTo(THE_ONE_PROBE, "getTheOneProbe", TOPSupport::new);
    }

    @Override
    public Void apply(ITheOneProbe theOneProbe) {
        TOPSupport.theOneProbe = theOneProbe;
        AtumProbeInfoProvider atumProbeInfoProvider = new AtumProbeInfoProvider();
        theOneProbe.registerBlockDisplayOverride(atumProbeInfoProvider);
        return null;
    }

    static IProbeConfig getProbeConfig() {
        return theOneProbe.createProbeConfig();
    }
}*/