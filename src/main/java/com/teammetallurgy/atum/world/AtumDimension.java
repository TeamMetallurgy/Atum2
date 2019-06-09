package com.teammetallurgy.atum.world;

import com.teammetallurgy.atum.utils.AtumConfig;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.world.DimensionType;
import net.minecraftforge.common.DimensionManager;

public class AtumDimension {
    public static final DimensionType ATUM = DimensionType.register(Constants.MOD_ID, "_" + Constants.MOD_ID, AtumConfig.DIMENSION_ID, WorldProviderAtum.class, false);

    public static void register() {
        if (!DimensionManager.isDimensionRegistered(AtumConfig.DIMENSION_ID)) {
            DimensionManager.registerDimension(AtumConfig.DIMENSION_ID, ATUM);
        } else {
            throw new DuplicateDimensionIDException("Dimension ID: " + AtumConfig.DIMENSION_ID + " is already being used by " + DimensionManager.getProviderType(AtumConfig.DIMENSION_ID).getName()
                                                    + ", please change Atum's dimension ID to a unique ID");
        }
    }

    public static class DuplicateDimensionIDException extends RuntimeException {
        DuplicateDimensionIDException(final String message) {
            super(message);
        }
    }
}