package com.teammetallurgy.atum.world;

import com.teammetallurgy.atum.handler.AtumConfig;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.world.DimensionType;
import net.minecraftforge.common.DimensionManager;

public class AtumDimension {
    public static final DimensionType ATUM = DimensionType.register(Constants.MOD_ID, "_" + Constants.MOD_ID, AtumConfig.DIMENSION_ID, WorldProviderAtum.class, false);

    public static void register() {
        DimensionManager.registerDimension(AtumConfig.DIMENSION_ID, ATUM);
    }
}