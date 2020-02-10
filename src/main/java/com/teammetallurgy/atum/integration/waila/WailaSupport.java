package com.teammetallurgy.atum.integration.waila;

import com.teammetallurgy.atum.blocks.base.DoorAtumBlock;
import com.teammetallurgy.atum.blocks.vegetation.BlockDate;
import com.teammetallurgy.atum.utils.Constants;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.TooltipPosition;
import mcp.mobius.waila.api.WailaPlugin;

@WailaPlugin(value = Constants.MOD_ID)
public class WailaSupport implements IWailaPlugin {

    public WailaSupport() {
    }

    @Override
    public void register(IRegistrar registrar) {
        registrar.registerComponentProvider(WailaHUDHandler.INSTANCE, TooltipPosition.HEAD, DoorAtumBlock.class);
        registrar.registerComponentProvider(WailaHUDHandler.INSTANCE, TooltipPosition.BODY, BlockDate.class);
    }
}