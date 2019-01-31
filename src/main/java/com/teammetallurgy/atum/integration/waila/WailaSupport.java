package com.teammetallurgy.atum.integration.waila;

import com.teammetallurgy.atum.blocks.base.BlockAtumDoor;
import com.teammetallurgy.atum.blocks.vegetation.BlockDate;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.IWailaRegistrar;
import mcp.mobius.waila.api.WailaPlugin;

@WailaPlugin
public class WailaSupport implements IWailaPlugin {

    public WailaSupport() {
    }

    @Override
    public void register(IWailaRegistrar registrar) {
        IWailaDataProvider provider = new WailaHUDHandler();
        registrar.registerStackProvider(provider, BlockAtumDoor.class);
        registrar.registerBodyProvider(provider, BlockDate.class);
    }
}