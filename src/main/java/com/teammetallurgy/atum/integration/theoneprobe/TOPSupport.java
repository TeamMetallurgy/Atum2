package com.teammetallurgy.atum.integration.theoneprobe;

import com.teammetallurgy.atum.integration.IModIntegration;
import mcjty.theoneprobe.api.ITheOneProbe;
import net.minecraftforge.fml.common.event.FMLInterModComms;

import java.util.function.Function;

public class TOPSupport implements IModIntegration, Function<ITheOneProbe, Void> {
    public static final String THE_ONE_PROBE = "theoneprobe";

    public static ITheOneProbe theOneProbe;

    @Override
    public void init() {
        FMLInterModComms.sendFunctionMessage(THE_ONE_PROBE, "getTheOneProbe", "com.teammetallurgy.atum.integration.theoneprobe.TOPSupport");
    }

    @Override
    public Void apply(ITheOneProbe theOneProbe) {
        TOPSupport.theOneProbe = theOneProbe;
        AtumProbeInfoProvider atumProbeInfoProvider = new AtumProbeInfoProvider();
        theOneProbe.registerProvider(atumProbeInfoProvider);
        theOneProbe.registerBlockDisplayOverride(atumProbeInfoProvider);
        return null;
    }

    /*public static IProbeConfig getProbeConfig() {
        return theOneProbe.createProbeConfig();
    }*/
}