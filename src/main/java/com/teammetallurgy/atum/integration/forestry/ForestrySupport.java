package com.teammetallurgy.atum.integration.forestry;

import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.integration.IModIntegration;
import forestry.api.apiculture.hives.HiveManager;

public class ForestrySupport implements IModIntegration {

    @Override
    public void preInit() {
        HiveManager.genHelper.ground(AtumBlocks.SAND);


    }
}