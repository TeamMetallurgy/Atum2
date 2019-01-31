package com.teammetallurgy.atum.integration.theoneprobe;

import com.teammetallurgy.atum.integration.IModIntegration;
import mcjty.theoneprobe.TheOneProbe;

public class TOPSupport implements IModIntegration {
    public static final String THE_ONE_PROBE = "theoneprobe";

    @Override
    public void preInit() {
        AtumProbeInfoProvider atumProbeInfoProvider = new AtumProbeInfoProvider();
        TheOneProbe.theOneProbeImp.registerProvider(atumProbeInfoProvider);
        TheOneProbe.theOneProbeImp.registerBlockDisplayOverride(atumProbeInfoProvider);
    }
}