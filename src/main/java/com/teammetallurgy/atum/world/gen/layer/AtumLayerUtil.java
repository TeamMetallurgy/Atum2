package com.teammetallurgy.atum.world.gen.layer;

import com.teammetallurgy.atum.init.AtumBiomes;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.IExtendedNoiseRandom;
import net.minecraft.world.gen.LazyAreaLayerContext;
import net.minecraft.world.gen.area.IArea;
import net.minecraft.world.gen.area.IAreaFactory;
import net.minecraft.world.gen.area.LazyArea;
import net.minecraft.world.gen.layer.EdgeBiomeLayer;
import net.minecraft.world.gen.layer.EdgeLayer;
import net.minecraft.world.gen.layer.HillsLayer;
import net.minecraft.world.gen.layer.IslandLayer;
import net.minecraft.world.gen.layer.Layer;
import net.minecraft.world.gen.layer.LayerUtil;
import net.minecraft.world.gen.layer.SmoothLayer;
import net.minecraft.world.gen.layer.StartRiverLayer;
import net.minecraft.world.gen.layer.ZoomLayer;

import java.util.function.LongFunction;

public class AtumLayerUtil {
    public static int SAND_PLAINS;
    public static int OASIS;
    public static int DEAD_OASIS;
    public static int DRIED_RIVER_ID;

    public static <T extends IArea, C extends IExtendedNoiseRandom<T>> IAreaFactory<T> buildAtumLayers(int biomeSize, int riverSize, LongFunction<C> context, Registry<Biome> biomeRegistry) {
        IAreaFactory<T> layer = IslandLayer.INSTANCE.apply(context.apply(1L));
        layer = ZoomLayer.FUZZY.apply(context.apply(2000L), layer);
        layer = ZoomLayer.NORMAL.apply(context.apply(2001L), layer);
        layer = EdgeLayer.CoolWarm.INSTANCE.apply(context.apply(2L), layer);
        layer = ZoomLayer.NORMAL.apply(context.apply(2002L), layer);
        layer = ZoomLayer.NORMAL.apply(context.apply(2003L), layer);
        layer = LayerUtil.repeat(1000L, ZoomLayer.NORMAL, layer, 0, context);
        IAreaFactory<T> zoomLayer = LayerUtil.repeat(1000L, ZoomLayer.NORMAL, layer, 0, context);
        zoomLayer = StartRiverLayer.INSTANCE.apply(context.apply(100L), zoomLayer);
        IAreaFactory<T> biomeLayer = getBiomeLayer(layer, context, biomeRegistry);
        IAreaFactory<T> zoomLayerNormal = LayerUtil.repeat(1000L, ZoomLayer.NORMAL, zoomLayer, 2, context);
        biomeLayer = HillsLayer.INSTANCE.apply(context.apply(1000L), biomeLayer, zoomLayerNormal);
        zoomLayer = LayerUtil.repeat(1000L, ZoomLayer.NORMAL, zoomLayer, 2, context);
        zoomLayer = LayerUtil.repeat(1000L, ZoomLayer.NORMAL, zoomLayer, riverSize, context);
        zoomLayer = AtumRiverLayer.INSTANCE.apply(context.apply(1L), zoomLayer);
        zoomLayer = SmoothLayer.INSTANCE.apply(context.apply(1000L), zoomLayer);
        int oasisLayerSize = 3;

        for (int size = 0; size < oasisLayerSize; ++size) {
            biomeLayer = OasisLayer.INSTANCE.apply(context.apply(1001L + size), biomeLayer);
        }

        for (int size = 0; size < biomeSize; ++size) {
            biomeLayer = ZoomLayer.NORMAL.apply(context.apply(1000 + size), biomeLayer);
        }

        biomeLayer = SmoothLayer.INSTANCE.apply(context.apply(1000L), biomeLayer);
        biomeLayer = AtumRiverMixLayer.INSTANCE.apply(context.apply(100L), biomeLayer, zoomLayer);
        return biomeLayer;
    }

    private static <T extends IArea, C extends IExtendedNoiseRandom<T>> IAreaFactory<T> getBiomeLayer(IAreaFactory<T> parentLayer, LongFunction<C> context, Registry<Biome> biomeRegistry) {
        parentLayer = new AtumBiomeLayer(biomeRegistry).apply(context.apply(200L), parentLayer);
        parentLayer = LayerUtil.repeat(1000L, ZoomLayer.NORMAL, parentLayer, 2, context);
        parentLayer = EdgeBiomeLayer.INSTANCE.apply(context.apply(1000L), parentLayer);
        return parentLayer;
    }

    public static Layer getNoiseLayer(long seed, int biomeSize, int riverSize, Registry<Biome> biomeRegistry) {
        setupBiomeIntIDs(biomeRegistry);

        int maxCacheSize = 25;
        IAreaFactory<LazyArea> layer = buildAtumLayers(biomeSize, riverSize, (p_227473_2_) -> new LazyAreaLayerContext(maxCacheSize, seed, p_227473_2_), biomeRegistry);
        return new Layer(layer);
    }

    /**
     * Always call this in the Biome Source's constructor so we get the correct int biome id for this world.
     * (this is because the int id for the biome in the dynamic registry can be different in a different world)
     */
    private static void setupBiomeIntIDs(Registry<Biome> biomeRegistry) {
        SAND_PLAINS = biomeRegistry.getId(biomeRegistry.getValueForKey(AtumBiomes.SAND_PLAINS));
        OASIS = biomeRegistry.getId(biomeRegistry.getValueForKey(AtumBiomes.OASIS));
        DEAD_OASIS = biomeRegistry.getId(biomeRegistry.getValueForKey(AtumBiomes.DEAD_OASIS));
        DRIED_RIVER_ID = biomeRegistry.getId(biomeRegistry.getValueForKey(AtumBiomes.DRIED_RIVER));
    }
}