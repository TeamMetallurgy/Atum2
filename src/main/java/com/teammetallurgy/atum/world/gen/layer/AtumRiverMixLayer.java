package com.teammetallurgy.atum.world.gen.layer;

import com.teammetallurgy.atum.init.AtumBiomes;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.area.IArea;
import net.minecraft.world.gen.layer.traits.IAreaTransformer2;
import net.minecraft.world.gen.layer.traits.IDimOffset0Transformer;

import javax.annotation.Nonnull;

public enum AtumRiverMixLayer implements IAreaTransformer2, IDimOffset0Transformer {
    INSTANCE;

    private static final int DRIED_RIVER = Registry.BIOME.getId(AtumBiomes.DRIED_RIVER);

    @Override
    public int apply(@Nonnull INoiseRandom noiseRandom, IArea areaX, IArea areaY, int areaWidth, int areaHeight) {
        int i = areaX.getValue(this.func_215721_a(areaWidth), this.func_215722_b(areaHeight));
        int j = areaY.getValue(this.func_215721_a(areaWidth), this.func_215722_b(areaHeight));
        if (j == DRIED_RIVER) {
            return Registry.BIOME.getId(Registry.BIOME.getByValue(i).getRiver());
        } else {
            return i;
        }
    }
}