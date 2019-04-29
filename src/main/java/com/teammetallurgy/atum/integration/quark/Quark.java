package com.teammetallurgy.atum.integration.quark;

import com.teammetallurgy.atum.integration.IModIntegration;
import com.teammetallurgy.atum.integration.IntegrationHandler;
import vazkii.quark.base.module.ModuleLoader;

/**
 * Created by Weissmoon on 4/29/19.
 */
public class Quark implements IModIntegration {
    public static final String QUARK_ID = "quark";
    public void preInit() {
        IntegrationHandler.INSTANCE.quarkChests = ModuleLoader.isFeatureEnabled(vazkii.quark.decoration.feature.VariedChests.class);
    }
}
