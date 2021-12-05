package com.teammetallurgy.atum.world.gen.layer;

import net.minecraft.world.level.newbiome.context.Context;
import net.minecraft.world.level.newbiome.area.Area;
import net.minecraft.world.level.newbiome.layer.traits.AreaTransformer2;
import net.minecraft.world.level.newbiome.layer.traits.DimensionOffset0Transformer;

import javax.annotation.Nonnull;

public enum AtumRiverMixLayer implements AreaTransformer2, DimensionOffset0Transformer {
    INSTANCE;

    @Override
    public int applyPixel(@Nonnull Context noiseRandom, Area areaX, Area areaY, int areaWidth, int areaHeight) {
        int i = areaX.get(this.getParentX(areaWidth), this.getParentY(areaHeight));
        int j = areaY.get(this.getParentX(areaWidth), this.getParentY(areaHeight));
        if (j == AtumLayerUtil.DRIED_RIVER_ID) {
            return AtumLayerUtil.DRIED_RIVER_ID;
        }
        return i;
    }
}