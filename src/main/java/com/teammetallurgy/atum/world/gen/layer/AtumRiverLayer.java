package com.teammetallurgy.atum.world.gen.layer;

import com.teammetallurgy.atum.init.AtumBiomes;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.layer.traits.ICastleTransformer;

import javax.annotation.Nonnull;

public enum AtumRiverLayer implements ICastleTransformer {
    INSTANCE;

    public static final int DRIED_RIVER_ID = WorldGenRegistries.BIOME.getId(WorldGenRegistries.BIOME.getValueForKey(AtumBiomes.DRIED_RIVER));

    @Override
    public int apply(@Nonnull INoiseRandom context, int north, int west, int south, int east, int center) {
        int riverFilter = riverFilter(center);
        return riverFilter == riverFilter(east) && riverFilter == riverFilter(north) && riverFilter == riverFilter(west) && riverFilter == riverFilter(south) ? -1 : DRIED_RIVER_ID;
    }

    private static int riverFilter(int i) {
        return i >= 2 ? 2 + (i & 1) : i;
    }
}