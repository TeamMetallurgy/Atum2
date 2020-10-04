package com.teammetallurgy.atum.world.gen.layer;

import net.minecraft.world.gen.IExtendedNoiseRandom;
import net.minecraft.world.gen.LazyAreaLayerContext;
import net.minecraft.world.gen.area.IArea;
import net.minecraft.world.gen.area.IAreaFactory;
import net.minecraft.world.gen.area.LazyArea;
import net.minecraft.world.gen.layer.*;

import java.util.function.LongFunction;

public class AtumLayerUtil {

    public static <T extends IArea, C extends IExtendedNoiseRandom<T>> IAreaFactory<T> buildAtumLayers(int biomeSize, int riverSize, LongFunction<C> context) {
        IAreaFactory<T> layer = IslandLayer.INSTANCE.apply(context.apply(1L));
        layer = ZoomLayer.FUZZY.apply(context.apply(2000L), layer);
        layer = ZoomLayer.NORMAL.apply(context.apply(2001L), layer);
        layer = EdgeLayer.CoolWarm.INSTANCE.apply(context.apply(2L), layer);
        layer = ZoomLayer.NORMAL.apply(context.apply(2002L), layer);
        layer = ZoomLayer.NORMAL.apply(context.apply(2003L), layer);
        layer = LayerUtil.repeat(1000L, ZoomLayer.NORMAL, layer, 0, context);
        IAreaFactory<T> zoomLayer = LayerUtil.repeat(1000L, ZoomLayer.NORMAL, layer, 0, context);
        zoomLayer = StartRiverLayer.INSTANCE.apply(context.apply(100L), zoomLayer);
        IAreaFactory<T> biomeLayer = getBiomeLayer(layer, context);
        IAreaFactory<T> zoomLayerNormal = LayerUtil.repeat(1000L, ZoomLayer.NORMAL, zoomLayer, 2, context);
        biomeLayer = HillsLayer.INSTANCE.apply(context.apply(1000L), biomeLayer, zoomLayerNormal);
        zoomLayer = LayerUtil.repeat(1000L, ZoomLayer.NORMAL, zoomLayer, 2, context);
        zoomLayer = LayerUtil.repeat(1000L, ZoomLayer.NORMAL, zoomLayer, riverSize, context);
        zoomLayer = AtumRiverLayer.INSTANCE.apply(context.apply(1L), zoomLayer);
        zoomLayer = SmoothLayer.INSTANCE.apply(context.apply(1000L), zoomLayer);
        biomeLayer = OasisLayer.INSTANCE.apply(context.apply(1001L), biomeLayer);

        for (int size = 0; size < biomeSize; ++size) {
            biomeLayer = ZoomLayer.NORMAL.apply(context.apply(1000 + size), biomeLayer);
        }

        biomeLayer = SmoothLayer.INSTANCE.apply(context.apply(1000L), biomeLayer);
        biomeLayer = AtumRiverMixLayer.INSTANCE.apply(context.apply(100L), biomeLayer, zoomLayer);
        return biomeLayer;
    }

    private static <T extends IArea, C extends IExtendedNoiseRandom<T>> IAreaFactory<T> getBiomeLayer(IAreaFactory<T> parentLayer, LongFunction<C> context) {
        parentLayer = new AtumBiomeLayer().apply(context.apply(200L), parentLayer);
        parentLayer = LayerUtil.repeat(1000L, ZoomLayer.NORMAL, parentLayer, 2, context);
        parentLayer = EdgeBiomeLayer.INSTANCE.apply(context.apply(1000L), parentLayer);
        return parentLayer;
    }

    public static Layer getNoiseLayer(long seed, int biomeSize, int riverSize) {
        int maxCacheSize = 25;
        IAreaFactory<LazyArea> layer = buildAtumLayers(biomeSize, riverSize, (p_227473_2_) -> new LazyAreaLayerContext(maxCacheSize, seed, p_227473_2_));
        return new Layer(layer);
    }
}