package com.teammetallurgy.atum.world.gen.layer;

import com.teammetallurgy.atum.world.gen.AtumGenSettings;
import net.minecraft.world.WorldType;
import net.minecraft.world.gen.IExtendedNoiseRandom;
import net.minecraft.world.gen.LazyAreaLayerContext;
import net.minecraft.world.gen.area.IArea;
import net.minecraft.world.gen.area.IAreaFactory;
import net.minecraft.world.gen.area.LazyArea;
import net.minecraft.world.gen.layer.*;

import java.util.function.LongFunction;

public class AtumLayerUtil {

    public static <T extends IArea, C extends IExtendedNoiseRandom<T>> IAreaFactory<T> buildAtumLayers(WorldType worldType, AtumGenSettings settings, LongFunction<C> context) {
        IAreaFactory<T> layer = IslandLayer.INSTANCE.apply(context.apply(1L));
        layer = ZoomLayer.FUZZY.apply(context.apply(2000L), layer);
        layer = AddIslandLayer.INSTANCE.apply(context.apply(1L), layer);
        layer = ZoomLayer.NORMAL.apply(context.apply(2001L), layer);
        layer = AddIslandLayer.INSTANCE.apply(context.apply(2L), layer);
        layer = AddIslandLayer.INSTANCE.apply(context.apply(50L), layer);
        layer = AddIslandLayer.INSTANCE.apply(context.apply(70L), layer);
        layer = RemoveTooMuchOceanLayer.INSTANCE.apply(context.apply(2L), layer);
        IAreaFactory<T> oceanLayer = OceanLayer.INSTANCE.apply(context.apply(2L));
        oceanLayer = LayerUtil.repeat(2001L, ZoomLayer.NORMAL, oceanLayer, 6, context);
        layer = AddSnowLayer.INSTANCE.apply(context.apply(2L), layer);
        layer = AddIslandLayer.INSTANCE.apply(context.apply(3L), layer);
        layer = EdgeLayer.CoolWarm.INSTANCE.apply(context.apply(2L), layer);
        layer = EdgeLayer.HeatIce.INSTANCE.apply(context.apply(2L), layer);
        layer = EdgeLayer.Special.INSTANCE.apply(context.apply(3L), layer);
        layer = ZoomLayer.NORMAL.apply(context.apply(2002L), layer);
        layer = ZoomLayer.NORMAL.apply(context.apply(2003L), layer);
        layer = AddIslandLayer.INSTANCE.apply(context.apply(4L), layer);
        layer = AddMushroomIslandLayer.INSTANCE.apply(context.apply(5L), layer);
        layer = DeepOceanLayer.INSTANCE.apply(context.apply(4L), layer);
        layer = LayerUtil.repeat(1000L, ZoomLayer.NORMAL, layer, 0, context);
        int biomeSize = worldType == WorldType.LARGE_BIOMES ? 6 : settings.getBiomeSize();
        int riverSize = settings.getRiverSize();
        IAreaFactory<T> zoomLayer = LayerUtil.repeat(1000L, ZoomLayer.NORMAL, layer, 0, context);
        zoomLayer = StartRiverLayer.INSTANCE.apply(context.apply(100L), zoomLayer);
        IAreaFactory<T> biomeLayer = getBiomeLayer(layer, context);
        IAreaFactory<T> zoomLayerNormal = LayerUtil.repeat(1000L, ZoomLayer.NORMAL, zoomLayer, 2, context);
        biomeLayer = HillsLayer.INSTANCE.apply(context.apply(1000L), biomeLayer, zoomLayerNormal);
        zoomLayer = LayerUtil.repeat(1000L, ZoomLayer.NORMAL, zoomLayer, 2, context);
        zoomLayer = LayerUtil.repeat(1000L, ZoomLayer.NORMAL, zoomLayer, riverSize, context);
        zoomLayer = RiverLayer.INSTANCE.apply(context.apply(1L), zoomLayer);
        zoomLayer = SmoothLayer.INSTANCE.apply(context.apply(1000L), zoomLayer);
        biomeLayer = RareBiomeLayer.INSTANCE.apply(context.apply(1001L), biomeLayer);

        for (int size = 0; size < biomeSize; ++size) {
            biomeLayer = ZoomLayer.NORMAL.apply(context.apply(1000 + size), biomeLayer);
            if (size == 0) {
                biomeLayer = AddIslandLayer.INSTANCE.apply(context.apply(3L), biomeLayer);
            }
            if (size == 1 || biomeSize == 1) {
                biomeLayer = ShoreLayer.INSTANCE.apply(context.apply(1000L), biomeLayer);
            }
        }

        biomeLayer = SmoothLayer.INSTANCE.apply(context.apply(1000L), biomeLayer);
        biomeLayer = MixRiverLayer.INSTANCE.apply(context.apply(100L), biomeLayer, zoomLayer);
        biomeLayer = MixOceansLayer.INSTANCE.apply(context.apply(100L), biomeLayer, oceanLayer);
        return biomeLayer;
    }

    private static <T extends IArea, C extends IExtendedNoiseRandom<T>> IAreaFactory<T> getBiomeLayer(IAreaFactory<T> parentLayer, LongFunction<C> context) {
        parentLayer = new LayerAtumBiome().apply(context.apply(200L), parentLayer);
        parentLayer = LayerUtil.repeat(1000L, ZoomLayer.NORMAL, parentLayer, 2, context);
        parentLayer = EdgeBiomeLayer.INSTANCE.apply(context.apply(1000L), parentLayer);
        return parentLayer;
    }

    public static Layer getNoiseLayer(long seed, WorldType worldType, AtumGenSettings settings) {
        int maxCacheSize = 25;
        IAreaFactory<LazyArea> layer = buildAtumLayers(worldType, settings, (p_227473_2_) -> new LazyAreaLayerContext(maxCacheSize, seed, p_227473_2_));
        return new Layer(layer);
    }
}