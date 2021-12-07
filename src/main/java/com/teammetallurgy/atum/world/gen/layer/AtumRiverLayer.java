/*package com.teammetallurgy.atum.world.gen.layer;

import net.minecraft.world.level.newbiome.context.Context;
import net.minecraft.world.level.newbiome.layer.traits.CastleTransformer;

import javax.annotation.Nonnull;

public enum AtumRiverLayer implements CastleTransformer { //TODO?
    INSTANCE;

    @Override
    public int apply(@Nonnull Context context, int north, int west, int south, int east, int center) {
        int riverFilter = riverFilter(center);
        return riverFilter == riverFilter(east) && riverFilter == riverFilter(north) && riverFilter == riverFilter(west) && riverFilter == riverFilter(south) ? -1 : AtumLayerUtil.DRIED_RIVER_ID;
    }

    private static int riverFilter(int i) {
        return i >= 2 ? 2 + (i & 1) : i;
    }
}*/