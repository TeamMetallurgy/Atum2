package com.teammetallurgy.atum.world.gen.layer;

import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.area.IArea;
import net.minecraft.world.gen.layer.traits.IAreaTransformer2;
import net.minecraft.world.gen.layer.traits.IDimOffset0Transformer;

import javax.annotation.Nonnull;

public enum AtumRiverMixLayer implements IAreaTransformer2, IDimOffset0Transformer {
    INSTANCE;

    @Override
    public int apply(@Nonnull INoiseRandom noiseRandom, IArea areaX, IArea areaY, int areaWidth, int areaHeight) {
        int i = areaX.getValue(this.getOffsetX(areaWidth), this.getOffsetZ(areaHeight));
        int j = areaY.getValue(this.getOffsetX(areaWidth), this.getOffsetZ(areaHeight));
        return i;
    }
}