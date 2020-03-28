package com.teammetallurgy.atum.world.gen.layer;

import com.teammetallurgy.atum.init.AtumBiomes;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.layer.traits.ICastleTransformer;

import javax.annotation.Nonnull;

public enum AtumRiverLayer implements ICastleTransformer {
    INSTANCE;

    @Override
    public int apply(@Nonnull INoiseRandom noiseRandom, int i, int i1, int i2, int i3, int i4) {
        int riverFilter = riverFilter(i4);
        return riverFilter == riverFilter(i3) && riverFilter == riverFilter(i) && riverFilter == riverFilter(i1) && riverFilter == riverFilter(i2) ? -1 : Registry.BIOME.getId(AtumBiomes.DRIED_RIVER);
    }

    private static int riverFilter(int i) {
        return i >= 2 ? 2 + (i & 1) : i;
    }
}