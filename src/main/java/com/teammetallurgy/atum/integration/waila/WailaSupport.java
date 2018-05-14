package com.teammetallurgy.atum.integration.waila;

import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.IWailaRegistrar;
import mcp.mobius.waila.api.WailaPlugin;

@WailaPlugin
public class WailaSupport implements IWailaPlugin {

    public WailaSupport() {
    }

    @Override
    public void register(IWailaRegistrar registrar) {
        WailaHUDHandler.register();
    }
}