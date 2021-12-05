package com.teammetallurgy.atum.world.gen.layer;

import com.teammetallurgy.atum.init.AtumBiomes;
import net.minecraft.core.Registry;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.newbiome.context.BigContext;
import net.minecraft.world.level.newbiome.context.LazyAreaContext;
import net.minecraft.world.level.newbiome.area.Area;
import net.minecraft.world.level.newbiome.area.AreaFactory;
import net.minecraft.world.level.newbiome.area.LazyArea;
import net.minecraft.world.gen.layer.*;

import java.util.function.LongFunction;

import net.minecraft.world.level.newbiome.layer.AddEdgeLayer;
import net.minecraft.world.level.newbiome.layer.BiomeEdgeLayer;
import net.minecraft.world.level.newbiome.layer.IslandLayer;
import net.minecraft.world.level.newbiome.layer.Layer;
import net.minecraft.world.level.newbiome.layer.Layers;
import net.minecraft.world.level.newbiome.layer.RegionHillsLayer;
import net.minecraft.world.level.newbiome.layer.RiverInitLayer;
import net.minecraft.world.level.newbiome.layer.SmoothLayer;
import net.minecraft.world.level.newbiome.layer.ZoomLayer;

public class AtumLayerUtil {
    public static int SAND_PLAINS;
    public static int OASIS;
    public static int DEAD_OASIS;
    public static int DRIED_RIVER_ID;

    public static <T extends Area, C extends BigContext<T>> AreaFactory<T> buildAtumLayers(int biomeSize, int riverSize, LongFunction<C> context, Registry<Biome> biomeRegistry) {
        AreaFactory<T> layer = IslandLayer.INSTANCE.run(context.apply(1L));
        layer = ZoomLayer.FUZZY.run(context.apply(2000L), layer);
        layer = ZoomLayer.NORMAL.run(context.apply(2001L), layer);
        layer = AddEdgeLayer.CoolWarm.INSTANCE.run(context.apply(2L), layer);
        layer = ZoomLayer.NORMAL.run(context.apply(2002L), layer);
        layer = ZoomLayer.NORMAL.run(context.apply(2003L), layer);
        layer = Layers.zoom(1000L, ZoomLayer.NORMAL, layer, 0, context);
        AreaFactory<T> zoomLayer = Layers.zoom(1000L, ZoomLayer.NORMAL, layer, 0, context);
        zoomLayer = RiverInitLayer.INSTANCE.run(context.apply(100L), zoomLayer);
        AreaFactory<T> biomeLayer = getBiomeLayer(layer, context, biomeRegistry);
        AreaFactory<T> zoomLayerNormal = Layers.zoom(1000L, ZoomLayer.NORMAL, zoomLayer, 2, context);
        biomeLayer = RegionHillsLayer.INSTANCE.run(context.apply(1000L), biomeLayer, zoomLayerNormal);
        zoomLayer = Layers.zoom(1000L, ZoomLayer.NORMAL, zoomLayer, 2, context);
        zoomLayer = Layers.zoom(1000L, ZoomLayer.NORMAL, zoomLayer, riverSize, context);
        zoomLayer = AtumRiverLayer.INSTANCE.run(context.apply(1L), zoomLayer);
        zoomLayer = SmoothLayer.INSTANCE.run(context.apply(1000L), zoomLayer);
        int oasisLayerSize = 3;

        for (int size = 0; size < oasisLayerSize; ++size) {
            biomeLayer = OasisLayer.INSTANCE.run(context.apply(1001L + size), biomeLayer);
        }

        for (int size = 0; size < biomeSize; ++size) {
            biomeLayer = ZoomLayer.NORMAL.run(context.apply(1000 + size), biomeLayer);
        }

        biomeLayer = SmoothLayer.INSTANCE.run(context.apply(1000L), biomeLayer);
        biomeLayer = AtumRiverMixLayer.INSTANCE.run(context.apply(100L), biomeLayer, zoomLayer);
        return biomeLayer;
    }

    private static <T extends Area, C extends BigContext<T>> AreaFactory<T> getBiomeLayer(AreaFactory<T> parentLayer, LongFunction<C> context, Registry<Biome> biomeRegistry) {
        parentLayer = new AtumBiomeLayer(biomeRegistry).run(context.apply(200L), parentLayer);
        parentLayer = Layers.zoom(1000L, ZoomLayer.NORMAL, parentLayer, 2, context);
        parentLayer = BiomeEdgeLayer.INSTANCE.run(context.apply(1000L), parentLayer);
        return parentLayer;
    }

    public static Layer getNoiseLayer(long seed, int biomeSize, int riverSize, Registry<Biome> biomeRegistry) {
        setupBiomeIntIDs(biomeRegistry);

        int maxCacheSize = 25;
        AreaFactory<LazyArea> layer = buildAtumLayers(biomeSize, riverSize, (p_227473_2_) -> new LazyAreaContext(maxCacheSize, seed, p_227473_2_), biomeRegistry);
        return new Layer(layer);
    }

    /**
     * Always call this in the Biome Source's constructor so we get the correct int biome id for this world.
     * (this is because the int id for the biome in the dynamic registry can be different in a different world)
     */
    private static void setupBiomeIntIDs(Registry<Biome> biomeRegistry) {
        SAND_PLAINS = biomeRegistry.getId(biomeRegistry.get(AtumBiomes.SAND_PLAINS));
        OASIS = biomeRegistry.getId(biomeRegistry.get(AtumBiomes.OASIS));
        DEAD_OASIS = biomeRegistry.getId(biomeRegistry.get(AtumBiomes.DEAD_OASIS));
        DRIED_RIVER_ID = biomeRegistry.getId(biomeRegistry.get(AtumBiomes.DRIED_RIVER));
    }
}