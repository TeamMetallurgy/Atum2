package com.teammetallurgy.atum.world;

import com.teammetallurgy.atum.handler.AtumConfig;
import net.minecraft.world.DimensionType;
import net.minecraftforge.common.DimensionManager;

public class AtumDimension {
    public static DimensionType atum;

    public static void register() {
        atum = DimensionType.register("atum", "_atum", AtumConfig.DIMENSION_ID, WorldProviderAtum.class, false);
        DimensionManager.registerDimension(AtumConfig.DIMENSION_ID, atum);
    }
}